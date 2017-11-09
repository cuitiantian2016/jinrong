package com.honglu.future.ui.trade.bean;

import java.util.List;

/**
 * Created by zq on 2017/11/9.
 */

public class RealTimeBean {
    private List<Data> list;

    public void setList(List<Data> list) {
        this.list = list;
    }

    public List<Data> getList() {
        return list;
    }

    public class Data {
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
        private String startDeliveryDate;
        private int decimalPrecision;
        private String change;
        private String chg;
        private String name;

        public void setTradingDay(String tradingDay) {
            this.tradingDay = tradingDay;
        }

        public String getTradingDay() {
            return tradingDay;
        }

        public void setInstrumentID(String instrumentID) {
            this.instrumentID = instrumentID;
        }

        public String getInstrumentID() {
            return instrumentID;
        }

        public void setExchangeID(String exchangeID) {
            this.exchangeID = exchangeID;
        }

        public String getExchangeID() {
            return exchangeID;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductId() {
            return productId;
        }

        public void setLastPrice(String lastPrice) {
            this.lastPrice = lastPrice;
        }

        public String getLastPrice() {
            return lastPrice;
        }

        public void setPreSettlementPrice(String preSettlementPrice) {
            this.preSettlementPrice = preSettlementPrice;
        }

        public String getPreSettlementPrice() {
            return preSettlementPrice;
        }

        public void setPreClosePrice(String preClosePrice) {
            this.preClosePrice = preClosePrice;
        }

        public String getPreClosePrice() {
            return preClosePrice;
        }

        public void setPreOpenInterest(String preOpenInterest) {
            this.preOpenInterest = preOpenInterest;
        }

        public String getPreOpenInterest() {
            return preOpenInterest;
        }

        public void setOpenPrice(String openPrice) {
            this.openPrice = openPrice;
        }

        public String getOpenPrice() {
            return openPrice;
        }

        public void setHighestPrice(String highestPrice) {
            this.highestPrice = highestPrice;
        }

        public String getHighestPrice() {
            return highestPrice;
        }

        public void setLowestPrice(String lowestPrice) {
            this.lowestPrice = lowestPrice;
        }

        public String getLowestPrice() {
            return lowestPrice;
        }

        public void setVolume(String volume) {
            this.volume = volume;
        }

        public String getVolume() {
            return volume;
        }

        public void setTurnover(String turnover) {
            this.turnover = turnover;
        }

        public String getTurnover() {
            return turnover;
        }

        public void setOpenInterest(String openInterest) {
            this.openInterest = openInterest;
        }

        public String getOpenInterest() {
            return openInterest;
        }

        public void setClosePrice(String closePrice) {
            this.closePrice = closePrice;
        }

        public String getClosePrice() {
            return closePrice;
        }

        public void setSettlementPrice(String settlementPrice) {
            this.settlementPrice = settlementPrice;
        }

        public String getSettlementPrice() {
            return settlementPrice;
        }

        public void setUpperLimitPrice(String upperLimitPrice) {
            this.upperLimitPrice = upperLimitPrice;
        }

        public String getUpperLimitPrice() {
            return upperLimitPrice;
        }

        public void setLowerLimitPrice(String lowerLimitPrice) {
            this.lowerLimitPrice = lowerLimitPrice;
        }

        public String getLowerLimitPrice() {
            return lowerLimitPrice;
        }

        public void setPreDelta(String preDelta) {
            this.preDelta = preDelta;
        }

        public String getPreDelta() {
            return preDelta;
        }

        public void setCurrDelta(String currDelta) {
            this.currDelta = currDelta;
        }

        public String getCurrDelta() {
            return currDelta;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateMillisecond(String updateMillisecond) {
            this.updateMillisecond = updateMillisecond;
        }

        public String getUpdateMillisecond() {
            return updateMillisecond;
        }

        public void setBidPrice1(String bidPrice1) {
            this.bidPrice1 = bidPrice1;
        }

        public String getBidPrice1() {
            return bidPrice1;
        }

        public void setBidVolume1(String bidVolume1) {
            this.bidVolume1 = bidVolume1;
        }

        public String getBidVolume1() {
            return bidVolume1;
        }

        public void setAskPrice1(String askPrice1) {
            this.askPrice1 = askPrice1;
        }

        public String getAskPrice1() {
            return askPrice1;
        }

        public void setAskVolume1(String askVolume1) {
            this.askVolume1 = askVolume1;
        }

        public String getAskVolume1() {
            return askVolume1;
        }

        public void setBidPrice2(String bidPrice2) {
            this.bidPrice2 = bidPrice2;
        }

        public String getBidPrice2() {
            return bidPrice2;
        }

        public void setBidVolume2(String bidVolume2) {
            this.bidVolume2 = bidVolume2;
        }

        public String getBidVolume2() {
            return bidVolume2;
        }

        public void setAskPrice2(String askPrice2) {
            this.askPrice2 = askPrice2;
        }

        public String getAskPrice2() {
            return askPrice2;
        }

        public void setAskVolume2(String askVolume2) {
            this.askVolume2 = askVolume2;
        }

        public String getAskVolume2() {
            return askVolume2;
        }

        public void setBidPrice3(String bidPrice3) {
            this.bidPrice3 = bidPrice3;
        }

        public String getBidPrice3() {
            return bidPrice3;
        }

        public void setBidVolume3(String bidVolume3) {
            this.bidVolume3 = bidVolume3;
        }

        public String getBidVolume3() {
            return bidVolume3;
        }

        public void setAskPrice3(String askPrice3) {
            this.askPrice3 = askPrice3;
        }

        public String getAskPrice3() {
            return askPrice3;
        }

        public void setAskVolume3(String askVolume3) {
            this.askVolume3 = askVolume3;
        }

        public String getAskVolume3() {
            return askVolume3;
        }

        public void setBidPrice4(String bidPrice4) {
            this.bidPrice4 = bidPrice4;
        }

        public String getBidPrice4() {
            return bidPrice4;
        }

        public void setBidVolume4(String bidVolume4) {
            this.bidVolume4 = bidVolume4;
        }

        public String getBidVolume4() {
            return bidVolume4;
        }

        public void setAskPrice4(String askPrice4) {
            this.askPrice4 = askPrice4;
        }

        public String getAskPrice4() {
            return askPrice4;
        }

        public void setAskVolume4(String askVolume4) {
            this.askVolume4 = askVolume4;
        }

        public String getAskVolume4() {
            return askVolume4;
        }

        public void setBidPrice5(String bidPrice5) {
            this.bidPrice5 = bidPrice5;
        }

        public String getBidPrice5() {
            return bidPrice5;
        }

        public void setBidVolume5(String bidVolume5) {
            this.bidVolume5 = bidVolume5;
        }

        public String getBidVolume5() {
            return bidVolume5;
        }

        public void setAskPrice5(String askPrice5) {
            this.askPrice5 = askPrice5;
        }

        public String getAskPrice5() {
            return askPrice5;
        }

        public void setAskVolume5(String askVolume5) {
            this.askVolume5 = askVolume5;
        }

        public String getAskVolume5() {
            return askVolume5;
        }

        public void setAveragePrice(String averagePrice) {
            this.averagePrice = averagePrice;
        }

        public String getAveragePrice() {
            return averagePrice;
        }

        public void setActionDay(String actionDay) {
            this.actionDay = actionDay;
        }

        public String getActionDay() {
            return actionDay;
        }

        public void setQuotationDateTime(String quotationDateTime) {
            this.quotationDateTime = quotationDateTime;
        }

        public String getQuotationDateTime() {
            return quotationDateTime;
        }

        public void setStartDeliveryDate(String startDeliveryDate) {
            this.startDeliveryDate = startDeliveryDate;
        }

        public String getStartDeliveryDate() {
            return startDeliveryDate;
        }

        public void setDecimalPrecision(int decimalPrecision) {
            this.decimalPrecision = decimalPrecision;
        }

        public int getDecimalPrecision() {
            return decimalPrecision;
        }

        public void setChange(String change) {
            this.change = change;
        }

        public String getChange() {
            return change;
        }

        public void setChg(String chg) {
            this.chg = chg;
        }

        public String getChg() {
            return chg;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }
}
