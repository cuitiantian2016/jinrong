package com.honglu.future.ui.msg.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by 贺飞 on 2018/1/2
 *
 * 系统消息
 */

public class TradeMsgBean implements Serializable {
    @SerializedName("sendTime")
    public String time;
    public String content;
}
