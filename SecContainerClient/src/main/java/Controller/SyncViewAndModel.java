package Controller;

import View.ViewController;


public interface SyncViewAndModel {

    void setView(ViewController v);

    boolean signUp(String ip, int port, boolean isTls, String login, String password);
    boolean signIn(String ip, int port, boolean isTls, String login, String password);
    boolean signOut();

    void refreshList();
    void uploadFile(String path, String fileName, long fileSize);
    void downloadFile(String fileName);
    void removeFile(String filename);

    void setDownloadDirectory(String path);

    void activate2FA(String host, int port, boolean isTls, String login, String password);
    void login2FA(String login, String password, String code);
}
