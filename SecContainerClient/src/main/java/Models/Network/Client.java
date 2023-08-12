package Models.Network;

import Models.Network.Protocol.MessageProtocol;
import Models.Network.Protocol.ProtocolParser;

import javax.net.ssl.*;
import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.*;
import java.security.cert.CertificateException;

public class Client implements Runnable {

    private InetAddress ip;
    private int port;
    private boolean isTls;

    private boolean readFile = false;

    private BufferedReader fromServer;
    private PrintWriter toServer;
    private Socket socket;
    private ProtocolParser parser;

    public Client(InetAddress ip, int port, boolean isTls, ProtocolParser parser) throws ConnectException {
        this.ip = ip;
        this.port = port;
        this.isTls = isTls;
        this.parser = parser;
        if (isTls)
            connectWithTls();
        else
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

    private void connectWithTls() throws ConnectException {
        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream("src/main/resources/ffe.labo.swilabus.com.p12"), "labo2022".toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, "labo2022".toCharArray());

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ks);

            SSLContext sc = SSLContext.getInstance("TLS");
            TrustManager[] trustManagers = tmf.getTrustManagers();
            sc.init(kmf.getKeyManagers(), trustManagers, null);

            SSLSocketFactory ssf = sc.getSocketFactory();
            SSLSocket s = (SSLSocket) ssf.createSocket(ip, port);
            s.startHandshake();

            socket = s;
            fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
            toServer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")), true);
        } catch(ConnectException e) {
            throw new ConnectException();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
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

    public boolean readFile(String filename, long fileSize, String path) throws IOException {
        readFile = true;
        FileReceiver fileReceiver = new FileReceiver();
        boolean success = fileReceiver.receiveFile(filename, fileSize, socket.getInputStream(), path);
        readFile = false;
        return success;
    }

    public void sendFile(String filename, String path) throws IOException {
        FileSender fileSender = new FileSender(filename, socket.getOutputStream(), path);
        new Thread(fileSender).start();
    }

    private void handleMessage(String received) {
        MessageProtocol message = parser.parse(received);
        if (message != null) {
            message.deserialize(received);
        }
    }

    public boolean sendMessage(MessageProtocol message) {
        return sendMessage(message.serialize());
    }

    private String readLine(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if (line != null && line.length() > 2 && line.startsWith("\uFEFF"))
            return line.substring("\uFEFF".length());
        return line;
    }

    private void listen() {
        try {
            while (!socket.isClosed()) {
                if (!readFile) {
                    String message = readLine(fromServer);
                    if (message == null) {
                        disconnect();
                    } else {
                        System.out.println(message);
                        handleMessage(message);
                    }
                }
                Thread.sleep(10);
            }
        } catch (Exception e) {
            disconnect();
        }
    }

    @Override
    public void run() {
        listen();
    }

    public boolean isConnected() {
        return !socket.isClosed();
    }
}
