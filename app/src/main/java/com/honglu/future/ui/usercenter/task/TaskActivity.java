package com.honglu.future.ui.usercenter.task;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.ui.circle.praisesandreward.ThumbsListFragment;
import com.honglu.future.widget.kchart.SlidingTabLayout;

import java.util.ArrayList;


/**
 * deprecation:任务
 * author:ayb
 * time:2017/6/14
 */
public class TaskActivity extends BaseActivity {

    private static final String[] TAB_TITLES = new String[]{"新手任务","每日任务"};
    public static void startTaskActivity(Context context){
        Intent intent = new Intent(context,TaskActivity.class);
        context.startActivity(intent);
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_task;
    }
    @Override
    public void initPresenter() {
    }
    @Override
    public void loadData() {
        initViews();
    }

    private void initViews() {
        mTitle.setTitle(false, R.color.white, "任务");
        ArrayList<Fragment> mTabFragments = new ArrayList<>();

        for (String title:TAB_TITLES) {
            TaskFragment thumbsListFragment = new TaskFragment();
            Bundle rewardBundle = new Bundle();
            if ("新手任务".equals(title)){
                rewardBundle.putBoolean(TaskFragment.EXTRA_IS_NEW,true);
            }else {
                rewardBundle.putBoolean(TaskFragment.EXTRA_IS_NEW,false);
            }
            thumbsListFragment.setArguments(rewardBundle);
            mTabFragments.add(thumbsListFragment);
        }
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewPager);
        SlidingTabLayout mSlidingTabLy = (SlidingTabLayout) findViewById(R.id.ly_tabs);
        mSlidingTabLy.setViewPager(mViewPager, TAB_TITLES, this, mTabFragments);
        mSlidingTabLy.setCurrentTab(0);
    }

}
