package Models.Network.Protocol.FFE;

import Models.BaseRegex;
import Models.Network.Protocol.MessageProtocol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FFESaveFileResultMessage extends MessageProtocol {

    private boolean success;
    private final String protocolRegex = "^(SAVEFILE_OK|SAVEFILE_ERROR)" + BaseRegex.line +"$";

    public FFESaveFileResultMessage(boolean success) {
        this.success = success;
    }

    @Override
    public boolean deserialize(String message) {

        if (!message.matches(protocolRegex))
            return false;

        Pattern pattern = Pattern.compile(protocolRegex);
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            String status = matcher.group(1);
            success = "SAVEFILE_OK".equals(status);
        }

        return true;
    }

    @Override
    public String serialize() {
        return success ? "SAVEFILE_OK\r\n" : "SAVEFILE_ERROR\r\n";
    }
}
