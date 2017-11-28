package com.honglu.future.ui.details.presenter;

import com.google.gson.JsonNull;
import com.honglu.future.base.BasePresenter;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.details.bean.ConsultDetailsBean;
import com.honglu.future.ui.details.bean.InformationCommentBean;
import com.honglu.future.ui.details.contract.ConsultDetailsContract;
import com.honglu.future.util.ToastUtil;

import java.util.List;

/**
 * Created by hc on 2017/10/24.
 */

public class ConsultDetailsPresenter extends BasePresenter<ConsultDetailsContract.View>
        implements ConsultDetailsContract.Presenter{
    @Override
    public void getMessageData(String messageId) {
        toSubscribe(HttpManager.getApi().getMessageData(messageId), new HttpSubscriber<ConsultDetailsBean>() {
            @Override
            protected void _onNext(ConsultDetailsBean o) {
                super._onNext(o);
                mView.bindData(o);
            }
        });
    }

    @Override
    public void praiseMessage(String messageId, String userId) {
        toSubscribe(HttpManager.getApi().praiseMessage(messageId, userId), new HttpSubscriber<List<String>>() {
            @Override
            protected void _onNext(List<String> o) {
                super._onNext(o);
                mView.praiseSuccess(o);
            }

            @Override
            protected void _onError(String message) {
                super._onError(message);
                ToastUtil.show(message);
            }
        });
    }
    @Override
    public void commentMessage(String userID, String messageId, String repayUserID,String content) {
        toSubscribe(HttpManager.getApi().getInformationReply(messageId, userID, repayUserID, content), new HttpSubscriber<JsonNull>() {
            @Override
            protected void _onNext(JsonNull o) {
                super._onNext(o);
                mView.commentSuccess();
            }
            @Override
            protected void _onError(String message) {
                super._onError(message);
                mView.showErrorMsg(message,null);
            }
        });

    }
    @Override
    public void getReplyList(final String messageId) {
        toSubscribe(HttpManager.getApi().getInformationComment(messageId), new HttpSubscriber<List<InformationCommentBean>>() {
            @Override
            protected void _onNext(List<InformationCommentBean> o) {
                super._onNext(o);
                mView.bindReplyList(o);
            }

            @Override
            protected void _onError(String message) {
                super._onError(message);
                mView.showErrorMsg(message,null);
            }
        });
    }
}
