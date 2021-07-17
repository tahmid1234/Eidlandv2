package com.eidland.auxilium.voice.only.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {
  public   String name= "User";
    public String email = "user@gmail.com";
    public String imageurl= "https://auxiliumlivestreaming.000webhostapp.com/avatar/1.png";
    public String coins = "0";
    public String receivedCoins = "0";


    public User() {
    }

    public User(String name, String email, String imageurl,String coins, String receivedCoins) {
        this.name = name;
        this.email = email;
        this.imageurl = imageurl;
        this.coins = coins;
        this.receivedCoins = receivedCoins;
    }



    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", this.name);
        result.put("email", this.email);
        result.put("imageurl", this.imageurl);
        result.put("coins", this.coins);
        result.put("receivedCoins", this.receivedCoins);
        return result;
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
