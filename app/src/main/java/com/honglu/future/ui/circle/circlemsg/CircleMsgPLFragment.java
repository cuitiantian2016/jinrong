package com.honglu.future.ui.circle.circlemsg;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.widget.recycler.DividerItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zhuaibing on 2017/12/7
 */

public class CircleMsgPLFragment extends BaseFragment<CircleMsgPresenter> implements CircleMsgContract.View{
    @BindView(R.id.refresh_view)
    SmartRefreshLayout mRefreshView;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.et_input)
    EditText mInput;
    @BindView(R.id.tv_send)
    TextView mSend;

    private CircleMsgPLAdapter mAdapter;

    @Override
    public void initPresenter() {
       mPresenter.init(CircleMsgPLFragment.this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_circle_msg;
    }

    public void getData(){

    }

    @Override
    public void loadData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mAdapter = new CircleMsgPLAdapter();
        mRecyclerView.setAdapter(mAdapter);

        List<String> list = new ArrayList<>();
        for (int i = 1 ; i < 20 ; i++){
            list.add("---"+i);
        }
        mAdapter.addData(list);
    }
}
