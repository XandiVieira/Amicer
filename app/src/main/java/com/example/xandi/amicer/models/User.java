package com.example.xandi.amicer.models;

import com.example.xandi.amicer.objects.Location;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String uid;
    private String name;
    private String email;
    private int age;
    private List<Interest> interests = new ArrayList<Interest>();
    private List<String> photos;
    private String description;
    private Location location;
    private List<String> chatsUid;

    public User() {
    }

    public User(String uid, String name, String email, int age, List<Interest> interests, List<String> photos, String description, Location location, List<String> chatsUid) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.age = age;
        this.interests = interests;
        this.photos = photos;
        this.description = description;
        this.location = location;
        this.chatsUid = chatsUid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Interest> getInterests() {
        return interests;
    }

    public void setInterests(List<Interest> interests) {
        this.interests = interests;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<String> getChatsUid() {
        return chatsUid;
    }

    public void setChatsUid(List<String> chatsUid) {
        this.chatsUid = chatsUid;
    }
}
