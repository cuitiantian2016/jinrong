package com.honglu.future.ui.trade.kchart;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.config.Constant;
import com.honglu.future.ui.trade.adapter.KChartFragmentAdapter;
import com.honglu.future.ui.trade.bean.RealTimeBean;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.NumberUtils;
import com.honglu.future.widget.kchart.SlidingTabLayout;
import com.honglu.future.widget.kchart.ViewPagerEx;
import com.honglu.future.widget.kchart.fragment.KLineFragment;
import com.honglu.future.widget.kchart.fragment.KMinuteFragment;
import com.honglu.future.widget.tab.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.honglu.future.util.ToastUtil.showToast;

/**
 * Created by zq on 2017/11/7.
 */

public class KLineMarketActivity extends BaseActivity<KLineMarketPresenter> implements KLineMarketContract.View {
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
    private String mExcode;
    private String mCode;
    private String mClosed;
    private String isClosed;
    private boolean mIsShowDetail;

    private String[] mTitles = {"分时", "1分钟", "5分钟", "15分钟", "30分钟", "1小时", "4小时", "日线", "周线"};

    private KLineFragment mKLineFragment;

    @Override
    public int getLayoutId() {
        return R.layout.activity_k_line_market;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
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

    private void initListener() {

        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                //setUmengKLine(position);
            }

            @Override
            public void onTabReselect(int position) {

            }

            @Override
            public void onTabUnselected(int position) {

            }
        });
//        //选中K线监听
//        chooseKLineListener();
//
//        //选中分时线监听
//        chooseMinuteListener();
    }

    private void initViewPager(String closePrice) {
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < mTitles.length; i++) {
            if (i == 0) {
                KMinuteFragment fragment = new KMinuteFragment();
                fragment.setExcode(mExcode);
                fragment.setCode(mCode);
                fragment.setClosed(closePrice);
                fragment.setTimeStr(Constant.CLOSE_TIME_AG);
//                fragment.setTouchEnabled(false);
//                fragment.setShowHighLine(true);
//                fragment.setGoodsType(mGoodsType);
                //mOnKLineRefreshListener = fragment;
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
//                mLightningFragment = new LightningFragment();
//                mLightningFragment.setGoodsType(mGoodsType);
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
            mTvRiseRadio.setText("+" + NumberUtils.getFloatStr2(radio) + "%");
        } else {
            mTvRiseNum.setText("-" + riseNum);
            mTvRiseRadio.setText("-" + NumberUtils.getFloatStr2(radio) + "%");
        }
        mTvBuyPrice.setText(lastPrice);
        mTvSellPrice.setText(String.valueOf(Float.valueOf(lastPrice) + 1));
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

        initViewPager(mRealBean.getPreClosePrice());
        initListener();
    }

    @OnClick({R.id.iv_pull, R.id.iv_back, R.id.buy_up, R.id.buy_down, R.id.hold_position})
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
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.buy_up:
                showToast("买涨");
                break;
            case R.id.buy_down:
                showToast("买跌");
                break;
            case R.id.hold_position:
                showToast("持仓");
                break;
        }
    }
}
