package com.honglu.future.ui.usercenter.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.RadioButton;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.config.Constant;
import com.honglu.future.ui.login.activity.LoginActivity;
import com.honglu.future.ui.main.activity.MainActivity;
import com.honglu.future.ui.register.activity.RegisterActivity;
import com.honglu.future.ui.trade.fragment.TradeFragment;
import com.honglu.future.ui.usercenter.contract.UserCenterContract;
import com.honglu.future.ui.usercenter.presenter.UserCenterPresenter;
import com.honglu.future.util.LogUtils;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.StringUtil;
import com.honglu.future.util.Tool;

import butterknife.OnClick;

/**
 * Created by zq on 2017/10/24.
 */

public class UserCenterFragment extends BaseFragment<UserCenterPresenter> implements
        UserCenterContract.View {

    public static UserCenterFragment userCenterFragment;

    public static UserCenterFragment getInstance() {
        if (userCenterFragment == null) {
            userCenterFragment = new UserCenterFragment();
        }
        return userCenterFragment;
    }

    @OnClick({R.id.tv_loginRegister})
    public void onClick(View view) {
        if (Tool.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.tv_loginRegister:
                toLogin();
                break;
        }
    }

    private void toLogin() {
        String uName = SpUtil.getString(Constant.CACHE_TAG_USERNAME);
        LogUtils.loge(uName);
        if (!StringUtil.isBlank(uName) && StringUtil.isMobileNO(uName)) {
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.putExtra("phone", uName);
            startActivity(intent);
        } else {
            Intent intent = new Intent(mContext, RegisterActivity.class);
            startActivity(intent);
        }
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

    @Override
    public int getLayoutId() {
        return R.layout.fragment_user_center_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        initView();
    }

    private void initView() {
        mTitle.setTitle(false, R.color.white, "我的");
    }
}
