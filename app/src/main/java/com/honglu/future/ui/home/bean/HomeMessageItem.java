package com.honglu.future.ui.home.bean;

import java.util.List;

/**
 * Created by Admin on 2017/2/17.
 */

public class HomeMessageItem {

    /**
     * code : 200
     * message : 成功
     * data : {"successed":true,"data":[{"attentionCounts":2,"auditMan":"2","auditTime":"2017-02-14 10:30:49.0","authorId":"1","commentNum":2,"content":"过分过分过分","contentType":"14","ifHot":"1","informationComment":null,"informationId":"1","informationPraise":[],"isAttention":0,"modifyMan":"2","modifyTime":"2017-02-14 10:31:13.0","nickname":"www","picList":[],"picOne":"http://static5.518yin.com/mobile/images/default_avater.png","picThree":"http://mypics.zhaopin.com/pic/2017/2/7/662F717364084C3D91CC5B95B67A6696.jpg","picTwo":"http://static5.518yin.com/mobile/images/default_avater.png","praiseCounts":2,"registerMan":"2","registerTime":"2017-02-14 10:30:55.0","remark":"2","state":"2","title":"565","userAttentionRec":null,"userAvatar":"ee","userRole":"ww"},{"attentionCounts":1,"auditMan":"auditMan","auditTime":"2017-02-14 19:59:53.0","authorId":"4c8b8d7eff554d269915d7e491969b70","commentNum":2,"content":"content","contentType":"contentType","ifHot":"0","informationComment":null,"informationId":"eae463f4c49b418c8c360fe000357fe0","informationPraise":[],"isAttention":0,"modifyMan":"modifyMan","modifyTime":"2017-02-14 19:59:53.0","nickname":"ww","picList":[],"picOne":"picOne","picThree":"picThree","picTwo":"picTwo","praiseCounts":2,"registerMan":"registerMan","registerTime":"2017-02-14 19:59:53.0","remark":"remark","state":"1","title":"title","userAttentionRec":null,"userAvatar":"ee","userRole":"w"},{"attentionCounts":1,"auditMan":"auditMan","auditTime":"2017-02-14 20:15:36.0","authorId":"4c8b8d7eff554d269915d7e491969b70","commentNum":2,"content":"content","contentType":"contentType","ifHot":"0","informationComment":null,"informationId":"d82bc3d28e5643ee83cf6474462c37c2","informationPraise":[],"isAttention":0,"modifyMan":"modifyMan","modifyTime":"2017-02-14 20:15:36.0","nickname":"ww","picList":[],"picOne":"picOne","picThree":"picThree","picTwo":"picTwo","praiseCounts":2,"registerMan":"registerMan","registerTime":"2017-02-14 20:15:36.0","remark":"remark","state":"1","title":"title","userAttentionRec":null,"userAvatar":"ee","userRole":"w"},{"attentionCounts":1,"auditMan":"auditMan","auditTime":"2017-02-15 10:35:09.0","authorId":"1","commentNum":1,"content":"content","contentType":"contentType","ifHot":"0","informationComment":null,"informationId":"c1535785f4fd4df09c8caeb485435f2d","informationPraise":[],"isAttention":0,"modifyMan":"modifyMan","modifyTime":"2017-02-15 10:35:09.0","nickname":"ww","picList":[],"picOne":"picOne.jpg,1.jpg,2.jpg","picThree":"picThree","picTwo":"picTwo","praiseCounts":1,"registerMan":"registerMan","registerTime":"2017-02-15 10:35:09.0","remark":"remark","state":"1","title":"title","userAttentionRec":null,"userAvatar":"ee","userRole":"w"},{"attentionCounts":5,"auditMan":"1","auditTime":"2017-02-09 11:25:33.0","authorId":"2","commentNum":23,"content":" 攻倒家具","contentType":"21","ifHot":"3","informationComment":null,"informationId":"0b21640f671e427eaf025b8109a05121","informationPraise":[],"isAttention":0,"modifyMan":"23","modifyTime":"2017-02-16 11:45:27.0","nickname":"","picList":[],"picOne":"https://www.baidu.com/s?wd=%E9%A3%8E%E4%BF%A1%E5%AD%90&rsv_idx=2&tn=baiduhome_pg&usm=4&ie=utf-8&rsv_cq=%E8%8A%B1&rsv_dl=0_left_pet_multi_6829","picThree":"www.baidu.com","picTwo":"","praiseCounts":10,"registerMan":"1","registerTime":"2017-02-15 11:45:27.0","remark":"","state":"2","title":"213","userAttentionRec":null,"userAvatar":"","userRole":""}]}
     */

