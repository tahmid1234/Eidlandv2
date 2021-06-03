package com.eidland.auxilium.voice.only.model;

public class Gift {
    String gift;
    String sender;
    String sendername;
    String receivername;

    public Gift() {
    }

    public Gift(String gift, String sender, String sendername, String receivername) {
        this.gift = gift;
        this.sender = sender;
        this.sendername = sendername;
        this.receivername = receivername;
    }

    public String getReceivername() {
        return receivername;
    }

    public void setReceivername(String receivername) {
        this.receivername = receivername;
    }

    public String getSendername() {
        return sendername;
    }

    public void setSendername(String sendername) {
        this.sendername = sendername;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }


    public void setGift(String gift) {
        this.gift = gift;
    }

    public String getSender() {
        return sender;
    }


    public String getGift() {
        return gift;
    }
}
