package com.honglu.future.ui.home.presenter;


import com.honglu.future.base.BasePresenter;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.home.bean.HomeIndexResponseBean;
import com.honglu.future.ui.home.contract.HomeContract;

/**
 * 首页presenter
 */

public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Presenter {
    public final String TYPE_INDEX  = "index";
    public final String TYPE_LOAN   = "toLoan";
    public final String TYPE_FAILED = "failed";

    @Override
    public void loadIndex(final boolean isClickLend) {

    }


    @Override
    public void confirmFailed(String id) {

    }
}
