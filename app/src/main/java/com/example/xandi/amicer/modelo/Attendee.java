package com.example.xandi.amicer.modelo;

import java.util.List;

public class Attendee {

    public Attendee(){}

    private List<String> userUID;
    private String groupUID;

    public List<String> getUserUID() {
        return userUID;
    }

    public void setUserUID(List<String> userUID) {
        this.userUID = userUID;
    }

    public String getGroupUID() {
        return groupUID;
    }

    public void setGroupUID(String groupUID) {
        this.groupUID = groupUID;
    }
}
