package com.honglu.future.ui.usercenter.task;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.ui.circle.praisesandreward.ArewardListFragment;
import com.honglu.future.ui.circle.praisesandreward.ThumbsListFragment;
import com.honglu.future.widget.kchart.SlidingTabLayout;
import com.honglu.future.widget.tab.CommonTabLayout;
import com.honglu.future.widget.tab.CustomTabEntity;
import com.honglu.future.widget.tab.SimpleOnTabSelectListener;
import com.honglu.future.widget.tab.TabEntity;

import java.util.ArrayList;

import butterknife.BindView;


/**
 * deprecation:任务
 * author:ayb
 * time:2017/6/14
 */
@Route(path = "/future/task")
public class TaskActivity extends BaseActivity {

    private static final String[] TAB_TITLES = new String[]{"新手任务", "每日任务"};

    public static void startTaskActivity(Context context) {
        Intent intent = new Intent(context, TaskActivity.class);
        context.startActivity(intent);
    }

    @BindView(R.id.reward_record_tab)
    CommonTabLayout mTabLayout;

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
        mTitle.setTitle(false, R.color.white, "任务中心");
        ArrayList<Fragment> mFragments = new ArrayList<>();
        ArrayList<CustomTabEntity> mTabList = new ArrayList<>();
        TaskFragment taskFragmentOne = new TaskFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(TaskFragment.EXTRA_IS_NEW, true);
        taskFragmentOne.setArguments(bundle);
        mFragments.add(taskFragmentOne);
        mTabList.add(new TabEntity("新手任务"));

        TaskFragment taskFragment = new TaskFragment();
        Bundle bundle1 = new Bundle();
        bundle.putBoolean(TaskFragment.EXTRA_IS_NEW, false);
        taskFragmentOne.setArguments(bundle1);
        mFragments.add(taskFragment);
        mTabList.add(new TabEntity("每日任务"));
        mTabLayout.setTabData(mTabList, (FragmentActivity) mContext, R.id.fl_content, mFragments);
        mTabLayout.setOnTabSelectListener(new SimpleOnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                super.onTabSelect(position);
            }
        });

    }

    public void hindTab(){
        mTabLayout.setVisibility(View.GONE);
    }

    public void showTab(){
        if (mTabLayout.getVisibility()!=View.VISIBLE){
            mTabLayout.setVisibility(View.GONE);
        }
    }

}
