package Models.Commands;

public abstract class Command {

    private String command;
    private String description;

    public Command(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public abstract void execute();
}
