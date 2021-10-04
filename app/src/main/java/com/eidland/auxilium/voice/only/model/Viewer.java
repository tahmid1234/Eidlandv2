package com.eidland.auxilium.voice.only.model;

public class Viewer {
    public String id;
    public String photo;
    public String type;
    public String name;
    public String recievedCoins;
    public int uid;
    public Viewer(){

    }
    public Viewer(String id, String photo, String type, String n, String recievedCoins, int uid){
        this.id = id;
        this.photo = photo;
        this.type=type;
        this.name=n;
        this.uid=uid;
        this.recievedCoins=recievedCoins;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photo;
    }

    public int getUid() {
        return uid;
    }

    public void setPhotoUrl(String photo) {
        this.photo = photo;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRecievedCoins() { return recievedCoins; }

    public void setRecievedCoins(String recievedCoins) { this.recievedCoins = recievedCoins; }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
