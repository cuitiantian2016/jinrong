package com.honglu.future.ui.home.bean;


import java.io.Serializable;
import java.util.List;

/**
 * Banner实体类
 */
public class BannerData {

    /**
     * data : {"successed":true,"data":[{"pic":"http://mypics.zhaopin.com/pic/2017/2/7/662F717364084C3D91CC5B95B67A6696.jpg;http://img0.imgtn.bdimg.com/it/u=2134694061,3961462795&fm=23&gp=0.jpg","columnUrl":"http://mypics.zhaopin.com/pic/2017/2/7/662F717364084C3D91CC5B95B67A6696.jpg"}]}
     */
    private DataBeanX data;
    public DataBeanX getData() {
        return data;
    }
    public void setData(DataBeanX data) {
        this.data = data;
    }
    public static class DataBeanX implements Serializable{
        /**
         * successed : true
         * data : [{"pic":"http://mypics.zhaopin.com/pic/2017/2/7/662F717364084C3D91CC5B95B67A6696.jpg;http://img0.imgtn.bdimg.com/it/u=2134694061,3961462795&fm=23&gp=0.jpg","columnUrl":"http://mypics.zhaopin.com/pic/2017/2/7/662F717364084C3D91CC5B95B67A6696.jpg"}]
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

        public static class DataBean implements Serializable {
            /**
             * pic : http://mypics.zhaopin.com/pic/2017/2/7/662F717364084C3D91CC5B95B67A6696.jpg;http://img0.imgtn.bdimg.com/it/u=2134694061,3961462795&fm=23&gp=0.jpg
             * columnUrl : http://mypics.zhaopin.com/pic/2017/2/7/662F717364084C3D91CC5B95B67A6696.jpg
             */
            private String pic;
            private String columnUrl;
            private String columnName;

            public String getCircleColumnName() {
                return columnName;
            }

            public void setCircleColumnName(String circleColumnName) {
                this.columnName = circleColumnName;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }

            public String getColumnUrl() {
                return columnUrl;
            }

            public void setColumnUrl(String columnUrl) {
                this.columnUrl = columnUrl;
            }
        }
    }
}
