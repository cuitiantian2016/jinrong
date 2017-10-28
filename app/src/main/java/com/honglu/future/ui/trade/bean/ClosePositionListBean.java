package com.honglu.future.ui.trade.bean;

/**
 * Created by zq on 2017/10/28.
 */

public class ClosePositionListBean {
    private String productName;
    private String buyHands;

    public String getBuyRiseDown() {
        return buyRiseDown;
    }

    public void setBuyRiseDown(String buyRiseDown) {
        this.buyRiseDown = buyRiseDown;
    }

    private String buyRiseDown;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBuyHands() {
        return buyHands;
    }

    public void setBuyHands(String buyHands) {
        this.buyHands = buyHands;
    }

    public String getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(String averagePrice) {
        this.averagePrice = averagePrice;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(String newPrice) {
        this.newPrice = newPrice;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    private String averagePrice;
    private String newPrice;
    private String profit;
}
