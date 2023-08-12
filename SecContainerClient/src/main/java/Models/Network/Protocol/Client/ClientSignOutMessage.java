package Models.Network.Protocol.Client;

import Models.BaseRegex;
import Models.Network.Protocol.MessageProtocol;

public class ClientSignOutMessage extends MessageProtocol {

    private final String protocolRegex = "^(SIGNOUT)" + BaseRegex.line + "$";

    public ClientSignOutMessage() {

    }

    @Override
    public boolean deserialize(String message) {
        return message.matches(protocolRegex);
    }

    @Override
    public String serialize() {
       return "SIGNOUT\r\n";
    }
}
