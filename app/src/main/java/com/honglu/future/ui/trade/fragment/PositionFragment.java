package com.honglu.future.ui.trade.fragment;

import com.honglu.future.R;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.ui.trade.contract.PositionContract;
import com.honglu.future.ui.trade.presenter.PositionPresenter;

/**
 * Created by zq on 2017/10/26.
 */

public class PositionFragment extends BaseFragment<PositionPresenter> implements PositionContract.View {
    @Override
    public int getLayoutId() {
        return R.layout.fragment_position;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        mTitle.setTitle(false, R.color.white, "持仓");
    }
}
