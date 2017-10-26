package com.honglu.future.ui.trade.fragment;

import com.honglu.future.R;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.ui.trade.contract.EntrustContract;
import com.honglu.future.ui.trade.presenter.EntrustPresenter;

/**
 * Created by zq on 2017/10/26.
 */

public class EntrustFragment extends BaseFragment<EntrustPresenter> implements EntrustContract.View {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_entrust;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        mTitle.setTitle(false, R.color.white, "委托中");
    }
}
