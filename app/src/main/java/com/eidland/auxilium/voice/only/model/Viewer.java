package com.eidland.auxilium.voice.only.model;

public class Viewer {
    public String id;
    public String photo;
    public String type;
    public String name;
    public Viewer(){

    }
    public Viewer(String uid, String photo, String type, String n){
        this.id = uid;
        this.photo = photo;
        this.type=type;
        this.name=n;
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

    public String getUid() {
        return id;
    }

    public void setPhotoUrl(String photo) {
        this.photo = photo;
    }

    public void setUid(String uid) {
        this.id = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
