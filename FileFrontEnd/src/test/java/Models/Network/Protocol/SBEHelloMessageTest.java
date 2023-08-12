package Models.Network.Protocol;

import Models.Network.Protocol.SBE.SBEHelloMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SBEHelloMessageTest {

    @Test
    void invalidFormatProtocolTest() {
        SBEHelloMessage message = new SBEHelloMessage("HELMO-1", 8080);
        assertFalse(message.deserialize("HELLO Helmo-1 8081\r\n"));
        assertFalse(message.deserialize("HELLO Helmo.1 80908\r\n"));
        assertFalse(message.deserialize("HELLO Helmo.1 8081 "));
    }

    @Test
    void validFormatProtocolTest() {
        SBEHelloMessage sbeHelloMessage = new SBEHelloMessage(null, 8080);
        assertTrue(sbeHelloMessage.deserialize("HELLO Helmo.1 8081\r\n"));
        assertEquals("Helmo.1", sbeHelloMessage.getDomain());
        assertEquals(8081, sbeHelloMessage.getUnicastPort());

    }
}
