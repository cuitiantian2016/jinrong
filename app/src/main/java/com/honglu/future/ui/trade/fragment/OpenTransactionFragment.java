package com.honglu.future.ui.trade.fragment;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.config.Constant;
import com.honglu.future.dialog.AccountLoginDialog;
import com.honglu.future.dialog.AlertFragmentDialog;
import com.honglu.future.dialog.BillConfirmDialog;
import com.honglu.future.dialog.BuildTransactionDialog;
import com.honglu.future.dialog.TradeGuideDialog;
import com.honglu.future.dialog.TradeTipDialog;
import com.honglu.future.events.ReceiverMarketMessageEvent;
import com.honglu.future.events.RefreshUIEvent;
import com.honglu.future.events.UIBaseEvent;
import com.honglu.future.mpush.MPushUtil;
import com.honglu.future.ui.login.activity.LoginActivity;
import com.honglu.future.ui.main.contract.AccountContract;
import com.honglu.future.ui.main.presenter.AccountPresenter;
import com.honglu.future.ui.trade.adapter.OpenTransactionAdapter;
import com.honglu.future.ui.trade.bean.AccountBean;
import com.honglu.future.ui.trade.bean.ProductListBean;
import com.honglu.future.ui.trade.bean.SettlementInfoBean;
import com.honglu.future.ui.trade.contract.OpenTransactionContract;
import com.honglu.future.ui.trade.kchart.KLineMarketActivity;
import com.honglu.future.ui.trade.presenter.OpenTransactionPresenter;
import com.honglu.future.ui.usercenter.activity.UserAccountActivity;
import com.honglu.future.ui.usercenter.bean.AccountInfoBean;
import com.honglu.future.util.AESUtils;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.NumberUtil;
import com.honglu.future.util.NumberUtils;
import com.honglu.future.util.ProductViewHole4TradeContent;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.StringUtil;
import com.honglu.future.util.TimeUtil;
import com.honglu.future.util.ViewUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xulu.mpush.message.RequestMarketMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.honglu.future.util.ToastUtil.showToast;

/**
 * Created by zq on 2017/10/26.
 */

