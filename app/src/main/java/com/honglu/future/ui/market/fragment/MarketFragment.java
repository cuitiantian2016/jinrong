package com.honglu.future.ui.market.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.honglu.future.R;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.ui.market.adapter.MarketClassificationAdapter;
import com.honglu.future.ui.market.adapter.MarketListAdapter;
import com.honglu.future.ui.market.bean.AllClassificationBean;
import com.honglu.future.ui.market.bean.QuotesItemBean;
import com.honglu.future.ui.market.contract.MarketContract;
import com.honglu.future.ui.market.presenter.MarketPresenter;
import com.honglu.future.widget.recycler.BaseRecyclerAdapter;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zhuaibing on 2017/10/25
 */

public class MarketFragment extends BaseFragment<MarketPresenter> implements MarketContract.View{
    @BindView(R.id.market_recycler)
    RecyclerView mMarketRecycler; //行情列表
    @BindView(R.id.all_market_classification_recycler)
    RecyclerView mClassificationRecycler;
    private List<QuotesItemBean> mMarketList = new ArrayList<QuotesItemBean>();
    private List<AllClassificationBean> allQuotesNames = new ArrayList<AllClassificationBean>();
    public static MarketFragment marketFragment;


    private MarketListAdapter mMarketListAdapter;
    private MarketClassificationAdapter mMarketClassificationAdapter;


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
    public void loadData() {
        //.setTitle(false, R.color.white, "行情");
        for (int i = 0; i < 5; i++) {
            AllClassificationBean allClassificationBean = new AllClassificationBean();
            if (i == 0){
                allClassificationBean.setClassificationName("自选");
                allClassificationBean.setOnClick(true);
            }else if (i == 1){
                allClassificationBean.setClassificationName("主力合约");
                allClassificationBean.setOnClick(false);
            }else if (i == 2){
                allClassificationBean.setClassificationName("上期所");
                allClassificationBean.setOnClick(false);
            }else if (i == 3){
                allClassificationBean.setClassificationName("大商所");
                allClassificationBean.setOnClick(false);
            }else if (i == 4){
                allClassificationBean.setClassificationName("郑商所");
                allClassificationBean.setOnClick(false);
            }else {
            }
            allQuotesNames.add(allClassificationBean);
        }
        for (int i = 10; i < 20 ; i++) {
            QuotesItemBean itemBean = new QuotesItemBean();
            itemBean.setContractName("玉米10"+i);
            itemBean.setLatestPrice("20"+i);
            itemBean.setQuoteChange("+0.18%");
            itemBean.setHavedPositions("99658");
            mMarketList.add(itemBean);
        }
        initView();
    }
    private void initView(){
        LinearLayoutManager ms= new LinearLayoutManager(mContext);
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);// 设置 recyclerview 布局方式为横向布局
        mClassificationRecycler.setLayoutManager(ms);
        mClassificationRecycler.setFocusable(false);
        mMarketClassificationAdapter = new MarketClassificationAdapter();
        mClassificationRecycler.setAdapter(mMarketClassificationAdapter);
        mMarketClassificationAdapter.clearData();
        mMarketClassificationAdapter.addData(allQuotesNames);
        mMarketClassificationAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClick() {
            @Override
            public void onItemClick(View view, int position) {
                for (int i = 0; i < allQuotesNames.size(); i++) {
                    allQuotesNames.get(i).setOnClick((i == position)?true:false);
                }
                mMarketClassificationAdapter.notifyDataSetChanged();
            }
        });



        LinearLayout footerView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_quotes_optional_add, null);
        mMarketRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mMarketRecycler.setFocusable(false);
        mMarketListAdapter = new MarketListAdapter();
        mMarketRecycler.setAdapter(mMarketListAdapter);
        mMarketListAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClick() {
            @Override
            public void onItemClick(View view, int position) {
                /*Intent intent = new Intent(mActivity, WebViewActivity.class);
                intent.putExtra("url", mActivityListAdapter.getData().get(position).getReUrl());
                startActivity(intent);*/
            }
        });
        mMarketListAdapter.clearData();
        mMarketListAdapter.addData(mMarketList);
        mMarketListAdapter.addFooterView(footerView, DensityUtil.dp2px(85));
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
}
