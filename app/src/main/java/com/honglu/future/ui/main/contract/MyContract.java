package com.honglu.future.ui.main.contract;


import com.honglu.future.base.BaseView;
import com.honglu.future.ui.main.bean.MoreContentBean;

/**
 * Created by Administrator on 2017/2/13 0013.
 */

public interface MyContract {
    interface View extends BaseView {
        void userInfoSuccess(MoreContentBean result);
    }
    interface Presenter {
        void getInfo();
    }
}
