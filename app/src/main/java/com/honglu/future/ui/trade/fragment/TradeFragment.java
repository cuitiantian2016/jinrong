package com.honglu.future.ui.trade.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.config.ConfigUtil;
import com.honglu.future.events.ChangeTabEvent;
import com.honglu.future.events.RefreshUIEvent;
import com.honglu.future.events.UIBaseEvent;
import com.honglu.future.mpush.MPushUtil;
import com.honglu.future.ui.main.activity.WebViewActivity;
import com.honglu.future.ui.market.fragment.MarketFragment;
import com.honglu.future.ui.trade.contract.TradeContract;
import com.honglu.future.ui.trade.presenter.TradePresenter;
import com.honglu.future.util.Tool;
import com.honglu.future.widget.tab.CommonTabLayout;
import com.honglu.future.widget.tab.CustomTabEntity;
import com.honglu.future.widget.tab.SimpleOnTabSelectListener;
import com.honglu.future.widget.tab.TabEntity;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zq on 2017/10/24.
 */

public class TradeFragment extends BaseFragment<TradePresenter> implements TradeContract.View {
    @BindView(R.id.trade_common_tab_layout)
    CommonTabLayout mCommonTabLayout;
    private ArrayList<CustomTabEntity> mTabList;
    private ArrayList<Fragment> mFragments;
    private OpenTransactionFragment mOpenTransactionFragment;
    private PositionFragment mPositionFragment;
    private ClosePositionFragment mClosePositionFragment;
    //private EntrustFragment mEntrustFragment;
    private EntrustFragment mEntrustFragment;
    private MarketFragment mMarketFragment;
    private int currentPosition;

    public static TradeFragment tradeFragment;

    public static TradeFragment getInstance() {
        if (tradeFragment == null) {
            tradeFragment = new TradeFragment();
        }
        return tradeFragment;
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
    public int getLayoutId() {
        return R.layout.fragment_trade_layout;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        //添加tab实体
        addTabEntities();
        //添加fragment
        addFragments();
    }

    private void addTabEntities() {
        mTabList = new ArrayList<>();
        mTabList.add(new TabEntity(mContext.getString(R.string.trade_market), mContext.getString(R.string.trade_market_type)));
        mTabList.add(new TabEntity(mContext.getString(R.string.trade_build), mContext.getString(R.string.trade_build_type)));
        mTabList.add(new TabEntity(mContext.getString(R.string.trade_hold), mContext.getString(R.string.trade_hold_type)));
        mTabList.add(new TabEntity(mContext.getString(R.string.trade_closed), mContext.getString(R.string.trade_closed_type)));
        mTabList.add(new TabEntity(mContext.getString(R.string.trade_agent), mContext.getString(R.string.trade_agent_type)));
    }

    private void addFragments() {
        if (mFragments == null) {
            mFragments = new ArrayList<>();
        }
        mMarketFragment = new MarketFragment();//行情
        mFragments.add(mMarketFragment);
        mOpenTransactionFragment = new OpenTransactionFragment();//建仓
        mFragments.add(mOpenTransactionFragment);
        mPositionFragment = new PositionFragment();
        mFragments.add(mPositionFragment);
        mClosePositionFragment = new ClosePositionFragment();
        mFragments.add(mClosePositionFragment);
        mEntrustFragment = new EntrustFragment();
        mFragments.add(mEntrustFragment);

        mCommonTabLayout.setTabData(mTabList, (FragmentActivity) mContext, R.id.trade_fragment_container, mFragments);
        mCommonTabLayout.setOnTabSelectListener(new SimpleOnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                super.onTabSelect(position);
                currentPosition = position;
                click(position);
            }
        });
    }

    private void click(int currentPosition) {
        String one = "交易_" + mTabList.get(currentPosition).getTabTitle();
        String two = "jiaoyi_" + mTabList.get(currentPosition).getTabType() + "_click";
        MobclickAgent.onEvent(mContext, two, one);
    }

    @OnClick({R.id.iv_rule})
    public void onClick(View view) {
        if (Tool.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.iv_rule:
                Intent intent = new Intent(mActivity, WebViewActivity.class);
                intent.putExtra("title", "交易规则");
                intent.putExtra("url", ConfigUtil.TRADE_RULE);
                startActivity(intent);
                MobclickAgent.onEvent(mContext, "yiaoyi_jiaoyiguize_click", "交易_交易规则");
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            mOpenTransactionFragment.stopRun();
            mPositionFragment.stopRun();
            MPushUtil.pauseRequest();
        } else {
            if (!TextUtils.isEmpty(MPushUtil.CODES_TRADE_HOME) && currentPosition == 0) {
                MPushUtil.requestMarket(MPushUtil.CODES_TRADE_HOME);
            }
            if (currentPosition == 0) {
                mOpenTransactionFragment.refreshProductList();
                mOpenTransactionFragment.startRun();
            } else if (currentPosition == 1) {
                mPositionFragment.startRun();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mOpenTransactionFragment.stopRun();
        mPositionFragment.stopRun();
        MPushUtil.pauseRequest();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden() && isVisible()) {
            if (currentPosition == 0) {
                mOpenTransactionFragment.startRun();
            } else if (currentPosition == 1) {
                mPositionFragment.startRun();
            }
        }
        if (!TextUtils.isEmpty(MPushUtil.CODES_TRADE_HOME) && currentPosition == 0 && !isHidden()) {
            MPushUtil.requestMarket(MPushUtil.CODES_TRADE_HOME);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UIBaseEvent event) {
        if (event instanceof ChangeTabEvent) {
            mCommonTabLayout.setCurrentTab(((ChangeTabEvent) event).getLoanType());
        } else if (event instanceof RefreshUIEvent) {
            int code = ((RefreshUIEvent) event).getType();
            if (code == UIBaseEvent.EVENT_ACCOUNT_LOGOUT) {//安全退出期货账户
                if (!App.getConfig().getAccountLoginStatus()) {
                    mCommonTabLayout.setCurrentTab(0);
                }
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        tradeFragment = null;
    }

}
