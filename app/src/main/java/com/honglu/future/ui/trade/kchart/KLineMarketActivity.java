package com.honglu.future.ui.trade.kchart;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
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
import com.honglu.future.ui.login.activity.LoginActivity;
import com.honglu.future.ui.main.contract.AccountContract;
import com.honglu.future.ui.main.presenter.AccountPresenter;
import com.honglu.future.ui.market.bean.MarketnalysisBean;
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
import com.honglu.future.util.StringUtil;
import com.honglu.future.widget.RiseNumberTextView;
import com.honglu.future.widget.kchart.entity.KlineCycle;
import com.honglu.future.widget.kchart.fragment.KLineFragment;
import com.honglu.future.widget.kchart.fragment.KMinuteFragment;
import com.honglu.future.widget.kchart.util.BakSourceInterface;
import com.honglu.future.widget.popupwind.KLinePopupWin;
import com.umeng.analytics.MobclickAgent;
import com.xulu.mpush.message.RequestMarketMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import butterknife.BindView;
import butterknife.OnClick;

import static com.honglu.future.util.ToastUtil.showToast;

/**
 * Created by zq on 2017/11/7.
 */

public class KLineMarketActivity extends BaseActivity<KLineMarketPresenter> implements KLineMarketContract.View, AccountContract.View, KLinePopupWin.OnPopItemClickListener, BillConfirmDialog.OnConfirmClickListener {
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
    @BindView(R.id.fragment_container)
    View fragment_container;
    @BindView(R.id.recycleView)
    RecyclerView recyclerView;
    @BindView(R.id.landRecycleViewCycle)
    RecyclerView landRecycleViewCycle;
    @BindView(R.id.v_lineView)
    View mLineView;
    @BindView(R.id.rl_numHeadLayout)
    RelativeLayout mHeadLaout;
    @BindView(R.id.tv_open_price_yugu)
    TextView mTvBzjYugu;
    @BindView(R.id.add_zixuan)
    CheckBox mAddZixuan;

    private String mExcode;
    private String mCode;
    private String mClosed;
    private String isClosed;
    private boolean mIsShowDetail;

    private BuildTransactionDialog mBuildTransactionDialog;
    private AccountPresenter mAccountPresenter;
    private AccountLoginDialog mAccountLoginDialog;
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
    private BillConfirmDialog billConfirmDialog;
    private int selectPos = 0;
    private View selectTextView = null, currentDKView = null;
    public static final String CODE_DK_SHOCK = "shock";
    public static final String CODE_DK_FUTURE = "future";

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

            Fragment mFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (mFragment instanceof KLineFragment) {
                KLineFragment kFragment = (KLineFragment) mFragment;
                kFragment.setLastKData(mRequestBean);
            } else if (mFragment instanceof KMinuteFragment) {
                //刷新分时图
                KMinuteFragment minFragment = (KMinuteFragment) mFragment;
                minFragment.setLastKData(mRequestBean);
            }

