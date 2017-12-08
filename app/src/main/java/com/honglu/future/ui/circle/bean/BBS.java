package com.honglu.future.ui.circle.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *"avatarPic": "发帖人头像",
 "circleId": "帖子id",
 "commentCount": "评论数",
 "content": "帖子内容",
 "createTime": "发帖时间",
 "isFocus": "是否关注 0 否 1 是",
 "isPraise": "是否点赞 0 否 1 是",
 "nickName": "发帖昵称",
 "postUserId": "发帖人id",
 "praiseCount": "点赞数",
 "replyContent": "评论内容",
 "replyNickName": "回复人昵称",
 "userRole": "发帖人角色"

 {"avatarPic":"user/20171204/1512371804648-17.jpg","circleId":null,
 "commentCount":0,"content":"88888888888888888888888888888",
 "createTime":"2017-12-08 08:46:42.0","isFocus":0,
 "isPraise":0,"nickName":"开发",
 "postUserId":null,
 "praiseCount":1
 "replyContent":"",
 "replyNickName":"",
 "userRole":"超级管理员"}
 */
public class BBS implements Serializable {
    @SerializedName("circleId")
    public String topic_id;
    @SerializedName("nickName")
    public String user_name;
    @SerializedName("avatarPic")
    public String header_img;
    @SerializedName("createTime")
    public String announce_time;
    @SerializedName("content")
    public String content;
    public ArrayList<String> images;
    private String replyContent;
    private String replyNickName;
    @SerializedName("praiseCount")
    public String support_num;  //赞
    @SerializedName("commentCount")
    public String reply_num;    //回复数
    @SerializedName("isPraise")
    public String attutude;   //1点赞  2鄙视
    @SerializedName("userRole")
    public String user_level;   //是否显示后缀
    @SerializedName("isFocus")
    public String follow;
    public String type;
    public String hot_topic_title; //话题
    public String is_essence; //是否是精华 0 不是 1 是
    public String date;
    public String integralUserNum;  //帖子打赏人数
    public List<Reply> replyList;  //回复列表
    //话题相关
    @SerializedName("postUserId")
    public String uid;
    /**
     * 1白银  2石油 3开啡 4问答  5活动 6策略 7晒单 8公告 9资讯 10专访
     */
    public String topic_type;              // 发帖类型
    public String topic_link;

    public boolean isEssence() {
        return "1".equals(is_essence);
    }
    public List<Reply> getReplyList(){
        Reply reply = new Reply();
        reply.content = replyContent;
        reply.user_name = replyNickName;
        ArrayList<Reply> replies = new ArrayList<>();
        if (!TextUtils.isEmpty(replyContent)){
            replies.add(reply);
        }
        return replies;
    }


}
