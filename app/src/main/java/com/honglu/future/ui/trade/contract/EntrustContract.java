package com.honglu.future.ui.trade.contract;

import com.honglu.future.base.BaseView;
import com.honglu.future.ui.trade.bean.EntrustBean;

import java.util.List;

/**
 * Created by zq on 2017/10/26.
 */

public interface EntrustContract {
    interface View extends BaseView {
        void getEntrustListSuccess(List<EntrustBean> list);
    }

    interface Presenter {
        void getEntrustList(String userId, String token);
    }
}
