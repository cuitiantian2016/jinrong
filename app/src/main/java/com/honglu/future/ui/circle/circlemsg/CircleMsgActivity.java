package com.honglu.future.ui.circle.circlemsg;

import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.util.AndroidUtil;
import com.honglu.future.widget.tab.CommonTabLayout;
import com.honglu.future.widget.tab.CustomTabEntity;
import com.honglu.future.widget.tab.SimpleOnTabSelectListener;
import com.honglu.future.widget.tab.TabEntity;

import java.util.ArrayList;


import butterknife.BindView;

/**
 * 消息
 * Created by zhuaibing on 2017/12/7
 */

public class CircleMsgActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.circle_msg_tab)
    CommonTabLayout mCircleMsgTab;

    private CircleMsgHFragment mHfFragment;
    private CircleMsgPLFragment mPlFragment;
    private int mPosition = 0;

    @Override
    public void initPresenter() {

    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_circle_msg;
    }

    @Override
    public void loadData() {
        mTitle.setTitle(false, R.color.color_white,"消息");
        mTitle.setRightTitle("清空",this);

        ArrayList<CustomTabEntity> mTabList = new ArrayList<>();
        mTabList.add(new TabEntity("收到的回复"));
        mTabList.add(new TabEntity("收到的评论"));

        ArrayList<Fragment> mFragments = new ArrayList<>();
        mHfFragment = new CircleMsgHFragment();
        mPlFragment = new CircleMsgPLFragment();
        mFragments.add(mHfFragment);
        mFragments.add(mPlFragment);

        mCircleMsgTab.setTabData(mTabList, (FragmentActivity) mContext, R.id.fl_content, mFragments);
        mCircleMsgTab.setOnTabSelectListener(new SimpleOnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                super.onTabSelect(position);
                mPosition = position;
                AndroidUtil.hideInputKeyboard(CircleMsgActivity.this);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_right: //清空
                if (mPosition == 0){
                    mHfFragment.getClearReply();
                }else {
                    mPlFragment.getClearComments();
                }
                break;
        }
    }
}
