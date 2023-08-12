package Models.Commands;

import App.Program;
import Models.BaseRegex;
import Models.Cryptography.Aes;
import Models.Cryptography.SHA384;
import Models.Network.Protocol.FFE.FFESendFileMessage;
import Models.Network.Protocol.MessageProtocol;
import Models.Network.StorProcessor;
import Models.Task.Task;
import Models.Task.TaskController;
import Models.Task.TaskType;

import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;

public class BackupCommand extends Command {

    private BufferedReader console;
    private Map<String, StorProcessor> processors;
    private Aes aes;
    private TaskController taskController;
    private SHA384 sha384 = new SHA384();

    public BackupCommand(String command, String description, Map<String, StorProcessor> processors, Aes aes, TaskController taskController) {
        super(command, description);
        this.processors = processors;
        this.aes = aes;
        this.taskController = taskController;
        this.console = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void execute() {
        sendBackup();
    }

    private void sendBackup() {
        if (processors.isEmpty()) {
            System.out.println("No SBE is connected");
            return;
        }

        SecretKey key = aes.createKey();
        byte[] iv = aes.createIV();
        System.out.println("Save the aes key and iv");
        System.out.printf("AES Key: %s\n", aes.getKey(key));
        System.out.printf("IV: %s\n", aes.getIv(iv));

        System.out.println("Select a SBE:");
        for (String SBEID : processors.keySet()) {
            System.out.printf("ID: %s\n", SBEID);
        }

        System.out.println("SBE ID:");
        try {
            String sbeid = console.readLine();
            if (processors.containsKey(sbeid)) {

                Path source = Paths.get(Program.configPath);
                Path dest = Paths.get(Program.configPath + ".backup");
                Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);

                File file = new File(Program.configPath + ".backup");

                aes.encrypt(file.getParent(), file.getName(), key, iv);

                MessageProtocol protocol = new FFESendFileMessage(sha384.hash(file.getName()), sha384.getFileChecksum(file.getPath()), file.length());
                taskController.add(new Task(TaskType.BACKUP, file.getName(), protocol, sbeid, dest.toString()));
            }
        } catch (IOException e) {
            System.out.println("[IOE] Impossible to read");
        }
    }
}
