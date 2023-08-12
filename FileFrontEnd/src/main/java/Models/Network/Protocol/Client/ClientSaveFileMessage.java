package Models.Network.Protocol.Client;

import Models.*;
import Models.Network.Protocol.Event.ClientSaveFileEvent;
import Models.Network.Protocol.MessageProtocol;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientSaveFileMessage extends MessageProtocol {

    private String filename;
    private long fileSize;

    private final String protocolRegex = "^SAVEFILE" + BaseRegex.bl + "(" + BaseRegex.filename + ")" + BaseRegex.bl + "(" + BaseRegex.size + ")" + BaseRegex.line + "$";

    private List<ClientSaveFileEvent> listeners = new ArrayList<>();

    public ClientSaveFileMessage() {
    }

    public ClientSaveFileMessage(String filename, long fileSize) {
        this.filename = filename;
        this.fileSize = fileSize;
    }

    @Override
    public boolean deserialize(String message) {

        if (!message.matches(protocolRegex))
            return false;

        Pattern pattern = Pattern.compile(protocolRegex);
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            filename = matcher.group(1);
            fileSize = Long.parseLong(matcher.group(2));
            fire();
        }
        return true;
    }

    @Override
    public String serialize() {
        return String.format("SAVEFILE %s %d\r\n", filename, fileSize);
    }

    public void subscribe(ClientSaveFileEvent listener) {
        listeners.add(listener);
    }

    public void unsubscribe(ClientSaveFileEvent listener) {
        listeners.remove(listener);
    }

    private void fire() {
        for (ClientSaveFileEvent listener : listeners) {
            listener.onClientSaveFileEvent(client, filename, fileSize);
        }
    }
}
