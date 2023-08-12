package Models;

import Models.Cryptography.SHA384;
import Models.Network.ClientHandler;
import Models.Network.Protocol.Event.FFEEraseFileMessageEvent;
import Models.Network.Protocol.Event.FFERetrieveFileEvent;
import Models.Network.Protocol.Event.FFESendFileEvent;
import Models.Network.Protocol.SBE.SBEEraseResultMessage;
import Models.Network.Protocol.SBE.SBERetrieveResultMessage;
import Models.Network.Protocol.SBE.SBESendResultMessage;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.SecureRandom;

public class FileManager implements FFESendFileEvent, FFERetrieveFileEvent, FFEEraseFileMessageEvent {

    private Config config;
    private SHA384 SHA384 = new SHA384();

    public FileManager(Config config) {
        this.config = config;
    }

    public boolean safeDeleteFile(String hashFileName, int nbDelete) throws IOException {
        File file = new File(config.getDownLoadPath(), hashFileName);

        if (!file.exists()) return false;

        long length = file.length();

        SecureRandom random = new SecureRandom();
        RandomAccessFile raf = new RandomAccessFile(file, "rws");

        for (int i = 0; i < nbDelete; i++) {
            raf.seek(0);
            raf.getFilePointer();
            byte[] data = new byte[64];
            int pos = 0;
            while (pos < length) {
                random.nextBytes(data);
                raf.write(data);
                pos += data.length;
            }
        }

        raf.close();
        return file.delete();
    }

    @Override
    public void onFFESendFileEvent(ClientHandler client, String hashFileName, String hashFileContent, long size) {
        if (client == null) return;
        try {
            boolean success = client.readFile(hashFileName, size);
            if (success) {
                success = hashFileContent.equals(SHA384.getFileChecksum(config.getDownLoadPath(), hashFileName));
            }
            client.sendMessage(new SBESendResultMessage(success));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFFERetrieveFileEvent(ClientHandler client, String hashFileName) {
        if (client == null) return;
        try {

            File file = new File(config.getDownLoadPath(), hashFileName);
            boolean success = file.exists();

            if (success) {
                String checksum = SHA384.getFileChecksum(config.getDownLoadPath(), hashFileName);
                client.sendMessage(new SBERetrieveResultMessage(hashFileName, checksum, file.length()));
                Thread.sleep(2000);
                client.sendFile(hashFileName, config.getDownLoadPath());
            } else {
                client.sendMessage(new SBERetrieveResultMessage(false));
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFFEEraseFileMessage(ClientHandler client, String hashFileName) {
        if (client == null) return;

        try {
            boolean success = safeDeleteFile(hashFileName, 5);
            client.sendMessage(new SBEEraseResultMessage(success));
        } catch (IOException e) {
            System.out.printf("[File Manager] Impossible to safe delete: %s\n", hashFileName);
            e.printStackTrace();
        }

    }
}
