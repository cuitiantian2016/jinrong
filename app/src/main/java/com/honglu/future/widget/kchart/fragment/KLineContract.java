package com.honglu.future.widget.kchart.fragment;

import com.honglu.future.base.BaseView;
import com.honglu.future.ui.trade.bean.KLineBean;

/**
 * Created by zq on 2017/11/7.
 */

public interface KLineContract {
    interface View extends BaseView {
        void getKLineDataSuccess(KLineBean bean);
    }

    interface Presenter {
        void getKLineData(String excode, String code, String type);
    }
}
