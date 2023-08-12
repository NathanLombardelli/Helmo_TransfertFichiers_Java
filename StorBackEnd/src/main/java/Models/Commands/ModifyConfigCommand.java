package Models.Commands;

import Models.Config;
import Models.IO.Interfaces.IConfigRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class ModifyConfigCommand extends Command {

    private Config config;
    private BufferedReader console;

    public ModifyConfigCommand(String command, String description, Config config) {
        super(command, description);
        this.config = config;
        this.console = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void execute() {
        int selectedNumber = 0;

        do {
            System.out.println("Current configuration:");
            printConfig();
            System.out.println("Enter the config number you want to modify");

            do {
                try {
                    String selected = console.readLine();
                    selectedNumber = Integer.parseInt(selected);
                } catch (NumberFormatException e) {
                    selectedNumber = -1;
                } catch (IOException e) {
                    selectedNumber = 0;
                }
            } while (selectedNumber < 0);

            switch (selectedNumber) {
                case 1: modifyUniqueID(); break;
                case 2: modifyDLPath(); break;
                case 3: modifyMLAddress(); break;
                case 4: modifyMLPort(); break;
                case 5: modifyMLDelay(); break;
                case 6: modifyUnicastPort(); break;
                case 7: modifyNetworkInterface(); break;
            }

        } while (selectedNumber != 0);
    }

    private void printConfig() {
        System.out.printf("0. Exit Modify Configuration\n");
        System.out.printf("1. Unique ID: %s\n", config.getUniqueID());
        System.out.printf("2. Download Path: %s\n", config.getDownLoadPath());
        System.out.printf("3. Multicast Address: %s\n", config.getMulticastAddress());
        System.out.printf("4. Multicast Port: %d\n", config.getMulticastPort());
        System.out.printf("5. Multicast Delay: %d\n", config.getMulticastDelayInSeconds());
        System.out.printf("6. Unicast Port: %d\n", config.getUnicastPort());
        System.out.printf("7. Network Interface: %s\n", config.getNetworkInterface());
    }

    private void modifyUniqueID() {
        try {
            System.out.println("New Unique ID:");
            String newUniqueID = console.readLine();
            config.setUniqueID(newUniqueID);
        } catch (IOException e) {
            System.out.println("[IOE] Impossible to read");
        }
    }

    private void modifyDLPath() {
        try {
            System.out.println("New Download Path:");
            String newDLPath = console.readLine();
            config.setDownLoadPath(newDLPath);
        } catch (IOException e) {
            System.out.println("[IOE] Impossible to read");
        }
    }

    private void modifyMLAddress() {
        try {
            System.out.println("New Multicast Address:");
            String newMLAddress = console.readLine();
            config.setMulticastAddress(newMLAddress);
        } catch (IOException e) {
            System.out.println("[IOE] Impossible to read");
        }
    }

    private void modifyMLPort() {
        try {
            System.out.println("New Multicast Port:");
            String newMLPort = console.readLine();
            config.setMulticastPort(Integer.parseInt(newMLPort));
        } catch (NumberFormatException e) {
            System.out.println("[NFE] The port is not a valid number");
        } catch (IOException e) {
            System.out.println("[IOE] Impossible to read");
        }
    }

    private void modifyMLDelay() {
        try {
            System.out.println("New Multicast Delay (seconds):");
            String newMLDelay = console.readLine();
            config.setMulticastDelayInSeconds(Integer.parseInt(newMLDelay));
        } catch (NumberFormatException e) {
            System.out.println("[NFE] The delay is not a valid number");
        } catch (IOException e) {
            System.out.println("[IOE] Impossible to read");
        }
    }

    private void modifyUnicastPort() {
        try {
            System.out.println("New Unicast Port:");
            String newUnicastPort = console.readLine();
            config.setUnicastPort(Integer.parseInt(newUnicastPort));
        } catch (NumberFormatException e) {
            System.out.println("[NFE] The port is not a valid number");
        } catch (IOException e) {
            System.out.println("[IOE] Impossible to read");
        }
    }

    private void modifyNetworkInterface() {
        config.setNetworkInterface(getNetworkInterface());
    }

    private String getNetworkInterface(){

        List<String> networkInterfaces = new ArrayList<>();
        System.out.println("Network Interfaces :");

        String readline = "";

        try {
            var networkInterfacesEnum = NetworkInterface.getNetworkInterfaces();
            while(networkInterfacesEnum.hasMoreElements()) {
                var networkInterface = networkInterfacesEnum.nextElement();
                System.out.printf("%s => NAME: %s\r\n", networkInterface.getDisplayName(), networkInterface.getName());
                networkInterfaces.add(networkInterface.getName());
            }


            System.out.println("Select a network interface by NAME");
            readline = console.readLine();
            while(!networkInterfaces.contains(readline)) {
                System.out.println("Select a network interface by NAME");
                readline = console.readLine();
            }

        } catch (SocketException e) {
            System.out.println("[SE] Impossible to obtain network interfaces");
        } catch (IOException e) {
            System.out.println("[IOE] Impossible to read");
        }
        return readline;
    }
}


