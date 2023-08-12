package Models.Network.Protocol.SBE;

import Models.BaseRegex;
import Models.Network.Protocol.Event.SBESendResultEvent;
import Models.Network.Protocol.MessageProtocol;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SBESendResultMessage extends MessageProtocol {

    private boolean success;
    private final String protocolRegex = "^(SEND_OK|SEND_ERROR)" + BaseRegex.line + "$";

    private List<SBESendResultEvent> listeners = new ArrayList<>();

    @Override
    public boolean deserialize(String message) {

        if (!message.matches(protocolRegex))
            return false;

        Pattern pattern = Pattern.compile(protocolRegex);
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            String status = matcher.group(1);
            success = "SEND_OK".equals(status);
        }
        fire();
        return true;
    }

    @Override
    public String serialize() {
        return success ? "SEND_OK\r\n" : "SEND_ERROR\r\n";
    }

    public void subscribe(SBESendResultEvent listener) {
        listeners.add(listener);
    }

    public void unsubscribe(SBESendResultEvent listener) {
        listeners.remove(listener);
    }

    private void fire() {
        for (SBESendResultEvent listener : listeners) {
            listener.onSBESendResultEvent(success);
        }
    }
}
