package Models.IO.Interfaces;

import Models.Config;

public interface IConfigRepository {
    boolean load(String path, Config config);
    boolean save(String path, Config config);
}
