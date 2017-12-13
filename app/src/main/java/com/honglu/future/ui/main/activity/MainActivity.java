package com.honglu.future.ui.main.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.honglu.future.ARouter.DebugActivity;
import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.app.AppManager;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.bean.ActivityPopupBean;
import com.honglu.future.bean.UpdateBean;
import com.honglu.future.config.Constant;
import com.honglu.future.dialog.ActivityFragmentDialog;
import com.honglu.future.events.ChangeTabEvent;
import com.honglu.future.events.ChangeTabMainEvent;
import com.honglu.future.events.FragmentRefreshEvent;
import com.honglu.future.events.RefreshUIEvent;
import com.honglu.future.events.UIBaseEvent;
import com.honglu.future.ui.circle.publish.PublishActivity;
import com.honglu.future.ui.login.activity.LoginActivity;
import com.honglu.future.ui.main.FragmentFactory;
import com.honglu.future.ui.main.contract.ActivityContract;
import com.honglu.future.ui.main.presenter.ActivityPresenter;
import com.honglu.future.util.AppUtils;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ToastUtil;
import com.honglu.future.widget.MyRadioButton;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;

/**
 * 主界面
 * zq
 */
@Route(path = "/future/main")
public class MainActivity extends BaseActivity<ActivityPresenter> implements ActivityContract.View {
    @BindView(R.id.container)
    FrameLayout mContainer;
    @BindView(R.id.group)
    RadioGroup mGroup;
    @BindView(R.id.rb_circle)
    MyRadioButton mRbCircle;
    //哞一下布局
    @BindView(R.id.tv_mom_outline)
    RelativeLayout mMomOutLy;
    @Autowired(name = "select")
    public int select;
    //平均宽度
    private int mAverageWidth;
    //哞一下宽度
    private int mMomWidth;
    //哞一下位置
    private int mMomPosition = 2;
    private FragmentFactory.FragmentStatus toTabIndex = FragmentFactory.FragmentStatus.None;
    private int oldCheckId = 0;
    private Handler mHandler = new Handler();
    private Runnable mRunable = new Runnable() {
        @Override
        public void run() {
            if (mChangeTabType == 1) {
                EventBus.getDefault().post(new RefreshUIEvent(UIBaseEvent.EVENT_HOME_TO_MARKET_ZHULI));
            } else if (mChangeTabType == 2) {
                EventBus.getDefault().post(new ChangeTabEvent(1));
            }
        }
    };

