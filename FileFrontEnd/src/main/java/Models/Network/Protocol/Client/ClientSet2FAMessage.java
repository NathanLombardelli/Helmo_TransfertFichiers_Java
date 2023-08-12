package Models.Network.Protocol.Client;

import Models.BaseRegex;
import Models.Cryptography.Aes;
import Models.Network.ClientHandler;
import Models.Network.Protocol.Event.ClientRemoveFileEvent;
import Models.Network.Protocol.Event.ClientSet2FAEvent;
import Models.Network.Protocol.FFE.FFESet2FAResultMessage;
import Models.Network.Protocol.MessageProtocol;
import Models.User;
import Models.UserController;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientSet2FAMessage extends MessageProtocol {

    private String login;
    private String password;

    private final String protocolRegex = "^SET2FA" + BaseRegex.bl + BaseRegex.login + BaseRegex.bl + BaseRegex.password + BaseRegex.line + "$";

    private List<ClientSet2FAEvent> listeners = new ArrayList<>();

    public ClientSet2FAMessage() {
    }

    public ClientSet2FAMessage(String login, String password) {
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
        return String.format("SET2FA %s %s\r\n", login, password);
    }

    public void subscribe(ClientSet2FAEvent listener) {
        listeners.add(listener);
    }

    public void unsubscribe(ClientSet2FAEvent listener) {
        listeners.remove(listener);
    }

    private void fire() {
        for (ClientSet2FAEvent listener : listeners) {
            listener.onClientSet2FAEvent(client, login, password);
        }
    }
}
