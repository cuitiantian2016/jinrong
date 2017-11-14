package com.honglu.future.ui.recharge.activity;

import android.support.v7.widget.RecyclerView;

import com.honglu.future.R;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.base.BasePresenter;
import com.honglu.future.base.IBaseView;
import com.honglu.future.config.Constant;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.recharge.adapter.InAndOutDetailAdapter;
import com.honglu.future.ui.recharge.bean.RechangeDetailData;
import com.honglu.future.util.SpUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;


/**
 * Created by hefei on 2017/10/26.
 * 出入金明细页面
 */
public class InAndOutDetailFragment extends BaseFragment{
    @BindView(R.id.lv_listView)
    RecyclerView mListView;
    @BindView(R.id.srl_refreshView)
    SmartRefreshLayout mSmartRefreshLayout;
    private InAndOutDetailAdapter inAndOutDetailAdapter;
    int page = 0;
    private BasePresenter<InAndOutDetailFragment> basePresenter;

    public static InAndOutDetailFragment getInstance() {
        return new InAndOutDetailFragment();
    }
    @Override
    public int getLayoutId() {
        return R.layout.fragment_in_and_out_gold_detail;
    }
    @Override
    public void initPresenter() {
    }
    @Override
    public void loadData() {
        inAndOutDetailAdapter = new InAndOutDetailAdapter();
        mListView.setAdapter(inAndOutDetailAdapter);
        basePresenter = new BasePresenter<InAndOutDetailFragment>(InAndOutDetailFragment.this) {
            @Override
            public void getData() {
                super.getData();
                toSubscribe(HttpManager.getApi().getDetail(SpUtil.getString(Constant.CACHE_TAG_UID), page, 10, SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN)),
                        new HttpSubscriber<List<RechangeDetailData>>() {
                    @Override
                    protected void _onNext(List<RechangeDetailData> o) {
                        super._onNext(o);
                        boolean isRefresh = true;
                        if (page != 0){
                            isRefresh = false;
                        }
                        if (o.size()>=10){
                            ++page;
                        }
                        mView.setList(o,isRefresh);
                    }
                });
            }
        };
        basePresenter.getData();
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                basePresenter.getData();
            }
        });
    }

    private void setList(List<RechangeDetailData> o,boolean isRefresh){
        mSmartRefreshLayout.finishRefresh();
        if (isRefresh){
            inAndOutDetailAdapter.clearData();
        }
        inAndOutDetailAdapter.addData(o);
    }

}
