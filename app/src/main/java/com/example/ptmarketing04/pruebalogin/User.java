package com.example.ptmarketing04.pruebalogin;

/**
 * Created by ptmarketing04 on 18/04/2017.
 */

public class User {
    protected int id;
    protected String name,email,pass;

    public User(){}

    public User(int id, String name, String email, String pass) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.pass = pass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
