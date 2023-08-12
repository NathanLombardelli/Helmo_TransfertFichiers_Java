package Models.Network.Protocol.Client;

import Models.BaseRegex;
import Models.Network.Protocol.MessageProtocol;

public class ClientFileListMessage extends MessageProtocol {

    private final String protocolRegex = "^FILELIST" + BaseRegex.line + "$";

    public ClientFileListMessage() {
    }

    @Override
    public boolean deserialize(String message) {
        return message.matches(protocolRegex);
    }

    @Override
    public String serialize() {
        return "FILELIST\r\n";
    }

}
