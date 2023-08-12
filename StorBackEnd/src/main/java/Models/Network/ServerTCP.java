package Models.Network;


import Models.Network.Protocol.ProtocolParser;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTCP implements Runnable {

    private int port;

    private boolean isListening = false;
    private ServerSocket socket;
    private Socket client;

    private ProtocolParser parser;
    private FileReceiver fileReceiver;

    public ServerTCP(int port, ProtocolParser parser, FileReceiver fileReceiver) {
        this.port = port;
        this.parser = parser;
        this.fileReceiver = fileReceiver;
        init();
    }

    private void init() {
        try {
            this.socket = new ServerSocket(this.port);
            isListening = true;
            System.out.println("[TCP Server] Server started");
        } catch (IOException e) {
            System.out.println("[IOE] Impossible to create the TCP Server");
        }
    }

    private void listen() {
        try {
            System.out.println("[TCP Server] Waiting clients ...");
            while (isListening) {
                this.client = socket.accept();
                ClientHandler client = new ClientHandler(this.client, parser, fileReceiver);
                new Thread(client).start();
                System.out.println("[TCP Server] New Client Connected !");
            }
        } catch (IOException e) {
            System.out.println("[IOE] Error during listening (TCP Server)");
            isListening = false;
        }
    }

    @Override
    public void run() {
        listen();
    }
}
