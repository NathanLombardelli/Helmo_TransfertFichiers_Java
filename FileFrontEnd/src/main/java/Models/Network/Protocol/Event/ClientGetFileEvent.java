package Models.Network.Protocol.Event;

import Models.Network.ClientHandler;

public interface ClientGetFileEvent {
    void onClientGetFileEvent(ClientHandler client, String filename);
}
