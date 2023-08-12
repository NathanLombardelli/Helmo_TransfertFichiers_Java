package Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class File {

    private String filename;
    private long fileSize;
    private String iv;
    private List<String> storageProviders;

    public File() {
        this.storageProviders = new ArrayList();
    }

    public File(String filename, long fileSize, String iv, List<String> storageProviders) {
        this.filename = filename;
        this.fileSize = fileSize;
        this.iv = iv;
        this.storageProviders = storageProviders;
    }

    public File(String filename, long fileSize, String iv) {
        this.filename = filename;
        this.fileSize = fileSize;
        this.iv = iv;
        this.storageProviders = new ArrayList();
    }

    public File(String filename, long fileSize) {
        this.filename = filename;
        this.fileSize = fileSize;
        this.iv = iv;
        this.storageProviders = new ArrayList();
    }

    public String getFilename() {
        return filename;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getIv() {
        return iv;
    }

    public List<String> getStorageProviders() {
        return storageProviders;
    }

    public void addStorageProvider(String storageProvider) {
        this.storageProviders.add(storageProvider);
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        File file = (File) o;
        return Objects.equals(filename, file.filename);
    }
}
