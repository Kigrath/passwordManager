package com.passwordmanager.auth;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class UserAuthTest {

    @BeforeEach
    void setUp() throws Exception {
        Files.deleteIfExists(Path.of("master_user.json"));
        Field keyField = UserAuth.class.getDeclaredField("encryptionKey");
        keyField.setAccessible(true);
        keyField.set(null, null);
    }

    @AfterEach
    void tearDown() throws Exception {
        Files.deleteIfExists(Path.of("master_user.json"));
    }

    @Test
    void testRegisterAndLogin() {
        assertTrue(UserAuth.register("tester", "secret"));
        assertTrue(UserAuth.login("tester", "secret"));
        assertNotNull(UserAuth.getEncryptionKey());
    }

    @Test
    void testLoginWrongPassword() {
        UserAuth.register("tester", "secret");
        assertFalse(UserAuth.login("tester", "wrong"));
    }

    @Test
    void testLoginWithoutUserFile() {
        assertFalse(UserAuth.login("none", "nopass"));
    }
}
