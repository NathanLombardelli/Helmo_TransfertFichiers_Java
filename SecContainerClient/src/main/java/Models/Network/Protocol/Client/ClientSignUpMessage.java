package Models.Network.Protocol.Client;

import Models.BaseRegex;
import Models.Network.Protocol.MessageProtocol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientSignUpMessage extends MessageProtocol {

    private String login;
    private String password;

    private final String protocolRegex = "^SIGNUP" + BaseRegex.bl + BaseRegex.login + BaseRegex.bl + BaseRegex.password + BaseRegex.line + "$";

    public ClientSignUpMessage(String login, String password) {
        this.login = login;
        this.password = password;
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
        }

        return true;
    }

    @Override
    public String serialize() {
        return String.format("SIGNUP %s %s\r\n", this.login, this.password);
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

}
