package com.passwordmanager.storage;

import com.passwordmanager.database.Password;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PasswordStore {
    private List<Password> passwords;

    public PasswordStore() {
        this.passwords = new ArrayList<>();
    }

    // 1. Passwort hinzufügen
    public void addPassword(Password password) {
        if (getPasswordForSite(password.getSite()) != null) {
            System.out.println("Passwort für diese Seite existiert bereits!");
            return;
        }
        passwords.add(password);
    }

    // 2. Passwort löschen
    public void removePassword(String site) {
        passwords.removeIf(p -> p.getSite().equals(site));
    }

    // 3. Alle Passwörter abrufen
    public List<Password> getAllPasswords() {
        return passwords;
    }

    // 4. Passwort für eine bestimmte Seite abrufen
    public Password getPasswordForSite(String site) {
        for (Password password : passwords) {
            if (password.getSite().equals(site)) {
                return password;
            }
        }
        return null;
    }

    // 5. Passwörter in Datei speichern
    public void savePasswordsToFile(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            Gson gson = new Gson();
            gson.toJson(passwords, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 6. Passwörter aus Datei laden
    public void loadPasswordsFromFile(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Password>>() {}.getType();
            passwords = gson.fromJson(reader, listType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
