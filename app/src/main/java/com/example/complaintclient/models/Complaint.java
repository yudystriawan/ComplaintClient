package com.example.complaintclient.models;

import java.sql.Timestamp;
import java.util.List;

public class Complaint {

    private Integer id;
    private String topic;
    private String body;
    private String category;
    private Instance instance;
    private boolean negative;
    private double percent;
    private User user;
    private List<Comment> comments;
    private Timestamp created_at;
    private Timestamp updated_at;

    public Complaint() {
    }

    public Complaint(String topic, String body, String category) {
        this.topic = topic;
        this.body = body;
        this.category = category;
    }

    public Complaint(Boolean negative, String instance) {
        Instance newInstance = new Instance();
        newInstance.setName(instance);

        this.negative = negative;
        this.instance = newInstance;
    }

    public Integer getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public String getBody() {
        return body;
    }

    public String getCategory() {
        return category;
    }

    public Instance getInstance() {
        return instance;
    }

    public boolean isNegative() {
        return negative;
    }

    public double getPercent() {
        return percent;
    }

    public User getUser() {
        return user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }
}
