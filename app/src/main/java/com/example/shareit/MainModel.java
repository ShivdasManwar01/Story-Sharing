package com.example.shareit;

import android.net.Uri;

public class MainModel {
    String name,username;

    MainModel() {

    }

    public MainModel(String name, String username, Uri image) {
        this.name = name;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return "@"+username;
    }



    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
