package com.honglu.future.ui.circle.bean;

import java.util.List;

/**
 * Created by zq on 2017/12/8.
 */

public class CircleMineBean {
    private List<PostAndReplyBean> postAndReplyBoList;
    private int postNum;
    private int beFocusNum;
    private int focusNum;

    public void setPostAndReplyBoList(List<PostAndReplyBean> postAndReplyBoList) {
        this.postAndReplyBoList = postAndReplyBoList;
    }

    public List<PostAndReplyBean> getPostAndReplyBoList() {
        return postAndReplyBoList;
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
}
