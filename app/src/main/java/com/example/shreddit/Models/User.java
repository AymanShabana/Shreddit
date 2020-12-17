package com.example.shreddit.Models;

import com.google.firebase.firestore.Exclude;

public class User {
    private String email;
    private String username;
    private String bio;
    private String imageUrl;
    private String id;

    public User() {
    }

    public User(String email, String username, String bio, String imageUrl, String id) {
        this.email = email;
        this.username = username;
        this.bio = bio;
        this.imageUrl = imageUrl;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
