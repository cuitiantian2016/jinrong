package com.honglu.future.ui.trade.contract;

import com.honglu.future.base.BaseView;
import com.honglu.future.ui.trade.bean.HistoryBuiderPositionBean;
import com.honglu.future.ui.trade.bean.HistoryClosePositionBean;
import com.honglu.future.ui.trade.bean.HistoryMissPositionBean;
import com.honglu.future.ui.trade.bean.HistoryTradeBean;

import java.util.List;

/**
 * Created by hefei on 2017/10/26
 */
public interface TradeRecordContract {

    interface View extends BaseView {
        void bindHistoryTradeBean(HistoryTradeBean bean);
        void bindHistoryMissBean(List<HistoryMissPositionBean> list);
        void bindHistoryCloseBean(List<HistoryClosePositionBean> list);
        void bindHistoryBuilderBean(List<HistoryBuiderPositionBean> list);
    }
    interface Presenter{
        void getHistoryTradeBean(
                String dayStart,
                String userId,
                String token,
                String dayEnd);
        void getHistoryMissBean(
                String dayStart,
                String userId,
                String token,
                String dayEnd,
                int page,
                int pageSize);
        void getHistoryCloseBean(
                String dayStart,
                String userId,
                String token,
                String dayEnd,
                int page,
                int pageSize);
        void getHistoryBuilderBean(
                String dayStart,
                String userId,
                String token,
                String dayEnd,
                int page,
                int pageSize);
    }
}
