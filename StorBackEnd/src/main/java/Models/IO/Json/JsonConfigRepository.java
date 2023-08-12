package Models.IO.Json;

import Models.Config;
import Models.IO.Interfaces.IConfigRepository;
import Models.IO.Schema.SConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonConfigRepository implements IConfigRepository {

    private final Gson gson;

    public JsonConfigRepository() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public boolean load(String path, Config config) {
        if (!isExists(path)) {
            System.out.printf("The Configuration file does not exist : %s\n", path);
            return false;
        }

        try {
            String json = Files.readString(Paths.get(path));
            SConfig sConfig = gson.fromJson(json, SConfig.class);
            convertConfig(sConfig, config);
            return true;
        } catch (IOException e) {
            System.out.printf("Impossible to load the Configuration : %s\n", path);
            return false;
        }
    }

    @Override
    public boolean save(String path, Config config) {
        SConfig SConfig = new SConfig(config);

        try (FileWriter fileWriter = new FileWriter(new File(path), StandardCharsets.UTF_8)) {
            String json = gson.toJson(SConfig);
            fileWriter.write(json);
            fileWriter.flush();
            return true;
        } catch (IOException e) {
            System.out.printf("Impossible to save the Configuration : %s\n", path);
            return false;
        }
    }

    private boolean isExists(String path){
        return new File(path).exists();
    }

    private void convertConfig(SConfig sConfig, Config config) {
        config.setUniqueID(sConfig.getUniqueID());
        config.setDownLoadPath(sConfig.getDownLoadPath());
        config.setMulticastAddress(sConfig.getMulticastAddress());
        config.setMulticastPort(sConfig.getMulticastPort());
        config.setMulticastDelayInSeconds(sConfig.getMulticastDelayInSeconds());
        config.setUnicastPort(sConfig.getUnicastPort());
        config.setNetworkInterface(sConfig.getNetworkInterface());
    }
}
