package com.passwordmanager;

import com.passwordmanager.auth.UserAuth;
import com.passwordmanager.ui.CLI;
import com.passwordmanager.ui.GUI;

public class Main {
    public static void main(String[] args) {
        if (UserAuth.login()) {
            CLI cli = new CLI();
            cli.start();
            // Start graphical user interface
            new GUI();
        } else {
            System.out.println("⛔ Programm wird beendet.");
        }
    }
}
