package Models.Network.Protocol.Event;

import Models.Network.ClientHandler;

public interface ClientSignInWith2FAEvent {
    void onClientSignInWith2FAEvent(ClientHandler client, String login, String password, String digits);
}
