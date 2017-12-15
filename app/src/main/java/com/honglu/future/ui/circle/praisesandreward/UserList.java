package com.honglu.future.ui.circle.praisesandreward;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserList implements Serializable {
    @SerializedName("attentionCount")
    public String fans_num;
    @SerializedName("isFocus")
    public String follow;
    @SerializedName("avatarPic")
    public String headimgurl;
    @SerializedName("circleCount")
    public String topic_num;
    @SerializedName("userId")
    public String uid;
    @SerializedName("nickName")
    public String user_name;
    public String user_level;
    public String userRole;
}
