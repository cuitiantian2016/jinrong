package com.honglu.future.ui.usercenter.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.ui.usercenter.bean.HistoryRecordsBean;
import com.honglu.future.util.StringUtil;
import com.honglu.future.widget.recycler.BaseRecyclerAdapter;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zq on 2017/11/2.
 */

public class HistoryRecordsAdapter extends BaseRecyclerAdapter<HistoryRecordsAdapter.ViewHolder, HistoryRecordsBean> {
    public static final String TYPE_BUILD = "TYPE_BUILD";
    public static final String TYPE_CLOSED = "TYPE_CLOSED";
    public static final String TYPE_POSITION = "TYPE_POSITION";
    private String mType = TYPE_BUILD;

    @Override
    public HistoryRecordsAdapter.ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_history_records, parent, false);
        return new HistoryRecordsAdapter.ViewHolder(view);
    }

    @Override
    public void mOnBindViewHolder(HistoryRecordsAdapter.ViewHolder holder, int position) {
        holder.mTvProductName.setText(item.getName());
        holder.mTvBuyHands.setText(item.getBuyHands() + "手");
        if (mType.equals(TYPE_CLOSED)) {
            holder.mLlBuild.setVisibility(View.GONE);
            holder.mLlClosed.setVisibility(View.VISIBLE);
            holder.mLlPosition.setVisibility(View.GONE);
            holder.mBuildPriceClosed.setText(item.getBuildPrice());
            holder.mClosePrice.setText(item.getClosePrice());
            if (new BigDecimal(item.getProfitLoss()).doubleValue() > 0) {
                holder.mProfitLoss.setTextColor(mContext.getResources().getColor(R.color.color_FB4F4F));
                holder.mProfitLoss.setText("+"+ StringUtil.forNumber(new BigDecimal(item.getProfitLoss()).doubleValue()));
            } else if (new BigDecimal(item.getProfitLoss()).doubleValue() < 0) {
                holder.mProfitLoss.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
                holder.mProfitLoss.setText(StringUtil.forNumber(new BigDecimal(item.getProfitLoss()).doubleValue()));
            } else {
                holder.mProfitLoss.setTextColor(mContext.getResources().getColor(R.color.color_333333));
                holder.mProfitLoss.setText(StringUtil.forNumber(new BigDecimal(item.getProfitLoss()).doubleValue()));
            }
        } else if (mType.equals(TYPE_POSITION)) {
            holder.mLlBuild.setVisibility(View.GONE);
            holder.mLlClosed.setVisibility(View.GONE);
            holder.mLlPosition.setVisibility(View.VISIBLE);
            holder.mSettlePrice.setText(item.getSettlePrice());
            if (new BigDecimal(item.getTodayPl()).doubleValue() > 0) {
                holder.mTodayPl.setTextColor(mContext.getResources().getColor(R.color.color_FB4F4F));
                holder.mTodayPl.setText("+"+ StringUtil.forNumber(new BigDecimal(item.getTodayPl()).doubleValue()));
            } else if (new BigDecimal(item.getTodayPl()).doubleValue() < 0) {
                holder.mTodayPl.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
                holder.mTodayPl.setText(StringUtil.forNumber(new BigDecimal(item.getTodayPl()).doubleValue()));
            } else {
                holder.mTodayPl.setTextColor(mContext.getResources().getColor(R.color.color_333333));
                holder.mTodayPl.setText(StringUtil.forNumber(new BigDecimal(item.getTodayPl()).doubleValue()));
            }

            if (new BigDecimal(item.getProfitLoss()).doubleValue() > 0) {
                holder.mActualPl.setTextColor(mContext.getResources().getColor(R.color.color_FB4F4F));
                holder.mActualPl.setText("+"+ StringUtil.forNumber(new BigDecimal(item.getProfitLoss()).doubleValue()));
            } else if (new BigDecimal(item.getProfitLoss()).doubleValue() < 0) {
                holder.mActualPl.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
                holder.mActualPl.setText(StringUtil.forNumber(new BigDecimal(item.getProfitLoss()).doubleValue()));
            } else {
                holder.mActualPl.setTextColor(mContext.getResources().getColor(R.color.color_333333));
                holder.mActualPl.setText(StringUtil.forNumber(new BigDecimal(item.getProfitLoss()).doubleValue()));
            }
        } else {
            holder.mLlBuild.setVisibility(View.VISIBLE);
            holder.mLlClosed.setVisibility(View.GONE);
            holder.mLlPosition.setVisibility(View.GONE);
            holder.mTvBuildPrice.setText(item.getBuildPrice());
            holder.mTvServicePrice.setText(item.getServicePrice());
        }

        if (item.getBuyType().equals("买涨")) {
            holder.mTvBuyType.setText("买涨");
            holder.mTvBuyType.setTextColor(mContext.getResources().getColor(R.color.color_FB4F4F));
        } else {
            holder.mTvBuyType.setText("买跌");
            holder.mTvBuyType.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
        }
    }

    public void setType(String TYPE) {
        mType = TYPE;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView mTvProductName;
        @BindView(R.id.tv_buy_hands)
        TextView mTvBuyHands;
        @BindView(R.id.tv_buy_type)
        TextView mTvBuyType;
        @BindView(R.id.tv_build_price)
        TextView mTvBuildPrice;
        @BindView(R.id.tv_service_price)
        TextView mTvServicePrice;
        @BindView(R.id.ll_build)
        LinearLayout mLlBuild;
        @BindView(R.id.ll_closed)
        LinearLayout mLlClosed;
        @BindView(R.id.tv_build_price_closed)
        TextView mBuildPriceClosed;
        @BindView(R.id.tv_closed_price)
        TextView mClosePrice;
        @BindView(R.id.tv_profit_loss)
        TextView mProfitLoss;
        @BindView(R.id.ll_position)
        LinearLayout mLlPosition;
        @BindView(R.id.tv_settlement_price)
        TextView mSettlePrice;
        @BindView(R.id.tv_today_pl)
        TextView mTodayPl;
        @BindView(R.id.tv_actual_pl)
        TextView mActualPl;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
