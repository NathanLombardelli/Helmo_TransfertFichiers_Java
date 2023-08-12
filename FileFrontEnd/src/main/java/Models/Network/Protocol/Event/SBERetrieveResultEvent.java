package Models.Network.Protocol.Event;

public interface SBERetrieveResultEvent {
    void onSBERetrieveResultEvent(boolean success, String hashFileName, String hashFileContent, long size);
}
