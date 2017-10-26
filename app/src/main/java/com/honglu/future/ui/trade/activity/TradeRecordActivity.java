package com.honglu.future.ui.trade.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.ui.trade.adapter.TradeRecordAdapter;
import com.honglu.future.ui.trade.presenter.TradeRecordPresenter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 交易记录
 * Created by zhuaibing on 2017/10/26
 */

public class TradeRecordActivity extends BaseActivity<TradeRecordPresenter> implements View.OnClickListener{


    @BindView(R.id.lv_listView)
    ListView lvListView;
    @BindView(R.id.refreshView)
    SmartRefreshLayout refreshView;

    private View tabJcLayout;
    private TextView tabJcText;
    private View tabJcLine;
    private View tabCcLayout;
    private TextView tabCcText;
    private View tabCcLine;
    private View tabRLayout;
    private TextView tabRText;
    private View tabRLine;

    private TradeRecordAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_trade_record;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        View view = LayoutInflater.from(TradeRecordActivity.this).inflate(R.layout.layout_trade_record_top,null);
        //建仓
        tabJcLayout = view.findViewById(R.id.tab_jcLayout);
        tabJcText = (TextView) view.findViewById(R.id.tab_jcText);
        tabJcLine = view.findViewById(R.id.tab_jcLine);
        //持仓
        tabCcLayout = view.findViewById(R.id.tab_ccLayout);
        tabCcText = (TextView) view.findViewById(R.id.tab_ccText);
        tabCcLine = view.findViewById(R.id.tab_ccLine);
        //已撤单
        tabRLayout = view.findViewById(R.id.tab_rLayout);
        tabRText = (TextView) view.findViewById(R.id.tab_rText);
        tabRLine = view.findViewById(R.id.tab_rLine);

        tabJcLayout.setOnClickListener(this);
        tabCcLayout.setOnClickListener(this);
        tabRLayout.setOnClickListener(this);

        lvListView.addHeaderView(view);
        List<String>  list = new ArrayList<>();
        for (int i = 0 ; i < 30;i++){
            list.add(new String("111"));
        }
        mAdapter = new TradeRecordAdapter(TradeRecordActivity.this,list);
        lvListView.setAdapter(mAdapter);
    }



    @Override
    public void onClick(View v) {
      switch (v.getId()){
          case R.id.tab_jcLayout:
              tabJcText.setTextColor(getResources().getColor(R.color.color_008EFF));
              tabJcLine.setBackgroundResource(R.color.color_008EFF);
              tabJcLine.setVisibility(View.VISIBLE);
              tabCcText.setTextColor(getResources().getColor(R.color.color_333333));
              tabCcLine.setVisibility(View.INVISIBLE);
              tabRText.setTextColor(getResources().getColor(R.color.color_333333));
              tabRLine.setVisibility(View.INVISIBLE);
              break;
          case R.id.tab_ccLayout:
              tabJcText.setTextColor(getResources().getColor(R.color.color_333333));
              tabJcLine.setVisibility(View.INVISIBLE);
              tabCcText.setTextColor(getResources().getColor(R.color.color_008EFF));
              tabCcLine.setBackgroundResource(R.color.color_008EFF);
              tabCcLine.setVisibility(View.VISIBLE);
              tabRText.setTextColor(getResources().getColor(R.color.color_333333));
              tabRLine.setVisibility(View.INVISIBLE);
              break;
          case R.id.tab_rLayout:
              tabCcText.setTextColor(getResources().getColor(R.color.color_333333));
              tabCcLine.setVisibility(View.INVISIBLE);
              tabJcText.setTextColor(getResources().getColor(R.color.color_333333));
              tabJcLine.setVisibility(View.INVISIBLE);
              tabRText.setTextColor(getResources().getColor(R.color.color_008EFF));
              tabRLine.setBackgroundResource(R.color.color_008EFF);
              tabRLine.setVisibility(View.VISIBLE);
              break;
      }
    }
}
