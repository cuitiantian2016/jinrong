package com.honglu.future.ui.trade.details;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;

/**
 * 建仓详情
 * Created by zhuaibing on 2017/11/6
 */

public class OpenTransactionDetailsActivity extends BaseActivity<OpenTransactionDetailsPresenter> implements OpenTransactionDetailsContract.View{

    @Override
    public int getLayoutId() {
        return R.layout.activity_open_transaction_details;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        mTitle.setTitle(true,R.mipmap.ic_back_black,null,R.color.color_white,"建仓详情");
    }
}
