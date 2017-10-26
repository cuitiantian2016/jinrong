package com.honglu.future.ui.usercenter.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.config.Constant;
import com.honglu.future.events.RefreshUIEvent;
import com.honglu.future.events.UIBaseEvent;
import com.honglu.future.ui.login.activity.LoginActivity;
import com.honglu.future.ui.register.activity.RegisterActivity;
import com.honglu.future.ui.usercenter.activity.ModifyUserActivity;
import com.honglu.future.ui.usercenter.contract.UserCenterContract;
import com.honglu.future.ui.usercenter.presenter.UserCenterPresenter;
import com.honglu.future.util.LogUtils;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.StringUtil;
import com.honglu.future.util.ToastUtil;
import com.honglu.future.util.Tool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zq on 2017/10/24.
 */

public class UserCenterFragment extends BaseFragment<UserCenterPresenter> implements
        UserCenterContract.View {
    @BindView(R.id.tv_loginRegister)
    TextView mUserName;

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
                if (App.getConfig().getLoginStatus()) {
                    Intent intent = new Intent(mActivity, ModifyUserActivity.class);
                    startActivity(intent);
                } else {
                    toLogin();
                }
                break;
        }
    }

    private void toLogin() {
        String userId = SpUtil.getString(Constant.CACHE_TAG_UID);
        LogUtils.loge(userId);
        if (!StringUtil.isBlank(userId)) {
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.putExtra("userId", userId);
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
        EventBus.getDefault().register(this);
        initView();
    }

    /***********
     * eventBus 监听
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UIBaseEvent event) {
        if (event instanceof RefreshUIEvent) {
            int code = ((RefreshUIEvent) event).getType();
            if (code == UIBaseEvent.EVENT_LOGIN) {//登录
                // 数据刷新
                if (App.getConfig().getLoginStatus()) {
                    mUserName.setText(SpUtil.getString(Constant.CACHE_TAG_MOBILE));
                }
            }
        }
    }

    private void initView() {
        mTitle.setTitle(false, R.color.white, "我的");
        if (App.getConfig().getLoginStatus()) {
            mUserName.setText(SpUtil.getString(Constant.CACHE_TAG_USERNAME));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        userCenterFragment = null;
    }
}
