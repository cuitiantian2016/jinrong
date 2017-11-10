package com.honglu.future.ui.trade.bean;

import java.io.Serializable;

/**
 * Created by hefei on 2017/11/1.
 *
 {
 "instrumentId": "ag1711",
 "instrumentName": "白银1711",
 "openPrice": "3873.00",
 "openSxf": "5.81",
 "openTime": "2017-10-19 13:57:59",
 "orderSysId": "681327",
 "position": 2,
 "tradeId": "161063",
 "type": 2,
 "useMargin": "9295.2"
 }
 */

public class CloseBuiderBean implements Serializable{
    public String instrumentId;
    public String instrumentName;
    public String openPrice;
    public String openSxf;
    public String openTime;
    public String orderSysId;
    public int position;
    public String tradeId;
    public int type;
    public String useMargin;
}
