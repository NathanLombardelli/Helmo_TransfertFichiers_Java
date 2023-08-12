package Models.Network;

import Models.Network.Protocol.MessageProtocol;
import Models.Network.Protocol.ProtocolParser;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

public class    StorProcessor implements Runnable {

    private InetAddress ip;
    private int port;

    private boolean readFile = false;

    private BufferedReader fromServer;
    private PrintWriter toServer;
    private Socket socket;

    private ProtocolParser parser;
    private FileReceiver fileReceiver;

    public StorProcessor(InetAddress ip, int port, ProtocolParser parser, FileReceiver fileReceiver) {
        this.ip = ip;
        this.port = port;
        this.parser = parser;
        this.fileReceiver = fileReceiver;
        connect();
    }

    private void connect() {
        try {
            socket = new Socket(ip, port);
            fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
            toServer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")), true);
        } catch (UnknownHostException e) {
            disconnect();
        } catch (IOException e) {
            disconnect();
        }
    }

    public void disconnect(){
        try {
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            System.out.println("[IOE] Impossible to disconnect");
        }
    }

    public boolean sendMessage(String message) {
        if (socket != null && !socket.isClosed()) {
            toServer.write(message);
            toServer.flush();
            return true;
        }
        return false;
    }

    public boolean sendMessage(MessageProtocol message) {
        return sendMessage(message.serialize());
    }

    public void sendFile(String path) throws IOException {
        FileSender fileSender = new FileSender(path);
        fileSender.sendFile(socket.getOutputStream());
    }

    public void sendFile(String filename, String path) throws IOException {
        FileSender fileSender = new FileSender(path);
        fileSender.sendFile(filename, socket.getOutputStream());
    }

    private String readLine(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if(line != null && line.length() > 2 && line.startsWith("\uFEFF"))
            return line.substring("\uFEFF".length());
        return line;
    }

    public void readFile(String filename, long fileSize) throws IOException {
        readFile = true;
        fileReceiver.receiveFile(socket.getInputStream(), filename, fileSize);
        readFile = false;
    }

    private void listen() {
        try {
            while (!socket.isClosed()) {
                if (!readFile) {
                    String received = readLine(fromServer);
                    if (received == null) {
                        disconnect();
                    } else {
                        System.out.println("[StorBackEnd] Message: " + received);
                        handleMessage(received);
                    }
                }
            }
        } catch(Exception e) {
            disconnect();
        }
    }

    private void handleMessage(String received) {
        MessageProtocol message = parser.parse(received);

        if (message == null) return;

        message.deserialize(received);
    }

    @Override
    public void run() {
        listen();
    }
}
