package com.honglu.future.ui.login.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.ui.login.contract.ResetPwdContract;
import com.honglu.future.ui.login.presenter.ResetPwdPresenter;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.ToastUtil;
import com.honglu.future.util.Tool;
import com.honglu.future.widget.CheckUtils;
import com.honglu.future.widget.CountDownTextView;

import butterknife.BindView;
import butterknife.OnClick;

import static com.honglu.future.util.ToastUtil.showToast;

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
    @BindView(R.id.tv_mobile)
    EditText mMobile;
    @BindView(R.id.tv_sms_code)
    EditText mSmsCode;
    @BindView(R.id.tv_set_pwd)
    EditText mPwd;
    @BindView(R.id.btn_sendCode)
    CountDownTextView mSendCodeView;

    @Override
    public void showLoading(String content) {
        if (!TextUtils.isEmpty(content)) {
            App.loadingContent(this, content);
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
        return R.layout.activity_reset_pwd;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        mContent.setText("密码重置");
        mClose.setVisibility(View.GONE);
        mBack.setVisibility(View.VISIBLE);
        mTvLogin.setVisibility(View.GONE);
    }

    @OnClick({R.id.btn_reset_pwd, R.id.iv_back, R.id.btn_sendCode})
    public void onClick(View view) {
        if (Tool.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.btn_sendCode:
                if(DeviceUtils.isFastDoubleClick()){
                    return;
                }
                if (CheckUtils.checkPhoneNum(mMobile.getText().toString())) {
                    mSendCodeView.start();
                    mSmsCode.requestFocus();
                    mPresenter.getResetCode("10", mMobile.getText().toString());
                }
                break;
            case R.id.btn_reset_pwd:
                if(DeviceUtils.isFastDoubleClick()){
                    return;
                }
                if (mMobile.length() < 11) {
                    showToast("请输入正确的手机号码");
                    return;
                }
                if (mSmsCode.length() < 4) {
                    showToast("请输入正确的验证码");
                    return;
                }

                if (mPwd.length() < 6) {
                    showToast("密码必须为6~16字符");
                    return;
                }
                mPresenter.resetPwd(mMobile.getText().toString(), mPwd.getText().toString(),mSmsCode.getText().toString());
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public void resetPwdSuccess() {
        ToastUtil.show("密码重置成功");
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
