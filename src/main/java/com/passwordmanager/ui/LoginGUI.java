package com.passwordmanager.ui;

import com.passwordmanager.auth.UserAuth;

import javax.swing.*;
import java.awt.*;

/**
 * Simple login and registration window which derives the encryption key
 * after a successful login. On success the main GUI is opened.
 */
public class LoginGUI {
    public LoginGUI() {
        SwingUtilities.invokeLater(this::createAndShow);
    }

    private void createAndShow() {
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 200);

        GradientPanel panel = new GradientPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField userField = new JTextField(15);
        JPasswordField passField = new JPasswordField(15);
        JLabel statusLabel = new JLabel(" ");

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        loginBtn.addActionListener(e -> {
            String u = userField.getText();
            String p = new String(passField.getPassword());
            if (UserAuth.login(u, p)) {
                frame.dispose();
                new GUI();
            } else {
                statusLabel.setText("Login failed");
            }
        });

        registerBtn.addActionListener(e -> {
            String u = userField.getText();
            String p = new String(passField.getPassword());
            if (UserAuth.register(u, p)) {
                statusLabel.setText("Registered! Please login.");
            } else {
                statusLabel.setText("Registration failed");
            }
        });

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; panel.add(userField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; panel.add(passField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(loginBtn, gbc);
        gbc.gridx = 1; panel.add(registerBtn, gbc);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; panel.add(statusLabel, gbc);

        frame.setContentPane(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
