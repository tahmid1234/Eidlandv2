package com.eidland.auxilium.voice.only.model;

public class Rooms {
    public String name, imageurl, hostuid, token, viewers, roomname;
    public String startTime;
    public String endTime;
    public String offTimeMsg;

    public Rooms() {
    }

    public Rooms(String name, String imageurl, String hostuid, String token, String viewers, String roomname, String startTime, String endTime, String offTimeMsg) {
        this.name = name;
        this.imageurl = imageurl;
        this.hostuid = hostuid;
        this.token = token;
        this.viewers = viewers;
        this.roomname = roomname;
        this.startTime = startTime;
        this.endTime = endTime;
        this.offTimeMsg = offTimeMsg;
    }

    public Rooms(String name, String imageurl, String hostuid, String token, String viewers, String roomname) {
        this.name = name;
        this.imageurl = imageurl;
        this.hostuid = hostuid;
        this.token = token;
        this.viewers = viewers;
        this.roomname = roomname;
    }

    public String getRoomname() {
        return roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }

    public String getViewers() {
        return viewers;
    }

    public void setViewers(String viewers) {
        this.viewers = viewers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getHostuid() {
        return hostuid;
    }

    public void setHostuid(String hostuid) {
        this.hostuid = hostuid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getOffTimeMsg() {
        return offTimeMsg;
    }

    public void setOffTimeMsg(String offTimeMsg) {
        this.offTimeMsg = offTimeMsg;
    }
}
