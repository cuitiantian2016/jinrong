package com.honglu.future.ui.circle.circlemain;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.base.BasePresenter;
import com.honglu.future.bean.MaidianBean;
import com.honglu.future.config.ConfigUtil;
import com.honglu.future.config.Constant;
import com.honglu.future.events.BBSIndicatorEvent;
import com.honglu.future.events.LoginEvent;
import com.honglu.future.events.RefreshUIEvent;
import com.honglu.future.events.UIBaseEvent;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.http.RxHelper;
import com.honglu.future.ui.circle.bean.SignBean;
import com.honglu.future.ui.circle.bean.TopicFilter;
import com.honglu.future.ui.circle.circlemain.adapter.BBSFragmentAdapter;
import com.honglu.future.ui.circle.circlemine.CircleMineActivity;
import com.honglu.future.ui.circle.publish.PublishActivity;
import com.honglu.future.ui.msg.circlemsg.CircleMsgActivity;
import com.honglu.future.ui.main.activity.MainActivity;
import com.honglu.future.ui.msg.mainmsg.MainMsgActivity;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ViewHelper;
import com.honglu.future.widget.CircleImageView;
import com.honglu.future.widget.CircleSignView;
import com.honglu.future.widget.ExpandableLayout;
import com.honglu.future.widget.SlidingTabImageLayout;
import com.honglu.future.widget.kchart.ViewPagerEx;
import com.honglu.future.widget.popupwind.SignPopWind;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zq on 2017/12/6.
 */
