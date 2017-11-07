package com.honglu.future.ui.market.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.widget.recycler.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hc on 2017/10/25.
 */

public class MarketListAdapter extends BaseRecyclerAdapter<MarketListAdapter.ViewHolder,String> {

    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.layout_marketlist_item, parent, false));
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, int position) {
        holder.mTvLatestPrice.setBackgroundResource(R.drawable.shape_2dp_26fb4f4f);
        holder.mTvContractName.setTextSize(14);
        holder.mTvLatestPrice.setTextSize(16);
        holder.mTvQuoteChange.setTextSize(16);
        holder.mTvHavedPositions.setTextSize(16);

//        holder.mTvContractName.setText(item);
//        holder.mTvLatestPrice.setText(item);
//        holder.mTvQuoteChange.setText(item);
//        holder.mTvHavedPositions.setText(item);
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_contract_name)
        TextView mTvContractName;//合约名称
        @BindView(R.id.text_latest_price)
        TextView mTvLatestPrice;//最新价
        @BindView(R.id.text_quote_change)
        TextView mTvQuoteChange;//涨幅量
        @BindView(R.id.text_haved_positions)
        TextView mTvHavedPositions;//持仓量

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
