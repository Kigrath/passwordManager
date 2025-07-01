package com.passwordmanager.storage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.passwordmanager.database.Password;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PasswordStore {
    private List<Password> passwords;
    private String filePath = "passwords.json"; // Speicherort der Datei
    private final Gson gson = new Gson();

    public PasswordStore() {
        this("passwords.json");
    }

    public PasswordStore(String filePath) {
        this.filePath = filePath;
        this.passwords = new ArrayList<>(); // Stelle sicher, dass passwords initialisiert wird
        loadFromFile(); // Gespeicherte Passwörter laden
    }

    public void addPassword(Password password) {
        if (getPasswordForSite(password.getSite()) != null) {
            return; // bereits vorhanden
        }
        passwords.add(password);
        saveToFile();
    }

    public void removePassword(String site) {
        passwords.removeIf(p -> p.getSite().equals(site));
        saveToFile();
    }

    public List<Password> getAllPasswords() {
        return passwords;
    }

    public Password getPasswordForSite(String site) {
        return passwords.stream()
                .filter(p -> p.getSite().equals(site))
                .findFirst()
                .orElse(null);
    }

    public void savePasswordsToFile(String path) {
        try (FileWriter writer = new FileWriter(path)) {
            gson.toJson(passwords, writer);
        } catch (IOException e) {
            System.err.println("Fehler beim Speichern der Passwörter: " + e.getMessage());
        }
    }

    public void loadPasswordsFromFile(String path) {
        passwords = new ArrayList<>();
        File file = new File(path);
        if (!file.exists()) {
            return;
        }

        try (FileReader reader = new FileReader(path)) {
            Type listType = new TypeToken<ArrayList<Password>>() {}.getType();
            List<Password> loadedPasswords = gson.fromJson(reader, listType);
            if (loadedPasswords != null) {
                passwords.addAll(loadedPasswords);
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Laden der Passwörter: " + e.getMessage());
        }
    }

    private void saveToFile() {
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(passwords, writer);
        } catch (IOException e) {
            System.err.println("Fehler beim Speichern der Passwörter: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        passwords = new ArrayList<>(); // Immer eine neue Liste initialisieren

        File file = new File(filePath);
        if (!file.exists()) {
            return; // Falls Datei nicht existiert, bleibt die Liste leer
        }

        try (FileReader reader = new FileReader(filePath)) {
            Type listType = new TypeToken<ArrayList<Password>>() {}.getType();
            List<Password> loadedPasswords = gson.fromJson(reader, listType);
            if (loadedPasswords != null) {
                passwords.addAll(loadedPasswords); // Falls Daten geladen wurden, hinzufügen
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Laden der Passwörter: " + e.getMessage());
        }
    }

}
