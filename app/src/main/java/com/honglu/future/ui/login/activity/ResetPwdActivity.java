package com.honglu.future.ui.login.activity;

import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.config.Constant;
import com.honglu.future.ui.login.contract.ResetPwdContract;
import com.honglu.future.ui.login.presenter.ResetPwdPresenter;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.SpUtil;
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
    @BindView(R.id.reset_content)
    LinearLayout mResetView;
    @BindView(R.id.btn_reset_pwd)
    TextView mReset;

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
        if (!TextUtils.isEmpty(SpUtil.getString(Constant.CACHE_TAG_MOBILE))) {
            mMobile.setText(SpUtil.getString(Constant.CACHE_TAG_MOBILE));
            mMobile.setSelection(mMobile.getText().toString().length());
            mSendCodeView.setEnabled(true);
        }

        mMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mMobile.getText().toString().length() >= 11 && mSmsCode.getText().toString().length() >= 4 && mPwd.getText().toString().length() >= 6) {
                    mReset.setEnabled(true);
                } else {
                    mReset.setEnabled(false);
                }
            }
        });

        mSmsCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mMobile.getText().toString().length() >= 11 && mSmsCode.getText().toString().length() >= 4 && mPwd.getText().toString().length() >= 6) {
                    mReset.setEnabled(true);
                } else {
                    mReset.setEnabled(false);
                }
            }
        });

        mPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mMobile.getText().toString().length() >= 11 && mSmsCode.getText().toString().length() >= 4 && mPwd.getText().toString().length() >= 6) {
                    mReset.setEnabled(true);
                } else {
                    mReset.setEnabled(false);
                }
            }
        });

        controlKeyboardLayout(findViewById(R.id.rootView));
    }

    /**
     * @param root 最外层布局，需要调整的布局
     *             输入框，滚动root,使输入框在root可视区域的底部
     */
    private void controlKeyboardLayout(final View root) {
        if (root == null)
            return;
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mResetView == null)
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

                    mResetView.getLocationInWindow(location);

//                    int minH = (int)((double)getWindowManager().getDefaultDisplay().getHeight() * (1D/2D));
//                    if (focuseLocation[1] < minH)
//                        return;
                    //计算root滚动高度，使scrollToView在可见区域
//                    int srollHeight = (location[1] + focuseInputView.getHeight()) - rect.bottom;
                    final int srollHeight = mResetView.getHeight() + location[1];
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

    @OnClick({R.id.btn_reset_pwd, R.id.iv_back, R.id.btn_sendCode})
    public void onClick(View view) {
        if (Tool.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.btn_sendCode:
                if (DeviceUtils.isFastDoubleClick()) {
                    return;
                }
                if (CheckUtils.checkPhoneNum(mMobile.getText().toString())) {
                    mSendCodeView.start();
                    mSmsCode.requestFocus();
                    mPresenter.getResetCode("10", mMobile.getText().toString());
                }
                break;
            case R.id.btn_reset_pwd:
                if (DeviceUtils.isFastDoubleClick()) {
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
                mPresenter.resetPwd(mMobile.getText().toString(), mPwd.getText().toString(), mSmsCode.getText().toString());
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
