package com.passwordmanager.database;

import com.passwordmanager.auth.UserAuth;
import com.passwordmanager.encryption.EncryptionUtils;

public class Password {
    private String site;
    private String username;
    private String encryptedPassword;

    public Password(String site, String username, String password) {
        this.site = site;
        this.username = username;
        try {
            this.encryptedPassword = EncryptionUtils.encrypt(password, UserAuth.getEncryptionKey());
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

    public void setSite(String site) {
        this.site = site;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        try {
            this.encryptedPassword = EncryptionUtils.encrypt(password, UserAuth.getEncryptionKey());
        } catch (Exception e) {
            throw new RuntimeException("Fehler bei der Verschlüsselung!", e);
        }
    }

    public String getPassword() {
        try {
            return EncryptionUtils.decrypt(encryptedPassword, UserAuth.getEncryptionKey());
        } catch (Exception e) {
            throw new RuntimeException("Fehler bei der Entschlüsselung!", e);
        }
    }

    @Override
    public String toString() {
        return "Password{" +
                "site='" + site + '\'' +
                ", username='" + username + '\'' +
                ", password='***** (verschlüsselt)'" +
                '}';
    }
}
