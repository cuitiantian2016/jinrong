package com.honglu.future.ui.trade.bean;

import java.io.Serializable;

/**
 * Created by hefei on 2017/11/1.
 *
 *            "cancelTime": "2017-10-19 14:59:40",
 "cancelType": 2,
 "createTime": "2017-10-19 14:59:39",
 "exchangeName": "上期所",
 "excode": "SHFE",
 "instrumentId": "ag1711",
 "instrumentName": "白银1711",
 "openClose": 2,
 "position": 2,
 "price": "3873",
 "productId": "ag",
 "productName": "白银",
 "timeCondition": 3,
 "type": 2
 */

public class HistoryMissPositionBean implements Serializable {
    public String cancelTime;
    public int cancelType;
    public String createTime;
    public String exchangeName;
    public String excode;
    public String instrumentId;
    public String instrumentName;
    public int openClose;
    public int position;
    public String price;
    public String productId;
    public String productName;
    public int timeCondition;
    public int type;
}
