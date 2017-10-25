package com.honglu.future.ui.main.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.app.AppManager;
import com.honglu.future.app.JPushManager;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.config.Constant;
import com.honglu.future.dialog.ActivityFragmentDialog;
import com.honglu.future.events.ChangeTabMainEvent;
import com.honglu.future.events.FragmentRefreshEvent;
import com.honglu.future.events.RefreshUIEvent;
import com.honglu.future.events.UIBaseEvent;
import com.honglu.future.ui.home.fragment.HomeFragment;
import com.honglu.future.ui.main.FragmentFactory;
import com.honglu.future.ui.main.bean.ActivityBean;
import com.honglu.future.ui.main.contract.ActivityContract;
import com.honglu.future.ui.main.presenter.ActivityPresenter;
import com.honglu.future.ui.market.fragment.MarketFragment;
import com.honglu.future.ui.register.activity.RegisterActivity;
import com.honglu.future.ui.trade.fragment.TradeFragment;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.LogUtils;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.StatusBarUtils;
import com.honglu.future.util.StringUtil;
import com.honglu.future.util.ToastUtil;
import com.honglu.future.util.UDeskUtils;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * 主界面
 * zq
 */
public class MainActivity extends BaseActivity<ActivityPresenter> implements ActivityContract.View {
    @BindView(R.id.container)
    FrameLayout mContainer;
    @BindView(R.id.group)
    RadioGroup mGroup;
    private FragmentFactory.FragmentStatus toTabIndex = FragmentFactory.FragmentStatus.None;
    private int oldCheckId = 0;

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
        JPushManager.get().registerJPushAlias();
        UDeskUtils.getInstance().init(MainActivity.this);
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        EventBus.getDefault().register(this);
        mGroup.setOnCheckedChangeListener(changeListener);
        check(FragmentFactory.FragmentStatus.Home);
        //mPresenter.loadActivity(); //第一期不需要弹出活动
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
                setPaddingAndFillStatusBar(HomeFragment.getInstance());
                break;
            case R.id.rb_market:
                setPaddingAndFillStatusBar(MarketFragment.getInstance());
            case R.id.rb_trade:
                setPaddingAndFillStatusBar(TradeFragment.getInstance());
                break;
            case R.id.rb_account:
                StatusBarUtils.setTranslucentForImageViewInFragment(MainActivity.this, 0, null);
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
                    toTabIndex = FragmentFactory.FragmentStatus.Account;
                    oldCheckId = R.id.rb_account;
                    changeTab(FragmentFactory.FragmentStatus.Account);
                    break;

                default:
                    break;

            }
        }
    };

    /**
     * 设置 Fragment 根布局间距预留出状态栏高度
     */
    public void setPaddingAndFillStatusBar(Fragment fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View contentView = findViewById(android.R.id.content);
            if (contentView != null) {
                ViewGroup rootView;
                rootView = (ViewGroup) ((ViewGroup) contentView).getChildAt(0);
                if (rootView.getPaddingTop() != 0) {
                    rootView.setPadding(0, 0, 0, 0);
                }
            }
            if (fragment.getView() != null)
                fragment.getView().setPadding(0, DeviceUtils.getStatusBarHeight(MainActivity.this), 0, 0);
        }
        StatusBarUtils.setColor(MainActivity.this, getResources().getColor(R.color.black), 255);
    }

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
                JPushManager.get().registerJPushAlias();
                if (toTabIndex != odlState)//切换
                {
                    changeTab(toTabIndex);
                    ((RadioButton) findViewById(getCheckIdByStatus(toTabIndex))).setChecked(true);
                }
                EventBus.getDefault().post(new FragmentRefreshEvent(code));
            } else if (code == UIBaseEvent.EVENT_LOGOUT) {
                JPushManager.get().registerJPushAlias();
                //默认到首页
                changeTab(FragmentFactory.FragmentStatus.Home);
                ((RadioButton) findViewById(getCheckIdByStatus(FragmentFactory.FragmentStatus.Home))).setChecked(true);
                EventBus.getDefault().post(new FragmentRefreshEvent(code));
            } else if (code == UIBaseEvent.EVENT_LOAN_SUCCESS) {
                EventBus.getDefault().post(new FragmentRefreshEvent(code));
            }
        } else if (event instanceof ChangeTabMainEvent) {
            changeTab(((ChangeTabMainEvent) event).getTab());
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
}
