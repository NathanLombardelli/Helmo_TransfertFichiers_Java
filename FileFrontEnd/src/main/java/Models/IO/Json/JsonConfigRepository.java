package Models.IO.Json;

import Models.Config;
import Models.IO.Interfaces.IConfigRepository;
import Models.IO.Schema.SConfig;
import Models.IO.Schema.SFile;
import Models.IO.Schema.SUser;
import Models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JsonConfigRepository implements IConfigRepository {

    private final Gson gson;

    public JsonConfigRepository() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public boolean load(String path, Config config)  {

        if (!isExists(path)) {
            System.out.printf("The Configuration file does not exist : %s\n", path);
            return false;
        }

        config.reset();

        try (RandomAccessFile reader = new RandomAccessFile(path, "r");
             FileChannel channel = reader.getChannel();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            int bufferSize = 1024;
            if (bufferSize > channel.size()) {
                bufferSize = (int) channel.size();
            }
            ByteBuffer buff = ByteBuffer.allocate(bufferSize);

            while (channel.read(buff) > 0) {
                out.write(buff.array(), 0, buff.position());
                buff.clear();
            }

            String fileContent = new String(out.toByteArray(), StandardCharsets.UTF_8);
            SConfig sConfig = gson.fromJson(fileContent, SConfig.class);
            convertConfig(sConfig, config);
            return true;

        } catch (FileNotFoundException e) {
            System.out.println("Configuration file not found");
            return false;
        } catch (IOException exception) {
            System.out.printf("Impossible to load the Configuration : %s\n", path);
            return false;
        }

    }

    @Override
    public boolean save(String path, Config config) {
        SConfig SConfig = new SConfig(config);
        String text = gson.toJson(SConfig);

        try (RandomAccessFile writer = new RandomAccessFile(path, "rw");
             FileChannel channel = writer.getChannel()){
            ByteBuffer buff = ByteBuffer.wrap(text.getBytes(StandardCharsets.UTF_8));
            channel.truncate(0);
            channel.write(buff);
            channel.close();
            return true;
        } catch (IOException exception) {
            System.out.printf("Impossible to save the Configuration : %s\n", path);
            return false;
        }
    }

    private boolean isExists(String path){
        return new File(path).exists();
    }


    private void convertConfig(SConfig sConfig, Config config) {
        config.setMulticastAddress(sConfig.getMulticastAddress());
        config.setMulticastPort(sConfig.getMulticastPort());
        config.setUnicastPort(sConfig.getUnicastPort());
        config.setNetworkInterface(sConfig.getNetworkInterface());
        config.setTls(sConfig.isTls());
        config.setDownloadFolderPath(sConfig.getDownloadFolderPath());

        for (SUser sUser : sConfig.getUsers()) {
            List<Models.File> storedFiles = new ArrayList();

            for (SFile sFile : sUser.getStoredFiles()) {
                storedFiles.add(new Models.File(sFile.getFilename(), sFile.getFileSize(), sFile.getIv(), sFile.getStorageProviders()));
            }

            User user = new User(sUser.getLogin(), sUser.getHashPassword(), sUser.getAesKey(), storedFiles, sUser.getKey2FA(), sUser.getkeyIV2FA());
            config.addUser(user);
        }
    }

}
