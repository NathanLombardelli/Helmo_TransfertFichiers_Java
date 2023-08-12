package Models.Network.Protocol.Client;

import Models.BaseRegex;
import Models.Network.Protocol.MessageProtocol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientGetFileResultMessage extends MessageProtocol {

    private boolean success;

    private String filename;
    private long fileSize;

    private final String protocolRegexSuccess = "^(GETFILE_OK)" + BaseRegex.bl + "(" + BaseRegex.filename + ")" + BaseRegex.bl + "(" + BaseRegex.size + ")" + BaseRegex.line + "$";
    private final String protocolRegexError = "^(GETFILE_ERROR)" + BaseRegex.line + "$";

    public ClientGetFileResultMessage(boolean success) {
        this.success = success;
    }

    public ClientGetFileResultMessage(String filename, long fileSize) {
        this.success = true;
        this.filename = filename;
        this.fileSize = fileSize;
    }

    @Override
    public boolean deserialize(String message) {

        if (message.matches(protocolRegexError)) {
            success = false;
            return true;
        }

        if (!message.matches(protocolRegexSuccess)) {
            Pattern pattern = Pattern.compile(protocolRegexSuccess);
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                String status = matcher.group(1);
                success = "GETFILE_OK".equals(status);
                filename = matcher.group(2);
                fileSize = Long.parseLong(matcher.group(3));
            }
        }
        return true;
    }

    @Override
    public String serialize() {
        return success ? String.format("GETFILE_OK %s %d\r\n", filename, fileSize) : "GETFILE_ERROR\r\n";
    }
}
