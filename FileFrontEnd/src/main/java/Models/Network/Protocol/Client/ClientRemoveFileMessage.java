package Models.Network.Protocol.Client;

import Models.BaseRegex;
import Models.Network.Protocol.Event.ClientRemoveFileEvent;
import Models.Network.Protocol.MessageProtocol;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientRemoveFileMessage extends MessageProtocol {

    private String fileName;

    private final String protocolRegex = "^(REMOVEFILE)" + BaseRegex.bl + "(" + BaseRegex.filename + ")" + BaseRegex.line + "$";

    private List<ClientRemoveFileEvent> listeners = new ArrayList<>();

    public ClientRemoveFileMessage() {
    }

    @Override
    public boolean deserialize(String message) {
        if (!message.matches(protocolRegex))
            return false;

        Pattern pattern = Pattern.compile(protocolRegex);
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            fileName = matcher.group(2);
        }
        fire();
        return true;
    }


    @Override
    public String serialize() {
        return String.format("REMOVEFILE %s\r\n", fileName);
    }

    public void subscribe(ClientRemoveFileEvent listener) {
        listeners.add(listener);
    }

    public void unsubscribe(ClientRemoveFileEvent listener) {
        listeners.remove(listener);
    }

    private void fire() {
        for (ClientRemoveFileEvent listener : listeners) {
            listener.onClientRemoveFileEvent(client, fileName);
        }
    }
}
