package com.honglu.future.ui.recharge.activity;

import android.widget.ListView;

import com.honglu.future.R;
import com.honglu.future.base.BaseFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;


/**
 * Created by hefei on 2017/10/26.
 * 出入金明细页面
 */
public class InAndOutDetailFragment extends BaseFragment{
    @BindView(R.id.lv_listView)
    ListView mListView;
    @BindView(R.id.srl_refreshView)
    SmartRefreshLayout mSmartRefreshLayout;
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
    }


}
