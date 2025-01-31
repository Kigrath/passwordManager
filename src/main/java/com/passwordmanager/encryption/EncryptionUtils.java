package com.passwordmanager.encryption;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.security.SecureRandom;

public class EncryptionUtils {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

    // Erstellt einen zufälligen 16-Byte-Initialisierungsvektor (IV)
    private static String generateIV() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return Base64.getEncoder().encodeToString(iv);
    }

    // Korrigiert die Schlüssellänge auf 16 Zeichen für AES-128
    private static String fixKeyLength(String key) {
        if (key.length() < 16) {
            return String.format("%-16s", key);
        } else if (key.length() > 16) {
            return key.substring(0, 16);
        }
        return key;
    }

    public static String encrypt(String plainText, String key) throws Exception {
        String ivString = generateIV();
        IvParameterSpec iv = new IvParameterSpec(Base64.getDecoder().decode(ivString));
        SecretKeySpec secretKey = new SecretKeySpec(fixKeyLength(key).getBytes(), ALGORITHM);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

        return ivString + ":" + Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String cipherText, String key) throws Exception {
        String[] parts = cipherText.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Ungültiges verschlüsseltes Format");
        }

        IvParameterSpec iv = new IvParameterSpec(Base64.getDecoder().decode(parts[0]));
        byte[] encryptedBytes = Base64.getDecoder().decode(parts[1]);
        SecretKeySpec secretKey = new SecretKeySpec(fixKeyLength(key).getBytes(), ALGORITHM);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes);
    }
}
