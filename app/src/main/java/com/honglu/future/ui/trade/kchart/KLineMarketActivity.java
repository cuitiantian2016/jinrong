package com.honglu.future.ui.trade.kchart;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.dialog.AccountLoginDialog;
import com.honglu.future.dialog.BuildTransactionDialog;
import com.honglu.future.events.ChangeTabMainEvent;
import com.honglu.future.ui.main.FragmentFactory;
import com.honglu.future.ui.main.contract.AccountContract;
import com.honglu.future.ui.main.presenter.AccountPresenter;
import com.honglu.future.ui.trade.adapter.KChartFragmentAdapter;
import com.honglu.future.ui.trade.bean.AccountBean;
import com.honglu.future.ui.trade.bean.RealTimeBean;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.NumberUtils;
import com.honglu.future.widget.kchart.SlidingTabLayout;
import com.honglu.future.widget.kchart.ViewPagerEx;
import com.honglu.future.widget.kchart.fragment.KLineFragment;
import com.honglu.future.widget.kchart.fragment.KMinuteFragment;
import com.honglu.future.widget.tab.OnTabSelectListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zq on 2017/11/7.
 */

public class KLineMarketActivity extends BaseActivity<KLineMarketPresenter> implements KLineMarketContract.View, AccountContract.View {
    @BindView(R.id.tablayout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPagerEx mViewPager;
    @BindView(R.id.ll_pull_view)
    LinearLayout mLlPullView;
    @BindView(R.id.iv_pull)
    ImageView mIvPull;
    @BindView(R.id.tv_closed)
    TextView mTvClosed;
    @BindView(R.id.tv_new_price)
    TextView mTvNewPrice;
    @BindView(R.id.tv_rise_num)
    TextView mTvRiseNum;
    @BindView(R.id.tv_rise_radio)
    TextView mTvRiseRadio;
    @BindView(R.id.tv_buy_price)
    TextView mTvBuyPrice;
    @BindView(R.id.tv_sell_price)
    TextView mTvSellPrice;
    @BindView(R.id.tv_vol)
    TextView mTvVol;
    @BindView(R.id.tv_hold_vol)
    TextView mHoldVol;
    @BindView(R.id.tv_zt)
    TextView mTvZt;
    @BindView(R.id.tv_kp)
    TextView mTvKp;
    @BindView(R.id.tv_zg)
    TextView mTvZg;
    @BindView(R.id.tv_dt)
    TextView mTvDt;
    @BindView(R.id.tv_jj)
    TextView mTvJj;
    @BindView(R.id.tv_zd)
    TextView mTvZd;
    @BindView(R.id.tv_zs)
    TextView mTvZs;
    @BindView(R.id.tv_js)
    TextView mTvJs;
    @BindView(R.id.tv_zj)
    TextView mTvZj;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_rise_price)
    TextView mTvRisePrice;
    @BindView(R.id.tv_down_price)
    TextView mTvDownPrice;
    @BindView(R.id.ll_title_bar)
    LinearLayout mLlTitleBar;
    @BindView(R.id.tv_name_land)
    TextView mTvNameLand;
    @BindView(R.id.tv_rise_num_land)
    TextView mTvRiseNumLand;
    @BindView(R.id.tv_rise_radio_land)
    TextView mTvRiseRadioLand;
    @BindView(R.id.ll_bottom_tabs)
    LinearLayout mLlBottomTabs;
    @BindView(R.id.iv_full_screen)
    ImageView mIvFull;
    private String mExcode;
    private String mCode;
    private String mClosed;
    private String isClosed;
    private boolean mIsShowDetail;

    private String[] mTitles = {"分时", "1分钟", "5分钟", "15分钟", "30分钟", "1小时", "4小时", "日线", "周线"};

    private KLineFragment mKLineFragment;
    private BuildTransactionDialog mBuildTransactionDialog;
    private AccountPresenter mAccountPresenter;
    private AccountLoginDialog mAccountLoginDialog;
    private int mPosition;
    private List<Fragment> fragments;

    @Override
    public int getLayoutId() {
        return R.layout.activity_k_line_market;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
        mAccountPresenter = new AccountPresenter();
        mAccountPresenter.init(this);
    }

