package com.honglu.future.ui.live.player;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.honglu.future.R;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.ui.live.adapter.MainPointAdapter;
import com.honglu.future.ui.live.bean.LivePointBean;
import com.honglu.future.widget.recycler.DividerItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;

import static com.honglu.future.util.ToastUtil.showToast;

/**
 * Created by zq on 2017/12/25.
 */

public class MainPointFragment extends BaseFragment<MainPointPresenter> implements MainPointContract.View {
    @BindView(R.id.point_listView)
    RecyclerView mListView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mSmartRefreshLayout;

    private MainPointAdapter mMainPointAdapter;
    private String mRoomId = "";
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mPresenter.getLivePointList(mRoomId);
            mHandler.postDelayed(this, 60 * 1000);
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main_point;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        initView();
    }

    private void initView() {
        mListView.setLayoutManager(new LinearLayoutManager(mContext));
        mListView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mMainPointAdapter = new MainPointAdapter();
        mListView.setAdapter(mMainPointAdapter);
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPresenter.getLivePointList(mRoomId);
            }
        });
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        if (mSmartRefreshLayout.isRefreshing()) {
            mSmartRefreshLayout.finishRefresh();
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mHandler.post(mRunnable);
        } else {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    public void setRoomId(String roomId) {
        mRoomId = roomId;
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }

    @Override
    public void onPause() {
        mHandler.removeCallbacks(mRunnable);
        super.onPause();
    }

    @Override
    public void onResume() {
        mHandler.post(mRunnable);
        super.onResume();
    }

    @Override
    public void getLivePointListSuccess(List<LivePointBean> list) {
        if (mSmartRefreshLayout.isRefreshing()) {
            mSmartRefreshLayout.finishRefresh();
        }
        if (list != null && list.size() > 0) {
            mMainPointAdapter.getData().clear();
            mMainPointAdapter.addData(list);
        }
    }


}
