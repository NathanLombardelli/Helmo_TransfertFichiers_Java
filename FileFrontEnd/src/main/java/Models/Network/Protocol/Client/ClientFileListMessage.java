package Models.Network.Protocol.Client;

import Models.BaseRegex;
import Models.File;
import Models.FileController;
import Models.Network.Protocol.Event.ClientFileListEvent;
import Models.Network.Protocol.Event.ClientRemoveFileEvent;
import Models.Network.Protocol.FFE.FFEFileListResultMessage;
import Models.Network.Protocol.MessageProtocol;

import java.util.ArrayList;
import java.util.List;

public class ClientFileListMessage extends MessageProtocol {

    private final String protocolRegex = "^FILELIST" + BaseRegex.line + "$";

    private List<ClientFileListEvent> listeners = new ArrayList<>();

    public ClientFileListMessage() {
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
        return "FILELIST\r\n";
    }

    public void subscribe(ClientFileListEvent listener) {
        listeners.add(listener);
    }

    public void unsubscribe(ClientFileListEvent listener) {
        listeners.remove(listener);
    }

    private void fire() {
        for (ClientFileListEvent listener : listeners) {
            listener.onClientFileListEvent(client);
        }
    }
}
