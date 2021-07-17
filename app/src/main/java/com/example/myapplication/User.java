package com.example.myapplication;

public class User {
    public String email;
    public String password;
    public String id;
    public String invoice;

    public User(String email, String password, String id, String invoice) {
        this.email = email;
        this.password = password;
        this.id = id;
        this.invoice = invoice;
    }

    public User() {
        this.email = "";
        this.password = "";
        this.id = "";
        this.invoice = "";
    }
}
