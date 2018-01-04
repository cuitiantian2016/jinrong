package com.honglu.future.ui.circle.bean;

/*评论内容
 * Created by zhuaibing on 2017/12/11
 */

import com.google.gson.JsonNull;

import java.io.Serializable;
import java.util.List;

public class CommentBean implements Serializable{
    /**
     * avatarPic : 评论人头像
     * beReplyAvatarPic : 被评论人头像
     * beReplyNickName : 被评人昵称
     * circleReplyId : 16
     * createTime : 2017-12-08 16:13:21.0
     * isPraise : 0
     * nickName : 评论人昵称
     * praiseCount : 0
     * replyContent : 评论内容
     * replyUserId : 回复人id
     * userRole : 角色
     */

    public String avatarPic;
    public String beReplyAvatarPic;
    public String beReplyNickName;
    public int circleReplyId;
    public String createTime;
    public String replyType;//1:贴子评论 2:回复贴子评论
    public int isPraise;
    public String nickName;
    public int praiseCount;
    public String replyContent;
    public String replyUserId;
    public String userRole;
    public LayCommentBean layComment;

    public class LayCommentBean implements Serializable{
       public int count;
       public List<LayCommentListBean>  layList;
    }

    public class LayCommentListBean implements Serializable{
        public String avatarPic;
        public String beReplyAvatarPic;
        public String beReplyNickName;
        public String circleReplyId;
        public String createTime;
        public String fatherCircleReplyId;
        public String isPraise;
        public String layCircleReplyId;
        public String layComment;
        public String nickName;
        public String praiseCount;
        public String replyContent;
        public String replyType;
        public String replyUserId;
        public String userRole;
    }
}
