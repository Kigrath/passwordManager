package com.passwordmanager.database;

import com.passwordmanager.encryption.EncryptionUtils;

public class Password {
    private String site;
    private String username;
    private String encryptedPassword;
    private static final String KEY = "MeinGeheimerKey!!"; // Schlüssel (MUSS 16 Zeichen lang sein!)

    public Password(String site, String username, String password) {
        this.site = site;
        this.username = username;
        try {
            this.encryptedPassword = EncryptionUtils.encrypt(password, KEY);
        } catch (Exception e) {
            throw new RuntimeException("Fehler bei der Verschlüsselung!", e);
        }
    }

    public String getSite() {
        return site;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        try {
            return EncryptionUtils.decrypt(encryptedPassword, KEY);
        } catch (Exception e) {
            throw new RuntimeException("Fehler bei der Entschlüsselung!", e);
        }
    }

    @Override
    public String toString() {
        return "Password{" +
                "site='" + site + '\'' +
                ", username='" + username + '\'' +
                ", password='***** (verschlüsselt)' " + // Passwort nicht direkt anzeigen
                '}';
    }
}
