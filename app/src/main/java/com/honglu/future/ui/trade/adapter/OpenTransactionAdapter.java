package com.honglu.future.ui.trade.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.ui.trade.bean.OpenTransactionListBean;
import com.honglu.future.ui.trade.bean.ProductListBean;
import com.honglu.future.widget.recycler.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zq on 2017/10/26.
 */

public class OpenTransactionAdapter extends BaseRecyclerAdapter<OpenTransactionAdapter.ViewHolder, ProductListBean> {
    public interface OnRiseDownClickListener {
        void onRiseClick(View view, ProductListBean bean);

        void onDownClick(View view, ProductListBean bean);

        void onItemClick(ProductListBean bean);
    }

    private OnRiseDownClickListener mListener;

    public void setOnRiseDownClickListener(OnRiseDownClickListener listener) {
        mListener = listener;
    }

    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_open_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, final int position) {
        holder.mTvProductName.setText(item.getInstrumentName());
        holder.mTvNum.setText(item.getTradeVolume());
        holder.mTvRise.setText(item.getAskPrice1());
        holder.mTvDown.setText(item.getBidPrice1());
//        holder.mTvRiseRadio.setText(item.getLongRate() + "%");
//        holder.mTvDownRadio.setText((100 - Integer.valueOf(item.getLongRate())) + "%");
        if (item.getIsClosed().equals("2")) {
            holder.mTvClosed.setVisibility(View.VISIBLE);
        } else {
            holder.mTvClosed.setVisibility(View.GONE);
        }
        holder.mLlRise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRiseClick(v, data.get(position));
            }
        });

        holder.mLlDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDownClick(v, data.get(position));
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(data.get(position));
            }
        });
        if (position == getData().size() -1){
            holder.mColorLine.setVisibility(View.INVISIBLE);
        }else {
            holder.mColorLine.setVisibility(View.VISIBLE);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_product_name)
        TextView mTvProductName;
        @BindView(R.id.tv_num)
        TextView mTvNum;
        @BindView(R.id.tv_rise)
        TextView mTvRise;
        @BindView(R.id.tv_down)
        TextView mTvDown;
//        @BindView(R.id.tv_rise_radio)
//        TextView mTvRiseRadio;
//        @BindView(R.id.tv_down_radio)
//        TextView mTvDownRadio;
        @BindView(R.id.tv_closed)
        TextView mTvClosed;
        @BindView(R.id.ll_rise)
        LinearLayout mLlRise;
        @BindView(R.id.ll_down)
        LinearLayout mLlDown;
        @BindView(R.id.v_colorLine)
        View mColorLine;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
