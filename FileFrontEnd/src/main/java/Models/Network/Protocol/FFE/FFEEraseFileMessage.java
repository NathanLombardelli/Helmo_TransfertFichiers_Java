package Models.Network.Protocol.FFE;

import Models.BaseRegex;
import Models.Network.Protocol.MessageProtocol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FFEEraseFileMessage extends MessageProtocol {

    private String hashFileName;

    private final String protocolRegex = "^(ERASEFILE)" + BaseRegex.bl + "(" + BaseRegex.hash_filename + ")" + BaseRegex.line + "$";

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
        return true;
    }

    @Override
    public String serialize() {
        return String.format("ERASEFILE %s\r\n", hashFileName);
    }
}
