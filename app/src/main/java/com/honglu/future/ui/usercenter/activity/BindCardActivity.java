package com.honglu.future.ui.usercenter.activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.config.Constant;
import com.honglu.future.ui.usercenter.adapter.BindCardAdapter;
import com.honglu.future.ui.usercenter.bean.BindCardBean;
import com.honglu.future.ui.usercenter.contract.BindCardContract;
import com.honglu.future.ui.usercenter.presenter.BindCardPresenter;
import com.honglu.future.util.SpUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 绑卡列表（银行卡列表）
 * Created by zhuaibing on 2017/11/11
 */

public class BindCardActivity extends BaseActivity<BindCardPresenter> implements BindCardContract.View {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.bindcard_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.bindcard_smart_view)
    SmartRefreshLayout mRefreshView;

    private BindCardAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bind_card;
    }

    @Override
    public void initPresenter() {
          mPresenter.init(this);
    }

    @Override
    public void loadData() {
        mTitle.setTitle(true, R.mipmap.ic_back_black, R.color.white, "银行卡");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setFocusable(false);
        mAdapter = new BindCardAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mRefreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPresenter.getBindCardInfo(SpUtil.getString(Constant.CACHE_TAG_UID),SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN));
                mRefreshView.finishRefresh();
            }
        });

        mPresenter.getBindCardInfo(SpUtil.getString(Constant.CACHE_TAG_UID),SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN));
    }


    @Override
    public void getBindCardInfo(List<BindCardBean> list) {
        if (list !=null){
            mAdapter.getData().clear();
            mAdapter.addData(list);
        }
    }
}
