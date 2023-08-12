package Models.Network.Protocol;

import Models.Config;
import Models.IO.Interfaces.IConfigRepository;
import Models.IO.Json.JsonConfigRepository;
import Models.Network.Protocol.Client.ClientSignUpMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClientSignUpMessageTest {

    @Test
    void invalidFormatProtocolTest() {
        String path = "src/main/resources/FFEConfigExample.json";
        Config config = new Config();
        IConfigRepository configRepository = new JsonConfigRepository();
        configRepository.load(path, config);
        ClientSignUpMessage message = new ClientSignUpMessage();
        assertFalse(message.deserialize("SIGNUPbap71monMotDePasse5\r\n"));
        assertFalse(message.deserialize("SIGNUP  monMotDePasse5"));
        assertFalse(message.deserialize("SIGNIN bap71 monMotDePasse5\r\n"));


    }
    //TODO
    //Le test crée un user mais NullPointerExeption !
    @Test
    void validFormatProtocolTest() {
        String path = "src/main/resources/FFEConfigExample.json";
        Config config = new Config();
        IConfigRepository configRepository = new JsonConfigRepository();
        configRepository.load(path, config);
        ClientSignUpMessage message = new ClientSignUpMessage();
        assertTrue(message.deserialize("SIGNUP LebonBaptiste71 monMotDePasse5\r\n"));
    }

}
