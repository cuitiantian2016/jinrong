package com.honglu.future.ui.msg.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.gson.JsonNull;
import com.honglu.future.R;
import com.honglu.future.config.Constant;
import com.honglu.future.events.BBSFlownEvent;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.http.RxHelper;
import com.honglu.future.ui.circle.circlemain.adapter.CommonAdapter;
import com.honglu.future.ui.live.bean.LiveListBean;
import com.honglu.future.ui.live.player.PlayerActivity;
import com.honglu.future.ui.msg.bean.SystemMsgBean;
import com.honglu.future.ui.msg.mainmsg.SystemMsgDetailActivity;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SystemMsgAdapter extends CommonAdapter<SystemMsgBean> {

    public SystemMsgAdapter() {
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_system_msg, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.bindView(getItem(position) ,this);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.tv_time)
        TextView tv_time;;
        @BindView(R.id.v_msg_system_red)
       View view;
        View convertView;
        Context context;

        public ViewHolder(View convertView) {
            ButterKnife.bind(this, convertView);
            context = convertView.getContext();
            this.convertView = convertView;
        }

        public void bindView(final SystemMsgBean item, final SystemMsgAdapter adapter) {
            tv_title.setText(item.title);
            tv_time.setText(item.time);
            view.setVisibility(item.isRead()?View.VISIBLE:View.INVISIBLE);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!TextUtils.isEmpty(item.content)){
                        item.status = 1;
                        adapter.notifyDataSetChanged();
                        SystemMsgDetailActivity.startSystemMsgDetailActivity(context,item);
                    }
                }
            });
        }
    }

}
