package Models.Network.Protocol;

import Models.Network.Protocol.FFE.FFESignResultMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FFESignResultMessageTest {

    @Test
    void invalidFormatProtocolTest() {
        FFESignResultMessage message = new FFESignResultMessage();
        assertFalse(message.deserialize("SIGN_K"));
        assertFalse(message.deserialize("SIGNUP\n\r"));
        assertFalse(message.deserialize("ERROR\n\r"));


    }
    //TODO
    //si on rajoute \r\n alors le test ne marche plus !
    @Test
    void validFormatProtocolTest() {
        FFESignResultMessage message = new FFESignResultMessage();
        assertTrue(message.deserialize("SIGN_OK"));
        assertTrue(message.deserialize("SIGN_ERROR"));
    }
}
