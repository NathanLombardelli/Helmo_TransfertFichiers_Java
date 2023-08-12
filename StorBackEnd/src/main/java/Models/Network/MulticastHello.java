package Models.Network;

import Models.Network.Protocol.SBE.SBEHelloMessage;

public class MulticastHello implements Runnable {
    private MulticastPublisher multicastPublisher;
    private int intervalInSecond;
    private String domain;
    private int port;

    public MulticastHello(MulticastPublisher multicastPublisher, int intervalInSecond, String domain, int port) {
        this.multicastPublisher = multicastPublisher;
        this.intervalInSecond = intervalInSecond;
        this.domain = domain;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            System.out.printf("[MULTICAST] Hello Message Started (every %d seconds)\n", intervalInSecond);
            while (true) {
                multicastPublisher.publish(new SBEHelloMessage(domain, port).serialize());
                System.out.printf("[MULTICAST] Send => HELLO %s %d\n", domain, port);
                Thread.sleep(intervalInSecond * 1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
