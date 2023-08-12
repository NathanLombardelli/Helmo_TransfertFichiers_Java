package Models;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

public class User {

    private String login;
    private String hashPassword;
    private String aesKey;
    private List<File> storedFiles;
    private String key2FA = null; // Encrypt with Aes / Null if not activate
    private String keyIV2FA = null;

    public User() {
        this.storedFiles = new ArrayList();
    }

    public User(String login, String hashPassword, String aesKey) {
        this.login = login;
        this.hashPassword = hashPassword;
        this.aesKey = aesKey;
        this.storedFiles = new ArrayList();
    }

    public User(String login, String hashPassword, String aesKey, List<File> files, String key2FA, String keyIV2FA) {
        this.login = login;
        this.hashPassword = hashPassword;
        this.aesKey = aesKey;
        this.storedFiles = files;
        this.key2FA = key2FA;
        this.keyIV2FA = keyIV2FA;
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

    public List<File> getStoredFiles() {
        return storedFiles;
    }

    public void addFile(File file) {
        this.storedFiles.add(file);
    }

    public boolean removeFile(String fileName) {
        File file = getFilebyName(fileName);
        if (file!=null) {
            storedFiles.remove(file);
            return true;
        }
        return false;
    }

    private File getFilebyName(String fileName) {
        for (File actuel: storedFiles){
            if(actuel.getFilename().equals(fileName)){
                return actuel;
            }
        }
        return null;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }
    // Utilisé dans shéma
    public String getKey2FA() { return this.key2FA; }
    public String getkeyIV2FA() { return this.keyIV2FA; }

    public void setKey2FA(String key2FA) { this.key2FA = key2FA; }
    public void setkeyIV2FA(String key2FA) { this.keyIV2FA = key2FA; }

    // Utilisé dans controller
    public void setKey2FA(byte[] key2FA) { this.key2FA = Base64.getEncoder().encodeToString(key2FA); } // Setter(not in constructor) because AesKey needed
    public void setKeyIV2FA(byte[] keyIV2FA) { this.keyIV2FA = Base64.getEncoder().encodeToString(keyIV2FA); }

    public byte[] getKey2FA(Boolean toBytes) { return Base64.getDecoder().decode(this.key2FA); }
    public byte[] getkeyIV2FA(Boolean toBytes) { return Base64.getDecoder().decode(this.keyIV2FA); }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(login, user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }

}
