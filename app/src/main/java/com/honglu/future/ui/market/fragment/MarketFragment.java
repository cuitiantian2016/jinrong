package com.honglu.future.ui.market.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.honglu.future.R;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.config.Constant;
import com.honglu.future.ui.market.activity.OptionalQuotesActivity;
import com.honglu.future.ui.market.adapter.MarketListAdapter;
import com.honglu.future.ui.market.bean.MarketnalysisBean;
import com.honglu.future.ui.market.contract.MarketContract;
import com.honglu.future.ui.market.presenter.MarketPresenter;
import com.honglu.future.util.SpUtil;
import com.honglu.future.widget.recycler.BaseRecyclerAdapter;
import com.honglu.future.widget.tab.CustomTabEntity;
import com.honglu.future.widget.tab.HorizontalTabLayout;
import com.honglu.future.widget.tab.SimpleOnTabSelectListener;
import com.honglu.future.widget.tab.TabEntity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zhuaibing on 2017/10/25
 */

public class MarketFragment extends BaseFragment<MarketPresenter> implements MarketContract.View {
    public static final String ZXHQ_TYPE = "zxhq_type";//自选行情
    public static final String ZLHY_TYPE = "zlhy_type";//主力合约

    @BindView(R.id.market_recycler)
    RecyclerView mMarketRecycler; //行情列表
    @BindView(R.id.market_common_tab_layout)
    HorizontalTabLayout mCommonTab;
    @BindView(R.id.market_smart_view)
    SmartRefreshLayout mRefreshView;

    private List<MarketnalysisBean.ListBean.QuotationDataListBean> mZxMarketList = new ArrayList<>();//自选行情
    private List<MarketnalysisBean.ListBean.QuotationDataListBean> mZlhyMarketList =  new ArrayList<>();//主力合约
    private List<MarketnalysisBean.ListBean> mAllMarketList = new ArrayList<>();//除自选外全部数据
    private String mTabSelectType = ZXHQ_TYPE;

    private ArrayList<CustomTabEntity> mTabList = new ArrayList<>();

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
        mPresenter.init(this);
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
        List<MarketnalysisBean.ListBean.QuotationDataListBean> zxMarketList = getZxMarketList();
        if (zxMarketList !=null && zxMarketList.size() > 0){
            mZxMarketList = getZxMarketList();
        }
        LinearLayout footerView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_quotes_optional_add, null);
        mMarketRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mMarketRecycler.setFocusable(false);
        mMarketListAdapter = new MarketListAdapter();
        mMarketRecycler.setAdapter(mMarketListAdapter);
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
                if (mAllMarketList !=null && mAllMarketList.size() > 0){
                    Intent intent = new Intent(getActivity(),OptionalQuotesActivity.class);
                    intent.putExtra("data",(Serializable) mAllMarketList);
                    startActivity(intent);
                }
            }
        });

        mCommonTab.setOnTabSelectListener(new SimpleOnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mTabSelectType = mTabList.get(position).getTabType();
                setMarketData(mTabSelectType);
            }
        });

        mRefreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPresenter.getMarketData();
                mRefreshView.finishRefresh();
            }
        });

        mPresenter.getMarketData();
    }

    @Override
    public void onResume() {
        super.onResume();
        List<MarketnalysisBean.ListBean.QuotationDataListBean> zxMarketList = getZxMarketList();
        if (zxMarketList !=null && zxMarketList.size() > 0){
            mZxMarketList = getZxMarketList();
            if (mTabSelectType == ZXHQ_TYPE){
               setMarketData(mTabSelectType);
            }
        }
    }

    //行情请求数据
    @Override
    public void getMarketData(MarketnalysisBean alysisBean) {
        if (alysisBean == null || alysisBean.getList() == null) {
            return;
        }
        this.mAllMarketList = alysisBean.getList();

        //创建tab
        if (mTabList.size() <= 0) {
            mTabList.clear();
            mTabList.add(new TabEntity("自选", ZXHQ_TYPE));
            mTabList.add(new TabEntity("主力合约", ZLHY_TYPE));
            for (MarketnalysisBean.ListBean bean : mAllMarketList) {
                mTabList.add(new TabEntity(bean.getExchangeName(), bean.getExcode()));
            }
            mCommonTab.setTabData(mTabList);
        }

        //添加主力合约 list
         mZlhyMarketList.clear();
        for (MarketnalysisBean.ListBean bean : mAllMarketList) {
            if (bean.getQuotationDataList() !=null && bean.getQuotationDataList().size() > 0){
                mZlhyMarketList.addAll(bean.getQuotationDataList());
            }
        }

         setMarketData(mTabSelectType);
    }


    //根据 type 设置对应adapter 数据
    private void setMarketData(String type) {
        if (TextUtils.isEmpty(type)) {
            return;
        }
        mMarketListAdapter.clearData();
        if (type.equals(ZXHQ_TYPE)) {//自选行情
            mMarketListAdapter.addData(mZxMarketList);

        } else if (type.equals(ZLHY_TYPE)) { //主力合约
            mMarketListAdapter.addData(mZlhyMarketList);

        } else {
            if (mAllMarketList != null || mAllMarketList.size() > 0) {
                for (MarketnalysisBean.ListBean alysisBean : mAllMarketList) {
                    if (type.equals(alysisBean.getExcode())) {
                        mMarketListAdapter.addData(alysisBean.getQuotationDataList());
                        break;
                    }
                }
            }
        }
    }


    //获取自选数据
    private List<MarketnalysisBean.ListBean.QuotationDataListBean> getZxMarketList() {
        String zxMarketJson = SpUtil.getString(Constant.ZX_MARKET_KEY);
        if (!TextUtils.isEmpty(zxMarketJson)) {
            Gson gson = new Gson();
            try {
                List<MarketnalysisBean.ListBean.QuotationDataListBean> mZxMarketList = gson.fromJson(zxMarketJson,
                        new TypeToken<List<MarketnalysisBean.ListBean.QuotationDataListBean>>() {
                        }.getType());
                return mZxMarketList;
            } catch (JsonSyntaxException e) {
            }
        }
        return null;
    }
}
