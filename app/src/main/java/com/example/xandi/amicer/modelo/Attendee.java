package com.example.xandi.amicer.modelo;

public class Attendee {

    public Attendee(){}

    private String userUID;
    private String groupUID;

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getGroupUID() {
        return groupUID;
    }

    public void setGroupUID(String groupUID) {
        this.groupUID = groupUID;
    }
}
