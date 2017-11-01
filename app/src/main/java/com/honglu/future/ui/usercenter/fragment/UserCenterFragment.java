package com.honglu.future.ui.usercenter.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cfmmc.app.sjkh.MainActivity;
import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.config.Constant;
import com.honglu.future.events.ChangeTabMainEvent;
import com.honglu.future.events.RefreshUIEvent;
import com.honglu.future.events.UIBaseEvent;
import com.honglu.future.ui.login.activity.LoginActivity;
import com.honglu.future.ui.main.FragmentFactory;
import com.honglu.future.ui.recharge.activity.InAndOutGoldActivity;
import com.honglu.future.ui.recharge.activity.PasswordResetActivity;
import com.honglu.future.ui.trade.activity.TradeRecordActivity;
import com.honglu.future.ui.usercenter.activity.FutureAccountActivity;
import com.honglu.future.ui.usercenter.activity.ModifyUserActivity;
import com.honglu.future.ui.usercenter.contract.UserCenterContract;
import com.honglu.future.ui.usercenter.presenter.UserCenterPresenter;
import com.honglu.future.util.LogUtils;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.StringUtil;
import com.honglu.future.util.Tool;
import com.honglu.future.util.UserUtil;
import com.honglu.future.widget.CircleImageView;
import com.honglu.future.widget.ExpandableLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zq on 2017/10/24.
 */

public class UserCenterFragment extends BaseFragment<UserCenterPresenter> implements
        UserCenterContract.View {

    @BindView(R.id.tv_loginRegister)
    TextView mLoginRegister;
    @BindView(R.id.iv_setup)
    ImageView mSetup;
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

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorMsg(String msg, String type) {

    }


    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        EventBus.getDefault().register(this);
        if (App.getConfig().getLoginStatus()) {
            //mUserName.setText(SpUtil.getString(Constant.CACHE_TAG_USERNAME));
        }
        //// TODO: 2017/10/31 根据返回结果控制
        signinExpandCollapse(true);
    }

    @OnClick({R.id.tv_loginRegister, R.id.tv_signin, R.id.tv_novice, R.id.tv_trade_details, R.id.tv_account_manage,
            R.id.tv_bill_details, R.id.tv_position})
    public void onClick(View view) {
        if (Tool.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.tv_loginRegister:
                if (App.getConfig().getLoginStatus()) {
                    Intent intent = new Intent(mActivity, ModifyUserActivity.class);
                    startActivity(intent);
                } else {
                    toLogin();
                }
                break;
            case R.id.tv_signin:
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("brokerId", "0101");
                String userMobile = UserUtil.getUserMobile();
                if (!TextUtils.isEmpty(userMobile)) {
                    intent.putExtra("mobile", userMobile);
                }
                startActivity(intent);
                break;
            case R.id.tv_novice:
//                InAndOutGoldActivity.startInAndOutGoldActivity(getActivity(),1);
                PasswordResetActivity.startPasswordResetActivity(getActivity(), true);
                break;
            case R.id.tv_trade_details:
                startActivity(new Intent(mActivity, TradeRecordActivity.class));
                break;
            case R.id.tv_account_manage:
                startActivity(new Intent(mActivity, FutureAccountActivity.class));
                break;
            case R.id.tv_bill_details:
                startActivity(new Intent(mActivity, InAndOutGoldActivity.class));
                break;
            case R.id.tv_position:
                EventBus.getDefault().post(new ChangeTabMainEvent(FragmentFactory.FragmentStatus.Trade));
                break;
        }
    }

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
                    //mUserName.setText(SpUtil.getString(Constant.CACHE_TAG_MOBILE));
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        userCenterFragment = null;
    }
}
