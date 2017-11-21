package com.honglu.future.util;

import android.text.TextUtils;

import com.honglu.future.config.Constant;

import java.math.BigDecimal;

/**
 * 交易计算相关
 * Created by zhuaibing on 2017/11/20
 */

public class TradeUtil {

    /**
     * 获取平仓手续费
     * @param closeTodayRatioByMoney 平今手续费率 详情
     * @param bidPrice1 申买价一 详情
     * @param askPrice1 申卖价一 详情
     * @param volumeMultiple 合约数量乘数 详情
     * @param closeTodayRatioByVolume 平今手续费 详情
     * @param closeRatioByMoney 平仓手续费率 详情
     * @param closeRatioByVolume 平仓手续费 详情
     * @param todayPosition 今日持仓 list
     * @param type  购买方向 list
     * @param tradeNum 手数
     * @return
     */
    public static double getCloseTradePrice(String closeTodayRatioByMoney,String bidPrice1,String askPrice1,int volumeMultiple
            ,String closeTodayRatioByVolume,String closeRatioByMoney,String closeRatioByVolume
            , int todayPosition , int type , int tradeNum ,String excode) {

        //判断是否为上海期货交易所
        boolean mIsShfe = Constant.CODE_SHFE.equals(excode);

        //是否今平
        boolean mIsCloseToday = mIsShfe ? (todayPosition > 0 ? true : false) : false;

        //平今手续费率 null /“” 或者 0
        boolean mRatioByMoneyNull_0 = TextUtils.isEmpty(closeTodayRatioByMoney) || Double.parseDouble(closeTodayRatioByMoney) == 0 ? true : false;

        //平今一手的手续费
        double mOneCloseToday = 0;
        if (mRatioByMoneyNull_0){
            //根据手续费算
            mOneCloseToday = Double.parseDouble(closeTodayRatioByVolume);
        }else {
            if (Constant.TYPE_BUY_DOWN == type){
                mOneCloseToday = NumberUtil.multiply(new BigDecimal(closeTodayRatioByMoney).doubleValue(),new BigDecimal(bidPrice1).doubleValue()) * volumeMultiple;
            }else {
                mOneCloseToday = NumberUtil.multiply(new BigDecimal(closeTodayRatioByMoney).doubleValue(), new BigDecimal(askPrice1).doubleValue()) * volumeMultiple;
            }
        }

       //平昨一手的手续费
        double mOneCloseYd = 0;
        if (mRatioByMoneyNull_0){
            mOneCloseYd =  Double.parseDouble(closeRatioByVolume);
        }else {
            if (Constant.TYPE_BUY_DOWN == type){
                mOneCloseYd = NumberUtil.multiply(new BigDecimal(closeRatioByMoney).doubleValue(),new BigDecimal(bidPrice1).doubleValue()) * volumeMultiple;
            }else {
                mOneCloseYd =  NumberUtil.multiply(new BigDecimal(closeRatioByMoney).doubleValue(),new BigDecimal(askPrice1).doubleValue()) * volumeMultiple;
            }
        }


        //手续费
        double closeTradePrice = 0;
        if (mIsShfe){
             if (mIsCloseToday){
                 closeTradePrice = NumberUtil.multiply(mOneCloseToday, new BigDecimal(tradeNum).doubleValue());
             }else {
                 closeTradePrice = NumberUtil.multiply(mOneCloseYd , new BigDecimal(tradeNum).doubleValue());
             }
        }else {
             if (tradeNum <= todayPosition){
                 closeTradePrice = NumberUtil.multiply(mOneCloseToday, new BigDecimal(tradeNum).doubleValue());
             }else {
                 double todaySXF = NumberUtil.multiply(mOneCloseToday, new BigDecimal(todayPosition).doubleValue());
                 double ydSXF = NumberUtil.multiply(mOneCloseYd, new BigDecimal(tradeNum - todayPosition).doubleValue());
                 closeTradePrice = todaySXF + ydSXF;
             }
        }
       return closeTradePrice;
    }


