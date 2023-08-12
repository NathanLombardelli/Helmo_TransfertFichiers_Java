package Models.Commands;

import App.Program;
import Models.Config;
import Models.IO.Interfaces.IConfigRepository;


public class SaveConfigCommand extends Command {

    private IConfigRepository repository;
    private Config config;

    public SaveConfigCommand(String command, String description, IConfigRepository repository, Config config) {
        super(command, description);
        this.repository = repository;
        this.config = config;
    }

    @Override
    public void execute() {
        if (this.repository.save(Program.configPath, config)) {
            System.out.println("The configuration file has been saved successfully");
        }
    }
}
