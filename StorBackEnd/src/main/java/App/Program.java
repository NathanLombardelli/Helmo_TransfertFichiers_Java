package App;

import Models.Commands.*;
import Models.Config;
import Models.FileManager;
import Models.IO.Interfaces.IConfigRepository;
import Models.IO.Json.JsonConfigRepository;
import Models.Network.FileReceiver;
import Models.Network.Protocol.FFE.FFEEraseFileMessage;
import Models.Network.Protocol.FFE.FFERetrieveFileMessage;
import Models.Network.Protocol.FFE.FFESendFileMessage;
import Models.Network.Protocol.MessageProtocol;
import Models.Network.Protocol.ProtocolParser;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Program {

    public static String configPath = "src/main/resources/SBEConfigExample.json";

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

        // FileManager
        FileManager fileManager = new FileManager(config);

        // File Receiver
        FileReceiver fileReceiver = new FileReceiver(config.getDownLoadPath());

        // region Collections
        Map<String, MessageProtocol> protocols = new HashMap<>();
        Map<String, Command> commands = new HashMap();
        // endregion

        // region PROTOCOLS
        // FFESendFileEvent
        FFESendFileMessage FFESendFileMessage = new FFESendFileMessage();
        FFESendFileMessage.subscribe(fileManager);
        protocols.put("SENDFILE", FFESendFileMessage);

        // FFERetrieveFileEvent
        FFERetrieveFileMessage FFERetrieveFileMessage = new FFERetrieveFileMessage();
        FFERetrieveFileMessage.subscribe(fileManager);
        protocols.put("RETRIEVEFILE", FFERetrieveFileMessage);

        // FFERetrieveFileEvent
        FFEEraseFileMessage FFEEraseFileMessage = new FFEEraseFileMessage();
        FFEEraseFileMessage.subscribe(fileManager);
        protocols.put("ERASEFILE", FFEEraseFileMessage);

        ProtocolParser parser = new ProtocolParser(protocols);
        // endregion

        // region COMMANDS & COMMAND CONTROLLER
        commands.put("help", new HelpCommand("help", "Show all available commands", commands));
        commands.put("load", new LoadConfigCommand("load", "Allows you to load the configuration file", configRepository, config));
        commands.put("save", new SaveConfigCommand("save", "Allows you to save the current configuration", configRepository, config));
        commands.put("modify", new ModifyConfigCommand("modify", "Allows you to modify the current configuration", config));
        commands.put("start", new StartServerCommand("start", "Start the StorBackEnd Server (Multicast + Unicast)", config, parser, fileReceiver));
        commands.put("exit", new ExitCommand("exit", "Exit the program"));
        CommandController commandController = new CommandController(commands);
        commandController.loop();
        // endregion
    }
}
