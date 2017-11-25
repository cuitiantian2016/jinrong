package com.honglu.future.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.config.Constant;
import com.honglu.future.ui.trade.bean.SettlementInfoBean;
import com.honglu.future.ui.usercenter.activity.KeFuActivity;
import com.honglu.future.ui.usercenter.adapter.HistoryRecordsAdapter;
import com.honglu.future.ui.usercenter.bean.HistoryRecordsBean;
import com.honglu.future.util.ConvertUtil;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.StringUtil;
import com.honglu.future.widget.DrawableCenterTextView;
import com.honglu.future.widget.recycler.DividerItemDecoration;
import com.honglu.future.widget.tab.CommonTabLayout;
import com.honglu.future.widget.tab.CustomTabEntity;
import com.honglu.future.widget.tab.SimpleOnTabSelectListener;
import com.honglu.future.widget.tab.TabEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zq on 2017/11/22.
 */

public class BillConfirmDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    CommonTabLayout mCommonTabLayout;
    RecyclerView mRecordsListView;
    ImageView mIvBack;
    DrawableCenterTextView mRightTitle;
    TextView mTvConfirm;
    TextView mCompName;
    TextView mCustomerName;
    TextView mClientId;
    TextView mCurrency;
    TextView mTradeDate;
    TextView mQueryDate;
    TextView mPrevSttl;
    TextView mCurDayJc;
    TextView mKhqy;
    TextView mDrcrj;
    TextView mKyzj;
    TextView mDrpcyk;
    TextView mZtzj;
    TextView mDrsxf;
    TextView mCcyk;
    TextView mYzjzj;
    TextView mZybzj;
    TextView mFxl;
    TextView mTitle;
    private ArrayList<CustomTabEntity> mTabList;
    private HistoryRecordsAdapter mHistoryRecordsAdapter;
    private List<HistoryRecordsBean> mList;
    private SettlementInfoBean settlementInfoBean;

    public interface OnConfirmClickListener {
        void onConfirmClick();
    }

    private OnConfirmClickListener mListener;

    public void setOnConfirmClickListenerr(OnConfirmClickListener listener) {
        mListener = listener;
    }

    public BillConfirmDialog(@NonNull Context context, SettlementInfoBean bean) {
        super(context, R.style.DateDialog);
        this.mContext = context;
        this.settlementInfoBean = bean;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_confirm);
        Window mWindow = this.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        WindowManager manage = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        params.width = manage.getDefaultDisplay().getWidth();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        mWindow.setAttributes(params);
        setCanceledOnTouchOutside(false);
        initView();
        initData();
    }

    private void initView() {
        mCommonTabLayout = (CommonTabLayout) findViewById(R.id.trade_common_tab_layout);
        mRecordsListView = (RecyclerView) findViewById(R.id.rv_records);
        mIvBack = (ImageView) findViewById(R.id.tv_back);
        mIvBack.setOnClickListener(this);
        mRightTitle = (DrawableCenterTextView) findViewById(R.id.tv_right);
        mRightTitle.setOnClickListener(this);
        mTvConfirm = (TextView) findViewById(R.id.tv_confirm);
        mTvConfirm.setOnClickListener(this);
        mCompName = (TextView) findViewById(R.id.tv_comp_name);
        mCustomerName = (TextView) findViewById(R.id.tv_customer_name);
        mClientId = (TextView) findViewById(R.id.tv_client_id);
        mCurrency = (TextView) findViewById(R.id.tv_currency);
        mTradeDate = (TextView) findViewById(R.id.tv_trade_date);
        mQueryDate = (TextView) findViewById(R.id.tv_query_date);
        mPrevSttl = (TextView) findViewById(R.id.tv_prev_sttl);
        mCurDayJc = (TextView) findViewById(R.id.tv_cur_day_jc);
        mKhqy = (TextView) findViewById(R.id.tv_khqy);
        mDrcrj = (TextView) findViewById(R.id.tv_cur_day_crj);
        mKyzj = (TextView) findViewById(R.id.tv_kyzj);
        mDrpcyk = (TextView) findViewById(R.id.tv_cur_day_pcyk);
        mZtzj = (TextView) findViewById(R.id.tv_ztzj);
        mDrsxf = (TextView) findViewById(R.id.tv_cur_day_sxf);
        mCcyk = (TextView) findViewById(R.id.tv_ccyk);
        mYzjzj = (TextView) findViewById(R.id.tv_append_money);
        mZybzj = (TextView) findViewById(R.id.tv_zybzj);
        mFxl = (TextView) findViewById(R.id.tv_fengxian_rate);
        mTitle = (TextView) findViewById(R.id.tv_title);

        mIvBack.setVisibility(View.VISIBLE);
        mTitle.setText("账单确认");
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
        //基本资料
        mCompName.setText(settlementInfoBean.getExchangeName());
        mCustomerName.setText(settlementInfoBean.getClientName());
        mClientId.setText(settlementInfoBean.getClientId());
        mCurrency.setText(settlementInfoBean.getCurrency());
        mTradeDate.setText(settlementInfoBean.getCreationDate());
        mQueryDate.setText(settlementInfoBean.getDate());

        //资金详情
        mPrevSttl.setText(settlementInfoBean.getBalanceBF());
        mCurDayJc.setText(settlementInfoBean.getBalanceCF());
        mKhqy.setText(settlementInfoBean.getClientEquity());
        mDrcrj.setText(settlementInfoBean.getDepositWithdrawal());
        mKyzj.setText(settlementInfoBean.getFundAvail());
        mDrpcyk.setText(settlementInfoBean.getRealizedPL());
        mZtzj.setText(settlementInfoBean.getPledgeAmount());
        mDrsxf.setText(settlementInfoBean.getCommission());
        mCcyk.setText(settlementInfoBean.getMtmPL());
        mYzjzj.setText(settlementInfoBean.getMarginCall());
        mZybzj.setText(settlementInfoBean.getMarginOccupied());
        mFxl.setText(settlementInfoBean.getRiskDegree());

        mList = new ArrayList<>();
        setTabData();
        mHistoryRecordsAdapter.clearData();
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
                mList.clear();
                if (position == 0 && settlementInfoBean.getTransactionList() != null) {
                    setTabData();
                } else if (position == 1 && settlementInfoBean.getCloseList() != null) {
                    mHistoryRecordsAdapter.setType(HistoryRecordsAdapter.TYPE_CLOSED);
                    for (int i = 0; i < settlementInfoBean.getCloseList().size(); i++) {
                        HistoryRecordsBean bean = new HistoryRecordsBean();
                        bean.setName(settlementInfoBean.getCloseList().get(i).getProduct());
                        bean.setBuyType(settlementInfoBean.getCloseList().get(i).getBs());
                        bean.setBuyHands(settlementInfoBean.getCloseList().get(i).getLots());
                        bean.setBuildPrice(settlementInfoBean.getCloseList().get(i).getPosOpenPrice());
                        bean.setClosePrice(settlementInfoBean.getCloseList().get(i).getTransPrice());
                        bean.setProfitLoss(StringUtil.forNumber(new BigDecimal(ConvertUtil.NVL(settlementInfoBean.getCloseList().get(i).getRealizedPL(),"0")).doubleValue()));
                        mList.add(bean);
                    }
                } else if (position == 1 && settlementInfoBean.getPositionsList() != null) {
                    mHistoryRecordsAdapter.setType(HistoryRecordsAdapter.TYPE_POSITION);
                    for (int i = 0; i < settlementInfoBean.getPositionsList().size(); i++) {
                        HistoryRecordsBean bean = new HistoryRecordsBean();
                        bean.setName(settlementInfoBean.getPositionsList().get(i).getProduct());
                        bean.setBuyType(settlementInfoBean.getPositionsList().get(i).getSh());
                        bean.setBuyHands(settlementInfoBean.getPositionsList().get(i).getLongPos());
                        bean.setBuildPrice(settlementInfoBean.getPositionsList().get(i).getAvgBuyPrice());
                        bean.setServicePrice(settlementInfoBean.getPositionsList().get(i).getMarkeValueLong());
                        mList.add(bean);
                    }
                }
                mHistoryRecordsAdapter.clearData();
                mHistoryRecordsAdapter.addData(mList);
            }
        });
    }

    private void setTabData() {
        mHistoryRecordsAdapter.setType(HistoryRecordsAdapter.TYPE_BUILD);
        if (settlementInfoBean.getTransactionList() != null && settlementInfoBean.getTransactionList().size() > 0) {
            for (int i = 0; i < settlementInfoBean.getTransactionList().size(); i++) {
                HistoryRecordsBean bean = new HistoryRecordsBean();
                bean.setName(settlementInfoBean.getTransactionList().get(i).getProduct());
                bean.setBuyType(settlementInfoBean.getTransactionList().get(i).getBs());
                bean.setBuyHands(settlementInfoBean.getTransactionList().get(i).getLots());
                bean.setBuildPrice(settlementInfoBean.getTransactionList().get(i).getPrice());
                bean.setServicePrice(settlementInfoBean.getTransactionList().get(i).getFee());
                mList.add(bean);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                dismiss();
                break;
            case R.id.tv_right:
                mContext.startActivity(new Intent(mContext, KeFuActivity.class));
                break;
            case R.id.tv_confirm:
                mListener.onConfirmClick();
                break;
        }
    }
}
