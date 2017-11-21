package com.honglu.future.ui.trade.details;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.base.BasePresenter;
import com.honglu.future.config.Constant;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.trade.bean.CloseBuiderBean;
import com.honglu.future.ui.trade.bean.HistoryClosePositionBean;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ToastUtil;
import com.honglu.future.widget.ExpandableLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;

/**
 * 平仓详情
 * Created by zhuaibing on 2017/11/6
 */

public class CloseTransactionDetailsActivity extends BaseActivity {

    @BindView(R.id.tv_name)
    TextView mName;
    @BindView(R.id.real_profit_loss)
    TextView real_profit_loss;
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
    @BindView(R.id.build_sxf)
    TextView mBuildSXF;
    @BindView(R.id.trade_id)
    TextView mTradeId;
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

    private static final String KEY_DATA = "KEY_DATA";

    /**
     * 平仓类型3.手动平仓,4.止盈平仓,5.止损平仓,6.爆仓,7.休市平仓
     *
     * @param context
     * @param item
     */
    private String[] closeType = new String[]{"手动平仓", "止盈平仓", "止损平仓", "爆仓", "休市平仓"};
    private BasePresenter<CloseTransactionDetailsActivity> basePresenter;
    private HistoryClosePositionBean bean;

    public static void startCloseTransactionDetailsActivity(Context context, HistoryClosePositionBean item) {
        Intent intent = new Intent(context, CloseTransactionDetailsActivity.class);
        intent.putExtra(KEY_DATA, item);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_close_transaction_details;
    }

    @Override
    public void initPresenter() {
        basePresenter = new BasePresenter<CloseTransactionDetailsActivity>(CloseTransactionDetailsActivity.this) {
            @Override
            public void getData() {
                super.getData();
                toSubscribe(HttpManager.getApi().getCloseBuiderBean(
                        SpUtil.getString(Constant.CACHE_TAG_UID), bean.id, SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN)
                ), new HttpSubscriber<List<CloseBuiderBean>>() {
                    @Override
                    protected void _onNext(List<CloseBuiderBean> o) {
                        super._onNext(o);
                        if (o != null && o.size() >= 1) {
                            bindBuildCloseData(o.get(0));
                        }
                    }

                    @Override
                    protected void _onError(String message) {
                        super._onError(message);
                        ToastUtil.show(message);
                    }
                });
            }
        };

    }

    @Override
    public void loadData() {
        mTitle.setTitle(true, R.mipmap.ic_back_black, null, R.color.color_white, "平仓详情");
        mExpandable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mExpandableView.getVisibility() == View.VISIBLE) {
                    mExpandableView.setVisibility(View.GONE);
                    mExpandableLayout.collapse(true);
                } else {
                    mExpandableView.setVisibility(View.VISIBLE);
                    mExpandableLayout.expand(true);
                }
            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            bean = (HistoryClosePositionBean) intent.getSerializableExtra(KEY_DATA);
            if (bean != null) {
                mName.setText(bean.instrumentName);
                String num;
                if (bean.type == 1) {
                    mBuyRise.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
                    num = mContext.getString(R.string.buy_down_num, bean.position);
                } else {
                    mBuyRise.setTextColor(mContext.getResources().getColor(R.color.color_FB4F4F));
                    num = mContext.getString(R.string.buy_up_num, bean.position);
                }
                mBuyRise.setText(num);
                mServiceCharge.setText(bean.closeSxf);
                mProfitLoss.setText(bean.closeProfitLoss);
                if (Double.parseDouble(bean.closeProfitLoss) > 0) {
                    mProfitLoss.setTextColor(mContext.getResources().getColor(R.color.color_FB4F4F));
                } else if (Double.parseDouble(bean.closeProfitLoss) < 0) {
                    mProfitLoss.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
                } else {
                    mProfitLoss.setTextColor(mContext.getResources().getColor(R.color.color_333333));
                }
                real_profit_loss.setText(bean.profitLoss);
                if (Double.parseDouble(bean.profitLoss) > 0) {
                    real_profit_loss.setTextColor(mContext.getResources().getColor(R.color.color_FB4F4F));
                } else if (Double.parseDouble(bean.profitLoss) < 0) {
                    real_profit_loss.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
                } else {
                    real_profit_loss.setTextColor(mContext.getResources().getColor(R.color.color_333333));
                }
                mChicangAveragePrice.setText(bean.holdAvgPrice);
                mJiancangAveragePrice.setText(bean.price);
                mPrice.setText(bean.closePrice);
                mTiem.setText(bean.tradeTime);
                mBaodanNum.setText(bean.orderSysId);
                mDealNum.setText(bean.tradeId);
                if (bean.closeType > 1) {
                    mType.setText(closeType[bean.closeType - 1]);
                }
            }
        }
        basePresenter.getData();
    }

    private void bindBuildCloseData(CloseBuiderBean data) {
        String num;
        if (data.type == 1) {
            mExpBuyRise.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
            num = mContext.getString(R.string.buy_down_num, data.position);
        } else {
            mExpBuyRise.setTextColor(mContext.getResources().getColor(R.color.color_FB4F4F));
            num = mContext.getString(R.string.buy_up_num, data.position);
        }
        mExpBuyRise.setText(num);
        mExpJiancangPrice.setText("建仓价 " + data.openPrice);
        mExpTime.setText(data.openTime);
        mExpBaodanNum.setText("报单编号 " + data.orderSysId);
        mBuildSXF.setText("建仓手续费 " + data.openSxf);
        mTradeId.setText("成交编号 " + data.openSxf);
    }
}