            if (mKLinePositionDialog != null
                    && mKLinePositionDialog.isShowing()) {
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
        mTvNewPrice.setTextFonts("fonts/DIN-Medium.ttf");
        mTvClosed.getBackground().mutate().setAlpha(100);
        mLineView.getBackground().mutate().setAlpha(60);
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
        View layoutBottom_land = findViewById(R.id.layoutBottom_land);
        if (isLand) {
            mLlTitleBar.setVisibility(View.GONE);
            mTvNameLand.setVisibility(View.VISIBLE);
            mTvRiseNumLand.setVisibility(View.VISIBLE);
            mTvRiseRadioLand.setVisibility(View.VISIBLE);
            mLlBottomTabs.setVisibility(View.GONE);
            mTvRiseNum.setVisibility(View.GONE);
            mTvRiseRadio.setVisibility(View.GONE);
            mIvFull.setVisibility(View.VISIBLE);
            if (recyclerView != null)
                recyclerView.setVisibility(View.GONE);
            if (layoutBottom_land != null)
                layoutBottom_land.setVisibility(View.VISIBLE);
            if (landRecycleViewCycle != null)
                landRecycleViewCycle.getAdapter().notifyDataSetChanged();

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mHeadLaout.getLayoutParams();
            params.topMargin = getResources().getDimensionPixelSize(R.dimen.dimen_20dp);
            mHeadLaout.setLayoutParams(params);

        } else {
            mLlTitleBar.setVisibility(View.VISIBLE);
            mTvNameLand.setVisibility(View.GONE);
            mTvRiseNumLand.setVisibility(View.GONE);
            mTvRiseRadioLand.setVisibility(View.GONE);
            mLlBottomTabs.setVisibility(View.VISIBLE);
            mTvRiseNum.setVisibility(View.VISIBLE);
            mTvRiseRadio.setVisibility(View.VISIBLE);
            mIvFull.setVisibility(View.GONE);
            if (recyclerView != null)
                recyclerView.setVisibility(View.VISIBLE);
            if (layoutBottom_land != null)
                layoutBottom_land.setVisibility(View.GONE);
            if (recyclerView != null)
                recyclerView.getAdapter().notifyDataSetChanged();

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mHeadLaout.getLayoutParams();
            params.topMargin = 0;
            mHeadLaout.setLayoutParams(params);
        }

    }


    private void initViewPager() {
        List<KlineCycle> klineCycles = new ArrayList<>();
        if (BakSourceInterface.specialSource.contains(mExcode)) {
            klineCycles.addAll(BakSourceInterface.specialklineCycleList);
            //如果不需要天玑线去掉下面代码
//            klineCycles.add(1, new KlineCycle(getResources().getString(R.string.str_dk_shock), CODE_DK_SHOCK));
//            klineCycles.add(2, new KlineCycle(getResources().getString(R.string.str_dk_future), CODE_DK_FUTURE));
        } else {
            klineCycles.addAll(BakSourceInterface.klineCycleList);
//            klineCycles.add(1, new KlineCycle(getResources().getString(R.string.str_dk_shock), CODE_DK_SHOCK));
//            klineCycles.add(2, new KlineCycle(getResources().getString(R.string.str_dk_future), CODE_DK_FUTURE));
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);//linearLayoutManager is already attached recyclerView不能共用linearLayoutManager
        if (recyclerView != null) {
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(new MyAdapter(klineCycles, MyAdapter.ITEM_NORMAL));
            recyclerView.setItemViewCacheSize(klineCycles.size());
        }

        if (landRecycleViewCycle != null) {
            LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(this);
            linearLayoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
            landRecycleViewCycle.setLayoutManager(linearLayoutManager3);
            landRecycleViewCycle.setAdapter(new MyAdapter(klineCycles, MyAdapter.ITEM_LAND));
            landRecycleViewCycle.setItemViewCacheSize(klineCycles.size());
        }

        if (selectPos == 0)
            replaceFragment(getCurrentMinuteFragment());
        else {
            MyAdapter adapter = (MyAdapter) recyclerView.getAdapter();
            String cycle = adapter.getItem(selectPos).getCode();
            replaceFragment(getKlineFragment(cycle));
        }
    }

    protected Fragment getCurrentMinuteFragment() {
        if (mBean == null) {
            return null;
        }

        KMinuteFragment fragment = new KMinuteFragment();
        Bundle bundle = new Bundle();
        bundle.putString("exCode", mExcode);
        bundle.putString("code", mCode);
        bundle.putDouble("closePrice", Double.parseDouble(mBean.getPreClosePrice()));
        fragment.setArguments(bundle);
        return fragment;
    }

    protected void replaceFragment(Fragment fragment) {
        if (isFinishing())
            return;
        if (mBean == null) {
            return;
        }

        if (fragment_container == null)
            return;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commitAllowingStateLoss();
    }

