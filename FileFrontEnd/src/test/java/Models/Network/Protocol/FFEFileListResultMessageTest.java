package Models.Network.Protocol;

import Models.File;
import Models.Network.Protocol.FFE.FFEFileListResultMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FFEFileListResultMessageTest {

    @Test
    void validFormatProtocolTest() {
        FFEFileListResultMessage client = new FFEFileListResultMessage();
        client.deserialize("FILES toto.png!16 Gaming.jpg!24 Burger.jfif!201");
        assertTrue(client.getListFiles().size()==3);
        File file =client.getListFiles().get(1);
        assertEquals("Gaming.jpg", file.getFilename());
        assertEquals(24, file.getFileSize());
        file =client.getListFiles().get(0);
        assertEquals("toto.png", file.getFilename());
    }

    @Test
    void invalidFormatProtocolTest() {
        FFEFileListResultMessage client = new FFEFileListResultMessage();
        client.deserialize("FILES toto.png!16 Gaming.jpg!24 Burger.jfif!201");
        assertFalse(client.getListFiles().size()==2);
        File file =client.getListFiles().get(1);
        assertNotEquals("jpg", file.getFilename());
        assertNotEquals(48, file.getFileSize());
        file =client.getListFiles().get(0);
        assertNotEquals("toto", file.getFilename());
    }
}
