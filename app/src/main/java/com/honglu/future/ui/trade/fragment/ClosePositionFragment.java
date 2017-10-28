package com.honglu.future.ui.trade.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.honglu.future.R;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.ui.trade.adapter.ClosePositionAdapter;
import com.honglu.future.ui.trade.adapter.OpenTransactionAdapter;
import com.honglu.future.ui.trade.bean.ClosePositionListBean;
import com.honglu.future.ui.trade.bean.OpenTransactionListBean;
import com.honglu.future.ui.trade.contract.ClosePositionContract;
import com.honglu.future.ui.trade.presenter.ClosePositionPresenter;
import com.honglu.future.widget.recycler.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zq on 2017/10/26.
 */

public class ClosePositionFragment extends BaseFragment<ClosePositionPresenter> implements ClosePositionContract.View {
    @BindView(R.id.rv_position)
    RecyclerView mPositionListView;
    private ClosePositionAdapter mClosePositionAdapter;
    private List<ClosePositionListBean> mList;

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
        initView();
        initData();
    }

    private void initView() {
        mPositionListView.setLayoutManager(new LinearLayoutManager(mContext));
        mPositionListView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mClosePositionAdapter = new ClosePositionAdapter();
        mPositionListView.setAdapter(mClosePositionAdapter);
    }

    private void initData() {
        mList = new ArrayList<>();
        for (int i = 0; i <= 10; i++) {
            ClosePositionListBean bean = new ClosePositionListBean();
            if (i % 2 == 0) {
                bean.setProductName("玉米1801");
                bean.setBuyHands("买涨1手");
                bean.setAveragePrice("2754");
                bean.setNewPrice("2765");
                bean.setProfit("+110");
                bean.setBuyRiseDown("rise");
                mList.add(bean);
            } else {
                bean.setProductName("甲醇1801");
                bean.setBuyHands("买跌2手");
                bean.setAveragePrice("2754");
                bean.setNewPrice("2765");
                bean.setProfit("-220");
                bean.setBuyRiseDown("down");
                mList.add(bean);
            }
        }
        mClosePositionAdapter.addData(mList);
    }
}
