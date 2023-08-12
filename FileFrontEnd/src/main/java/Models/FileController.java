package Models;

import App.Program;
import Models.Cryptography.SHA384;
import Models.IO.Interfaces.IConfigRepository;
import Models.Network.ClientHandler;
import Models.Network.Protocol.Event.ClientGetFileEvent;
import Models.Network.Protocol.FFE.FFERetrieveFileMessage;
import Models.Network.Protocol.MessageProtocol;
import Models.Task.Task;
import Models.Task.TaskController;
import Models.Task.TaskType;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class FileController implements ClientGetFileEvent {

    private Queue<Task> tasks;
    private IConfigRepository repository;
    private Config config;
    private SHA384 sha384 = new SHA384();

    public FileController(IConfigRepository repository, Config config, Queue<Task> tasks) {
        this.repository = repository;
        this.config = config;
        this.tasks = tasks;
    }

    public List<File> getFiles(String login) {
        if (!repository.load(Program.configPath, config))
            return null;

        User user = findUserByLogin(login);
        return user != null ? user.getStoredFiles() : new ArrayList<>();
    }

    public File getFile(String login, String filename) {
        List<File> files = getFiles(login);
        for (File file : files) {
            if (filename.equals(file.getFilename())) {
                return file;
            }
        }
        return null;
    }

    public boolean addFileConfig(String login, File file) {
        if (!repository.load(Program.configPath, config))
            return false;

        User user = findUserByLogin(login);
        if (user == null) return false;

        user.addFile(file);
        config.updateUser(user);
        repository.save(Program.configPath, config);

        return true;
    }

    public boolean removeFileConfig(String login, String filename) {
        if (!repository.load(Program.configPath, config))
            return false;

        User user = findUserByLogin(login);
        if (user == null) return false;

        user.removeFile(filename);
        config.updateUser(user);
        repository.save(Program.configPath, config);
        return true;
    }

    private User findUserByLogin(String login) {
        List<User> users = config.getUsers();

        for (User user : users) {
            if (login.equals(user.getLogin())) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void onClientGetFileEvent(ClientHandler client, String filename) {
        if (client == null)
            return;

        User user = client.getConnectedUser();
        if (user == null)
            return;

        try {
            List<File> files = getFiles(user.getLogin());
            File file = getFile(files, filename);
            if (file != null) {
                MessageProtocol protocol = new FFERetrieveFileMessage(sha384.hash(file.getFilename()));

                for (String SBEID : file.getStorageProviders()) {
                    Task task = new Task(TaskType.RECEIVING, filename, protocol, user, SBEID, client);
                    tasks.add(task);
                    break;
                }
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File getFile(List<File> files, String filename) {
        for (File file : files) {
            if (filename.equals(file.getFilename())) {
                return file;
            }
        }
        return null;
    }
}
