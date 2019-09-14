package com.example.xandi.amicer.models;

import com.example.xandi.amicer.objects.Tag;

import java.util.List;

public class Interest {

    private List<Tag> tags;
    private Boolean distanceOn = false;
    private Boolean ageOn = false;
    private int distanceEnd;
    private int ageStart;
    private int ageEnd;
    private int distanceStart;
    private String category;

    public Interest() {
    }

    public Interest(List<Tag> tags, Boolean distanceOn, Boolean ageOn, int distanceEnd, int ageStart, int ageEnd, int distanceStart, String category) {
        this.tags = tags;
        this.distanceOn = distanceOn;
        this.ageOn = ageOn;
        this.distanceEnd = distanceEnd;
        this.ageStart = ageStart;
        this.ageEnd = ageEnd;
        this.distanceStart = distanceStart;
        this.category = category;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Boolean getDistanceOn() {
        return distanceOn;
    }

    public void setDistanceOn(Boolean distanceOn) {
        this.distanceOn = distanceOn;
    }

    public Boolean getAgeOn() {
        return ageOn;
    }

    public void setAgeOn(Boolean ageOn) {
        this.ageOn = ageOn;
    }

    public int getDistanceEnd() {
        return distanceEnd;
    }

    public void setDistanceEnd(int distanceEnd) {
        this.distanceEnd = distanceEnd;
    }

    public int getAgeStart() {
        return ageStart;
    }

    public void setAgeStart(int ageStart) {
        this.ageStart = ageStart;
    }

    public int getAgeEnd() {
        return ageEnd;
    }

    public void setAgeEnd(int ageEnd) {
        this.ageEnd = ageEnd;
    }

    public int getDistanceStart() {
        return distanceStart;
    }

    public void setDistanceStart(int distanceStart) {
        this.distanceStart = distanceStart;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