    protected KLineFragment getKlineFragment(String cycleCode) {
        if (mBean == null) {
            return null;
        }
        Bundle bundle = new Bundle();
        bundle.putString("exCode", mExcode);
        bundle.putString("code", mCode);
        bundle.putString("interval", cycleCode);

        KLineFragment fragment = KLineFragment.newInstance(bundle);
        return fragment;

    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        List<KlineCycle> goodsList;
        int type;
        private static final int ITEM_NORMAL = 0;//正常状态下 竖屏
        private static final int ITEM_LAND = 1;//横屏状态下

        private MyAdapter(List<KlineCycle> goodsList, int type) {
            this.goodsList = goodsList;
            this.type = type;
        }

        public KlineCycle getItem(int position) {
            if (goodsList != null && position < goodsList.size())
                return goodsList.get(position);
            return null;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View rootView = null;
            if (type == ITEM_LAND) {
                rootView = View.inflate(viewGroup.getContext(), R.layout.product_land_cycle_item, null);
            } else {
                rootView = View.inflate(viewGroup.getContext(), R.layout.product_klinecycle_item, null);
            }

            return new MyViewHolder(rootView);
        }

        public void clear() {
            if (null != goodsList) {
                goodsList.clear();
                notifyDataSetChanged();
            }

        }

        @Override
        public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
            if (myViewHolder.textView == null)
                return;
            if (i == selectPos) {
                selectTextView = myViewHolder.textView;
                selectTextView.setSelected(true);
            } else {
                myViewHolder.textView.setSelected(false);
            }
            final int position = i;
            myViewHolder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (DeviceUtils.isFastDoubleClick()) {
                        return;
                    }
                    String code = goodsList.get(position).getCode();
                    String name = goodsList.get(position).getName();
                    if (mBean != null) {
                        MobclickAgent.onEvent(mContext, mCode + "_click_" + code, mBean.getName() + "_" + name);
                    }
                    if (code.equalsIgnoreCase(BakSourceInterface.PARAM_KLINE_TIME) || code.equalsIgnoreCase(BakSourceInterface.PARAM_KLINE_TIME_WEIPAN)) {
                        //分时
                        replaceFragment(getCurrentMinuteFragment());
                    } else if (CODE_DK_FUTURE.equals(code) || CODE_DK_SHOCK.equals(code)) {
//                        //八卦线
                    } else {
                        //周期K线
                        replaceFragment(getKlineFragment(code));
                    }

                    if (selectTextView != null)
                        selectTextView.setSelected(false);
                    selectTextView = v;
                    selectPos = position;
                    if (selectTextView != null)
                        selectTextView.setSelected(true);
                    if (recyclerView != null)
                        recyclerView.scrollToPosition(position);
                    if (landRecycleViewCycle != null)
                        landRecycleViewCycle.scrollToPosition(position);
                }
            });

            myViewHolder.textView.setText(goodsList.get(i).getName());
        }

        @Override
        public int getItemCount() {
            return goodsList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView textView;

            private MyViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.item_name);
            }
        }
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

        initViewPager();
    }

    private void setBzjYugu(ProductListBean bean) {
//        if (App.getConfig().getLoginStatus()) {
//
//        } else {
        //未登录默认国富规则计算保证金
        double oneSlBZj = NumberUtil.multiply(new BigDecimal(bean.getShortMarginRatioByMoney()).doubleValue(), new BigDecimal(bean.getBidPrice1()).doubleValue()) * bean.getVolumeMultiple();
        String BZJStr = NumberUtil.moveLast0(NumberUtil.multiply(oneSlBZj, new BigDecimal(1).doubleValue()));
        mTvBzjYugu.setText(StringUtil.forNumber2(new BigDecimal(BZJStr).doubleValue()) + "元/手");
//        }
    }

    public void setTextValue(RealTimeBean.Data bean) {
        if (productListBean != null) {
//            if (App.getConfig().getLoginStatus()) {
//
//            } else {
            //未登录默认国富规则计算保证金
            double oneSlBZj = NumberUtil.multiply(new BigDecimal(productListBean.getShortMarginRatioByMoney()).doubleValue(), new BigDecimal(bean.getBidPrice1()).doubleValue()) * productListBean.getVolumeMultiple();
            String BZJStr = NumberUtil.moveLast0(NumberUtil.multiply(oneSlBZj, new BigDecimal(1).doubleValue()));
            mTvBzjYugu.setText(StringUtil.forNumber2(new BigDecimal(BZJStr).doubleValue()) + "元/手");
//            }
        }
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
        mPresenter.getHoldPositionList(SpUtil.getString(Constant.CACHE_TAG_UID), SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN), SpUtil.getString(Constant.COMPANY_TYPE));
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
        setBzjYugu(bean);
        isClosed = bean.getIsClosed();
        if (bean.getIsClosed().equals("2")) {
            mTvClosed.setVisibility(View.VISIBLE);
        } else {
            mTvClosed.setVisibility(View.GONE);
        }
        mProductRuleDialog = new ProductRuleDialog(this, bean);
    }

    @OnClick({R.id.iv_pull, R.id.iv_back, R.id.buy_up, R.id.buy_down, R.id.hold_position, R.id.iv_full_screen,
            R.id.iv_show_popup})
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
                if (DeviceUtils.isFastDoubleClick()) {
                    return;
                }
                if (!App.getConfig().getLoginStatus()) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                } else {
                    if (App.getConfig().getAccountLoginStatus()) {
                        if (productListBean != null) {
                            mBuildTransactionDialog = new BuildTransactionDialog(mContext, BuildTransactionDialog.TRADE_BUY_RISE, productListBean);
                            mBuildTransactionDialog.show();
                        } else {
                            showToast("暂未获取数据");
                        }
                    } else {
                        showAccountLoginDialog();
                    }
                }
                break;
            case R.id.buy_down:
                if (DeviceUtils.isFastDoubleClick()) {
                    return;
                }
                if (!App.getConfig().getLoginStatus()) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                } else {
                    if (App.getConfig().getAccountLoginStatus()) {
                        if (productListBean != null) {
                            mBuildTransactionDialog = new BuildTransactionDialog(mContext, BuildTransactionDialog.TRADE_BUY_DOWN, productListBean);
                            mBuildTransactionDialog.show();
                        } else {
                            showToast("暂未获取数据");
                        }
                    } else {
                        showAccountLoginDialog();
                    }
                }
                break;
            case R.id.hold_position:
                if (DeviceUtils.isFastDoubleClick()) {
                    return;
                }
                if (!App.getConfig().getLoginStatus()) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                } else {
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
                }
                break;
            case R.id.iv_full_screen:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case R.id.iv_show_popup:
                mKLinePopupWin.showPopupWind(v);
                break;
        }
    }

    private void showAccountLoginDialog() {
        mAccountLoginDialog = new AccountLoginDialog(mContext, mAccountPresenter);
        mAccountLoginDialog.show();
    }

    @Override
    public void loginSuccess(AccountBean bean) {
        if (billConfirmDialog != null && billConfirmDialog.isShowing()) {
            billConfirmDialog.dismiss();
        }
        mAccountLoginDialog.dismiss();
    }

    @Override
    public void showSettlementDialog(SettlementInfoBean bean) {
        billConfirmDialog = new BillConfirmDialog(mContext, bean);
        billConfirmDialog.setOnConfirmClickListenerr(this);
        billConfirmDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (isLandScape()) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    finish();
                }
                return true;
            default:
                break;
        }

        return super.onKeyDown(keyCode, event);
    }

    public boolean isLandScape() {
        return Configuration.ORIENTATION_LANDSCAPE == getResources()
                .getConfiguration().orientation;
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

    @Override
    public void onProductInfo() {
        if (DeviceUtils.isFastDoubleClick()) {
            return;
        }
        if (mProductRuleDialog != null) {
            mProductRuleDialog.show();
        } else {
            showToast("暂未获取数据");
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
