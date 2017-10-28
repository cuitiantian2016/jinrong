package com.honglu.future.ui.trade.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.widget.popupwind.BottomPopupWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhuaibing on 2017/10/27
 */

public class PositionAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mList;

    public PositionAdapter(Context context) {
        this.mContext = context;
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

    public void notifyDataChanged(boolean isLoadMore, List<String> list) {
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
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_trade_position_layout, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvBuyCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BottomPopupWindow(mContext, v);
            }
        });
        return convertView;
    }

    static class ViewHolder {
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

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}


