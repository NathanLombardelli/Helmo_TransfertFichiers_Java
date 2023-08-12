package Models.Network.Protocol.Event;

import Models.Network.ClientHandler;

public interface ClientSignOutEvent {
    void onClientSignOutEvent(ClientHandler client);
}
