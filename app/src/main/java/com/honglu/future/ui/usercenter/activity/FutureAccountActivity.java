package com.honglu.future.ui.usercenter.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.config.Constant;
import com.honglu.future.ui.recharge.activity.PasswordResetActivity;
import com.honglu.future.ui.usercenter.contract.FutureAccountContract;
import com.honglu.future.ui.usercenter.presenter.FutureAccountPresenter;
import com.honglu.future.util.SpUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zq on 2017/10/31.
 */

public class FutureAccountActivity extends BaseActivity<FutureAccountPresenter> implements
        FutureAccountContract.View {
    @BindView(R.id.tv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_account)
    TextView mTvAccount;

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
        return R.layout.activity_future_account;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        initViews();
    }

    private void initViews() {
        mIvBack.setVisibility(View.VISIBLE);
        mTitle.setTitle(false, R.color.white, "期货账户管理");
        mTvAccount.setText(SpUtil.getString(Constant.CACHE_ACCOUNT_USER_NAME));
    }

    @OnClick({R.id.tv_back,R.id.rl_reset_market_pwd,R.id.rl_reset_asses_pwd})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.rl_reset_market_pwd:
                clickTab("wode_accountqihuo_xiugaijiaoyikey","我的_期货账户_修改交易密码");
                PasswordResetActivity.startPasswordResetActivity(FutureAccountActivity.this,true);
                break;
            case R.id.rl_reset_asses_pwd:
                clickTab("wode_accountqihuo_xiugaizijinkey","我的_期货账户_修改资金密码");
                PasswordResetActivity.startPasswordResetActivity(FutureAccountActivity.this,false);
                break;
        }
    }

    /**
     * 埋点
     * @param value1
     * @param value2
     */
    private void clickTab(String value1 , String value2){
        MobclickAgent.onEvent(mContext,value1, value2);
    }
}
