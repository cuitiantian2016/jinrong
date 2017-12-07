package com.honglu.future.ui.circle.circlemsg;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
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

public class CircleMsgActivity extends BaseActivity{
    @BindView(R.id.circle_msg_tab)
    CommonTabLayout mCircleMsgTab;

    private CircleMsgHFragment mHfFragment;
    private CircleMsgPLFragment mPlFragment;


    @Override
    public void initPresenter() {

    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_circle_msg;
    }

    @Override
    public void loadData() {

        ArrayList<CustomTabEntity> mTabList = new ArrayList<>();
        mTabList.add(new TabEntity("回复我的"));
        mTabList.add(new TabEntity("帖子评论"));

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
            }
        });
    }

}
