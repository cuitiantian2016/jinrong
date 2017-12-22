package com.honglu.future.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.honglu.future.app.App;
import com.honglu.future.app.AppManager;
import com.honglu.future.base.BasePresenter;
import com.honglu.future.base.IBaseView;
import com.honglu.future.bean.CheckAccountBean;
import com.honglu.future.config.ConfigUtil;
import com.honglu.future.config.Constant;
import com.honglu.future.dialog.AlertFragmentDialog;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.main.activity.WebViewActivity;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ToastUtil;

import java.util.List;

/**
 * Created by zq on 2017/12/22.
 */

public class CheckAccount extends IBaseView<CheckAccountBean> {
    private BasePresenter mPresenter;
    private Context mContext;

    public CheckAccount(Context context) {
        mContext = context;
    }

    @Override
    public void bindData(CheckAccountBean bean) {

    }

    /**
     * 刷新数据
     */
    public void checkAccount() {
        if (mPresenter == null) {
            mPresenter = new BasePresenter<IBaseView<CheckAccountBean>>(this) {
                @Override
                public void getData() {
                    super.getData();
                    toSubscribe(HttpManager.getApi().accountCheck(SpUtil.getString(Constant.CACHE_TAG_UID)), new HttpSubscriber<CheckAccountBean>() {
                        @Override
                        protected void _onStart() {
                            App.loadingContent(AppManager.getInstance().currentActivity(), "查询开户信息...");
                        }

                        @Override
                        protected void _onNext(CheckAccountBean bean) {
                            if (bean == null) {
                                Intent intent = new Intent(AppManager.getInstance().currentActivity(), WebViewActivity.class);
                                intent.putExtra("title", "开户");
                                intent.putExtra("url", ConfigUtil.OPEN_ACCOUNT_RESEARCH);
                                AppManager.getInstance().currentActivity().startActivity(intent);
                            } else {
                                if (bean.status.equals("0")) {
                                    new AlertFragmentDialog.Builder(AppManager.getInstance().currentActivity())
                                            .setRightBtnText("知道了").setContent("开户审核中，请耐心等待...")
                                            .create(AlertFragmentDialog.Builder.TYPE_NORMAL);
                                } else if (bean.status.equals("1")) {
                                    Intent intent = new Intent(AppManager.getInstance().currentActivity(), WebViewActivity.class);
                                    intent.putExtra("title", "开户");
                                    intent.putExtra("url", ConfigUtil.OPEN_ACCOUNT_HOME + "?company=" + bean.company);
                                    AppManager.getInstance().currentActivity().startActivity(intent);
                                } else if (bean.status.equals("2")) {
                                    Intent intent = new Intent(AppManager.getInstance().currentActivity(), WebViewActivity.class);
                                    intent.putExtra("title", "开户");
                                    intent.putExtra("url", ConfigUtil.OPEN_ACCOUNT_RESEARCH);
                                    AppManager.getInstance().currentActivity().startActivity(intent);
                                }
                            }
                        }

                        @Override
                        protected void _onError(String message) {
                            ToastUtil.show(message);
                        }

                        @Override
                        protected void _onCompleted() {
                            App.hideLoading();
                        }

                    });
                }
            };
        }
        mPresenter.getData();
    }
}
