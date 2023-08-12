package Models.Task;

import Models.Config;
import Models.Cryptography.Aes;
import Models.Cryptography.SHA384;
import Models.File;
import Models.FileController;
import Models.Network.ClientHandler;
import Models.Network.Protocol.*;
import Models.Network.Protocol.Client.ClientGetFileResultMessage;
import Models.Network.Protocol.Event.*;
import Models.Network.Protocol.FFE.FFEEraseFileMessage;
import Models.Network.Protocol.FFE.FFERemoveFileResultMessage;
import Models.Network.Protocol.FFE.FFESaveFileResultMessage;
import Models.Network.Protocol.FFE.FFESendFileMessage;
import Models.Network.StorProcessor;
import Models.User;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class TaskController implements Runnable, SBESendResultEvent, SBERetrieveResultEvent, ClientSaveFileEvent, ClientRemoveFileEvent, SBEEraseResultEvent {

    private Queue<Task> tasks;
    private Map<String, StorProcessor> processors;
    private Config config;
    private FileController fileController;

    private SHA384 sha384 = new SHA384();
    private Aes aes;

    public TaskController(Queue<Task> tasks, Map<String, StorProcessor> processors, Config config, FileController fileController, Aes aes) {
        this.tasks = tasks;
        this.processors = processors;
        this.config = config;
        this.fileController = fileController;
        this.aes = aes;
    }

    public boolean add(Task task) {
        return tasks.add(task);
    }

    @Override
    public void run() {
        try {
            while (true) {
                execute();
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void execute() {
        if (tasks.isEmpty())
            return;

        // Take the first task (FIFO)
        Task task = tasks.peek();

        // Execute idle task only
        if (task.getStatus() != TaskStatus.IDLE)
            return;

        // Process task ...
        task.setStatus(TaskStatus.PROCESS);

        // Client #TCP (Storbackend)
        StorProcessor client = processors.get(task.getSBEID());
        
        // Send Protocol
        MessageProtocol protocol = task.getProtocol();
        client.sendMessage(protocol);

        try {
            // Send file to Storbackend
            if (task.getType() == TaskType.SENDING) {
                Thread.sleep(2000);
                client.sendFile(task.getFileName(), config.getDownloadFolderPath());
            }
            // Send backup to Storbackend
            if (task.getType() == TaskType.BACKUP) {
                Thread.sleep(2000);
                client.sendFile(task.getBackupFile());
            }
        } catch (InterruptedException | IOException e) {
            System.out.println("[TaskController] Execute Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void onSBESendResultEvent(boolean success) {

        if (tasks.isEmpty())
            return;

        // Take the first task (FIFO)
        Task task = tasks.peek();

        if (success) {
            task.setStatus(TaskStatus.TERMINATED);
        } else {
            task.setStatus(TaskStatus.ERROR);
        }

        // Client #TCP GUI
        ClientHandler client = task.getClient();

        // Send SaveFileResult (SUCCESS|ERROR)
        client.sendMessage(new FFESaveFileResultMessage(success));

        // Task terminated (remove task in queue)
        tasks.poll();
    }

    @Override
    public void onSBERetrieveResultEvent(boolean success, String hashFileName, String hashFileContent, long size) {
        if (tasks.isEmpty())
            return;

        // Take the first task (FIFO)
        Task task = tasks.peek();

        // Client #TCP GUI
        ClientHandler client = task.getClient();
        // Client #TCP (Storbackend)
        StorProcessor clientProcessor = processors.get(task.getSBEID());

        User user = task.getUser();

        try {

            if (success) {
                clientProcessor.readFile(hashFileName, size);

                // Checksum
                String checksum = sha384.getFileChecksum(config.getDownloadFolderPath(), hashFileName);
                if(checksum.equals(hashFileContent)) {
                    // AES Encrypt file TODO MOVE INTO SENDFILE & RECEIVEFILE
                    File file = fileController.getFile(user.getLogin(), task.getFileName());

                    if (file != null) {
                        aes.decrypt(config.getDownloadFolderPath(), hashFileName, aes.getKey(user.getAesKey()), aes.getIv(file.getIv()));
                        client.sendMessage(new ClientGetFileResultMessage(task.getFileName(), file.getFileSize()));
                        Thread.sleep(2000);
                        client.sendFile(hashFileName, config.getDownloadFolderPath());
                        task.setStatus(TaskStatus.TERMINATED);
                    }
                } else {
                    // Send ClientGetFileResult ERROR
                    client.sendMessage(new ClientGetFileResultMessage(false));
                    task.setStatus(TaskStatus.ERROR);
                }
            } else {
                // Send ClientGetFileResult ERROR
                client.sendMessage(new ClientGetFileResultMessage(false));
                task.setStatus(TaskStatus.ERROR);
            }

            // Task terminated (remove task in queue)
            tasks.poll();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClientSaveFileEvent(ClientHandler client, String filename, long filesize) {
        try {
            // Check if user is log on
            User user = client.getConnectedUser();

            if (user == null)
                return;

            // Receive file from Client GUI
            boolean success = client.readFile(filename, filesize);

            if (success) {
                // Create IV for the file
                String IV = aes.getIv(aes.createIV());

                // TODO MOVE AES ENCRYPT
                aes.encrypt(config.getDownloadFolderPath(), filename, aes.getKey(user.getAesKey()), aes.getIv(IV));

                for (String SBEID : processors.keySet()) {
                    // Create File
                    File file = new File(filename, filesize, IV);
                    file.addStorageProvider(SBEID);
                    // Save file into FFEConfigExample.json (current user)
                    fileController.addFileConfig(user.getLogin(), file);

                    MessageProtocol protocol = new FFESendFileMessage(sha384.hash(filename), sha384.getFileChecksum(config.getDownloadFolderPath(), filename), filesize);
                    add(new Task(TaskType.SENDING, filename, protocol, user, SBEID, client));
                    break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClientRemoveFileEvent(ClientHandler client, String filename) {

        // Check if user is log on
        User user = client.getConnectedUser();

        if (user == null)
            return;

        File file = fileController.getFile(user.getLogin(), filename);

        if (file == null)
            return;

        String SBEID = "";

        for (String sbeid : file.getStorageProviders()) {
            SBEID = sbeid;
            break;
        }

        MessageProtocol protocol = new FFEEraseFileMessage(sha384.hash(filename));
        add(new Task(TaskType.DELETING, filename, protocol, user, SBEID, client));
    }

    @Override
    public void onSBEEraseResultEvent(boolean success) {
        if (tasks.isEmpty())
            return;

        // Take the first task (FIFO)
        Task task = tasks.peek();

        // Client #TCP GUI
        ClientHandler client = task.getClient();

        User user = task.getUser();

        // Remove file in FFEConfigExample.json
        fileController.removeFileConfig(user.getLogin(), task.getFileName());

        // Send FFERemoveFileResultMessage (SUCCESS|ERROR)
        client.sendMessage(new FFERemoveFileResultMessage(success));

        if (success) {
            task.setStatus(TaskStatus.TERMINATED);
        } else {
            task.setStatus(TaskStatus.ERROR);
        }

        // Task terminated (remove task in queue)
        tasks.poll();
    }
}
