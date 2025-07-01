package com.passwordmanager;

import com.passwordmanager.ui.LoginGUI;

public class Main {
    public static void main(String[] args) {
        // Show graphical login window. The main GUI will start after
        // successful authentication inside LoginGUI.
        new LoginGUI();
    }
}
