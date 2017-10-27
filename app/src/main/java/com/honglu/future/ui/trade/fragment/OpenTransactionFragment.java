package com.honglu.future.ui.trade.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.ui.login.activity.LoginActivity;
import com.honglu.future.ui.trade.adapter.OpenTransactionAdapter;
import com.honglu.future.ui.trade.bean.OpenTransactionListBean;
import com.honglu.future.ui.trade.contract.OpenTransactionContract;
import com.honglu.future.ui.trade.presenter.OpenTransactionPresenter;
import com.honglu.future.ui.usercenter.activity.ModifyUserActivity;
import com.honglu.future.util.Tool;
import com.honglu.future.widget.recycler.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zq on 2017/10/26.
 */

public class OpenTransactionFragment extends BaseFragment<OpenTransactionPresenter> implements OpenTransactionContract.View {
    @BindView(R.id.rv_open_transaction_list_view)
    RecyclerView mOpenTransactionListView;
    LinearLayout mTradeHeader;
    private OpenTransactionAdapter mOpenTransactionAdapter;
    private List<OpenTransactionListBean> mList;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_open_transaction;
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
        mOpenTransactionListView.setLayoutManager(new LinearLayoutManager(mContext));
        mOpenTransactionListView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mOpenTransactionAdapter = new OpenTransactionAdapter();
        View headView = LayoutInflater.from(mActivity).inflate(R.layout.item_trade_list_header, null);
        mTradeHeader = (LinearLayout) headView.findViewById(R.id.ll_trade_header);
        mOpenTransactionAdapter.addHeaderView(headView);
        mOpenTransactionListView.setAdapter(mOpenTransactionAdapter);
        setListener();
    }

    private void setListener() {
        mTradeHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!App.getConfig().getLoginStatus()) {
                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void initData() {
        mList = new ArrayList<>();
        for (int i = 0; i <= 10; i++) {
            OpenTransactionListBean bean = new OpenTransactionListBean();
            if (i % 2 == 0) {
                bean.setProductName("玉米1801");
                bean.setNum("338730");
                bean.setRiseNum("买涨1678");
                bean.setDownNum("买跌1677");
                bean.setRiseRadio("50%");
                bean.setDownRadio("50%");
                bean.setIsRest("0");
                mList.add(bean);
            } else {
                bean.setProductName("甲醇1801");
                bean.setNum("4009004");
                bean.setRiseNum("买涨2646");
                bean.setDownNum("买跌2645");
                bean.setRiseRadio("0%");
                bean.setDownRadio("100%");
                bean.setIsRest("1");
                mList.add(bean);
            }
        }
        mOpenTransactionAdapter.addData(mList);
    }
}
