package com.honglu.future.ui.market.fragment;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.honglu.future.config.Constant;
import com.honglu.future.ui.market.bean.MarketnalysisBean;
import com.honglu.future.util.SpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuaibing on 2018/1/6
 */

public class MarketHelper {

    public MarketHelper(){

    }


    /**
     * http 数据切换或者返回刷新数据
     */
    public void  marketDataRefresh(ArrayList<MarketItemFragment> mFragments ,MarketnalysisBean alysisBean){
        for (Fragment fragment : mFragments){
            MarketItemFragment mItemFragment = (MarketItemFragment) fragment;
            if (TextUtils.equals(MarketFragment.ZXHQ_TYPE, mItemFragment.getTabSelectType())){ //自选行情
                List<MarketnalysisBean.ListBean.QuotationDataListBean> adapterList = mItemFragment.getList();
                if (adapterList != null && adapterList.size() > 0) {
                    for (MarketnalysisBean.ListBean.QuotationDataListBean zxBean : adapterList) {

                        for (MarketnalysisBean.ListBean listBean : alysisBean.getList()) {

                            if (listBean.getQuotationDataList() !=null && listBean.getQuotationDataList().size() > 0) {
                                if (zxBean.getExchangeID().equals(listBean.getExcode())) {
                                    for (MarketnalysisBean.ListBean.QuotationDataListBean allBean : listBean.getQuotationDataList()) {

                                        if (zxBean.getInstrumentID().equals(allBean.getInstrumentID())) {
                                            zxBean.setLastPrice(allBean.getLastPrice());
                                            zxBean.setChg(allBean.getChg());
                                            zxBean.setOpenInterest(allBean.getOpenInterest());
                                            zxBean.setChange(allBean.getChange());
                                        }
                                    }
                                }
                            }
                        }
                    }
                    mItemFragment.notifyDataChanged();
                }

            }else if (TextUtils.equals(MarketFragment.ZXHQ_TYPE , mItemFragment.getTabSelectType())){ //主力合约
                List<MarketnalysisBean.ListBean.QuotationDataListBean> adapterList = mItemFragment.getList();
                if (adapterList !=null && adapterList.size() > 0){
                    for (MarketnalysisBean.ListBean.QuotationDataListBean zlBean : adapterList) {
                        for (MarketnalysisBean.ListBean listBean : alysisBean.getList()) {

                            if (listBean.getQuotationDataList() !=null && listBean.getQuotationDataList().size() > 0) {
                                if (zlBean.getExchangeID().equals(listBean.getExcode())) {
                                    for (MarketnalysisBean.ListBean.QuotationDataListBean allBean : listBean.getQuotationDataList()) {

                                        if (zlBean.getInstrumentID().equals(allBean.getInstrumentID())) {
                                            zlBean.setLastPrice(allBean.getLastPrice());
                                            zlBean.setChg(allBean.getChg());
                                            zlBean.setOpenInterest(allBean.getOpenInterest());
                                            zlBean.setChange(allBean.getChange());
                                        }
                                    }
                                }
                            }
                        }
                    }
                    mItemFragment.notifyDataChanged();
                }
            }else{
                List<MarketnalysisBean.ListBean.QuotationDataListBean> adapterList = mItemFragment.getList();
                if (adapterList !=null && adapterList.size() > 0){
                    for (MarketnalysisBean.ListBean listBean : alysisBean.getList()) {
                        if (TextUtils.equals( mItemFragment.getTabSelectType(),listBean.getExcode())) {
                            if (listBean.getQuotationDataList() !=null && listBean.getQuotationDataList().size() > 0){

                                for (MarketnalysisBean.ListBean.QuotationDataListBean mBean : adapterList){

                                    for (MarketnalysisBean.ListBean.QuotationDataListBean hBean :listBean.getQuotationDataList()){

                                        if (TextUtils.equals(mBean.getInstrumentID(),hBean.getInstrumentID())){

                                            mBean.setLastPrice(hBean.getLastPrice());
                                            mBean.setChg(hBean.getChg());
                                            mBean.setOpenInterest(hBean.getOpenInterest());
                                            mBean.setChange(hBean.getChange());
                                        }
                                    }
                                }
                            }
                        }
                    }
                    mItemFragment.notifyDataChanged();
                }
            }
        }
    }



    //获取自选数据
    public List<MarketnalysisBean.ListBean.QuotationDataListBean> getZxMarketList(MarketnalysisBean alysisBean) {
        String zxMarketJson = SpUtil.getString(Constant.ZX_MARKET_KEY);
        if (!TextUtils.isEmpty(zxMarketJson)) {
            Gson gson = new Gson();
            try {
                List<MarketnalysisBean.ListBean.QuotationDataListBean> mZxMarketList = gson.fromJson(zxMarketJson,
                        new TypeToken<List<MarketnalysisBean.ListBean.QuotationDataListBean>>() {
                        }.getType());

                if (alysisBean !=null && alysisBean.getList() !=null && alysisBean.getList().size() > 0){
                    for (MarketnalysisBean.ListBean listBean : alysisBean.getList()) {
                        if (!TextUtils.isEmpty(listBean.getExcode())
                                && listBean.getQuotationDataList() != null
                                && listBean.getQuotationDataList().size() > 0) {

                            for (MarketnalysisBean.ListBean.QuotationDataListBean mBean : listBean.getQuotationDataList()) {
                                for (MarketnalysisBean.ListBean.QuotationDataListBean zxBean : mZxMarketList) {
                                    if (!TextUtils.isEmpty(mBean.getInstrumentID()) && mBean.getInstrumentID().equals(zxBean.getInstrumentID())) {
                                        zxBean.setChg(mBean.getChg());
                                        zxBean.setLastPrice(mBean.getLastPrice());
                                        zxBean.setOpenInterest(mBean.getOpenInterest());
                                        zxBean.setChange(mBean.getChange());
                                    }
                                }
                            }
                        }
                    }
                }

                return mZxMarketList;
            } catch (JsonSyntaxException e) {
            }
        }
        return null;
    }



    //获取主力合约
    public List<MarketnalysisBean.ListBean.QuotationDataListBean> getZlhyMarketList(List<MarketnalysisBean.ListBean> list) {
        List<MarketnalysisBean.ListBean.QuotationDataListBean> mZlhyMarketList = new ArrayList<>();
        for (MarketnalysisBean.ListBean bean : list) {
            if (bean.getQuotationDataList() != null && bean.getQuotationDataList().size() > 0) {
                mZlhyMarketList.addAll(bean.getQuotationDataList());
            }
        }
        return mZlhyMarketList;
    }



    //拼接 MPush code
    public String mosaicMPushCode(List<MarketnalysisBean.ListBean.QuotationDataListBean> list) {
        StringBuilder builder = new StringBuilder();
        if (list != null && list.size() > 0) {

            for (MarketnalysisBean.ListBean.QuotationDataListBean bean : list) {
                if (!TextUtils.isEmpty(bean.getExchangeID()) && !TextUtils.isEmpty(bean.getInstrumentID())) {
                    if (builder.length() > 0) {
                        builder.append(",");
                    }
                    builder.append(bean.getExchangeID());
                    builder.append("|");
                    builder.append(bean.getInstrumentID());
                }
            }
        }
        return builder.toString();
    }



}
