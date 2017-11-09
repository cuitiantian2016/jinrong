package com.honglu.future.ui.trade.bean;


import java.util.List;

/**
 * Created by zq on 2017/11/9.
 */

public class TickChartBean {
    private String totalVolume;
    private String closeTime;
    private String startTime;
    private String endTime;
    private int reqTime;
    private List<Data> list;
    private String middleTime;

    public void setTotalVolume(String totalVolume) {
        this.totalVolume = totalVolume;
    }

    public String getTotalVolume() {
        return totalVolume;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setReqTime(int reqTime) {
        this.reqTime = reqTime;
    }

    public int getReqTime() {
        return reqTime;
    }

    public void setList(List<Data> list) {
        this.list = list;
    }

    public List<Data> getList() {
        return list;
    }

    public void setMiddleTime(String middleTime) {
        this.middleTime = middleTime;
    }

    public String getMiddleTime() {
        return middleTime;
    }


    public class Data {
        private String t;
        private String p;
        private String v;
        private String i;

        public void setT(String t) {
            this.t = t;
        }

        public String getT() {
            return t;
        }

        public void setP(String p) {
            this.p = p;
        }

        public String getP() {
            return p;
        }

        public void setV(String v) {
            this.v = v;
        }

        public String getV() {
            return v;
        }

        public void setI(String i) {
            this.i = i;
        }

        public String getI() {
            return i;
        }

    }
}
