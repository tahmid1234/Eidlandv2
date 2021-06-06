package com.eidland.auxilium.voice.only;

public class User {
  public   String name;
    public String email;
    public String imageurl;
    public String coins;



    public String recievedCoins;

    public User() {
    }

    public User(String name, String email, String imageurl,String coins, String recievedCoins) {
        this.name = name;
        this.email = email;
        this.imageurl = imageurl;
        this.coins = coins;
        this.recievedCoins = recievedCoins;
    }

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }

    public String getRecievedCoins() { return recievedCoins; }

    public void setRecievedCoins(String recievedCoins) { this.recievedCoins = recievedCoins; }

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
