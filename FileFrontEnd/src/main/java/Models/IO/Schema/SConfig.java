package Models.IO.Schema;

import Models.Config;
import Models.User;

import java.util.ArrayList;
import java.util.List;

public class SConfig {

    private String multicastAddress;
    private int multicastPort;
    private int unicastPort;
    private String networkInterface;
    private boolean tls;
    private String downloadFolderPath;
    private List<SUser> users;

    public SConfig(Config config) {
        this.multicastAddress = config.getMulticastAddress();
        this.multicastPort = config.getMulticastPort();
        this.unicastPort = config.getUnicastPort();
        this.networkInterface = config.getNetworkInterface();
        this.tls = config.isTls();
        this.downloadFolderPath = config.getDownloadFolderPath();
        this.users = new ArrayList<>();

        for (User user : config.getUsers()) {
            this.users.add(new SUser(user));
        }
    }

    public String getMulticastAddress() {
        return multicastAddress;
    }

    public int getMulticastPort() {
        return multicastPort;
    }

    public int getUnicastPort() {
        return unicastPort;
    }

    public String getNetworkInterface() {
        return networkInterface;
    }

    public boolean isTls() {
        return tls;
    }

    public String getDownloadFolderPath() {
        return downloadFolderPath;
    }

    public List<SUser> getUsers() {
        return users;
    }
}
