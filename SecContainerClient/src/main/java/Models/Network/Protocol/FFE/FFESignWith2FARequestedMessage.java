package Models.Network.Protocol.FFE;

import Models.BaseRegex;
import Models.Network.Protocol.Event.FFESignWith2FARequestedEvent;
import Models.Network.Protocol.MessageProtocol;

import java.util.ArrayList;
import java.util.List;

public class FFESignWith2FARequestedMessage extends MessageProtocol {

    private List<FFESignWith2FARequestedEvent> listeners;

    private final String protocolRegex = "^SIGN_2FA_NEEDED" + BaseRegex.line + "$";

    public FFESignWith2FARequestedMessage() {
        listeners = new ArrayList<>();
    }

    @Override
    public boolean deserialize(String message) {

        if (!message.matches(protocolRegex))
            return false;

        fire();
        return true;
    }

    @Override
    public String serialize() {
        return "SIGN_2FA_NEEDED\r\n";
    }

    public void subscribe(FFESignWith2FARequestedEvent listener) {
        listeners.add(listener);
    }

    public void unsubscribe(FFESignWith2FARequestedEvent listener) {
        listeners.remove(listener);
    }

    private void fire() {
        for (FFESignWith2FARequestedEvent listener : listeners) {
            listener.onFFESignWith2FARequestedEvent();
        }
    }
}

