package com.honglu.future.ui.trade.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.bean.MaidianBean;
import com.honglu.future.config.Constant;
import com.honglu.future.dialog.AccountLoginDialog;
import com.honglu.future.dialog.BillConfirmDialog;
import com.honglu.future.dialog.PositionDialog;
import com.honglu.future.dialog.TradeTipDialog;
import com.honglu.future.dialog.closetransaction.CloseTransactionDialog;
import com.honglu.future.events.ChangeTabEvent;
import com.honglu.future.events.ReceiverMarketMessageEvent;
import com.honglu.future.events.RefreshUIEvent;
import com.honglu.future.events.UIBaseEvent;
import com.honglu.future.mpush.MPushUtil;
import com.honglu.future.ui.login.activity.LoginActivity;
import com.honglu.future.ui.main.contract.AccountContract;
import com.honglu.future.ui.main.presenter.AccountPresenter;
import com.honglu.future.ui.trade.adapter.PositionAdapter;
import com.honglu.future.ui.trade.bean.AccountBean;
import com.honglu.future.ui.trade.bean.HoldDetailBean;
import com.honglu.future.ui.trade.bean.HoldPositionBean;
import com.honglu.future.ui.trade.bean.ProductListBean;
import com.honglu.future.ui.trade.bean.SettlementInfoBean;
import com.honglu.future.ui.trade.contract.PositionContract;
import com.honglu.future.ui.trade.presenter.PositionPresenter;
import com.honglu.future.ui.usercenter.activity.UserAccountActivity;
import com.honglu.future.ui.usercenter.bean.AccountInfoBean;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.StringUtil;
import com.honglu.future.util.ToastUtil;
import com.honglu.future.util.TradeUtil;
import com.honglu.future.util.ViewUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xulu.mpush.message.RequestMarketMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.honglu.future.util.ToastUtil.showToast;

/**
 * Created by zq on 2017/10/26.
 */
