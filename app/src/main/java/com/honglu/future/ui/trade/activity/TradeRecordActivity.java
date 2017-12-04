package com.honglu.future.ui.trade.activity;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.config.Constant;
import com.honglu.future.dialog.SingleDateDialog;
import com.honglu.future.ui.trade.adapter.TradeRecordAdapter;
import com.honglu.future.ui.trade.bean.HistoryBuiderPositionBean;
import com.honglu.future.ui.trade.bean.HistoryClosePositionBean;
import com.honglu.future.ui.trade.bean.HistoryMissPositionBean;
import com.honglu.future.ui.trade.bean.HistoryTradeBean;
import com.honglu.future.ui.trade.contract.TradeRecordContract;
import com.honglu.future.ui.trade.presenter.TradeRecordPresenter;
import com.honglu.future.util.DateUtil;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.TimeUtil;
import com.honglu.future.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * 交易记录
 * Created by zhuaibing on 2017/10/26
 */

public class TradeRecordActivity extends BaseActivity<TradeRecordPresenter> implements View.OnClickListener, TradeRecordContract.View {
    private static final String START_TIME_TYPE = "start_time_type";
    private static final String END_TIME_TYPE = "end_time_type";

    @BindView(R.id.lv_listView)
    ListView mListView;
    @BindView(R.id.refreshView)
    SmartRefreshLayout mRefreshView;
    @BindView(R.id.tv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_startTime)
    TextView mStartTime;
    @BindView(R.id.tv_endTime)
    TextView mEndTime;

    private TextView mTextOne;
    private TextView mTextTwo;
    private TextView mTextThr;
    private TextView mTextFour;
    private LinearLayout mLinearTitle;

    private TextView mTextRisk;
    private TextView mJiancang;
    private TextView mPingcang;
    private TextView mRevoke;
    private View mTabJcLayout;
    private TextView mTabJcText;
    private View mTabJcLine;
    private View mTabCcLayout;
    private TextView mTabCcText;
    private View mTabCcLine;
    private View mTabRLayout;
    private TextView mTabRText;
    private View mTabRLine;

    private SingleDateDialog mSingleDateDialog;
    private TradeRecordAdapter mAdapter;
    private String startTime, endTime, today;
    private int clickId = R.id.tab_jcLayout;
    private int pageBuider = 1;
    private int pageClose = 1;
    private int pageMiss = 1;
    private int pageSize = 5;
    private int tabIndex = 0;
    private String mToday;

    private Handler mHandler = new Handler();


