package com.honglu.future.ui.main.contract;


import com.honglu.future.base.BaseView;
import com.honglu.future.bean.UpdateBean;
import com.honglu.future.ui.main.bean.ActivityBean;

/**
 * Created by xiejingwen on 2017/3/31 0031.
 */

public interface ActivityContract {
    interface View extends BaseView {
        void loadActivitySuccess(ActivityBean result);

        void getUpdateVersionSuccess(UpdateBean bean);
    }

    interface Presenter {
        void loadActivity();

        void getUpdateVersion();
    }
}
