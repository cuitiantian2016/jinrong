package com.honglu.future.util;

import android.text.TextUtils;

import com.honglu.future.config.Constant;
import com.honglu.future.ui.trade.bean.ProductListBean;

import java.math.BigDecimal;

/**
 * 交易计算相关
 * Created by zhuaibing on 2017/11/20
 */

public class TradeUtil {

    /**
     * 平仓手续费
     *
     * @param todayPosition           今日持仓
     * @param closeTodayRatioByMoney
     * @param closeTodayRatioByVolume
     * @param closeRatioByMoney
     * @param closeRatioByVolume
     * @param price                   平仓价位
     * @param tradeNum                手数
     * @param volumeMultiple
     * @return
     */
    public static double getCloseTradePrice(ProductListBean bean, int todayPosition, String closeTodayRatioByMoney, String closeTodayRatioByVolume, String closeRatioByMoney
            , String closeRatioByVolume, double price, int tradeNum, int volumeMultiple) {
        double mRate = 0;
        if (SpUtil.getString(Constant.COMPANY_TYPE).equals(Constant.COMPANY_TYPE_GUOFU)) {
            //国富
            if (todayPosition > 0) {
                if (!TextUtils.isEmpty(closeTodayRatioByMoney) && Double.parseDouble(closeTodayRatioByMoney) > 0) {
                    mRate = Double.parseDouble(closeTodayRatioByMoney);
                } else {
                    mRate = !TextUtils.isEmpty(closeTodayRatioByVolume) ? Double.parseDouble(closeTodayRatioByVolume) : 0;
                }
            } else {
                if (!TextUtils.isEmpty(closeRatioByMoney) && Double.parseDouble(closeRatioByMoney) > 0) {
                    mRate = Double.parseDouble(closeRatioByMoney);
                } else {
                    mRate = !TextUtils.isEmpty(closeRatioByVolume) ? Double.parseDouble(closeRatioByVolume) : 0;
                }
            }
        } else if (SpUtil.getString(Constant.COMPANY_TYPE).equals(Constant.COMPANY_TYPE_MEIERYA)) {
            //美尔雅
            if (bean.getProductId().equals(Constant.PRODUCT_SPECIAL_AU) || bean.getProductId().equals(Constant.PRODUCT_SPECIAL_NI)
                    || bean.getProductId().equals(Constant.PRODUCT_SPECIAL_MA) || bean.getProductId().equals(Constant.PRODUCT_SPECIAL_M)
                    || bean.getProductId().equals(Constant.PRODUCT_SPECIAL_C)) {
                if (todayPosition > 0) {
                    return NumberUtil.multiply(new BigDecimal(closeTodayRatioByMoney).doubleValue(),new BigDecimal(tradeNum).doubleValue());
                } else {
                    return NumberUtil.multiply(new BigDecimal(closeRatioByMoney).doubleValue(),new BigDecimal(tradeNum).doubleValue());
                }
            } else {
                if (todayPosition > 0) {
                    if (!TextUtils.isEmpty(closeTodayRatioByMoney) && Double.parseDouble(closeTodayRatioByMoney) > 0) {
                        mRate = Double.parseDouble(closeTodayRatioByMoney);
                    } else {
                        mRate = !TextUtils.isEmpty(closeTodayRatioByVolume) ? Double.parseDouble(closeTodayRatioByVolume) : 0;
                    }
                } else {
                    if (!TextUtils.isEmpty(closeRatioByMoney) && Double.parseDouble(closeRatioByMoney) > 0) {
                        mRate = Double.parseDouble(closeRatioByMoney);
                    } else {
                        mRate = !TextUtils.isEmpty(closeRatioByVolume) ? Double.parseDouble(closeRatioByVolume) : 0;
                    }
                }
            }
        }

        BigDecimal mPriceBig = NumberUtil.getBigDecimal(price);
        BigDecimal mTradeNumBig = NumberUtil.getBigDecimal(tradeNum);
        BigDecimal mVolumeMultipleBig = NumberUtil.getBigDecimal(volumeMultiple);
        BigDecimal mRateBig = NumberUtil.getBigDecimal(mRate);

        return mPriceBig.multiply(mTradeNumBig).multiply(mVolumeMultipleBig).multiply(mRateBig).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    /**
     * 平仓盈亏
     *
     * @param holdAvgPrice   持仓均价 list
     * @param priceTick      最小变动价位 详情
     * @param volumeMultiple 合约数量乘数 详情
     * @param price          价格
     * @param tradeNum       手数
     * @return
     */
    public static double getCloseProfitLoss(int type, String holdAvgPrice, String priceTick, int volumeMultiple, double price, int tradeNum) {
        BigDecimal mPriceBig = NumberUtil.getBigDecimal(price);
        BigDecimal mholdAvgPriceBig = NumberUtil.getBigDecimal(Double.parseDouble(holdAvgPrice));
        BigDecimal mTradeNumBig = NumberUtil.getBigDecimal(tradeNum);
        BigDecimal mVolumeMultipleBig = NumberUtil.getBigDecimal(volumeMultiple);
        BigDecimal mPriceTickBig = NumberUtil.getBigDecimal(Double.parseDouble(priceTick));
        if (type == Constant.TYPE_BUY_UP) {
            return mPriceBig.subtract(mholdAvgPriceBig).divide(mPriceTickBig).multiply(mPriceTickBig).multiply(mVolumeMultipleBig).multiply(mTradeNumBig).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        } else {
            return mholdAvgPriceBig.subtract(mPriceBig).divide(mPriceTickBig).multiply(mPriceTickBig).multiply(mVolumeMultipleBig).multiply(mTradeNumBig).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
    }


    /**
     * @param type
     * @param openAvgPrice   建仓均价
     * @param priceTick      最小变动价位 详情
     * @param volumeMultiple 合约数量乘数 详情
     * @param price          价格
     * @param tradeNum       手数
     * @return
     */
    public static double getActualProfitLoss(int type, String openAvgPrice, String priceTick, int volumeMultiple, double price, int tradeNum) {
        BigDecimal mPriceBig = NumberUtil.getBigDecimal(price);
        BigDecimal mTradeNumBig = NumberUtil.getBigDecimal(tradeNum);
        BigDecimal mOpenAvgPriceBig = NumberUtil.getBigDecimal(Double.parseDouble(openAvgPrice));
        BigDecimal mPriceTickBig = NumberUtil.getBigDecimal(Double.parseDouble(priceTick));
        BigDecimal mVolumeMultipleBig = NumberUtil.getBigDecimal(volumeMultiple);
        if (type == Constant.TYPE_BUY_UP) {
            return mPriceBig.subtract(mOpenAvgPriceBig).divide(mPriceTickBig).multiply(mPriceTickBig).multiply(mVolumeMultipleBig).multiply(mTradeNumBig).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        } else {
            return mOpenAvgPriceBig.subtract(mPriceBig).divide(mPriceTickBig).multiply(mPriceTickBig).multiply(mVolumeMultipleBig).multiply(mTradeNumBig).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
    }
}
