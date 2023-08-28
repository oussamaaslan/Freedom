package com.azlan.freedom.models;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class User implements Serializable {
    public String uid;
    public String name;
    public String photoUrl;
    @SuppressWarnings("WeakerAccess")
    public String email;
    @Exclude
    public boolean isAuthenticated;
    @Exclude
    public
    boolean isNew;
    @Exclude
    public
    boolean isCreated;

    @Exclude
    public
    String provider;

    public User() {}

    public User(String uid, String name, String email,String provider,String photoUrl) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.provider=provider;
        this.photoUrl=photoUrl;
    }
}