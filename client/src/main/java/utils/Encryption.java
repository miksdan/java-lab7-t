package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class Encryption {
    public static final Logger logger = LoggerFactory.getLogger(Encryption.class);

    public static String encryptPassword(String password) {
        String sha = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.reset();
            messageDigest.update(password.getBytes(StandardCharsets.UTF_8));
            sha = format(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
        }
        return sha;
    }

    private static String format(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        String res = formatter.toString();
        formatter.close();
        return res;
    }
}
