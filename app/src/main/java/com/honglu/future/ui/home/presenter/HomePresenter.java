package com.honglu.future.ui.home.presenter;

import com.honglu.future.base.BasePresenter;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.home.bean.BannerData;
import com.honglu.future.ui.home.contract.HomeContract;
/**
 * 首页presenter
 * */
public class HomePresenter {

    public static class BannerPresenter extends BasePresenter<HomeContract.BannerView> implements HomeContract.BannerPresenter{

        public BannerPresenter(HomeContract.BannerView mView) {
            super(mView);
        }

        @Override
        public void getBannerData() {
            toSubscribe(HttpManager.getApi().getBannerData(), new HttpSubscriber<BannerData>() {
                @Override
                protected void _onNext(BannerData o) {
                    mView.bindData(o);
                }
            });
        }
    }

}
