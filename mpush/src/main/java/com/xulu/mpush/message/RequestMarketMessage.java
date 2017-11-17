/*
 * (C) Copyright 2015-2016 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * Contributors:
 * ohun@live.cn (夜色)
 */
package com.xulu.mpush.message;

import android.util.Log;

import com.xulu.mpush.api.connection.Connection;
import com.xulu.mpush.api.protocol.Command;
import com.xulu.mpush.api.protocol.Packet;
import com.xulu.mpush.util.ByteBuf;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hefei on 2015/12/30.
 * 自定义的请求
 */
public final class RequestMarketMessage extends ByteBufMessage {

    private static final String TAG = "RequestMarketMessage";
    ///申卖价一
    private String askPrice1;
    ///申卖量一
    private String askVolume1;
    ///申买价二
    private String bidPrice2;
    ///申买量二
    private String bidVolume2;
    ///申卖价二
    private String askPrice2;
    ///申卖量二
    private String askVolume2;
    ///申买价三
    private String bidPrice3;
    ///申买量三
    private String bidVolume3;
    ///申卖价三
    private String askPrice3;
    ///申卖量三
    private String askVolume3;
    ///申买价四
    private String bidPrice4;
    ///申买量四
    private String bidVolume4;
    ///申卖价四
    private String askPrice4;
    ///申卖量四
    private String askVolume4;
    ///申买价五
    private String bidPrice5;
    ///申买量五
    private String bidVolume5;
    ///申卖价五
    private String askPrice5;
    ///申卖量五
    private String askVolume5;
    //行情时间
    private String quotationDateTime;
    //开始交割日
    private String startDeliveryDate;
    //涨跌
    private String change;
    //涨跌幅
    private String chg;
    //当日均价
    private String averagePrice;
    //昨结
    private String preSettlementPrice;
    ///合约代码
    private String instrumentID;
    ///交易所代码
    private String exchangeID;
    ///最新价
    private String lastPrice;
    ///昨收盘
    private String preClosePrice;
    ///今开盘
    private String openPrice;
    ///最高价
    private String highestPrice;
    ///最低价
    private String lowestPrice;
    ///成交数量
    private String volume;
    ///成交金额
    private String turnover;
    ///持仓量
    private String openInterest;
    ///今收盘
    private String closePrice;
    ///本次结算价
    private String settlementPrice;
    ///涨停板价
    private String upperLimitPrice;
    ///跌停板价
    private String lowerLimitPrice;
    ///申买价一
    private String bidPrice1;
    ///申买量一
    private String bidVolume1;
    private String codes;
    public RequestMarketMessage(Packet packet,Connection connection) {super(packet, connection); }
    public RequestMarketMessage(Connection connection, String codes) {
        super(new Packet(Command.MARKET, genSessionId()), connection);
        this.codes = codes;
    }
    @Override
    protected void decode(ByteBuffer byteBuf) {
        instrumentID = decodeString(byteBuf);
        exchangeID = decodeString(byteBuf);
        lastPrice = decodeString(byteBuf);
        preClosePrice =  decodeString(byteBuf);
        openPrice = decodeString(byteBuf);
        highestPrice = decodeString(byteBuf);
        lowestPrice = decodeString(byteBuf);
        volume = decodeString(byteBuf);
        turnover = decodeString(byteBuf);
        openInterest = decodeString(byteBuf);
        closePrice = decodeString(byteBuf);
        settlementPrice = decodeString(byteBuf);
        upperLimitPrice = decodeString(byteBuf);
        lowerLimitPrice = decodeString(byteBuf);
        bidPrice1 = decodeString(byteBuf);
        bidVolume1 = decodeString(byteBuf);
        askPrice1 = decodeString(byteBuf);
        askVolume1 = decodeString(byteBuf);
        bidPrice2 = decodeString(byteBuf);
        bidVolume2 = decodeString(byteBuf);
        askPrice2 = decodeString(byteBuf);
        askVolume2 = decodeString(byteBuf);
        bidPrice3 = decodeString(byteBuf);
        bidVolume3 = decodeString(byteBuf);
        askPrice3 = decodeString(byteBuf);
        askVolume3 = decodeString(byteBuf);
        bidPrice4 = decodeString(byteBuf);
        bidVolume4 = decodeString(byteBuf);
        askPrice4 = decodeString(byteBuf);
        askVolume4 = decodeString(byteBuf);
        bidPrice5 = decodeString(byteBuf);
        bidVolume5 = decodeString(byteBuf);
        askPrice5 = decodeString(byteBuf);
        askVolume5 = decodeString(byteBuf);
        quotationDateTime = decodeString(byteBuf);
        startDeliveryDate = decodeString(byteBuf);
        change = decodeString(byteBuf);
        chg = decodeString(byteBuf);
        averagePrice = decodeString(byteBuf);
        preSettlementPrice = decodeString(byteBuf);
    }

    public String getAskPrice1() {
        return askPrice1;
    }

    public String getAskVolume1() {
        return askVolume1;
    }

    public String getBidPrice2() {
        return bidPrice2;
    }

    public String getBidVolume2() {
        return bidVolume2;
    }

    public String getAskPrice2() {
        return askPrice2;
    }

    public String getAskVolume2() {
        return askVolume2;
    }

    public String getBidPrice3() {
        return bidPrice3;
    }

    public String getBidVolume3() {
        return bidVolume3;
    }

    public String getAskPrice3() {
        return askPrice3;
    }

    public String getAskVolume3() {
        return askVolume3;
    }

    public String getBidPrice4() {
        return bidPrice4;
    }

    public String getBidVolume4() {
        return bidVolume4;
    }

