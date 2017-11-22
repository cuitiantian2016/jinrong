package com.honglu.future.ui.main.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BasePresenter;
import com.honglu.future.config.Constant;
import com.honglu.future.dialog.AlertFragmentDialog;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.main.contract.AccountContract;
import com.honglu.future.ui.recharge.activity.PasswordResetActivity;
import com.honglu.future.ui.trade.bean.AccountBean;
import com.honglu.future.ui.trade.bean.SettlementInfoBean;
import com.honglu.future.ui.trade.billconfirm.BillConfirmActivity;
import com.honglu.future.util.AESUtils;
import com.honglu.future.util.SpUtil;

import rx.Observable;
import rx.Subscriber;
import rx.observables.SyncOnSubscribe;

/**
 * Created by zq on 2017/11/3.
 */

public class AccountPresenter extends BasePresenter<AccountContract.View> implements AccountContract.Presenter {
    private Context mContext;
    private AccountBean mBean;

    @Override
    public void login(final String account, String password, final String userId, final String company, final TextView tv_pass, final Context context) {
        mContext = context;
        toSubscribe(HttpManager.getApi().loginAccount(account, AESUtils.encrypt(password), userId, company), new HttpSubscriber<AccountBean>() {
            @Override
            public void _onStart() {
                mView.showLoading("登录中...");
            }

            @Override
            protected void _onNext(final AccountBean bean) {
                mBean = bean;
                SpUtil.putString(Constant.CACHE_ACCOUNT_USER_NAME, account);
                tv_pass.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        querySettlementInfo(userId, bean.getToken(), company);

                    }
                }, 1000);

            }

            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                if ("首次登录必须修改密码，请修改密码后重新登录".equals(message)) {//首次登录
                    tv_pass.setText("");
                    SpUtil.putString(Constant.CACHE_ACCOUNT_USER_NAME, account);
                    new AlertFragmentDialog.Builder((FragmentActivity) context)
                            .setLeftBtnText("取消").setContent("首次登录必须修改交易密码，请修改密码后重新登录", R.color.color_A4A5A6, R.dimen.dimen_15sp).setTitle("修改交易面", R.color.color_333333, R.dimen.dimen_18sp)
                            .setRightBtnText("去修改").setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                        @Override
                        public void dialogRightBtnClick(String string) {
                            PasswordResetActivity.startPasswordResetActivity(App.mApp.mActivity, true);
                        }
                    }).create(AlertFragmentDialog.Builder.TYPE_NORMAL);
                } else {
                    mView.showErrorMsg(message, null);
                }
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }

    @Override
    public void querySettlementInfo(String userId, final String token, String company) {
        toSubscribe(HttpManager.getApi().querySettlementInfo(userId, token, company), new HttpSubscriber<SettlementInfoBean>() {
            @Override
            public void _onStart() {
                mView.showLoading("查询结算单中...");
            }

            @Override
            protected void _onNext(SettlementInfoBean bean) {
                if (bean == null) {
                    SpUtil.putString(Constant.CACHE_ACCOUNT_TOKEN, token);
                    mView.loginSuccess(mBean);
                    return;
                }
                //SpUtil.putString(Constant.CACHE_ACCOUNT_TOKEN, "");
                Intent intent = new Intent(mContext, BillConfirmActivity.class);
                intent.putExtra("SettlementBean", new Gson().toJson(bean));
                intent.putExtra("token", token);
                mContext.startActivity(intent);
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message, null);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }
}
