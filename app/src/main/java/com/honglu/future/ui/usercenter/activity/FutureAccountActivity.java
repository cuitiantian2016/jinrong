package com.honglu.future.ui.usercenter.activity;

import android.view.View;
import android.widget.ImageView;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.ui.usercenter.contract.FutureAccountContract;
import com.honglu.future.ui.usercenter.presenter.FutureAccountPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zq on 2017/10/31.
 */

public class FutureAccountActivity extends BaseActivity<FutureAccountPresenter> implements
        FutureAccountContract.View {
    @BindView(R.id.tv_back)
    ImageView mIvBack;

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
        return R.layout.activity_future_account;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        initViews();
    }

    private void initViews() {
        mIvBack.setVisibility(View.VISIBLE);
        mTitle.setTitle(false, R.color.white, "期货账户管理");
    }

    @OnClick({R.id.tv_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
        }
    }
}
