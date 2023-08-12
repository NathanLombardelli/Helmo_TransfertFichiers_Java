package Models.Network.Protocol.FFE;

import Models.BaseRegex;
import Models.File;
import Models.Network.Protocol.Event.FFEFileResultEvent;
import Models.Network.Protocol.Event.FFESignResultEvent;
import Models.Network.Protocol.MessageProtocol;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FFEFileListResultMessage extends MessageProtocol {

    private final String files = "(" + BaseRegex.bl + BaseRegex.filename + "!" + BaseRegex.size + ")";
    private final String protocolRegex = "^FILES" + files + "{0,50}" + BaseRegex.line + "$";
    private List<File> listFiles;

    private List<FFEFileResultEvent> listeners;

    public FFEFileListResultMessage() {
        listeners = new ArrayList<>();
    }

    public FFEFileListResultMessage(List<File> listFiles) {
        this.listFiles = listFiles;
    }

    @Override
    public boolean deserialize(String message) {
        if (!message.matches(protocolRegex))
            return false;

        listFiles = new ArrayList();

        Pattern pattern = Pattern.compile(protocolRegex);
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            Pattern patternFile = Pattern.compile("(" + BaseRegex.bl + BaseRegex.filename + "!" + BaseRegex.size + ")");
            Matcher matcherFile = patternFile.matcher(message);

            while (matcherFile.find()) {
                String[] tabFiles = matcherFile.group(0).split("!", 2);
                listFiles.add(new File(tabFiles[0].trim(), Long.parseLong(tabFiles[1])));
            }
            fire();
            return true;
        }

        return false;
    }

    @Override
    public String serialize() {
        String message ="FILES";
        for (File file:listFiles){
            message += " " + file.getFilename() + "!" + file.getFileSize();
        }
        return  message + "\r\n";
    }

    public void subscribe(FFEFileResultEvent listener) {
        listeners.add(listener);
    }

    public void unsubscribe(FFEFileResultEvent listener) {
        listeners.remove(listener);
    }

    private void fire() {
        for (FFEFileResultEvent listener : listeners) {
            listener.onFFEFileResult(listFiles);
        }
    }
}
