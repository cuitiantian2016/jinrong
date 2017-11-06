package com.honglu.future.ui.main.contract;

import com.honglu.future.base.BaseView;
import com.honglu.future.ui.main.bean.MoreContentBean;

/**
 * Created by zhuaibing on 2017/11/4
 */

public interface SplashContract {
    interface View extends BaseView {

    }
    interface Presenter {
        void getAudited();
    }
}
