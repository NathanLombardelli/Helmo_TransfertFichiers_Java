package Models.Network;

import java.io.IOException;
import java.net.*;

public class MulticastPublisher {

    private InetAddress group;
    private final int port;
    private MulticastSocket socket;

    public MulticastPublisher(String group, int port, String networkInterfaceName) {
        this.port = port;
        try {
            this.group = InetAddress.getByName(group);
            this.socket = new MulticastSocket(port);
            this.socket.setNetworkInterface(NetworkInterface.getByName(networkInterfaceName));
            this.socket.joinGroup(this.group);
        } catch (UnknownHostException e) {
            System.out.println("[UnknownHostException] The multicast group is not correct");
        } catch (IOException e) {
            System.out.println("[IOE] Impossible to create MulticastPublisher");
        }
    }

    public boolean publish(String message) {
        try {
            byte[] buf;
            buf = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, group, port);
            this.socket.send(packet);
            return true;
        } catch (IOException e) {
            System.out.printf("[IOE] Impossible to publish via Multicast (%s %n)\n", group, port);
            return false;
        }
    }

}
