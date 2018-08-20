package com.example.xandi.amicer.modelo;

public class Message {

    private String userUid;
    private String text;
    private String name;
    private String photoUrl;
    private String time;

    public Message() {
    }

    public Message(String text, String name, String photoUrl, String time, String userUid) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
        this.time = time;
        this.userUid = userUid;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
