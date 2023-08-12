package Models.Network.Protocol.FFE;

import Models.BaseRegex;
import Models.Network.Protocol.MessageProtocol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FFERemoveFileResultMessage extends MessageProtocol {

    private boolean success;
    private final String protocolRegex = "^(ERASE_OK|ERASE_ERROR)" + BaseRegex.line + "$";


    public FFERemoveFileResultMessage() {

    }

    public FFERemoveFileResultMessage(Boolean succes) {
        this.success = succes;
    }

    @Override
    public boolean deserialize(String message) {
        if (!message.matches(protocolRegex))
            return false;


        Pattern pattern = Pattern.compile(protocolRegex);
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            String status = matcher.group(1);
            success = "ERASE_OK".equals(status);
        }

        return true;
    }

    @Override
    public String serialize() {
        return success ? "ERASE_OK\r\n" : "ERASE_ERROR\r\n";
    }
}
