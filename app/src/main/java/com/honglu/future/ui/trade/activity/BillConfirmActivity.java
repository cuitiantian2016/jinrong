package com.honglu.future.ui.trade.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.ui.usercenter.adapter.HistoryRecordsAdapter;
import com.honglu.future.ui.usercenter.bean.HistoryRecordsBean;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.widget.DrawableCenterTextView;
import com.honglu.future.widget.recycler.DividerItemDecoration;
import com.honglu.future.widget.tab.CommonTabLayout;
import com.honglu.future.widget.tab.CustomTabEntity;
import com.honglu.future.widget.tab.SimpleOnTabSelectListener;
import com.honglu.future.widget.tab.TabEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zq on 2017/11/2.
 */

public class BillConfirmActivity extends BaseActivity {
    @BindView(R.id.trade_common_tab_layout)
    CommonTabLayout mCommonTabLayout;
    @BindView(R.id.rv_records)
    RecyclerView mRecordsListView;
    @BindView(R.id.tv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_right)
    DrawableCenterTextView mRightTitle;
    @BindView(R.id.tv_confirm)
    TextView mTvConfirm;
    private ArrayList<CustomTabEntity> mTabList;
    private HistoryRecordsAdapter mHistoryRecordsAdapter;
    private List<HistoryRecordsBean> mList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bill_confirm;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        initView();
        initData();
    }

    private void initView() {
        mIvBack.setVisibility(View.VISIBLE);
        mTitle.setTitle(false, R.color.white, "账单确认");
        mRightTitle.setText("客服");
        int screenWidthDip = DeviceUtils.px2dip(mContext, DeviceUtils.getScreenWidth(mContext));
        int indicatorWidth = (int) (screenWidthDip * 0.12f);
        mCommonTabLayout.setIndicatorWidth(indicatorWidth);
        //添加tab实体
        addTabEntities();
        mRecordsListView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecordsListView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mHistoryRecordsAdapter = new HistoryRecordsAdapter();
        mRecordsListView.setAdapter(mHistoryRecordsAdapter);
    }

    private void initData() {
        mList = new ArrayList<>();
        for (int i = 0; i <= 10; i++) {
            HistoryRecordsBean bean = new HistoryRecordsBean();
            if (i % 2 == 0) {
                bean.setName("玉米1801");
                bean.setBuyHands("1手");
                bean.setBuyType("rise");
                bean.setBuildPrice("1665");
                bean.setServicePrice("6.66");
                mList.add(bean);
            } else {
                bean.setName("甲醇1801");
                bean.setBuyHands("3手");
                bean.setBuyType("down");
                bean.setBuildPrice("2665");
                bean.setServicePrice("8.66");
                mList.add(bean);
            }
        }
        mHistoryRecordsAdapter.addData(mList);
    }

    private void addTabEntities() {
        mTabList = new ArrayList<>();
        mTabList.add(new TabEntity(mContext.getString(R.string.trade_build)));
        mTabList.add(new TabEntity(mContext.getString(R.string.actual_close)));
        mTabList.add(new TabEntity(mContext.getString(R.string.trade_hold)));
        mCommonTabLayout.setTabData(mTabList);
        mCommonTabLayout.setOnTabSelectListener(new SimpleOnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                super.onTabSelect(position);
            }
        });
    }

    @OnClick({R.id.tv_back, R.id.tv_right, R.id.tv_confirm})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_right:
                //// TODO: 2017/11/2 跳转客服 
                break;
            case R.id.tv_confirm:
                finish();
                break;
        }
    }
}
