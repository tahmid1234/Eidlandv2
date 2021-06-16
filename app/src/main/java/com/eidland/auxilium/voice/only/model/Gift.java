package com.eidland.auxilium.voice.only.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Gift {
    public String gift;
    public long giftValue;
    public String senderName;
    public String receiverName;
    public String senderUID;
    public String receiverUID;
    public String senderImg;
    public String receiverImg;
    public long time;

    public Gift(String gift, long giftValue, String senderUID, String senderName, String senderImg, String receiverUID, String receiverName, String receiverImg, long time) {
        this.gift = gift;
        this.giftValue = giftValue;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.senderUID = senderUID;
        this.receiverUID = receiverUID;
        this.senderImg = senderImg;
        this.receiverImg = receiverImg;
        this.time = time;
    }

    public Gift() {
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("gift", this.gift);
        result.put("giftValue", this.giftValue);
        result.put("senderUID", this.senderUID);
        result.put("senderName", this.senderName);
        result.put("senderImg", this.senderImg);
        result.put("receiverUID", this.receiverUID);
        result.put("receiverName", this.receiverName);
        result.put("receiverImg", this.receiverImg);
        result.put("time", this.time);
        return result;
    }

    public String getGift() {
        return gift;
    }

    public void setGift(String gift) {
        this.gift = gift;
    }

    public long getGiftValue() {
        return giftValue;
    }

    public void setGiftValue(long giftValue) {
        this.giftValue = giftValue;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getSenderUID() {
        return senderUID;
    }

    public void setSenderUID(String senderUID) {
        this.senderUID = senderUID;
    }

    public String getReceiverUID() {
        return receiverUID;
    }

    public void setReceiverUID(String receiverUID) {
        this.receiverUID = receiverUID;
    }

    public String getSenderImg() {
        return senderImg;
    }

    public void setSenderImg(String senderImg) {
        this.senderImg = senderImg;
    }

    public String getReceiverImg() {
        return receiverImg;
    }

    public void setReceiverImg(String receiverImg) {
        this.receiverImg = receiverImg;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


}
