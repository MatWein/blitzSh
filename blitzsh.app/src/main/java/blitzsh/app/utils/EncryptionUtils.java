package blitzsh.app.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class EncryptionUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptionUtils.class);

    private static final String ALGORITHM = "AES";
    private static final String ENCODING = "UTF-8";
    private static final int AES_PASSWORD_LENGTH = 16;

    public static String encryptString(String source, String password) {
        if (StringUtils.isEmpty(source)) {
            return null;
        }

        try {
            byte[] utf8Content = source.getBytes(ENCODING);
            byte[] passwordBytes = generatePasswordBytes(password);
            SecretKeySpec key = new SecretKeySpec(passwordBytes, ALGORITHM);

            Cipher encryptionCipher = Cipher.getInstance(ALGORITHM);
            encryptionCipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedSource = encryptionCipher.doFinal(utf8Content);

            return Base64.encodeBase64URLSafeString(encryptedSource);
        } catch (Throwable e) {
            String message = "Error on encryption of string.";
            LOGGER.error(message, e);
            throw new RuntimeException(message, e);
        }
    }

    public static String decryptString(String source, String password) {
        if (StringUtils.isBlank(source) || StringUtils.isBlank(password)) {
            return null;
        }

        try {
            byte[] decryptedSource = Base64.decodeBase64(source);
            byte[] passwordBytes = generatePasswordBytes(password);
            SecretKeySpec key = new SecretKeySpec(passwordBytes, ALGORITHM);

            Cipher decryptionCipher = Cipher.getInstance(ALGORITHM);
            decryptionCipher.init(Cipher.DECRYPT_MODE, key);
            byte[] utf8Content = decryptionCipher.doFinal(decryptedSource);

            return new String(utf8Content, ENCODING);
        } catch (Throwable e) {
            String message = "Error on decryption of string.";
            LOGGER.error(message, e);
            return null;
        }
    }

    private static byte[] generatePasswordBytes(String password) {
        byte[] passwordAsBytes = getBytesFromString(DigestUtils.md5Hex(password));
        return Arrays.copyOf(passwordAsBytes, AES_PASSWORD_LENGTH);
    }

    private static byte[] getBytesFromString(String value) {
        try {
            return value.getBytes(ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(String.format("Error on generating password. Encoding '%s' unknown.", ENCODING), e);
        }
    }
}
