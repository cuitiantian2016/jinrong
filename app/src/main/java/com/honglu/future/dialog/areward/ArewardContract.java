package com.honglu.future.dialog.areward;

import com.google.gson.JsonNull;
import com.honglu.future.base.BaseView;

/**
 * Created by zhuaibing on 2017/12/20
 */

public interface ArewardContract {
    interface View extends BaseView {
        void getArewardScore(Integer arewardScore);
        void getReward(JsonNull jsonNull);
    }

    interface Presenter {
        void getArewardScore(String userId);
        void getReward(String userId,String postId,String beUserId,int score);
    }
}
