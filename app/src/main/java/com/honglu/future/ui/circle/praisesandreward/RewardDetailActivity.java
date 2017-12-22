package com.honglu.future.ui.circle.praisesandreward;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.widget.kchart.SlidingTabLayout;
import com.honglu.future.widget.tab.CommonTabLayout;
import com.honglu.future.widget.tab.CustomTabEntity;
import com.honglu.future.widget.tab.SimpleOnTabSelectListener;
import com.honglu.future.widget.tab.TabEntity;

import java.util.ArrayList;

import butterknife.BindView;


/**
 * deprecation:打赏详情
 * author:ayb
 * time:2017/6/14
 */
public class RewardDetailActivity extends BaseActivity {
    private static final String KEY_TOPIC = "KEY_TOPIC";
    private static final String KEY_IS_ATTENTION = "KEY_IS_ATTENTION";

    @BindView(R.id.reward_record_tab)
    CommonTabLayout mTabLayout;

    private boolean mIsThumbsList = false;
    private ThumbsListFragment mThumbsListFragment;



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
        mTitle.setTitle(false, R.color.white, "详情");
        ArrayList<Fragment> mFragments = new ArrayList<>();
        ArrayList<CustomTabEntity> mTabList = new ArrayList<>();

        ArewardListFragment mArewardFragment = new ArewardListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ArewardListFragment.CIRCLEID_KEY,getIntent().getStringExtra(KEY_TOPIC));
        mArewardFragment.setArguments(bundle);
        mFragments.add(mArewardFragment);
        mTabList.add(new TabEntity("打赏列表"));

        mThumbsListFragment = new ThumbsListFragment();
        Bundle rewardBundle = new Bundle();
        rewardBundle.putString(ThumbsListFragment.EXTRA_TID,getIntent().getStringExtra(KEY_TOPIC));
        rewardBundle.putString(ThumbsListFragment.EXTRA_TIDE_ID,getIntent().getStringExtra(KEY_IS_ATTENTION));
        mThumbsListFragment.setArguments(rewardBundle);
        mFragments.add(mThumbsListFragment);
        mTabList.add(new TabEntity("点赞列表"));

        mTabLayout.setTabData(mTabList, (FragmentActivity) mContext, R.id.fl_content, mFragments);
        mTabLayout.setOnTabSelectListener(new SimpleOnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                super.onTabSelect(position);
                if (position !=0 && !mIsThumbsList){
                    mIsThumbsList = true;
                    mThumbsListFragment.getFriendList(true);
                }
            }
        });
    }
}
