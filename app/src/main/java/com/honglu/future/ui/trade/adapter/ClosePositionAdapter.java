package com.honglu.future.ui.trade.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.ui.trade.bean.ClosePositionListBean;
import com.honglu.future.widget.recycler.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zq on 2017/10/28.
 */

public class ClosePositionAdapter extends BaseRecyclerAdapter<ClosePositionAdapter.ViewHolder, ClosePositionListBean> {

    @Override
    public ClosePositionAdapter.ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_position_layout, parent, false);
        return new ClosePositionAdapter.ViewHolder(view);
    }

    @Override
    public void mOnBindViewHolder(ClosePositionAdapter.ViewHolder holder, int position) {
        holder.mTvProductName.setText(item.getProductName());
        holder.mTvBuyHands.setText(item.getBuyHands());
        holder.mTvAveragePrice.setText(item.getAveragePrice());
        holder.mTvNewPrice.setText(item.getNewPrice());
        holder.mTvProfitLoss.setText(item.getProfit());
        if (item.getBuyRiseDown().equals("rise")) {
            holder.mTvBuyHands.setTextColor(mContext.getResources().getColor(R.color.color_FB4F4F));
            holder.mTvProfitLoss.setTextColor(mContext.getResources().getColor(R.color.color_FB4F4F));
        } else {
            holder.mTvBuyHands.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
            holder.mTvProfitLoss.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
        }
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


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
