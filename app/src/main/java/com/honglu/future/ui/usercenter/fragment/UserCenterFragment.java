package com.honglu.future.ui.usercenter.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.app.AppManager;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.base.PermissionsListener;
import com.honglu.future.bean.CheckAccountBean;
import com.honglu.future.config.ConfigUtil;
import com.honglu.future.config.Constant;
import com.honglu.future.dialog.AccountLoginDialog;
import com.honglu.future.dialog.AlertFragmentDialog;
import com.honglu.future.dialog.BillConfirmDialog;
import com.honglu.future.events.ChangeTabMainEvent;
import com.honglu.future.events.FragmentRefreshEvent;
import com.honglu.future.events.RefreshUIEvent;
import com.honglu.future.events.UIBaseEvent;
import com.honglu.future.ui.login.activity.LoginActivity;
import com.honglu.future.ui.main.CheckAccount;
import com.honglu.future.ui.main.FragmentFactory;
import com.honglu.future.ui.main.activity.WebViewActivity;
import com.honglu.future.ui.main.contract.AccountContract;
import com.honglu.future.ui.main.presenter.AccountPresenter;
import com.honglu.future.ui.recharge.activity.InAndOutGoldActivity;
import com.honglu.future.ui.trade.activity.TradeRecordActivity;
import com.honglu.future.ui.trade.bean.AccountBean;
import com.honglu.future.ui.trade.bean.SettlementInfoBean;
import com.honglu.future.ui.trade.historybill.HistoryBillActivity;
import com.honglu.future.ui.usercenter.activity.FutureAccountActivity;
import com.honglu.future.ui.usercenter.activity.ModifyUserActivity;
import com.honglu.future.ui.usercenter.activity.UserAccountActivity;
import com.honglu.future.ui.usercenter.bean.AccountInfoBean;
import com.honglu.future.ui.usercenter.contract.UserCenterContract;
import com.honglu.future.ui.usercenter.presenter.UserCenterPresenter;
import com.honglu.future.util.AndroidUtil;
import com.honglu.future.util.ConvertUtil;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.util.LogUtils;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.StringUtil;
import com.honglu.future.util.ToastUtil;
import com.honglu.future.util.Tool;
import com.honglu.future.util.ViewUtil;
import com.honglu.future.widget.CircleImageView;
import com.honglu.future.widget.ExpandableLayout;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.honglu.future.util.ToastUtil.showToast;

/**
 * Created by zq on 2017/10/24.
 */

