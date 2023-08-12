package Models.Network.Protocol.Client;

import Models.BaseRegex;
import Models.Network.Protocol.MessageProtocol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientRemoveFileMessage extends MessageProtocol {

    private String fileName;

    private final String protocolRegex = "^(REMOVEFILE)" + BaseRegex.bl + "(" + BaseRegex.filename + ")" + BaseRegex.line + "$";

    public ClientRemoveFileMessage() {
    }

    public ClientRemoveFileMessage(String fileName) {
        this.fileName = fileName;
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
        return true;
    }


    @Override
    public String serialize() {
        return String.format("REMOVEFILE %s\r\n", fileName);
    }

}
