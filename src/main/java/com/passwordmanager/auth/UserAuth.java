package com.passwordmanager.auth;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.passwordmanager.encryption.EncryptionUtils;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;

public class UserAuth {
    private static final String FILE_PATH = "master_user.json";
    private static final Gson gson = new Gson();
    private static String encryptionKey;  // Abgeleiteter Schlüssel aus dem Master-Passwort

    public static boolean login() {
        Scanner scanner = new Scanner(System.in);

        if (!Files.exists(Paths.get(FILE_PATH))) {
            System.out.println("🔑 Kein Benutzer gefunden. Registrierung erforderlich.");
            return register();
        }

        System.out.print("🔐 Benutzername: ");
        String username = scanner.nextLine();

        System.out.print("🔐 Passwort: ");
        String password = scanner.nextLine();

        return login(username, password);
    }

    public static boolean login(String username, String password) {
        if (!Files.exists(Paths.get(FILE_PATH))) {
            return false;
        }

        try {
            String json = Files.readString(Paths.get(FILE_PATH));
            JsonObject credentials = gson.fromJson(json, JsonObject.class);

            String savedUsername = credentials.get("username").getAsString();
            String savedSalt = credentials.get("salt").getAsString();
            String savedHashedPassword = credentials.get("password").getAsString();

            String hashedPassword = hashPassword(password, savedSalt);

            if (username.equals(savedUsername) && hashedPassword.equals(savedHashedPassword)) {
                encryptionKey = deriveKeyFromMasterPassword(password, savedSalt);
                return true;
            }
        } catch (Exception ignored) {
        }

        return false;
    }

    private static boolean register() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("🔑 Neuer Benutzername: ");
        String username = scanner.nextLine();

        System.out.print("🔑 Neues Passwort: ");
        String password = scanner.nextLine();

        return register(username, password);
    }

    public static boolean register(String username, String password) {
        String salt = generateSalt();
        String hashedPassword = hashPassword(password, salt);

        JsonObject credentials = new JsonObject();
        credentials.addProperty("username", username);
        credentials.addProperty("password", hashedPassword);
        credentials.addProperty("salt", salt);

        try {
            Files.write(Paths.get(FILE_PATH), gson.toJson(credentials).getBytes(StandardCharsets.UTF_8));
            encryptionKey = deriveKeyFromMasterPassword(password, salt);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static String generateSalt() {
        return Base64.getEncoder().encodeToString(EncryptionUtils.generateSecureRandomBytes(16));
    }

    private static String hashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((password + salt).getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("❌ Fehler beim Hashen des Passworts.");
        }
    }

    private static String deriveKeyFromMasterPassword(String password, String salt) {
        return Base64.getEncoder().encodeToString(EncryptionUtils.hashSHA256(password + salt));
    }

    public static String getEncryptionKey() {
        return encryptionKey;
    }
}
