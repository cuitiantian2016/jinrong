package com.honglu.future.ui.trade.fragment;

import com.honglu.future.R;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.ui.trade.contract.ClosePositionContract;
import com.honglu.future.ui.trade.presenter.ClosePositionPresenter;

/**
 * Created by zq on 2017/10/26.
 */

public class ClosePositionFragment extends BaseFragment<ClosePositionPresenter> implements ClosePositionContract.View {
    @Override
    public int getLayoutId() {
        return R.layout.fragment_close_position;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        mTitle.setTitle(false, R.color.white, "已平仓");
    }
}
