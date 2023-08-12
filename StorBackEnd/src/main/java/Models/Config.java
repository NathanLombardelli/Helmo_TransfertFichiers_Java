package Models;

public class Config {
    private String uniqueID;
    private String downLoadPath;
    private String multicastAddress;
    private int multicastPort;
    private int multicastDelayInSeconds;
    private int unicastPort;
    private String networkInterface;

    public Config() {
    }

    public Config(String uniqueID, String downLoadPath, String multicastAddress, int multicastPort, int multicastDelayInSeconds, int unicastPort, String networkInterface) {
        this.uniqueID = uniqueID;
        this.downLoadPath = downLoadPath;
        this.multicastAddress = multicastAddress;
        this.multicastPort = multicastPort;
        this.multicastDelayInSeconds = multicastDelayInSeconds;
        this.unicastPort = unicastPort;
        this.networkInterface = networkInterface;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public String getDownLoadPath() {
        return downLoadPath;
    }

    public String getMulticastAddress() {
        return multicastAddress;
    }

    public int getMulticastPort() {
        return multicastPort;
    }

    public int getMulticastDelayInSeconds() {
        return multicastDelayInSeconds;
    }

    public int getUnicastPort() {
        return unicastPort;
    }

    public String getNetworkInterface() {
        return networkInterface;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public void setDownLoadPath(String downLoadPath) {
        this.downLoadPath = downLoadPath;
    }

    public void setMulticastAddress(String multicastAddress) {
        this.multicastAddress = multicastAddress;
    }

    public void setMulticastPort(int multicastPort) {
        this.multicastPort = multicastPort;
    }

    public void setMulticastDelayInSeconds(int multicastDelayInSeconds) {
        this.multicastDelayInSeconds = multicastDelayInSeconds;
    }

    public void setUnicastPort(int unicastPort) {
        this.unicastPort = unicastPort;
    }

    public void setNetworkInterface(String networkInterface) {
        this.networkInterface = networkInterface;
    }
}
