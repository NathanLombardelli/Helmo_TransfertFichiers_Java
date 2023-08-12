package Models.Network.Protocol.Client;

import Models.BaseRegex;
import Models.Network.Protocol.Event.ClientRemoveFileEvent;
import Models.Network.Protocol.Event.ClientSignOutEvent;
import Models.Network.Protocol.MessageProtocol;

import java.util.ArrayList;
import java.util.List;

public class ClientSignOutMessage extends MessageProtocol {

    private final String protocolRegex = "^(SIGNOUT)" + BaseRegex.line + "$";

    private List<ClientSignOutEvent> listeners = new ArrayList<>();

    public ClientSignOutMessage() {

    }

    @Override
    public boolean deserialize(String message) {
        if (!message.matches(protocolRegex))
            return false;
        fire();
        return true;
    }

    @Override
    public String serialize() {
       return "SIGNOUT\r\n";
    }

    public void subscribe(ClientSignOutEvent listener) {
        listeners.add(listener);
    }

    public void unsubscribe(ClientSignOutEvent listener) {
        listeners.remove(listener);
    }

    private void fire() {
        for (ClientSignOutEvent listener : listeners) {
            listener.onClientSignOutEvent(client);
        }
    }
}
