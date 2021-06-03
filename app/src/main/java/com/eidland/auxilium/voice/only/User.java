package com.eidland.auxilium.voice.only;

public class User {
  public   String name,email,imageurl,coins;

    public User() {
    }

    public User(String name, String email, String imageurl,String coins) {
        this.name = name;
        this.email = email;
        this.imageurl = imageurl;
        this.coins = coins;
    }

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
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

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
