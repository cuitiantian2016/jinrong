package com.honglu.future.ui.trade.fragment;

import android.view.View;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.ui.trade.contract.TradeContract;
import com.honglu.future.ui.trade.presenter.TradePresenter;

/**
 * Created by zq on 2017/10/24.
 */

public class TradeFragment extends BaseFragment<TradePresenter> implements View.OnClickListener,
        TradeContract.View {

    public static TradeFragment tradeFragment;

    public static TradeFragment getInstance() {
        if (tradeFragment == null) {
            tradeFragment = new TradeFragment();
        }
        return tradeFragment;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void showLoading(String content) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorMsg(String msg, String type) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_trade_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        initView();
    }

    private void initView() {
        mTitle.setTitle(false, R.color.white, "交易");
    }
}
