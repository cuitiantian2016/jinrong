package com.honglu.future.ui.trade.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.ui.trade.bean.HoldPositionBean;
import com.honglu.future.ui.trade.fragment.PositionFragment;
import com.honglu.future.ui.trade.kchart.KLineMarketActivity;
import com.honglu.future.widget.popupwind.PositionPopWind;

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
        void onShowPopupClick(View view, HoldPositionBean bean);
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
    public View getView(int position, View convertView, ViewGroup parent) {
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
        } else {
            holder.tvBuyCount.setText("买涨" + holdPositionBean.getPosition() + "手");
        }
        holder.tvAveragePrice.setText(holdPositionBean.getHoldAvgPrice());
        // TODO: 2017/11/6 需要确认最新价的字段 zq
        holder.tvNewPrice.setText(holdPositionBean.getSettlementPrice());
        holder.tvProfitLoss.setText(holdPositionBean.getTotalProfit());

        holder.vBottomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onShowPopupClick(v, holdPositionBean);
            }
        });
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, KLineMarketActivity.class);
                intent.putExtra("excode",holdPositionBean.getExcode());
                intent.putExtra("code",holdPositionBean.getInstrumentId());
                intent.putExtra("isClosed","1");
                mContext.startActivity(intent);
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

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}


