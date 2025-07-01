package com.passwordmanager.storage;

import com.passwordmanager.database.Password;
import com.passwordmanager.auth.UserAuth;
import com.passwordmanager.encryption.EncryptionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

class PasswordStoreTest {

    private PasswordStore passwordStore;
    private Password password1;
    private Password password2;
    private String tempFilePath;

    // Setup-Methode, die vor jedem Test ausgeführt wird
    @BeforeEach
    void setUp() throws Exception {
        try {
            Path tempFile = Files.createTempFile("passwords", ".json");
            tempFilePath = tempFile.toString();
            passwordStore = new PasswordStore(tempFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String key = java.util.Base64.getEncoder()
                .encodeToString(EncryptionUtils.hashSHA256("secret"));
        java.lang.reflect.Field keyField = UserAuth.class.getDeclaredField("encryptionKey");
        keyField.setAccessible(true);
        keyField.set(null, key);
        password1 = new Password("example.com", "user1", "securepassword123");
        password2 = new Password("testsite.com", "user2", "password456");
    }

    // Test für das Hinzufügen eines Passworts
    @Test
    void testAddPassword() {
        passwordStore.addPassword(password1);
        assertEquals(1, passwordStore.getAllPasswords().size());
        assertEquals(password1, passwordStore.getPasswordForSite("example.com"));
    }

    // Test für das Hinzufügen eines Passworts, das bereits existiert
    @Test
    void testAddPasswordAlreadyExists() {
        passwordStore.addPassword(password1);
        passwordStore.addPassword(password1); // Sollte nicht hinzugefügt werden
        assertEquals(1, passwordStore.getAllPasswords().size());
    }

    // Test für das Löschen eines Passworts
    @Test
    void testRemovePassword() {
        passwordStore.addPassword(password1);
        passwordStore.removePassword("example.com");
        assertNull(passwordStore.getPasswordForSite("example.com"));
        assertEquals(0, passwordStore.getAllPasswords().size());
    }

    // Test für das Abrufen aller Passwörter
    @Test
    void testGetAllPasswords() {
        passwordStore.addPassword(password1);
        passwordStore.addPassword(password2);
        List<Password> passwords = passwordStore.getAllPasswords();
        assertEquals(2, passwords.size());
        assertTrue(passwords.contains(password1));
        assertTrue(passwords.contains(password2));
    }

    // Test für das Speichern und Laden von Passwörtern
    @Test
    void testSaveAndLoadPasswords() {
        passwordStore.addPassword(password1);
        passwordStore.addPassword(password2);

        // Speichern in eine temporäre Datei
        passwordStore.savePasswordsToFile(tempFilePath);

        // Neues PasswordStore-Objekt erstellen und Passwörter laden
        PasswordStore newPasswordStore = new PasswordStore(tempFilePath);
        newPasswordStore.loadPasswordsFromFile(tempFilePath);

        assertEquals(2, newPasswordStore.getAllPasswords().size());
        assertNotNull(newPasswordStore.getPasswordForSite("example.com"));
        assertNotNull(newPasswordStore.getPasswordForSite("testsite.com"));
    }

    // Cleanup-Methode nach jedem Test
    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        // Löschen der temporären Datei
        try {
            Files.deleteIfExists(Paths.get(tempFilePath));
            java.lang.reflect.Field keyField = UserAuth.class.getDeclaredField("encryptionKey");
            keyField.setAccessible(true);
            keyField.set(null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
