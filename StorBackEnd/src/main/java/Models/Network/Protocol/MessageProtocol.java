package Models.Network.Protocol;

import Models.Network.ClientHandler;

public abstract class MessageProtocol {
    protected ClientHandler client;
    public void setClient(ClientHandler client) {
        this.client = client;
    }

    public abstract boolean deserialize(String message);
    public abstract String serialize();
}
