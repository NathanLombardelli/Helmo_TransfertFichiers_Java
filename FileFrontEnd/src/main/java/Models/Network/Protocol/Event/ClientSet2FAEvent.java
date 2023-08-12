package Models.Network.Protocol.Event;

import Models.Network.ClientHandler;

public interface ClientSet2FAEvent {
    void onClientSet2FAEvent(ClientHandler client, String login, String password);
}
