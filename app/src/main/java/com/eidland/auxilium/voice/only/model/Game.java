package com.eidland.auxilium.voice.only.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Game {
    public String game;

    public Game(String gift, long giftValue, String senderUID, String senderName, String senderImg, String receiverUID, String receiverName, String receiverImg, long time) {
        this.game = gift;

    }

    public Game() {
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("gift", this.game);
        return result;
    }

    public String getGift() {
        return game;
    }

    public void setGift(String gift) {
        this.game = gift;
    }


}
