package com.honglu.future.ui.trade.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.ui.trade.bean.HistoryClosePositionBean;
import com.honglu.future.ui.trade.details.CloseTransactionDetailsActivity;
import com.honglu.future.widget.recycler.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zq on 2017/10/28.
 */

public class ClosePositionAdapter extends BaseRecyclerAdapter<ClosePositionAdapter.ViewHolder, HistoryClosePositionBean> {

    @Override
    public ClosePositionAdapter.ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_position_layout, parent, false);
        return new ClosePositionAdapter.ViewHolder(view);
    }

    @Override
    public void mOnBindViewHolder(ClosePositionAdapter.ViewHolder holder, final int position) {
        holder.mTvProductName.setText(item.instrumentName);
        if (item.type == 1) {
            holder.mTvBuyHands.setText("买跌" + item.position + "手");
            holder.mTvBuyHands.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
        } else {
            holder.mTvBuyHands.setText("买涨" + item.position + "手");
            holder.mTvBuyHands.setTextColor(mContext.getResources().getColor(R.color.color_FB4F4F));
        }

        holder.mTvAveragePrice.setText(item.holdAvgPrice);
        holder.mTvNewPrice.setText(item.closePrice);

        if (TextUtils.isEmpty(item.closeProfitLoss)) {
            holder.mTvProfitLoss.setTextColor(mContext.getResources().getColor(R.color.color_333333));
            holder.mTvProfitLoss.setText("--");
        } else {
            if (Double.parseDouble(item.closeProfitLoss) > 0) {
                holder.mTvProfitLoss.setTextColor(mContext.getResources().getColor(R.color.color_FB4F4F));
                holder.mTvProfitLoss.setText("+" + item.closeProfitLoss);
            } else if (Double.parseDouble(item.closeProfitLoss) < 0) {
                holder.mTvProfitLoss.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
                holder.mTvProfitLoss.setText(item.closeProfitLoss);
            } else {
                holder.mTvProfitLoss.setTextColor(mContext.getResources().getColor(R.color.color_333333));
                holder.mTvProfitLoss.setText(item.closeProfitLoss);
            }
        }

        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CloseTransactionDetailsActivity.startCloseTransactionDetailsActivity(mContext, data.get(position));
            }
        });
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView mTvProductName;
        @BindView(R.id.tv_buy_hands)
        TextView mTvBuyHands;
        @BindView(R.id.tv_average_price)
        TextView mTvAveragePrice;
        @BindView(R.id.tv_new_price)
        TextView mTvNewPrice;
        @BindView(R.id.tv_profit_loss)
        TextView mTvProfitLoss;
        @BindView(R.id.ll_item)
        LinearLayout llItem;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
