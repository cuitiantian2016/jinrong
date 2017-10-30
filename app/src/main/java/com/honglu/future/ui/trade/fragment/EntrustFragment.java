package com.honglu.future.ui.trade.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.honglu.future.R;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.ui.trade.adapter.ClosePositionAdapter;
import com.honglu.future.ui.trade.adapter.EntrustAdapter;
import com.honglu.future.ui.trade.bean.ClosePositionListBean;
import com.honglu.future.ui.trade.bean.EntrustBean;
import com.honglu.future.ui.trade.contract.EntrustContract;
import com.honglu.future.ui.trade.presenter.EntrustPresenter;
import com.honglu.future.widget.popupwind.BottomPopupWindow;
import com.honglu.future.widget.recycler.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zq on 2017/10/26.
 */

public class EntrustFragment extends BaseFragment<EntrustPresenter> implements EntrustContract.View {
    @BindView(R.id.rv_entrust_list_view)
    RecyclerView mEntrustListView;
    private EntrustAdapter mEntrustAdapter;
    private List<EntrustBean> mList;

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
        initView();
        initData();
    }

    private void initView() {
        mEntrustListView.setLayoutManager(new LinearLayoutManager(mContext));
        mEntrustListView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mEntrustAdapter = new EntrustAdapter();
        mEntrustListView.setAdapter(mEntrustAdapter);
    }

    private void initData() {
        mList = new ArrayList<>();
        for (int i = 0; i <= 10; i++) {
            EntrustBean bean = new EntrustBean();
            if (i % 2 == 0) {
                bean.setName("玉米1801");
                bean.setBuyHands("买涨1手");
                bean.setEntrustType("open");
                bean.setEnturstPrice("1665");
                bean.setEntrustDate("2017-10-20");
                bean.setServiceCharge("6.66");
                bean.setLimitDate("当日有效");
                bean.setBond("1665");
                mList.add(bean);
            } else {
                bean.setName("甲醇1801");
                bean.setBuyHands("买涨3手");
                bean.setEntrustType("close");
                bean.setEnturstPrice("2665");
                bean.setEntrustDate("2017-10-16");
                bean.setServiceCharge("8.88");
                bean.setLimitDate("当日有效");
                bean.setBond("2865");
                mList.add(bean);
            }
        }
        mEntrustAdapter.addData(mList);
    }
}