public class PositionFragment extends BaseFragment<PositionPresenter> implements PositionContract.View, AccountContract.View,
        PositionAdapter.OnShowPopupClickListener, BillConfirmDialog.OnConfirmClickListener {
    @BindView(R.id.lv_listView)
    ListView mListView;
    @BindView(R.id.srl_refreshView)
    SmartRefreshLayout mRefreshView;
    @BindView(R.id.tv_remarksEmpty)
    TextView mRemarksEmpty;
    private View mFooterEmptyView;
    private TextView mDangerChance;
    private TextView mRightsInterests;
    private TextView mMoney;
    private TextView mProfitLoss;

    private PositionAdapter mAdapter;
    private AccountPresenter mAccountPresenter;
    private AccountLoginDialog mAccountLoginDialog;
    private PositionDialog mPositionDialog;
    private CloseTransactionDialog mCloseDialog;
    private BillConfirmDialog billConfirmDialog;
    private HoldPositionBean mHoldPositionBean;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            getAccountInfo();//每隔3秒刷一次
            mHandler.postDelayed(this, 3000);
        }
    };

    public String mRedirect;
    private int mPosition;
    private Map<String, String> productMap;
    private Map<String, String> priceMap;

    private Runnable mPositionRunnable = new Runnable() {
        @Override
        public void run() {
            getPositionList();
        }
    };

    private List<HoldPositionBean> mList;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_position;
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
    public void initPresenter() {
        mPresenter.init(this);
        mAccountPresenter = new AccountPresenter();
        mAccountPresenter.init(this);
    }

    public void setEmptyView(boolean isEmpty) {
        if (isEmpty) {
            mRemarksEmpty.setVisibility(View.VISIBLE);
            mFooterEmptyView.setVisibility(View.VISIBLE);
            if (mListView.getFooterViewsCount() <= 0 && mFooterEmptyView != null) {
                mListView.addFooterView(mFooterEmptyView);
            }
        } else {
            mRemarksEmpty.setVisibility(View.GONE);
            mFooterEmptyView.setVisibility(View.GONE);
            if (mListView.getFooterViewsCount() > 0 && mFooterEmptyView != null) {
                mListView.removeFooterView(mFooterEmptyView);
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (App.getConfig().getLoginStatus()) {
                if (!App.getConfig().getAccountLoginStatus()) {
                    if (isVisible()) {
                        stopRun();
                        mAccountLoginDialog = AccountLoginDialog.getInstance(mActivity, mAccountPresenter);
                        mAccountLoginDialog.show();
                    }
                } else {
                    if (isVisible()) {

                        mPresenter.getProductList();
                    }
                }
            } else {
                EventBus.getDefault().post(new ChangeTabEvent(0));
                startActivity(LoginActivity.class);
            }

            if (!TextUtils.isEmpty(MPushUtil.CODES_TRADE_HOME)) {
                MPushUtil.requestMarket(MPushUtil.CODES_TRADE_HOME);
            }
        } else {
            MPushUtil.pauseRequest();
            if (mPositionDialog != null && mPositionDialog.isShowing()) {
                mPositionDialog.dismiss();
            }

            if (mCloseDialog != null && mCloseDialog.isShowing()) {
                mCloseDialog.dismiss();
            }
            stopRun();
        }
    }

    public void stopRun() {
        mHandler.removeCallbacks(mRunnable);
        mHandler.removeCallbacks(mPositionRunnable);
    }

    /*******
     * 将事件交给事件派发controller处理
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ReceiverMarketMessageEvent event) {
        if (MPushUtil.CODES_TRADE_HOME == null || !MPushUtil.CODES_TRADE_HOME.equals(MPushUtil.requestCodes) || isHidden()
                || mList == null) {
            return;
        }
        List<HoldPositionBean> data = mList;
        int index = -1;
        RequestMarketMessage marketMessage = event.marketMessage;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getInstrumentId().equals(marketMessage.getInstrumentID())) {
                index = i;
                break;
            }
        }
        HoldPositionBean productListBean = null;
        if (index >= 0) {
            productListBean = data.get(index);
            data.set(index, productListBean);
            int visiblePosition = mListView.getFirstVisiblePosition();
//                Log.e(TAG,optional.getName()+"=====");
            int visibleLastPosition = mListView.getLastVisiblePosition();

            //只有当要更新的view在可见的位置时才更新，不可见时，跳过不更新
            int itemIndex = mAdapter.getProductIndex(marketMessage.getInstrumentID()) + mListView.getHeaderViewsCount();
            if (itemIndex - visiblePosition >= 0 && itemIndex < visibleLastPosition + mListView.getHeaderViewsCount()) {
                //得到要更新的item的view
                View view = mListView.getChildAt(itemIndex - visiblePosition);
//                    //调用adapter更新界面
//                    dateAdapter.updateView(view, itemIndex);
                TextView tv_new_price = (TextView) view.findViewById(R.id.tv_new_price);
                TextView tv_profit_loss = (TextView) view.findViewById(R.id.tv_profit_loss);
                tv_new_price.setText(marketMessage.getLastPrice());
                if (!TextUtils.isEmpty(productMap.get(data.get(index).getInstrumentId()))) {
                    String ccYk = String.valueOf(getCloseProfitLoss(Double.parseDouble(marketMessage.getLastPrice()), productListBean.getPosition(), productMap.get(data.get(index).getInstrumentId()), productListBean));
                    tv_profit_loss.setText(ccYk);

                if (Double.parseDouble(ccYk) > 0) {
                    tv_profit_loss.setTextColor(mContext.getResources().getColor(R.color.color_opt_gt));
                    tv_profit_loss.setText("+" + ccYk);
                } else if (Double.parseDouble(ccYk) < 0) {
                    tv_profit_loss.setTextColor(mContext.getResources().getColor(R.color.color_opt_lt));
                    tv_profit_loss.setText(ccYk);
                } else {
                    tv_profit_loss.setTextColor(mContext.getResources().getColor(R.color.color_333333));
                    tv_profit_loss.setText(ccYk);
                }
                }
            }
        }

    }

    @Override
    public void loadData() {
        EventBus.getDefault().register(this);
        mList = new ArrayList<>();
        mPositionDialog = new PositionDialog(mContext);
        mCloseDialog = new CloseTransactionDialog(getActivity());
        mAdapter = new PositionAdapter(PositionFragment.this);
        View headView = LayoutInflater.from(mContext).inflate(R.layout.layout_trade_position_list_header, null);
        LinearLayout tradeHeader = (LinearLayout) headView.findViewById(R.id.ll_trade_header);
        TextView tvTip = (TextView) headView.findViewById(R.id.tv_tip);
        ImageView ivTip = (ImageView) headView.findViewById(R.id.iv_trade_tip);
        TextView tvNewPrice = (TextView) headView.findViewById(R.id.tv_new_price);
        tvNewPrice.setText("最新价");

        //剩余平分
        LinearLayout rootLayoutLeft = (LinearLayout) headView.findViewById(R.id.rootLayout_left);
        int screenWidth = ViewUtil.getScreenWidth(getActivity());
        int pixelSize_60 = getResources().getDimensionPixelSize(R.dimen.dimen_80dp);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rootLayoutLeft.getLayoutParams();
        params.width = (screenWidth - pixelSize_60) / 2;
        rootLayoutLeft.setLayoutParams(params);

        mDangerChance = (TextView) headView.findViewById(R.id.tv_danger_chance);
        mRightsInterests = (TextView) headView.findViewById(R.id.tv_rights_interests);
        mMoney = (TextView) headView.findViewById(R.id.tv_money);
        mProfitLoss = (TextView) headView.findViewById(R.id.tv_profit_loss);
        mFooterEmptyView = LayoutInflater.from(mContext).inflate(R.layout.layout_trade_position_emptyview, null);
        mListView.addHeaderView(headView);
        mListView.addFooterView(mFooterEmptyView);
        mListView.setAdapter(mAdapter);
        setEmptyView(true);

        mAdapter.setOnShowPopupClickListener(this);
        mPositionDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                startRun();
            }
        });
        mCloseDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                startRun();
            }
        });
        mRefreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mRefreshView.finishRefresh();
            }

        });

        tvTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TradeTipDialog tipDialog = new TradeTipDialog(mContext, R.layout.layout_trade_heyue_tip);
                tipDialog.show();
            }
        });

        ivTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TradeTipDialog tipDialog = new TradeTipDialog(mContext, R.layout.layout_trade_tip_pop_window);
                tipDialog.show();
            }
        });

        tradeHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                MaidianBean maidianBean = new MaidianBean();
                maidianBean.page_name = "点击账户管理的数据";
                maidianBean.even_name = "点击账户管理的数据";
                MaidianBean.Data data = new MaidianBean.Data();
                data.buriedName = "点击账户管理的数据";
                data.buriedRemark = "点击账户管理的数据";
                data.key = "qihuo_account_info";
                maidianBean.data = data;
                MaidianBean.postMaiDian(maidianBean);
            }
        });
    }

    private void getPositionList() {
        mPresenter.getHoldPositionList(SpUtil.getString(Constant.CACHE_TAG_UID), SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN), SpUtil.getString(Constant.COMPANY_TYPE));
    }

    private void getAccountInfo() {
        mPresenter.getAccountInfo(SpUtil.getString(Constant.CACHE_TAG_UID), SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN), SpUtil.getString(Constant.COMPANY_TYPE));
    }

    /**
     * 平仓盈亏
     *
     * @param tradeNum 手数
     * @return
     */
    private double getCloseProfitLoss(double price, int tradeNum, String volumeMultiple, HoldPositionBean holdPositionBean) {
        String holdAvgPrice = holdPositionBean.getOpenAvgPrice();
        int type = holdPositionBean.getType();
        try {
            return TradeUtil.getCloseProfitLoss(type, holdAvgPrice, "1", Integer.parseInt(volumeMultiple), price, tradeNum);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void loginSuccess(AccountBean bean) {
        if (billConfirmDialog != null && billConfirmDialog.isShowing()) {
            billConfirmDialog.dismiss();
        }
        mAccountLoginDialog.dismiss();
        startRun();
        if (!TextUtils.isEmpty(mRedirect)) {
            ARouter.getInstance().build(Uri.parse(mRedirect)).navigation(getActivity());
            mRedirect = null;
        }
    }

    @Override
    public void showSettlementDialog(SettlementInfoBean bean) {
        billConfirmDialog = new BillConfirmDialog(mContext, bean);
        billConfirmDialog.setOnConfirmClickListenerr(this);
        billConfirmDialog.show();
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

    public void requestMarketData(){
        if (!TextUtils.isEmpty(MPushUtil.CODES_TRADE_HOME)) {
            MPushUtil.requestMarket(MPushUtil.CODES_TRADE_HOME);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(MPushUtil.CODES_TRADE_HOME)) {
            MPushUtil.requestMarket(MPushUtil.CODES_TRADE_HOME);
        }
    }

    public void stopAccountRun() {
        mHandler.removeCallbacks(mRunnable);
    }


    @Override
    public void getHoldPositionListSuccess(List<HoldPositionBean> list) {
        mList.clear();
        if(priceMap == null){
            mList = list;
        } else {
            for (HoldPositionBean bean : list) {
                bean.setCcProfit(String.valueOf(getCloseProfitLoss(Double.parseDouble(priceMap.get(bean.getInstrumentId())), bean.getPosition(), productMap.get(bean.getInstrumentId()), bean)));
                bean.setLastPrice(priceMap.get(bean.getInstrumentId()));
                mList.add(bean);
            }
        }
        if (mPositionDialog != null && mPositionDialog.isShowing() && list != null && list.size() > 0) {
            mPositionDialog.setCjyk(list.get(mPosition).getTodayProfit());
        }

        mAdapter.notifyDataChanged(false, list);
        mRefreshView.finishRefresh();
    }

    @Override
    public void getHoldDetailListSuccess(List<HoldDetailBean> list) {
        startRun();
        mPositionDialog.showPopupWind(mHoldPositionBean, list);
    }


    //产品详情
    @Override
    public void getProductDetailSuccess(ProductListBean productListBean) {
        mCloseDialog.showDialog(mHoldPositionBean, productListBean);
    }

    @Override
    public void getProductListSuccess(List<ProductListBean> bean) {
        productMap = new HashMap<>();
        priceMap = new HashMap<>();
        for (int i = 0; i < bean.size(); i++) {
            productMap.put(bean.get(i).getInstrumentId(), String.valueOf(bean.get(i).getVolumeMultiple()));
            priceMap.put(bean.get(i).getInstrumentId(),bean.get(i).getLastPrice());
        }
        if (!TextUtils.isEmpty(MPushUtil.CODES_TRADE_HOME)) {
            MPushUtil.requestMarket(MPushUtil.CODES_TRADE_HOME);
        }
        startRun();
    }


    @Override
    public void closeOrderSuccess() {
        showToast("平仓成功");
        mCloseDialog.dismiss();
        getPositionList();
    }

    @Override
    public void getAccountInfoSuccess(AccountInfoBean bean) {
        mDangerChance.setText(bean.getCapitalProportion());
        mRightsInterests.setText(StringUtil.forNumber(new BigDecimal(bean.getRightsInterests()).doubleValue()));
        mMoney.setText(StringUtil.forNumber(new BigDecimal(bean.getAvailable()).doubleValue()));

        if (Integer.parseInt(bean.getPositionProfit()) > 0) {
            mProfitLoss.setTextColor(mContext.getResources().getColor(R.color.color_opt_gt));
            mProfitLoss.setText("+" + bean.getPositionProfit());
        } else if (Integer.parseInt(bean.getPositionProfit()) < 0) {
            mProfitLoss.setTextColor(mContext.getResources().getColor(R.color.color_opt_lt));
            mProfitLoss.setText(bean.getPositionProfit());
        } else {
            mProfitLoss.setTextColor(mContext.getResources().getColor(R.color.color_333333));
            mProfitLoss.setText(bean.getPositionProfit());
        }
        mHandler.postDelayed(mPositionRunnable, 1000);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        if (mCloseDialog != null) {
            mCloseDialog.onDestroy();
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
                    mList = new ArrayList<>();
                    mAdapter.notifyDataChanged(false, mList);
                    setEmptyView(true);
                }
            }
        }
    }

    @Override
    public void onConfirmClick() {
        mAccountPresenter.settlementConfirm(SpUtil.getString(Constant.CACHE_TAG_UID));
    }

    @Override
    public void onClosePositionClick(View view, HoldPositionBean bean, int position) {
        if (DeviceUtils.isFastDoubleClick()) {
            return;
        }
        mPosition = position;
        if (bean != null && !TextUtils.isEmpty(bean.getInstrumentId())) {
            this.mHoldPositionBean = bean;
            mPresenter.getProductDetail(bean.getInstrumentId());
        }
        MaidianBean maidianBean = new MaidianBean();
        maidianBean.page_name = "用户在持仓页面点击快速平仓的数据";
        maidianBean.even_name = "用户在持仓页面点击快速平仓的数据";
        MaidianBean.Data maidianData = new MaidianBean.Data();
        maidianData.buriedName = "用户在持仓页面点击快速平仓的数据";
        maidianData.buriedRemark = "用户在持仓页面点击快速平仓的数据";
        maidianData.key = "qihuo_hold_closeInfo";
        maidianBean.data = maidianData;
        MaidianBean.postMaiDian(maidianBean);
    }

    @Override
    public void onHoldDetailClick(View view, HoldPositionBean bean, int position) {
        mPosition = position;
        stopAccountRun();
        mHoldPositionBean = bean;
        mPresenter.getHoldPositionDetail(bean.getInstrumentId(),
                String.valueOf(bean.getType()),
                String.valueOf(bean.getTodayPosition()),
                SpUtil.getString(Constant.CACHE_TAG_UID),
                SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN));
    }
}