public class UserCenterFragment extends BaseFragment<UserCenterPresenter> implements
        UserCenterContract.View, AccountContract.View, BillConfirmDialog.OnConfirmClickListener {

    @BindView(R.id.tv_loginRegister)
    TextView mLoginRegister;
    //    @BindView(R.id.iv_setup)
//    ImageView mSetup;
    @BindView(R.id.ll_signin_suc_layout)
    LinearLayout mSigninSucLayout;
    @BindView(R.id.iv_head)
    CircleImageView mHead;
    @BindView(R.id.tv_mobphone)
    TextView mMobphone;
    @BindView(R.id.tv_signin)
    TextView mSignin;
    @BindView(R.id.ll_signin_layout)
    LinearLayout mSigninLayout;
    @BindView(R.id.ex_expandableView)
    ExpandableLayout mExpandableView;
    @BindView(R.id.tv_signout)
    TextView mSignout;
    @BindView(R.id.tv_danger_chance)
    TextView mDangerChance;
    @BindView(R.id.ll_leftAccount)
    LinearLayout mLeftAccountView;
    @BindView(R.id.tv_money)
    TextView mMoney;
    @BindView(R.id.tv_rights_interests)
    TextView mRightsInterests;
    @BindView(R.id.tv_profit_loss)
    TextView mProfitLoss;
    @BindView(R.id.tv_withdrawals)
    TextView mWithdrawals;
    @BindView(R.id.tv_recharge)
    TextView mRecharge;
    @BindView(R.id.tv_position)
    TextView mPosition;
    @BindView(R.id.tv_account_manage)
    TextView mAccountManage;
    @BindView(R.id.tv_bond_query)
    TextView mBondQuery;
    @BindView(R.id.tv_trade_details)
    TextView mTradeDetails;
    @BindView(R.id.tv_bill_details)
    TextView mBillDetails;
    @BindView(R.id.tv_history_bill)
    TextView mHistoryBill;
    @BindView(R.id.ll_signin_account_layout)
    LinearLayout mSigninAccountLayout;
    @BindView(R.id.tv_open_account)
    TextView mOpenAccount;
    @BindView(R.id.tv_novice)
    TextView mNovice;
    @BindView(R.id.tv_kefu)
    TextView mKefu;
    @BindView(R.id.ll_bottomLayout1)
    LinearLayout mBottomLayout1;
    @BindView(R.id.tv_phone)
    TextView mPhone;
    @BindView(R.id.tv_aboutus)
    TextView mAboutus;
    @BindView(R.id.tv_update)
    TextView mUpdate;
    @BindView(R.id.ll_bottomLayout2)
    LinearLayout mBottomLayout2;
    @BindView(R.id.fl_config)
    FrameLayout mFlConfig;
    @BindView(R.id.ll_viper)
    RelativeLayout mViper;
    private AccountLoginDialog mAccountLoginDialog;
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
        mAccountPresenter.init(this);
    }

    @Override
    public void loadData() {
        EventBus.getDefault().register(this);
        mCheckAccount = new CheckAccount(mContext);
        int screenWidth = ViewUtil.getScreenWidth(getActivity());
        int dimen_10dp = getResources().getDimensionPixelSize(R.dimen.dimen_10dp);
        int dimen_20dp = getResources().getDimensionPixelSize(R.dimen.dimen_20dp);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mLeftAccountView.getLayoutParams();
        params.width = (screenWidth - dimen_10dp * 2 - dimen_20dp * 2) / 2;
        mLeftAccountView.setLayoutParams(params);

        if (App.getConfig().getLoginStatus()) {
            setViperVisible();
        }

        if (!App.getConfig().getAccountLoginStatus()) {
            signinExpandCollapse(false);
        } else {
            startRun();
            signinExpandCollapse(true);
        }
        setAvatar();
    }

    private void setAvatar() {
        ImageUtil.display(ConfigUtil.baseImageUserUrl + SpUtil.getString(Constant.CACHE_USER_AVATAR), mHead, R.mipmap.img_head);
    }

    @OnClick({R.id.fl_config, R.id.tv_novice, R.id.tv_trade_details, R.id.tv_account_manage,
            R.id.tv_bill_details, R.id.tv_position, R.id.ll_signin_layout, R.id.tv_signout,
            R.id.tv_my_account, R.id.ll_account, R.id.tv_history_bill, R.id.tv_open_account,
            R.id.tv_kefu, R.id.tv_withdrawals, R.id.tv_recharge, R.id.tv_phone, R.id.tv_aboutus,
            R.id.tv_bond_query, R.id.tv_update, R.id.ll_viper})
    public void onClick(View view) {
        if (Tool.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.tv_novice:
                clickTab("wode_xinshourumen_click","我的_新手入门");
                Intent intentTeach = new Intent(mActivity, WebViewActivity.class);
                intentTeach.putExtra("title", "新手学堂");
                intentTeach.putExtra("url", ConfigUtil.NEW_USER_TEACH);
                startActivity(intentTeach);
                break;
            case R.id.tv_trade_details:
                clickTab("wode_account_jiaoyimingxi","我的_我的交易明细");
                stopRun();
                startActivity(new Intent(mActivity, TradeRecordActivity.class));
                break;
            case R.id.tv_account_manage:
                clickTab("wode_account_qihuoaccountguanli","我的_期货账户管理");
                startActivity(new Intent(mActivity, FutureAccountActivity.class));
                break;
            case R.id.tv_bill_details:
                clickTab("wode_account_churujinmingxi","我的_出入金明细");
                InAndOutGoldActivity.startInAndOutGoldActivity(getActivity(), 2);
                break;
            case R.id.tv_position:
                clickTab("wode_account_chicang","我的_我的持仓");
                EventBus.getDefault().post(new ChangeTabMainEvent(FragmentFactory.FragmentStatus.Trade));
                break;
            case R.id.ll_signin_layout:
                clickTab("wode_qudenglu_click","去登录");
                AndroidUtil.putAccountMineLogin(false);
                if (!App.getConfig().getLoginStatus()) {
                    Intent loginActivity = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(loginActivity);
                } else {
                    mAccountLoginDialog = new AccountLoginDialog(mActivity, mAccountPresenter);
                    mAccountLoginDialog.show();
                }
                break;
            case R.id.tv_signout:
                if (DeviceUtils.isFastDoubleClick()) {
                    return;
                }
                new AlertFragmentDialog.Builder(mActivity).setContent("确定退出期货账户吗？")
                        .setRightBtnText("确定")
                        .setLeftBtnText("取消")
                        .setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                            @Override
                            public void dialogRightBtnClick(String string) {
                                mPresenter.accountLogout(SpUtil.getString(Constant.CACHE_TAG_UID), SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN), SpUtil.getString(Constant.COMPANY_TYPE));
                            }
                        }).build();
                break;
            case R.id.tv_my_account:
            case R.id.ll_account:
                clickTab("wode_myaccount_click","我的_我的账户");
                startActivity(UserAccountActivity.class);
                break;
            case R.id.tv_history_bill:
                clickTab("wode_account_lishizhangdan","我的_历史账单查询");
                startActivity(HistoryBillActivity.class);
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
            case R.id.tv_withdrawals:
                clickTab("wode_myaccount_tixian","我的_我的账户_提现");
                InAndOutGoldActivity.startInAndOutGoldActivity(getActivity(), 1);
                break;
            case R.id.tv_recharge:
                clickTab("wode_myaccount_chongzhi","我的_我的账户_充值");
                InAndOutGoldActivity.startInAndOutGoldActivity(getActivity(), 0);
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
            case R.id.tv_bond_query:
                clickTab("wode_account_jiancezhongxin","我的_保证金检测中心查询");
                Intent intentQuery = new Intent(mActivity, WebViewActivity.class);
                intentQuery.putExtra("url", ConfigUtil.QUERY_FUTURE);
                intentQuery.putExtra("title", "保证金监控中心查询");
                startActivity(intentQuery);
                break;
            case R.id.tv_update:
                clickTab("wode_shezhi_click","我的_设置");
                if (App.getConfig().getLoginStatus()) {
                    startActivity(ModifyUserActivity.class);
                } else {
                    toLogin();
                }
                break;
            case R.id.ll_viper:
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

    //isSignin true  登录期货账号成功
    private void signinExpandCollapse(boolean isSignin) {
        if (!isSignin) {
            mSigninAccountLayout.setVisibility(View.GONE);
            mExpandableView.expand();
        } else {
            mSigninAccountLayout.setVisibility(View.VISIBLE);
            if (mExpandableView.isExpanded()) {
                mExpandableView.collapse(true);
            }
        }
        if (App.getConfig().getLoginStatus()) {
            mSigninSucLayout.setVisibility(View.VISIBLE);
            mLoginRegister.setVisibility(View.GONE);
//
//            if (SpUtil.getString(Constant.CACHE_TAG_USERNAME).length() >= 8) {
//                mMobphone.setMaxEms(6);
//                mMobphone.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
//            }
            mMobphone.setText(SpUtil.getString(Constant.CACHE_TAG_USERNAME));
        }
    }


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
                    mSigninSucLayout.setVisibility(View.VISIBLE);
                    mLoginRegister.setVisibility(View.GONE);
