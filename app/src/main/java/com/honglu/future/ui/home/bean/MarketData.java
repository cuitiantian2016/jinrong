package com.honglu.future.ui.home.bean;


import java.io.Serializable;
import java.util.List;

/**
 * Created by fengpeihao on 2017/3/14.
 */

public class MarketData {
    /**
     * code : 200
     * message : 成功
     * data : {"successed":true,"data":{"dataList":[{"changeRange":"","changeValue":"1","closingPrice":"2552.00","createMan":"","createTime":"20170314153914","floorPrice":"2534.00","highestPrice":"2554.00","investProductId":"3","investProductName":"吉大豆","lastPrice":"2552.00","marketId":"072157cd450f46899f1cf2e0d73cad5a","modifyMan":"","modifyTime":"20170314153914","openingPrice":"2552.00","remark":"","state":1,"stockIndex":"2539.0","stockTime":"2017-03-14 15:39:11","todayPrice":"2539.0"},{"changeRange":"","changeValue":"1.0000","closingPrice":"21679.0000","createMan":"","createTime":"2017-03-14 02:30:01.0","floorPrice":"21237.0000","highestPrice":"21994.0000","investProductId":"2","investProductName":"吉咖啡","lastPrice":"21679.0000","marketId":"abeacba4eb3944cd8e6cd16ee69d7337","modifyMan":"","modifyTime":"2017-03-14 02:30:01.0","openingPrice":"21619.0000","remark":"","state":1,"stockIndex":"21826.0000","stockTime":"2017-03-14 02:29:58.0","todayPrice":"21826.0000"},{"changeRange":"","changeValue":"1","closingPrice":"3673.00","createMan":"","createTime":"20170314153914","floorPrice":"3662.00","highestPrice":"3676.00","investProductId":"4","investProductName":"吉尿素","lastPrice":"3673.00","marketId":"eaaa119266024426a1aae96fa5849691","modifyMan":"","modifyTime":"20170314153914","openingPrice":"3671.00","remark":"","state":1,"stockIndex":"3667.0","stockTime":"2017-03-14 15:39:11","todayPrice":"3667.0"}]}}
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
         * data : {"dataList":[{"changeRange":"","changeValue":"1","closingPrice":"2552.00","createMan":"","createTime":"20170314153914","floorPrice":"2534.00","highestPrice":"2554.00","investProductId":"3","investProductName":"吉大豆","lastPrice":"2552.00","marketId":"072157cd450f46899f1cf2e0d73cad5a","modifyMan":"","modifyTime":"20170314153914","openingPrice":"2552.00","remark":"","state":1,"stockIndex":"2539.0","stockTime":"2017-03-14 15:39:11","todayPrice":"2539.0"},{"changeRange":"","changeValue":"1.0000","closingPrice":"21679.0000","createMan":"","createTime":"2017-03-14 02:30:01.0","floorPrice":"21237.0000","highestPrice":"21994.0000","investProductId":"2","investProductName":"吉咖啡","lastPrice":"21679.0000","marketId":"abeacba4eb3944cd8e6cd16ee69d7337","modifyMan":"","modifyTime":"2017-03-14 02:30:01.0","openingPrice":"21619.0000","remark":"","state":1,"stockIndex":"21826.0000","stockTime":"2017-03-14 02:29:58.0","todayPrice":"21826.0000"},{"changeRange":"","changeValue":"1","closingPrice":"3673.00","createMan":"","createTime":"20170314153914","floorPrice":"3662.00","highestPrice":"3676.00","investProductId":"4","investProductName":"吉尿素","lastPrice":"3673.00","marketId":"eaaa119266024426a1aae96fa5849691","modifyMan":"","modifyTime":"20170314153914","openingPrice":"3671.00","remark":"","state":1,"stockIndex":"3667.0","stockTime":"2017-03-14 15:39:11","todayPrice":"3667.0"}]}
         */

        private boolean successed;
        private DataBean data;

        public boolean isSuccessed() {
            return successed;
        }

        public void setSuccessed(boolean successed) {
            this.successed = successed;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean implements Serializable{
            private List<DataListBean> dataList;

            public List<DataListBean> getDataList() {
                return dataList;
            }

            public void setDataList(List<DataListBean> dataList) {
                this.dataList = dataList;
            }

            @Override
            public String toString() {
                return "DataBean{" +
                        "dataList=" + dataList +
                        '}';
            }

            public static class DataListBean implements Serializable{
                /**
                 * changeRange :
                 * changeValue : 1
                 * closingPrice : 2552.00
                 * createMan :
                 * createTime : 20170314153914
                 * floorPrice : 2534.00
                 * highestPrice : 2554.00
                 * investProductId : 3
                 * investProductName : 吉大豆
                 * lastPrice : 2552.00
                 * marketId : 072157cd450f46899f1cf2e0d73cad5a
                 * modifyMan :
                 * modifyTime : 20170314153914
                 * openingPrice : 2552.00
                 * remark :
                 * state : 1
                 * stockIndex : 2539.0
                 * stockTime : 2017-03-14 15:39:11
                 * todayPrice : 2539.0
                 * isAddddState:false
                 */

                private String changeRange;
                private String closingPrice;
                private String createMan;
                private String createTime;
                private String floorPrice;
                private String highestPrice;
                private String investProductId;
                private String investProductName;
                private String modifyMan;
                private String modifyTime;
                private String openingPrice;
                private String remark;
                private String state;
                private String stockIndex;
                private String todayPrice;
                private boolean isAddState;
                public String market;//用户当前点击的位置
                public boolean getAddState(){
                    return isAddState;
                }
                public void setAddState(boolean isAddState){
                    this.isAddState=isAddState;
                }
                public String getClosingPrice() {
                    return closingPrice;
                }
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
                public String getFloorPrice() {
                    return floorPrice;
                }
                public String getHighestPrice() {
                    return highestPrice;
                }
                public String getInvestProductId() {
                    return investProductId;
                }
                public String getInvestProductName() {
                    return investProductName;
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
                public String getOpeningPrice() {
                    return openingPrice;
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
                public String getStockIndex() {
                    return stockIndex;
                }
                public String getTodayPrice() {
                    return todayPrice;
                }

                @Override
                public String toString() {
                    return "DataListBean{" +
                            "changeRange='" + changeRange + '\'' +
                            ", closingPrice='" + closingPrice + '\'' +
                            ", createMan='" + createMan + '\'' +
                            ", createTime='" + createTime + '\'' +
                            ", floorPrice='" + floorPrice + '\'' +
                            ", highestPrice='" + highestPrice + '\'' +
                            ", investProductId='" + investProductId + '\'' +
                            ", investProductName='" + investProductName + '\'' +
                            ", modifyMan='" + modifyMan + '\'' +
                            ", modifyTime='" + modifyTime + '\'' +
                            ", openingPrice='" + openingPrice + '\'' +
                            ", remark='" + remark + '\'' +
                            ", state='" + state + '\'' +
                            ", stockIndex='" + stockIndex + '\'' +
                            ", todayPrice='" + todayPrice + '\'' +
                            ", isAddState=" + isAddState +
                            '}';
                }
            }
        }
    }
}
