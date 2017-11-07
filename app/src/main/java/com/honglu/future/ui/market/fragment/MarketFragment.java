package com.honglu.future.ui.market.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.honglu.future.R;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.config.Constant;
import com.honglu.future.ui.market.activity.OptionalQuotesActivity;
import com.honglu.future.ui.market.adapter.MarketListAdapter;
import com.honglu.future.ui.market.contract.MarketContract;
import com.honglu.future.ui.market.presenter.MarketPresenter;
import com.honglu.future.widget.recycler.BaseRecyclerAdapter;
import com.honglu.future.widget.tab.CustomTabEntity;
import com.honglu.future.widget.tab.HorizontalTabLayout;
import com.honglu.future.widget.tab.TabEntity;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zhuaibing on 2017/10/25
 */

public class MarketFragment extends BaseFragment<MarketPresenter> implements MarketContract.View {

    @BindView(R.id.market_recycler)
    RecyclerView mMarketRecycler; //行情列表
    @BindView(R.id.market_common_tab_layout)
    HorizontalTabLayout mCommonTab;

    private MarketListAdapter mMarketListAdapter;

    public static MarketFragment marketFragment;

    public static MarketFragment getInstance() {
        if (marketFragment == null) {
            marketFragment = new MarketFragment();
        }
        return marketFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_market_layout;
    }

    @Override
    public void initPresenter() {

    }


    @Override
    public void showLoading(String content) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorMsg(String msg, String type) {

    }

    @Override
    public void loadData() {
        ArrayList<CustomTabEntity> mList = new ArrayList<>();
        mList.add(new TabEntity("自选"));
        mList.add(new TabEntity("主力合约"));
        mList.add(new TabEntity("上期所"));
        mList.add(new TabEntity("郑商所"));
        mList.add(new TabEntity("大商所"));
        mCommonTab.setTabData(mList);


        LinearLayout footerView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_quotes_optional_add, null);
        mMarketRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mMarketRecycler.setFocusable(false);
        mMarketListAdapter = new MarketListAdapter();
        mMarketRecycler.setAdapter(mMarketListAdapter);
        mMarketListAdapter.clearData();
        mMarketListAdapter.addData(getTestList());
        mMarketListAdapter.addFooterView(footerView, DensityUtil.dp2px(85));


        mMarketListAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClick() {
            @Override
            public void onItemClick(View view, int position) {
                /*Intent intent = new Intent(mActivity, WebViewActivity.class);
                intent.putExtra("url", mActivityListAdapter.getData().get(position).getReUrl());
                startActivity(intent);*/
            }
        });

        footerView.findViewById(R.id.text_add_qptional).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(OptionalQuotesActivity.class, Constant.OptionalQuotesLITEPALl);
            }
        });
    }


    private List<String> getTestList() {
        List<String> mList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            mList.add("1111" + i);
        }
        return mList;
    }
}
