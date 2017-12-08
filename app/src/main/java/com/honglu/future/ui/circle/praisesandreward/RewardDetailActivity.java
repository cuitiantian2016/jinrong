package com.honglu.future.ui.circle.praisesandreward;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.widget.kchart.SlidingTabLayout;

import java.util.ArrayList;


/**
 * deprecation:打赏详情
 * author:ayb
 * time:2017/6/14
 */
public class RewardDetailActivity extends BaseActivity {

    private int mIndex = 0;
    private String mTid;
    private String mTideId;

    private ViewPager mViewPager;

    private SlidingTabLayout mSlidingTabLy;

    private ArrayList<Fragment> mTabFragments;

    private static final String[] TAB_TITLES = new String[]{"点赞列表"};

    @Override
    public int getLayoutId() {
        return R.layout.activity_reward_record;
    }
    @Override
    public void initPresenter() {
    }
    @Override
    public void loadData() {
        initViews();
    }

    private void initViews() {
        mTitle.setTitle(false, R.color.white, "点赞列表");
        mTabFragments = new ArrayList<>();
//        RewardListFragment rewardListFragment = new RewardListFragment();
//        Bundle rewardBundle = new Bundle();
//        rewardBundle.putString(RewardListFragment.EXTRA_TOPIC_ID,mTid);
//        rewardListFragment.setArguments(rewardBundle);
//        mTabFragments.add(rewardListFragment);
        ThumbsListFragment thumbsListFragment = new ThumbsListFragment();
        mTabFragments.add(thumbsListFragment);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mSlidingTabLy = (SlidingTabLayout) findViewById(R.id.ly_tabs);
        mSlidingTabLy.setViewPager(mViewPager, TAB_TITLES, this, mTabFragments);
        mViewPager.setCurrentItem(mIndex);
    }

}
