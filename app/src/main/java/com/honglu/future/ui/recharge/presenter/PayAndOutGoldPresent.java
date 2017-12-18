package com.honglu.future.ui.recharge.presenter;

import com.google.gson.JsonNull;
import com.honglu.future.base.BasePresenter;
import com.honglu.future.config.Constant;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.recharge.bean.AssesData;
import com.honglu.future.ui.recharge.contract.PayAndOutGoldContract;
import com.honglu.future.ui.usercenter.bean.BindCardBean;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ToastUtil;

import java.util.IllegalFormatCodePointException;
import java.util.List;


/**
 * Created by hefei on 2017/10/26.
 * 充值提现页面
 */
public class PayAndOutGoldPresent extends BasePresenter<PayAndOutGoldContract.View> implements PayAndOutGoldContract.Present{
    @Override
    public void getBankList(String userId, String token) {
        toSubscribe(HttpManager.getApi().geBindCardData(userId, token, SpUtil.getString(Constant.COMPANY_TYPE)), new HttpSubscriber<List<BindCardBean>>() {
            @Override
            protected void _onNext(List<BindCardBean> o) {
                super._onNext(o);
                mView.bindBnakList(o);
            }

            @Override
            protected void _onError(String message) {
                super._onError(message);
                mView.bindBankErr(message);
            }
        });
    }

    @Override
    public void rechage(String userId, String brokerBranchId, String password, String bankId, String bankBranchId, String bankAccount, String bankPassword, String amount, String token) {
        toSubscribe(HttpManager.getApi().getRechargeAsses(userId, brokerBranchId, password, bankId, bankBranchId, bankAccount, bankPassword, amount, token,SpUtil.getString(Constant.COMPANY_TYPE))
                , new HttpSubscriber<JsonNull>() {
                    @Override
                    protected void _onNext(JsonNull o) {
                        super._onNext(o);
                        mView.rechageSuccess();
                    }

                    @Override
                    protected void _onError(String message) {
                        super._onError(message);
                        ToastUtil.show(message);
                    }
                });
    }

    @Override
    public void cashout(String userId, String brokerBranchId, String password, String bankId, String bankBranchId, String bankAccount, String bankPassword, String amount, String token) {
        toSubscribe(HttpManager.getApi().getCashoutAsses(userId, brokerBranchId, password, bankId, bankBranchId, bankAccount, bankPassword, amount, token,SpUtil.getString(Constant.COMPANY_TYPE))
                , new HttpSubscriber<JsonNull>() {
                    @Override
                    protected void _onNext(JsonNull o) {
                        super._onNext(o);
                        mView.cashout();
                    }

                    @Override
                    protected void _onError(String message, int code) {
                        super._onError(message, code);
                        mView.cashoutErr(message);
                    }
                });
    }

    @Override
    public void getBalanceAsses(String userId, String brokerBranchId, String password, String bankId, String bankBranchId, String bankAccount, String bankPassword, String token) {
        toSubscribe(HttpManager.getApi().getBalanceAsses(userId, brokerBranchId, password, bankId, bankBranchId, bankAccount, bankPassword,token,SpUtil.getString(Constant.COMPANY_TYPE))
                , new HttpSubscriber<AssesData>() {
                    @Override
                    protected void _onNext(AssesData o) {
                        super._onNext(o);
                        mView.bindAssess(o);
                    }

                    @Override
                    protected void _onError(String message) {
                        super._onError(message);
                        ToastUtil.show(message);
                    }
                });
    }
}
