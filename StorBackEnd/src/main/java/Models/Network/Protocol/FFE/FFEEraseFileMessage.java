package Models.Network.Protocol.FFE;

import Models.BaseRegex;
import Models.Network.Protocol.Event.FFEEraseFileMessageEvent;
import Models.Network.Protocol.MessageProtocol;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FFEEraseFileMessage extends MessageProtocol {

    private String hashFileName;

    private final String protocolRegex = "^(ERASEFILE)" + BaseRegex.bl + "(" + BaseRegex.hash_filename + ")" + BaseRegex.line + "$";

    private List<FFEEraseFileMessageEvent> listeners = new ArrayList<>();

    public FFEEraseFileMessage() {
    }

    public FFEEraseFileMessage(String hashFileName) {
        this.hashFileName = hashFileName;
    }

    @Override
    public boolean deserialize(String message) {
        if (!message.matches(protocolRegex))
            return false;

        Pattern pattern = Pattern.compile(protocolRegex);
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            hashFileName = matcher.group(2);
        }
        fire();
        return true;
    }

    @Override
    public String serialize() {
        return String.format("ERASEFILE %s\r\n", hashFileName);
    }

    public void subscribe(FFEEraseFileMessageEvent listener) {
        listeners.add(listener);
    }

    public void unsubscribe(FFEEraseFileMessageEvent listener) {
        listeners.remove(listener);
    }

    private void fire() {
        for (FFEEraseFileMessageEvent listener : listeners) {
            listener.onFFEEraseFileMessage(client, hashFileName);
        }
    }
}
