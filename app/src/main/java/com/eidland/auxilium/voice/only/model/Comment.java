package com.eidland.auxilium.voice.only.model;

import android.net.Uri;

import java.io.Serializable;

public class Comment implements Serializable {

    String name;
    String comment;
    String firebaseuid;
    boolean hasimg;
    String imgid;
    String giftcount;
    String userphoto;

    public String getUserphoto() {
        return userphoto;
    }

    public void setUserphoto(String userphoto) {
        this.userphoto = userphoto;
    }

    public Comment() {

    }
    public Comment (String hostname,String welcomecomment)
    {
        this.name=hostname;
        this.comment=welcomecomment;
    }
    public Comment(String name, String comment, String firebaseuid,  boolean hasimg, String imgid, String giftcount, String uphoto) {
        this.name = name;
        this.comment = comment;
        this.firebaseuid = firebaseuid;

        this.hasimg = hasimg;
        this.imgid = imgid;
        this.giftcount = giftcount;
        this.userphoto=uphoto;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFirebaseuid() {
        return firebaseuid;
    }

    public void setFirebaseuid(String firebaseuid) {
        this.firebaseuid = firebaseuid;
    }

    public boolean isHasimg() {
        return hasimg;
    }

    public void setHasimg(boolean hasimg) {
        this.hasimg = hasimg;
    }

    public String getImgid() {
        return imgid;
    }

    public void setImgid(String imgid) {
        this.imgid = imgid;
    }

    public String getGiftcount() {
        return giftcount;
    }

    public void setGiftcount(String giftcount) {
        this.giftcount = giftcount;
    }
}
