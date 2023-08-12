package Models.Network.Protocol;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class ProtocolParser {

    private Map<String, MessageProtocol> protocols;

    public ProtocolParser(Map<String, MessageProtocol> protocols) {
        this.protocols = protocols;
    }

    public MessageProtocol parse(String message) {
        String firstElement = getFirstProtocolElement(message);
        return this.protocols.get(firstElement);
    }

    private String getFirstProtocolElement(final String protocol) {
        Pattern pattern = compile("^\\w*");
        Matcher matcher = pattern.matcher(protocol);
        return matcher.find() ? matcher.group() : "DEFAULT";
    }
}
