package com.honglu.future.ui.circle.circlemine;

import android.view.View;
import android.widget.ImageView;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;

/**
 * Created by zq on 2017/12/7.
 */

public class CircleMineActivity extends BaseActivity<CircleMinePresenter> implements CircleMineContract.View {
    @Override
    public int getLayoutId() {
        return R.layout.activity_circle_mine;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        MineFragment fragmet = (MineFragment) getSupportFragmentManager().findFragmentById(R.id.ly_bbs_mine);

        final ImageView mBackWhiteIV = (ImageView) findViewById(R.id.iv_back);
        final ImageView mBackAlphaIV = (ImageView) findViewById(R.id.iv_alpha_bg);
        mBackWhiteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fragmet.setOnTopicAlaph(new MineFragment.OnTopicAlaph() {
            @Override
            public void onAlaphValue(float value) {
                if (value <= 05f) {
                    mBackAlphaIV.setAlpha(value);
                }
            }
        });
    }

}
