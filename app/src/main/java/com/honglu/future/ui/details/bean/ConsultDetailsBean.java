package com.honglu.future.ui.details.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by hc on 2017/10/24.
 */

public class ConsultDetailsBean {

    public ArrayList<String> praiseAvatars;
    public ArrayList<String> picList;
    public String content;
    @SerializedName("praise")
    public int isPraise;
    public String homePic;//顶部图片
    public String title;//消息标题
    public String userAvatar;//用户的图片
    public String nickname;//用户的名字
    public String userRole;//用户的角色
    public String showTime;//发帖的时间
    public int commentNum;//评论数
    public int position;//当前的位置
    public int praiseCounts;//点赞数
}