    @Override
    public void loadData() {
        mExcode = getIntent().getStringExtra("excode");
        mCode = getIntent().getStringExtra("code");
        mClosed = getIntent().getStringExtra("close");
        isClosed = getIntent().getStringExtra("isClosed");//是否休市
        if (isClosed.equals("2")) {
            mTvClosed.setVisibility(View.VISIBLE);
        }

        mPresenter.getProductRealTime(mExcode + "|" + mCode);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //当前为横屏
            handLandView(true);

        } else {
            //切换到竖屏
            handLandView(false);
        }
    }

    private void handLandView(boolean isLand) {
        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) mTabLayout.getLayoutParams();
        RelativeLayout.LayoutParams viewPagerParams =
                (RelativeLayout.LayoutParams) mViewPager.getLayoutParams();

        if (isLand) {
            mLlTitleBar.setVisibility(View.GONE);
            mTvNameLand.setVisibility(View.VISIBLE);
            mTvRiseNumLand.setVisibility(View.VISIBLE);
            mTvRiseRadioLand.setVisibility(View.VISIBLE);
            mLlBottomTabs.setVisibility(View.GONE);
            mTvRiseNum.setVisibility(View.GONE);
            mTvRiseRadio.setVisibility(View.GONE);
            mIvFull.setVisibility(View.VISIBLE);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mTabLayout.setLayoutParams(params);
            viewPagerParams.removeRule(RelativeLayout.BELOW);
            viewPagerParams.removeRule(RelativeLayout.ABOVE);
            viewPagerParams.addRule(RelativeLayout.ABOVE, R.id.tablayout);
            mViewPager.setLayoutParams(viewPagerParams);
        } else {
            mLlTitleBar.setVisibility(View.VISIBLE);
            mTvNameLand.setVisibility(View.GONE);
            mTvRiseNumLand.setVisibility(View.GONE);
            mTvRiseRadioLand.setVisibility(View.GONE);
            mLlBottomTabs.setVisibility(View.VISIBLE);
            mTvRiseNum.setVisibility(View.VISIBLE);
            mTvRiseRadio.setVisibility(View.VISIBLE);
            mIvFull.setVisibility(View.GONE);
            params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mTabLayout.setLayoutParams(params);
            viewPagerParams.addRule(RelativeLayout.BELOW, R.id.tablayout);
            viewPagerParams.removeRule(RelativeLayout.ABOVE);
            viewPagerParams.addRule(RelativeLayout.ABOVE, R.id.ll_bottom_tabs);
            mViewPager.setLayoutParams(viewPagerParams);
        }

    }

    private void initListener() {

        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                //setUmengKLine(position);
                mPosition = position;
            }

            @Override
            public void onTabReselect(int position) {

            }

            @Override
            public void onTabUnselected(int position) {

            }
        });
    }

    private void initViewPager(String closePrice) {
        fragments = new ArrayList<>();
        for (int i = 0; i < mTitles.length; i++) {
            if (i == 0) {
                KMinuteFragment fragment = new KMinuteFragment();
                fragment.setExcode(mExcode);
                fragment.setCode(mCode);
                fragment.setClosed(closePrice);
                fragments.add(fragment);
            } else {
                mKLineFragment = new KLineFragment();
                mKLineFragment.setExcode(mExcode);
                mKLineFragment.setCode(mCode);
                switch (i) {
                    case 1:
                        mKLineFragment.setType("10");
                        break;
                    case 2:
                        mKLineFragment.setType("2");
                        break;
                    case 3:
                        mKLineFragment.setType("3");
                        break;
                    case 4:
                        mKLineFragment.setType("4");
                        break;
                    case 5:
                        mKLineFragment.setType("5");
                        break;
                    case 6:
                        mKLineFragment.setType("9");
                        break;
                    case 7:
                        mKLineFragment.setType("6");
                        break;
                    case 8:
                        mKLineFragment.setType("7");
                        break;

                }
                fragments.add(mKLineFragment);
            }
        }
        KChartFragmentAdapter adapter = new KChartFragmentAdapter(getSupportFragmentManager());
        adapter.setTitles(mTitles);
        adapter.setFragments(fragments);
        mViewPager.setAdapter(adapter);
        int screenWidthDip = DeviceUtils.px2dip(this, DeviceUtils.getScreenWidth(this) - DeviceUtils.dip2px(this, 45));
        int tabWidth = (int) ((float) screenWidthDip / 5.7f);
        int indicatorWidth = (int) (tabWidth * 0.8f);
        mTabLayout.setTabWidth(tabWidth);
        mTabLayout.setIndicatorWidth(indicatorWidth);
        mTabLayout.setViewPager(mViewPager);
    }

    @Override
    public void getProductRealTimeSuccess(RealTimeBean bean) {
        if (bean == null || bean.getList() == null || bean.getList().size() == 0) {
            return;
        }
        RealTimeBean.Data mRealBean = bean.getList().get(0);
        String lastPrice = mRealBean.getLastPrice();
        float riseNum = Float.valueOf(lastPrice) - Float.valueOf(mRealBean.getPreSettlementPrice());
        float radio = (riseNum / Float.valueOf(mRealBean.getPreSettlementPrice())) * 100;
        mTvName.setText(mRealBean.getName());
        mTvNewPrice.setText(lastPrice);

        if (riseNum >= 0) {
            mTvRiseNum.setText("+" + riseNum);
            mTvRiseNumLand.setText("+" + riseNum);
            mTvRiseRadio.setText("+" + NumberUtils.getFloatStr2(radio) + "%");
            mTvRiseRadioLand.setText("+" + NumberUtils.getFloatStr2(radio) + "%");
        } else {
            mTvRiseNum.setText("-" + riseNum);
            mTvRiseNumLand.setText("-" + riseNum);
            mTvRiseRadio.setText("-" + NumberUtils.getFloatStr2(radio) + "%");
            mTvRiseRadioLand.setText("-" + NumberUtils.getFloatStr2(radio) + "%");
        }
        mTvBuyPrice.setText(String.valueOf(Float.valueOf(lastPrice) - 1));
        mTvSellPrice.setText(lastPrice);
        mTvVol.setText(mRealBean.getAskVolume1());
        mHoldVol.setText(mRealBean.getBidVolume1());
        mTvZd.setText(mRealBean.getUpperLimitPrice());
        mTvKp.setText(mRealBean.getOpenPrice());
        mTvZg.setText(mRealBean.getHighestPrice());
        mTvDt.setText(mRealBean.getLowerLimitPrice());
        mTvJj.setText(mRealBean.getAveragePrice());
        mTvZd.setText(mRealBean.getLowestPrice());
        mTvZs.setText(mRealBean.getClosePrice());
        mTvJs.setText(mRealBean.getSettlementPrice());
        mTvZj.setText(mRealBean.getPreSettlementPrice());
        mTvRisePrice.setText(lastPrice);
        mTvDownPrice.setText(String.valueOf(Float.valueOf(lastPrice) - 1));
        mTvNameLand.setText(mRealBean.getName());


        initViewPager(mRealBean.getPreClosePrice());
        initListener();
    }

    @OnClick({R.id.iv_pull, R.id.iv_back, R.id.buy_up, R.id.buy_down, R.id.hold_position, R.id.iv_full_screen})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_pull:
                if (!mIsShowDetail) {
                    mLlPullView.setVisibility(View.VISIBLE);
                    mIvPull.setImageResource(R.mipmap.ic_kline_up);
                    mIsShowDetail = true;
                } else {
                    mLlPullView.setVisibility(View.GONE);
                    mIvPull.setImageResource(R.mipmap.ic_kline_pull);
                    mIsShowDetail = false;
                }
                if (mPosition != 0) {
                    ((KLineFragment) fragments.get(mPosition)).setTabsLocation();
                }
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.buy_up:
                if (App.getConfig().getAccountLoginStatus()) {
                    mBuildTransactionDialog = new BuildTransactionDialog(mContext, BuildTransactionDialog.TRADE_BUY_RISE, mCode);
                    mBuildTransactionDialog.show();
                } else {
                    showAccountLoginDialog();
                }
                break;
            case R.id.buy_down:
                if (App.getConfig().getAccountLoginStatus()) {
                    mBuildTransactionDialog = new BuildTransactionDialog(mContext, BuildTransactionDialog.TRADE_BUY_DOWN, mCode);
                    mBuildTransactionDialog.show();
                } else {
                    showAccountLoginDialog();
                }
                break;
            case R.id.hold_position:
                if (App.getConfig().getAccountLoginStatus()) {
                    EventBus.getDefault().post(new ChangeTabMainEvent(FragmentFactory.FragmentStatus.Trade));
                    finish();
                } else {
                    showAccountLoginDialog();
                }
                break;
            case R.id.iv_full_screen:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
        }
    }

    private void showAccountLoginDialog() {
        mAccountLoginDialog = new AccountLoginDialog(mContext, mAccountPresenter);
        mAccountLoginDialog.show();
    }

    @Override
    public void loginSuccess(AccountBean bean) {
        mAccountLoginDialog.dismiss();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (Configuration.ORIENTATION_LANDSCAPE == getResources()
                    .getConfiguration().orientation) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
