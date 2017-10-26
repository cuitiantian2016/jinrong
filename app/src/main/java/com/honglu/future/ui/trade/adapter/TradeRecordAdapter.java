package com.honglu.future.ui.trade.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.honglu.future.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhuaibing on 2017/10/26
 */

public class TradeRecordAdapter extends BaseAdapter {
    private List<String> mList;
    private Context mContext;

    public TradeRecordAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.mList = list;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_trade_record_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_purchaseSize)
        TextView tvPurchaseSize;
        @BindView(R.id.tv_ccmoney)
        TextView tvCcmoney;
        @BindView(R.id.tv_newMoney)
        TextView tvNewMoney;
        @BindView(R.id.tv_profitLossMoney)
        TextView tvProfitLossMoney;
        @BindView(R.id.v_line)
        View vLine;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
