package com.honglu.future.ui.msg.bean;

/**
 * MainMsgActivity 红点显示bean
 * Created by zhuaibing on 2018/1/6
 */

public class HasUnreadMsgBean {


    /**
     * sysMsgNoticeFlag : true
     * transMsgNoticeFlag : false
     * commentAndPraiseMsgNoticeFlag : true
     * replyMsgNoticeFlag : true
     * sysMsgCnt : 1
     * transMsgCnt : 0
     * commentMsgCnt : 1
     * praiseMsgCnt : 0
     * replyMsgCnt : 2
     */

    public boolean sysMsgNoticeFlag; //是否有未读系统消息
    public boolean transMsgNoticeFlag; //是否有未读交易消息
    public boolean commentAndReplyMsgNoticeFlag; //是否有未读评论（回复）消息
    public boolean praiseMsgNoticeFlag;  //是否有未读点赞消息
    public String sysMsgCnt;
    public String transMsgCnt;
    public String commentMsgCnt;
    public String praiseMsgCnt;
    public String replyMsgCnt;
}
