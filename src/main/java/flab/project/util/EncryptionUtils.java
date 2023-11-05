package flab.project.util;

import flab.project.exception.ExceptionCode;
import flab.project.exception.KakaoException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptionUtils {
    private final static String salt = "flab";
    public static String generateEncryptedKey(String word) {
        String input = word + salt;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes("UTF-8"));
            String encryptedKey = Base64.getEncoder().encodeToString(digest);
            return encryptedKey;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new KakaoException(ExceptionCode.SERVER_ERROR);
        }
    }
}
