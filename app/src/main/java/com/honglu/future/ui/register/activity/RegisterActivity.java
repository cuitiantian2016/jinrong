package com.honglu.future.ui.register.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.config.ConfigUtil;
import com.honglu.future.events.LoginEvent;
import com.honglu.future.ui.login.activity.LoginActivity;
import com.honglu.future.ui.main.activity.WebViewActivity;
import com.honglu.future.ui.register.contract.RegisterContract;
import com.honglu.future.ui.register.presenter.RegisterPresenter;
import com.honglu.future.ui.usercenter.bean.UserInfoBean;
import com.honglu.future.util.Tool;
import com.honglu.future.widget.CheckUtils;
import com.honglu.future.widget.CountDownTextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

import static com.honglu.future.util.ToastUtil.showToast;

/**
 * Created by zq on 2017/10/24.
 */

public class RegisterActivity extends BaseActivity<RegisterPresenter> implements RegisterContract.View {
    @BindView(R.id.tv_content)
    TextView mContent;
    @BindView(R.id.tv_login)
    TextView mTvLogin;
    @BindView(R.id.tv_set_pwd)
    EditText mEtPassword;
    @BindView(R.id.tv_sms_code)
    EditText mSmsCode;
    @BindView(R.id.tv_mobile)
    EditText mMobile;
    @BindView(R.id.btn_sendCode)
    CountDownTextView mSendCodeView;

    @Override
    public void showLoading(String content) {
        if (!TextUtils.isEmpty(content)) {
            App.loadingContent(this, content);
        }
    }

    public void stopLoading() {
        App.hideLoading();
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        showToast(msg);
    }

    @Override
    public int getLayoutId() {
        return R.layout.acticity_register;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        mContent.setText("注册");
        mTvLogin.setText("登录");
        initViews();
    }

    private void initViews() {
    }

    @OnClick({R.id.tv_login, R.id.iv_close, R.id.btn_register, R.id.btn_sendCode,R.id.tv_protocol})
    public void onClick(View view) {
        if (Tool.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.tv_login:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_register:
                if (mMobile.length() < 11) {
                    showToast("请输入正确的手机号码");
                    return;
                }
                if (mSmsCode.length() < 4) {
                    showToast("请输入正确的验证码");
                    return;
                }

                if (mEtPassword.length() < 6) {
                    showToast("密码必须为6~16字符");
                    return;
                }
                mPresenter.register(mSmsCode.getText().toString(), "10",
                        mMobile.getText().toString(), mEtPassword.getText().toString(), mMobile.getText().toString());
                break;
            case R.id.btn_sendCode:
                if (CheckUtils.checkPhoneNum(mMobile.getText().toString())) {
                    mSendCodeView.start();
                    mSmsCode.requestFocus();
                    mPresenter.getCode("10", mMobile.getText().toString());
                }
                break;
            case R.id.iv_close:
                finish();
                break;
            case R.id.tv_protocol:
                Intent intentProtocol = new Intent(mActivity, WebViewActivity.class);
                intentProtocol.putExtra("title", "用户协议");
                intentProtocol.putExtra("url", ConfigUtil.USER_PROTPCOL);
                startActivity(intentProtocol);
                break;
        }
    }

    @Override
    public void registerSuccess(UserInfoBean bean) {
        EventBus.getDefault().post(new LoginEvent(getApplicationContext(), bean));
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //得到InputMethodManager的实例
        imm.hideSoftInputFromWindow(mEtPassword.getWindowToken(), 0);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSendCodeView != null) {
            mSendCodeView.setOnCountDownStopListeners(null);
            mSendCodeView.removeMessages();
        }
    }
}
