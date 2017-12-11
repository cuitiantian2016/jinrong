package com.honglu.future.ui.circle.praisesandreward;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLy;
    private ArrayList<Fragment> mTabFragments;
    private static final String[] TAB_TITLES = new String[]{"点赞列表"};

    private static final String KEY_TOPIC = "KEY_TOPIC";
    private static final String KEY_IS_ATTENTION = "KEY_IS_ATTENTION";

    public static void startRewardDetailActivity(Context context,String topic,String attention){
        Intent intent = new Intent(context,RewardDetailActivity.class);
        intent.putExtra(KEY_TOPIC,topic);
        intent.putExtra(KEY_IS_ATTENTION,attention);
        context.startActivity(intent);
    }

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
        ThumbsListFragment thumbsListFragment = new ThumbsListFragment();
        Bundle rewardBundle = new Bundle();
        rewardBundle.putString(ThumbsListFragment.EXTRA_TID,getIntent().getStringExtra(KEY_TOPIC));
        rewardBundle.putString(ThumbsListFragment.EXTRA_TIDE_ID,getIntent().getStringExtra(KEY_IS_ATTENTION));
        thumbsListFragment.setArguments(rewardBundle);
        mTabFragments.add(thumbsListFragment);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mSlidingTabLy = (SlidingTabLayout) findViewById(R.id.ly_tabs);
        mSlidingTabLy.setViewPager(mViewPager, TAB_TITLES, this, mTabFragments);
        mViewPager.setCurrentItem(0);
    }

}
