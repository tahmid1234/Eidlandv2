package com.eidland.auxilium.voice.only;

public class User {
  public   String name;
    public String email;
    public String imageurl;
    public String coins;
    public String receivedCoins;

    public User() {
    }

    public User(String name, String email, String imageurl,String coins, String receivedCoins) {
        this.name = name;
        this.email = email;
        this.imageurl = imageurl;
        this.coins = coins;
        this.receivedCoins = receivedCoins;
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

    public String getReceivedCoins() { return receivedCoins; }
    public void setReceivedCoins(String receivedCoins) { this.receivedCoins = receivedCoins; }
}