    private String code;
    private String message;
    private DataBeanX data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * successed : true
         * data : [{"attentionCounts":2,"auditMan":"2","auditTime":"2017-02-14 10:30:49.0","authorId":"1","commentNum":2,"content":"过分过分过分","contentType":"14","ifHot":"1","informationComment":null,"informationId":"1","informationPraise":[],"isAttention":0,"modifyMan":"2","modifyTime":"2017-02-14 10:31:13.0","nickname":"www","picList":[],"picOne":"http://static5.518yin.com/mobile/images/default_avater.png","picThree":"http://mypics.zhaopin.com/pic/2017/2/7/662F717364084C3D91CC5B95B67A6696.jpg","picTwo":"http://static5.518yin.com/mobile/images/default_avater.png","praiseCounts":2,"registerMan":"2","registerTime":"2017-02-14 10:30:55.0","remark":"2","state":"2","title":"565","userAttentionRec":null,"userAvatar":"ee","userRole":"ww"},{"attentionCounts":1,"auditMan":"auditMan","auditTime":"2017-02-14 19:59:53.0","authorId":"4c8b8d7eff554d269915d7e491969b70","commentNum":2,"content":"content","contentType":"contentType","ifHot":"0","informationComment":null,"informationId":"eae463f4c49b418c8c360fe000357fe0","informationPraise":[],"isAttention":0,"modifyMan":"modifyMan","modifyTime":"2017-02-14 19:59:53.0","nickname":"ww","picList":[],"picOne":"picOne","picThree":"picThree","picTwo":"picTwo","praiseCounts":2,"registerMan":"registerMan","registerTime":"2017-02-14 19:59:53.0","remark":"remark","state":"1","title":"title","userAttentionRec":null,"userAvatar":"ee","userRole":"w"},{"attentionCounts":1,"auditMan":"auditMan","auditTime":"2017-02-14 20:15:36.0","authorId":"4c8b8d7eff554d269915d7e491969b70","commentNum":2,"content":"content","contentType":"contentType","ifHot":"0","informationComment":null,"informationId":"d82bc3d28e5643ee83cf6474462c37c2","informationPraise":[],"isAttention":0,"modifyMan":"modifyMan","modifyTime":"2017-02-14 20:15:36.0","nickname":"ww","picList":[],"picOne":"picOne","picThree":"picThree","picTwo":"picTwo","praiseCounts":2,"registerMan":"registerMan","registerTime":"2017-02-14 20:15:36.0","remark":"remark","state":"1","title":"title","userAttentionRec":null,"userAvatar":"ee","userRole":"w"},{"attentionCounts":1,"auditMan":"auditMan","auditTime":"2017-02-15 10:35:09.0","authorId":"1","commentNum":1,"content":"content","contentType":"contentType","ifHot":"0","informationComment":null,"informationId":"c1535785f4fd4df09c8caeb485435f2d","informationPraise":[],"isAttention":0,"modifyMan":"modifyMan","modifyTime":"2017-02-15 10:35:09.0","nickname":"ww","picList":[],"picOne":"picOne.jpg,1.jpg,2.jpg","picThree":"picThree","picTwo":"picTwo","praiseCounts":1,"registerMan":"registerMan","registerTime":"2017-02-15 10:35:09.0","remark":"remark","state":"1","title":"title","userAttentionRec":null,"userAvatar":"ee","userRole":"w"},{"attentionCounts":5,"auditMan":"1","auditTime":"2017-02-09 11:25:33.0","authorId":"2","commentNum":23,"content":" 攻倒家具","contentType":"21","ifHot":"3","informationComment":null,"informationId":"0b21640f671e427eaf025b8109a05121","informationPraise":[],"isAttention":0,"modifyMan":"23","modifyTime":"2017-02-16 11:45:27.0","nickname":"","picList":[],"picOne":"https://www.baidu.com/s?wd=%E9%A3%8E%E4%BF%A1%E5%AD%90&rsv_idx=2&tn=baiduhome_pg&usm=4&ie=utf-8&rsv_cq=%E8%8A%B1&rsv_dl=0_left_pet_multi_6829","picThree":"www.baidu.com","picTwo":"","praiseCounts":10,"registerMan":"1","registerTime":"2017-02-15 11:45:27.0","remark":"","state":"2","title":"213","userAttentionRec":null,"userAvatar":"","userRole":""}]
         */

