package com.honglu.future.ui.usercenter.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.ui.usercenter.contract.UserAccountContract;
import com.honglu.future.ui.usercenter.presenter.UserAccountPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的账户
 * Created by zhuaibing on 2017/10/31
 */

public class UserAccountActivity extends BaseActivity<UserAccountPresenter> implements UserAccountContract.View ,View.OnClickListener{

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
        mTitle.setTitle(true,R.mipmap.ic_back_black, R.color.white, getResources().getString(R.string.user_account));
        mTitle.setRightTitle(R.mipmap.ic_trade_tip,this);
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.tv_right://title 右边按钮

               break;
       }
    }

    @Override
    public void initPresenter() {

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
}
