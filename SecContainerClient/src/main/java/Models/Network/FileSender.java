package Models.Network;

import java.io.*;

public class FileSender implements Runnable {
    private static final int DEFAULT_BUFFER=8000;

    private String filename;
    private OutputStream out;
    private String path;

    public FileSender(String filename, OutputStream out, String path) {
        this.filename = filename;
        this.out = out;
        this.path = path;
    }

    public boolean sendFile() {
        BufferedInputStream bisFile = null;
        int bytesReaded = 0;

        try {
            File f = new File(path);
            long fileSize = f.length();
            if(f.exists()) {
                byte[] buffer = new byte[DEFAULT_BUFFER];
                bisFile = new BufferedInputStream(new FileInputStream(f));
                long currentOffset = 0;
                while((currentOffset < fileSize) && (bytesReaded = bisFile.read(buffer)) > 0) {
                    out.write(buffer, 0, bytesReaded); out.flush();
                    currentOffset+= bytesReaded;
                }
                bisFile.close();
                return true;
            } else
                return false;
        } catch(IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public void run() {
        sendFile();
    }
}
