package com.example.complaintclient.models;

import java.sql.Timestamp;

public class Comment {

    private Integer id;
    private String body;
    private Complaint complaint;
    private User user;
    private Timestamp created_at;
    private Timestamp updated_at;

    public Comment(Integer id, String body, Complaint complaint, User user, Timestamp created_at, Timestamp updated_at) {
        this.id = id;
        this.body = body;
        this.complaint = complaint;
        this.user = user;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Comment(String body) {
        this.body = body;
    }

    public Integer getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public Complaint getComplaint() {
        return complaint;
    }

    public User getUser() {
        return user;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }
}
