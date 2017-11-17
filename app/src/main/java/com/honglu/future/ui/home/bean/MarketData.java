package com.honglu.future.ui.home.bean;


import java.util.ArrayList;

/**
 * Created by hefei on 2017/3/14.
 *
 *             "tradingDay": "20171031",
 "instrumentID": "ag1712",
 "exchangeID": "SHFE",
 "productId": "ag",
 "lastPrice": "3846",
 "preSettlementPrice": "3837",
 "preClosePrice": "3841",
 "preOpenInterest": "493898",
 "openPrice": "3835",
 "highestPrice": "3851",
 "lowestPrice": "3831",
 "volume": "153700",
 "turnover": "8861388930",
 "openInterest": "474256",
 "closePrice": "0",
 "settlementPrice": "0",
 "upperLimitPrice": "4028",
 "lowerLimitPrice": "3645",
 "preDelta": "0",
 "currDelta": "0",
 "updateTime": "14:09:08",
 "updateMillisecond": "0",
 "bidPrice1": "3846",
 "bidVolume1": "55",
 "askPrice1": "3847",
 "askVolume1": "179",
 "bidPrice2": "0",
 "bidVolume2": "0",
 "askPrice2": "0",
 "askVolume2": "0",
 "bidPrice3": "0",

 "bidVolume3": "0",
 "askPrice3": "0",
 "askVolume3": "0",
 "bidPrice4": "0",
 "bidVolume4": "0",
 "askPrice4": "0",
 "askVolume4": "0",
 "bidPrice5": "0",
 "bidVolume5": "0",
 "askPrice5": "0",
 "askVolume5": "0",

 "averagePrice": "3844",


 "actionDay": "20171031",
 "quotationDateTime": "2017-10-31 14:09:08",
 "startDeliveryDate": null,
 "decimalPrecision": 0,
 "change": "9",
 "chg": "0.23%",
 "name": "白银1712"
 */

public class MarketData {
public ArrayList<MarketDataBean> list;
          public static class MarketDataBean{
                public String tradingDay;
                public String instrumentID;
                public String exchangeID;
                public String productId;
                public String lastPrice;//最新价
                public String preSettlementPrice;
                public String preClosePrice;
                public String openPrice;
                public String highestPrice;
                public String lowestPrice;
                public String volume;
                public String turnover;
                public String openInterest;
                public String closePrice;
                public String settlementPrice;
                public String upperLimitPrice;
                public String lowerLimitPrice;
                public String preDelta;
                public String currDelta;
                public String updateTime;
                public String updateMillisecond;
                public String bidPrice1;
                public String bidVolume1;
                public String askPrice1;
                public String askVolume1;
                public String bidPrice2;
                public String bidVolume2;
                public String askPrice2;
                public String askVolume2;
                public String bidPrice3;
                public String bidVolume3;
                public String askPrice3;
                public String askVolume3;
                public String bidPrice4;
                public String bidVolume4;
                public String askPrice4;
                public String askVolume4;
                public String bidPrice5;
                public String bidVolume5;
                public String askPrice5;
                public String askVolume5;
                public String averagePrice;
                public String actionDay;
                public String quotationDateTime;
                public String startDeliveryDate;
                public String decimalPrecision;
                public String change; //改变量
                public String chg; //利率
                public String name; //名字
                /**
                 * 获取产品 excode|code
                 *
                 * @return
                 */
                public String getCode() {
                      return exchangeID + "|" + instrumentID;
                }

          }

}
