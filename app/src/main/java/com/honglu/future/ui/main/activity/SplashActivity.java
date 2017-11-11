package com.honglu.future.ui.main.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.widget.RelativeLayout;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.base.PermissionsListener;
import com.honglu.future.config.Constant;
import com.honglu.future.dialog.AlertFragmentDialog;
import com.honglu.future.events.LoginNoRefreshUIEvent;
import com.honglu.future.ui.main.contract.SplashContract;
import com.honglu.future.ui.main.guide.GuideActivity;
import com.honglu.future.ui.main.presenter.SplashPresenter;
import com.honglu.future.util.SpUtil;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

/**
 * 启动页
 * xiejingwen
 */
public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashContract.View {

    @BindView(R.id.rl_splash)
    RelativeLayout mRlSplash;
    private boolean isRequesting;//为了避免在onResume中多次请求权限
    private String[] permissions = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_CONTACTS};

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void loadData() {
        //渠道是否过审
        mPresenter.getAudited();

        //新的应用重复启动解决方法
        if (!isTaskRoot()) {
            //判断该Activity是不是任务空间的源Activity,"非"也就是说是被系统重新实例化出来的
            //如果你就放在Launcher Activity中的话，这里可以直接return了
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }
        XGPushClickedResult click = XGPushManager.onActivityStarted(this);
        if (click != null) {
            //从推送通知栏打开-Service打开Activity会重新执行Laucher流程
            //查看是不是全新打开的面板
            if (isTaskRoot()) {
                return;
            }
            //如果有面板存在则关闭当前的面板
            finish();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //if (!isRequesting) {
        mRlSplash.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SpUtil.getInt(Constant.CACHE_IS_FIRST_LOGIN, Constant.HAS_ALREADY_LOGIN) == Constant.HAS_ALREADY_LOGIN) {
                    startActivity(GuideActivity.class);
                    finish();
                } else {
                    startActivity(MainActivity.class);
                    finish();
                }
            }
        }, 800);
        //  isRequesting = true;
        //}
    }

    private PermissionsListener mListener = new PermissionsListener() {
        @Override
        public void onGranted() {
            isRequesting = false;
            if (App.getConfig().isDebug()) {
                startActivity(MainActivity.class);
                //startActivity(UrlSelectorActivity.class);
            } else {
                startActivity(MainActivity.class);
                //为了测试修改过
                //startActivity(UrlSelectorActivity.class);
            }
            finish();
            /*if (SpUtil.getInt(Constant.CACHE_IS_FIRST_LOGIN,Constant.HAS_ALREADY_LOGIN) == Constant.HAS_ALREADY_LOGIN) {
                mLoginOutPresenter.loginOut();
                updateDeviceReport();
                startActivity(GuideActivity.class);
                finish();
            } else {
                if (App.getConfig().isDebug()){
                    startActivity(UrlSelectorActivity.class);
                }else{
                    startActivity(MainActivity.class);
                }
                finish();
            }*/
            EventBus.getDefault().post(new LoginNoRefreshUIEvent(getApplicationContext(), App.getConfig().getUserInfo()));
        }

        @Override
        public void onDenied(List<String> deniedPermissions, boolean isNeverAsk) {
            if (!isNeverAsk) {//请求权限没有全被勾选不再提示然后拒绝
                new AlertFragmentDialog.Builder(mActivity)
                        .setLeftBtnText("退出").setLeftCallBack(new AlertFragmentDialog.LeftClickCallBack() {
                    @Override
                    public void dialogLeftBtnClick() {
                        finish();
                    }
                }).setContent("为了能正常使用\"" + App.getAPPName() + "\"，请授予所需权限")
                        .setRightBtnText("授权").setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                    @Override
                    public void dialogRightBtnClick(String string) {
                        requestPermissions(permissions, mListener);
                    }
                }).build();
            } else {//全被勾选不再提示
                new AlertFragmentDialog.Builder(mActivity)
                        .setLeftBtnText("退出").setLeftCallBack(new AlertFragmentDialog.LeftClickCallBack() {
                    @Override
                    public void dialogLeftBtnClick() {
                        finish();
                    }
                }).setContent("\"" + App.getAPPName() + "\"缺少必要权限\n请手动授予\"" + App.getAPPName() + "\"访问您的权限")
                        .setRightBtnText("授权").setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                    @Override
                    public void dialogRightBtnClick(String string) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                        isRequesting = false;
                    }
                }).build();
            }
        }
    };


}
