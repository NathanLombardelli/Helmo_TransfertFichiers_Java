package App;

import Models.Commands.*;
import Models.Config;
import Models.Cryptography.Aes;
import Models.FileController;
import Models.IO.Interfaces.IConfigRepository;
import Models.IO.Json.JsonConfigRepository;
import Models.Network.FileReceiver;
import Models.Network.Protocol.*;
import Models.Network.Protocol.Client.*;
import Models.Network.Protocol.FFE.FFEFileListResultMessage;
import Models.Network.Protocol.FFE.FFESendFileMessage;
import Models.Network.Protocol.SBE.SBEEraseResultMessage;
import Models.Network.Protocol.SBE.SBEHelloMessage;
import Models.Network.Protocol.SBE.SBERetrieveResultMessage;
import Models.Network.Protocol.SBE.SBESendResultMessage;
import Models.Network.StorProcessor;
import Models.Task.Task;
import Models.Task.TaskController;
import Models.UserController;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Program {

    public static String configPath = "src/main/resources/FFEConfigExample.json";

    public static void main(String[] args) {

        // Allow you to get the config path from application arguments (first argument)
        if (args.length > 0 && new File(args[0]).exists()) {
            configPath = args[0];
            System.out.println("The configuration path has been changed");
        }
        init();
    }

    private static void init() {
        Config config = new Config();

        // Repository
        IConfigRepository configRepository = new JsonConfigRepository();
        if (configRepository.load(configPath, config)) {
            System.out.println("A configuration has been loaded from a file");
        }

        // region Collections
        Queue<Task> tasks = new LinkedList<>();
        Map<String, StorProcessor> processors = new HashMap<>();
        Map<String, Command> commands = new HashMap();
        Map<String, MessageProtocol> protocols = new HashMap<>();
        // endregion

        // AES
        Aes aes = new Aes(256, 12, 16);

        // Controller
        FileController fileController = new FileController(configRepository, config, tasks);
        UserController userController = new UserController(configRepository, config, aes, fileController);

        // FileReceiver
        FileReceiver fileReceiver = new FileReceiver(config.getDownloadFolderPath());

        // TASKS
        TaskController taskController = new TaskController(tasks, processors, config, fileController, aes);
        new Thread(taskController).start();

        // region PROTOCOLS
        ProtocolParser parser = new ProtocolParser(protocols);
        protocols.put("HELLO", new SBEHelloMessage(processors, parser, fileReceiver));

        // ClientSet2FAEvent
        ClientSet2FAMessage ClientSet2FAMessage = new ClientSet2FAMessage();
        ClientSet2FAMessage.subscribe(userController);
        protocols.put("SET2FA", ClientSet2FAMessage);

        // ClientSignInWith2FAEvent
        ClientSignInWith2FAMessage ClientSignInWith2FAMessage = new ClientSignInWith2FAMessage();
        ClientSignInWith2FAMessage.subscribe(userController);
        protocols.put("SIGNINWITH2FA", ClientSignInWith2FAMessage);

        // ClientSignInEvent
        ClientSignInMessage ClientSignInMessage = new ClientSignInMessage();
        ClientSignInMessage.subscribe(userController);
        protocols.put("SIGNIN", ClientSignInMessage);

        // ClientSignUpEvent
        ClientSignUpMessage ClientSignUpMessage = new ClientSignUpMessage();
        ClientSignUpMessage.subscribe(userController);
        protocols.put("SIGNUP", ClientSignUpMessage);

        // ClientSignOutEvent
        ClientSignOutMessage ClientSignOutMessage = new ClientSignOutMessage();
        ClientSignOutMessage.subscribe(userController);
        protocols.put("SIGNOUT", ClientSignOutMessage);

        // ClientFileListEvent
        ClientFileListMessage ClientFileListMessage = new ClientFileListMessage();
        ClientFileListMessage.subscribe(userController);
        protocols.put("FILELIST", ClientFileListMessage);

        // ClientGetFileEvent
        ClientGetFileMessage ClientGetFileMessage = new ClientGetFileMessage();
        ClientGetFileMessage.subscribe(fileController);
        protocols.put("GETFILE", ClientGetFileMessage);

        // SBESendResultEvent
        SBESendResultMessage SBESendResultMessage = new SBESendResultMessage();
        SBESendResultMessage.subscribe(taskController);
        protocols.put("SEND_OK", SBESendResultMessage);
        protocols.put("SEND_ERROR", SBESendResultMessage);

        // SBERetrieveResultEvent
        SBERetrieveResultMessage SBERetrieveResultMessage = new SBERetrieveResultMessage();
        SBERetrieveResultMessage.subscribe(taskController);
        protocols.put("RETRIEVE_OK", SBERetrieveResultMessage);
        protocols.put("RETRIEVE_ERROR", SBERetrieveResultMessage);

        // ClientSaveFileMessageEvent
        ClientSaveFileMessage ClientSaveFileMessage = new ClientSaveFileMessage();
        ClientSaveFileMessage.subscribe(taskController);
        protocols.put("SAVEFILE", ClientSaveFileMessage);

        // SBEEraseResultMessageEvent
        SBEEraseResultMessage SBEEraseResultMessage = new SBEEraseResultMessage();
        SBEEraseResultMessage.subscribe(taskController);
        protocols.put("ERASE_OK", SBEEraseResultMessage);
        protocols.put("ERASE_ERROR", SBEEraseResultMessage);

        // ClientRemoveFileMessageEvent
        ClientRemoveFileMessage ClientRemoveFileMessage = new ClientRemoveFileMessage();
        ClientRemoveFileMessage.subscribe(taskController);
        protocols.put("REMOVEFILE", ClientRemoveFileMessage);

        //endregion

        // region COMMANDS & COMMAND CONTROLLER
        commands.put("help", new HelpCommand("help", "Show all available commands", commands));
        commands.put("load", new LoadConfigCommand("load", "Allows you to load the configuration file", configRepository, config));
        commands.put("save", new SaveConfigCommand("save", "Allows you to save the current configuration", configRepository, config));
        commands.put("modify", new ModifyConfigCommand("modify", "Allows you to modify the current configuration", config));
        commands.put("start", new StartServerCommand("start", "Start the StorBackEnd Server (Multicast + Unicast)", config, parser, fileReceiver));
        commands.put("backup", new BackupCommand("backup", "Backup the configuration file to SBE", processors, aes, taskController));
        commands.put("exit", new ExitCommand("exit", "Exit the program"));
        CommandController commandController = new CommandController(commands);
        commandController.loop();
        // endregion
    }

}
