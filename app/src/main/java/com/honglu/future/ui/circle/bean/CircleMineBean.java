package com.honglu.future.ui.circle.bean;

import java.util.List;

/**
 * Created by zq on 2017/12/8.
 */

public class CircleMineBean {
    private List<PostAndReplyBean> postAndReplyBoList;
    private List<ContactUser> contactUserList;
    private int postNum;
    private int beFocusNum;
    private int focusNum;
    private boolean isFocued;

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    private String userRole;

    public void setPostAndReplyBoList(List<PostAndReplyBean> postAndReplyBoList) {
        this.postAndReplyBoList = postAndReplyBoList;
    }

    public List<PostAndReplyBean> getPostAndReplyBoList() {
        return postAndReplyBoList;
    }

    public List<ContactUser> getContactUserList() {
        return contactUserList;
    }

    public void setContactUserList(List<ContactUser> contactUserList) {
        this.contactUserList = contactUserList;
    }

    public boolean isFocued() {
        return isFocued;
    }

    public void setFocued(boolean focued) {
        isFocued = focued;
    }

    public void setPostNum(int postNum) {
        this.postNum = postNum;
    }

    public int getPostNum() {
        return postNum;
    }

    public void setBeFocusNum(int beFocusNum) {
        this.beFocusNum = beFocusNum;
    }

    public int getBeFocusNum() {
        return beFocusNum;
    }

    public void setFocusNum(int focusNum) {
        this.focusNum = focusNum;
    }

    public int getFocusNum() {
        return focusNum;
    }

    public class ContactUser {
        public String avatarPic;
        public String beFocusNum;
        public boolean focued;
        public String nickName;
        public String postNum;
        public String userId;
    }
}
