package server.security;

import org.mindrot.jbcrypt.BCrypt;
import server.etc.Constants;

public class Password {
    public static String hashPassword(String plaintextPwd) {
        String salt = BCrypt.gensalt();
        String pwdHash = BCrypt.hashpw(plaintextPwd, salt);
        return pwdHash;
    }

    public static boolean isCorrectPassword(String plainTextPwd, String storedHash) {
        if (!storedHash.startsWith("$" + Constants.PASSWORD_HASH_BLOWFISH + "$")) {
            throw new IllegalArgumentException("Hash: " + storedHash + " was not derived from blowfish encryption");
        }
        return BCrypt.checkpw(plainTextPwd, storedHash);
    }
}
