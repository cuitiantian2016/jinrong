package com.honglu.future.ui.trade.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.ui.trade.bean.EntrustBean;
import com.honglu.future.widget.recycler.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zq on 2017/10/30.
 */

public class EntrustAdapter extends BaseRecyclerAdapter<EntrustAdapter.ViewHolder, EntrustBean> {
    @Override
    public EntrustAdapter.ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_entrust_layout, parent, false);
        return new EntrustAdapter.ViewHolder(view);
    }

    @Override
    public void mOnBindViewHolder(EntrustAdapter.ViewHolder holder, int position) {
        holder.mTvProductName.setText(item.getName());
        holder.mTvBuyHands.setText(item.getBuyHands());
        holder.mTvEntrustPrice.setText(item.getEnturstPrice());
        holder.mTvDate.setText(item.getEntrustDate());
        holder.mTvServiceCharge.setText(item.getServiceCharge());
        holder.mTvLimitDate.setText(item.getLimitDate());
        holder.mTvBond.setText(item.getBond());
        if (item.getEntrustType().equals("open")) {
            holder.mBtnEntrust.setText("建仓委托");
        } else {
            holder.mBtnEntrust.setText("平仓委托");
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView mTvProductName;
        @BindView(R.id.tv_buy_hands)
        TextView mTvBuyHands;
        @BindView(R.id.tv_entrust_price)
        TextView mTvEntrustPrice;
        @BindView(R.id.tv_date)
        TextView mTvDate;
        @BindView(R.id.tv_service_charge)
        TextView mTvServiceCharge;
        @BindView(R.id.tv_limit_date)
        TextView mTvLimitDate;
        @BindView(R.id.tv_bond)
        TextView mTvBond;
        @BindView(R.id.btn_entrust)
        TextView mBtnEntrust;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
