package Models.Network.Protocol.SBE;

import Models.BaseRegex;
import Models.Network.Protocol.MessageProtocol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SBERetrieveResultMessage extends MessageProtocol {

    private String hashFileName;
    private String hashFileContent;
    private long size;

    private boolean success;

    private final String protocolRegexOk = "^RETRIEVE_OK" + BaseRegex.bl + BaseRegex.file_info + BaseRegex.line + "$";
    private final String protocolRegexError = "^RETRIEVE_ERROR" + BaseRegex.line + "$";

    public SBERetrieveResultMessage() {
    }

    public SBERetrieveResultMessage(boolean success) {
        this.success = success;
    }

    public SBERetrieveResultMessage(String hashFileName, String hashFileContent, long size) {
        this.success = true;
        this.hashFileName = hashFileName;
        this.hashFileContent = hashFileContent;
        this.size = size;
    }

    @Override
    public boolean deserialize(String message) {

        if (message.matches(protocolRegexError)) {
            success = false;
            return true;
        }

        if (!message.matches(protocolRegexOk))
            return false;

        Pattern pattern = Pattern.compile(protocolRegexOk);
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            hashFileName = matcher.group(1);
            size = Integer.parseInt(matcher.group(2));
            hashFileContent = matcher.group(3);
        }

        return true;
    }

    @Override
    public String serialize() {
        return success ? String.format("RETRIEVE_OK %s %d %s\r\n", hashFileName, size, hashFileContent) : "RETRIEVE_ERROR\r\n";
    }
}
