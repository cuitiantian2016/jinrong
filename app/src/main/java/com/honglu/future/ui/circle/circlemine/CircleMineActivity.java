package com.honglu.future.ui.circle.circlemine;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.widget.recycler.DividerItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;

/**
 * Created by zq on 2017/12/7.
 */

public class CircleMineActivity extends BaseActivity<CircleMinePresenter> implements CircleMineContract.View {
    @BindView(R.id.recyclerView)
    RecyclerView mBbsRecyclerView;
    @BindView(R.id.refreshView)
    SmartRefreshLayout mSmartRefresh;

    private CircleMineBbsAdapter mCircleMineBbsAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_circle_mine;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        mTitle.setTitle(true, R.color.trans, "");
        mBbsRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBbsRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        View headView = LayoutInflater.from(mActivity).inflate(R.layout.circle_mine_head_layout, null);
        mCircleMineBbsAdapter = new CircleMineBbsAdapter();
        mCircleMineBbsAdapter.addHeaderView(headView);
        mBbsRecyclerView.setAdapter(mCircleMineBbsAdapter);
    }
}
