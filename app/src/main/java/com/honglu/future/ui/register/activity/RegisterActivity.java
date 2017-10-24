package com.honglu.future.ui.register.activity;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.ui.register.contract.RegisterContract;
import com.honglu.future.ui.register.presenter.RegisterPresenter;

/**
 * Created by zq on 2017/10/24.
 */

public class RegisterActivity extends BaseActivity<RegisterPresenter> implements RegisterContract.View{
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
        return R.layout.acticity_register;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        mTitle.setTitle(false, R.color.white, "注册登录");
    }
}
