package com.passwordmanager.utils;

import java.security.SecureRandom;

public class PasswordGenerator {
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+<>?";

    public static String generate(int length, boolean useUpper, boolean useLower, boolean useDigits, boolean useSpecial) {
        if (length < 4) {
            throw new IllegalArgumentException("Passwort muss mindestens 4 Zeichen lang sein!");
        }

        String characterPool = "";
        if (useUpper) characterPool += UPPERCASE;
        if (useLower) characterPool += LOWERCASE;
        if (useDigits) characterPool += DIGITS;
        if (useSpecial) characterPool += SPECIAL_CHARACTERS;

        if (characterPool.isEmpty()) {
            throw new IllegalArgumentException("Mindestens eine Zeichenart muss ausgewählt sein!");
        }

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characterPool.length());
            password.append(characterPool.charAt(index));
        }

        return password.toString();
    }
}
