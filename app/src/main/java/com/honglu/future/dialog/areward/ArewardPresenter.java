package com.honglu.future.dialog.areward;

import com.google.gson.JsonNull;
import com.honglu.future.base.BasePresenter;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;

/**
 * Created by zhuaibing on 2017/12/20
 */

public class ArewardPresenter extends BasePresenter<ArewardContract.View> implements ArewardContract.Presenter{

    //打赏积分
    @Override
    public void getArewardScore(String userId) {
     toSubscribe(HttpManager.getApi().getArewardScore(userId), new HttpSubscriber<Integer>() {
         @Override
         protected void _onStart() {
            mView.showLoading("数据加载中...");
         }

         @Override
         protected void _onError(String message, int code) {
             mView.stopLoading();
             mView.showErrorMsg(message,null);
         }

         @Override
         public void onNext(Integer integer) {
             mView.stopLoading();
             mView.getArewardScore(integer);
         }
     });
    }

    /**
     *打赏
     * @param userId 当前登陆用户id
     * @param postId  帖子id
     * @param beUserId 被打赏用户id
     * @param score
     */
    @Override
    public void getReward(String userId, String postId, String beUserId,final int score ,int exceptionalType) {
         //|type|否|int|打赏不传 其它传1-签到 2-打赏 3-交易 4-兑换 5-赠送 6-任务|
        //exceptionalType 2：对贴子打赏 1：对人打赏 打赏时必传，其它不传|
        toSubscribe(HttpManager.getApi().getReward(userId,postId,beUserId,score,2 ,exceptionalType), new HttpSubscriber<JsonNull>() {
            @Override
            protected void _onStart() {
                mView.showLoading("打赏中...");
            }

            @Override
            protected void _onError(String message, int code) {
                mView.stopLoading();
                mView.showErrorMsg(message,null);
            }

            @Override
            public void onNext(JsonNull jsonNull) {
                mView.stopLoading();
                mView.getReward(score);
            }
        });
    }
}
