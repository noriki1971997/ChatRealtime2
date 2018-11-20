package com.example.norik.chatrealtime;

public class MessageLine {
    String own;
    String msg;

    public String getOwn() {
        return own;
    }

    public void setOwn(String own) {
        this.own = own;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MessageLine() {
        this.own = null;
        this.msg = null;

    }
}
