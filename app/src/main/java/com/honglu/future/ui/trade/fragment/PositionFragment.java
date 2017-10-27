package com.honglu.future.ui.trade.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.honglu.future.R;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.ui.trade.adapter.PositionAdapter;
import com.honglu.future.ui.trade.contract.PositionContract;
import com.honglu.future.ui.trade.presenter.PositionPresenter;
import com.honglu.future.widget.recycler.DividerItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zq on 2017/10/26.
 */

public class PositionFragment extends BaseFragment<PositionPresenter> implements PositionContract.View {
    @BindView(R.id.lv_listView)
    ListView lvListView;
    @BindView(R.id.srl_refreshView)
    SmartRefreshLayout srlRefreshView;


    private PositionAdapter mAdapter;


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
        mAdapter = new PositionAdapter(getActivity());
        View headView = LayoutInflater.from(mActivity).inflate(R.layout.layout_trade_position_list_header, null);
        lvListView.addHeaderView(headView);
        lvListView.setAdapter(mAdapter);
        List<String> mList = new ArrayList<>();
        for (int i = 0 ; i < 50 ; i++){
            mList.add(new String("1111"));
        }
        mAdapter.notifyDataChanged(false,mList);
    }
}
