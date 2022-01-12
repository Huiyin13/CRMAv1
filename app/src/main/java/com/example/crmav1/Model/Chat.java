package com.example.crmav1.Model;

public class Chat {

    private String sender;
    private String receiver;
    private String msg;
    private String mid;

    public Chat() {
    }

    public Chat(String sender, String receiver, String msg, String mid) {
        this.sender = sender;
        this.receiver = receiver;
        this.msg = msg;
        this.mid = mid;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String type) {
        this.mid = mid;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMsg() {
        return msg;
    }

    public void setMeesage(String msg) {
        this.msg = msg;
    }
}
