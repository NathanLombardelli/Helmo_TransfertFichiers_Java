package Models.Network;

import Models.Network.Protocol.ProtocolParser;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;

public class ServerTCP implements Runnable {

    private int port;

    private boolean isListening = false;
    private ServerSocket socket;
    private Socket client;
    private boolean isTls;


    private ProtocolParser parser;
    private FileReceiver fileReceiver;

    public ServerTCP(int port, boolean tls, ProtocolParser parser, FileReceiver fileReceiver) {
        this.port = port;
        this.parser = parser;
        this.fileReceiver = fileReceiver;
        this.isTls = tls;

        if (isTls)
            initWithTls();
        else
            initWithoutTls();
    }

    private void initWithTls() {

        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            // Certificate + Password
            ks.load(new FileInputStream("src/main/resources/ffe.labo.swilabus.com.p12"), "labo2022".toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, "labo2022".toCharArray());

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ks);

            SSLContext sc = SSLContext.getInstance("TLS");
            TrustManager[] trustManagers = tmf.getTrustManagers();
            sc.init(kmf.getKeyManagers(), trustManagers, null);

            SSLServerSocketFactory ssf = sc.getServerSocketFactory();
            this.socket = ssf.createServerSocket(port);
            this.isListening = true;
            System.out.printf("[TCP] Server Tls created on port %d\n", port);

        } catch (IOException | NoSuchAlgorithmException | CertificateException FileNotFound) {
            FileNotFound.printStackTrace();
            System.out.println("Certificat SSL not found");
        } catch (KeyStoreException keyStore) {
            keyStore.printStackTrace();
            System.out.println("ERROR KeyStore");
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

    }

    private void initWithoutTls() {
        try {
            this.socket = new ServerSocket(port);
            isListening = true;
            System.out.printf("[TCP] Server created on port %d\n", port);
        } catch (IOException e) {
            System.out.println("[IOE] Impossible to create the TCP Server");
        }
    }

    private void listen() {
        try {
            while (isListening) {
                client = socket.accept();
                ClientHandler clientHandler = new ClientHandler(client, parser, fileReceiver);
                new Thread(clientHandler).start();
                clientHandler.sendMessage("\r\n"); // Fix that allows to receive messages on the helmo network only (Not necessary on any other network)
                System.out.println("[TCP] New Client Connected !");
            }
        } catch (IOException ex) {
            System.out.println("[IOE] Error during listening (TCP Server)");
            isListening = false;
        }
    }

    @Override
    public void run() {
        listen();
    }


}
