package com.honglu.future.ui.trade.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.ui.trade.bean.EntrustBean;
import com.honglu.future.util.TimeUtil;
import com.honglu.future.widget.recycler.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zq on 2017/10/30.
 */

public class EntrustAdapter extends BaseRecyclerAdapter<EntrustAdapter.ViewHolder, EntrustBean> {
    public interface OnCancelClickListener {
        void onCancelClick(EntrustBean bean);
    }

    private OnCancelClickListener mListener;

    public void setOnCancelClickListener(OnCancelClickListener listener) {
        mListener = listener;
    }

    @Override
    public EntrustAdapter.ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_entrust_layout, parent, false);
        return new EntrustAdapter.ViewHolder(view);
    }

    @Override
    public void mOnBindViewHolder(EntrustAdapter.ViewHolder holder, final int position) {
        holder.mTvProductName.setText(item.getInstrumentName());
        if (item.getType() == 1) {
            holder.mTvBuyHands.setText("买跌" + item.getPosition() + "手");
            holder.mTvBuyHands.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
        } else {
            holder.mTvBuyHands.setText("买涨" + item.getPosition() + "手");
            holder.mTvBuyHands.setTextColor(mContext.getResources().getColor(R.color.color_FB4F4F));
        }


        holder.mTvEntrustPrice.setText(item.getPrice());
        holder.mTvDate.setText(TimeUtil.getStringByFormat(item.getInsertTime(),TimeUtil.dateFormatYMD));
        holder.mTvServiceCharge.setText(item.getSxf());
        // TODO: 2017/11/6 确定是否需要区分当日有效 today
        holder.mTvLimitDate.setText("当日有效");
        holder.mTvBond.setText(item.getUseMargin());
        if (item.getOpenClose() == 1) {
            holder.mBtnEntrust.setText("建仓委托");
        } else {
            holder.mBtnEntrust.setText("平仓委托");
        }
        holder.mTvChedan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancelClick(getData().get(position));
            }
        });
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
        @BindView(R.id.tv_chedan)
        TextView mTvChedan;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
