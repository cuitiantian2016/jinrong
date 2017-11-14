package com.honglu.future.ui.recharge.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.ui.market.bean.MarketnalysisBean;
import com.honglu.future.ui.recharge.bean.RechangeDetailData;
import com.honglu.future.widget.recycler.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hefei on 2017/10/26.
 * 投资明细的适配器
 */

public class InAndOutDetailAdapter extends BaseRecyclerAdapter<InAndOutDetailAdapter.ViewHolder, RechangeDetailData> {
    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_in_and_out_detail, parent, false));
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, int position) {
        holder.mTvAsses.setText(item.amount);
        holder.mTvType.setText(item.type);
        holder.mTvBank.setText(item.bankName);
        holder.mTvTime.setText(item.date);
        holder.mTvState.setText(item.status);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_type)
        TextView mTvType;
        @BindView(R.id.tv_bank)
        TextView mTvBank;
        @BindView(R.id.tv_asses)
        TextView mTvAsses;
        @BindView(R.id.tv_time)
        TextView mTvTime;
        @BindView(R.id.tv_state)
        TextView mTvState;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
