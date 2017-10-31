package com.honglu.future.ui.usercenter.activity;

import android.view.View;
import android.widget.ImageView;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.ui.usercenter.contract.ModifyNicknameContract;
import com.honglu.future.ui.usercenter.presenter.ModifyNicknamePresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zq on 2017/10/31.
 */

public class ModifyNicknameActivity extends BaseActivity<ModifyNicknamePresenter> implements
        ModifyNicknameContract.View {
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
        return R.layout.activity_modify_nickname;
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
        mTitle.setTitle(false, R.color.white, "修改昵称");
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
