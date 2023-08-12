package Models.Network.Protocol;

public abstract class MessageProtocol {
    public abstract boolean deserialize(String message);
    public abstract String serialize();
}
