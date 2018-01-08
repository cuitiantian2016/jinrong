package com.honglu.future.ui.market.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by zhuaibing on 2018/1/8
 */

public class QuotationDataListBean implements Serializable {
    /**
     * tradingDay : 20171031
     * instrumentID : ag1712
     * exchangeID : SHFE
     * productId : ag
     * lastPrice : 3846
     * preSettlementPrice : 3837
     * preClosePrice : 3841
     * preOpenInterest : 493898
     * openPrice : 3835
     * highestPrice : 3851
     * lowestPrice : 3831
     * volume : 153698
     * turnover : 8861273550
     * openInterest : 474256
     * closePrice : 0
     * settlementPrice : 0
     * upperLimitPrice : 4028
     * lowerLimitPrice : 3645
     * preDelta : 0
     * currDelta : 0
     * updateTime : 14:09:07
     * updateMillisecond : 500
     * bidPrice1 : 3846
     * bidVolume1 : 56
     * askPrice1 : 3847
     * askVolume1 : 179
     * bidPrice2 : 0
     * bidVolume2 : 0
     * askPrice2 : 0
     * askVolume2 : 0
     * bidPrice3 : 0
     * bidVolume3 : 0
     * askPrice3 : 0
     * askVolume3 : 0
     * bidPrice4 : 0
     * bidVolume4 : 0
     * askPrice4 : 0
     * askVolume4 : 0
     * bidPrice5 : 0
     * bidVolume5 : 0
     * askPrice5 : 0
     * askVolume5 : 0
     * averagePrice : 3844
     * actionDay : 20171031
     * quotationDateTime : 2017-10-31 14:09:08
     * startDeliveryDate : null
     * decimalPrecision : 0
     * change : 9
     * chg : 0.23%
     * name : 白银1712
     */

    private String isIcAdd;
    private String excode;    //1.0.0取  1.0.0 后 取exchangeID
    private String tradingDay;
    private String instrumentID;
    private String exchangeID;
    private String productId;
    private String lastPrice;
    private String preSettlementPrice;
    private String preClosePrice;
    private String preOpenInterest;
    private String openPrice;
    private String highestPrice;
    private String lowestPrice;
    private String volume;
    private String turnover;
    private String openInterest;
    private String closePrice;
    private String settlementPrice;
    private String upperLimitPrice;
    private String lowerLimitPrice;
    private String preDelta;
    private String currDelta;
    private String updateTime;
    private String updateMillisecond;
    private String bidPrice1;
    private String bidVolume1;
    private String askPrice1;
    private String askVolume1;
    private String bidPrice2;
    private String bidVolume2;
    private String askPrice2;
    private String askVolume2;
    private String bidPrice3;
    private String bidVolume3;
    private String askPrice3;
    private String askVolume3;
    private String bidPrice4;
    private String bidVolume4;
    private String askPrice4;
    private String askVolume4;
    private String bidPrice5;
    private String bidVolume5;
    private String askPrice5;
    private String askVolume5;
    private String averagePrice;
    private String actionDay;
    private String quotationDateTime;
    private MarketnalysisBean.DeliveryDate startDeliveryDate;
    private int decimalPrecision;
    private String change;
    private String chg;
    private String name;
    private int isClosed; //1 开市 2 休市

    public int getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(int isClosed) {
        this.isClosed = isClosed;
    }

    public String getIcAdd() {
        return isIcAdd;
    }

    public void setIcAdd(String icAdd) {
        isIcAdd = icAdd;
    }

    public String getExcode() {
        return excode;
    }

    public void setExcode(String excode) {
        this.excode = excode;
    }

    public String getTradingDay() {
        return tradingDay;
    }

    public void setTradingDay(String tradingDay) {
        this.tradingDay = tradingDay;
    }

    public String getInstrumentID() {
        return instrumentID;
    }

    public void setInstrumentID(String instrumentID) {
        this.instrumentID = instrumentID;
    }

    public String getExchangeID() {
        return exchangeID;
    }

