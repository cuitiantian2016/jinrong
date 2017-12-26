package com.honglu.future.ui.live;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * "roomId": null,
 * "roomName": "早盘先锋",
 * "roomDescribe": "骚老师的早盘",
 * "roomStatus": 0,
 * "domainUrl": "xnzt.gensee.com",
 * "rtmpJoinUrl": "http://xnzt.gensee.com/webcast/site/entry/join-5e7164885bf7450e99d7bff73bdda11e",
 * "roomJoinPassword": "453873",
 * "chatRoomId": "59138744",
 * "activityName": null,
 * "liveBeginTime": "9:00",
 * "liveEndTime": "10:15",
 * "onlineNumber": 100,
 * "authorId": 2,
 * "authorVar": null,
 * "authorName": "洪老师",
 * "authorDescribe": null,
 * "channelType": null,
 * "createTime": null,
 * "updateTime": null,
 * "appName": null,
 * "isGuanzhu": 0,
 * "liveTime": "12月26日9:00-10:15直播"
 */

//任务的bean类
public class LiveListBean implements Serializable{

    public boolean isLive(){//是否正在直播
       return roomStatus ==1;
   }
    public String roomId;
    @SerializedName("onlineNumber")
    public String liveNum;//在线人数
    @SerializedName("backGroundUrl")
    public String liveImg;//直播图片
    public int roomStatus;
    public String domainUrl;
    public String rtmpJoinUrl;
    public String roomJoinPassword;
    public String chatRoomId;
    @SerializedName("roomName")
    public String liveTitle;//直播标题
    public String liveTime;//直播时间
    @SerializedName("roomDescribe")
    public String liveDes;//直播描述
    @SerializedName("authorName")
    public String liveTeacher;//直播老师
    @SerializedName("authorDescribe")
    public String liveTeacherDes;//直播老师简介
    @SerializedName("authorVar")
    public String liveTeacherICon;//直播老师的头像
    @SerializedName("authorId")
    public String liveTeacherID;//老师的id
    @SerializedName("isGuanzhu")
    public String follow;//老师的id
    public boolean isExpend;//标记是否打开
    public String authorDescribeLimit;//老师特色简要描述
    public boolean isFollow(){
        return Integer.parseInt(follow)==1;
    }
}
