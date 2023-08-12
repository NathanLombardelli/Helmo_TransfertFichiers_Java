package Models.Network.Protocol.Event;

import Models.Network.ClientHandler;

public interface ClientSaveFileEvent {
    void onClientSaveFileEvent(ClientHandler client, String filename, long filesize);
}
