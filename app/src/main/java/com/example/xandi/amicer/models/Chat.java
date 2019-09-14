package com.example.xandi.amicer.models;

import java.util.List;

public class Chat {

    private String uid;
    private String conversationUid;
    private String lastMessage;
    private long timestamp;
    private List<User> participants;

    public Chat() {
    }

    public Chat(String uid, String conversationUid, String lastMessage, long timestamp, List<User> participants) {
        this.uid = uid;
        this.conversationUid = conversationUid;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.participants = participants;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getConversationUid() {
        return conversationUid;
    }

    public void setConversationUid(String conversationUid) {
        this.conversationUid = conversationUid;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }
}
