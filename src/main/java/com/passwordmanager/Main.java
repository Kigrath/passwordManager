package com.passwordmanager;

import com.passwordmanager.auth.UserAuth;
import com.passwordmanager.ui.CLI;

public class Main {
    public static void main(String[] args) {
        if (UserAuth.login()) {
            CLI cli = new CLI();
            cli.start();
        } else {
            System.out.println("⛔ Programm wird beendet.");
        }
    }
}
