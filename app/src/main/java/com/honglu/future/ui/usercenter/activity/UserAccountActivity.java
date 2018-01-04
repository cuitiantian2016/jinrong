package com.honglu.future.ui.usercenter.activity;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.config.ConfigUtil;
import com.honglu.future.config.Constant;
import com.honglu.future.dialog.AlertFragmentDialog;
import com.honglu.future.dialog.TradeTipDialog;
import com.honglu.future.events.ChangeTabMainEvent;
import com.honglu.future.events.RefreshUIEvent;
import com.honglu.future.events.UIBaseEvent;
import com.honglu.future.ui.main.FragmentFactory;
import com.honglu.future.ui.main.activity.WebViewActivity;
import com.honglu.future.ui.recharge.activity.InAndOutGoldActivity;
import com.honglu.future.ui.trade.activity.TradeRecordActivity;
import com.honglu.future.ui.trade.historybill.HistoryBillActivity;
import com.honglu.future.ui.usercenter.bean.AccountInfoBean;
import com.honglu.future.ui.usercenter.contract.UserAccountContract;
import com.honglu.future.ui.usercenter.presenter.UserAccountPresenter;
import com.honglu.future.util.ConvertUtil;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.NumberUtils;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.StringUtil;
import com.honglu.future.util.ViewUtil;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.OnClick;

import static com.honglu.future.util.ToastUtil.showToast;

/**
 * 我的账户
 * Created by zhuaibing on 2017/10/31
 */

public class UserAccountActivity extends BaseActivity<UserAccountPresenter> implements UserAccountContract.View, View.OnClickListener {

