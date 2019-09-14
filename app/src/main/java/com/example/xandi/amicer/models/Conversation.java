package com.example.xandi.amicer.models;

import java.util.List;

public class Conversation {

    private String uid;
    private List<Message> messages;

    public Conversation() {
    }

    public Conversation(String uid, List<Message> messages) {
        this.uid = uid;
        this.messages = messages;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
