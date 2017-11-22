package com.honglu.future.ui.trade.kchart;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.config.Constant;
import com.honglu.future.dialog.AccountLoginDialog;
import com.honglu.future.dialog.BillConfirmDialog;
import com.honglu.future.dialog.BuildTransactionDialog;
import com.honglu.future.dialog.ProductRuleDialog;
import com.honglu.future.dialog.klineposition.KLinePositionDialog;
import com.honglu.future.events.ReceiverMarketMessageEvent;
import com.honglu.future.events.RefreshUIEvent;
import com.honglu.future.events.UIBaseEvent;
import com.honglu.future.mpush.MPushUtil;
import com.honglu.future.ui.main.contract.AccountContract;
import com.honglu.future.ui.main.presenter.AccountPresenter;
import com.honglu.future.ui.trade.adapter.KChartFragmentAdapter;
import com.honglu.future.ui.trade.bean.AccountBean;
import com.honglu.future.ui.trade.bean.HoldPositionBean;
import com.honglu.future.ui.trade.bean.ProductListBean;
import com.honglu.future.ui.trade.bean.RealTimeBean;
import com.honglu.future.ui.trade.bean.SettlementInfoBean;
import com.honglu.future.util.ConvertUtil;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.NumberUtil;
import com.honglu.future.util.ProFormatConfig;
import com.honglu.future.util.SpUtil;
import com.honglu.future.widget.RiseNumberTextView;
import com.honglu.future.widget.kchart.SlidingTabLayout;
import com.honglu.future.widget.kchart.ViewPagerEx;
import com.honglu.future.widget.kchart.fragment.KLineFragment;
import com.honglu.future.widget.kchart.fragment.KMinuteFragment;
import com.honglu.future.widget.kchart.util.BakSourceInterface;
import com.honglu.future.widget.popupwind.KLinePopupWin;
import com.honglu.future.widget.tab.OnTabSelectListener;
import com.xulu.mpush.message.RequestMarketMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import butterknife.BindView;
import butterknife.OnClick;

import static com.honglu.future.util.ToastUtil.showToast;

/**
 * Created by zq on 2017/11/7.
 */

public class KLineMarketActivity extends BaseActivity<KLineMarketPresenter> implements KLineMarketContract.View, AccountContract.View, KLinePopupWin.OnPopItemClickListener,BillConfirmDialog.OnConfirmClickListener {
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
    RiseNumberTextView mTvNewPrice;
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
    @BindView(R.id.iv_show_popup)
    ImageView mIvShowPopup;
    @BindView(R.id.tv_hold_num)
    TextView mHoldNum;

    private String mExcode;
    private String mCode;
    private String mClosed;
    private String isClosed;
    private boolean mIsShowDetail;

    private String[] mTitles = {"分时", "1分钟", "5分钟", "15分钟", "30分钟", "1小时", "4小时", "日线"};

