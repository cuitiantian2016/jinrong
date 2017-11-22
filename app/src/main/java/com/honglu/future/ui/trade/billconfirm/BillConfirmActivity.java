package com.honglu.future.ui.trade.billconfirm;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.config.Constant;
import com.honglu.future.ui.trade.bean.ConfirmBean;
import com.honglu.future.ui.trade.bean.SettlementInfoBean;
import com.honglu.future.ui.usercenter.adapter.HistoryRecordsAdapter;
import com.honglu.future.ui.usercenter.bean.HistoryRecordsBean;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.SpUtil;
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

import static com.honglu.future.util.ToastUtil.showToast;

/**
 * Created by zq on 2017/11/2.
 */

public class BillConfirmActivity extends BaseActivity<BillConfirmPresenter> implements
        BillConfirmContract.View {
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
    @BindView(R.id.tv_comp_name)
    TextView mCompName;
    @BindView(R.id.tv_customer_name)
    TextView mCustomerName;
    @BindView(R.id.tv_client_id)
    TextView mClientId;
    @BindView(R.id.tv_currency)
    TextView mCurrency;
    @BindView(R.id.tv_trade_date)
    TextView mTradeDate;
    @BindView(R.id.tv_query_date)
    TextView mQueryDate;
    @BindView(R.id.tv_prev_sttl)
    TextView mPrevSttl;
    @BindView(R.id.tv_cur_day_jc)
    TextView mCurDayJc;
    @BindView(R.id.tv_khqy)
    TextView mKhqy;
    @BindView(R.id.tv_cur_day_crj)
    TextView mDrcrj;
    @BindView(R.id.tv_kyzj)
    TextView mKyzj;
    @BindView(R.id.tv_cur_day_pcyk)
    TextView mDrpcyk;
    @BindView(R.id.tv_ztzj)
    TextView mZtzj;
    @BindView(R.id.tv_cur_day_sxf)
    TextView mDrsxf;
    @BindView(R.id.tv_ccyk)
    TextView mCcyk;
    @BindView(R.id.tv_append_money)
    TextView mYzjzj;
    @BindView(R.id.tv_zybzj)
    TextView mZybzj;
    @BindView(R.id.tv_fengxian_rate)
    TextView mFxl;
    private ArrayList<CustomTabEntity> mTabList;
    private HistoryRecordsAdapter mHistoryRecordsAdapter;
    private List<HistoryRecordsBean> mList;
    private SettlementInfoBean settlementInfoBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bill_confirm;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        initView();
        initData();
    }

    @Override
    public void showLoading(String content) {
        if (!TextUtils.isEmpty(content)) {
            App.loadingContent(this, content);
        }
    }

    @Override
    public void stopLoading() {
        App.hideLoading();
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        showToast(msg);
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
        String settlementJson = getIntent().getStringExtra("SettlementBean");
        settlementInfoBean = new Gson().fromJson(settlementJson, SettlementInfoBean.class);
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
                    for (int i = 0; i < settlementInfoBean.getTransactionList().size(); i++) {
                        HistoryRecordsBean bean = new HistoryRecordsBean();
                        bean.setName(settlementInfoBean.getTransactionList().get(i).getProduct());
                        bean.setBuyType(settlementInfoBean.getTransactionList().get(i).getBs());
                        bean.setBuyHands(settlementInfoBean.getTransactionList().get(i).getLots());
                        bean.setBuildPrice(settlementInfoBean.getTransactionList().get(i).getPrice());
                        bean.setServicePrice(settlementInfoBean.getTransactionList().get(i).getFee());
                        mList.add(bean);
                    }
                } else if (position == 1 && settlementInfoBean.getCloseList() != null) {
                    for (int i = 0; i < settlementInfoBean.getCloseList().size(); i++) {
                        HistoryRecordsBean bean = new HistoryRecordsBean();
                        bean.setName(settlementInfoBean.getCloseList().get(i).getProduct());
                        bean.setBuyType(settlementInfoBean.getCloseList().get(i).getBs());
                        bean.setBuyHands(settlementInfoBean.getCloseList().get(i).getLots());
                        bean.setBuildPrice(settlementInfoBean.getCloseList().get(i).getTransPrice());
                        bean.setServicePrice(settlementInfoBean.getCloseList().get(i).getPremiumReceivedPaid());
                        mList.add(bean);
                    }
                } else if (position == 1 && settlementInfoBean.getPositionsList() != null) {
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

                mHistoryRecordsAdapter.addData(mList);
            }
        });
    }

    @OnClick({R.id.tv_back, R.id.tv_confirm})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_confirm:
                mPresenter.settlementConfirm(SpUtil.getString(Constant.CACHE_TAG_UID), getIntent().getStringExtra("token"));
                finish();
                break;
        }
    }

    @Override
    public void queryConfirmSuccess(ConfirmBean bean) {
        SpUtil.putString(Constant.CACHE_ACCOUNT_TOKEN, getIntent().getStringExtra("token"));
        showToast("结算单确认成功");
    }
}
