package blitzsh.app.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EncryptionUtilsTest {
    @Test
    void encryptAndDecrypt() {
        String password = "test password";
        String plainText = "test string";

        String encrypted = EncryptionUtils.encryptString(plainText, password);
        String result = EncryptionUtils.decryptString(encrypted, password);

        Assertions.assertEquals(plainText, result);
    }
}