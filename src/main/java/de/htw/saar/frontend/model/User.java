package de.htw.saar.frontend.model;

import java.io.Serializable;

public class User implements Serializable
{
    private String token;
    private String username;
    private String email;
    private int id;

    public User(String token, String username, String email, int id) {
        this.token = token;
        this.username = username;
        this.email = email;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }
}
