package com.honglu.future.ui.main.presenter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

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
import com.honglu.future.util.AESUtils;
import com.honglu.future.util.SpUtil;

/**
 * Created by zq on 2017/11/3.
 */

public class AccountPresenter extends BasePresenter<AccountContract.View> implements AccountContract.Presenter {
    @Override
    public void login(final String account, String password, String userId, String company, final TextView tv_pass, final Context context) {
        toSubscribe(HttpManager.getApi().loginAccount(account, AESUtils.encrypt(password), userId, company), new HttpSubscriber<AccountBean>() {
            @Override
            public void _onStart() {
                mView.showLoading("登录中...");
            }

            @Override
            protected void _onNext(AccountBean bean) {
                SpUtil.putString(Constant.CACHE_ACCOUNT_USER_NAME, account);
                SpUtil.putString(Constant.CACHE_ACCOUNT_TOKEN, bean.getToken());
                mView.loginSuccess(bean);
            }

            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                if (code == 70000) {//首次登录
                    tv_pass.setText("");
                    SpUtil.putString(Constant.CACHE_ACCOUNT_USER_NAME, account);
                    new AlertFragmentDialog.Builder((FragmentActivity)context)
                            .setLeftBtnText("取消").setContent(message, R.color.color_333333, R.dimen.dimen_14sp).setTitle("", R.color.color_3C383F, R.dimen.dimen_16sp)
                            .setRightBtnText("确定").setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
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
}
