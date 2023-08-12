package Models.Task;

import Models.Network.ClientHandler;
import Models.Network.Protocol.MessageProtocol;
import Models.User;

public class Task {

    private TaskType type;
    private String fileName;
    private MessageProtocol protocol;
    private User user;
    private String SBEID;
    private TaskStatus status;
    private ClientHandler client;
    private String backupFile;

    public Task(TaskType type, String fileName, MessageProtocol protocol, String SBEID, String backupFile) {
        this.type = type;
        this.fileName = fileName;
        this.protocol = protocol;
        this.SBEID = SBEID;
        this.backupFile = backupFile;
        this.status = TaskStatus.IDLE;
    }

    public Task(TaskType type, String fileName, MessageProtocol protocol, User user, String SBEID, ClientHandler client) {
        this.type = type;
        this.fileName = fileName;
        this.protocol = protocol;
        this.user = user;
        this.SBEID = SBEID;
        this.status = TaskStatus.IDLE;
        this.client = client;
        System.out.println(toString());
    }

    public TaskType getType() {
        return type;
    }

    public String getFileName() {
        return fileName;
    }

    public MessageProtocol getProtocol() {
        return protocol;
    }

    public User getUser() {
        return user;
    }

    public String getSBEID() {
        return SBEID;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public ClientHandler getClient() {
        return client;
    }

    public String getBackupFile() {
        return backupFile;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
        System.out.println(toString());
    }

    @Override
    public String toString() {
        return String.format("[TASK Update] Type:%s Filename:%s SBEID:%s Status:%s", type, fileName, SBEID, status);
    }
}