    @BindView(R.id.tv_danger_chance)
    TextView mDangerChance;
    @BindView(R.id.tv_money)
    TextView mMoney;
    @BindView(R.id.tv_rights_interests)
    TextView mRightsInterests;
    @BindView(R.id.tv_profit_loss)
    TextView mProfitLoss;
    @BindView(R.id.tv_position_profit_loss)
    TextView mPositionProfitLoss;
    @BindView(R.id.tv_service_charge)
    TextView mServiceCharge;
    @BindView(R.id.tv_withdrawals)
    TextView mWithdrawals;
    @BindView(R.id.tv_recharge)
    TextView mRecharge;
    @BindView(R.id.tv_position)
    TextView mPosition;
    @BindView(R.id.tv_trade_details)
    TextView mTradeDetails;
    @BindView(R.id.tv_bill_details)
    TextView mBillDetails;
    @BindView(R.id.tv_bond_query)
    TextView mBondQuery;
    @BindView(R.id.tv_history_bill)
    TextView mHistoryBill;
    @BindView(R.id.tv_account_manage)
    TextView mAccountManage;
    @BindView(R.id.tv_signout)
    TextView mSignout;

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            getAccountBasicInfo();//每隔3秒刷一次
            mHandler.postDelayed(this, 3000);
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_account;
    }

    @Override
    public void loadData() {
        mTitle.setTitle(true, R.mipmap.ic_back_black, R.color.white, "账户管理");
//        mTitle.setRightTitle(R.mipmap.ic_trade_tip, this);

        startRun();
    }

    @OnClick({R.id.tv_withdrawals, R.id.tv_recharge, R.id.tv_right, R.id.tv_history_bill, R.id.tv_position,
            R.id.tv_trade_details, R.id.tv_bill_details, R.id.tv_bond_query,R.id.tv_account_manage,R.id.tv_signout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_withdrawals:
                clickTab("wode_myaccount_tixian", "我的_我的账户_提现");
                InAndOutGoldActivity.startInAndOutGoldActivity(this, 1);
                break;
            case R.id.tv_recharge:
                clickTab("wode_myaccount_chongzhi", "我的_我的账户_充值");
                InAndOutGoldActivity.startInAndOutGoldActivity(this, 0);
                break;
            case R.id.tv_right:
                TradeTipDialog tipDialog = new TradeTipDialog(mContext, R.layout.layout_my_account_tip);
                tipDialog.show();
                break;
            case R.id.tv_history_bill:
                clickTab("wode_account_lishizhangdan", "我的_历史账单查询");
                startActivity(HistoryBillActivity.class);
                break;
            case R.id.tv_position:
                clickTab("wode_account_chicang", "我的_我的持仓");
                EventBus.getDefault().post(new ChangeTabMainEvent(FragmentFactory.FragmentStatus.Trade));
                finish();
                break;
            case R.id.tv_trade_details:
                clickTab("wode_account_jiaoyimingxi", "我的_我的交易明细");
                stopRun();
                startActivity(new Intent(mActivity, TradeRecordActivity.class));
                break;
            case R.id.tv_bill_details:
                clickTab("wode_account_churujinmingxi", "我的_出入金明细");
                InAndOutGoldActivity.startInAndOutGoldActivity(mActivity, 2);
                break;
            case R.id.tv_bond_query:
                clickTab("wode_account_jiancezhongxin", "我的_保证金检测中心查询");
                Intent intentQuery = new Intent(mActivity, WebViewActivity.class);
                intentQuery.putExtra("url", ConfigUtil.QUERY_FUTURE);
                intentQuery.putExtra("title", "保证金监控中心查询");
                startActivity(intentQuery);
                break;
            case R.id.tv_account_manage:
                clickTab("wode_account_qihuoaccountguanli","我的_期货账户管理");
                startActivity(new Intent(mActivity, FutureAccountActivity.class));
                break;
            case R.id.tv_signout:
                if (DeviceUtils.isFastDoubleClick()) {
                    return;
                }
                new AlertFragmentDialog.Builder(mActivity).setContent("确定退出期货账户吗？")
                        .setRightBtnText("确定")
                        .setLeftBtnText("取消")
                        .setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                            @Override
                            public void dialogRightBtnClick(String string) {
                                mPresenter.accountLogout(SpUtil.getString(Constant.CACHE_TAG_UID), SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN), SpUtil.getString(Constant.COMPANY_TYPE));
                            }
                        }).build();
                break;
        }
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
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
    public void getAccountInfoSuccess(AccountInfoBean bean) {
        mDangerChance.setText(bean.getCapitalProportion());
        mRightsInterests.setText(StringUtil.forNumber(new BigDecimal(bean.getRightsInterests()).doubleValue()));
        mMoney.setText(StringUtil.forNumber(new BigDecimal(bean.getAvailable()).doubleValue()));

        if (new BigDecimal(ConvertUtil.NVL(bean.getPositionProfit(), "0")).doubleValue() > 0) {
            mProfitLoss.setTextColor(getResources().getColor(R.color.color_FB4F4F));
            mProfitLoss.setText("+" + StringUtil.forNumber(new BigDecimal(ConvertUtil.NVL(bean.getPositionProfit(), "0")).doubleValue()));
        } else if (new BigDecimal(ConvertUtil.NVL(bean.getPositionProfit(), "0")).doubleValue() < 0) {
            mProfitLoss.setTextColor(getResources().getColor(R.color.color_2CC593));
            mProfitLoss.setText(StringUtil.forNumber(new BigDecimal(ConvertUtil.NVL(bean.getPositionProfit(), "0")).doubleValue()));
        } else {
            mProfitLoss.setTextColor(getResources().getColor(R.color.color_333333));
            mProfitLoss.setText(StringUtil.forNumber(new BigDecimal(ConvertUtil.NVL(bean.getPositionProfit(), "0")).doubleValue()));
        }


        if (new BigDecimal(bean.getCloseProfit()).doubleValue() > 0) {
            mPositionProfitLoss.setTextColor(getResources().getColor(R.color.color_FB4F4F));
            mPositionProfitLoss.setText("+" + StringUtil.forNumber(new BigDecimal(bean.getCloseProfit()).doubleValue()));
        } else if (new BigDecimal(bean.getCloseProfit()).doubleValue() < 0) {
            mPositionProfitLoss.setTextColor(getResources().getColor(R.color.color_2CC593));
            mPositionProfitLoss.setText(StringUtil.forNumber(new BigDecimal(bean.getCloseProfit()).doubleValue()));
        } else {
            mPositionProfitLoss.setTextColor(getResources().getColor(R.color.color_333333));
            mPositionProfitLoss.setText(StringUtil.forNumber(new BigDecimal(bean.getCloseProfit()).doubleValue()));
        }
        mServiceCharge.setText(String.valueOf(bean.getCommission()));
    }

    @Override
    public void accountLogoutSuccess() {
        SpUtil.putString(Constant.CACHE_ACCOUNT_TOKEN, "");
        EventBus.getDefault().post(new RefreshUIEvent(UIBaseEvent.EVENT_ACCOUNT_LOGOUT));
        stopRun();
    }

    private void getAccountBasicInfo() {
        mPresenter.getAccountInfo(SpUtil.getString(Constant.CACHE_TAG_UID), SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN), SpUtil.getString(Constant.COMPANY_TYPE));
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

    public void stopRun() {
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRun();
    }


    /**
     * 埋点
     *
     * @param value1
     * @param value2
     */
    private void clickTab(String value1, String value2) {
        MobclickAgent.onEvent(mContext, value1, value2);
    }
}
