package com.honglu.future.ui.usercenter.bean;

/**
 * Created by zq on 2017/11/2.
 */

public class HistoryRecordsBean {
    private String name;
    private String buyHands;

    public String getBuyType() {
        return buyType;
    }

    public void setBuyType(String buyType) {
        this.buyType = buyType;
    }

    private String buyType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBuyHands() {
        return buyHands;
    }

    public void setBuyHands(String buyHands) {
        this.buyHands = buyHands;
    }

    public String getBuildPrice() {
        return buildPrice;
    }

    public void setBuildPrice(String buildPrice) {
        this.buildPrice = buildPrice;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    private String buildPrice;
    private String servicePrice;
}