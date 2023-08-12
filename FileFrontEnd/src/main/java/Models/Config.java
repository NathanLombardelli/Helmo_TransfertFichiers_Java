package Models;

import java.util.ArrayList;
import java.util.List;

public class Config {

    private String multicastAddress;
    private int multicastPort;
    private int unicastPort;
    private String networkInterface;
    private boolean tls;
    private String downloadFolderPath;
    private List<User> users;

    public Config() {
        this.users = new ArrayList<>();
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

    public List<User> getUsers() {
        return users;
    }

    public void updateUser(User user) {
        int index = users.indexOf(user);
        if (index >= 0) {
            users.set(index, user);
        }
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void setMulticastAddress(String multicastAddress) {
        this.multicastAddress = multicastAddress;
    }

    public void setMulticastPort(int multicastPort) {
        this.multicastPort = multicastPort;
    }

    public void setUnicastPort(int unicastPort) {
        this.unicastPort = unicastPort;
    }

    public void setNetworkInterface(String networkInterface) {
        this.networkInterface = networkInterface;
    }

    public void setTls(boolean tls) {
        this.tls = tls;
    }

    public void setDownloadFolderPath(String downloadFolderPath) {
        this.downloadFolderPath = downloadFolderPath;
    }

    public void reset() {
        multicastAddress = "";
        multicastPort = 0;
        unicastPort = 0;
        networkInterface = "";
        tls = false;
        downloadFolderPath = "";
        this.users = new ArrayList<>();
    }
}
