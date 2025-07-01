package com.passwordmanager;

import com.passwordmanager.auth.UserAuth;
import com.passwordmanager.ui.GUI;

public class Main {
    public static void main(String[] args) {
        if (UserAuth.login()) {
            // Start graphical user interface
            new GUI();
        } else {
            System.out.println("⛔ Programm wird beendet.");
        }
    }
}
