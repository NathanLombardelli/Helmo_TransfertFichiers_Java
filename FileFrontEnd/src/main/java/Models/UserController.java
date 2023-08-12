package Models;

import App.Program;
import Models.Cryptography.Aes;
import Models.Cryptography.BCryptHash;
import Models.IO.Interfaces.IConfigRepository;
import Models.Network.ClientHandler;
import Models.Network.Protocol.Event.*;
import Models.Network.Protocol.FFE.FFEFileListResultMessage;
import Models.Network.Protocol.FFE.FFESet2FAResultMessage;
import Models.Network.Protocol.FFE.FFESignResultMessage;
import Models.Network.Protocol.FFE.FFESignWith2FARequestedMessage;
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class UserController implements ClientSignInEvent, ClientSignUpEvent, ClientSignOutEvent, ClientFileListEvent, ClientSet2FAEvent, ClientSignInWith2FAEvent {

    private IConfigRepository repository;
    private FileController fileController;
    private Config config;
    private Aes aes;
    private BCryptHash bcrypt = new BCryptHash(12);

    public UserController(IConfigRepository repository, Config config, Aes aes, FileController fileController) {
        this.repository = repository;
        this.config = config;
        this.aes = aes;
        this.fileController = fileController;
    }

    public User login(String login, String password) {
        if (!repository.load(Program.configPath, config))
            return null;

        User user = findUserByLogin(login);
        return user != null && bcrypt.verifyHash(password, user.getHashPassword()) ? user : null;
    }

    public User register(String login, String password) {
        if (!repository.load(Program.configPath, config))
            return null;

        if (findUserByLogin(login) != null)
            return null;

        User user = new User(login, bcrypt.hash(password), aes.getKey(aes.createKey()));
        config.addUser(user);
        repository.save(Program.configPath, config);
        return user;
    }

    private User findUserByLogin(String login) {
        List<User> users = config.getUsers();

        for (User user : users) {
            if (login.equals(user.getLogin())) {
                return user;
            }
        }
        return null;
    }

    private String generateSecretKeyFor2FA() { // à stocker dans la classe user + crypt
        SecretGenerator secretGenerator = new DefaultSecretGenerator(16);
        return secretGenerator.generate();
    }

    private Boolean checkSecretKey2FA(String secretKey, String inputKey){ // result tested (inputKey = 6digits)
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator(HashingAlgorithm.SHA1, 6);
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        return verifier.isValidCode(secretKey, inputKey);
    }

    public void updateUserInRepository(User user){
        config.updateUser(user);
        repository.save(Program.configPath, config);
    }

    @Override
    public void onClientSignInEvent(ClientHandler client, String login, String password) {

        if (client == null)
            return;

        User user = login(login, password);
        if (user != null) {
            if (user.getKey2FA() == null) { // Si 2FA pas activée
                client.setConnectedUser(user);
                System.out.printf("[ClientSignInMessage] %s successfully login\n", user.getLogin());
                client.sendMessage(new FFESignResultMessage(true));
            } else { // Si 2FA activée
                client.sendMessage(new FFESignWith2FARequestedMessage()); // 'SIGN_2FA_NEEDED' => demander 6digits / exemple à la reception de ce message une fenetre s'ouvre avec un champ et un bouton login qui lui envoie 'SIGNINWITH2FA' + login + mdp + 6digits
            }
        } else {
            client.sendMessage(new FFESignResultMessage(false));
        }
    }

    @Override
    public void onClientSignUpEvent(ClientHandler client, String login, String password) {

        if (client == null)
            return;

        User user = register(login, password);

        if (user != null) {
            System.out.printf("[ClientSignUpMessage] User %s has been added to configuration\n", login);
            client.setConnectedUser(user);
            client.sendMessage(new FFESignResultMessage(true));
        } else {
            System.out.printf("[ClientSignUpMessage] User already registered ! (%s)\n", login);
            client.sendMessage(new FFESignResultMessage(false));
            client.disconnect();
        }
    }

    @Override
    public void onClientSignOutEvent(ClientHandler client) {
        if (client == null)
            return;
        System.out.println("[ClientSignOutMessage] Goodbye Sir");
        client.disconnect();
    }

    @Override
    public void onClientFileListEvent(ClientHandler client) {
        if (client == null && client.getConnectedUser() == null)
            return;
        List<File> files = fileController.getFiles(client.getConnectedUser().getLogin());
        client.sendMessage(new FFEFileListResultMessage(files));
    }

    @Override
    public void onClientSet2FAEvent(ClientHandler client, String login, String password) {

        if (client == null)
            return;

        try {
            User user = login(login, password);
            if (user != null && user.getKey2FA() == null) {
                String key2FA = generateSecretKeyFor2FA();
                user.setKeyIV2FA(aes.createIV());
                user.setKey2FA(aes.encrypt(key2FA.getBytes(StandardCharsets.UTF_8), aes.getKey(user.getAesKey()), user.getkeyIV2FA(true)));
                System.out.printf("[ClientSet2FAMessage] %s successfully set 2FA Auth\n", user.getLogin());
                updateUserInRepository(user);
                client.sendMessage(new FFESet2FAResultMessage(true, key2FA)); // envoie la clé au client
            } else {
                client.sendMessage(new FFESet2FAResultMessage(false, null));
            }
        } catch (Exception ex) {
            System.out.println("[ClientSet2FAMessage] Error in the AES coding of the 2FA key");
        }
    }

    @Override
    public void onClientSignInWith2FAEvent(ClientHandler client, String login, String password, String digits) {
        if (client == null)
            return;

        try {
            User user = login(login, password);
            if (user != null) {
                if (checkSecretKey2FA(new String(aes.decrypt(user.getKey2FA(true), aes.getKey(user.getAesKey()), user.getkeyIV2FA(true)), StandardCharsets.UTF_8), digits)){
                    client.setConnectedUser(user);
                    System.out.printf("[ClientSignInWith2FAMessage] %s successfully login with 2FA Auth\n", user.getLogin());
                    client.sendMessage(new FFESignResultMessage(true));
                } else {
                    client.sendMessage(new FFESignResultMessage(false));
                }
            } else {
                client.sendMessage(new FFESignResultMessage(false));
            }
        } catch (Exception ex) {
            System.out.println("[ClientSignInWith2FAMessage] Error in the AES coding of the 2FA key");
        }
    }
}
