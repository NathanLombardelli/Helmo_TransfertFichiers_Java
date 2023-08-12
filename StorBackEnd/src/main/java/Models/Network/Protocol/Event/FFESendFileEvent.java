package Models.Network.Protocol.Event;

import Models.Network.ClientHandler;

public interface FFESendFileEvent {
    void onFFESendFileEvent(ClientHandler client, String hashFileName, String hashFileContent, long size);
}
