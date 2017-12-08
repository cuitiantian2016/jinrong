package com.honglu.future.ui.circle.circlemain;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.honglu.future.R;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.config.Constant;
import com.honglu.future.events.BBSIndicatorEvent;
import com.honglu.future.ui.circle.bean.TopicFilter;
import com.honglu.future.ui.circle.circlemain.adapter.BBSFragmentAdapter;
import com.honglu.future.ui.circle.circlemine.CircleMineActivity;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ViewHelper;
import com.honglu.future.widget.CircleImageView;
import com.honglu.future.widget.ExpandableLayout;
import com.honglu.future.widget.SlidingTabImageLayout;
import com.honglu.future.widget.kchart.ViewPagerEx;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zq on 2017/12/6.
 */

public class CircleMainFragment extends BaseFragment {
    public static CircleMainFragment circleMainFragment;
    private String currTopicType;
    private CircleImageView mHeadPortraitIV;
    private View mMessageHintLy;
    private ViewPagerEx mViewPager;
    private View mNQTipLy;
    private TextView mNQTipContentTV;
    private ExpandableLayout mExpandableLy;
    public static CircleMainFragment getInstance() {
        if (circleMainFragment == null) {
            circleMainFragment = new CircleMainFragment();
        }
        return circleMainFragment;
    }
    @Override
    public int getLayoutId() {
        return R.layout.fragment_circle_main;
    }
    @Override
    public void initPresenter() {
    }
    @Override
    public void loadData() {
        initViews();
    }
    private void initViews() {
        mHeadPortraitIV = (CircleImageView) mView.findViewById(R.id.iv_head_portrait);
        mHeadPortraitIV.setOnClickListener(getHeadPortraitClickListener());
        mMessageHintLy = mView.findViewById(R.id.ly_message_hint);
        mMessageHintLy.setOnClickListener(getMessageLabelClickListener());
        mViewPager = (ViewPagerEx) mView.findViewById(R.id.viewPager);
        mViewPager.isEnable(false);
        int screenWidthDip = DeviceUtils.px2dip(getContext(), DeviceUtils.getScreenWidth(getContext()));
        int tabWidth = (int) (screenWidthDip / 4.5f);
        int indicatorWidth = (int) (tabWidth / 1.4f);
        SlidingTabImageLayout mTabsIndicatorLy = (SlidingTabImageLayout) mView.findViewById(R.id.tablayout);
        mTabsIndicatorLy.setTabWidth(tabWidth);
        mTabsIndicatorLy.setIndicatorWidth(indicatorWidth);
        List<TopicFilter> topicFilters;
        if (Constant.topic_filter != null){
            topicFilters= Constant.topic_filter;
        }else {
            topicFilters= Constant.topic_filter;
        }
        if (topicFilters != null && topicFilters.size() > 0) {
            currTopicType = topicFilters.get(0).type;
           List<String> fragmentsTopicType = new ArrayList<>();
            for (TopicFilter topicFilter : topicFilters) {
                BBSClassifyFragment fragment = new BBSClassifyFragment();
                Bundle bundle = new Bundle();
                bundle.putString(BBSClassifyFragment.EXTRA_CLASSIFY_TYPE, topicFilter.type);
                fragment.setArguments(bundle);
                fragmentsTopicType.add(topicFilter.type);
                mTabsIndicatorLy.addNewTab(topicFilter.title, topicFilter.selected_icon, topicFilter.icon);
            }
            mViewPager.setAdapter(new BBSFragmentAdapter(getChildFragmentManager(), fragmentsTopicType));
            mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    currTopicType =Constant.topic_filter.get(position).type;
                    EventBus.getDefault().post(new BBSIndicatorEvent(currTopicType, true));
                }
            });
            mTabsIndicatorLy.setViewPager(mViewPager);
        }
        mNQTipLy = mView.findViewById(R.id.ly_nq_tip_container);
        mNQTipContentTV = (TextView) mView.findViewById(R.id.tv_nq_bottom_tip_content);
        mExpandableLy = (ExpandableLayout) mView.findViewById(R.id.exly_container);

    }
    @Override
    public void onResume() {
        super.onResume();
        ViewHelper.setVisibility(mMessageHintLy
                , !TextUtils.isEmpty(SpUtil.getString(Constant.CACHE_TAG_UID)));
    }
    private View.OnClickListener getHeadPortraitClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CircleMineActivity.class);
            }
        };
    }
    private View.OnClickListener getMessageLabelClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转我的消息
            }
        };
    }
}
