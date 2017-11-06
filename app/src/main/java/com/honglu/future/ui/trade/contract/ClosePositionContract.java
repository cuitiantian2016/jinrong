package com.honglu.future.ui.trade.contract;

import com.honglu.future.base.BaseView;
import com.honglu.future.ui.trade.bean.ClosePositionListBean;

import java.util.List;

/**
 * Created by zq on 2017/10/26.
 */

public interface ClosePositionContract {
    interface View extends BaseView {
        void getCloseListSuccess(List<ClosePositionListBean> list);
    }

    interface Presenter {
        void getCloseList(String dayStart, String dayEnd, String userId, String token, String startTime, String endTime);
    }
}
