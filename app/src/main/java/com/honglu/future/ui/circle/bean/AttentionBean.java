package com.honglu.future.ui.circle.bean;

import java.util.List;

/**
 * Created by zq on 2017/12/16.
 */

public class AttentionBean {
    public String focusNum;
    public String beFocusNum;
    public List<FriendBean> circleUserBoList;

    public class FriendBean {
        public String avatarPic;
        public String beFocusNum;
        public String nickName;
        public String postNum;
        public String userRole;
        public String userId;
        public boolean focued;
    }
}
