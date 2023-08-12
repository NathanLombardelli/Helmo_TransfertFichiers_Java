package Models.Network.Protocol.Event;

public interface FFESet2FAResultEvent {
    void onFFESet2FAResultEvent(boolean success, String key2FA);
}
