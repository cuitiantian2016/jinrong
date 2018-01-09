package com.honglu.future.ui.msg.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by 贺飞 on 2018/1/2
 * <p>
 * 系统消息
 * "meaageId": "消息id",
 * "content": "消息内容",
 * "sendTime": "时间"
 */

public class SystemMsgBean implements Serializable {
    public String title;
    @SerializedName("sendTime")
    public String time;
    public String content;
    @SerializedName("messageId")
    public String meaageId;
    public int status = -1 ;
    public boolean isRead(){
        return status==0;
    }
}
