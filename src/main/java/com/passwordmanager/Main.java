package com.passwordmanager;

import com.passwordmanager.ui.GUI;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}
