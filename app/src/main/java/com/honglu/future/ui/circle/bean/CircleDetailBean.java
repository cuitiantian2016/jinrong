package com.honglu.future.ui.circle.bean;

import java.util.List;

/**
 * 帖子详情-head bean
 * Created by zhuaibing on 2017/12/11
 */

public class CircleDetailBean {
    public List<PraiseListBean> praiseList;
    public CircleHeadBean circleIndexBo;

    public class CircleHeadBean{
        public String avatarPic; //发帖人头像
        public String circleId; //帖子id
        public int commentCount;//评论数
        public String content;//内容
        public String createTime; //时间
        public List<String> images;
        public String isFocus; //是否关注  0 否 1 是
        public String isPraise; //是否点赞 0 否 1 是
        public String nickName; //发帖人昵称
        public String postUserId;
        public String praiseCount; //点赞数
        public String replyContent;
        public String replyNickName;
        public String userRole; //管理员

        public boolean isFocus(){
            return "1".equals(isFocus) ? true : false;
        }

        public boolean isPraise(){
            return "1".equals(isPraise) ? true : false;
        }
    }
}
