package Models.Network.Protocol.Client;

import Models.BaseRegex;
import Models.Network.Protocol.MessageProtocol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientGetFileMessage extends MessageProtocol {

    private String filename;

    private final String protocolRegex = "^GETFILE" + BaseRegex.bl + "(" + BaseRegex.filename + ")" + BaseRegex.line + "$";

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
        return true;
    }

    @Override
    public String serialize() {
        return String.format("GETFILE %s\r\n", filename);
    }
}
