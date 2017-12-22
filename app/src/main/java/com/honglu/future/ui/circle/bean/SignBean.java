package com.honglu.future.ui.circle.bean;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhuaibing on 2017/9/29
 */

public class SignBean implements Serializable{
    /**
     * signList : [{"paramNameOne":"1","paramValueOne":"10"},{"paramNameOne":"2","paramValueOne":"20"}]
     * conseSign : 0
     * isSign : false
     */
    public boolean isSign;
    public List<SignListBean> signList;
    @SerializedName("conseSign")
    public int count;
    public boolean isIsSign() {
        return isSign;
    }

    public List<SignListBean> getSignList() {
        return signList;
    }

    public static class SignListBean {
        /**
         * paramNameOne : 1
         * paramValueOne : 10
         */
        public String paramNameOne;
        public String paramValueOne;
        private boolean isSign = false; //false 当前item 没签到 true 当前已经签到
        private boolean isSignClick = false;//true 当前item 为签到标签item

        public String getParamNameOne() {
            return paramNameOne;
        }

        public void setParamNameOne(String paramNameOne) {
            this.paramNameOne = paramNameOne;
        }

        public String getParamValueOne() {
            return paramValueOne;
        }

        public void setParamValueOne(String paramValueOne) {
            this.paramValueOne = paramValueOne;
        }
        public boolean isSign() {
            return isSign;
        }

        public void setSign(boolean sign) {
            isSign = sign;
        }

        public boolean isSignClick() {
            return isSignClick;
        }

        public void setSignClick(boolean signClick) {
            isSignClick = signClick;
        }
    }
}
