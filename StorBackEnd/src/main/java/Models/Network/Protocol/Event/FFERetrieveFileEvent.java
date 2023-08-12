package Models.Network.Protocol.Event;

import Models.Network.ClientHandler;

public interface FFERetrieveFileEvent {
    void onFFERetrieveFileEvent(ClientHandler client, String hashFileName);
}
