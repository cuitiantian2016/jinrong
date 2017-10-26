package com.honglu.future.ui.market.bean;

/**
 * Created by hc on 2017/10/25.
 */

public class QuotesItemBean {
    private String contractName;//合约名称
    private String latestPrice;//最新价
    private String quoteChange;//涨幅量
    private String havedPositions;//持仓量

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getLatestPrice() {
        return latestPrice;
    }

    public void setLatestPrice(String latestPrice) {
        this.latestPrice = latestPrice;
    }

    public String getQuoteChange() {
        return quoteChange;
    }

    public void setQuoteChange(String quoteChange) {
        this.quoteChange = quoteChange;
    }

    public String getHavedPositions() {
        return havedPositions;
    }

    public void setHavedPositions(String havedPositions) {
        this.havedPositions = havedPositions;
    }

    @Override
    public String toString() {
        return "QuotesItemBean{" +
                "contractName='" + contractName + '\'' +
                ", latestPrice='" + latestPrice + '\'' +
                ", quoteChange='" + quoteChange + '\'' +
                ", havedPositions='" + havedPositions + '\'' +
                '}';
    }
}
