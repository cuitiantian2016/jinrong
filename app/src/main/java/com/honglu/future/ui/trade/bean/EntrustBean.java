package com.honglu.future.ui.trade.bean;

/**
 * Created by zq on 2017/10/30.
 */

public class EntrustBean {
    private String name;
    private String buyHands;

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

    public String getEntrustType() {
        return entrustType;
    }

    public void setEntrustType(String entrustType) {
        this.entrustType = entrustType;
    }

    public String getEnturstPrice() {
        return enturstPrice;
    }

    public void setEnturstPrice(String enturstPrice) {
        this.enturstPrice = enturstPrice;
    }

    public String getEntrustDate() {
        return entrustDate;
    }

    public void setEntrustDate(String entrustDate) {
        this.entrustDate = entrustDate;
    }

    public String getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public String getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(String limitDate) {
        this.limitDate = limitDate;
    }

    public String getBond() {
        return bond;
    }

    public void setBond(String bond) {
        this.bond = bond;
    }

    private String entrustType;
    private String enturstPrice;
    private String entrustDate;
    private String serviceCharge;
    private String limitDate;
    private String bond;
}