    private KLineFragment mKLineFragment;
    private BuildTransactionDialog mBuildTransactionDialog;
    private AccountPresenter mAccountPresenter;
    private AccountLoginDialog mAccountLoginDialog;
    private int mPosition;
    private List<Fragment> fragments;
    private KMinuteFragment fragment;
    private KLinePopupWin mKLinePopupWin;
    private ProductRuleDialog mProductRuleDialog;
    private KLinePositionDialog mKLinePositionDialog;
    private List<HoldPositionBean> mChiCangList;//持仓列表
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            getPositionList();
        }
    };
    private RealTimeBean.Data mBean;
    private RequestMarketMessage mRequestBean;
    private ProductListBean productListBean = null;
    private String mKlineCycleType;
    private BillConfirmDialog billConfirmDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_k_line_market;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mKLinePositionDialog.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMarketEventMainThread(ReceiverMarketMessageEvent event) {
        if (event.marketMessage.getInstrumentID().equals(mCode)) {
            mRequestBean = event.marketMessage;
            transferBean(mRequestBean);
            if (mPosition == 0) {
                fragment.setLastKData(mRequestBean);
            } else {
                ((KLineFragment) fragments.get(mPosition)).setLastKData(mRequestBean, mKlineCycleType);
            }
            if (mKLinePositionDialog != null
                    && mKLinePositionDialog.isShowing() && !TextUtils.isEmpty(mKLinePositionDialog.getPushCode())) {
                mKLinePositionDialog.pushRefresh(mRequestBean.getLowerLimitPrice(), mRequestBean.getUpperLimitPrice(), mRequestBean.getLastPrice());
            }

            if (mBuildTransactionDialog != null
                    && mBuildTransactionDialog.isShowing()) {
                productListBean.setAskPrice1(mRequestBean.getAskPrice1());
                productListBean.setBidPrice1(mRequestBean.getBidPrice1());
                productListBean.setTradeVolume(mRequestBean.getVolume());
                mBuildTransactionDialog.pushRefresh(productListBean);
            }
        }
    }

    @Override
    public void showLoading(String content) {
        if (!TextUtils.isEmpty(content)) {
            App.loadingContent(this, content);
        }
    }

    @Override
    public void stopLoading() {
        App.hideLoading();
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        showToast(msg);
    }

    private void transferBean(RequestMarketMessage bean) {
        mBean.setBidPrice1(bean.getBidPrice1());
        mBean.setAskPrice1(bean.getAskPrice1());
        mBean.setVolume(bean.getVolume());
        mBean.setOpenInterest(bean.getOpenInterest());
        mBean.setUpperLimitPrice(bean.getUpperLimitPrice());
        mBean.setOpenPrice(bean.getOpenPrice());
        mBean.setHighestPrice(bean.getHighestPrice());
        mBean.setLowerLimitPrice(bean.getLowerLimitPrice());
        mBean.setAveragePrice(bean.getAveragePrice());
        mBean.setLowestPrice(bean.getLowestPrice());
        mBean.setPreClosePrice(bean.getPreClosePrice());
        mBean.setSettlementPrice(bean.getSettlementPrice());
        mBean.setPreSettlementPrice(bean.getPreSettlementPrice());
        mBean.setChange(bean.getChange());
        mBean.setChg(bean.getChg());
        mBean.setLastPrice(bean.getLastPrice());
        setTextValue(mBean);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UIBaseEvent event) {
        if (event instanceof RefreshUIEvent) {
            int type = ((RefreshUIEvent) event).getType();
            if (event.EVENT_CLOSETRAD_REFRESH == type) {
                int position = Integer.parseInt(event.getCode());
                if (mChiCangList != null && mChiCangList.size() > position) {
                    mChiCangList.remove(position);
                    mHoldNum.setText(String.valueOf(mChiCangList.size()));
                }
            }
        }
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
        mAccountPresenter = new AccountPresenter();
        mAccountPresenter.init(this);
    }

    @Override
    public void loadData() {
        EventBus.getDefault().register(this);
        mExcode = getIntent().getStringExtra("excode");
        mCode = getIntent().getStringExtra("code");
        mClosed = getIntent().getStringExtra("close");
        isClosed = getIntent().getStringExtra("isClosed");//是否休市
        mKLinePositionDialog = new KLinePositionDialog(KLineMarketActivity.this);
        mKLinePopupWin = new KLinePopupWin(this);
        mKLinePopupWin.setOnPopItemClickListener(this);
        mPresenter.getProductDetail(mCode);
        mPresenter.getProductRealTime(mExcode + "|" + mCode);

        if (App.getConfig().getAccountLoginStatus()) {
            mHandler.postDelayed(mRunnable, 300);
        }
        MPushUtil.requestMarket(mExcode + "|" + mCode);
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
                switch (position) {
                    case 1:
                        mKlineCycleType = BakSourceInterface.PARAM_KLINE_1M_WEIPAN;
                        break;
                    case 2:
                        mKlineCycleType = BakSourceInterface.PARAM_KLINE_5M_WEIPAN;
                        break;
                    case 3:
                        mKlineCycleType = BakSourceInterface.PARAM_KLINE_15M_WEIPAN;
                        break;
                    case 4:
                        mKlineCycleType = BakSourceInterface.PARAM_KLINE_30M_WEIPAN;
                        break;
                    case 5:
                        mKlineCycleType = BakSourceInterface.PARAM_KLINE_60M_WEIPAN;
                        break;
                    case 6:
                        mKlineCycleType = BakSourceInterface.PARAM_KLINE_4H_WEIPAN;
                        break;
                    case 7:
                        mKlineCycleType = BakSourceInterface.PARAM_KLINE_1D_WEIPAN;
                        break;
                    case 8:
                        mKlineCycleType = BakSourceInterface.PARAM_KLINE_WEEK_WEIPAN;
                        break;
                }
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
                fragment = new KMinuteFragment();
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
        mBean = bean.getList().get(0);
        mTvName.setText(mBean.getName());

        mTvNameLand.setText(mBean.getName());

        setTextValue(mBean);

        initViewPager(mBean.getPreClosePrice());
        initListener();
    }

    public void setTextValue(RealTimeBean.Data bean) {
        if (mTvBuyPrice != null)
            mTvBuyPrice.setText(bean.getBidPrice1());
        if (mTvVol != null)
            mTvVol.setText(bean.getVolume());
        if (mTvSellPrice != null)
            mTvSellPrice.setText(bean.getAskPrice1());
        if (mHoldVol != null)
            mHoldVol.setText(bean.getOpenInterest());
        if (mTvZt != null)
            mTvZt.setText(bean.getUpperLimitPrice());
        if (mTvKp != null)
            mTvKp.setText(bean.getOpenPrice());
        if (mTvZg != null)
            mTvZg.setText(bean.getHighestPrice());
        if (mTvDt != null)
            mTvDt.setText(bean.getLowerLimitPrice());
        if (mTvJj != null)
            mTvJj.setText(bean.getAveragePrice());
        if (mTvZd != null)
            mTvZd.setText(bean.getLowestPrice());
        if (mTvZs != null)
            mTvZs.setText(bean.getPreClosePrice());
        if (mTvRisePrice != null)
            mTvRisePrice.setText(bean.getAskPrice1());
        if (mTvDownPrice != null)
            mTvDownPrice.setText(bean.getBidPrice1());

        if (mTvJs != null) {
            if (TextUtils.isEmpty(bean.getSettlementPrice())) {
                mTvJs.setText("--");
            } else {
                if (Double.parseDouble(bean.getSettlementPrice()) == 0) {
                    mTvJs.setText("--");
                } else {
                    mTvJs.setText(bean.getSettlementPrice());
                }
            }
        }

        if (mTvZj != null) {
            if (TextUtils.isEmpty(bean.getPreSettlementPrice())) {
                mTvZj.setText("--");
            } else {
                if (Double.parseDouble(bean.getPreSettlementPrice()) == 0) {
                    mTvZj.setText("--");
                } else {
                    mTvZj.setText(bean.getPreSettlementPrice());
                }
            }
        }

        String prefix = "";
        int mcolor = getResources().getColor(R.color.color_opt_lt);

        double diff = Double.parseDouble(bean.getChange());
        if (diff > 0) {
            mcolor = getResources().getColor(R.color.color_opt_gt);
            prefix = "+";
        }
        if (diff == 0) {
            mcolor = getResources().getColor(R.color.color_opt_eq);
            prefix = "";
        }

        mTvKp.setTextColor(mcolor);
        mTvZg.setTextColor(mcolor);
        mTvJj.setTextColor(mcolor);
        mTvZd.setTextColor(mcolor);

        //格式化小数点
        String change = bean.getChange();
        String changeRate = bean.getChg();

        if (mTvNewPrice != null) {
            mTvNewPrice.setOptional(bean);
            mTvNewPrice.setTextByAnimation(ConvertUtil.NVL(bean.getLastPrice(), ""));

            mTvNewPrice.setTextByAnimation(ProFormatConfig.formatByCodes(mCode, ConvertUtil.NVL(bean.getLastPrice(), ConvertUtil.NVL(bean.getLastPrice(), ""))));
            mTvNewPrice.setTextColor(mcolor);
        }

        if (mTvRiseNum != null) {
            mTvRiseNum.setTextColor(mcolor);
            mTvRiseNum.setText(prefix + NumberUtil.moveLast0(change));
            mTvRiseRadio.setTextColor(mcolor);
            mTvRiseRadio.setText(prefix + changeRate);
        }

        if (mTvRiseNumLand != null) {
            mTvRiseNumLand.setText(prefix + NumberUtil.moveLast0(change));
            mTvRiseNumLand.setTextColor(mcolor);
            mTvRiseRadioLand.setText(prefix + changeRate);
            mTvRiseRadioLand.setTextColor(mcolor);
        }
    }

    private void getPositionList() {
        mPresenter.getHoldPositionList(SpUtil.getString(Constant.CACHE_TAG_UID), SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN), Constant.COMPANY_CODE);
    }

    //持仓列表
    @Override
    public void getHoldPositionListSuccess(List<HoldPositionBean> list) {
        if (list == null || list.size() <= 0 || mHoldNum == null) {
            return;
        }
        this.mChiCangList = getList(mExcode, mCode, list);
        if (mChiCangList != null) {
            mHoldNum.setText(String.valueOf(mChiCangList.size()));
        }
    }

    @Override
    public void getProductDetailSuccess(ProductListBean bean) {
        productListBean = bean;
        isClosed = bean.getIsClosed();
        if (bean.getIsClosed().equals("2")) {
            mTvClosed.setVisibility(View.VISIBLE);
        } else {
            mTvClosed.setVisibility(View.GONE);
        }
        mProductRuleDialog = new ProductRuleDialog(this, bean);
    }

    @OnClick({R.id.iv_pull, R.id.iv_back, R.id.buy_up, R.id.buy_down, R.id.hold_position, R.id.iv_full_screen,
            R.id.iv_show_popup, R.id.show_tip})
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
                    mBuildTransactionDialog = new BuildTransactionDialog(mContext, BuildTransactionDialog.TRADE_BUY_RISE, productListBean);
                    mBuildTransactionDialog.show();
                } else {
                    showAccountLoginDialog();
                }
                break;
            case R.id.buy_down:
                if (App.getConfig().getAccountLoginStatus()) {
                    mBuildTransactionDialog = new BuildTransactionDialog(mContext, BuildTransactionDialog.TRADE_BUY_DOWN, productListBean);
                    mBuildTransactionDialog.show();
                } else {
                    showAccountLoginDialog();
                }
                break;
            case R.id.hold_position:
                if (App.getConfig().getAccountLoginStatus()) {

                    String mName = mTvName.getText().toString();
                    if (!TextUtils.isEmpty(mName)) {
                        if (mChiCangList != null && mChiCangList.size() > 0) {
                            boolean mClosed = "2".equals(isClosed);
                            mKLinePositionDialog.setPositionData(mClosed, mExcode, mCode, mName, mChiCangList).showDialog();
                        } else {
                            //getPositionList();
//                            EventBus.getDefault().post(new ChangeTabMainEvent(FragmentFactory.FragmentStatus.Trade));
//                            finish();
                        }
                    }
                } else {
                    showAccountLoginDialog();
                }
                break;
            case R.id.iv_full_screen:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case R.id.iv_show_popup:
                mKLinePopupWin.showPopupWind(v);
                break;
            case R.id.show_tip:
                mProductRuleDialog.show();
                break;
        }
    }

    private void showAccountLoginDialog() {
        mAccountLoginDialog = new AccountLoginDialog(mContext, mAccountPresenter);
        mAccountLoginDialog.show();
    }

    @Override
    public void loginSuccess(AccountBean bean) {
        showToast("登录成功");
        if(billConfirmDialog!=null&& billConfirmDialog.isShowing()){
            billConfirmDialog.dismiss();
        }
        mAccountLoginDialog.dismiss();
    }

    @Override
    public void showSettlementDialog(SettlementInfoBean bean) {
        billConfirmDialog = new BillConfirmDialog(mContext,bean);
        billConfirmDialog.setOnConfirmClickListenerr(this);
        billConfirmDialog.show();
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

    @Override
    public void onFullScreeClick() {
        //横屏竖屏切换
        if (Configuration.ORIENTATION_LANDSCAPE == getResources()
                .getConfiguration().orientation) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }


    //过滤数据 获取当前产品list
    private List<HoldPositionBean> getList(String excode, String instrumentId, List<HoldPositionBean> list) {
        if (TextUtils.isEmpty(instrumentId) || list == null || list.size() <= 0) {
            return null;
        }
        ListIterator<HoldPositionBean> iterator = list.listIterator();
        while (iterator.hasNext()) {
            HoldPositionBean bean = iterator.next();
            if (!excode.equals(bean.getExcode()) || !instrumentId.equals(bean.getInstrumentId())) {
                iterator.remove();
            }
        }
        return list;
    }

    //获取当前产品持仓数量
    private int getPingCangNum(List<HoldPositionBean> list) {
        int mPingCangNum = 0;
        if (list != null && list.size() > 0) {
            for (HoldPositionBean bean : list) {
                mPingCangNum += bean.getPosition();
            }
        }
        return mPingCangNum;
    }

    @Override
    public void onConfirmClick() {
        mAccountPresenter.settlementConfirm(SpUtil.getString(Constant.CACHE_TAG_UID));
    }
}
