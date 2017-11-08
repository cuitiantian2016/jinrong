package com.honglu.future.ui.market.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.ui.market.bean.MarketnalysisBean;
import com.honglu.future.widget.recycler.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hc on 2017/10/25.
 */

public class MarketListAdapter extends BaseRecyclerAdapter<MarketListAdapter.ViewHolder,MarketnalysisBean.ListBean.QuotationDataListBean> {

    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.layout_marketlist_item, parent, false));
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, int position) {
        holder.mTvContractName.setTextSize(14);
        holder.mTvLatestPrice.setTextSize(16);
        holder.mTvQuoteChange.setTextSize(16);
        holder.mTvHavedPositions.setTextSize(16);
        //holder.mRedLayout.setVisibility(View.GONE);
        //holder.mRedLayout.setVisibility(View.VISIBLE);
        //ObjectAnimator alpha = ObjectAnimator.ofFloat(holder.mRedLayout, "alpha", 0f, 1f, 0f);
        //alpha.setDuration(500);
        //alpha.setRepeatCount(1);
        //alpha.start();

        holder.mTvContractName.setText(item.getName());
        holder.mTvLatestPrice.setText(item.getLastPrice());
        holder.mTvQuoteChange.setText(item.getChg());
        holder.mTvHavedPositions.setText(item.getOpenInterest());
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_contract_name)
        TextView mTvContractName;//合约名称
        @BindView(R.id.text_latest_price)
        TextView mTvLatestPrice;//最新价
        @BindView(R.id.fl_red)
        FrameLayout mRedLayout;
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
