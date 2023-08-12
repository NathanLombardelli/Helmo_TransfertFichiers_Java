package Models.Network.Protocol.SBE;

import Models.BaseRegex;
import Models.Network.Protocol.MessageProtocol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SBESendResultMessage extends MessageProtocol {

    private boolean success;
    private final String protocolRegex = "^(SEND_OK|SEND_ERROR)" + BaseRegex.line + "$";

    public SBESendResultMessage(boolean success) {
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
            success = status.equals("SEND_OK");
        }
        return true;
    }

    @Override
    public String serialize() {
        return success ? "SEND_OK\r\n" : "SEND_ERROR\r\n";
    }
}
