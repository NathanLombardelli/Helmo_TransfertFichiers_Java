package Models.Commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class CommandController {

    private Map<String, Command> commands;

    public CommandController(Map<String, Command> commands) {
        this.commands = commands;
    }

    public void loop() {
        try {
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                System.out.println("Enter a command : ");
                String commandName = console.readLine();

                Command command = this.commands.get(commandName);
                if (command != null) {
                    command.execute();
                } else {
                    System.out.println("The command does not exist, please retry (help)");
                }
            }

        } catch (IOException e) {
            System.out.println("[IOE] Impossible to read the command");
        }
    }

}
