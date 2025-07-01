package com.passwordmanager.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordGeneratorTest {

    @Test
    void testGenerateValidPassword() {
        String pw = PasswordGenerator.generate(10, true, true, true, true);
        assertEquals(10, pw.length());
    }

    @Test
    void testGenerateTooShortThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> PasswordGenerator.generate(3, true, true, false, false));
    }

    @Test
    void testGenerateWithNoCharTypesThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> PasswordGenerator.generate(10, false, false, false, false));
    }
}
