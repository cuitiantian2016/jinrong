package com.honglu.future.ui.trade.contract;

import com.honglu.future.base.BaseView;
import com.honglu.future.ui.trade.bean.HoldDetailBean;
import com.honglu.future.ui.trade.bean.HoldPositionBean;

import java.util.List;

/**
 * Created by zq on 2017/10/26.
 */

public interface PositionContract {
    interface View extends BaseView {
        void getHoldPositionListSuccess(List<HoldPositionBean> list);
        void getHoldDetailListSuccess(List<HoldDetailBean> list);
    }

    interface Presenter {
        void getHoldPositionList(String userId, String token, String company);
        void getHoldPositionDetail(String instrumentId, String type, String todayPosition, String userId, String token);
    }
}
