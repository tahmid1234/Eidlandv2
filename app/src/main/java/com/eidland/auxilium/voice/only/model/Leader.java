package com.eidland.auxilium.voice.only.model;

public class Leader {
    public String name;
    public long coins;
    public String imgUrl;
    public String uid;

    public Leader(String name, long coins, String imgUrl, String uid) {
        this.name = name;
        this.coins = coins;
        this.imgUrl = imgUrl;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCoins() {
        return coins;
    }

    public void setCoins(long coins) {
        this.coins = coins;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
