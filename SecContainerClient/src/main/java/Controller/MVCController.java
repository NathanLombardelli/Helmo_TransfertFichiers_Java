package Controller;

import Models.File;
import Models.Network.Client;
import Models.Network.Protocol.Client.*;
import Models.Network.Protocol.Event.*;
import Models.Network.Protocol.ProtocolParser;
import View.ViewController;
import javafx.application.Platform;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class MVCController implements SyncViewAndModel, FFESignResultEvent, FFEFileResultEvent, FFESaveFileResultEvent, ClientGetFileResultEvent, FFERemoveFileResultEvent, FFESet2FAResultEvent, FFESignWith2FARequestedEvent {

    private ViewController view;
    private Client client;

    private Boolean userIsConnected = false;

    private ProtocolParser parser;

    private String downloadDirectory;

    private final int WAIT_BEFORE_SEND = 2000;

    public MVCController(ProtocolParser parser) {
        this.parser = parser;
    }

    @Override
    public void setView(final ViewController view) {
        this.view = view;
    }

    @Override
    public void setDownloadDirectory(String path) {
        downloadDirectory = path;
    }

    @Override
    public boolean signUp(String ip, int port, boolean isTls, String login, String password) {
        boolean success = false;
        if (connect(ip, port, isTls)) {
            success = client.sendMessage(new ClientSignUpMessage(login, password));
        }
        return success;
    }

    @Override
    public boolean signIn(String ip, int port, boolean isTls, String login, String password) {
        boolean success = false;
        if (connect(ip, port, isTls)) {
            success = client.sendMessage(new ClientSignInMessage(login, password));
        }
        return success;
    }

    @Override
    public void activate2FA(String host, int port, boolean isTls, String login, String password) {
        if (userIsConnected) {
            view.setStatus("PLEASE SIGN OUT BEFORE ACTIVATE 2FA");
        } else if (connect(host, port, isTls)) {
            Platform.runLater(() -> {
                if (view.confimationSet2FA())
                    client.sendMessage(new ClientSet2FAMessage(login, password));
            });
        }
    }

    private boolean connect(String ip, int port, boolean isTls) {
        try {
            client = new Client(InetAddress.getByName(ip), port, isTls, parser);
            new Thread(client).start();
        } catch (UnknownHostException e) {
            return false;
        } catch (ConnectException e) {
            view.setStatus("Connection refused by host");
            return false;
        }
        return true;
    }

    @Override
    public void login2FA(String login, String password, String code) {
        client.sendMessage(new ClientSignInWith2FAMessage(login, password, code));
    }

    @Override
    public boolean signOut() {
        if (client != null) {
            client.sendMessage(new ClientSignOutMessage());
            client.disconnect();
            userIsConnected = false;
            return true;
        }
        return false;
    }

    @Override
    public void refreshList() {
        client.sendMessage(new ClientFileListMessage());
        view.setStatus("List refreshed");
    }

    @Override
    public void uploadFile(String path, String fileName, long fileSize) {
        try {
            view.setDownloadGroupEnabled(false);
            client.sendMessage(new ClientSaveFileMessage(fileName, fileSize));
            Thread.sleep(WAIT_BEFORE_SEND);
            client.sendFile(fileName, path);
        } catch (IOException | InterruptedException e) {
            System.out.println("Impossible to send file");
        }
    }

    @Override
    public void downloadFile(String fileName) {
        view.setDownloadGroupEnabled(false);
        client.sendMessage(new ClientGetFileMessage(fileName));
        view.setStatus("Downloading ... (" + fileName + ")");
    }

    @Override
    public void removeFile(String filename) {
        view.setDownloadGroupEnabled(false);
        client.sendMessage(new ClientRemoveFileMessage(filename));
        view.setStatus("Removing ...");
    }

    @Override
    public void onFFESignResult(boolean success) {
        Platform.runLater(() -> {
            if (success) {
                view.setStatus("SIGNIN SUCCESS !");
                view.setConnectionGroupEnabled(true);
                userIsConnected = true;
                client.sendMessage(new ClientFileListMessage());
            } else {
                view.setStatus("SIGNIN ERROR !");
            }
        });
    }

    @Override
    public void onFFEFileResult(List<File> files) {
        Platform.runLater(() -> {
            view.setFilesList(files);
        });
    }

    @Override
    public void onFFESaveFileResult(boolean success) {
        Platform.runLater(() -> {
            if (success) {
                view.setStatus("FILE SAVED !");
                client.sendMessage(new ClientFileListMessage());
            } else {
                view.setStatus("FILE NOT SAVED ! (Error)");
            }
            view.setDownloadGroupEnabled(true);
        });
    }

    @Override
    public void onFFESaveFileResult(boolean success, String filename, long fileSize) {
        String status = "";

        if (success) {
            try {
                if (client.readFile(filename, fileSize, downloadDirectory)) {
                    System.out.printf("File %s downloaded in %s\n", filename, downloadDirectory);
                    status = String.format("File %s downloaded !\n", filename);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            status = String.format("File %s not downloaded (Error)");
        }

        String finalStatus = status;
        Platform.runLater(() -> {
            view.setStatus(finalStatus);
            view.setDownloadGroupEnabled(success);
        });
    }

    @Override
    public void onFFERemoveFileResult(boolean success) {
        Platform.runLater(() -> {
            if (success) {
                view.setStatus("FILE DELETED (SAFE) !");
                client.sendMessage(new ClientFileListMessage());
            } else {
                view.setStatus("FILE NOT DELETED !");
            }
            view.setDownloadGroupEnabled(true);
        });
    }

    @Override
    public void onFFESet2FAResultEvent(boolean success, String key2FA) {
        Platform.runLater(() -> {
            if (success) {
                view.setStatus("2FA SET !");
                view.open2FAWindow(key2FA);
            } else {
                view.setStatus("ERROR TO SET 2FA OR IS ALREADY SET!");
            }
        });
    }

    @Override
    public void onFFESignWith2FARequestedEvent() {
        Platform.runLater(() -> {
            view.open2FAWindow(null);
        });
    }
}
