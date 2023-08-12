package Models.Commands;

import Models.Config;
import Models.Network.FileReceiver;
import Models.Network.MulticastReceiver;
import Models.Network.Protocol.ProtocolParser;
import Models.Network.ServerTCP;

public class StartServerCommand extends Command {

    private Config config;
    private ProtocolParser parser;
    private FileReceiver fileReceiver;

    public StartServerCommand(String command, String description, Config config, ProtocolParser parser, FileReceiver fileReceiver) {
        super(command, description);
        this.config = config;
        this.parser = parser;
        this.fileReceiver = fileReceiver;
    }

    @Override
    public void execute() {
        MulticastReceiver multicastReceiver = new MulticastReceiver(config.getMulticastAddress(), config.getMulticastPort(), config.getNetworkInterface(), parser);
        new Thread(multicastReceiver).start();

        ServerTCP server = new ServerTCP(config.getUnicastPort(), config.isTls(), parser, fileReceiver);
        new Thread(server).start();
    }
}
