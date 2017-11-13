package com.honglu.future.ui.market.fragment;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.honglu.future.R;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.config.Constant;
import com.honglu.future.events.ReceiverMarketMessageEvent;
import com.honglu.future.mpush.MPushUtil;
import com.honglu.future.ui.market.activity.OptionalQuotesActivity;
import com.honglu.future.ui.market.bean.MarketnalysisBean;
import com.honglu.future.ui.market.contract.MarketContract;
import com.honglu.future.ui.market.presenter.MarketPresenter;
import com.honglu.future.util.SpUtil;
import com.honglu.future.widget.tab.CustomTabEntity;
import com.honglu.future.widget.tab.HorizontalTabLayout;
import com.honglu.future.widget.tab.SimpleOnTabSelectListener;
import com.honglu.future.widget.tab.TabEntity;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

/**
 * Created by zhuaibing on 2017/10/25
 */

public class MarketFragment extends BaseFragment<MarketPresenter> implements MarketContract.View, MarketItemFragment.OnAddAptionalListener,MarketItemFragment.OnMPushCodeRefreshListener,MarketItemFragment.OnZXMarketListListener{
    public static final String ZXHQ_TYPE = "zxhq_type";//自选行情
    public static final String ZLHY_TYPE = "zlhy_type";//主力合约
    @BindView(R.id.market_common_tab_layout)
    HorizontalTabLayout mCommonTab;


    //除自选外全部数据
    private List<MarketnalysisBean.ListBean> mAllMarketList;
    //tab 数据
    private ArrayList<CustomTabEntity> mTabList = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String mPushCode;
    private String mTabSelectType;
    private int mPosition = 0;

    public static MarketFragment marketFragment;
    private MarketItemFragment mZxFragment;


    public static MarketFragment getInstance() {
        if (marketFragment == null) {
            marketFragment = new MarketFragment();
        }
        return marketFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_market_layout;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }


    @Override
    public void showLoading(String content) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorMsg(String msg, String type) {

    }

    @Override
    public void onPause() {
        super.onPause();
        MPushUtil.pauseRequest();
    }

