package com.honglu.future.ui.circle.praisesandreward;

import android.content.Intent;
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
import com.honglu.future.ui.login.activity.LoginActivity;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ToastUtil;
import com.honglu.future.widget.CircleImageView;

import org.greenrobot.eventbus.EventBus;


public class GetFriendsAdapter extends CommonAdapter<UserList> {
    private ListView mListView;
    private ScrollToLastCallBack mScrollToLastCallBack;

    public void setListView(ListView listView, ScrollToLastCallBack scrollToLastCallBack) {
        this.mListView = listView;
        this.mScrollToLastCallBack = scrollToLastCallBack;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myfriends, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        UserList item = getItem(position);
        holder.bindView(item, convertView, position);
        return convertView;
    }

    class ViewHolder {
        CircleImageView user_img;
        TextView user_name, flag, tv_attention_num, tv_topic_num;
        ImageView iv_attention;

        public ViewHolder(View convertView) {
            user_img = (CircleImageView) convertView.findViewById(R.id.user_img);
            user_name = (TextView) convertView.findViewById(R.id.user_name);
            flag = (TextView) convertView.findViewById(R.id.flag);
            tv_attention_num = (TextView) convertView.findViewById(R.id.tv_attention_num);
            tv_topic_num = (TextView) convertView.findViewById(R.id.tv_topic_num);
            iv_attention = (ImageView) convertView.findViewById(R.id.iv_attention);
        }

        public void bindView(final UserList item, final View mContext, final int position) {
            ImageUtil.display(item.headimgurl, user_img, R.mipmap.img_head);
            user_name.setText(item.user_name);
            flag.setVisibility(TextUtils.isEmpty(item.user_level) ? View.GONE : View.VISIBLE);
            flag.setText(item.user_level);
            tv_attention_num.setText("粉丝数: " + item.fans_num);
            tv_topic_num.setText("发帖数: " + item.topic_num);
            if (item.uid.equals(SpUtil.getString(Constant.CACHE_TAG_UID))) {
                iv_attention.setVisibility(View.INVISIBLE);
            } else {
                iv_attention.setVisibility(View.VISIBLE);
                iv_attention.setImageResource(item.follow.equals("0") ? R.drawable.add_recommend : R.mipmap.already_recommend);
                iv_attention.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (DeviceUtils.isFastDoubleClick()) {
                            return;
                        }
                        if (TextUtils.isEmpty(SpUtil.getString(Constant.CACHE_TAG_UID))) {
                            mContext.getContext().startActivity(new Intent(mContext.getContext(), LoginActivity.class));
                            return;
                        }
                        String user_id = SpUtil.getString(Constant.CACHE_TAG_UID);
                        if (user_id.equals(item.uid)) {
                            ToastUtil.show("自己不能关注自己");
                            return;
                        }
                        final String type = item.follow.equals("1") ? "0" : "1";
                        HttpManager.getApi().focus(item.uid,SpUtil.getString(Constant.CACHE_TAG_UID),type).compose(RxHelper.<JsonNull>handleSimplyResult()).subscribe(new HttpSubscriber<JsonNull>() {
                            @Override
                            protected void _onNext(JsonNull jsonNull) {
                                super._onNext(jsonNull);
                                iv_attention.setImageResource(R.mipmap.already_recommend);
                                item.follow = type;
                                BBSFlownEvent bbsFlownEvent = new BBSFlownEvent();
                                bbsFlownEvent.follow = type;
                                bbsFlownEvent.uid = item.uid;
                                EventBus.getDefault().post(bbsFlownEvent);
                                notifyDataSetChanged();
                            }

                            @Override
                            protected void _onError(String message) {
                                super._onError(message);
                                ToastUtil.show(message);
                            }
                        });
//                        ServerAPI.follow(mContext.getContext(), type, item.uid, new ServerCallBack<JSONObject>() {
//                            @Override
//                            public void onSucceed(Context context, JSONObject result) {
//                                try {
//                                    if (MessageController.getInstance().getFriendCountChange() != null) {
//                                        MessageController.getInstance().getFriendCountChange().change();
//                                    }
//                                    String msg = result.getString("msg");
//                                    if (msg.equals("取消关注成功")) {
//                                        iv_attention.setImageResource(R.drawable.add_recommend);
//                                        item.follow = "0";
//                                    } else if (msg.equals("关注成功")) {
//                                        iv_attention.setImageResource(R.drawable.already_recommend);
//                                        item.follow = "1";
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                            @Override
//                            public void onError(Context context, String errorMsg) {
//                                Toaster.toast(errorMsg);
//                            }
//                            @Override
//                            public void onFinished(Context context) {
//                            }
//                        });
                    }
                });
            }
            user_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (DeviceUtils.isFastDoubleClick()) {
                        return;
                    }
                    if (TextUtils.isEmpty(SpUtil.getString(Constant.CACHE_TAG_UID))) {
                        mContext.getContext().startActivity(new Intent(mContext.getContext(), LoginActivity.class));
                        return;
                    }
                    if (item.uid.equals(SpUtil.getString(Constant.CACHE_TAG_UID))) {
                        return;
                    } else {//个人主页
//                        Intent i1 = new Intent(mContext.getContext(), OtherDetailActivity.class);
//                        i1.putExtra("fid", item.uid);
//                        i1.putExtra("user_name", item.user_name);
//                        mContext.getContext().startActivity(i1);
                    }
                }
            });
            if (mListView != null && mScrollToLastCallBack != null){
                int end = mListView.getLastVisiblePosition();
                if (getCount() - 2 <= end && end <= getCount())
                    mScrollToLastCallBack.onScrollToLast(position);
            }
        }
    }

    public interface ScrollToLastCallBack {
        void onScrollToLast(Integer pos);
    }

}
