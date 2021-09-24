package com.eidland.auxilium.voice.only.model;

public class Rooms {
   public String name;
    public String imageurl;
    public String hostuid;
    public String token;
    public String viewers;
    public String roomname;
    public String inviteLink;
    public String startTime;
    public String endTime;
    public String offTimeMsg;
    public String welcomemsg;


    public Rooms() {
    }

    public Rooms(String name, String imageurl, String hostuid, String token, String viewers, String roomname, String startTime, String endTime, String offTimeMsg, String inviteLink, String welcomemsg) {
        this.name = name;
        this.imageurl = imageurl;
        this.hostuid = hostuid;
        this.token = token;
        this.viewers = viewers;
        this.roomname = roomname;
        this.startTime = startTime;
        this.endTime = endTime;
        this.offTimeMsg = offTimeMsg;
        this.inviteLink = inviteLink;
        this.welcomemsg = welcomemsg;

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

    public String getInviteLink() { return inviteLink; }
    public void setInviteLink(String inviteLink) { this.inviteLink = inviteLink; }

    public String getWelcomemsg() { return welcomemsg; }
    public void setWelcomemsg(String welcomemsg) { this.welcomemsg = welcomemsg; }
}