    private int mChangeTabType = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            savedInstanceState = null;
        }
        super.onCreate(savedInstanceState);
        JPushInterface.setAlias(this, SpUtil.getString(Constant.CACHE_TAG_UID), null);
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        App.mApp.setIsMainDestroy(false);
        showPopupWindow();
        EventBus.getDefault().register(this);
        mGroup.setOnCheckedChangeListener(changeListener);
        // check(FragmentFactory.FragmentStatus.Home);
        select(getIntent());
        mMomOutLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DeviceUtils.isFastDoubleClick()) return;
                startActivity(PublishActivity.class);
            }
        });
        mAverageWidth = DeviceUtils.getScreenWidth(mContext) / 5;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mMomOutLy.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mMomWidth = mMomOutLy.getMeasuredWidth();
                    if (mMomWidth > 0) {
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        params.leftMargin = (int) ((mMomPosition + 0.5) * mAverageWidth - mMomWidth / 2);
                        mMomOutLy.setLayoutParams(params);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mMomOutLy.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            });
        } else {
            mMomWidth = DeviceUtils.getScreenWidth(mContext) / 5;
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.leftMargin = (int) ((mMomPosition + 0.5) * mAverageWidth - mMomWidth / 2);
            mMomOutLy.setLayoutParams(params);
        }
        //mPresenter.loadActivity(); //第一期不需要弹出活动
        mPresenter.getUpdateVersion();
    }

    //tab切换动画
    private void startChangedImageAnim(FragmentFactory.FragmentStatus status) {
        if (status == FragmentFactory.FragmentStatus.Circle) {
            //牛圈哞一下
            Animation animation = AnimationUtils.loadAnimation(mActivity, R.anim.tv_mom_anim_in);
            mMomOutLy.startAnimation(animation);
            mMomOutLy.setVisibility(View.VISIBLE);
        } else {
            if (mMomOutLy.isShown()) {
                Animation animation = AnimationUtils.loadAnimation(mActivity, R.anim.tv_mom_anim_out);
                mMomOutLy.startAnimation(animation);
            }
            mMomOutLy.setVisibility(View.INVISIBLE);
        }

    }

    private void select(Intent intent) {
        Log.d("Tag", "select-->" + select);
        if (intent != null) {
            select = intent.getIntExtra("select", 0);
        }
        if (this.select == 0) {
            MobclickAgent.onEvent(mContext, "shouye_anniu_click", "首页");
            check(FragmentFactory.FragmentStatus.Home);
        } else if (this.select == 1) {
            MobclickAgent.onEvent(mContext, "shouye_hangqing_click", "行情");
            check(FragmentFactory.FragmentStatus.Market);
        } else if (this.select == 2) {
            MobclickAgent.onEvent(mContext, "shouye_quanzi_click", "牛圈");
            check(FragmentFactory.FragmentStatus.Circle);
        } else if (this.select == 3) {
            MobclickAgent.onEvent(mContext, "shouye_jiaoyi_click", "交易");
            check(FragmentFactory.FragmentStatus.Trade);
        } else if (this.select == 4) {
            MobclickAgent.onEvent(mContext, "shouye_wode_click", "我的");
            check(FragmentFactory.FragmentStatus.Account);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        select(intent);
    }

    public void check(FragmentFactory.FragmentStatus status) {
        mGroup.check(getCheckIdByStatus(status));
    }

    @Override
    protected void onResume() {
        super.onResume();
        oldCheckId = 0;
        mGroup.setOnCheckedChangeListener(changeListener);
    }

    OnCheckedChangeListener changeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_home:
                    toTabIndex = FragmentFactory.FragmentStatus.Home;
                    changeTab(FragmentFactory.FragmentStatus.Home);
                    /*Logs.e("首页111");*/
                    oldCheckId = R.id.rb_home;
                    break;
                case R.id.rb_market:
                    toTabIndex = FragmentFactory.FragmentStatus.Market;
                    oldCheckId = R.id.rb_market;
                    changeTab(FragmentFactory.FragmentStatus.Market);
                    break;
                case R.id.rb_trade:
                    toTabIndex = FragmentFactory.FragmentStatus.Trade;
                    oldCheckId = R.id.rb_trade;
                    changeTab(FragmentFactory.FragmentStatus.Trade);
                    break;
                case R.id.rb_circle:
                    toTabIndex = FragmentFactory.FragmentStatus.Circle;
                    oldCheckId = R.id.rb_circle;
                    changeTab(FragmentFactory.FragmentStatus.Circle);
                    break;
                case R.id.rb_account:
//                    StatusBarUtils.setTranslucentForImageViewInFragment(MainActivity.this, 0, null);
                    toTabIndex = FragmentFactory.FragmentStatus.Account;
                    oldCheckId = R.id.rb_account;
                    changeTab(FragmentFactory.FragmentStatus.Account);
                    break;

                default:
                    break;

            }
        }
    };


    /***********
     * eventBus 监听
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UIBaseEvent event) {
        if (event instanceof RefreshUIEvent) {
            int code = ((RefreshUIEvent) event).getType();
            if (code == UIBaseEvent.EVENT_LOGIN)//登录
            {
                JPushInterface.setAlias(this, SpUtil.getString(Constant.CACHE_TAG_UID), null);
                if (toTabIndex != odlState)//切换
                {
                    changeTab(toTabIndex);
                    ((RadioButton) findViewById(getCheckIdByStatus(toTabIndex))).setChecked(true);
                }
                EventBus.getDefault().post(new FragmentRefreshEvent(code));
            } else if (code == UIBaseEvent.EVENT_LOGOUT) {
                JPushInterface.setAlias(this, SpUtil.getString(Constant.CACHE_TAG_UID), null);
                //默认到首页
                changeTab(FragmentFactory.FragmentStatus.Home);
                ((RadioButton) findViewById(getCheckIdByStatus(FragmentFactory.FragmentStatus.Home))).setChecked(true);
                EventBus.getDefault().post(new FragmentRefreshEvent(code));
            } else if (code == UIBaseEvent.EVENT_LOAN_SUCCESS) {
                EventBus.getDefault().post(new FragmentRefreshEvent(code));

            } else if (code == UIBaseEvent.EVENT_CIRCLE_MSG_RED_VISIBILITY) {//红点显示
                int tag = mRbCircle.getTag() != null ? (Integer) mRbCircle.getTag() : 0;
                if (tag != 1) {// 1显示红点
                    mRbCircle.setTag(1);
                    mRbCircle.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.icon_menu_5_normal_red), null, null);
                }
            } else if (code == UIBaseEvent.EVENT_CIRCLE_MSG_RED_GONE) {//红点隐藏
                int tag = mRbCircle.getTag() != null ? (Integer) mRbCircle.getTag() : 0;
                if (tag != 2) {
                    mRbCircle.setTag(2);
                    mRbCircle.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.icon_menu_5_normal), null, null);
                }
            }
        } else if (event instanceof ChangeTabMainEvent) {
            if (((ChangeTabMainEvent) event).getTab().equals(FragmentFactory.FragmentStatus.Trade)) {
                mChangeTabType = 2;
                mHandler.postDelayed(mRunable, 300);
            } else if (((ChangeTabMainEvent) event).getTab().equals(FragmentFactory.FragmentStatus.Market)) {
                mChangeTabType = 1;
                mHandler.postDelayed(mRunable, 300);
            }
            changeTab(((ChangeTabMainEvent) event).getTab());
            ((RadioButton) findViewById(getCheckIdByStatus(((ChangeTabMainEvent) event).getTab()))).setChecked(true);
        }
    }


    /***********
     * 获取所选状态的checkId
     *
     * @return
     */
    public int getCheckIdByStatus(FragmentFactory.FragmentStatus status) {
        int id = R.id.rb_home;
        switch (status) {
            case Home:
                id = R.id.rb_home;
                break;
            case Market:
                id = R.id.rb_market;
                break;
            case Trade:
                id = R.id.rb_trade;
                break;
            case Circle:
                id = R.id.rb_circle;
                break;
            case Account:
                id = R.id.rb_account;
                break;
            default:
                break;
        }

        return id;
    }

    /***********
     * 切换导航栏
     */
    private FragmentFactory.FragmentStatus odlState = FragmentFactory.FragmentStatus.None;

    public void changeTab(FragmentFactory.FragmentStatus status) {
        if (status == odlState)
            return;
        if (status == FragmentFactory.FragmentStatus.Circle && TextUtils.isEmpty(SpUtil.getString(Constant.CACHE_TAG_UID))) {
            startActivity(LoginActivity.class);
            return;
        }
        FragmentFactory.changeFragment(getSupportFragmentManager(), status, R.id.container);
        odlState = status;
        startChangedImageAnim(status);
    }


    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtil.showToast("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.mApp.setIsMainDestroy(true);
        EventBus.getDefault().unregister(this);
        AppManager.getInstance().AppExit(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showLoading(String content) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorMsg(String msg, String type) {

    }

    @Override
    public void loadActivitySuccess(ActivityPopupBean result) {
//        if ("0".equals(result.getTcStatus())) {//0为显示，其他为不显示
//            //缓存图片url与点击url。每个活动只弹出一次
//            String imgUrl = SpUtil.getString(Constant.CACHE_TAG_ACTIVITY_IMGURL);
//            String url = SpUtil.getString(Constant.CACHE_TAG_ACTIVITY_URL);
//            if (!result.getTcImage().equals(imgUrl) || !result.getTcUrl().contains(url)) {
//                SpUtil.putString(Constant.CACHE_TAG_ACTIVITY_IMGURL, result.getTcImage());
//                SpUtil.putString(Constant.CACHE_TAG_ACTIVITY_URL, result.getTcUrl());
        ActivityFragmentDialog.newInstance(result.image, result.url)
                .show(getSupportFragmentManager(), ActivityFragmentDialog.TAG);
//            }
//        }
    }

    @Override
    public void getUpdateVersionSuccess(UpdateBean bean) {
        boolean isForced = false;
        if ("1".equals(bean.getChangeProperties())) {
            isForced = true;
        }
        if ("1".equals(bean.getPopup())) {
            String title = "当前版本:" + bean.getOldVersionNumber() + ",最新版本:" + bean.getVersionNumber();
            ezy.boost.update.UpdateHelper.getInstance().update(
                    this,
                    isForced,
                    title,
                    bean.getChangeDesc(),
                    AppUtils.getVersionCode(),
                    bean.getVersionNumber(),
                    bean.getDownloadUrl(),
                    bean.getMd5());
        }
    }

    /**
     * 显示弹出框
     */
    public void showPopupWindow() {
        if (!com.honglu.future.BuildConfig.DEBUG) {
            return;
        }
        // 获取WindowManager
        final WindowManager mWindowManager = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        Button button = new Button(getApplicationContext());
        button.setText("测试页面");
        button.setTextColor(getResources().getColor(R.color.white));
        button.setBackgroundColor(getResources().getColor(R.color.actionsheet_blue));
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        // 类型
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        // WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        // 设置flag
        int flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        // | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 如果设置了WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE，弹出的View收不到Back键的事件
        params.flags = flags;
        // 不设置这个弹出框的透明遮罩显示为黑色
        params.format = PixelFormat.TRANSLUCENT;
        // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
        // 设置 FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
        // 不设置这个flag的话，home页的划屏会有问题
        params.width = DeviceUtils.dip2px(this, 80);
        params.height = DeviceUtils.dip2px(this, 40);
        params.gravity = Gravity.LEFT;
        mWindowManager.addView(button, params);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(DebugActivity.class);
            }
        });
    }

    public int getReadTag() {
        return mRbCircle.getTag() != null ? (Integer) mRbCircle.getTag() : 0;
    }
}
