package Models.Commands;

public class ExitCommand extends Command {

    public ExitCommand(String command, String description) {
        super(command, description);
    }

    @Override
    public void execute() {
        System.out.println("Good bye !");
        System.exit(0);
    }
}
