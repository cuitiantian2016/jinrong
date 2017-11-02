package com.honglu.future.ui.usercenter.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.ui.usercenter.bean.HistoryRecordsBean;
import com.honglu.future.widget.recycler.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zq on 2017/11/2.
 */

public class HistoryRecordsAdapter extends BaseRecyclerAdapter<HistoryRecordsAdapter.ViewHolder, HistoryRecordsBean> {
    @Override
    public HistoryRecordsAdapter.ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_history_records, parent, false);
        return new HistoryRecordsAdapter.ViewHolder(view);
    }

    @Override
    public void mOnBindViewHolder(HistoryRecordsAdapter.ViewHolder holder, int position) {
        holder.mTvProductName.setText(item.getName());
        holder.mTvBuyHands.setText(item.getBuyHands());
        holder.mTvBuildPrice.setText(item.getBuildPrice());
        holder.mTvServicePrice.setText(item.getServicePrice());
        if (item.getBuyType().equals("rise")) {
            holder.mTvBuyType.setText("买涨");
        } else {
            holder.mTvBuyType.setText("买跌");
        }
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

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