    public void setExchangeID(String exchangeID) {
        this.exchangeID = exchangeID;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(String lastPrice) {
        this.lastPrice = lastPrice;
    }

    public String getPreSettlementPrice() {
        return preSettlementPrice;
    }

    public void setPreSettlementPrice(String preSettlementPrice) {
        this.preSettlementPrice = preSettlementPrice;
    }

    public String getPreClosePrice() {
        return preClosePrice;
    }

    public void setPreClosePrice(String preClosePrice) {
        this.preClosePrice = preClosePrice;
    }

    public String getPreOpenInterest() {
        return preOpenInterest;
    }

    public void setPreOpenInterest(String preOpenInterest) {
        this.preOpenInterest = preOpenInterest;
    }

    public String getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(String openPrice) {
        this.openPrice = openPrice;
    }

    public String getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(String highestPrice) {
        this.highestPrice = highestPrice;
    }

    public String getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(String lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getTurnover() {
        return turnover;
    }

    public void setTurnover(String turnover) {
        this.turnover = turnover;
    }

    public String getOpenInterest() {
        return openInterest;
    }

    public void setOpenInterest(String openInterest) {
        this.openInterest = openInterest;
    }

    public String getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(String closePrice) {
        this.closePrice = closePrice;
    }

    public String getSettlementPrice() {
        return settlementPrice;
    }

    public void setSettlementPrice(String settlementPrice) {
        this.settlementPrice = settlementPrice;
    }

    public String getUpperLimitPrice() {
        return upperLimitPrice;
    }

    public void setUpperLimitPrice(String upperLimitPrice) {
        this.upperLimitPrice = upperLimitPrice;
    }

    public String getLowerLimitPrice() {
        return lowerLimitPrice;
    }

    public void setLowerLimitPrice(String lowerLimitPrice) {
        this.lowerLimitPrice = lowerLimitPrice;
    }

    public String getPreDelta() {
        return preDelta;
    }

    public void setPreDelta(String preDelta) {
        this.preDelta = preDelta;
    }

    public String getCurrDelta() {
        return currDelta;
    }

    public void setCurrDelta(String currDelta) {
        this.currDelta = currDelta;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateMillisecond() {
        return updateMillisecond;
    }

    public void setUpdateMillisecond(String updateMillisecond) {
        this.updateMillisecond = updateMillisecond;
    }

    public String getBidPrice1() {
        return bidPrice1;
    }

    public void setBidPrice1(String bidPrice1) {
        this.bidPrice1 = bidPrice1;
    }

    public String getBidVolume1() {
        return bidVolume1;
    }

    public void setBidVolume1(String bidVolume1) {
        this.bidVolume1 = bidVolume1;
    }

    public String getAskPrice1() {
        return askPrice1;
    }

    public void setAskPrice1(String askPrice1) {
        this.askPrice1 = askPrice1;
    }

    public String getAskVolume1() {
        return askVolume1;
    }

    public void setAskVolume1(String askVolume1) {
        this.askVolume1 = askVolume1;
    }

    public String getBidPrice2() {
        return bidPrice2;
    }

    public void setBidPrice2(String bidPrice2) {
        this.bidPrice2 = bidPrice2;
    }

    public String getBidVolume2() {
        return bidVolume2;
    }

    public void setBidVolume2(String bidVolume2) {
        this.bidVolume2 = bidVolume2;
    }

    public String getAskPrice2() {
        return askPrice2;
    }

    public void setAskPrice2(String askPrice2) {
        this.askPrice2 = askPrice2;
    }

    public String getAskVolume2() {
        return askVolume2;
    }

    public void setAskVolume2(String askVolume2) {
        this.askVolume2 = askVolume2;
    }

    public String getBidPrice3() {
        return bidPrice3;
    }

    public void setBidPrice3(String bidPrice3) {
        this.bidPrice3 = bidPrice3;
    }

    public String getBidVolume3() {
        return bidVolume3;
    }

    public void setBidVolume3(String bidVolume3) {
        this.bidVolume3 = bidVolume3;
    }

    public String getAskPrice3() {
        return askPrice3;
    }

    public void setAskPrice3(String askPrice3) {
        this.askPrice3 = askPrice3;
    }

    public String getAskVolume3() {
        return askVolume3;
    }

    public void setAskVolume3(String askVolume3) {
        this.askVolume3 = askVolume3;
    }

    public String getBidPrice4() {
        return bidPrice4;
    }

    public void setBidPrice4(String bidPrice4) {
        this.bidPrice4 = bidPrice4;
    }

    public String getBidVolume4() {
        return bidVolume4;
    }

    public void setBidVolume4(String bidVolume4) {
        this.bidVolume4 = bidVolume4;
    }

    public String getAskPrice4() {
        return askPrice4;
    }

    public void setAskPrice4(String askPrice4) {
        this.askPrice4 = askPrice4;
    }

    public String getAskVolume4() {
        return askVolume4;
    }

    public void setAskVolume4(String askVolume4) {
        this.askVolume4 = askVolume4;
    }

    public String getBidPrice5() {
        return bidPrice5;
    }

    public void setBidPrice5(String bidPrice5) {
        this.bidPrice5 = bidPrice5;
    }

    public String getBidVolume5() {
        return bidVolume5;
    }

    public void setBidVolume5(String bidVolume5) {
        this.bidVolume5 = bidVolume5;
    }

    public String getAskPrice5() {
        return askPrice5;
    }

    public void setAskPrice5(String askPrice5) {
        this.askPrice5 = askPrice5;
    }

    public String getAskVolume5() {
        return askVolume5;
    }

    public void setAskVolume5(String askVolume5) {
        this.askVolume5 = askVolume5;
    }

    public String getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(String averagePrice) {
        this.averagePrice = averagePrice;
    }

    public String getActionDay() {
        return actionDay;
    }

    public void setActionDay(String actionDay) {
        this.actionDay = actionDay;
    }

    public String getQuotationDateTime() {
        return quotationDateTime;
    }

    public void setQuotationDateTime(String quotationDateTime) {
        this.quotationDateTime = quotationDateTime;
    }

    public MarketnalysisBean.DeliveryDate getStartDeliveryDate() {
        return startDeliveryDate;
    }

    public void setStartDeliveryDate(MarketnalysisBean.DeliveryDate startDeliveryDate) {
        this.startDeliveryDate = startDeliveryDate;
    }

    public int getDecimalPrecision() {
        return decimalPrecision;
    }

    public void setDecimalPrecision(int decimalPrecision) {
        this.decimalPrecision = decimalPrecision;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getChg() {
        return chg;
    }

    public void setChg(String chg) {
        this.chg = chg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (!TextUtils.isEmpty(instrumentID)) {
            QuotationDataListBean obj1 = (QuotationDataListBean) obj;
            return instrumentID.equals(obj1.getInstrumentID());
        }
        return super.equals(obj);
    }
}
