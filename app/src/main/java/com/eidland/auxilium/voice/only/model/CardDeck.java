package com.eidland.auxilium.voice.only.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class CardDeck {

    public String deckImage;
    public String deckName;

    public CardDeck(String deckName, String deckImage) {
        this.deckImage = deckImage;
        this.deckName = deckName;

    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("game image", this.deckImage);
        result.put("game name", this.deckName);
        return result;
    }

    public String getDeckImage() {
        return deckImage;
    }

    public void setDeckImage(String deckImage) {
        this.deckImage = deckImage;
    }

    public String getDeckName() {
        return deckName;
    }

    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }

}
