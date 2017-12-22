package com.honglu.future.ui.circle.praisesandreward;

import com.google.gson.JsonNull;
import com.honglu.future.base.BaseView;
import com.honglu.future.ui.circle.bean.ArewardListBean;

import java.util.List;

/**
 * Created by zhuaibing on 2017/12/20
 */

public interface ArewardAndSupportContract {
    interface View extends BaseView {
       void getArewardList(List<ArewardListBean> list);
       void focus(String userId,String attentionState);
    }

    interface Presenter {
       void getArewardList(String userId,String postId,int rows);
        void focus(String postUserId ,String userId ,String attentionState);
    }
}
