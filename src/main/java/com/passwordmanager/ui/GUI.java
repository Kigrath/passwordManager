package com.passwordmanager.ui;

import com.passwordmanager.auth.UserAuth;
import com.passwordmanager.database.Password;
import com.passwordmanager.storage.PasswordStore;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GUI {
    private JFrame frame;
    private PasswordStore store;
    private DefaultListModel<Password> listModel;

    public GUI() {
        store = new PasswordStore();
        setLookAndFeel();
        SwingUtilities.invokeLater(this::showLogin);
    }

    private void setLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}
    }

    private void showLogin() {
        frame = new JFrame("Password Manager - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Benutzername:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Passwort:");
        JPasswordField passField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Registrieren");

        gbc.gridx = 0; gbc.gridy = 0; panel.add(userLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(userField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(passLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(passField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(loginButton, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(registerButton, gbc);

        loginButton.addActionListener(e -> {
            if (UserAuth.login(userField.getText(), new String(passField.getPassword()))) {
                JOptionPane.showMessageDialog(frame, "Login erfolgreich!");
                frame.dispose();
                showMainUI();
            } else {
                JOptionPane.showMessageDialog(frame, "Login fehlgeschlagen", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerButton.addActionListener(e -> {
            if (UserAuth.register(userField.getText(), new String(passField.getPassword()))) {
                JOptionPane.showMessageDialog(frame, "Registrierung erfolgreich!");
            } else {
                JOptionPane.showMessageDialog(frame, "Registrierung fehlgeschlagen", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    private void showMainUI() {
        frame = new JFrame("Password Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        listModel = new DefaultListModel<>();
        refreshList();
        JList<Password> list = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(list);

        JButton addButton = new JButton("Hinzufügen");
        JButton deleteButton = new JButton("Löschen");

        addButton.addActionListener(e -> showAddDialog());
        deleteButton.addActionListener(e -> {
            Password selected = list.getSelectedValue();
            if (selected != null) {
                store.removePassword(selected.getSite());
                refreshList();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void showAddDialog() {
        JTextField siteField = new JTextField();
        JTextField userField = new JTextField();
        JTextField passField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0,1));
        panel.add(new JLabel("Seite:"));
        panel.add(siteField);
        panel.add(new JLabel("Benutzername:"));
        panel.add(userField);
        panel.add(new JLabel("Passwort:"));
        panel.add(passField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Passwort hinzufügen", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Password p = new Password(siteField.getText(), userField.getText(), passField.getText());
            store.addPassword(p);
            refreshList();
        }
    }

    private void refreshList() {
        if (listModel == null) return;
        listModel.clear();
        for (Password p : store.getAllPasswords()) {
            listModel.addElement(p);
        }
    }
}
