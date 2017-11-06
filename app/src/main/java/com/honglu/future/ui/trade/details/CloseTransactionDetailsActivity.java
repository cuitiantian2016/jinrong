package com.honglu.future.ui.trade.details;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.widget.ExpandableLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 平仓详情
 * Created by zhuaibing on 2017/11/6
 */

public class CloseTransactionDetailsActivity extends BaseActivity<CloseTransactionDetailsPresenter> implements CloseTransactionDetailsContract.View {

    @BindView(R.id.tv_name)
    TextView mName;
    @BindView(R.id.tv_buy_rise)
    TextView mBuyRise;
    @BindView(R.id.tv_profit_loss)
    TextView mProfitLoss;
    @BindView(R.id.tv_chicang_average_price)
    TextView mChicangAveragePrice;
    @BindView(R.id.tv_jiancang_average_price)
    TextView mJiancangAveragePrice;
    @BindView(R.id.iv_expandable)
    ImageView mExpandable;
    @BindView(R.id.tv_exp_buy_rise)
    TextView mExpBuyRise;
    @BindView(R.id.tv_exp_jiancang_price)
    TextView mExpJiancangPrice;
    @BindView(R.id.tv_exp_baodan_num)
    TextView mExpBaodanNum;
    @BindView(R.id.tv_exp_time)
    TextView mExpTime;
    @BindView(R.id.ll_expandable_view)
    LinearLayout mExpandableView;
    @BindView(R.id.el_expandable_layout)
    ExpandableLayout mExpandableLayout;
    @BindView(R.id.tv_price)
    TextView mPrice;
    @BindView(R.id.tv_tiem)
    TextView mTiem;
    @BindView(R.id.tv_type)
    TextView mType;
    @BindView(R.id.tv_service_charge)
    TextView mServiceCharge;
    @BindView(R.id.tv_baodan_num)
    TextView mBaodanNum;
    @BindView(R.id.tv_deal_num)
    TextView mDealNum;
    @BindView(R.id.refreshView)
    SmartRefreshLayout mRefreshView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_close_transaction_details;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        mTitle.setTitle(true,R.mipmap.ic_back_black,null,R.color.color_white,"平仓详情");
        mExpandable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mExpandableView.getVisibility() == View.VISIBLE){
                    mExpandableView.setVisibility(View.GONE);
                    mExpandableLayout.collapse(true);
                }else {
                    mExpandableView.setVisibility(View.VISIBLE);
                    mExpandableLayout.expand(true);
                }
            }
        });
    }
}
