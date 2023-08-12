package Models.Network.Protocol.Client;

import Models.*;
import Models.Cryptography.SHA384;
import Models.Network.Protocol.Event.ClientGetFileEvent;
import Models.Network.Protocol.Event.ClientRemoveFileEvent;
import Models.Network.Protocol.FFE.FFERetrieveFileMessage;
import Models.Network.Protocol.MessageProtocol;
import Models.Task.Task;
import Models.Task.TaskController;
import Models.Task.TaskType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientGetFileMessage extends MessageProtocol {

    private String filename;
    private final String protocolRegex = "^GETFILE" + BaseRegex.bl + "(" + BaseRegex.filename + ")" + BaseRegex.line + "$";

    private List<ClientGetFileEvent> listeners = new ArrayList<>();

    public ClientGetFileMessage() {
    }

    public ClientGetFileMessage(String filename) {
        this.filename = filename;
    }

    @Override
    public boolean deserialize(String message) {
        if (!message.matches(protocolRegex))
            return false;

        Pattern pattern = Pattern.compile(protocolRegex);
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            filename = matcher.group(1);
        }
        fire();
        return true;
    }

    @Override
    public String serialize() {
        return String.format("GETFILE %s\r\n", filename);
    }

    public void subscribe(ClientGetFileEvent listener) {
        listeners.add(listener);
    }

    public void unsubscribe(ClientGetFileEvent listener) {
        listeners.remove(listener);
    }

    private void fire() {
        for (ClientGetFileEvent listener : listeners) {
            listener.onClientGetFileEvent(client, filename);
        }
    }
}
