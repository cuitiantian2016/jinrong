package com.honglu.future.ui.register.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.ui.login.activity.LoginActivity;
import com.honglu.future.ui.login.activity.ResetPwdActivity;
import com.honglu.future.ui.register.contract.RegisterContract;
import com.honglu.future.ui.register.presenter.RegisterPresenter;
import com.honglu.future.util.Tool;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zq on 2017/10/24.
 */

public class RegisterActivity extends BaseActivity<RegisterPresenter> implements RegisterContract.View {
    @BindView(R.id.tv_content)
    TextView mContent;
    @BindView(R.id.tv_login)
    TextView mTvLogin;

    @Override
    public void showLoading(String content) {

    }

    public void stopLoading() {

    }

    @Override
    public void showErrorMsg(String msg, String type) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.acticity_register;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        mContent.setText("注册");
        mTvLogin.setText("登录");
    }

    @OnClick({R.id.tv_login, R.id.iv_close})
    public void onClick(View view) {
        if (Tool.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.tv_login:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
//            case R.id.btn_register:
//                finish();
//                break;
            case R.id.iv_close:
                finish();
                break;
        }
    }
}
