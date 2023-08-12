package Models.Cryptography;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptHash {

    private final int logRounds;

    public BCryptHash(int logRounds) {
        this.logRounds = logRounds;
    }

    public String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(logRounds));
    }

    public boolean verifyHash(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }
}
