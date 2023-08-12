package Models.Network.Protocol.Event;

public interface ClientGetFileResultEvent {
    void onFFESaveFileResult(boolean success, String filename, long fileSize);
}
