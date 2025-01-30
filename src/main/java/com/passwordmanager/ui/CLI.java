package com.passwordmanager.ui;

import com.passwordmanager.database.Password;
import com.passwordmanager.storage.PasswordStore;

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
        System.out.println("Befehle: add, delete, list, show, exit");

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
        System.out.print("Seite: ");
        String site = scanner.nextLine();
        System.out.print("Benutzername: ");
        String username = scanner.nextLine();
        System.out.print("Passwort: ");
        String password = scanner.nextLine();

        passwordStore.addPassword(new Password(site, username, password));
        System.out.println("Passwort hinzugefügt.");
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

}
