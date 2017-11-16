package com.honglu.future.ui.main.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.honglu.future.ARouter.DebugActivity;
import com.honglu.future.R;
import com.honglu.future.app.AppManager;
import com.honglu.future.app.JPushManager;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.bean.UpdateBean;
import com.honglu.future.config.Constant;
import com.honglu.future.dialog.ActivityFragmentDialog;
import com.honglu.future.events.ChangeTabEvent;
import com.honglu.future.events.ChangeTabMainEvent;
import com.honglu.future.events.FragmentRefreshEvent;
import com.honglu.future.events.RefreshUIEvent;
import com.honglu.future.events.UIBaseEvent;
import com.honglu.future.ui.main.FragmentFactory;
import com.honglu.future.ui.main.bean.ActivityBean;
import com.honglu.future.ui.main.contract.ActivityContract;
import com.honglu.future.ui.main.presenter.ActivityPresenter;
import com.honglu.future.ui.register.activity.RegisterActivity;
import com.honglu.future.util.AppUtils;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.LogUtils;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.StringUtil;
import com.honglu.future.util.ToastUtil;
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
    @Autowired(name="select")
    public int select;
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
        JPushInterface.setAlias(this, SpUtil.getString(Constant.CACHE_TAG_MOBILE), null);
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        showPopupWindow();
        EventBus.getDefault().register(this);
        mGroup.setOnCheckedChangeListener(changeListener);
        // check(FragmentFactory.FragmentStatus.Home);
        select(getIntent());
        //mPresenter.loadActivity(); //第一期不需要弹出活动
        mPresenter.getUpdateVersion();
    }

    private void select(Intent intent) {
        Log.d("Tag", "select-->" + select);
        if (intent!=null){
            select = intent.getIntExtra("select",0);
        }
        if (this.select == 0) {
            check(FragmentFactory.FragmentStatus.Home);
        } else if (this.select == 1) {
            check(FragmentFactory.FragmentStatus.Market);
        } else if (this.select == 2) {
            check(FragmentFactory.FragmentStatus.Trade);
        } else if (this.select == 3) {
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
        switch (oldCheckId) {
            case R.id.rb_home:
                //setPaddingAndFillStatusBar(HomeFragment.getInstance());
                break;
            case R.id.rb_market:
                //setPaddingAndFillStatusBar(MarketFragment.getInstance());
            case R.id.rb_trade:
                //setPaddingAndFillStatusBar(TradeFragment.getInstance());
                break;
            case R.id.rb_account:
                //StatusBarUtils.setTranslucentForImageViewInFragment(MainActivity.this, 0, null);
                break;
            default:
                break;
        }
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

//    /**
//     * 设置 Fragment 根布局间距预留出状态栏高度
//     */
//    public void setPaddingAndFillStatusBar(Fragment fragment) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            View contentView = findViewById(android.R.id.content);
//            if (contentView != null) {
//                ViewGroup rootView;
//                rootView = (ViewGroup) ((ViewGroup) contentView).getChildAt(0);
//                if (rootView.getPaddingTop() != 0) {
//                    rootView.setPadding(0, 0, 0, 0);
//                }
//            }
//            if (fragment.getView() != null)
//                fragment.getView().setPadding(0, DeviceUtils.getStatusBarHeight(MainActivity.this), 0, 0);
//        }
//        StatusBarUtils.setColor(MainActivity.this, getResources().getColor(R.color.black), 255);
//    }

    private void toLogin() {
        mGroup.setOnCheckedChangeListener(null);
        ((RadioButton) findViewById(oldCheckId)).setChecked(true);
        String uName = SpUtil.getString(Constant.CACHE_TAG_USERNAME);
        LogUtils.loge(uName);
        if (!StringUtil.isBlank(uName) && StringUtil.isMobileNO(uName)) {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            intent.putExtra("phone", uName);
            startActivity(intent);
            /*Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("tag", StringUtil.changeMobile(uName));
            intent.putExtra("phone", uName);
            startActivity(intent);*/
        } else {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
    }

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
                JPushInterface.setAlias(this, SpUtil.getString(Constant.CACHE_TAG_MOBILE), null);
                if (toTabIndex != odlState)//切换
                {
                    changeTab(toTabIndex);
                    ((RadioButton) findViewById(getCheckIdByStatus(toTabIndex))).setChecked(true);
                }
                EventBus.getDefault().post(new FragmentRefreshEvent(code));
            } else if (code == UIBaseEvent.EVENT_LOGOUT) {
                JPushInterface.setAlias(this, SpUtil.getString(Constant.CACHE_TAG_MOBILE), null);
                //默认到首页
                changeTab(FragmentFactory.FragmentStatus.Home);
                ((RadioButton) findViewById(getCheckIdByStatus(FragmentFactory.FragmentStatus.Home))).setChecked(true);
                EventBus.getDefault().post(new FragmentRefreshEvent(code));
            } else if (code == UIBaseEvent.EVENT_LOAN_SUCCESS) {
                EventBus.getDefault().post(new FragmentRefreshEvent(code));
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
        FragmentFactory.changeFragment(getSupportFragmentManager(), status, R.id.container);
        odlState = status;
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

    public RadioGroup getGroup() {
        return mGroup;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //MobclickAgent.onKillProcess(this);
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
    public void loadActivitySuccess(ActivityBean result) {
        if ("0".equals(result.getTcStatus())) {//0为显示，其他为不显示
            //缓存图片url与点击url。每个活动只弹出一次
            String imgUrl = SpUtil.getString(Constant.CACHE_TAG_ACTIVITY_IMGURL);
            String url = SpUtil.getString(Constant.CACHE_TAG_ACTIVITY_URL);
            if (!result.getTcImage().equals(imgUrl) || !result.getTcUrl().contains(url)) {
                SpUtil.putString(Constant.CACHE_TAG_ACTIVITY_IMGURL, result.getTcImage());
                SpUtil.putString(Constant.CACHE_TAG_ACTIVITY_URL, result.getTcUrl());
                ActivityFragmentDialog.newInstance(result.getTcImage(), result.getTcUrl())
                        .show(getSupportFragmentManager(), ActivityFragmentDialog.TAG);
            }
        }
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
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
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
}
