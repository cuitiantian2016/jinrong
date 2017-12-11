package com.honglu.future.ui.circle.bean;

/*评论内容
 * Created by zhuaibing on 2017/12/11
 */

import java.util.List;

public class CommentAllBean {

    /**
     * commentBosAll : [{"avatarPic":"评论人头像","beReplyAvatarPic":"被评论人头像","beReplyNickName":"被评人昵称","circleReplyId":16,"createTime":"2017-12-08 16:13:21.0","isPraise":0,"nickName":"评论人昵称","praiseCount":0,"replyContent":"评论内容","replyUserId":"回复人id","userRole":"角色"}]
     * commentCountAuth : 只看楼主评论数
     * commentCountAll : 所有评论数
     */

    public int commentCountAuth;
    public int commentCountAll;
    public List<CommentBean> commentBosAll;

}