public class CircleMainFragment extends BaseFragment implements CircleSignView.OnSignClickListener {
    public static CircleMainFragment circleMainFragment;
    private String currTopicType;
    private CircleImageView mHeadPortraitIV;
    private View mMessageHintLy;
    private ViewPagerEx mViewPager;
    private View mNQTipLy;
    private TextView mNQTipContentTV;
    private View mRendView;
    @BindView(R.id.ebl_expandable)
    ExpandableLayout mEblLayout;
    @BindView(R.id.ll_sign)
    LinearLayout mFlSign;
    @BindView(R.id.tv_click)
    TextView tv_click;
    @BindView(R.id.tv_sign)
    View tv_sign;
    @BindView(R.id.rl_title)
    View rl_title;
    @BindView(R.id.bg_image)
    View bg_image;
    //是否查询签到 0 没请求/请求失败 1 请求中  2 请求成功
    private int isQuerySignState = 0;
    List<TopicFilter> topicFilters = null;
    private SlidingTabImageLayout mTabsIndicatorLy;
    private boolean isClickSign;
    private SignPopWind signPopWind;

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
        tv_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEblLayout.toggle(true);
                mEblLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mEblLayout.isExpanded()) {
                            tv_click.setText("点击收起");
                        } else {
                            tv_click.setText("点击下拉");
                        }
                    }
                }, 400);
            }
        });
        signPopWind = new SignPopWind(getActivity());
        signPopWind.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                bg_image.setVisibility(View.GONE);
            }
        });
        tv_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!signPopWind.isShowing()) {
                    signPopWind.getSigleData();
                    bg_image.setVisibility(View.VISIBLE);
                    signPopWind.showPopupWind(rl_title);
                }
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCircleThread(UIBaseEvent event) {
        if (event instanceof RefreshUIEvent) {
            int code = ((RefreshUIEvent) event).getType();
            if (code == UIBaseEvent.EVENT_CIRCLE_MSG_RED_VISIBILITY) {
                //红点显示
                if (mRendView.getVisibility() != View.VISIBLE) {
                    mRendView.setVisibility(View.VISIBLE);
                }
            } else if (code == UIBaseEvent.EVENT_CIRCLE_MSG_RED_GONE) {
                getMsgRed();
            } else if (code == UIBaseEvent.EVENT_LOGIN) {//登录
                setAvatar();
                signPopWind.getSigleData();
            } else if (code == UIBaseEvent.EVENT_UPDATE_AVATAR) {//修改头像
                setAvatar();
            }
        }
    }

    private void getMsgRed() {
        HttpManager.getApi().getMsgRed(SpUtil.getString(Constant.CACHE_TAG_UID)).compose(RxHelper.<Boolean>handleSimplyResult()).subscribe(new HttpSubscriber<Boolean>() {
            @Override
            protected void _onNext(Boolean aBoolean) {
                super._onNext(aBoolean);
                if (mRendView != null) {
                    mRendView.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
                }
            }

            @Override
            protected void _onError(String message) {
                super._onError(message);
                if (mRendView != null) {
                    mRendView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setAvatar() {
        ImageUtil.display(ConfigUtil.baseImageUserUrl + SpUtil.getString(Constant.CACHE_USER_AVATAR), mHeadPortraitIV, R.mipmap.img_head);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getMsgRed();
            //getSigleData();
            if (topicFilters == null) {
                getTopicList();
            } else {
                mTabsIndicatorLy.setCurrentTab(0);
                currTopicType = Constant.topic_filter.get(0).type;
                EventBus.getDefault().post(new BBSIndicatorEvent(currTopicType, true));
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
        if (Constant.topic_filter != null) {
            topicFilters = Constant.topic_filter;
            initCircle();
        } else {
            getTopicList();
        }
        mNQTipLy = mView.findViewById(R.id.ly_nq_tip_container);
        mNQTipContentTV = (TextView) mView.findViewById(R.id.tv_nq_bottom_tip_content);
    }

    private BasePresenter<CircleMainFragment> basePresenter;

    private void getTopicList() {
        if (basePresenter == null) {
            basePresenter = new BasePresenter<CircleMainFragment>(CircleMainFragment.this) {
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
                            topicFilters = Constant.topic_filter;
                            initCircle();
                        }
                    });
                }
            };
        }
        basePresenter.getData();
    }

    /*******
     * 将事件交给事件派发controller处理
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoginEvent event) {
        if (topicFilters != null && topicFilters.size() > 0) {
            mTabsIndicatorLy.setCurrentTab(0);
            currTopicType = Constant.topic_filter.get(0).type;
            EventBus.getDefault().post(new BBSIndicatorEvent(currTopicType, true));
        } else {
            getTopicList();
        }
    }

    private void initCircle() {
        if (topicFilters != null && topicFilters.size() > 0) {
            int tabWidth = 0;
            int tabIndicatorWidth = 0;
            if (topicFilters.size() <= 4) {
                int screenWidthDip = DeviceUtils.px2dip(getActivity(), DeviceUtils.getScreenWidth(getActivity())) / topicFilters.size();
                tabWidth = screenWidthDip;
                tabIndicatorWidth = screenWidthDip / 2;
            } else {
                int screenWidthDip = DeviceUtils.px2dip(getActivity(), DeviceUtils.getScreenWidth(getActivity())) / 4;
                tabWidth = screenWidthDip - (screenWidthDip / 2 / 4);
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
                    currTopicType = Constant.topic_filter.get(position).type;
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
            if (topicFilters.size() == 1) {
                mTabsIndicatorLy.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewHelper.setVisibility(mMessageHintLy
                , !TextUtils.isEmpty(SpUtil.getString(Constant.CACHE_TAG_UID)));
        if (!isHidden()) {
            //getSigleData();
            getMsgRed();
        }
    }

    private View.OnClickListener getHeadPortraitClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CircleMineActivity.class);
                intent.putExtra("userId", SpUtil.getString(Constant.CACHE_TAG_UID));
                intent.putExtra("imgHead", SpUtil.getString(Constant.CACHE_USER_AVATAR));
                intent.putExtra("nickName", SpUtil.getString(Constant.CACHE_TAG_USERNAME));
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener getMessageLabelClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DeviceUtils.isFastDoubleClick()){
                    return;
                }
                //隐藏main 红点
                EventBus.getDefault().post(new RefreshUIEvent(UIBaseEvent.EVENT_CIRCLE_MSG_RED_GONE));
                //跳转消息
                startActivity(new Intent(getActivity(), MainMsgActivity.class));
                MaidianBean maidianBean = new MaidianBean();
                maidianBean.page_name = "点击消息中心的数据";
                maidianBean.even_name = "点击消息中心的数据";
                MaidianBean.Data data = new MaidianBean.Data();
                data.buriedName ="点击消息中心的数据";
                data.buriedRemark = "点击消息中心的数据";
                data.key = "qihuo_msgCenter_info";
                maidianBean.data = data;
                MaidianBean.postMaiDian(maidianBean);
            }
        };
    }

    /**
     * 获取签到数据
     */
    private void getSigleData() {
        if (!App.getConfig().getLoginStatus()) {
            return;
        }
        if (isQuerySignState == 0 || isClickSign) {
            mEblLayout.collapse();
        }
        if (isQuerySignState == 2) {
            if (!isClickSign) {
                if (!mEblLayout.isExpanded()) {
                    //创建 签到标签
                    mEblLayout.expand();
                    tv_click.setText("点击收起");
                }
            } else {
                mEblLayout.collapse();
                tv_click.setText("点击下拉");
            }
        }
        //签到
        if (isQuerySignState == 0) {
            isQuerySignState = 1;
            String string = SpUtil.getString(Constant.CACHE_TAG_MOBILE);
            //访问接口
            HttpManager.getApi().getSignData(string).compose(RxHelper.<SignBean>handleSimplyResult()).subscribe(new HttpSubscriber<SignBean>() {
                @Override
                protected void _onNext(SignBean signBean) {
                    super._onNext(signBean);
                    tv_click.setVisibility(View.VISIBLE);
                    CircleMainFragment.this.isQuerySignState = 2;
                    boolean isSign = signBean.isIsSign();
                    isClickSign = isSign;
                    List<SignBean.SignListBean> signList = signBean.getSignList();
                    int signCount = signBean.count;
                    for (int i = 0; i < signList.size(); i++) {
                        if (i < signCount) {
                            SignBean.SignListBean bean = signList.get(i);
                            bean.setSign(true);
                        } else if (i == signCount && !isSign) {
                            SignBean.SignListBean bean = signList.get(i);
                            bean.setSignClick(true);
                        }
                    }
                    if (!isClickSign) {
                        //创建 签到标签
                        mEblLayout.expand();
                        tv_click.setText("点击收起");
                    } else {
                        mEblLayout.collapse();
                        tv_click.setText("点击下拉");
                    }
                    mFlSign.removeAllViews();
                    for (int i = 0; i < signList.size(); i++) {
                        CircleSignView signView = new CircleSignView(CircleMainFragment.this.getActivity(), i, signList.size(), CircleMainFragment.this);
                        signView.setSignData(signList.get(i));
                        mFlSign.addView(signView);
                    }
                }

                @Override
                protected void _onError(String message) {
                    super._onError(message);
                    tv_click.setVisibility(View.GONE);
                    CircleMainFragment.this.isQuerySignState = 0;
                    Log.d("getSigleData", "_onError: " + message);
                }
            });
        }
    }

    @Override
    public void OnSignClick() {
        mEblLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mEblLayout.isExpanded()) {
                    mEblLayout.collapse(true);
                    isClickSign = true;
                    tv_click.setText("点击下拉");
                }
            }
        }, 400);
    }

}
