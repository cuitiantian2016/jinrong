package com.honglu.future.ui.trade.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.dialog.DateDialog;
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
    @BindView(R.id.tv_back)
    ImageView mIvBack;

    private TextView tvStartTime;
    private TextView tvEndTime;
    private TextView tvRisk;
    private TextView tvJiancang;
    private TextView tvPingcang;
    private TextView tvRevoke;
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
    private DateDialog mDateDialog;


    @Override
    public int getLayoutId() {
        return R.layout.activity_trade_record;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        mIvBack.setVisibility(View.VISIBLE);
        mTitle.setTitle(false, R.color.white, "交易记录");
        mDateDialog = new DateDialog(TradeRecordActivity.this);
        View view = LayoutInflater.from(TradeRecordActivity.this).inflate(R.layout.layout_trade_record_top,null);
        tvStartTime = (TextView) view.findViewById(R.id.tv_startTime);
        tvEndTime = (TextView) view.findViewById(R.id.tv_endTime);
        //风险率
        tvRisk = (TextView) view.findViewById(R.id.tv_risk);
        //建仓手数
        tvJiancang = (TextView) view.findViewById(R.id.tv_jiancang);
        //平仓手数
        tvPingcang = (TextView) view.findViewById(R.id.tv_pingcang);
        //已撤单手数
        tvRevoke = (TextView) view.findViewById(R.id.tv_revoke);
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

        tvStartTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        tabJcLayout.setOnClickListener(this);
        tabCcLayout.setOnClickListener(this);
        tabRLayout.setOnClickListener(this);
        mIvBack.setOnClickListener(this);

        lvListView.addHeaderView(view);
        List<String>  list = new ArrayList<>();
        for (int i = 0 ; i < 30;i++){
            list.add(new String("111"));
        }
        mAdapter = new TradeRecordAdapter(TradeRecordActivity.this,list);
        lvListView.setAdapter(mAdapter);

        mDateDialog.setBirthdayListener(new DateDialog.OnBirthListener() {
            @Override
            public void onClick(String start, String end) {
                tvStartTime.setText(start);
                tvEndTime.setText(end);
                mDateDialog.dismiss();
            }
        });
    }



    @Override
    public void onClick(View v) {
      switch (v.getId()){
          case R.id.tv_startTime:
              mDateDialog.show();
              break;
          case R.id.tv_endTime:
              mDateDialog.show();
              break;
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
          case R.id.tv_back:
              finish();
              break;
      }
    }
}
