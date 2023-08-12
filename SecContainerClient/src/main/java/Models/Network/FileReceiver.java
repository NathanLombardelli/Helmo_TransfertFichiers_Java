package Models.Network;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileReceiver {
    private static final int DEFAULT_BUFFER = 8000;

    public boolean receiveFile(String filename, long fileSize, InputStream input, String path) {
        int bytesReceived = 0;
        BufferedOutputStream bosFile = null;

        try {
            byte[] buffer = new byte[DEFAULT_BUFFER];
            bosFile = new BufferedOutputStream(new FileOutputStream(String.format("%s/%s", path, filename)));
            long currentOffset = 0;

            while ((currentOffset < fileSize) && ((bytesReceived = input.read(buffer)) > 0)) {
                bosFile.write(buffer, 0, bytesReceived);
                currentOffset += bytesReceived;
            }
            bosFile.flush();
            bosFile.close();

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (bosFile != null) {
                try {
                    bosFile.close();
                } catch (Exception e) {
                }
            }
            return false;
        }
    }
}
