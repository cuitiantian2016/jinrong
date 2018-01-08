package com.honglu.future.ui.market.bean;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhuaibing on 2018/1/8
 */

public class ListBean implements Serializable{
        /**
         * excode : SHFE
         * exchangeName : 上期所
         * quotationDataList : [{"tradingDay":"20171031","instrumentID":"ag1712","exchangeID":"SHFE","productId":"ag","lastPrice":"3846","preSettlementPrice":"3837","preClosePrice":"3841","preOpenInterest":"493898","openPrice":"3835","highestPrice":"3851","lowestPrice":"3831","volume":"153698","turnover":"8861273550","openInterest":"474256","closePrice":"0","settlementPrice":"0","upperLimitPrice":"4028","lowerLimitPrice":"3645","preDelta":"0","currDelta":"0","updateTime":"14:09:07","updateMillisecond":"500","bidPrice1":"3846","bidVolume1":"56","askPrice1":"3847","askVolume1":"179","bidPrice2":"0","bidVolume2":"0","askPrice2":"0","askVolume2":"0","bidPrice3":"0","bidVolume3":"0","askPrice3":"0","askVolume3":"0","bidPrice4":"0","bidVolume4":"0","askPrice4":"0","askVolume4":"0","bidPrice5":"0","bidVolume5":"0","askPrice5":"0","askVolume5":"0","averagePrice":"3844","actionDay":"20171031","quotationDateTime":"2017-10-31 14:09:08","startDeliveryDate":null,"decimalPrecision":0,"change":"9","chg":"0.23%","name":"白银1712"},{"tradingDay":"20171031","instrumentID":"ag","exchangeID":"SHFE","productId":"ag","lastPrice":"3846","preSettlementPrice":"3837","preClosePrice":"3841","preOpenInterest":"493898","openPrice":"3835","highestPrice":"3851","lowestPrice":"3831","volume":"153698","turnover":"8861273550","openInterest":"474256","closePrice":"0","settlementPrice":"0","upperLimitPrice":"4028","lowerLimitPrice":"3645","preDelta":"0","currDelta":"0","updateTime":"14:09:07","updateMillisecond":"500","bidPrice1":"3846","bidVolume1":"56","askPrice1":"3847","askVolume1":"179","bidPrice2":"0","bidVolume2":"0","askPrice2":"0","askVolume2":"0","bidPrice3":"0","bidVolume3":"0","askPrice3":"0","askVolume3":"0","bidPrice4":"0","bidVolume4":"0","askPrice4":"0","askVolume4":"0","bidPrice5":"0","bidVolume5":"0","askPrice5":"0","askVolume5":"0","averagePrice":"3844","actionDay":"20171031","quotationDateTime":"2017-10-31 14:09:08","startDeliveryDate":null,"decimalPrecision":0,"change":"9","chg":"0.23%","name":"白银主连"},{"tradingDay":"20171031","instrumentID":"au1712","exchangeID":"SHFE","productId":"au","lastPrice":"274.50","preSettlementPrice":"274.30","preClosePrice":"274.30","preOpenInterest":"220844","openPrice":"274.25","highestPrice":"275.15","lowestPrice":"274.15","volume":"113666","turnover":"31217801600.00","openInterest":"204136","closePrice":"0","settlementPrice":"0","upperLimitPrice":"288.00","lowerLimitPrice":"260.55","preDelta":"0","currDelta":"0","updateTime":"14:09:05","updateMillisecond":"500","bidPrice1":"274.45","bidVolume1":"523","askPrice1":"274.50","askVolume1":"47","bidPrice2":"0","bidVolume2":"0","askPrice2":"0","askVolume2":"0","bidPrice3":"0","bidVolume3":"0","askPrice3":"0","askVolume3":"0","bidPrice4":"0","bidVolume4":"0","askPrice4":"0","askVolume4":"0","bidPrice5":"0","bidVolume5":"0","askPrice5":"0","askVolume5":"0","averagePrice":"274.65","actionDay":"20171031","quotationDateTime":"2017-10-31 14:09:06","startDeliveryDate":null,"decimalPrecision":2,"change":"0.2","chg":"0.07%","name":"黄金1712"},{"tradingDay":"20171031","instrumentID":"au","exchangeID":"SHFE","productId":"au","lastPrice":"274.50","preSettlementPrice":"274.30","preClosePrice":"274.30","preOpenInterest":"220844","openPrice":"274.25","highestPrice":"275.15","lowestPrice":"274.15","volume":"113666","turnover":"31217801600.00","openInterest":"204136","closePrice":"0","settlementPrice":"0","upperLimitPrice":"288.00","lowerLimitPrice":"260.55","preDelta":"0","currDelta":"0","updateTime":"14:09:05","updateMillisecond":"500","bidPrice1":"274.45","bidVolume1":"523","askPrice1":"274.50","askVolume1":"47","bidPrice2":"0","bidVolume2":"0","askPrice2":"0","askVolume2":"0","bidPrice3":"0","bidVolume3":"0","askPrice3":"0","askVolume3":"0","bidPrice4":"0","bidVolume4":"0","askPrice4":"0","askVolume4":"0","bidPrice5":"0","bidVolume5":"0","askPrice5":"0","askVolume5":"0","averagePrice":"274.65","actionDay":"20171031","quotationDateTime":"2017-10-31 14:09:06","startDeliveryDate":null,"decimalPrecision":2,"change":"0.2","chg":"0.07%","name":"黄金主连"},{"tradingDay":"20171031","instrumentID":"rb1801","exchangeID":"SHFE","productId":"rb","lastPrice":"3630","preSettlementPrice":"3591","preClosePrice":"3571","preOpenInterest":"2600058","openPrice":"3575","highestPrice":"3649","lowestPrice":"3535","volume":"2532354","turnover":"90878778260","openInterest":"2619054","closePrice":"0","settlementPrice":"0","upperLimitPrice":"3842","lowerLimitPrice":"3339","preDelta":"0","currDelta":"0","updateTime":"14:09:07","updateMillisecond":"500","bidPrice1":"3629","bidVolume1":"100","askPrice1":"3630","askVolume1":"66","bidPrice2":"0","bidVolume2":"0","askPrice2":"0","askVolume2":"0","bidPrice3":"0","bidVolume3":"0","askPrice3":"0","askVolume3":"0","bidPrice4":"0","bidVolume4":"0","askPrice4":"0","askVolume4":"0","bidPrice5":"0","bidVolume5":"0","askPrice5":"0","askVolume5":"0","averagePrice":"3589","actionDay":"20171031","quotationDateTime":"2017-10-31 14:09:08","startDeliveryDate":null,"decimalPrecision":0,"change":"39","chg":"1.09%","name":"螺纹1801"},{"tradingDay":"20171031","instrumentID":"rb","exchangeID":"SHFE","productId":"rb","lastPrice":"3630","preSettlementPrice":"3591","preClosePrice":"3571","preOpenInterest":"2600058","openPrice":"3575","highestPrice":"3649","lowestPrice":"3535","volume":"2532354","turnover":"90878778260","openInterest":"2619054","closePrice":"0","settlementPrice":"0","upperLimitPrice":"3842","lowerLimitPrice":"3339","preDelta":"0","currDelta":"0","updateTime":"14:09:07","updateMillisecond":"500","bidPrice1":"3629","bidVolume1":"100","askPrice1":"3630","askVolume1":"66","bidPrice2":"0","bidVolume2":"0","askPrice2":"0","askVolume2":"0","bidPrice3":"0","bidVolume3":"0","askPrice3":"0","askVolume3":"0","bidPrice4":"0","bidVolume4":"0","askPrice4":"0","askVolume4":"0","bidPrice5":"0","bidVolume5":"0","askPrice5":"0","askVolume5":"0","averagePrice":"3589","actionDay":"20171031","quotationDateTime":"2017-10-31 14:09:08","startDeliveryDate":null,"decimalPrecision":0,"change":"39","chg":"1.09%","name":"螺纹主连"}]
         */

        private String excode;
        private String exchangeName;
        private List<QuotationDataListBean> quotationDataList;

        public String getExcode() {
            return excode;
        }

        public void setExcode(String excode) {
            this.excode = excode;
        }

        public String getExchangeName() {
            return exchangeName;
        }

        public void setExchangeName(String exchangeName) {
            this.exchangeName = exchangeName;
        }

        public List<QuotationDataListBean> getQuotationDataList() {
            return quotationDataList;
        }

        public void setQuotationDataList(List<QuotationDataListBean> quotationDataList) {
            this.quotationDataList = quotationDataList;
        }
}
