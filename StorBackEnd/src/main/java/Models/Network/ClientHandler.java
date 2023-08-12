package Models.Network;

import Models.Network.Protocol.MessageProtocol;
import Models.Network.Protocol.ProtocolParser;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class ClientHandler implements Runnable {

    private Socket client;
    private BufferedReader fromClient;
    private PrintWriter toClient;

    private ProtocolParser parser;
    private boolean readFile;
    private FileReceiver fileReceiver;

    public ClientHandler(Socket client, ProtocolParser parser, FileReceiver fileReceiver) {
        this.client = client;
        this.parser = parser;
        this.fileReceiver = fileReceiver;
        init();
    }

    private void init() {
        try {
            fromClient = new BufferedReader(new InputStreamReader(this.client.getInputStream(), Charset.forName("UTF-8")));
            toClient = new PrintWriter(new OutputStreamWriter(this.client.getOutputStream(), Charset.forName("UTF-8")), true);
        } catch (IOException e) {
            System.out.println("[IOE] Client Thread IO error");
        }
    }

    private String readLine(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if(line != null && line.length() > 2 && line.startsWith("\uFEFF"))
            return line.substring("\uFEFF".length());
        return line;
    }

    public boolean readFile(String filename, long fileSize) throws IOException {
        readFile = true;
        Boolean success = fileReceiver.receiveFile(client.getInputStream(), filename, fileSize);
        readFile = false;
        return success;
    }

    public void sendMessage(String message) {
        toClient.write(message);
        toClient.flush();
    }

    public void sendMessage(MessageProtocol message) {
        sendMessage(message.serialize());
    }

    public boolean sendFile(String filename, String path) throws IOException {
        FileSender fileSender = new FileSender(path);
        return fileSender.sendFile(filename, client.getOutputStream());
    }

    private void listen() {
        try {
            while(!client.isClosed()) {
                if (!readFile) {
                    String received = readLine(fromClient);
                    if (received == null) {
                        disconnect();
                        System.out.println("[TCP Server] Client disconnected");
                    } else {
                        System.out.printf("[TCP] Message from client => %s\n", received);
                        handleMessage(received);
                    }
                }
            }
        } catch(Exception ex) {
            disconnect();
        }
    }

    private void handleMessage(String received) {
        MessageProtocol protocol = parser.parse(received);

        if (protocol == null) return;

        protocol.setClient(this);
        protocol.deserialize(received);
    }

    public void disconnect(){
        try {
            client.close();
        } catch (IOException e) {
            System.out.println("[IOE] Impossible to close the connection");
        }
    }

    @Override
    public void run() {
        listen();
    }
}
