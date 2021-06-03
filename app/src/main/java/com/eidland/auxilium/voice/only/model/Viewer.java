package com.eidland.auxilium.voice.only.model;

public class Viewer {
    String id;
    String photo;
    String type;
    String name;
    public Viewer(){

    }
    public Viewer(String id, String photo, String type,String n){
        this.id=id;
        this.photo=photo;
        this.type=type;
        this.name=n;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public String getId() {
        return id;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
