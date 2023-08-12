package Models.Network.Protocol.Event;

import Models.Network.ClientHandler;

public interface FFEEraseFileMessageEvent {
    void onFFEEraseFileMessage(ClientHandler client, String hashFileName);
}
