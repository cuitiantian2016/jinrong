package com.honglu.future.ui.market.fragment;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.events.ReceiverMarketMessageEvent;
import com.honglu.future.events.RefreshUIEvent;
import com.honglu.future.events.UIBaseEvent;
import com.honglu.future.mpush.MPushUtil;
import com.honglu.future.ui.market.activity.OptionalQuotesActivity;
import com.honglu.future.ui.market.adapter.MarketTabAdapter;
import com.honglu.future.ui.market.bean.ListBean;
import com.honglu.future.ui.market.bean.MarketTabBean;
import com.honglu.future.ui.market.bean.MarketnalysisBean;
import com.honglu.future.ui.market.bean.QuotationDataListBean;
import com.honglu.future.ui.market.contract.MarketContract;
import com.honglu.future.ui.market.presenter.MarketPresenter;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zhuaibing on 2017/10/25
 */

public class MarketFragment extends BaseFragment<MarketPresenter> implements MarketContract.View, MarketItemFragment.OnAddAptionalListener, MarketItemFragment.OnMPushCodeRefreshListener, MarketItemFragment.OnZXMarketListListener {
    public static final String ZXHQ_TYPE = "ZiXuan";//自选行情
    public static final String ZLHY_TYPE = "Zhuli";//主力合约

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    //除自选外全部数据
    private List<ListBean> mAllMarketList;
    private ArrayList<MarketTabBean> mTabList = new ArrayList<>();
    private ArrayList<MarketItemFragment> mFragments = new ArrayList<>();

    private MarketHelper mMarketHelper;
    public static MarketFragment marketFragment;
    private MarketItemFragment mZxFragment;
    private MarketTabAdapter tabAdapter;
    private String mPushCode;
    private String mTabSelectType;
    private int mPosition = 0;
    private int mHttpState = 0; //0  1中  2 成功
    private boolean mIsInItFragment = false;



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
    public void showLoading(String content) {}

    @Override
    public void stopLoading() {}

    @Override
    public void showErrorMsg(String msg, String type) { }

    @Override
    public void initHttpState(int httpState) {
        this.mHttpState = httpState;
    }

    @Override
    public void onPause() {
        super.onPause();
        MPushUtil.pauseRequest();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden()) {
            requestMarket(mPushCode);
            if (mHttpState == 0 || mHttpState == 2 && mPresenter != null) {
                mHttpState = 1;
                mPresenter.getMarketData();
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            MPushUtil.pauseRequest();
        } else {
            requestMarket(mPushCode);
            if (mHttpState == 0 || mHttpState==2 && mPresenter != null) {
                mHttpState = 1;
                mPresenter.getMarketData();
            }
        }
    }

    public void startPush(){
        mPosition = 1;
        clickTab(1);
        if (mFragments != null && mFragments.size() > 0) {
            MarketItemFragment fragment =  mFragments.get(1);
            fragment.setOnAddAptionalListener(MarketFragment.this);
            mPushCode = fragment.getPushCode();
            mTabSelectType = fragment.getTabSelectType();
            requestMarket(mPushCode);
            fragment.getRealTimeData();
        }
    }

