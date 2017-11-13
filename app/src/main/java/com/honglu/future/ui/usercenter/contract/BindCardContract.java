package com.honglu.future.ui.usercenter.contract;

import com.honglu.future.base.BaseView;
import com.honglu.future.ui.usercenter.bean.BindCardBean;

import java.util.List;

/**
 * Created by zhuaibing on 2017/11/11
 */

public interface BindCardContract {
    interface View extends BaseView {
        void getBindCardInfo(List<BindCardBean> list);
    }
    interface Presenter{
       void getBindCardInfo(String userId ,String token);
    }
}
