package com.honglu.future.ui.circlefriend;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.widget.tab.CommonTabLayout;
import com.honglu.future.widget.tab.CustomTabEntity;
import com.honglu.future.widget.tab.SimpleOnTabSelectListener;
import com.honglu.future.widget.tab.TabEntity;

import java.util.ArrayList;

import butterknife.BindView;

public class MyFriendActivity extends BaseActivity {
    @BindView(R.id.trade_common_tab_layout)
    CommonTabLayout mCommonTabLayout;
    @BindView(R.id.tv_back)
    ImageView mIvBack;

    private ArrayList<CustomTabEntity> mTabList;
    private ArrayList<Fragment> mFragments;
    private MyToFriendFragment mMyToFriendFragment;
    private FriendToMyFragment mFriendToMyFragment;

    @Override
    public int getLayoutId() {
        return R.layout.activity_myfriend;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        initView();
    }

    private void initView() {
        mIvBack.setVisibility(View.VISIBLE);
        mTitle.setTitle(false, R.color.white, "圈友");
        //添加tab实体
        addTabEntities();
        //添加fragment
        addFragments();
    }

    private void addTabEntities() {
        mTabList = new ArrayList<>();
        mTabList.add(new TabEntity("我的关注(0)"));
        mTabList.add(new TabEntity("我的粉丝(0)"));
    }

    private void addFragments() {
        if (mFragments == null) {
            mFragments = new ArrayList<>();
        }
        mMyToFriendFragment = new MyToFriendFragment();
        mFragments.add(mMyToFriendFragment);
        mFriendToMyFragment = new FriendToMyFragment();
        mFragments.add(mFriendToMyFragment);

        mCommonTabLayout.setTabData(mTabList, (FragmentActivity) mContext, R.id.trade_fragment_container, mFragments);
        mCommonTabLayout.setOnTabSelectListener(new SimpleOnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                super.onTabSelect(position);

            }
        });
    }

}
