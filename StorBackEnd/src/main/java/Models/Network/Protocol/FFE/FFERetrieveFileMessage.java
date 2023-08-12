package Models.Network.Protocol.FFE;

import Models.BaseRegex;
import Models.Network.Protocol.Event.FFERetrieveFileEvent;
import Models.Network.Protocol.Event.FFESendFileEvent;
import Models.Network.Protocol.MessageProtocol;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FFERetrieveFileMessage extends MessageProtocol {

    private String hashFileName;
    private final String protocolRegex = "^RETRIEVEFILE" + BaseRegex.bl + "(" + BaseRegex.hash_filename + ")" + BaseRegex.line + "$";

    private List<FFERetrieveFileEvent> listeners = new ArrayList<>();

    public FFERetrieveFileMessage() {
    }

    public FFERetrieveFileMessage(String hashFileName) {
        this.hashFileName = hashFileName;
    }

    @Override
    public boolean deserialize(String message) {

        if (!message.matches(protocolRegex))
            return false;

        Pattern pattern = Pattern.compile(protocolRegex);
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            hashFileName = matcher.group(1);
        }
        fire();
        return true;
    }

    @Override
    public String serialize() {
        return String.format("RETRIEVEFILE %s\r\n", hashFileName);
    }

    public void subscribe(FFERetrieveFileEvent listener) {
        listeners.add(listener);
    }

    public void unsubscribe(FFERetrieveFileEvent listener) {
        listeners.remove(listener);
    }

    private void fire() {
        for (FFERetrieveFileEvent listener : listeners) {
            listener.onFFERetrieveFileEvent(client, hashFileName);
        }
    }
}
