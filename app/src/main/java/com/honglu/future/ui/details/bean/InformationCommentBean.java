package com.honglu.future.ui.details.bean;

/**
 * 评论实体类
 * Created by tie on 2017/2/21.
 *
 * "auditMan": "",
 "auditTime": 1511859526000,
 "commentContent": "this is the comment",
 "commentTitle": "",
 "createMan": 3,
 "createTime": 1511859526000,
 "fatherId": 3,
 "informationCommentId": 1,
 "informationId": 8,
 "layers": 0,
 "modifyMan": 3,
 "modifyTime": 1511859526000,
 "nickname": "贺飞",
 "postTime": 1511859526000,
 "postmanId": 3,
 "remark": "",
 "replyNickname": "贺飞",
 "state": 1,
 "userAvatar": "user/20171121/1511258391266-3.png",
 "userRole": ""
 */

public class InformationCommentBean {
    private String auditMan;
    private String auditTime;
    private String commentContent;
    private String commentTitle;
    private String informationCommentId;
    private String informationId;
    private int isAttention;
    private String modifyMan;
    private String showTime;
    private String nickname;
    private String postmanId;
    private String registerMan;
    private String registerTime;
    private String remark;
    private String replyNickname;

    public String getReplyNickname() {
        return replyNickname;
    }

    public void setReplyNickname(String replyNickname) {
        this.replyNickname = replyNickname;
    }

    private int state;
    private String fatherId;

    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }

    private String userAvatar;
    private String userRole;

    public String getAuditMan() {
        return auditMan;
    }

    public void setAuditMan(String auditMan) {
        this.auditMan = auditMan;
    }

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommentTitle() {
        return commentTitle;
    }

    public void setCommentTitle(String commentTitle) {
        this.commentTitle = commentTitle;
    }

    public String getInformationCommentId() {
        return informationCommentId;
    }

    public void setInformationCommentId(String informationCommentId) {
        this.informationCommentId = informationCommentId;
    }

    public String getInformationId() {
        return informationId;
    }

    public void setInformationId(String informationId) {
        this.informationId = informationId;
    }

    public int getIsAttention() {
        return isAttention;
    }

    public void setIsAttention(int isAttention) {
        this.isAttention = isAttention;
    }

    public String getModifyMan() {
        return modifyMan;
    }

    public void setModifyMan(String modifyMan) {
        this.modifyMan = modifyMan;
    }

    public String getModifyTime() {
        return showTime;
    }

    public void setModifyTime(String modifyTime) {
        this.showTime = modifyTime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPostmanId() {
        return postmanId;
    }

    public void setPostmanId(String postmanId) {
        this.postmanId = postmanId;
    }

    public String getRegisterMan() {
        return registerMan;
    }

    public void setRegisterMan(String registerMan) {
        this.registerMan = registerMan;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

}
