package Models.Network.Protocol.FFE;

import Models.BaseRegex;
import Models.Network.Protocol.Event.FFESignResultEvent;
import Models.Network.Protocol.MessageProtocol;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FFESignResultMessage extends MessageProtocol {

    private List<FFESignResultEvent> listeners;

    private boolean success;
    private final String protocolRegex = "^(SIGN_OK|SIGN_ERROR)" + BaseRegex.line + "$";

    public FFESignResultMessage() {
        listeners = new ArrayList<>();
    }

    public FFESignResultMessage(boolean success) {
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
            success = status.equals("SIGN_OK");
            fire();
        }
        return true;
    }

    @Override
    public String serialize() {
        return success ? "SIGN_OK\r\n" : "SIGN_ERROR\r\n";
    }

    public void subscribe(FFESignResultEvent listener) {
        listeners.add(listener);
    }

    public void unsubscribe(FFESignResultEvent listener) {
        listeners.remove(listener);
    }

    private void fire() {
        for (FFESignResultEvent listener : listeners) {
            listener.onFFESignResult(success);
        }
    }
}
