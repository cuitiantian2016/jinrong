package com.honglu.future.ui.home.bean;
import java.util.List;

/**
 * Created by Admin on 2017/2/17.
 */
public class HomeIcon {

    /**
     * code : 200
     * message : 3005
     * data : {"successed":true,"data":[{"createMan":"eb70273025984db3a387b30158f32c83","createTime":"2017-04-20 14:31:35.0","endTime":"2017-04-28 14:31:32.0","homeIconId":"a77ccb59ad334a7b8a366694250c0a90","image":"http://101.37.33.121/systemCenter/4563a0ee59e5411ab626281f5da25ab2.jpg","modifyMan":"分析师","modifyTime":"2017-04-20 15:17:27.0","sort":1,"startTime":"2017-04-20 14:31:31.0","state":1,"title":"实时新闻","titleCode":"2","url":"http://baidu.com"}]}
     */

    private DataBeanX data;
    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * successed : true
         * data : [{"createMan":"eb70273025984db3a387b30158f32c83","createTime":"2017-04-20 14:31:35.0","endTime":"2017-04-28 14:31:32.0","homeIconId":"a77ccb59ad334a7b8a366694250c0a90","image":"http://101.37.33.121/systemCenter/4563a0ee59e5411ab626281f5da25ab2.jpg","modifyMan":"分析师","modifyTime":"2017-04-20 15:17:27.0","sort":1,"startTime":"2017-04-20 14:31:31.0","state":1,"title":"实时新闻","titleCode":"2","url":"http://baidu.com"}]
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
            /**
             * createMan : eb70273025984db3a387b30158f32c83
             * createTime : 2017-04-20 14:31:35.0
             * endTime : 2017-04-28 14:31:32.0
             * homeIconId : a77ccb59ad334a7b8a366694250c0a90
             * image : http://101.37.33.121/systemCenter/4563a0ee59e5411ab626281f5da25ab2.jpg
             * modifyMan : 分析师
             * modifyTime : 2017-04-20 15:17:27.0
             * sort : 1
             * startTime : 2017-04-20 14:31:31.0
             * state : 1
             * title : 实时新闻
             * titleCode : 2
             * url : http://baidu.com
             */

            private String createMan;
            private String createTime;
            private String endTime;
            private String homeIconId;
            private String image;
            private String modifyMan;
            private String modifyTime;
            private int sort;
            private String startTime;
            private int state;
            private String title;
            private String titleCode;
            private int isH5;

            public int getIsH5() {
                return isH5;
            }

            public void setIsH5(int isH5) {
                this.isH5 = isH5;
            }

            private String url;

            public String getCreateMan() {
                return createMan;
            }

            public void setCreateMan(String createMan) {
                this.createMan = createMan;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public String getHomeIconId() {
                return homeIconId;
            }

            public void setHomeIconId(String homeIconId) {
                this.homeIconId = homeIconId;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
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

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTitleCode() {
                return titleCode;
            }

            public void setTitleCode(String titleCode) {
                this.titleCode = titleCode;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
