package com.passwordmanager.database;

import com.passwordmanager.auth.UserAuth;
import com.passwordmanager.encryption.EncryptionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class PasswordTest {

    private Password password;

    // Setup-Methode, die vor jedem Test aufgerufen wird
    @BeforeEach
    void setUp() throws Exception {
        String key = Base64.getEncoder().encodeToString(EncryptionUtils.hashSHA256("secret"));
        Field keyField = UserAuth.class.getDeclaredField("encryptionKey");
        keyField.setAccessible(true);
        keyField.set(null, key);
        password = new Password("example.com", "user1", "securepassword123");
    }

    // Test für den Konstruktor und die Getter
    @Test
    void testPasswordConstructorAndGetters() {
        assertEquals("example.com", password.getSite());
        assertEquals("user1", password.getUsername());
        assertEquals("securepassword123", password.getPassword());
    }

    // Test für die Setter-Methoden
    @Test
    void testPasswordSetters() {
        password.setSite("newsite.com");
        password.setUsername("newuser");
        password.setPassword("newpassword123");

        assertEquals("newsite.com", password.getSite());
        assertEquals("newuser", password.getUsername());
        assertEquals("newpassword123", password.getPassword());
    }

    // Test für die toString-Methode (wobei das Passwort nicht ausgegeben wird)
    @Test
    void testToString() {
        String expectedOutput = "Password{site='example.com', username='user1', password='***** (verschluesselt)'}";
        assertEquals(expectedOutput, password.toString());
    }
}
