package com.honglu.future.ui.home.contract;


import com.honglu.future.base.IBaseView;
import com.honglu.future.ui.home.bean.BannerData;

/**
 * Created by xiejingwen at 2017/2/15 0015
 */

public interface HomeContract {
    abstract class BannerView extends IBaseView {//banner的界面组件
       public abstract void bindData(BannerData bannerData);
    }

    interface BannerPresenter{
        void getBannerData();
    }
}
