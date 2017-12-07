package com.honglu.future.ui.register.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.Tool;
import com.honglu.future.widget.CheckUtils;
import com.honglu.future.widget.CountDownTextView;
import com.umeng.analytics.MobclickAgent;

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
    @BindView(R.id.register_content)
    LinearLayout registerView;

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
        controlKeyboardLayout(findViewById(R.id.rootView));
    }

    /**
     * @param root 最外层布局，需要调整的布局
     * 输入框，滚动root,使输入框在root可视区域的底部
     */
    private void controlKeyboardLayout(final View root) {
        if (root == null)
            return;
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (registerView == null)
                    return;

                Rect rect = new Rect();
                //获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);
                //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                //若不可视区域高度大于100，则键盘显示
                if (rootInvisibleHeight > 100) {
                    int[] location = new int[2];
//                    int[] focuseLocation = new int[2];
//                    获取scrollToView在窗体的坐标
//                    focuseInputView.getLocationInWindow(focuseLocation);

                    registerView.getLocationInWindow(location);

//                    int minH = (int)((double)getWindowManager().getDefaultDisplay().getHeight() * (1D/2D));
//                    if (focuseLocation[1] < minH)
//                        return;
                    //计算root滚动高度，使scrollToView在可见区域
//                    int srollHeight = (location[1] + focuseInputView.getHeight()) - rect.bottom;
                    final int srollHeight = registerView.getHeight() + location[1];
                    root.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            root.scrollTo(0, srollHeight);
                        }
                    }, 50);

//                    root.scrollTo(0, 1483);
                } else {
                    //键盘隐藏
                    root.scrollTo(0, 0);
                }
            }
        });
    }

    private void initViews() {
    }

    @OnClick({R.id.tv_login, R.id.iv_close, R.id.btn_register, R.id.btn_sendCode,R.id.tv_protocol})
    public void onClick(View view) {
        if (Tool.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.tv_login:
                if(DeviceUtils.isFastDoubleClick()){
                    return;
                }
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_register:
                clickTab("zcdl_huoquyanzhengma_zhuce","获取验证码_ 注册");
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

                if (mEtPassword.length() < 6) {
                    showToast("密码必须为6~16字符");
                    return;
                }
                mPresenter.register(mSmsCode.getText().toString(), "10",
                        mMobile.getText().toString(), mEtPassword.getText().toString(), mMobile.getText().toString());
                break;
            case R.id.btn_sendCode:
                if(DeviceUtils.isFastDoubleClick()){
                    return;
                }
                clickTab("zcdl_huoquyanzhengma","注册_获取验证码");
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


    /**
     * 埋点
     * @param value1
     * @param value2
     */
    private void clickTab(String value1 , String value2){
        MobclickAgent.onEvent(mContext,value1, value2);
    }
}
