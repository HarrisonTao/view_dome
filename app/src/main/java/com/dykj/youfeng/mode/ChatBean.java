package com.dykj.youfeng.mode;

public class ChatBean {
    public boolean isKf;
    public String msg;
    public String time;

    public ChatBean() {
    }

    public ChatBean(boolean isKf, String msg) {
        this.isKf = isKf;
        this.msg = msg;
    }

    public ChatBean(boolean isKf, String msg, String time) {
        this.isKf = isKf;
        this.msg = msg;
        this.time = time;
    }
}
