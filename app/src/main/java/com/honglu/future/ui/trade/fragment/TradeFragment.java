package com.honglu.future.ui.trade.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.honglu.future.R;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.events.ChangeTabEvent;
import com.honglu.future.events.ChangeTabMainEvent;
import com.honglu.future.events.FragmentRefreshEvent;
import com.honglu.future.events.UIBaseEvent;
import com.honglu.future.mpush.MPushUtil;
import com.honglu.future.ui.main.activity.WebViewActivity;
import com.honglu.future.ui.trade.contract.TradeContract;
import com.honglu.future.ui.trade.presenter.TradePresenter;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.Tool;
import com.honglu.future.widget.kchart.fragment.KMinuteFragment;
import com.honglu.future.widget.tab.CommonTabLayout;
import com.honglu.future.widget.tab.CustomTabEntity;
import com.honglu.future.widget.tab.SimpleOnTabSelectListener;
import com.honglu.future.widget.tab.TabEntity;

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
        int screenWidthDip = DeviceUtils.px2dip(mContext, DeviceUtils.getScreenWidth(mContext));
        int indicatorWidth = (int) (screenWidthDip * 0.12f);
        mCommonTabLayout.setIndicatorWidth(indicatorWidth);
        //添加tab实体
        addTabEntities();
        //添加fragment
        addFragments();
    }

    private void addTabEntities() {
        mTabList = new ArrayList<>();
        mTabList.add(new TabEntity(mContext.getString(R.string.trade_build)));
        mTabList.add(new TabEntity(mContext.getString(R.string.trade_hold)));
        mTabList.add(new TabEntity(mContext.getString(R.string.trade_closed)));
        mTabList.add(new TabEntity(mContext.getString(R.string.trade_agent)));
    }

    private void addFragments() {
        if (mFragments == null) {
            mFragments = new ArrayList<>();
        }
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
            }
        });
    }

    @OnClick({R.id.iv_rule})
    public void onClick(View view) {
        if (Tool.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.iv_rule:
                //// TODO: 2017/10/27 跳转交易规则页面 
                Intent intent = new Intent(mActivity, WebViewActivity.class);
                intent.putExtra("url", "http://www.baidu.com");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            mOpenTransactionFragment.stopRun();
            MPushUtil.pauseRequest();
        } else {
            if (!TextUtils.isEmpty(MPushUtil.CODES_TRADE_HOME) && currentPosition == 0) {
                MPushUtil.requestMarket(MPushUtil.CODES_TRADE_HOME);
            }
            if (currentPosition == 0){
                mOpenTransactionFragment.startRun();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mOpenTransactionFragment.stopRun();
        MPushUtil.pauseRequest();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (currentPosition == 0&&!isHidden()&&isVisible()){
            mOpenTransactionFragment.startRun();
        }
        if (!TextUtils.isEmpty(MPushUtil.CODES_TRADE_HOME) && currentPosition == 0) {
            MPushUtil.requestMarket(MPushUtil.CODES_TRADE_HOME);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UIBaseEvent event) {
        if (event instanceof ChangeTabEvent) {
                mCommonTabLayout.setCurrentTab(((ChangeTabEvent) event).getLoanType());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        tradeFragment = null;
    }

}
