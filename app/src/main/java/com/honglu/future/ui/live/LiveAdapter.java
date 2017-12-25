package com.honglu.future.ui.live;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonNull;
import com.honglu.future.R;
import com.honglu.future.config.Constant;
import com.honglu.future.events.BBSFlownEvent;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.http.RxHelper;
import com.honglu.future.ui.circle.circlemain.adapter.CommonAdapter;
import com.honglu.future.ui.register.activity.RegisterActivity;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class LiveAdapter extends CommonAdapter<LiveListBean> {
    ListView listView;
    public LiveAdapter(ListView listView){
        this.listView = listView;
    }

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

        final LiveListBean item = getItem(position);

        holder.bindView(item,position ==(getCount()-1),listView,position);
        if (item.isFollow()){
            holder.tv_teacher_attention.setOnClickListener(null);
        }else {
            holder.tv_teacher_attention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (DeviceUtils.isFastDoubleClick()) {
                        return;
                    }
                    String user_id = SpUtil.getString(Constant.CACHE_TAG_UID);
                    if (user_id.equals(item.liveTeacherID)) {
                        ToastUtil.show("自己不能关注自己");
                        return;
                    }
                    final String foll = item.follow.equals("1") ? "0" : "1";
                    HttpManager.getApi().focus(item.liveTeacherID, SpUtil.getString(Constant.CACHE_TAG_UID), foll).compose(RxHelper.<JsonNull>handleSimplyResult()).subscribe(new HttpSubscriber<JsonNull>() {
                        @Override
                        protected void _onNext(JsonNull jsonNull) {
                            super._onNext(jsonNull);
                            if (foll.equals("0")) {
                                ToastUtil.show("取消关注成功");
                            } else {
                                ToastUtil.show("关注成功");
                            }
                            item.follow = foll;
                            BBSFlownEvent bbsFlownEvent = new BBSFlownEvent();
                            bbsFlownEvent.follow = foll;
                            bbsFlownEvent.uid = item.liveTeacherID;
                            EventBus.getDefault().post(bbsFlownEvent);
                            notifyDataSetChanged();
                        }

                        @Override
                        protected void _onError(String message) {
                            super._onError(message);
                            ToastUtil.show(message);
                        }
                    });
                }
            });
        }
        return convertView;
    }

   static class ViewHolder {
        @BindView(R.id.iv_bg)
        ImageView iv_bg;
        @BindView(R.id.iv_teach)
        ImageView iv_teach;
        @BindView(R.id.tv_teacher_name)
        TextView tv_teacher_name;
        @BindView(R.id.tv_teacher_attention)
        ImageView tv_teacher_attention;
        @BindView(R.id.tv_live_state)
        TextView tv_live_state;
        @BindView(R.id.tv_live_content)
        TextView tv_live_content;
        @BindView(R.id.tv_live_title)
        TextView tv_live_title;
        @BindView(R.id.tv_live_num)
        TextView tv_live_num;
        @BindView(R.id.tv_teach_des)
        TextView tv_teach_des;
       Context context;
       View convertView;

        public ViewHolder(View convertView) {
            ButterKnife.bind(this,convertView);
            context = convertView.getContext();
            this.convertView = convertView;
        }

        public void bindView(final LiveListBean item, final boolean islast, final ListView listView, final int p) {
            ImageUtil.display(item.liveImg, iv_bg, R.mipmap.timg);
            ImageUtil.display(item.liveTeacherICon, iv_teach, R.mipmap.img_head);
            tv_teacher_name.setText(item.liveTeacher);
            tv_teach_des.setText(item.liveTeacherDes);
            tv_teach_des.setVisibility(View.GONE);
            tv_live_title.setText(item.liveTitle);
            tv_live_content.setText(item.liveDes);
            tv_teacher_attention.setImageResource(item.isFollow()?R.mipmap.already_recommend:R.mipmap.add_recommend);
            if (item.isLive()&&!TextUtils.isEmpty(item.liveNum)){
                tv_live_num.setVisibility(View.VISIBLE);
                tv_live_num.setText(context.getString(R.string.live_num,item.liveNum));
                tv_live_state.setText("直播中");
                tv_live_state.setBackground(context.getResources().getDrawable(R.drawable.bg_live));
                Drawable drawable = context.getResources().getDrawable(R.drawable.combinedshape);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tv_live_state.setCompoundDrawables(drawable,null,null,null);
            }else {
                tv_live_num.setVisibility(View.GONE);
                tv_live_state.setText(item.liveTime);
                tv_live_state.setBackground(context.getResources().getDrawable(R.drawable.bg_no_live));
                Drawable drawable = context.getResources().getDrawable(R.mipmap.oval);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tv_live_state.setCompoundDrawables(drawable,null,null,null);
            }
            tv_teacher_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int visibility = tv_teach_des.getVisibility();
                    if (visibility !=View.VISIBLE){
                        tv_teach_des.setVisibility(View.VISIBLE);
                        Drawable drawable = context.getResources().getDrawable(R.mipmap.icon_up);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        tv_teacher_name.setCompoundDrawables(null,null,drawable,null);
                        if (islast){
                            listView.setSelection(p);
                        }
                    }else {
                        tv_teach_des.setVisibility(View.GONE);
                        Drawable drawable = context.getResources().getDrawable(R.mipmap.icon_down);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        tv_teacher_name.setCompoundDrawables(null,null,drawable,null);
                    }
                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToastUtil.show("点击了"+p);
                }
            });
        }
    }

}
