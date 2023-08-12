package Models.IO.Schema;

import Models.Config;

public class SConfig {
    private String uniqueID;
    private String downLoadPath;
    private String multicastAddress;
    private int multicastPort;
    private int multicastDelayInSeconds;
    private int unicastPort;
    private String networkInterface;

    public SConfig(Config config) {
        this.uniqueID = config.getUniqueID();
        this.downLoadPath = config.getDownLoadPath();
        this.multicastAddress = config.getMulticastAddress();
        this.multicastPort = config.getMulticastPort();
        this.multicastDelayInSeconds = config.getMulticastDelayInSeconds();
        this.unicastPort = config.getUnicastPort();
        this.networkInterface = config.getNetworkInterface();
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
}
