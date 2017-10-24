package com.honglu.future.ui.login.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.ui.register.activity.RegisterActivity;
import com.honglu.future.ui.register.contract.RegisterContract;
import com.honglu.future.ui.register.presenter.RegisterPresenter;
import com.honglu.future.util.Tool;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zq on 2017/10/24.
 */

public class LoginActivity extends BaseActivity<RegisterPresenter> implements RegisterContract.View {

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
        return R.layout.activity_login;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {

    }

    @OnClick({R.id.tv_login, R.id.tv_forget_pwd, R.id.btn_login, R.id.iv_close})
    public void onClick(View view) {
        if (Tool.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.tv_login:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.tv_forget_pwd:
                Intent resetPwd = new Intent(this, ResetPwdActivity.class);
                startActivity(resetPwd);
                finish();
                break;
            case R.id.btn_login:
                finish();
                break;
            case R.id.iv_close:
                finish();
                break;
        }
    }
}
