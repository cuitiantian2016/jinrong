package com.honglu.future.ui.live;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.ui.circle.circlemain.adapter.CommonAdapter;


public class LiveAdapter extends CommonAdapter<LiveListBean> {

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        LiveListBean item = getItem(position);
        holder.bindView(item, convertView, position);
        return convertView;
    }

    class ViewHolder {

        public ViewHolder(View convertView) {

        }

        public void bindView(final LiveListBean item, final View mContext, final int position) {

        }
    }

}
