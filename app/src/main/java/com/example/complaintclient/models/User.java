package com.example.complaintclient.models;

import java.sql.Timestamp;

public class User {

    private Integer id;
    private Role role;
    private Instance instance;
    private String name;
    private String email;
    private String username;
    private String password;


    private Timestamp created_at;
    private Timestamp updated_at;

    public User() {
    }

    public User(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public Role getRole() {
        return role;
    }

    public Instance getInstance() {
        return instance;
    }
}
