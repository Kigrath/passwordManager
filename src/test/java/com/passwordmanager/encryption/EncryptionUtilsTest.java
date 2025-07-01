package com.passwordmanager.encryption;

import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionUtilsTest {

    @Test
    void testEncryptAndDecrypt() throws Exception {
        String key = Base64.getEncoder().encodeToString(EncryptionUtils.hashSHA256("secret"));
        String text = "Hallo Welt";
        String encrypted = EncryptionUtils.encrypt(text, key);
        assertNotNull(encrypted);
        String decrypted = EncryptionUtils.decrypt(encrypted, key);
        assertEquals(text, decrypted);
    }

    @Test
    void testDecryptWithWrongKeyThrows() throws Exception {
        String key = Base64.getEncoder().encodeToString(EncryptionUtils.hashSHA256("secret"));
        String otherKey = Base64.getEncoder().encodeToString(EncryptionUtils.hashSHA256("wrong"));
        String encrypted = EncryptionUtils.encrypt("abc", key);
        assertThrows(Exception.class, () -> EncryptionUtils.decrypt(encrypted, otherKey));
    }

    @Test
    void testGenerateSecureRandomBytes() {
        byte[] bytes = EncryptionUtils.generateSecureRandomBytes(16);
        assertEquals(16, bytes.length);
    }

    @Test
    void testHashSHA256() {
        byte[] hash = EncryptionUtils.hashSHA256("test");
        assertEquals(32, hash.length);
    }
}
