package Models.Network.Protocol.Client;

import Models.BaseRegex;
import Models.Network.Protocol.Event.ClientGetFileResultEvent;
import Models.Network.Protocol.Event.FFESaveFileResultEvent;
import Models.Network.Protocol.MessageProtocol;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientGetFileResult extends MessageProtocol {

    private boolean success;

    private String filename;
    private long fileSize;

    private final String protocolRegexSuccess = "^(GETFILE_OK)" + BaseRegex.bl + "(" + BaseRegex.filename + ")" + BaseRegex.bl + "(" + BaseRegex.size + ")" + BaseRegex.line + "$";
    private final String protocolRegexError = "^(GETFILE_ERROR)" + BaseRegex.line + "$";

    private List<ClientGetFileResultEvent> listeners = new ArrayList<>();

    public ClientGetFileResult() {
    }

    public ClientGetFileResult(boolean success) {
        this.success = success;
    }

    public ClientGetFileResult(String filename, long fileSize) {
        this.success = true;
        this.filename = filename;
        this.fileSize = fileSize;
    }

    @Override
    public boolean deserialize(String message) {

        if (message.matches(protocolRegexError)) {
            success = false;
            fire();
            return true;
        }

        if (message.matches(protocolRegexSuccess)) {
            Pattern pattern = Pattern.compile(protocolRegexSuccess);
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                String status = matcher.group(1);
                success = status.equals("GETFILE_OK");
                filename = matcher.group(2);
                fileSize = Long.parseLong(matcher.group(3));
            }
        }
        fire();
        return true;
    }

    @Override
    public String serialize() {
        return success ? String.format("GETFILE_OK %s %d\r\n", filename, fileSize) : "GETFILE_ERROR\r\n";
    }

    public void subscribe(ClientGetFileResultEvent listener) {
        listeners.add(listener);
    }

    public void unsubscribe(ClientGetFileResultEvent listener) {
        listeners.remove(listener);
    }

    private void fire() {
        for (ClientGetFileResultEvent listener : listeners) {
            listener.onFFESaveFileResult(success, filename, fileSize);
        }
    }
}
