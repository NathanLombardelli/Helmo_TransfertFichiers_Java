package Models.Commands;

import App.Program;
import Models.Config;
import Models.IO.Interfaces.IConfigRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoadConfigCommand extends Command {

    private IConfigRepository repository;
    private Config config;

    public LoadConfigCommand(String command, String description, IConfigRepository repository, Config config) {
        super(command, description);
        this.repository = repository;
        this.config = config;
    }

    @Override
    public void execute() {
        try {
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            System.out.printf("Enter the path of the configuration file: \n");
            String path = console.readLine().toLowerCase();

            if (this.repository.load(path, this.config)) {
                Program.configPath = path;
                System.out.println("The configuration file has been loaded successfully");
            } else {
                System.out.println("Impossible to load the configuration file");
            }
        } catch (IOException e) {
            System.out.println("Impossible to read the config path");
        }
    }

}
