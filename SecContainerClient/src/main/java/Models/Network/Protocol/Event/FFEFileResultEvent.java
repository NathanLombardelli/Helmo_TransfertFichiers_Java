package Models.Network.Protocol.Event;

import Models.File;

import java.util.List;

public interface FFEFileResultEvent {
    void onFFEFileResult(List<File> files);
}
