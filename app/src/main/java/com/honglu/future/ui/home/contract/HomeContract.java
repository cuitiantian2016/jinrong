package com.honglu.future.ui.home.contract;


import com.honglu.future.base.BaseView;
import com.honglu.future.ui.home.bean.HomeIndexResponseBean;

/**
 * Created by xiejingwen at 2017/2/15 0015
 */

public interface HomeContract {
    interface View extends BaseView {
        void indexSuccess(HomeIndexResponseBean result, boolean isClickLend);
        void confirmFailedSuccess();
    }
    interface Presenter{
        /**
         * 首页数据
         * @param isClickLend  是否刷新index接口以获取最新认证状态（用于点击借款按钮）
         */
        void loadIndex(boolean isClickLend);

        /**
         * 借款被拒 点击按钮调用。
         *
         * @param id
         */
        void confirmFailed(String id);
    }
}
