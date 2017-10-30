package com.honglu.future.dialog;

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
 * Created by zhuaibing on 2017/10/30
 */

public class PositionDialogAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mList;

    public PositionDialogAdapter(Context context, List<String> list) {
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

    public void notifyDataChanged(List<String> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_position_dialog_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }


    static class ViewHolder {
        TextView tvBuyHands;
        TextView tvTime;
        TextView tvPrice;
        TextView tvBond;
        TextView tvServiceCharge;

        ViewHolder(View view) {
            tvBuyHands = (TextView) view.findViewById(R.id.tv_buy_hands);
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            tvPrice = (TextView) view.findViewById(R.id.tv_price);
            tvBond = (TextView) view.findViewById(R.id.tv_bond);
            tvServiceCharge = (TextView) view.findViewById(R.id.tv_service_charge);
        }
    }
}
