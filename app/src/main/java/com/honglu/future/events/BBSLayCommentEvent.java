package com.honglu.future.events;

/**
 * Created by zhuaibing on 2018/1/4
 */

public class BBSLayCommentEvent {
    public String nickName;
    public String fatherCircleReplyId;
    public String circleReplyId;
    public String content;

    public BBSLayCommentEvent(String nickName,String fatherCircleReplyId,String circleReplyId,String content){
        this.nickName = nickName;
        this.fatherCircleReplyId = fatherCircleReplyId;
        this.circleReplyId = circleReplyId;
        this.content = content;
    }
}
