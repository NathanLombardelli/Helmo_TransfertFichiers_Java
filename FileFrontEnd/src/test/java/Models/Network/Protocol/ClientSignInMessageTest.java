package Models.Network.Protocol;

import Models.Network.Protocol.Client.ClientSignInMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClientSignInMessageTest {
    @Test
    void invalidFormatProtocolTest() {

        ClientSignInMessage message = new ClientSignInMessage();
        assertFalse(message.deserialize("SIGNUP LebonBaptiste71 monMotDePasse5"));
        assertFalse(message.deserialize("SIGNIN  monMotDePasse5"));
        assertFalse(message.deserialize("SIGNIN ok monMotDePasse5\r\n"));


    }
    @Test
    void validFormatProtocolTest() {

        ClientSignInMessage message = new ClientSignInMessage();
        assertTrue(message.deserialize("SIGNIN LebonBaptiste71 monMotDePasse5"));
    }
}