//                    if (SpUtil.getString(Constant.CACHE_TAG_USERNAME).length() >= 8) {
//                        mMobphone.setMaxEms(6);
//                        mMobphone.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
//                    }
                    mMobphone.setText(SpUtil.getString(Constant.CACHE_TAG_USERNAME));
                    setViperVisible();
                    setAvatar();
                }
            } else if (code == UIBaseEvent.EVENT_UPDATE_AVATAR) {//修改头像
                setAvatar();
            } else if (code == UIBaseEvent.EVENT_UPDATE_NICK_NAME) {//修改昵称
//                if (SpUtil.getString(Constant.CACHE_TAG_USERNAME).length() >= 8) {
//                    mMobphone.setMaxEms(6);
//                    mMobphone.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
//                }
                mMobphone.setText(SpUtil.getString(Constant.CACHE_TAG_USERNAME));
            } else if (code == UIBaseEvent.EVENT_ACCOUNT_LOGOUT) {//token失效或者被挤掉
                signinExpandCollapse(false);
                stopRun();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FragmentRefreshEvent event) {
        if (UIBaseEvent.EVENT_LOGOUT == event.getType()) {
            stopRun();
            mSigninSucLayout.setVisibility(View.GONE);
            mLoginRegister.setVisibility(View.VISIBLE);
            mViper.setVisibility(View.GONE);
            signinExpandCollapse(false);
        }
    }

    private void setViperVisible() {
        if (Double.parseDouble(SpUtil.getString(Constant.CACHE_TAG_UID)) <= 30000) {
            mViper.setVisibility(View.VISIBLE);
        } else {
            mViper.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        userCenterFragment = null;
    }

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            getAccountBasicInfo();//每隔3秒刷一次
            mHandler.postDelayed(this, 3000);
        }
    };

    @Override
    public void loginSuccess(AccountBean bean) {
        if (billConfirmDialog != null && billConfirmDialog.isShowing()) {
            billConfirmDialog.dismiss();
        }
        mAccountLoginDialog.dismiss();
        signinExpandCollapse(true);
        startRun();
    }

    @Override
    public void showSettlementDialog(SettlementInfoBean bean) {
        billConfirmDialog = new BillConfirmDialog(mContext, bean);
        billConfirmDialog.setOnConfirmClickListenerr(this);
        billConfirmDialog.show();
    }

    /**
     * 开始刷新用户信息
     */
    public void startRun() {
        if (App.getConfig().getAccountLoginStatus()) {
            mHandler.removeCallbacks(mRunnable);
            mHandler.post(mRunnable);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            stopRun();
        } else {
            if (!App.getConfig().getAccountLoginStatus()) {
                signinExpandCollapse(false);
                stopRun();
            } else {
                startRun();
                signinExpandCollapse(true);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden() && isVisible()) {
            startRun();
            if (AndroidUtil.getAccountMineLogin()){
                mSigninLayout.performClick();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopRun();
    }

    public void stopRun() {
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void getAccountInfoSuccess(AccountInfoBean bean) {
        mDangerChance.setText(bean.getCapitalProportion());
        mRightsInterests.setText(StringUtil.forNumber(new BigDecimal(bean.getRightsInterests()).doubleValue()));
        mMoney.setText(StringUtil.forNumber(new BigDecimal(bean.getAvailable()).doubleValue()));

        if (new BigDecimal(ConvertUtil.NVL(bean.getPositionProfit(), "0")).doubleValue() > 0) {
            mProfitLoss.setTextColor(getActivity().getResources().getColor(R.color.color_FB4F4F));
            mProfitLoss.setText("+" + StringUtil.forNumber(new BigDecimal(ConvertUtil.NVL(bean.getPositionProfit(), "0")).doubleValue()));
        } else if (new BigDecimal(ConvertUtil.NVL(bean.getPositionProfit(), "0")).doubleValue() < 0) {
            mProfitLoss.setTextColor(getActivity().getResources().getColor(R.color.color_2CC593));
            mProfitLoss.setText(StringUtil.forNumber(new BigDecimal(ConvertUtil.NVL(bean.getPositionProfit(), "0")).doubleValue()));
        } else {
            mProfitLoss.setTextColor(getActivity().getResources().getColor(R.color.color_333333));
            mProfitLoss.setText(StringUtil.forNumber(new BigDecimal(ConvertUtil.NVL(bean.getPositionProfit(), "0")).doubleValue()));
        }
    }

    @Override
    public void accountLogoutSuccess() {
        SpUtil.putString(Constant.CACHE_ACCOUNT_TOKEN, "");
        EventBus.getDefault().post(new RefreshUIEvent(UIBaseEvent.EVENT_ACCOUNT_LOGOUT));
        signinExpandCollapse(false);
        stopRun();
    }

    private void getAccountBasicInfo() {
        mPresenter.getAccountInfo(SpUtil.getString(Constant.CACHE_TAG_UID), SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN), SpUtil.getString(Constant.COMPANY_TYPE));
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
}
