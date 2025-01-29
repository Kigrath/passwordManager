package com.passwordmanager.database;

public class Password {
    private String site;
    private String username;
    private String password;

    // Konstruktor
    public Password(String site, String username, String password) {
        this.site = site;
        this.username = username;
        this.password = password;
    }

    // Getter und Setter
    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Für Debugging: Optionale Ausgabe ohne Passwort
    @Override
    public String toString() {
        return "Password{" +
                "site='" + site + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
