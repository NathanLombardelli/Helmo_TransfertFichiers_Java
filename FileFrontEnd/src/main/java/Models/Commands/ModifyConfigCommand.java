package Models.Commands;

import Models.Config;

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
        int selectedNumber;

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
                case 1: modifyMLAddress(); break;
                case 2: modifyMLPort(); break;
                case 3: modifyUnicastPort(); break;
                case 4: modifyNetworkInterface(); break;
                case 5: modifyTLS(); break;
                case 6: modifyDLPath(); break;
            }

        } while (selectedNumber != 0);
    }

    private void printConfig() {
        System.out.printf("0. Exit Modify Configuration\n");
        System.out.printf("1. Multicast Address: %s\n", config.getMulticastAddress());
        System.out.printf("2. Multicast Port: %d\n", config.getMulticastPort());
        System.out.printf("3. Unicast Port: %d\n", config.getUnicastPort());
        System.out.printf("4. Network Interface: %s\n", config.getNetworkInterface());
        System.out.printf("5. TLS: %b\n", config.isTls());
        System.out.printf("6. Download Path: %s\n", config.getDownloadFolderPath());
    }

    private void modifyDLPath() {
        try {
            System.out.println("New Download Path:");
            String newDLPath = console.readLine();
            config.setDownloadFolderPath(newDLPath);
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

    private void modifyTLS() {
        config.setTls(!config.isTls());
        System.out.printf("TLS has been changed to %b\n", config.isTls());
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


