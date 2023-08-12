package Models.Network.Protocol.Event;

import Models.Network.ClientHandler;

public interface ClientSignInEvent {
    void onClientSignInEvent(ClientHandler client, String login, String password);
}