    @Override
    public void onResume() {
        super.onResume();
        requestMarket(mPushCode);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden){
            MPushUtil.pauseRequest();
        }else {
            requestMarket(mPushCode);
        }
    }

    /*******
     * 将事件交给事件派发controller处理
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMarketEventMainThread(ReceiverMarketMessageEvent event) {
        if (!TextUtils.isEmpty(mPushCode) && mPushCode.equals(MPushUtil.requestCodes) && !(isHidden())) {
            if (mFragments != null && mFragments.size() > 0 && mFragments.size() > mPosition) {
                MarketItemFragment fragment = (MarketItemFragment) mFragments.get(mPosition);
                fragment.mPushRefresh(event.marketMessage.getInstrumentID(), event.marketMessage.getLastPrice(), event.marketMessage.getChg(), event.marketMessage.getOpenInterest());
            }
        }
    }

    //MPush
    public void requestMarket(String productList) {
        if (!TextUtils.isEmpty(productList))
            MPushUtil.requestMarket(productList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mZxFragment != null) {
            List<MarketnalysisBean.ListBean.QuotationDataListBean> zxList = mZxFragment.getList();
            if (zxList != null && zxList.size() > 0) {
                Gson gson = new Gson();
                String toJson = gson.toJson(zxList);
                SpUtil.putString(Constant.ZX_MARKET_KEY, toJson);
            } else {
                SpUtil.putString(Constant.ZX_MARKET_KEY, "");
            }
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onAddAptional() {
        if (mAllMarketList != null && mAllMarketList.size() > 0) {
            if (mZxFragment != null) {
                List<MarketnalysisBean.ListBean.QuotationDataListBean> zxList = mZxFragment.getList();
                Intent intent = new Intent(getActivity(), OptionalQuotesActivity.class);
                intent.putExtra("allmarketlist", (Serializable) mAllMarketList);
                intent.putExtra("zxmarketlist", (Serializable)  zxList);
                startActivity(intent);
            }
        }
    }

    @Override
    public void OnMPushCodeRefresh(String mpushCode) {
        if (mFragments != null && mFragments.size() > 0 && mFragments.size() > mPosition) {
            MarketItemFragment fragment = (MarketItemFragment) mFragments.get(mPosition);
            fragment.setOnAddAptionalListener(MarketFragment.this);
            mPushCode = fragment.getPushCode();
            mTabSelectType = fragment.getTabSelectType();
            if (ZXHQ_TYPE.equals(mTabSelectType)){
                requestMarket(mPushCode);
            }
        }
    }


    @Override
    public List<MarketnalysisBean.ListBean.QuotationDataListBean> OnZXMarketList() {
        return mZxFragment !=null ? mZxFragment.getList() : null;
    }

    @Override
    public void loadData() {
        EventBus.getDefault().register(this);
        //tab 切换
        mCommonTab.setOnTabSelectListener(new SimpleOnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mPosition = position;
                if (mFragments != null && mFragments.size() > 0) {
                    MarketItemFragment fragment = (MarketItemFragment) mFragments.get(position);
                    fragment.setOnAddAptionalListener(MarketFragment.this);
                    mPushCode = fragment.getPushCode();
                    mTabSelectType = fragment.getTabSelectType();
                    requestMarket(mPushCode);
                }
            }
        });
        mPresenter.getMarketData();
    }

    /**
     * 接口请求返回数据
     *
     * @param alysisBean
     */
    @Override
    public void getMarketData(MarketnalysisBean alysisBean) {
        if (alysisBean == null || alysisBean.getList() == null) {
            return;
        }
        List<MarketnalysisBean.ListBean.QuotationDataListBean> zxMarketList = getZxMarketList();
        this.mAllMarketList = addItemDataExcode(alysisBean.getList(),zxMarketList);
        mPushCode = mosaicMPushCode(zxMarketList);
        mPosition = 0;
        mTabList.clear();
        mFragments.clear();
        mTabList.add(new TabEntity("自选", ZXHQ_TYPE));
        mTabList.add(new TabEntity("主力合约", ZLHY_TYPE));
        mZxFragment = new MarketItemFragment();
        mZxFragment.setArguments(mZxFragment.setArgumentData(ZXHQ_TYPE, zxMarketList));
        MarketItemFragment zlhyFragment = new MarketItemFragment();
        zlhyFragment.setArguments(zlhyFragment.setArgumentData(ZLHY_TYPE, getZlhyMarketList(mAllMarketList)));
        zlhyFragment.setOnZXMarketListListener(this);
        mZxFragment.setOnAddAptionalListener(this);
        mZxFragment.setOnMPushCodeRefreshListener(this);
        mFragments.add(mZxFragment);
        mFragments.add(zlhyFragment);
        for (MarketnalysisBean.ListBean bean : mAllMarketList) {
            mTabList.add(new TabEntity(bean.getExchangeName(), bean.getExcode()));
            MarketItemFragment fragment = new MarketItemFragment();
            fragment.setArguments(fragment.setArgumentData(bean.getExcode(), bean.getQuotationDataList()));
            fragment.setOnZXMarketListListener(this);
            mFragments.add(fragment);
        }
        mCommonTab.setTabData(mTabList, (FragmentActivity) mContext, R.id.market_fragment_container, mFragments);
        requestMarket(mPushCode);
    }


    //获取自选数据
    private List<MarketnalysisBean.ListBean.QuotationDataListBean> getZxMarketList() {
        String zxMarketJson = SpUtil.getString(Constant.ZX_MARKET_KEY);
        if (!TextUtils.isEmpty(zxMarketJson)) {
            Gson gson = new Gson();
            try {
                List<MarketnalysisBean.ListBean.QuotationDataListBean> mZxMarketList = gson.fromJson(zxMarketJson,
                        new TypeToken<List<MarketnalysisBean.ListBean.QuotationDataListBean>>() {
                        }.getType());
                return mZxMarketList;
            } catch (JsonSyntaxException e) {
            }
        }
        return null;
    }

    //获取主力合约
    private List<MarketnalysisBean.ListBean.QuotationDataListBean> getZlhyMarketList(List<MarketnalysisBean.ListBean> list) {
        List<MarketnalysisBean.ListBean.QuotationDataListBean> mZlhyMarketList = new ArrayList<>();
        for (MarketnalysisBean.ListBean bean : list) {
            if (bean.getQuotationDataList() != null && bean.getQuotationDataList().size() > 0) {
                mZlhyMarketList.addAll(bean.getQuotationDataList());
            }
        }
        return mZlhyMarketList;
    }


    //拼接 MPush code
    private String mosaicMPushCode(List<MarketnalysisBean.ListBean.QuotationDataListBean> list) {
        StringBuilder builder = new StringBuilder();
        if (list != null && list.size() > 0) {

            for (MarketnalysisBean.ListBean.QuotationDataListBean bean : list) {
                if (!TextUtils.isEmpty(bean.getExcode()) && !TextUtils.isEmpty(bean.getInstrumentID())) {
                    if (builder.length() > 0) {
                        builder.append(",");
                    }
                    builder.append(bean.getExcode() + "|" + bean.getInstrumentID());
                }
            }
        }
        return builder.toString();
    }


    //给每条数据添加 Excode
    private List<MarketnalysisBean.ListBean> addItemDataExcode(List<MarketnalysisBean.ListBean> list, List<MarketnalysisBean.ListBean.QuotationDataListBean> mZxMarketList) {
        if (mZxMarketList != null && mZxMarketList.size() > 0) {
            for (MarketnalysisBean.ListBean listBean : list) {
                if (!TextUtils.isEmpty(listBean.getExcode())
                        && listBean.getQuotationDataList() != null
                        && listBean.getQuotationDataList().size() > 0) {

                    for (MarketnalysisBean.ListBean.QuotationDataListBean mBean : listBean.getQuotationDataList()) {
                        mBean.setExcode(listBean.getExcode());
                        for (MarketnalysisBean.ListBean.QuotationDataListBean zxBean : mZxMarketList) {
                            if (!TextUtils.isEmpty(mBean.getInstrumentID()) && mBean.getInstrumentID().equals(zxBean.getInstrumentID())) {
                                //已经存在自选 img 显示删除
                                mBean.setIcAdd("1");
                            } else {
                                //不存在自选 img 显示添加
                                if (!"1".equals(mBean.getIcAdd())){
                                    mBean.setIcAdd("0");
                                }
                            }

                        }
                    }
                }
            }
        } else {
            for (MarketnalysisBean.ListBean listBean : list) {

                if (!TextUtils.isEmpty(listBean.getExcode())
                        && listBean.getQuotationDataList() != null
                        && listBean.getQuotationDataList().size() > 0) {

                    for (MarketnalysisBean.ListBean.QuotationDataListBean mBean : listBean.getQuotationDataList()) {
                        mBean.setIcAdd("0");
                        mBean.setExcode(listBean.getExcode());
                    }
                }
            }
        }
        return list;
    }
}
