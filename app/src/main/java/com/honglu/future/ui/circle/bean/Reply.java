package com.honglu.future.ui.circle.bean;

import java.io.Serializable;

public class Reply implements Serializable {
    public String user_name;
    public String content;
    public String id;
    public String send_id;
    public String sender;
    public String receiver_id;
    public String receiver;
    public String add_time;
    public String sender_image;

    @Override
    public String toString() {
        return "Reply{" +
                "user_name='" + user_name + '\'' +
                ", content='" + content + '\'' +
                ", id='" + id + '\'' +
                ", send_id='" + send_id + '\'' +
                ", sender='" + sender + '\'' +
                ", receiver_id='" + receiver_id + '\'' +
                ", receiver='" + receiver + '\'' +
                ", add_time='" + add_time + '\'' +
                ", sender_image='" + sender_image + '\'' +
                '}';
    }
}
