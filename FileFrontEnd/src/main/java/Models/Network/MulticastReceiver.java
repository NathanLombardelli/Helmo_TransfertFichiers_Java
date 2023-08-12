package Models.Network;

import Models.Network.Protocol.MessageProtocol;
import Models.Network.Protocol.ProtocolParser;
import Models.Network.Protocol.SBE.SBEHelloMessage;

import java.io.IOException;
import java.net.*;

public class MulticastReceiver implements Runnable {

    private boolean isListening = true;
    private InetAddress group;
    private String groupName;
    private String networkInterfaceName;
    private int port;
    private MulticastSocket socket = null;

    private ProtocolParser parser;

    public MulticastReceiver(String group, int port, String networkInterfaceName, ProtocolParser parser) {
        this.groupName = group;
        this.port = port;
        this.networkInterfaceName = networkInterfaceName;
        this.parser = parser;
        init();
    }

    private void init() {
        try {
            socket = new MulticastSocket(this.port);
            group = InetAddress.getByName(this.groupName);
            NetworkInterface netIf = NetworkInterface.getByName(networkInterfaceName);
            socket.setNetworkInterface(netIf);
            socket.joinGroup(this.group);
        } catch (UnknownHostException e) {
            System.out.println("[UnknownHostException] The multicast group is not correct");
        } catch (IOException e) {
            System.out.println("[IOE] Impossible to create MulticastReceiver");
        }
    }

    private void listen() {
        try {
            while (isListening) {
                byte[] buf = new byte[256];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());

                MessageProtocol message = parser.parse(received);

                if (message.getClass() == SBEHelloMessage.class)
                    ((SBEHelloMessage) message).setIp(packet.getAddress());

                if (message != null) {
                    message.deserialize(received);
                }

            }
            socket.leaveGroup(this.group);
            socket.close();
        } catch (IOException e) {
            System.out.println("[IOE] Impossible to listen MulticastReceiver");
        } finally {
            isListening = false;
        }
    }

    @Override
    public void run() {
        listen();
    }
}
