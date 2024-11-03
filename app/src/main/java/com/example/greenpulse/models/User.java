package com.example.greenpulse.models;
import java.util.List;

public class User {
    private String email;
    private String password;
    private List<Field> fields; // List of Field objects
    private String dpUrl; // Display picture URL

    // Constructor
    public User(String email, String password, List<Field> fields, String dpUrl) {
        this.email = email;
        this.password = password;
        this.fields = fields;
        this.dpUrl = dpUrl;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public String getDpUrl() {
        return dpUrl;
    }

    public void setDpUrl(String dpUrl) {
        this.dpUrl = dpUrl;
    }
}
