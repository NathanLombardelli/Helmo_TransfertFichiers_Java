package Models.Network.Protocol.FFE;

import Models.BaseRegex;
import Models.Network.Protocol.Event.FFESendFileEvent;
import Models.Network.Protocol.MessageProtocol;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FFESendFileMessage extends MessageProtocol {

    private String hashFileName;
    private String hashFileContent;
    private long size;

    private final String protocolRegex = "^SENDFILE" + BaseRegex.bl  + BaseRegex.file_info + BaseRegex.line + "$";

    private List<FFESendFileEvent> listeners = new ArrayList<>();

    public FFESendFileMessage() {
    }

    public FFESendFileMessage(String hashFileName, String hashFileContent, long size) {
        this.hashFileName = hashFileName;
        this.hashFileContent = hashFileContent;
        this.size = size;
    }

    @Override
    public boolean deserialize(String message) {
        if (!message.matches(protocolRegex))
            return false;

        Pattern pattern = Pattern.compile(protocolRegex);
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            hashFileName = matcher.group(1);
            size = Integer.parseInt(matcher.group(2));
            hashFileContent = matcher.group(3);
        }
        fire();

        return true;
    }

    @Override
    public String serialize() {
        return String.format("SENDFILE %s %d %s\r\n", hashFileName, size, hashFileContent);
    }

    public void subscribe(FFESendFileEvent listener) {
        listeners.add(listener);
    }

    public void unsubscribe(FFESendFileEvent listener) {
        listeners.remove(listener);
    }

    private void fire() {
        for (FFESendFileEvent listener : listeners) {
            listener.onFFESendFileEvent(client, hashFileName, hashFileContent, size);
        }
    }

}
