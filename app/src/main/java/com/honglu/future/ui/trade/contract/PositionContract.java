package com.honglu.future.ui.trade.contract;

import com.honglu.future.base.BaseView;
import com.honglu.future.ui.trade.bean.HoldPositionBean;

import java.util.List;

/**
 * Created by zq on 2017/10/26.
 */

public interface PositionContract {
    interface View extends BaseView {
        void getHoldPositionListSuccess(List<HoldPositionBean> list);
    }

    interface Presenter {
        void getHoldPositionList(String userId, String token, String company);
    }
}