        private boolean successed;
        private List<DataBean> data;

        public boolean isSuccessed() {
            return successed;
        }

        public void setSuccessed(boolean successed) {
            this.successed = successed;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {

            private int attentionCounts;
            private String auditMan;
            private String auditTime;
            private String authorId;
            private int commentNum;
            private String content;
            private String contentType;
            private String ifHot;
            private Object informationComment;
            private String informationId;
            private int isAttention;
            private String modifyMan;
            private String modifyTime;
            public int isPraise;

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            private String createTime;
            private String nickname;
            private String picOne;
            private String picThree;
            private String picTwo;
            private int praiseCounts;
            private String registerMan;
            private String registerTime;
            private String remark;
            private String state;
            private String title;
            private String homePic;

            public String getHomePic() {
                return homePic;
            }

            public void setHomePic(String homePic) {
                this.homePic = homePic;
            }

            private String describeContent;

            public String getDescribeContent() {
                return describeContent;
            }

            public void setDescribeContent(String describeContent) {
                this.describeContent = describeContent;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            private String type;
            private Object userAttentionRec;
            private String userAvatar;
            private String userRole;
            private List<?> informationPraise;
            private List<?> picList;

            public int getAttentionCounts() {
                return attentionCounts;
            }

            public void setAttentionCounts(int attentionCounts) {
                this.attentionCounts = attentionCounts;
            }

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

            public String getAuthorId() {
                return authorId;
            }

            public void setAuthorId(String authorId) {
                this.authorId = authorId;
            }

            public int getCommentNum() {
                return commentNum;
            }

            public void setCommentNum(int commentNum) {
                this.commentNum = commentNum;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getContentType() {
                return contentType;
            }

            public void setContentType(String contentType) {
                this.contentType = contentType;
            }

            public String getIfHot() {
                return ifHot;
            }

            public void setIfHot(String ifHot) {
                this.ifHot = ifHot;
            }

            public Object getInformationComment() {
                return informationComment;
            }

            public void setInformationComment(Object informationComment) {
                this.informationComment = informationComment;
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
                return modifyTime;
            }

            public void setModifyTime(String modifyTime) {
                this.modifyTime = modifyTime;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getPicOne() {
                return picOne;
            }

            public void setPicOne(String picOne) {
                this.picOne = picOne;
            }

            public String getPicThree() {
                return picThree;
            }

            public void setPicThree(String picThree) {
                this.picThree = picThree;
            }

            public String getPicTwo() {
                return picTwo;
            }

            public void setPicTwo(String picTwo) {
                this.picTwo = picTwo;
            }

            public int getPraiseCounts() {
                return praiseCounts;
            }

            public void setPraiseCounts(int praiseCounts) {
                this.praiseCounts = praiseCounts;
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

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public Object getUserAttentionRec() {
                return userAttentionRec;
            }

            public void setUserAttentionRec(Object userAttentionRec) {
                this.userAttentionRec = userAttentionRec;
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

            public List<?> getInformationPraise() {
                return informationPraise;
            }

            public void setInformationPraise(List<?> informationPraise) {
                this.informationPraise = informationPraise;
            }

            public List<?> getPicList() {
                return picList;
            }

            public void setPicList(List<?> picList) {
                this.picList = picList;
            }
        }
    }
}
