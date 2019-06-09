package com.example.complaintclient.models;

public class Message {

    private int id;
    private String subject;
    private String text;

    public Message(int id, String subject, String text) {
        this.id = id;
        this.subject = subject;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }


}
