package com.honglu.future.ui.trade.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.ui.trade.bean.HoldPositionBean;
import com.honglu.future.ui.trade.bean.ProductListBean;
import com.honglu.future.ui.trade.fragment.PositionFragment;
import com.honglu.future.ui.trade.kchart.KLineMarketActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhuaibing on 2017/10/27
 */

public class PositionAdapter extends BaseAdapter {

    private Context mContext;
    private PositionFragment mFragment;
    private List<HoldPositionBean> mList;

    public interface OnShowPopupClickListener {
        void onClosePositionClick(View view, HoldPositionBean bean,int position);

        void onHoldDetailClick(View view, HoldPositionBean bean,int position);
    }

    private OnShowPopupClickListener mListener;

    public void setOnShowPopupClickListener(OnShowPopupClickListener listener) {
        mListener = listener;
    }

    public PositionAdapter(PositionFragment fragment) {
        this.mFragment = fragment;
        this.mContext = mFragment.getActivity();
        this.mList = new ArrayList<>();
    }

    public int getProductIndex(String codes) {
        for (HoldPositionBean productObj : mList) {
            if (codes.equals(productObj.getInstrumentId())) {
                return mList.indexOf(productObj);
            }
        }
        return 0;
    }


    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void notifyDataChanged(boolean isLoadMore, List<HoldPositionBean> list) {
        if (isLoadMore) {
            if (list != null && list.size() > 0) {
                mList.addAll(list);
            }
        } else {
            mList.clear();
            if (list != null && list.size() > 0) {
                mList = list;
            }
        }
        if (mList.size() > 0) {
            mFragment.setEmptyView(false);
        } else {
            mFragment.setEmptyView(true);
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final HoldPositionBean holdPositionBean = mList.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_trade_position_layout, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            holder.vLine.setVisibility(View.GONE);
        } else {
            holder.vLine.setVisibility(View.VISIBLE);
        }

        final ViewHolder finalHolder = holder;
        holder.tvName.setText(holdPositionBean.getInstrumentName());
        if (holdPositionBean.getType() == 1) {
            holder.tvBuyCount.setText("买跌" + holdPositionBean.getPosition() + "手");
            holder.tvBuyCount.setTextColor(mContext.getResources().getColor(R.color.color_opt_lt));
        } else {
            holder.tvBuyCount.setText("买涨" + holdPositionBean.getPosition() + "手");
            holder.tvBuyCount.setTextColor(mContext.getResources().getColor(R.color.color_opt_gt));
        }

        holder.tvAveragePrice.setText(holdPositionBean.getOpenAvgPrice());
        holder.tvNewPrice.setText(holdPositionBean.getLastPrice());

        if (Double.parseDouble(holdPositionBean.getCcProfit()) > 0) {
            holder.tvProfitLoss.setTextColor(mContext.getResources().getColor(R.color.color_opt_gt));
            holder.tvProfitLoss.setText("+" + holdPositionBean.getCcProfit());
        } else if (Double.parseDouble(holdPositionBean.getCcProfit()) < 0) {
            holder.tvProfitLoss.setTextColor(mContext.getResources().getColor(R.color.color_opt_lt));
            holder.tvProfitLoss.setText(holdPositionBean.getCcProfit());
        } else {
            holder.tvProfitLoss.setTextColor(mContext.getResources().getColor(R.color.color_333333));
            holder.tvProfitLoss.setText(holdPositionBean.getCcProfit());
        }

        holder.mLlView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, KLineMarketActivity.class);
                intent.putExtra("excode", holdPositionBean.getExcode());
                intent.putExtra("code", holdPositionBean.getInstrumentId());
                intent.putExtra("isClosed", "1");
                mContext.startActivity(intent);
            }
        });

        holder.mTvFastClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClosePositionClick(v, holdPositionBean,position);
            }
        });

        holder.mTvHoldDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onHoldDetailClick(v, holdPositionBean,position);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.v_line)
        View vLine;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_buy_count)
        TextView tvBuyCount;
        @BindView(R.id.tv_average_price)
        TextView tvAveragePrice;
        @BindView(R.id.tv_new_price)
        TextView tvNewPrice;
        @BindView(R.id.tv_profit_loss)
        TextView tvProfitLoss;
        @BindView(R.id.v_bootomView)
        View vBottomView;
        @BindView(R.id.tv_fast_close)
        TextView mTvFastClose;
        @BindView(R.id.tv_hold_detail)
        TextView mTvHoldDetail;
        @BindView(R.id.ll_view)
        LinearLayout mLlView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}