    /*******
     * 接受mpush消息
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ReceiverMarketMessageEvent event) {
        if (!TextUtils.isEmpty(mPushCode) && mPushCode.equals(MPushUtil.requestCodes) && !(isHidden())) {
            if (mFragments != null && mFragments.size() > 0 && mFragments.size() > mPosition) {
                MarketItemFragment fragment =  mFragments.get(mPosition);
                fragment.mPushRefresh(event.marketMessage.getInstrumentID(), event.marketMessage.getLastPrice(), event.marketMessage.getChg(), event.marketMessage.getOpenInterest(), event.marketMessage.getChange());
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UIBaseEvent event) {
        if (event instanceof RefreshUIEvent) {
            int code = ((RefreshUIEvent) event).getType();
            if (code == UIBaseEvent.EVENT_HOME_TO_MARKET_ZHULI_TRADE_ZHULI) {//首页跳转主力合约
                tabAdapter.updateTabSelection(ZLHY_TYPE);
                setCurrentTab(1);
            }else if (code == UIBaseEvent.EVENT_HOME_TO_MARKET_ZHULI_TRADE_ZX){//跳转自选
                tabAdapter.updateTabSelection(ZXHQ_TYPE);
                setCurrentTab(0);
            }
        }
    }

    //MPush
    public void requestMarket(String productList) {
        if (!TextUtils.isEmpty(productList)) {
            MPushUtil.requestMarket(productList);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onAddAptional() {
        String one ="行情_添加自选";
        String two ="hangqing_add_zixuan_click";
        MobclickAgent.onEvent(mContext,two, one);
        if (mAllMarketList != null && mAllMarketList.size() > 0) {
            if (mZxFragment != null) {
                    List<QuotationDataListBean> zxList = mZxFragment.getList();
                    Intent intent = new Intent(getActivity(), OptionalQuotesActivity.class);
                    intent.putExtra("allmarketlist", (Serializable) mAllMarketList);
                    intent.putExtra("zxmarketlist", (Serializable) zxList);
                    startActivity(intent);
            }
        }
    }

    @Override
    public void onMPushCodeRefresh(String mpushCode) {
        if (mFragments != null && mFragments.size() > 0 && mFragments.size() > mPosition) {
            MarketItemFragment fragment = mFragments.get(mPosition);
            fragment.setOnAddAptionalListener(MarketFragment.this);
            mPushCode = fragment.getPushCode();
            mTabSelectType = fragment.getTabSelectType();
            if (ZXHQ_TYPE.equals(mTabSelectType)) {
                requestMarket(mPushCode);
            }
        }
    }


    @Override
    public List<QuotationDataListBean> onZXMarketList() {
        return mZxFragment != null ? mZxFragment.getList() : null;
    }

    @Override
    public void loadData() {
        mMarketHelper = new MarketHelper();
        EventBus.getDefault().register(this);
        tabAdapter = new MarketTabAdapter(mTabList,ZXHQ_TYPE,getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(tabAdapter);
        tabAdapter.setOnItemClickListener(new MarketTabAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position,String type) {
                mPosition = position;
                tabAdapter.updateTabSelection(type);
                setCurrentTab(position);
            }
        });
        if (mHttpState == 0 && mPresenter != null) {
            mHttpState = 1;
            mPresenter.getMarketData();
        }
    }

    private void clickTab(int position){
        MarketTabBean tabBean = mTabList.get(position);
        String one ="hangqing_"+tabBean.getType()+"_click";
        String two ="行情_"+tabBean.getTitle();
        MobclickAgent.onEvent(mContext,one, two);
    }

    /**
     * 接口请求返回数据
     *
     * @param alysisBean
     */
    @Override
    public void getMarketData(MarketnalysisBean alysisBean) {
        if (mTabList != null && mTabList.size() > 0 && mFragments !=null && mFragments.size() > 0) {
            if (alysisBean !=null && alysisBean.getList() !=null && alysisBean.getList().size() > 0){
                //接口请求刷新数据
                mMarketHelper.marketDataRefresh(mFragments,alysisBean);
            }
        }else if (alysisBean !=null && alysisBean.getList() !=null){
            //自选行情
            List<QuotationDataListBean> zxMarketList = mMarketHelper.getZxMarketList(alysisBean);
            this.mAllMarketList =  alysisBean.getList();
            //主力合约
            List<QuotationDataListBean> zlhyMarketList = mMarketHelper.getZlhyMarketList(mAllMarketList);
            mTabList.clear();
            mFragments.clear();
            mTabList.add(new MarketTabBean("自选", ZXHQ_TYPE));
            mTabList.add(new MarketTabBean("主力合约", ZLHY_TYPE));
            mZxFragment = new MarketItemFragment();
            mZxFragment.setArguments(mZxFragment.setArgumentData(ZXHQ_TYPE,mAllMarketList,zxMarketList, "自选"));
            MarketItemFragment zlhyFragment = new MarketItemFragment();
            zlhyFragment.setArguments(zlhyFragment.setArgumentData(ZLHY_TYPE,null ,zlhyMarketList, "主力合约"));
            zlhyFragment.setOnZXMarketListListener(this);
            mZxFragment.setOnAddAptionalListener(this);
            mZxFragment.setOnMPushCodeRefreshListener(this);
            mFragments.add(mZxFragment);
            mFragments.add(zlhyFragment);
            for (ListBean bean : mAllMarketList) {
                mTabList.add(new MarketTabBean(bean.getExchangeName(), bean.getExcode()));
                MarketItemFragment fragment = new MarketItemFragment();
                fragment.setArguments(fragment.setArgumentData(bean.getExcode(), null,bean.getQuotationDataList(), bean.getExchangeName()));
                fragment.setOnZXMarketListListener(this);
                mFragments.add(fragment);
            }

            if (zxMarketList != null && zxMarketList.size() > 0) {
                mPosition = 0;
                tabAdapter.notifyDataSetChanged(mTabList,ZXHQ_TYPE);
                setTabData(mPosition);
                mPushCode = mMarketHelper.mosaicMPushCode(zxMarketList);
            } else {
                mPosition = 1;
                tabAdapter.notifyDataSetChanged(mTabList,ZLHY_TYPE);
                setTabData(mPosition);
                mPushCode = mMarketHelper.mosaicMPushCode(zlhyMarketList);
            }
            App.mApp.setIsMarketInit();
            requestMarket(mPushCode);
        }
    }


    private void setTabData(int position){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        if (mTabList !=null && mFragments !=null && mTabList.size() == mFragments.size()){
            for (int i = 0;i < mFragments.size() ; i ++){
                MarketItemFragment itemFragment = mFragments.get(i);
                if (i == position){
                    transaction.add(R.id.market_fragment_container,itemFragment).show(itemFragment);
                }else {
                    transaction.add(R.id.market_fragment_container,itemFragment).hide(itemFragment);
                }
            }
            transaction.commitAllowingStateLoss();
            mIsInItFragment = true;
        }
    }


    //选择对应的mFragments
    private void setCurrentTab(int position){
        mPosition = position;
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        if (mTabList !=null && mFragments !=null && mTabList.size() == mFragments.size() && mIsInItFragment){

            for (int i = 0 ; i < mFragments.size() ; i ++){
                MarketItemFragment itemFragment = mFragments.get(i);
                if (i == position){
                    clickTab(position);
                    transaction.show(itemFragment);
                    mPushCode = itemFragment.getPushCode();
                    mTabSelectType = itemFragment.getTabSelectType();
                    requestMarket(mPushCode);
                    itemFragment.getRealTimeData();
                }else {
                    transaction.hide(itemFragment);
                }
            }
            transaction.commitAllowingStateLoss();
        }
    }

}
