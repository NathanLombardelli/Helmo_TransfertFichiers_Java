package Models.Commands;

import Models.Config;
import Models.Network.*;
import Models.Network.Protocol.ProtocolParser;

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
        MulticastPublisher multicastPublisher = new MulticastPublisher(config.getMulticastAddress(), config.getMulticastPort(), config.getNetworkInterface());
        MulticastHello mHello = new MulticastHello(multicastPublisher, config.getMulticastDelayInSeconds(), config.getUniqueID(), config.getUnicastPort());
        new Thread(mHello).start();

        ServerTCP server = new ServerTCP(config.getUnicastPort(), parser, fileReceiver);
        new Thread(server).start();
    }
}
