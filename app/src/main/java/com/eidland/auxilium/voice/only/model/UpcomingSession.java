package com.eidland.auxilium.voice.only.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class UpcomingSession {
    public String roomId;
    public String title;
    public String description;
    public String iconUrl;
    public String startTime;

    public UpcomingSession() {
    }

    public UpcomingSession(String roomId, String title, String description, String iconUrl, String startTime) {
        this.roomId = roomId;
        this.title = title;
        this.description = description;
        this.iconUrl = iconUrl;
        this.startTime = startTime;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("roomId", this.roomId);
        result.put("title", this.title);
        result.put("description", this.description);
        result.put("iconUrl", this.iconUrl);
        result.put("startTime", this.startTime);
        return result;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
