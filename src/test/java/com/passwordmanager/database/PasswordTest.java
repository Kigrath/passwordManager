package com.passwordmanager.database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordTest {

    private Password password;

    // Setup-Methode, die vor jedem Test aufgerufen wird
    @BeforeEach
    void setUp() {
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
        String expectedOutput = "Password{site='example.com', username='user1'}";
        assertEquals(expectedOutput, password.toString());
    }
}
