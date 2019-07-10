package de.htw.saar.frontend.model;

import java.io.Serializable;

public class User implements Serializable
{
    private String token;
    private String username;
    private String email;

    public User(String token, String username, String email) {
        this.token = token;
        this.username = username;
        this.email = email;
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
}
