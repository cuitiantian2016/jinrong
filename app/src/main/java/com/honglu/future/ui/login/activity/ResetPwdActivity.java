package com.honglu.future.ui.login.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.ui.login.contract.ResetPwdContract;
import com.honglu.future.ui.login.presenter.ResetPwdPresenter;
import com.honglu.future.util.Tool;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zq on 2017/10/24.
 */

public class ResetPwdActivity extends BaseActivity<ResetPwdPresenter> implements ResetPwdContract.View {
    @BindView(R.id.iv_close)
    ImageView mClose;
    @BindView(R.id.iv_back)
    ImageView mBack;
    @BindView(R.id.tv_content)
    TextView mContent;
    @BindView(R.id.tv_login)
    TextView mTvLogin;

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
        return R.layout.activity_reset_pwd;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        mContent.setText("密码重置");
        mClose.setVisibility(View.GONE);
        mBack.setVisibility(View.VISIBLE);
        mTvLogin.setVisibility(View.GONE);
    }

    @OnClick({R.id.btn_reset_pwd, R.id.iv_back})
    public void onClick(View view) {
        if (Tool.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.btn_reset_pwd:
                finish();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
