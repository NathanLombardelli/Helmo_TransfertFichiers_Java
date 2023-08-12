package Models.Network.Protocol.Client;

import Models.BaseRegex;
import Models.Network.Protocol.MessageProtocol;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientSignInWith2FAMessage extends MessageProtocol {

    private String login;
    private String password;
    private String digits; // 6 digits pour la verification 2FA

    private final String protocolRegex = "^SIGNINWITH2FA" + BaseRegex.bl + BaseRegex.login + BaseRegex.bl + BaseRegex.password + BaseRegex.bl + BaseRegex.digits2FA + BaseRegex.line + "$";

    public ClientSignInWith2FAMessage(String login, String password, String digits) {
        this.login = login;
        this.password = password;
        this.digits = digits;
    }

    @Override
    public boolean deserialize(String message) {
        if (!message.matches(protocolRegex))
            return false;

        Pattern pattern = Pattern.compile(protocolRegex);
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            login = matcher.group(1);
            password = matcher.group(2);
            digits = matcher.group(3);
        }
        return true;
    }


    @Override
    public String serialize() {
        return String.format("SIGNINWITH2FA %s %s %s\r\n", login, password, digits);
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getDigits() {
        return digits;
    }

}
