package Models.Network.Protocol.Event;

import Models.Network.ClientHandler;

public interface ClientRemoveFileEvent {
    void onClientRemoveFileEvent(ClientHandler client, String filename);
}
