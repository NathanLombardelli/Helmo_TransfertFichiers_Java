package Models.IO.Schema;

import Models.File;
import Models.User;

import java.util.ArrayList;
import java.util.List;

public class SUser {
    private String login;
    private String hashPassword;
    private String aesKey;
    private List<SFile> storedFiles = new ArrayList();
    private String key2FA = null; // Encrypt with Aes / Null if not activate
    private String keyIV2FA = null;

    public SUser(User user) {
        this.login = user.getLogin();
        this.hashPassword = user.getHashPassword();
        this.aesKey = user.getAesKey();
        this.key2FA = user.getKey2FA();
        this.keyIV2FA = user.getkeyIV2FA();

        for (File file : user.getStoredFiles()) {
            this.storedFiles.add(new SFile(file));
        }
    }

    public String getLogin() {
        return login;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public String getAesKey() {
        return aesKey;
    }

    public List<SFile> getStoredFiles() {
        return storedFiles;
    }

    public String getKey2FA() { return key2FA; }

    public String getkeyIV2FA() { return keyIV2FA; }
}
