package Models.Network.Protocol.Client;

import Models.BaseRegex;
import Models.Network.Protocol.Event.ClientRemoveFileEvent;
import Models.Network.Protocol.Event.ClientSignUpEvent;
import Models.Network.Protocol.FFE.FFESignResultMessage;
import Models.Network.Protocol.MessageProtocol;
import Models.User;
import Models.UserController;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientSignUpMessage extends MessageProtocol {

    private String login;
    private String password;

    private final String protocolRegex = "^SIGNUP" + BaseRegex.bl + BaseRegex.login + BaseRegex.bl + BaseRegex.password + BaseRegex.line + "$";

    private List<ClientSignUpEvent> listeners = new ArrayList<>();

    public ClientSignUpMessage() {
    }

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

        fire();
        return true;
    }

    @Override
    public String serialize() {
        return String.format("SIGNUP %s %s\r\n", this.login, this.password);
    }

    public void subscribe(ClientSignUpEvent listener) {
        listeners.add(listener);
    }

    public void unsubscribe(ClientSignUpEvent listener) {
        listeners.remove(listener);
    }

    private void fire() {
        for (ClientSignUpEvent listener : listeners) {
            listener.onClientSignUpEvent(client, login, password);
        }
    }
}