    /**
     * 实际盈亏
     * @param openAvgPrice 建仓均价 list
     * @param bidPrice1 申买价一 详情
     * @param askPrice1 申卖价一 详情
     * @param priceTick 最小变动价位 详情
     * @param volumeMultiple ;合约数量乘数 详情
     * @param type 购买方向 list
     * @param tradeNum 手数
     * @return
     */
    public static double getActualProfitLoss(String openAvgPrice,String bidPrice1,String askPrice1,String priceTick,int volumeMultiple,int type ,int tradeNum){
        double oneProfitAndLossTotal = 0;
        if (Constant.TYPE_BUY_DOWN == type) {

            oneProfitAndLossTotal = NumberUtil.multiply(NumberUtil.divide(NumberUtil.subtract(new BigDecimal(openAvgPrice).doubleValue(), new BigDecimal(askPrice1).doubleValue()),
                    new BigDecimal(priceTick).doubleValue()),
                    NumberUtil.multiply(new BigDecimal(volumeMultiple).doubleValue(), new BigDecimal(priceTick).doubleValue()));
        } else {
            oneProfitAndLossTotal = NumberUtil.multiply(NumberUtil.divide(NumberUtil.subtract(new BigDecimal(bidPrice1).doubleValue(), new BigDecimal(openAvgPrice).doubleValue()),
                    new BigDecimal(priceTick).doubleValue()),
                    NumberUtil.multiply(new BigDecimal(volumeMultiple).doubleValue(), new BigDecimal(priceTick).doubleValue()));
        }
        return NumberUtil.multiply(oneProfitAndLossTotal, new BigDecimal(tradeNum).doubleValue());
    }


    /**
     * 平仓盈亏
     * @param holdAvgPrice 持仓均价 list
     * @param askPrice1 申卖价一 详情
     * @param bidPrice1 申买价一 详情
     * @param priceTick 最小变动价位 详情
     * @param volumeMultiple  合约数量乘数 详情
     * @param type  购买方向 list
     * @param tradeNum 手数
     * @return
     */
    public static double getCloseProfitLoss(String holdAvgPrice ,String askPrice1 ,String bidPrice1 ,String priceTick ,int volumeMultiple ,int type ,int tradeNum){
        double oneProfitAndLossToday = 0;
        if (Constant.TYPE_BUY_DOWN == type) {
            oneProfitAndLossToday = NumberUtil.multiply(NumberUtil.divide(NumberUtil.subtract(new BigDecimal(holdAvgPrice).doubleValue(), new BigDecimal(askPrice1).doubleValue()),
                    new BigDecimal(priceTick).doubleValue()), NumberUtil.multiply(new BigDecimal(volumeMultiple).doubleValue(), new BigDecimal(priceTick).doubleValue()));
        } else {
            oneProfitAndLossToday = NumberUtil.multiply(NumberUtil.divide(NumberUtil.subtract(new BigDecimal(bidPrice1).doubleValue(), new BigDecimal(holdAvgPrice).doubleValue()),
                    new BigDecimal(priceTick).doubleValue()),
                    NumberUtil.multiply(new BigDecimal(volumeMultiple).doubleValue(), new BigDecimal(priceTick).doubleValue()));
        }
        return NumberUtil.multiply(oneProfitAndLossToday, new BigDecimal(tradeNum).doubleValue());
    }

    /**
     * 最大手数
     * @param position
     * @param ydPosition
     * @param todayPosition
     * @param excode
     * @return
     */
    public static int getMaxCloseTradeNum(int position ,int ydPosition,int todayPosition ,String excode){
        //判断是否为上海期货交易所
        boolean mIsShfe = Constant.CODE_SHFE.equals(excode);
        //是否今平
        boolean mIsCloseToday = mIsShfe ? (todayPosition > 0 ? true : false) : false;

        int maxClosePosition = mIsShfe ? (mIsCloseToday ? position : ydPosition) :  position;
        return maxClosePosition;
    }
}
