package com.honglu.future.ui.circle.bean;

import java.io.Serializable;
import java.util.List;

public class BBS implements Serializable {
    public String topic_id;
    public String user_name;
    public String header_img;
    public String announce_time;
    public String share_detail_title;//分享标题
    public String share_detail_content;//分享内容
    public String share_detail_url;//分享地址
    public String share_content;
    public String content;
    public String images;
    public String title;
    public String user_flag;  //2-认证
    public String support_num;  //赞
    public String oppose_num;   //踩
    public String reply_num;    //回复数
    public String attutude;   //1点赞  2鄙视
    public String user_level;   //是否显示后缀
    public String follow;
    public String type;
    public String start_date;
    public String hot_topic_title; //话题
    public String is_essence; //是否是精华 0 不是 1 是
    public String date;
    public String integralUserNum;  //帖子打赏人数
    public String hasTopicReward;  //帖子打赏状态

    public List<Reply> replyList;  //回复列表
    public List<AttutudeUser> attutude_user; //点赞人列表

    //话题相关
    public String reply_id;
    public String reply_user_name;
    public String uid;
    public String reply_uid;

    public String order_num;

    public int itemType;
    public boolean isLastGroupItem;
    public String subfiledTime;

    //消息新增
    public String id;
    public String status;
    public String rname;
    public String rimg;
    public String tcontent;
    public String tstatus;
    public String rcontent;
    public String icontent;
    public String rtime;
    public String video_url;  //列表图片跳转网址

    /**
     * 1白银  2石油 3开啡 4问答  5活动 6策略 7晒单 8公告 9资讯 10专访
     */
    public String topic_type;              // 发帖类型

    public String topic_link;

    public boolean isEssence() {
        return "1".equals(is_essence);
    }

    public BBS() {
    }

    public BBS(int itemType) {
        this.itemType = itemType;
    }

    public BBS(int itemType, String subfiledTime) {
        this.itemType = itemType;
        this.subfiledTime = subfiledTime;
    }
}