public class OpenTransactionFragment extends BaseFragment<OpenTransactionPresenter> implements OpenTransactionContract.View,
        AccountContract.View, OpenTransactionAdapter.OnRiseDownClickListener, BillConfirmDialog.OnConfirmClickListener {
    @BindView(R.id.rv_open_transaction_list_view)
    ListView mOpenTransactionListView;
    @BindView(R.id.srl_refreshView)
    SmartRefreshLayout mSmartRefreshLayout;
    private LinearLayout mTradeHeader;
    private ImageView mTradeTip;
    private OpenTransactionAdapter mOpenTransactionAdapter;

    private AccountPresenter mAccountPresenter;
    private AccountLoginDialog mAccountLoginDialog;
    private BuildTransactionDialog mBuildTransactionDialog;
    private String mToken;
    private String mSelectCode;
    private boolean mIsOneGuide; //是否开启交易引导
    private BillConfirmDialog billConfirmDialog;
    private NumberFormat nf;
    private List<ProductListBean> mProductList;
    private ProductViewHole4TradeContent productViewHole4TradeContent;

    @Override
    public void loginSuccess(AccountBean bean) {
        showToast("登录成功");
        if (billConfirmDialog != null && billConfirmDialog.isShowing()) {
            billConfirmDialog.dismiss();
        }
        mToken = bean.getToken();
        mAccountLoginDialog.dismiss();
        // SpUtil.putString(Constant.CACHE_ACCOUNT_TOKEN, bean.getToken());
        startRun();

    }

    @Override
    public void showSettlementDialog(SettlementInfoBean bean) {
        billConfirmDialog = new BillConfirmDialog(mContext, bean);
        billConfirmDialog.setOnConfirmClickListenerr(this);
        billConfirmDialog.show();
    }

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            getAccountBasicInfo();//每隔3秒刷一次
            mHandler.postDelayed(this, 3000);
        }
    };

    private void getAccountBasicInfo() {
        mPresenter.getAccountInfo(SpUtil.getString(Constant.CACHE_TAG_UID), SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN), "GUOFU");
    }

    /**
     * 开始刷新用户信息
     */
    public void startRun() {
        if (App.getConfig().getAccountLoginStatus()) {
            mHandler.removeCallbacks(mRunnable);
            mHandler.post(mRunnable);
        }
    }

    /**
     * 切换底部tab刷新产品列表
     */
    public void refreshProductList() {
        if (App.getConfig().getAccountLoginStatus()) {
            mPresenter.getProductList();
        }
    }

    public void stopRun() {
        mHandler.removeCallbacks(mRunnable);
    }


    /*******
     * 将事件交给事件派发controller处理
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ReceiverMarketMessageEvent event) {
        if (MPushUtil.CODES_TRADE_HOME == null || !MPushUtil.CODES_TRADE_HOME.equals(MPushUtil.requestCodes) || isHidden()) {
            return;
        }
        List<ProductListBean> data = mProductList;
        int index = -1;
        RequestMarketMessage marketMessage = event.marketMessage;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getInstrumentId().equals(marketMessage.getInstrumentID())) {
                index = i;
                break;
            }
        }
        ProductListBean productListBean = null;
        if (index >= 0) {
            productListBean = data.get(index);
            productListBean.setAskPrice1(marketMessage.getAskPrice1());
            productListBean.setBidPrice1(marketMessage.getBidPrice1());
            productListBean.setTradeVolume(marketMessage.getVolume());
            data.set(index, productListBean);
            int visiblePosition = mOpenTransactionListView.getFirstVisiblePosition();
//                Log.e(TAG,optional.getName()+"=====");
            int visibleLastPosition = mOpenTransactionListView.getLastVisiblePosition();

            //只有当要更新的view在可见的位置时才更新，不可见时，跳过不更新
            int itemIndex = mOpenTransactionAdapter.getProductIndex(marketMessage.getInstrumentID()) + mOpenTransactionListView.getHeaderViewsCount();
            if (itemIndex - visiblePosition >= 0 && itemIndex < visibleLastPosition + mOpenTransactionListView.getHeaderViewsCount()) {
                //得到要更新的item的view
                View view = mOpenTransactionListView.getChildAt(itemIndex - visiblePosition);
//                    //调用adapter更新界面
//                    dateAdapter.updateView(view, itemIndex);
                TextView tv_buyup_rate = (TextView) view.findViewById(R.id.tv_rise);
                TextView tv_buydown_rate = (TextView) view.findViewById(R.id.tv_down);
                TextView tv_vol = (TextView) view.findViewById(R.id.tv_num);
                tv_buyup_rate.setText(marketMessage.getAskPrice1());
                tv_buydown_rate.setText(marketMessage.getBidPrice1());
                tv_vol.setText(marketMessage.getVolume());
            }
        }

        if (mBuildTransactionDialog != null
                && mBuildTransactionDialog.isShowing()
                && !TextUtils.isEmpty(mSelectCode) && mSelectCode.equals(marketMessage.getInstrumentID())) {
            mBuildTransactionDialog.pushRefresh(productListBean);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            stopRun();
            MPushUtil.pauseRequest();
        } else {
            if (!App.getConfig().getAccountLoginStatus()) {
                stopRun();
            } else {
                startRun();
                mPresenter.getProductList();
            }
            if (!TextUtils.isEmpty(MPushUtil.CODES_TRADE_HOME)) {
                MPushUtil.requestMarket(MPushUtil.CODES_TRADE_HOME);
            }

            if (TextUtils.isEmpty(SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN)) || TextUtils.isEmpty(SpUtil.getString(Constant.CACHE_TAG_UID))) {
                return;
            }
//            mPresenter.querySettlementInfo(SpUtil.getString(Constant.CACHE_TAG_UID), SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN), "GUOFU");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN)) || TextUtils.isEmpty(SpUtil.getString(Constant.CACHE_TAG_UID))) {
            return;
        }
        //mPresenter.querySettlementInfo(SpUtil.getString(Constant.CACHE_TAG_UID), SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN), "GUOFU");
    }

    @Override
    public void finishRefreshView() {
        if (mSmartRefreshLayout != null && mSmartRefreshLayout.isRefreshing()) {
            mSmartRefreshLayout.finishRefresh();
        }
    }

    @Override
    public void getProductListSuccess(List<ProductListBean> bean) {
        if (bean == null || bean.size() <= 0) {
            return;
        }
        mProductList = bean;

        if (!TextUtils.isEmpty(MPushUtil.CODES_TRADE_HOME)) {
            MPushUtil.requestMarket(MPushUtil.CODES_TRADE_HOME);
        }
        if (mOpenTransactionAdapter.getProductViewHole4TradeContent() == null) {
            productViewHole4TradeContent = new ProductViewHole4TradeContent(mContext, getProductsCodes());
            mOpenTransactionAdapter.setProductViewHole4TradeContent(productViewHole4TradeContent);
        }
        mOpenTransactionAdapter.setItems(bean);
        if (!mIsOneGuide) {
            mIsOneGuide = true;
            new TradeGuideDialog(getActivity()).show();
        }
    }

    String productCodes = "";

    private String getProductsCodes() {
        if (!TextUtils.isEmpty(productCodes)) {
            return productCodes;
        }
//        List<ProductObj> objects = dateAdapter.getItems();
        if (mProductList == null) {
            return productCodes;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (ProductListBean productObj : mProductList) {
            stringBuffer.append(productObj.getExcode());
            stringBuffer.append("|");
            stringBuffer.append(productObj.getInstrumentId());
            stringBuffer.append(",");
        }
        String temp = stringBuffer.toString();
        if (!TextUtils.isEmpty(temp)) {
            productCodes = temp.substring(0, temp.length() - 1);
        }
        return productCodes;
    }

    private TextView mDangerChance;
    private TextView mRightsInterests;
    private TextView mMoney;
    private TextView mProfitLoss;

    @Override
    public void getAccountInfoSuccess(AccountInfoBean bean) {
        mDangerChance.setText(bean.getCapitalProportion());
        mRightsInterests.setText(StringUtil.forNumber(new BigDecimal(bean.getRightsInterests()).doubleValue()));
        mMoney.setText(StringUtil.forNumber(new BigDecimal(bean.getAvailable()).doubleValue()));

        if (Integer.parseInt(bean.getPositionProfit()) > 0) {
            mProfitLoss.setTextColor(mContext.getResources().getColor(R.color.color_opt_gt));
            mProfitLoss.setText("+" + bean.getPositionProfit());
        } else if (Integer.parseInt(bean.getPositionProfit()) < 0) {
            mProfitLoss.setText(bean.getPositionProfit());
            mProfitLoss.setTextColor(mContext.getResources().getColor(R.color.color_opt_lt));
        } else {
            mProfitLoss.setText(bean.getPositionProfit());
            mProfitLoss.setTextColor(mContext.getResources().getColor(R.color.color_333333));
        }
        try {
            Number danger = nf.parse(bean.getCapitalProportion());
            if (danger.doubleValue() >= 0.8) {
                if (!TimeUtil.isToday(SpUtil.getString(Constant.CACHE_ACCOUNT_DANGER_ALERT_DATE))) {
                    SpUtil.putBoolean(Constant.CACHE_ACCOUNT_DANGER_HAS_ALERT, false);
                    if (!SpUtil.getBoolean(Constant.CACHE_ACCOUNT_DANGER_HAS_ALERT)) {
                        SpUtil.putString(Constant.CACHE_ACCOUNT_DANGER_ALERT_DATE, TimeUtil.getCurrentDay2());
                        SpUtil.putBoolean(Constant.CACHE_ACCOUNT_DANGER_HAS_ALERT, true);
                        new AlertFragmentDialog.Builder(mActivity)
                                .setLeftBtnText("我知道了").setContent("风险率超过"+bean.getCapitalProportion()+","+"风险较高,请及时补充资金避免强行平仓造成损失").setTitle("资金风险高")
                                .create(AlertFragmentDialog.Builder.TYPE_NORMAL);
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    /***********
     * eventBus 监听
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UIBaseEvent event) {
        if (event instanceof RefreshUIEvent) {
            int code = ((RefreshUIEvent) event).getType();
            if (code == UIBaseEvent.EVENT_ACCOUNT_LOGOUT) {//安全退出期货账户
                if (!App.getConfig().getAccountLoginStatus()) {
                    stopRun();
                    mDangerChance.setText("--");
                    mRightsInterests.setText("--");
                    mMoney.setText("--");
                    mProfitLoss.setText("--");
                }
            }
        }
    }

    @Override
    public void showLoading(String content) {
        if (!TextUtils.isEmpty(content)) {
            App.loadingContent(mActivity, content);
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


    @Override
    public int getLayoutId() {
        return R.layout.fragment_open_transaction;
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
        mIsOneGuide = SpUtil.getBoolean(Constant.GUIDE_OPEN_TRANSACTION, false);
        nf = NumberFormat.getPercentInstance();
        initView();
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
//        mOpenTransactionListView.setLayoutManager(new LinearLayoutManager(mContext));
//        mOpenTransactionListView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mOpenTransactionAdapter = new OpenTransactionAdapter(mContext, 0, new ArrayList<ProductListBean>());
        View headView = LayoutInflater.from(mActivity).inflate(R.layout.item_trade_list_header, null);

        mTradeHeader = (LinearLayout) headView.findViewById(R.id.ll_trade_header);
        LinearLayout.LayoutParams mTradeHeaderParams = (LinearLayout.LayoutParams) mTradeHeader.getLayoutParams();
        mTradeHeaderParams.bottomMargin = getActivity().getResources().getDimensionPixelSize(R.dimen.dimen_7dp);
        mTradeHeader.setLayoutParams(mTradeHeaderParams);

        //剩余平分
        LinearLayout rootLayoutLeft = (LinearLayout) headView.findViewById(R.id.rootLayout_left);
        int screenWidth = ViewUtil.getScreenWidth(getActivity());
        int pixelSize_60 = getResources().getDimensionPixelSize(R.dimen.dimen_60dp);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rootLayoutLeft.getLayoutParams();
        params.width = (screenWidth - pixelSize_60) / 2;
        rootLayoutLeft.setLayoutParams(params);

        mDangerChance = (TextView) headView.findViewById(R.id.tv_danger_chance);
        mRightsInterests = (TextView) headView.findViewById(R.id.tv_rights_interests);
        mMoney = (TextView) headView.findViewById(R.id.tv_money);
        mProfitLoss = (TextView) headView.findViewById(R.id.tv_profit_loss);
        mTradeTip = (ImageView) headView.findViewById(R.id.iv_trade_tip);
        mOpenTransactionListView.addHeaderView(headView);
        mOpenTransactionListView.setAdapter(mOpenTransactionAdapter);
        setListener();
    }

    private void setListener() {
        mOpenTransactionAdapter.setOnRiseDownClickListener(this);

        mTradeHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DeviceUtils.isFastDoubleClick()) {
                    return;
                }
                if (!App.getConfig().getLoginStatus()) {
                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    startActivity(intent);
                } else {
                    if (!App.getConfig().getAccountLoginStatus()) {
                        mAccountLoginDialog = new AccountLoginDialog(mContext, mAccountPresenter);
                        mAccountLoginDialog.show();
                    } else {
                        startActivity(UserAccountActivity.class);
                    }
                }
            }
        });
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                MPushUtil.pauseRequest();
                mPresenter.getProductList();
            }
        });
        mTradeTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DeviceUtils.isFastDoubleClick()) {
                    return;
                }
                TradeTipDialog tradeTipDialog = new TradeTipDialog(mContext, R.layout.layout_trade_tip_pop_window);
                tradeTipDialog.show();
            }
        });
    }

    private void initData() {
        mPresenter.getProductList();
    }

    @Override
    public void onRiseClick(View view, ProductListBean bean) {
        if(DeviceUtils.isFastDoubleClick()){
            return;
        }
        if (!App.getConfig().getLoginStatus()) {
            Intent intent = new Intent(mContext, LoginActivity.class);
            mContext.startActivity(intent);
        } else {
            if (App.getConfig().getAccountLoginStatus()) {
                if (bean != null) {
                    mSelectCode = bean.getInstrumentId();
                    mBuildTransactionDialog = new BuildTransactionDialog(mContext, BuildTransactionDialog.TRADE_BUY_RISE, bean);
                    mBuildTransactionDialog.show();
                } else {
                    showToast("暂未获取数据");
                }
            } else {
                mAccountLoginDialog = new AccountLoginDialog(mActivity, mAccountPresenter);
                mAccountLoginDialog.show();
            }
        }
    }

    @Override
    public void onDownClick(View view, ProductListBean bean) {
        if(DeviceUtils.isFastDoubleClick()){
            return;
        }
        if (!App.getConfig().getLoginStatus()) {
            Intent intent = new Intent(mContext, LoginActivity.class);
            mContext.startActivity(intent);
        } else {
            if (App.getConfig().getAccountLoginStatus()) {
                if (bean != null) {
                    mSelectCode = bean.getInstrumentId();
                    mBuildTransactionDialog = new BuildTransactionDialog(mContext, BuildTransactionDialog.TRADE_BUY_DOWN, bean);
                    mBuildTransactionDialog.show();
                } else {
                    showToast("暂未获取数据");
                }
            } else {
                mAccountLoginDialog = new AccountLoginDialog(mActivity, mAccountPresenter);
                mAccountLoginDialog.show();
            }
        }
    }

    @Override
    public void onItemClick(ProductListBean bean) {
        Intent intent = new Intent(mActivity, KLineMarketActivity.class);
        intent.putExtra("excode", bean.getExcode());
        intent.putExtra("code", bean.getInstrumentId());
        intent.putExtra("isClosed", bean.getIsClosed());
        startActivity(intent);
    }

    @Override
    public void onConfirmClick() {
        if (DeviceUtils.isFastDoubleClick()) {
            return;
        }
        mAccountPresenter.settlementConfirm(SpUtil.getString(Constant.CACHE_TAG_UID));
    }
}
