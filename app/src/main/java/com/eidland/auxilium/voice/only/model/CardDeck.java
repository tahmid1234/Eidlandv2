package com.eidland.auxilium.voice.only.model;

import android.net.Uri;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class CardDeck {

    public Uri deckImage;
    public String deckName;

    public CardDeck(Uri deckImage, String gameName) {
        this.deckImage = deckImage;
        this.deckName = gameName;

    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("game image", this.deckImage);
        result.put("game name", this.deckName);
        return result;
    }

    public Uri getDeckImage() { return deckImage; }

    public void setDeckImage(Uri deckImage) { this.deckImage = deckImage; }

    public String getDeckName() { return deckName; }

    public void setDeckName(String deckName) { this.deckName = deckName; }

}
