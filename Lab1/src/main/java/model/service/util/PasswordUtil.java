package model.service.util;

import model.service.util.exception.PasswordTransformError;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;


public class PasswordUtil {
    private static final String SECRET_KEY = System.getenv("PASSWORD_SECRET_KEY");
    private static final String SALT = System.getenv("PASSWORD_SALT");

    private static final int IV_LENGTH = 16;

    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 256;

    private static final String SECRET_KEY_FACTORY = "PBKDF2WithHmacSHA256";

    private static final String SECRET_KEY_ALGORITHM = "AES";
    private static final String CIPHER = "AES/CBC/PKCS5Padding";


    public static String encryptPassword(String password) {
        try {
            Cipher cipher = createCipher(Cipher.ENCRYPT_MODE);
            return Base64.getEncoder()
                    .encodeToString(cipher.doFinal(password.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new PasswordTransformError("Failed to encrypt password", e);
        }
    }

    public static String decryptPassword(String encryptedPassword) {
        try {
            Cipher cipher = createCipher(Cipher.DECRYPT_MODE);
            return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedPassword)));
        } catch (Exception e) {
            throw new PasswordTransformError("Failed to decrypt password", e);
        }
    }

    private static Cipher createCipher(int mode) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException,
            InvalidAlgorithmParameterException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(CIPHER);
        cipher.init(mode, createSecretKeySpec(), new IvParameterSpec(byteArrayOf(IV_LENGTH, (byte) 0)));
        return cipher;
    }

    private static SecretKeySpec createSecretKeySpec() throws InvalidKeySpecException, NoSuchAlgorithmException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY);
        KeySpec keySpec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), ITERATION_COUNT, KEY_LENGTH);
        SecretKey tmpSecretKey = factory.generateSecret(keySpec);

        return new SecretKeySpec(tmpSecretKey.getEncoded(), SECRET_KEY_ALGORITHM);
    }

    private static byte[] byteArrayOf(int length, byte value) {
        byte[] array = new byte[length];
        Arrays.fill(array, value);
        return array;
    }
}
