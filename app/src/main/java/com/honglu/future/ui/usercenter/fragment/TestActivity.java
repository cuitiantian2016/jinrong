package com.honglu.future.ui.usercenter.fragment;

import android.os.Bundle;
import android.widget.Button;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhuaibing on 2017/11/29
 */

public class TestActivity extends BaseActivity {

    @BindView(R.id.bt_sub)
    Button btSub;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {

    }
}
