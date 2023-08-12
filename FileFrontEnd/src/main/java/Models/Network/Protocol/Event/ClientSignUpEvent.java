package Models.Network.Protocol.Event;

import Models.Network.ClientHandler;

public interface ClientSignUpEvent {
    void onClientSignUpEvent(ClientHandler client, String login, String password);
}
