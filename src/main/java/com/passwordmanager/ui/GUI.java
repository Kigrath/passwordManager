package com.passwordmanager.ui;

import com.passwordmanager.database.Password;
import com.passwordmanager.storage.PasswordStore;
import com.passwordmanager.utils.PasswordGenerator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class GUI {
    private final PasswordStore passwordStore;
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;

    public GUI() {
        passwordStore = new PasswordStore();
        SwingUtilities.invokeLater(this::createAndShow);
    }

    private void createAndShow() {
        frame = new JFrame("Password Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        GradientPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"Site", "Username"}, 0);
        table = new JTable(tableModel);
        refreshTable();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        JButton addBtn = new JButton("Add");
        JButton delBtn = new JButton("Delete");
        JButton copyUser = new JButton("Copy Username");
        JButton copyPass = new JButton("Copy Password");

        addBtn.addActionListener(e -> showAddDialog());
        delBtn.addActionListener(e -> deleteSelected());
        copyUser.addActionListener(e -> copySelectedUsername());
        copyPass.addActionListener(e -> copySelectedPassword());

        buttonPanel.add(addBtn);
        buttonPanel.add(delBtn);
        buttonPanel.add(copyUser);
        buttonPanel.add(copyPass);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Password p : passwordStore.getAllPasswords()) {
            tableModel.addRow(new Object[]{p.getSite(), p.getUsername()});
        }
    }

    private void showAddDialog() {
        JTextField siteField = new JTextField();
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();

        JCheckBox upper = new JCheckBox("A-Z", true);
        JCheckBox lower = new JCheckBox("a-z", true);
        JCheckBox digits = new JCheckBox("0-9", true);
        JCheckBox special = new JCheckBox("!@#", true);
        JSpinner lengthSpinner = new JSpinner(new SpinnerNumberModel(12, 4, 64, 1));

        JButton generateBtn = new JButton("Generate");
        generateBtn.addActionListener(e -> {
            String generated = PasswordGenerator.generate(
                    (int) lengthSpinner.getValue(),
                    upper.isSelected(),
                    lower.isSelected(),
                    digits.isSelected(),
                    special.isSelected());
            passField.setText(generated);
        });

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Site:"));
        panel.add(siteField);
        panel.add(new JLabel("Username:"));
        panel.add(userField);
        panel.add(new JLabel("Password:"));
        panel.add(passField);
        panel.add(generateBtn);
        JPanel options = new JPanel();
        options.add(upper);
        options.add(lower);
        options.add(digits);
        options.add(special);
        options.add(new JLabel("Len:"));
        options.add(lengthSpinner);
        panel.add(new JLabel());
        panel.add(options);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Add Password",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            Password pw = new Password(siteField.getText(), userField.getText(),
                    new String(passField.getPassword()));
            passwordStore.addPassword(pw);
            refreshTable();
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String site = (String) tableModel.getValueAt(row, 0);
            passwordStore.removePassword(site);
            refreshTable();
        }
    }

    private void copySelectedUsername() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String username = (String) tableModel.getValueAt(row, 1);
            copyToClipboard(username);
        }
    }

    private void copySelectedPassword() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String site = (String) tableModel.getValueAt(row, 0);
            Password pw = passwordStore.getPasswordForSite(site);
            if (pw != null) {
                copyToClipboard(pw.getPassword());
            }
        }
    }

    private void copyToClipboard(String text) {
        StringSelection selection = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
    }

    private static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int w = getWidth();
            int h = getHeight();
            Color start = new Color(173, 216, 230); // light blue
            Color end = new Color(192, 132, 252);   // purple
            GradientPaint gp = new GradientPaint(0, 0, start, w, h, end);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
        }
    }
}
