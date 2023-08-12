package Models.IO.Schema;

import Models.File;

import java.util.List;

public class SFile {
    private String filename;
    private long fileSize;
    private String iv;
    private List<String> storageProviders;

    public SFile(File file) {
        this.filename = file.getFilename();
        this.fileSize = file.getFileSize();
        this.iv = file.getIv();
        this.storageProviders = file.getStorageProviders();
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
}
