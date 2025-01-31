package com.passwordmanager.ui;

import com.passwordmanager.database.Password;
import com.passwordmanager.storage.PasswordStore;
import com.passwordmanager.utils.PasswordGenerator;

import java.util.Scanner;

public class CLI {
    private PasswordStore passwordStore;
    private Scanner scanner;

    public CLI() {
        this.passwordStore = new PasswordStore();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Willkommen zum Password Manager!");
        System.out.println("Befehle: add, delete, list, show, generate, exit");

        boolean running = true;
        while (running) {
            System.out.print("> ");
            String command = scanner.nextLine();

            switch (command) {
                case "add":
                    handleAddPassword();
                    break;
                case "delete":
                    handleDeletePassword();
                    break;
                case "list":
                    handleListPasswords();
                    break;
                case "show":
                    handleShowPassword();
                    break;
                case "generate":
                    handleGeneratePassword();
                    break;
                case "exit":
                    running = false;
                    System.out.println("Programm beendet.");
                    break;
                default:
                    System.out.println("Ungültiger Befehl. Versuche es erneut.");
            }
        }
    }

    private void handleAddPassword() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Seite: ");
        String site = scanner.nextLine();

        System.out.print("Benutzername: ");
        String username = scanner.nextLine();

        String password = "";

        System.out.print("Eigenes Passwort eingeben oder generieren lassen? (e/g): ");
        String choice = scanner.nextLine().trim().toLowerCase();

        if (choice.equals("g")) {
            System.out.print("Passwortlänge eingeben: ");
            int length = scanner.nextInt();
            scanner.nextLine();  // Zeilenumbruch entfernen

            System.out.print("Großbuchstaben verwenden? (y/n): ");
            boolean useUpper = scanner.nextLine().trim().equalsIgnoreCase("y");

            System.out.print("Kleinbuchstaben verwenden? (y/n): ");
            boolean useLower = scanner.nextLine().trim().equalsIgnoreCase("y");

            System.out.print("Zahlen verwenden? (y/n): ");
            boolean useDigits = scanner.nextLine().trim().equalsIgnoreCase("y");

            System.out.print("Sonderzeichen verwenden? (y/n): ");
            boolean useSpecial = scanner.nextLine().trim().equalsIgnoreCase("y");

            try {
                password = PasswordGenerator.generate(length, useUpper, useLower, useDigits, useSpecial);
                System.out.println("Generiertes Passwort: " + password);
                System.out.print("Willst du dieses Passwort verwenden? (y/n): ");
                String confirm = scanner.nextLine().trim().toLowerCase();
                if (!confirm.equals("y")) {
                    System.out.print("Gib dein eigenes Passwort ein: ");
                    password = scanner.nextLine();
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Fehler: " + e.getMessage());
                return;  // Abbruch, falls ungültige Eingaben
            }
        } else {
            System.out.print("Passwort: ");
            password = scanner.nextLine();
        }

        Password newPassword = new Password(site, username, password);
        passwordStore.addPassword(newPassword);
        System.out.println("Passwort gespeichert!");
    }


    private void handleDeletePassword() {
        System.out.print("Seite zum Löschen: ");
        String site = scanner.nextLine();

        passwordStore.removePassword(site);
        System.out.println("Passwort gelöscht.");
    }

    private void handleListPasswords() {
        System.out.println("Gespeicherte Passwörter:");
        passwordStore.getAllPasswords().forEach(System.out::println);
    }
    private void handleShowPassword() {
        System.out.print("Seite: ");
        String site = scanner.nextLine();

        Password foundPassword = passwordStore.getAllPasswords().stream()
                .filter(p -> p.getSite().equals(site))
                .findFirst()
                .orElse(null);

        if (foundPassword != null) {
            System.out.println("Benutzername: " + foundPassword.getUsername());
            System.out.println("Passwort: " + foundPassword.getPassword());
        } else {
            System.out.println("Kein Passwort für diese Seite gefunden.");
        }
    }

    private void handleGeneratePassword() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Passwortlänge eingeben: ");
        int length = scanner.nextInt();
        scanner.nextLine();  // Zeilenumbruch entfernen

        System.out.print("Großbuchstaben verwenden? (y/n): ");
        boolean useUpper = scanner.nextLine().trim().equalsIgnoreCase("y");

        System.out.print("Kleinbuchstaben verwenden? (y/n): ");
        boolean useLower = scanner.nextLine().trim().equalsIgnoreCase("y");

        System.out.print("Zahlen verwenden? (y/n): ");
        boolean useDigits = scanner.nextLine().trim().equalsIgnoreCase("y");

        System.out.print("Sonderzeichen verwenden? (y/n): ");
        boolean useSpecial = scanner.nextLine().trim().equalsIgnoreCase("y");

        try {
            String generatedPassword = PasswordGenerator.generate(length, useUpper, useLower, useDigits, useSpecial);
            System.out.println("Generiertes Passwort: " + generatedPassword);
        } catch (IllegalArgumentException e) {
            System.out.println("Fehler: " + e.getMessage());
        }
    }


}
