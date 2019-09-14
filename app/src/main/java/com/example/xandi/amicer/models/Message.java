package com.example.xandi.amicer.models;

public class Message {

    private String messageUid;
    private String senderUid;
    private String text;
    private String time;
    private String receiverUid;

    public Message() {
    }

    public Message(String messageUid, String senderUid, String text, String time, String receiverUid) {
        this.messageUid = messageUid;
        this.senderUid = senderUid;
        this.text = text;
        this.time = time;
        this.receiverUid = receiverUid;
    }

    public String getMessageUid() {
        return messageUid;
    }

    public void setMessageUid(String messageUid) {
        this.messageUid = messageUid;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public void setReceiverUid(String receiverUid) {
        this.receiverUid = receiverUid;
    }
}