    public String getAskPrice4() {
        return askPrice4;
    }

    public String getAskVolume4() {
        return askVolume4;
    }

    public String getBidPrice5() {
        return bidPrice5;
    }

    public String getBidVolume5() {
        return bidVolume5;
    }

    public String getAskPrice5() {
        return askPrice5;
    }

    public String getAskVolume5() {
        return askVolume5;
    }

    public String getQuotationDateTime() {
        return quotationDateTime;
    }

    public String getStartDeliveryDate() {
        return startDeliveryDate;
    }

    public String getChange() {
        return change;
    }

    public String getChg() {
        return chg;
    }

    public String getAveragePrice() {
        return averagePrice;
    }

    public String getPreSettlementPrice() {
        return preSettlementPrice;
    }

    public String getInstrumentID() {
        return instrumentID;
    }

    public String getExchangeID() {
        return exchangeID;
    }

    public String getLastPrice() {
        return lastPrice;
    }

    public String getPreClosePrice() {
        return preClosePrice;
    }

    public String getOpenPrice() {
        return openPrice;
    }

    public String getHighestPrice() {
        return highestPrice;
    }

    public String getLowestPrice() {
        return lowestPrice;
    }

    public String getVolume() {
        return volume;
    }

    public String getTurnover() {
        return turnover;
    }

    public String getOpenInterest() {
        return openInterest;
    }

    public String getClosePrice() {
        return closePrice;
    }

    public String getSettlementPrice() {
        return settlementPrice;
    }

    public String getUpperLimitPrice() {
        return upperLimitPrice;
    }

    public String getLowerLimitPrice() {
        return lowerLimitPrice;
    }

    public String getBidPrice1() {
        return bidPrice1;
    }

    public String getBidVolume1() {
        return bidVolume1;
    }

    public String getCodes() {
        return codes;
    }

    @Override
    protected void encode(ByteBuf body) {
        encodeString(body,codes);
    }

    @Override
    public String toString() {
        return "RequestMarketMessage{" +
                "askPrice1='" + askPrice1 + '\'' +
                ", askVolume1='" + askVolume1 + '\'' +
                ", bidPrice2='" + bidPrice2 + '\'' +
                ", bidVolume2='" + bidVolume2 + '\'' +
                ", askPrice2='" + askPrice2 + '\'' +
                ", askVolume2='" + askVolume2 + '\'' +
                ", bidPrice3='" + bidPrice3 + '\'' +
                ", bidVolume3='" + bidVolume3 + '\'' +
                ", askPrice3='" + askPrice3 + '\'' +
                ", askVolume3='" + askVolume3 + '\'' +
                ", bidPrice4='" + bidPrice4 + '\'' +
                ", bidVolume4='" + bidVolume4 + '\'' +
                ", askPrice4='" + askPrice4 + '\'' +
                ", askVolume4='" + askVolume4 + '\'' +
                ", bidPrice5='" + bidPrice5 + '\'' +
                ", bidVolume5='" + bidVolume5 + '\'' +
                ", askPrice5='" + askPrice5 + '\'' +
                ", askVolume5='" + askVolume5 + '\'' +
                ", quotationDateTime='" + quotationDateTime + '\'' +
                ", startDeliveryDate='" + startDeliveryDate + '\'' +
                ", change='" + change + '\'' +
                ", chg='" + chg + '\'' +
                ", averagePrice='" + averagePrice + '\'' +
                ", preSettlementPrice='" + preSettlementPrice + '\'' +
                ", instrumentID='" + instrumentID + '\'' +
                ", exchangeID='" + exchangeID + '\'' +
                ", lastPrice='" + lastPrice + '\'' +
                ", preClosePrice='" + preClosePrice + '\'' +
                ", openPrice='" + openPrice + '\'' +
                ", highestPrice='" + highestPrice + '\'' +
                ", lowestPrice='" + lowestPrice + '\'' +
                ", volume='" + volume + '\'' +
                ", turnover='" + turnover + '\'' +
                ", openInterest='" + openInterest + '\'' +
                ", closePrice='" + closePrice + '\'' +
                ", settlementPrice='" + settlementPrice + '\'' +
                ", upperLimitPrice='" + upperLimitPrice + '\'' +
                ", lowerLimitPrice='" + lowerLimitPrice + '\'' +
                ", bidPrice1='" + bidPrice1 + '\'' +
                ", bidVolume1='" + bidVolume1 + '\'' +
                ", codes='" + codes + '\'' +
                '}';
    }

    /**
     * 将实时的行情转换成一个 k线对象
     *
     * @return
     */
    public KCandleObj obj2KCandleObj() {
        try {
            KCandleObj kCandleObj = new KCandleObj();
//            kCandleObj.setOpen(Double.parseDouble(opening));
//            kCandleObj.setHigh(Double.parseDouble(highest));
//            kCandleObj.setLow(Double.parseDouble(lowest));
//            kCandleObj.setClose(Double.parseDouble(sellone));
            kCandleObj.setOpen(Double.parseDouble(openPrice));
            kCandleObj.setHigh(Double.parseDouble(highestPrice));
            kCandleObj.setLow(Double.parseDouble(lowestPrice));
            kCandleObj.setClose(Double.parseDouble(lastPrice));
            //2016-10-14 17:21:50
            kCandleObj.setTime(quotationDateTime);
            kCandleObj.setTimeLong(parser(quotationDateTime, "yyyy-MM-dd HH:mm:ss").getTime());
            return kCandleObj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static Date parser(String strDate, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(strDate);
        } catch (Exception e) {
            return null;
        }
    }
}
