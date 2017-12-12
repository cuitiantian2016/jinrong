package com.honglu.future.ui.circle.circlemain;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.base.BasePresenter;
import com.honglu.future.config.ConfigUtil;
import com.honglu.future.config.Constant;
import com.honglu.future.events.BBSIndicatorEvent;
import com.honglu.future.events.LoginEvent;
import com.honglu.future.events.RefreshUIEvent;
import com.honglu.future.events.UIBaseEvent;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.circle.bean.TopicFilter;
import com.honglu.future.ui.circle.circlemain.adapter.BBSFragmentAdapter;
import com.honglu.future.ui.circle.circlemine.CircleMineActivity;
import com.honglu.future.ui.circle.circlemsg.CircleMsgActivity;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ViewHelper;
import com.honglu.future.widget.CircleImageView;
import com.honglu.future.widget.SlidingTabImageLayout;
import com.honglu.future.widget.kchart.ViewPagerEx;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
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
    private View mRendView;
    List<TopicFilter> topicFilters = null;
    private SlidingTabImageLayout mTabsIndicatorLy;

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
        EventBus.getDefault().register(this);
        initViews();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCircleThread(UIBaseEvent event) {
        if (event instanceof RefreshUIEvent) {
            int code = ((RefreshUIEvent) event).getType();
            if (code == UIBaseEvent.EVENT_CIRCLE_MSG_CIRCLE_RED){
               //红点显示
                mRendView.setVisibility(View.VISIBLE);
                String headUrl = event.getMessage();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void initViews() {
        mRendView = mView.findViewById(R.id.v_red);
        mRendView.setVisibility(View.INVISIBLE);
        mHeadPortraitIV = (CircleImageView) mView.findViewById(R.id.iv_head_portrait);
        mHeadPortraitIV.setOnClickListener(getHeadPortraitClickListener());
        ImageUtil.display(ConfigUtil.baseImageUserUrl + SpUtil.getString(Constant.CACHE_USER_AVATAR), mHeadPortraitIV, R.mipmap.img_head);
        mMessageHintLy = mView.findViewById(R.id.ly_message_hint);
        mMessageHintLy.setOnClickListener(getMessageLabelClickListener());
        mViewPager = (ViewPagerEx) mView.findViewById(R.id.viewPager);
        mViewPager.isEnable(false);
        mTabsIndicatorLy = (SlidingTabImageLayout) mView.findViewById(R.id.tablayout);
        if (Constant.topic_filter != null){
            topicFilters= Constant.topic_filter;
            initCircle();
        }else {
            getTopicList();
        }
        mNQTipLy = mView.findViewById(R.id.ly_nq_tip_container);
        mNQTipContentTV = (TextView) mView.findViewById(R.id.tv_nq_bottom_tip_content);
    }

    private void getTopicList(){
        new BasePresenter<CircleMainFragment>(CircleMainFragment.this){
            @Override
            public void getData() {
                super.getData();
                toSubscribe(HttpManager.getApi().getTopicFilter(), new HttpSubscriber<List<TopicFilter>>() {
                    @Override
                    protected void _onError(String message, int code) {
                    }
                    @Override
                    protected void _onNext(List<TopicFilter> bean) {
                        Constant.topic_filter = bean;
                        topicFilters= Constant.topic_filter;
                        initCircle();
                    }
                });
            }
        };
    }
    /*******
     * 将事件交给事件派发controller处理
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoginEvent event) {
       if (topicFilters!=null&&topicFilters.size()>0){
           mTabsIndicatorLy.setCurrentTab(0);
       }
    }

    private void initCircle(){
        if (topicFilters != null && topicFilters.size() > 0) {
            int tabWidth = 0;
            int tabIndicatorWidth = 0;
            if (topicFilters.size() <= 4){
                int screenWidthDip = DeviceUtils.px2dip(getActivity(), DeviceUtils.getScreenWidth(getActivity())) / topicFilters.size();
                tabWidth = screenWidthDip;
                tabIndicatorWidth = screenWidthDip / 2;
            }else {
                int screenWidthDip = DeviceUtils.px2dip(getActivity(), DeviceUtils.getScreenWidth(getActivity())) / 4;
                tabWidth = screenWidthDip - (screenWidthDip / 2  / 4);
                tabIndicatorWidth = (int) (tabWidth / 1.5);
            }
            mTabsIndicatorLy.setTabWidth(tabWidth);
            mTabsIndicatorLy.setIndicatorWidth(tabIndicatorWidth);
            currTopicType = topicFilters.get(0).type;
            List<String> fragmentsTopicType = new ArrayList<>();
            for (TopicFilter topicFilter : topicFilters) {
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
            mTabsIndicatorLy.setOnTabClickListener(new SlidingTabImageLayout.OnTabClickListener() {
                @Override
                public boolean onClickProcess(View view, int position) {
                    return false;
                }
            });
            if (topicFilters.size()==1){
                mTabsIndicatorLy.setVisibility(View.GONE);
            }
        }
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
                Intent intent = new Intent(mContext,CircleMineActivity.class);
                intent.putExtra("userId",SpUtil.getString(Constant.CACHE_TAG_UID));
                intent.putExtra("imgHead",SpUtil.getString(Constant.CACHE_USER_AVATAR));
                intent.putExtra("nickName",SpUtil.getString(Constant.CACHE_TAG_USERNAME));
                startActivity(intent);
            }
        };
    }
    private View.OnClickListener getMessageLabelClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRendView.setVisibility(View.INVISIBLE);
                //隐藏main 红点
                EventBus.getDefault().post(new RefreshUIEvent(UIBaseEvent.EVENT_CIRCLE_MSG));
                //跳转消息
                startActivity(new Intent(getActivity(), CircleMsgActivity.class));
            }
        };
    }
}
