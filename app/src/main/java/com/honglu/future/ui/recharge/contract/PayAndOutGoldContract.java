package com.honglu.future.ui.recharge.contract;


import com.honglu.future.base.BaseView;
import com.honglu.future.ui.usercenter.bean.BindCardBean;

import java.util.List;

/**
 * Created by hefei on 2017/10/26.
 * 充值提现页面
 */
public interface PayAndOutGoldContract{

    /**
     * view
     */
    public interface View extends BaseView{
        /**
         * 绑定银行卡的页面
         * @param list
         */
        void bindBnakList(List<BindCardBean> list);

        /**
         * 充值成功
         */
        void rechageSuccess();
        /**
         * 提现成功
         */
        void cashout();

        /**
         * 提现失败
         */
        void cashoutErr(String msg);

        /**
         * 获取银行资产
         * @param amount
         */
        void bindAssess(String amount);

    }

    /**
     * present
     */
    public interface Present{
        /**
         * 获取银行列表接口
         */
        void getBankList(String userId,String token);

        /**
         * 入金(充值)接口
         * @param userId
         * @param brokerBranchId
         * @param password
         * @param bankId
         * @param bankBranchId
         * @param bankAccount
         * @param bankPassword
         * @param amount
         * @param token
         */
        void rechage(String userId,String brokerBranchId,
                    String password, String bankId,
                    String bankBranchId, String bankAccount,
                    String bankPassword, String amount, String token);
        /**
         * 出金(提现)接口
         * @param userId
         * @param brokerBranchId
         * @param password
         * @param bankId
         * @param bankBranchId
         * @param bankAccount
         * @param bankPassword
         * @param amount
         * @param token
         */
        void cashout(String userId,String brokerBranchId,
                     String password, String bankId,
                     String bankBranchId, String bankAccount,
                     String bankPassword, String amount, String token);

        /**
         * 获取银行资产的接口
         * @param userId
         * @param brokerBranchId
         * @param password
         * @param bankId
         * @param bankBranchId
         * @param bankAccount
         * @param bankPassword
         * @param token
         */
        void getBalanceAsses(String userId,String brokerBranchId,
                     String password, String bankId,
                     String bankBranchId, String bankAccount,
                     String bankPassword,String token);

    }
}
