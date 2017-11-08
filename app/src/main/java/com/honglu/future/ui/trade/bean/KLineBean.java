package com.honglu.future.ui.trade.bean;

import java.util.List;

/**
 * Created by zq on 2017/11/7.
 */

public class KLineBean {
    private String totalVolume;
    private List<Candle> candle;
    private int reqTime;

    public void setTotalVolume(String totalVolume) {
        this.totalVolume = totalVolume;
    }

    public String getTotalVolume() {
        return totalVolume;
    }

    public void setCandle(List<Candle> candle) {
        this.candle = candle;
    }

    public List<Candle> getCandle() {
        return candle;
    }

    public void setReqTime(int reqTime) {
        this.reqTime = reqTime;
    }

    public int getReqTime() {
        return reqTime;
    }

    public class Candle {

        private String t;
        private String v;
        private String c;
        private String o;
        private String l;
        private String h;
        private long u;
        private String i;

        public void setT(String t) {
            this.t = t;
        }

        public String getT() {
            return t;
        }

        public void setV(String v) {
            this.v = v;
        }

        public String getV() {
            return v;
        }

        public void setC(String c) {
            this.c = c;
        }

        public String getC() {
            return c;
        }

        public void setO(String o) {
            this.o = o;
        }

        public String getO() {
            return o;
        }

        public void setL(String l) {
            this.l = l;
        }

        public String getL() {
            return l;
        }

        public void setH(String h) {
            this.h = h;
        }

        public String getH() {
            return h;
        }

        public void setU(long u) {
            this.u = u;
        }

        public long getU() {
            return u;
        }

        public void setI(String i) {
            this.i = i;
        }

        public String getI() {
            return i;
        }

    }

}