    @Override
    public int getLayoutId() {
        return R.layout.activity_trade_record;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        tabIndex = getIntent().getIntExtra("tabIndex", 0);
        mToday = TimeUtil.getCurrentDate(TimeUtil.dateFormatYMD);
        mSingleDateDialog = new SingleDateDialog(TradeRecordActivity.this);
        mIvBack.setVisibility(View.VISIBLE);
        mTitle.setTitle(false, R.color.white, "交易记录");
        View view = LayoutInflater.from(TradeRecordActivity.this).inflate(R.layout.layout_trade_record_top, null);
        mTextOne = (TextView) view.findViewById(R.id.tv_one);
        mTextTwo = (TextView) view.findViewById(R.id.tv_two);
        mTextThr = (TextView) view.findViewById(R.id.tv_thr);
        mTextFour = (TextView) view.findViewById(R.id.tv_four);
        mLinearTitle = (LinearLayout) view.findViewById(R.id.ll_tab_title);
        //风险率
        mTextRisk = (TextView) view.findViewById(R.id.tv_risk);
        //建仓手数
        mJiancang = (TextView) view.findViewById(R.id.tv_jiancang);
        //平仓手数
        mPingcang = (TextView) view.findViewById(R.id.tv_pingcang);
        //已撤单手数
        mRevoke = (TextView) view.findViewById(R.id.tv_revoke);
        //建仓
        mTabJcLayout = view.findViewById(R.id.tab_jcLayout);
        mTabJcText = (TextView) view.findViewById(R.id.tab_jcText);
        mTabJcLine = view.findViewById(R.id.tab_jcLine);
        //持仓
        mTabCcLayout = view.findViewById(R.id.tab_ccLayout);
        mTabCcText = (TextView) view.findViewById(R.id.tab_ccText);
        mTabCcLine = view.findViewById(R.id.tab_ccLine);
        //已撤单
        mTabRLayout = view.findViewById(R.id.tab_rLayout);
        mTabRText = (TextView) view.findViewById(R.id.tab_rText);
        mTabRLine = view.findViewById(R.id.tab_rLine);

        mTabJcText.getPaint().setFakeBoldText(true);
        mTabCcText.getPaint().setFakeBoldText(false);
        mTabRText.getPaint().setFakeBoldText(false);

        mStartTime.setOnClickListener(this);
        mEndTime.setOnClickListener(this);
        mTabJcLayout.setOnClickListener(this);
        mTabCcLayout.setOnClickListener(this);
        mTabRLayout.setOnClickListener(this);
        mIvBack.setOnClickListener(this);

        mListView.addHeaderView(view);
        mAdapter = new TradeRecordAdapter(TradeRecordActivity.this);
        mListView.setAdapter(mAdapter);

        mSingleDateDialog.setOnBirthdayListener(new SingleDateDialog.OnBirthdayListener() {
            @Override
            public void onBirthday(String type, String time) {

                String[] split = time.split("-");
                String mTime = split[0] + "-" + Integer.parseInt(split[1]) + "-" + Integer.parseInt(split[2]);
                String start = START_TIME_TYPE.equals(type) ? mTime : startTime;
                String end = END_TIME_TYPE.equals(type) ? mTime : endTime;
                if (DateUtil.compareDate(start, end)) {
                    ToastUtil.show("开始日期不能早于结束日期");
                    return;
                }
                if (DateUtil.compareDate(end, today)) {
                    ToastUtil.show("结束日期不能早于今天");
                    return;
                }
                startTime = start;
                endTime = end;
                mStartTime.setText(start);
                mEndTime.setText(end);
                getHistoryData(clickId);
                mSingleDateDialog.dismiss();
            }
        });

        endTime = mSingleDateDialog.getYear() + "-" + mSingleDateDialog.getMonth() + "-" + mSingleDateDialog.getDay();
        today = endTime;
        startTime = getStartTime();
        mStartTime.setText(startTime);
        mEndTime.setText(endTime);


        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String end;
                if (TimeUtil.getFridayOfWeek(TimeUtil.dateFormatYMD).equals(TimeUtil.getStringByFormat(endTime, TimeUtil.dateFormatYMD))) {
                    end = TimeUtil.getStringByOffset(endTime, TimeUtil.dateFormatYMD, Calendar.DATE, 3);
                } else {
                    end = TimeUtil.getStringByOffset(endTime, TimeUtil.dateFormatYMD, Calendar.DATE, 1);
                }

                mPresenter.getHistoryTradeBean(startTime, SpUtil.getString(Constant.CACHE_TAG_UID), SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN),
                        end);
            }
        }, 500);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getHistoryData(clickId);
            }
        }, 1000);

        mRefreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getHistoryData(clickId);
            }
        });
        mRefreshView.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                loadMore();
            }
        });
        mTextTwo.setVisibility(View.INVISIBLE);
        mTextThr.setVisibility(View.INVISIBLE);
        mTextFour.setText("建仓价");
        if (tabIndex == 1) {
            clickId = R.id.tab_ccLayout;
            mTabCcLayout.performClick();
        }
    }


    private void loadMore() {
        String end;
        if (TimeUtil.getFridayOfWeek(TimeUtil.dateFormatYMD).equals(TimeUtil.getStringByFormat(endTime, TimeUtil.dateFormatYMD))) {
            end = TimeUtil.getStringByOffset(endTime, TimeUtil.dateFormatYMD, Calendar.DATE, 3);
        } else {
            end = TimeUtil.getStringByOffset(endTime, TimeUtil.dateFormatYMD, Calendar.DATE, 1);
        }
        switch (clickId) {
            case R.id.tab_jcLayout:
                if (!isHistoryBuilderMore) {
                    mRefreshView.finishLoadmore();
                    return;
                }
                mPresenter.getHistoryBuilderBean(startTime, SpUtil.getString(Constant.CACHE_TAG_UID), SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN), end, pageBuider, pageSize);
                break;
            case R.id.tab_ccLayout:
                if (!isHistoryCloseMore) {
                    mRefreshView.finishLoadmore();
                    return;
                }
                mPresenter.getHistoryCloseBean(startTime, SpUtil.getString(Constant.CACHE_TAG_UID), SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN), end, pageClose, pageSize);
                break;
            case R.id.tab_rLayout:
                if (!isHistoryMissMore) {
                    mRefreshView.finishLoadmore();
                    return;
                }
                mPresenter.getHistoryMissBean(startTime, SpUtil.getString(Constant.CACHE_TAG_UID), SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN), end, pageMiss, pageSize);
                break;
        }
    }

    private String getStartTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        //过去七天
        c.setTime(new Date());
        c.add(Calendar.DATE, -7);
        Date d = c.getTime();
        return format.format(d);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_startTime:
                mSingleDateDialog.showDateDialog(START_TIME_TYPE);
                break;
            case R.id.tv_endTime:
                mSingleDateDialog.showDateDialog(END_TIME_TYPE);
                break;
            case R.id.tab_jcLayout:
                clickId = v.getId();
                getHistoryData(clickId);
                mTabJcText.setTextColor(getResources().getColor(R.color.color_008EFF));
                mTabJcLine.setVisibility(View.VISIBLE);
                mTabCcText.setTextColor(getResources().getColor(R.color.color_333333));
                mTabCcLine.setVisibility(View.INVISIBLE);
                mTabRText.setTextColor(getResources().getColor(R.color.color_333333));
                mTabRLine.setVisibility(View.INVISIBLE);
                mTextTwo.setVisibility(View.INVISIBLE);
                mTextThr.setVisibility(View.INVISIBLE);
                mTextFour.setText("建仓价");
                mTabJcText.getPaint().setFakeBoldText(true);
                mTabCcText.getPaint().setFakeBoldText(false);
                mTabRText.getPaint().setFakeBoldText(false);
                break;
            case R.id.tab_ccLayout:
                clickId = v.getId();
                getHistoryData(clickId);
                mTabJcText.setTextColor(getResources().getColor(R.color.color_333333));
                mTabJcLine.setVisibility(View.INVISIBLE);
                mTabCcText.setTextColor(getResources().getColor(R.color.color_008EFF));
                mTabCcLine.setVisibility(View.VISIBLE);
                mTabRText.setTextColor(getResources().getColor(R.color.color_333333));
                mTabRLine.setVisibility(View.INVISIBLE);
                mTextTwo.setVisibility(View.VISIBLE);
                mTextTwo.setText("建仓价");
                mTextThr.setVisibility(View.VISIBLE);
                mTextThr.setText("平仓价");
                mTextFour.setText("平仓盈亏");
                mTabJcText.getPaint().setFakeBoldText(false);
                mTabCcText.getPaint().setFakeBoldText(true);
                mTabRText.getPaint().setFakeBoldText(false);
                break;
            case R.id.tab_rLayout:
                clickId = v.getId();
                getHistoryData(clickId);
                mTabCcText.setTextColor(getResources().getColor(R.color.color_333333));
                mTabCcLine.setVisibility(View.INVISIBLE);
                mTabJcText.setTextColor(getResources().getColor(R.color.color_333333));
                mTabJcLine.setVisibility(View.INVISIBLE);
                mTabRText.setTextColor(getResources().getColor(R.color.color_008EFF));
                mTabRLine.setVisibility(View.VISIBLE);
                mTextTwo.setVisibility(View.INVISIBLE);
                mTextThr.setVisibility(View.VISIBLE);
                mTextThr.setText("委托类型");
                mTextFour.setText("委托价");
                mTabJcText.getPaint().setFakeBoldText(false);
                mTabCcText.getPaint().setFakeBoldText(false);
                mTabRText.getPaint().setFakeBoldText(true);
                break;
            case R.id.tv_back:
                finish();
                break;
        }
    }

    private void getHistoryData(int id) {
        String end;
        if (TimeUtil.getFridayOfWeek(TimeUtil.dateFormatYMD).equals(TimeUtil.getStringByFormat(endTime, TimeUtil.dateFormatYMD))) {
            end = TimeUtil.getStringByOffset(endTime, TimeUtil.dateFormatYMD, Calendar.DATE, 3);
        } else {
            end = TimeUtil.getStringByOffset(endTime, TimeUtil.dateFormatYMD, Calendar.DATE, 1);
        }
        switch (id) {
            case R.id.tab_jcLayout:
                pageBuider = 1;
                mPresenter.getHistoryBuilderBean(startTime, SpUtil.getString(Constant.CACHE_TAG_UID), SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN), end, pageBuider, pageSize);
                break;
            case R.id.tab_ccLayout:
                pageClose = 1;
                mPresenter.getHistoryCloseBean(startTime, SpUtil.getString(Constant.CACHE_TAG_UID), SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN), end, pageClose, pageSize);
                break;
            case R.id.tab_rLayout:
                pageMiss = 1;
                mPresenter.getHistoryMissBean(startTime, SpUtil.getString(Constant.CACHE_TAG_UID), SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN), end, pageMiss, pageSize);
                break;
        }
    }

    @Override
    public void bindHistoryTradeBean(HistoryTradeBean bean) {
        //持仓盈亏
        if (Double.parseDouble(bean.profitLoss) > 0) {
            mTextRisk.setText(bean.profitLoss);
            mTextRisk.setTextColor(mContext.getResources().getColor(R.color.color_FB4F4F));
        } else if (Double.parseDouble(bean.profitLoss) < 0) {
            mTextRisk.setText(bean.profitLoss);
            mTextRisk.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
        } else {
            mTextRisk.setText(bean.profitLoss);
            mTextRisk.setTextColor(mContext.getResources().getColor(R.color.color_333333));
        }
        mJiancang.setText(bean.open);
        mPingcang.setText(bean.close);
        mRevoke.setText(bean.cancel);
    }

    boolean isHistoryMissMore = false;

    @Override
    public void bindHistoryMissBean(List<HistoryMissPositionBean> list) {
        if (pageMiss > 1) {
            mAdapter.setMList(list, true);
            mRefreshView.finishLoadmore();
        } else {
            mAdapter.setMList(list, false);
            mRefreshView.finishRefresh();
            if (list.size() == 0) {
                mLinearTitle.setVisibility(View.GONE);
                return;
            }
            mLinearTitle.setVisibility(View.VISIBLE);
        }
        if (list.size() >= pageSize) {
            ++pageMiss;
            isHistoryMissMore = true;
        } else {
            isHistoryMissMore = false;
        }
    }

    boolean isHistoryCloseMore = false;

    @Override
    public void bindHistoryCloseBean(List<HistoryClosePositionBean> list) {
        if (pageClose > 1) {
            mAdapter.setCList(list, true);
            mRefreshView.finishLoadmore();
        } else {
            mAdapter.setCList(list, false);
            mRefreshView.finishRefresh();
            if (list.size() == 0) {
                mLinearTitle.setVisibility(View.GONE);
                return;
            }
            mLinearTitle.setVisibility(View.VISIBLE);
        }
        if (list.size() >= pageSize) {
            ++pageClose;
            isHistoryCloseMore = true;
        } else {
            isHistoryCloseMore = false;
        }

    }

    boolean isHistoryBuilderMore = false;

    @Override
    public void bindHistoryBuilderBean(List<HistoryBuiderPositionBean> list) {
        if (pageBuider > 1) {
            mAdapter.setBList(list, true);
            mRefreshView.finishLoadmore();
        } else {
            mAdapter.setBList(list, false);
            mRefreshView.finishRefresh();
            if (list.size() == 0) {
                mLinearTitle.setVisibility(View.GONE);
                return;
            }
            mLinearTitle.setVisibility(View.VISIBLE);
        }
        if (list.size() >= pageSize) {
            ++pageBuider;
            isHistoryBuilderMore = true;
        } else {
            isHistoryBuilderMore = false;
        }
    }

    private int count = 0;

    @Override
    public void showErrorMsg(String msg, String type) {
        super.showErrorMsg(msg, type);
        if (msg.contains("服务器忙") || msg.contains("频繁")) {
            if (TradeRecordPresenter.TYPE_HISTORY_TRADE.equals(type)) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ++count;
                        if (count > 2) {
                            return;
                        }
                        String end;
                        if (TimeUtil.getFridayOfWeek(TimeUtil.dateFormatYMD).equals(TimeUtil.getStringByFormat(endTime, TimeUtil.dateFormatYMD))) {
                            end = TimeUtil.getStringByOffset(endTime, TimeUtil.dateFormatYMD, Calendar.DATE, 3);
                        } else {
                            end = TimeUtil.getStringByOffset(endTime, TimeUtil.dateFormatYMD, Calendar.DATE, 1);
                        }
                        mPresenter.getHistoryTradeBean(startTime, SpUtil.getString(Constant.CACHE_TAG_UID), SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN), end);
                    }
                }, 1000);
            } else {
                getHistoryData(clickId);
            }
        }
        mRefreshView.finishRefresh();
        mRefreshView.finishLoadmore();
    }
}
