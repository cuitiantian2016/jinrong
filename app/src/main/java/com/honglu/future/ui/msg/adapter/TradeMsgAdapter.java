package com.honglu.future.ui.msg.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.ui.circle.circlemain.adapter.CommonAdapter;
import com.honglu.future.ui.msg.bean.SystemMsgBean;
import com.honglu.future.ui.msg.bean.TradeMsgBean;
import com.honglu.future.ui.msg.mainmsg.SystemMsgDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TradeMsgAdapter extends CommonAdapter<SystemMsgBean> {

    public TradeMsgAdapter() {
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trade_msg, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.bindView(getItem(position));
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.tv_time)
        TextView tv_time;
        View convertView;
        Context context;

        public ViewHolder(View convertView) {
            ButterKnife.bind(this, convertView);
            context = convertView.getContext();
            this.convertView = convertView;
        }

        public void bindView(final SystemMsgBean item) {
            tv_title.setText(item.content);
            tv_time.setText(item.time);
        }
    }

}
