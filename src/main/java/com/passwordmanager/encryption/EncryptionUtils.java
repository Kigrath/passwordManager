package com.passwordmanager.encryption;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionUtils {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

    public static String encrypt(String plainText, String key) throws Exception {
        byte[] ivBytes = generateSecureRandomBytes(16);
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), ALGORITHM);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

        return Base64.getEncoder().encodeToString(ivBytes) + ":" + Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String cipherText, String key) throws Exception {
        String[] parts = cipherText.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Ungültiges verschlüsseltes Format");
        }

        IvParameterSpec iv = new IvParameterSpec(Base64.getDecoder().decode(parts[0]));
        byte[] encryptedBytes = Base64.getDecoder().decode(parts[1]);
        SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), ALGORITHM);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes);
    }

    public static byte[] generateSecureRandomBytes(int length) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return bytes;
    }

    public static byte[] hashSHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(input.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Hashen mit SHA-256.");
        }
    }
}
