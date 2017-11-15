package com.honglu.future.ui.usercenter.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.config.Constant;
import com.honglu.future.dialog.TradeTipDialog;
import com.honglu.future.ui.recharge.activity.InAndOutGoldActivity;
import com.honglu.future.ui.usercenter.bean.AccountInfoBean;
import com.honglu.future.ui.usercenter.contract.UserAccountContract;
import com.honglu.future.ui.usercenter.presenter.UserAccountPresenter;
import com.honglu.future.util.NumberUtils;
import com.honglu.future.util.SpUtil;

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
    @BindView(R.id.tv_take_bond)
    TextView mTakeBond;
    @BindView(R.id.tv_occupy_bond)
    TextView mOccupyBond;
    @BindView(R.id.tv_frozen_bond)
    TextView mFrozenBond;
    @BindView(R.id.tv_service_charge)
    TextView mServiceCharge;
    @BindView(R.id.tv_frozen_service_charge)
    TextView mFrozenServiceCharge;
    @BindView(R.id.tv_withdrawals)
    TextView mWithdrawals;
    @BindView(R.id.tv_recharge)
    TextView mRecharge;

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_account;
    }

    @Override
    public void loadData() {
        mTitle.setTitle(true, R.mipmap.ic_back_black, R.color.white, getResources().getString(R.string.user_account));
        mTitle.setRightTitle(R.mipmap.ic_trade_tip, this);
        getAccountBasicInfo();
    }

    @OnClick({R.id.tv_withdrawals, R.id.tv_recharge, R.id.tv_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_withdrawals:
                InAndOutGoldActivity.startInAndOutGoldActivity(this, 1);
                break;
            case R.id.tv_recharge:
                InAndOutGoldActivity.startInAndOutGoldActivity(this, 0);
                break;
            case R.id.tv_right:
                TradeTipDialog tipDialog = new TradeTipDialog(mContext, R.layout.layout_my_account_tip);
                tipDialog.show();
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
        mRightsInterests.setText(NumberUtils.formatFloatNumber(bean.getRightsInterests()));
        mMoney.setText(NumberUtils.formatFloatNumber(bean.getAvailable()));
        mProfitLoss.setText(bean.getPositionProfit() + "");
        mPositionProfitLoss.setText(bean.getCloseProfit() + "");
        mTakeBond.setText(NumberUtils.formatFloatNumber(bean.getWithdrawQuota()));
        mOccupyBond.setText(bean.getCapitalProportionNum());
        mFrozenBond.setText(bean.getFrozenCash() + "");
        mServiceCharge.setText(String.valueOf(bean.getCommission()));
        mFrozenServiceCharge.setText(String.valueOf(bean.getFrozenCommission()));
    }

    private void getAccountBasicInfo() {
        mPresenter.getAccountInfo(SpUtil.getString(Constant.CACHE_TAG_UID), SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN), "GUOFU");
    }

}
