package com.honglu.future.ui.usercenter.fragment;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.base.PermissionsListener;
import com.honglu.future.config.ConfigUtil;
import com.honglu.future.config.Constant;
import com.honglu.future.dialog.AlertFragmentDialog;
import com.honglu.future.dialog.BillConfirmDialog;
import com.honglu.future.events.FragmentRefreshEvent;
import com.honglu.future.events.RefreshUIEvent;
import com.honglu.future.events.UIBaseEvent;
import com.honglu.future.ui.login.activity.LoginActivity;
import com.honglu.future.ui.main.CheckAccount;
import com.honglu.future.ui.main.activity.WebViewActivity;
import com.honglu.future.ui.main.presenter.AccountPresenter;
import com.honglu.future.ui.msg.mainmsg.MainMsgActivity;
import com.honglu.future.ui.usercenter.activity.ModifyUserActivity;
import com.honglu.future.ui.usercenter.contract.UserCenterContract;
import com.honglu.future.ui.usercenter.presenter.UserCenterPresenter;
import com.honglu.future.util.AndroidUtil;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.util.LogUtils;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.StringUtil;
import com.honglu.future.util.Tool;
import com.honglu.future.widget.CircleImageView;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.honglu.future.util.ToastUtil.showToast;

/**
 * Created by zq on 2017/10/24.
 */

