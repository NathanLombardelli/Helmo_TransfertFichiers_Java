package Models.Network.Protocol.FFE;

import Models.BaseRegex;
import Models.Network.Protocol.MessageProtocol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FFESet2FAResultMessage extends MessageProtocol {

    private boolean success;
    private String key2FA;

    private final String protocolRegexERROR = "^SET2FA_ERROR" + BaseRegex.line + "$";
    private final String protocolRegexSuccess = "^SET2FA_OK" + BaseRegex.bl + BaseRegex.key2FA + BaseRegex.line + "$";

    public FFESet2FAResultMessage(boolean success, String key2FA) {
        this.success = success;
        this.key2FA = key2FA;
    }

    @Override
    public boolean deserialize(String message) {
        if (!message.matches(protocolRegexERROR) && !message.matches(protocolRegexSuccess))
            return false;

        if (message.matches(protocolRegexERROR)) {
            success = false;
            return true;
        }

        if (message.matches(protocolRegexSuccess)) {
            success = true;
            Pattern pattern = Pattern.compile(protocolRegexSuccess);
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                this.key2FA = matcher.group(1);
            }
        }
        return true;
    }

    @Override
    public String serialize() {
        return success ? "SET2FA_OK " + key2FA + "\r\n" : "SET2FA_ERROR\r\n";
    }
}
