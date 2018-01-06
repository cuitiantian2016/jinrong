package com.honglu.future.ui.login.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.config.Constant;
import com.honglu.future.events.LoginEvent;
import com.honglu.future.ui.login.contract.LoginContract;
import com.honglu.future.ui.login.presenter.LoginPresenter;
import com.honglu.future.ui.register.activity.RegisterActivity;
import com.honglu.future.ui.usercenter.bean.UserInfoBean;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.Tool;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

import static com.honglu.future.util.ToastUtil.showToast;

/**
 * Created by zq on 2017/10/24.
 */
@Route(path = "/future/login")
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {
    @BindView(R.id.tv_mobile)
    EditText mMobile;
    @BindView(R.id.tv_pwd)
    EditText mPwd;
    @BindView(R.id.login_content)
    LinearLayout loginView;
    @BindView(R.id.btn_login)
    TextView mLogin;
    @Autowired
    public String redirect;

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
        return R.layout.activity_login;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        redirect = getIntent().getStringExtra("redirect");
        if (!TextUtils.isEmpty(redirect)) {
            redirect = Uri.decode(redirect);
        }
        String uID = SpUtil.getString(Constant.CACHE_TAG_UID);
        if (!TextUtils.isEmpty(uID)) {//说明已经登录
            if (!goToRedirect()) {
                finish();
            }
        }
        if (!TextUtils.isEmpty(SpUtil.getString(Constant.CACHE_TAG_MOBILE))) {
            mMobile.setText(SpUtil.getString(Constant.CACHE_TAG_MOBILE));
            mMobile.setSelection(mMobile.getText().toString().length());
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
                if (mMobile.getText().toString().length() >= 11 && mPwd.getText().toString().length() >= 6) {
                    mLogin.setEnabled(true);
                } else {
                    mLogin.setEnabled(false);
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
                if (mMobile.getText().toString().length() >= 11 && mPwd.getText().toString().length() >= 6) {
                    mLogin.setEnabled(true);
                } else {
                    mLogin.setEnabled(false);
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
                if (loginView == null)
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

                    loginView.getLocationInWindow(location);

//                    int minH = (int)((double)getWindowManager().getDefaultDisplay().getHeight() * (1D/2D));
//                    if (focuseLocation[1] < minH)
//                        return;
                    //计算root滚动高度，使scrollToView在可见区域
//                    int srollHeight = (location[1] + focuseInputView.getHeight()) - rect.bottom;
                    final int srollHeight = loginView.getHeight() + location[1];
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

    @OnClick({R.id.tv_login, R.id.tv_forget_pwd, R.id.btn_login, R.id.iv_close})
    public void onClick(View view) {
        if (Tool.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.tv_login:
                if (DeviceUtils.isFastDoubleClick()) {
                    return;
                }
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.tv_forget_pwd:
                if (DeviceUtils.isFastDoubleClick()) {
                    return;
                }
                Intent resetPwd = new Intent(this, ResetPwdActivity.class);
                startActivity(resetPwd);
                finish();
                break;
            case R.id.btn_login:
                if (DeviceUtils.isFastDoubleClick()) {
                    return;
                }
                clickTab("zcdl_denglu_land", "我的_登录");
                if (mMobile.length() < 11) {
                    showToast("请输入正确的手机号码");
                    return;
                }

                if (mPwd.length() < 6) {
                    showToast("密码必须为6~16字符");
                    return;
                }

                mPresenter.login(mMobile.getText().toString(),
                        mPwd.getText().toString());
                break;
            case R.id.iv_close:
                finish();
                break;
        }
    }

    @Override
    public void loginSuccess(UserInfoBean bean) {
        EventBus.getDefault().post(new LoginEvent(getApplicationContext(), bean));
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //得到InputMethodManager的实例
        imm.hideSoftInputFromWindow(mPwd.getWindowToken(), 0);
        if (!goToRedirect()) {
            finish();
        }
    }

    private boolean goToRedirect() {
        if (!TextUtils.isEmpty(redirect)) {
            Log.d("ARouter", redirect + "--->redirect");
            ARouter.getInstance().build(Uri.parse(redirect)).navigation(this, new NavCallback() {
                @Override
                public void onArrival(Postcard postcard) {
                    finish();
                }

                @Override
                public void onLost(Postcard postcard) {
                    super.onLost(postcard);
                    finish();
                }
            });
            return true;
        }
        return false;
    }

    /**
     * 埋点
     *
     * @param value1
     * @param value2
     */
    private void clickTab(String value1, String value2) {
        MobclickAgent.onEvent(mContext, value1, value2);
    }
}
