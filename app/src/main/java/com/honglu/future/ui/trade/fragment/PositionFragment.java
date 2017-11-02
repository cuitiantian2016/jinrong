package com.honglu.future.ui.trade.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.ui.trade.adapter.PositionAdapter;
import com.honglu.future.ui.trade.contract.PositionContract;
import com.honglu.future.ui.trade.presenter.PositionPresenter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zq on 2017/10/26.
 */
public class PositionFragment extends BaseFragment<PositionPresenter> implements PositionContract.View {
    @BindView(R.id.lv_listView)
    ListView mListView;
    @BindView(R.id.srl_refreshView)
    SmartRefreshLayout mRefreshView;
    @BindView(R.id.tv_remarksEmpty)
    TextView mRemarksEmpty;
    private View mFooterEmptyView;

    private PositionAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_position;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    public void setEmptyView(boolean isEmpty){
        if (isEmpty){
            mRemarksEmpty.setVisibility(View.VISIBLE);
            mFooterEmptyView.setVisibility(View.VISIBLE);
            if (mListView.getFooterViewsCount() <=0 && mFooterEmptyView !=null){
                mListView.addFooterView(mFooterEmptyView);
            }
        }else {
            mRemarksEmpty.setVisibility(View.GONE);
            mFooterEmptyView.setVisibility(View.GONE);
            if (mListView.getFooterViewsCount() >0 && mFooterEmptyView !=null){
                mListView.removeFooterView(mFooterEmptyView);
            }
        }
    }

    @Override
    public void loadData() {
        mAdapter = new PositionAdapter(PositionFragment.this);
        View headView = LayoutInflater.from(mContext).inflate(R.layout.layout_trade_position_list_header, null);
        mFooterEmptyView = LayoutInflater.from(mContext).inflate(R.layout.layout_trade_position_emptyview,null);
        mListView.addHeaderView(headView);
        mListView.addFooterView(mFooterEmptyView);
        mListView.setAdapter(mAdapter);
        setEmptyView(true);

        mRefreshView.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                List<String> mList = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    mList.add(new String("1111"));
                }
                mAdapter.notifyDataChanged(true, mList);
                mRefreshView.finishLoadmore();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                List<String> mList = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    mList.add(new String("1111"));
                }
                mAdapter.notifyDataChanged(false, mList);
                mRefreshView.finishRefresh();
            }
        });
    }
}
