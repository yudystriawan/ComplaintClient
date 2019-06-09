package com.example.complaintclient.models;

public class Instance {

    private Integer id;
    private String name;

    public Instance(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Instance() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
