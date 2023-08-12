package Models.Commands;

import java.util.Map;

public class HelpCommand extends Command {

    private Map<String, Command> commands;

    public HelpCommand(String command, String description, Map<String, Command> commands) {
        super(command, description);
        this.commands = commands;
    }

    @Override
    public void execute() {
        System.out.println("All available commands:");
        for (Command command : commands.values()) {
            System.out.printf("%s : %s\n", command.getCommand(), command.getDescription());
        }
    }
}
