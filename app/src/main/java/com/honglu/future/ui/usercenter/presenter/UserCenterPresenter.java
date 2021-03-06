package com.honglu.future.ui.usercenter.presenter;

import com.honglu.future.base.BasePresenter;
import com.honglu.future.config.Constant;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.usercenter.bean.AccountInfoBean;
import com.honglu.future.ui.usercenter.contract.UserCenterContract;
import com.honglu.future.util.SpUtil;

/**
 * Created by zq on 2017/10/24.
 */

public class UserCenterPresenter extends BasePresenter<UserCenterContract.View> implements UserCenterContract.Presenter {


    @Override
    public void getMsgRed(String userId) {
      toSubscribe(HttpManager.getApi().getMsgRed(userId), new HttpSubscriber<Boolean>() {

          @Override
          protected void _onError(String message, int code) {
              mView.getMsgRed(false);
          }

          @Override
          public void onNext(Boolean aBoolean) {
              mView.getMsgRed(aBoolean);
          }
      });
    }
}
