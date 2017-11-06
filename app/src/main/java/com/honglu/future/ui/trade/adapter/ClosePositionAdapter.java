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
        holder.mTvProductName.setText(item.getInstrumentName());
        if (item.getType() == 1) {
            holder.mTvBuyHands.setText("买跌" + item.getPosition() + "手");
        } else {
            holder.mTvBuyHands.setText("买涨" + item.getPosition() + "手");
        }

        holder.mTvAveragePrice.setText(item.getHoldAvgPrice());
        // TODO: 2017/11/6 确认是否展示最新价（猜测为平仓价） 
        holder.mTvNewPrice.setText(item.getClosePrice());
        holder.mTvProfitLoss.setText(item.getCloseProfitLoss());
        if (item.getType() == 2) {
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
