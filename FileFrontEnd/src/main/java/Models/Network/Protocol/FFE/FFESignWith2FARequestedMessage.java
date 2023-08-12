package Models.Network.Protocol.FFE;

import Models.BaseRegex;
import Models.Network.Protocol.MessageProtocol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FFESignWith2FARequestedMessage extends MessageProtocol {

    private final String protocolRegex = "^(SIGN_2FA_NEEDED)" + BaseRegex.line +"$";

    public FFESignWith2FARequestedMessage() {
    }

    @Override
    public boolean deserialize(String message) {
        return message.matches(protocolRegex);
    }

    @Override
    public String serialize() {
        return "SIGN_2FA_NEEDED\r\n";
    }
}