public class UserCenterFragment extends BaseFragment<UserCenterPresenter> implements
        UserCenterContract.View, BillConfirmDialog.OnConfirmClickListener {

    @BindView(R.id.tv_loginRegister)
    TextView mLoginRegister;
    @BindView(R.id.iv_head)
    CircleImageView mHead;
    @BindView(R.id.tv_mobphone)
    TextView mMobphone;
    @BindView(R.id.tv_open_account)
    TextView mOpenAccount;
    @BindView(R.id.tv_novice)
    TextView mNovice;
    @BindView(R.id.tv_kefu)
    TextView mKefu;
    @BindView(R.id.tv_phone)
    TextView mPhone;
    @BindView(R.id.tv_aboutus)
    TextView mAboutus;
    @BindView(R.id.img_vip)
    ImageView mViper;
    @BindView(R.id.v_red)
    View mReadMsg;

    private AccountPresenter mAccountPresenter;
    private BillConfirmDialog billConfirmDialog;
    private CheckAccount mCheckAccount;
    public static UserCenterFragment userCenterFragment;

    public static UserCenterFragment getInstance() {
        if (userCenterFragment == null) {
            userCenterFragment = new UserCenterFragment();
        }
        return userCenterFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_user_center_layout;
    }

    @Override
    public void showLoading(String content) {
        if (!TextUtils.isEmpty(content)) {
            App.loadingContent(mActivity, content);
        }
    }

    @Override
    public void stopLoading() {
        App.hideLoading();
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        showToast(msg);
    }


    @Override
    public void initPresenter() {
        mPresenter.init(this);
        mAccountPresenter = new AccountPresenter();
    }



    @Override
    public void loadData() {
        EventBus.getDefault().register(this);
        mCheckAccount = new CheckAccount(mContext);
        mReadMsg.setVisibility(View.INVISIBLE);
        if (App.getConfig().getLoginStatus()) {
            setViperVisible();
            mMobphone.setVisibility(View.VISIBLE);
            mLoginRegister.setVisibility(View.GONE);
            mMobphone.setText(SpUtil.getString(Constant.CACHE_TAG_USERNAME));
            setAvatar(ConfigUtil.baseImageUserUrl + SpUtil.getString(Constant.CACHE_USER_AVATAR));
            mReadMsg.setVisibility(View.INVISIBLE);
        }else {
            mMobphone.setVisibility(View.GONE);
            mLoginRegister.setVisibility(View.VISIBLE);
            mReadMsg.setVisibility(View.INVISIBLE);
            setAvatar("");
        }


    }

    private void setAvatar(String headUrl) {
        ImageUtil.display(headUrl, mHead, R.mipmap.img_head);
    }

    @OnClick({R.id.fl_config, R.id.tv_novice, R.id.tv_open_account,R.id.tv_kefu, R.id.tv_phone, R.id.tv_aboutus,
             R.id.iv_setup, R.id.img_vip , R.id.tv_shop_mall,R.id.tv_task,R.id.rl_message_hint})
    public void onClick(View view) {
        if (Tool.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.tv_shop_mall: //牛币商城
                if (!App.getConfig().getLoginStatus()) {
                    Intent loginActivity = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(loginActivity);
                }else {
                    Intent intentShopMall = new Intent(mActivity, WebViewActivity.class);
                    intentShopMall.putExtra("url", ConfigUtil.SHOP_MALL);
                    intentShopMall.putExtra("title", "牛币商城");
                    intentShopMall.putExtra("is_tool_bar", false);
                    startActivity(intentShopMall);
                }
                break;
            case R.id.tv_task://任务中心

                break;
            case R.id.tv_novice:
                clickTab("wode_xinshourumen_click","我的_新手入门");
                Intent intentTeach = new Intent(mActivity, WebViewActivity.class);
                intentTeach.putExtra("title", "新手教学");
                intentTeach.putExtra("url", ConfigUtil.NEW_USER_TEACH);
                startActivity(intentTeach);
                break;

            case R.id.tv_open_account:
                if (App.getConfig().getLoginStatus()){
                    clickTab("wode_lijikaihu_click","我的_立即开户");
                    mCheckAccount.checkAccount();
                } else{
                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                }
                break;
            case R.id.tv_kefu:
                clickTab("wode_zaixiankefu_click","我的_在线客服");
                AndroidUtil.startKF(getActivity());
                break;
            case R.id.tv_phone:
                //联系客服
                if (!DeviceUtils.isFastDoubleClick()) {
                    clickTab("wode_kefudianhua_click","我的_客服电话");
                    showCallPhoneDialog();
                }
                break;
            case R.id.tv_aboutus:
                clickTab("wode_guanyuwomen_click","我的_关于我们");
                Intent intentAbout = new Intent(mActivity, WebViewActivity.class);
                intentAbout.putExtra("url", ConfigUtil.ABOUT_US);
                intentAbout.putExtra("title", "关于我们");
                startActivity(intentAbout);
                break;
            case R.id.iv_setup:
                clickTab("wode_shezhi_click","我的_设置");
                if (App.getConfig().getLoginStatus()) {
                    startActivity(ModifyUserActivity.class);
                } else {
                    toLogin();
                }
                break;
            case R.id.img_vip:
                new AlertFragmentDialog.Builder(mActivity)
                        .setLeftBtnText("取消").setContent("享有官方活动优先参与权\n" +
                        "享受积分商城的兑换打折优惠\n" +
                        "更多敬请期待").setTitle("智投先锋特权")
                        .setTitleRightImage(R.mipmap.ic_pop_vip)
                        .setRightBtnText("确定").setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                    @Override
                    public void dialogRightBtnClick(String inputString) {

                    }
                }).create(AlertFragmentDialog.Builder.TYPE_TITLE_WITH_RIGHT_IMAGE);
                break;
            case R.id.fl_config:
                if (App.getConfig().getLoginStatus()) {
                    clickTab("wode_zhanghuguanli_click","我的_账户管理");
                    Intent intent = new Intent(mActivity, ModifyUserActivity.class);
                    startActivity(intent);
                } else {
                    toLogin();
                }
                break;
            case R.id.rl_message_hint: //消息
                if (App.getConfig().getLoginStatus()) {
                    startActivity(new Intent(mActivity, MainMsgActivity.class));
                    mReadMsg.setVisibility(View.INVISIBLE);
                } else {
                    toLogin();
                }
                break;

        }
    }

    private void showCallPhoneDialog() {
        new AlertFragmentDialog.Builder(mActivity).setContent(Constant.CUSTOMER_PHONE_TEXT, R.color.color_333333, R.dimen.dimen_20sp)
                .setRightBtnText("拨打")
                .setLeftBtnText("取消")
                .setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                    @Override
                    public void dialogRightBtnClick(String string) {
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, requestPermissions);
                    }
                }).build();
    }

    private PermissionsListener requestPermissions = new PermissionsListener() {
        @Override
        public void onGranted() {
            //拨打电话
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                    + Constant.CUSTOMER_PHONE));
            startActivity(intent);
        }

        @Override
        public void onDenied(List<String> deniedPermissions, boolean isNeverAsk) {
            for (String denied : deniedPermissions) {
                if (denied.equals(Manifest.permission.CALL_PHONE)) {
                    showToast(getString(R.string.please_open_permission, getString(R.string.call_phone)));
                }
            }
        }
    };

    private void toLogin() {
        String userId = SpUtil.getString(Constant.CACHE_TAG_UID);
        LogUtils.loge(userId);
        if (StringUtil.isBlank(userId)) {
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.putExtra("userId", userId);
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
            if (code == UIBaseEvent.EVENT_LOGIN) {//登录
                // 数据刷新
                if (App.getConfig().getLoginStatus()) {
                    mLoginRegister.setVisibility(View.GONE);
                    mMobphone.setVisibility(View.VISIBLE);
                    mMobphone.setText(SpUtil.getString(Constant.CACHE_TAG_USERNAME));
                    setViperVisible();
                    setAvatar(ConfigUtil.baseImageUserUrl + SpUtil.getString(Constant.CACHE_USER_AVATAR));
                }
            } else if (code == UIBaseEvent.EVENT_UPDATE_AVATAR) {//修改头像
                setAvatar(ConfigUtil.baseImageUserUrl + SpUtil.getString(Constant.CACHE_USER_AVATAR));
            } else if (code == UIBaseEvent.EVENT_UPDATE_NICK_NAME) {//修改昵称
                mMobphone.setText(SpUtil.getString(Constant.CACHE_TAG_USERNAME));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FragmentRefreshEvent event) {
        if (UIBaseEvent.EVENT_LOGOUT == event.getType()) {
            mLoginRegister.setVisibility(View.VISIBLE);
            mMobphone.setVisibility(View.GONE);
            mViper.setVisibility(View.INVISIBLE);
            mReadMsg.setVisibility(View.INVISIBLE);
            setAvatar("");
        }
    }

    private void setViperVisible() {
        if (Double.parseDouble(SpUtil.getString(Constant.CACHE_TAG_UID)) <= 30000) {
            mViper.setVisibility(View.VISIBLE);
        } else {
            mViper.setVisibility(View.INVISIBLE);
        }
    }


    /*******
     *消息红点控制
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(RefreshUIEvent event) {
        if (event.getType() == UIBaseEvent.EVENT_CIRCLE_MSG_RED_VISIBILITY) {//显示
            mReadMsg.setVisibility(View.VISIBLE);
        } else if (event.getType() == UIBaseEvent.EVENT_CIRCLE_MSG_RED_GONE) {//点击
            mReadMsg.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        userCenterFragment = null;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && mPresenter !=null && App.getConfig().getLoginStatus()){
            mPresenter.getMsgRed(SpUtil.getString(Constant.CACHE_TAG_UID));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter !=null && App.getConfig().getLoginStatus()){
            mPresenter.getMsgRed(SpUtil.getString(Constant.CACHE_TAG_UID));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onConfirmClick() {
        mAccountPresenter.settlementConfirm(SpUtil.getString(Constant.CACHE_TAG_UID));
    }

    /**
     * 埋点
     * @param value1
     * @param value2
     */
    private void clickTab(String value1 , String value2){
        MobclickAgent.onEvent(mContext,value1, value2);
    }


    /**
     * 消息是否已读
     * @param readMsg
     */
    @Override
    public void getMsgRed(boolean readMsg) {
       if (readMsg){
           mReadMsg.setVisibility(View.VISIBLE);
       }else {
           mReadMsg.setVisibility(View.INVISIBLE);
       }
    }
}
