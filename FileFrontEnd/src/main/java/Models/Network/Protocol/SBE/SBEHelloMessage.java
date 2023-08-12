package Models.Network.Protocol.SBE;

import Models.BaseRegex;
import Models.Network.FileReceiver;
import Models.Network.Protocol.MessageProtocol;
import Models.Network.Protocol.ProtocolParser;
import Models.Network.StorProcessor;

import java.net.InetAddress;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SBEHelloMessage extends MessageProtocol {

    private InetAddress ip;
    private String domain;
    private int unicastPort;
    private final String protocolRegex = "^HELLO" + BaseRegex.bl + BaseRegex.domain + BaseRegex.bl + BaseRegex.port + BaseRegex.line +"$";

    private Map<String, StorProcessor> processors;
    private ProtocolParser parser;
    private FileReceiver fileReceiver;

    public SBEHelloMessage(Map<String, StorProcessor> processors, ProtocolParser parser, FileReceiver fileReceiver) {
        this.processors = processors;
        this.parser = parser;
        this.fileReceiver = fileReceiver;
    }

    public SBEHelloMessage(String domain, int unicastPort) {
        this.domain = domain;
        this.unicastPort = unicastPort;
    }

    @Override
    public boolean deserialize(String message) {

        if (!message.matches(protocolRegex))
            return false;

        Pattern pattern = Pattern.compile(protocolRegex);
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            this.domain = matcher.group(1);
            this.unicastPort = Integer.parseInt(matcher.group(2));
        }

        addStorProcessor();

        return true;
    }

    @Override
    public String serialize() {
        return String.format("HELLO %s %s\r\n", this.domain, this.unicastPort);
    }

    public String getDomain() {
        return domain;
    }

    public int getUnicastPort() {
        return unicastPort;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    private void addStorProcessor() {
        if (ip != null && !processors.containsKey(domain)) {
            StorProcessor processor = new StorProcessor(ip, unicastPort, parser, fileReceiver);
            processors.put(domain, processor);
            new Thread(processor).start();
            System.out.printf("[SBE] Connected to %s on port %d\n", domain, unicastPort);
        }
    }
}
