package com.honglu.future.ui.circlefriend;

import android.app.Activity;
import android.content.Context;
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
import com.honglu.future.config.ConfigUtil;
import com.honglu.future.config.Constant;
import com.honglu.future.events.BBSFlownEvent;
import com.honglu.future.events.MessageController;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.http.RxHelper;
import com.honglu.future.ui.circle.bean.UserList;
import com.honglu.future.ui.circle.circlemine.CircleMineActivity;
import com.honglu.future.ui.details.presenter.CommonAdapter;
import com.honglu.future.ui.register.activity.RegisterActivity;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ToastUtil;
import com.honglu.future.widget.CircleImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


public class MyFriendsAdapter extends CommonAdapter<UserList> {

    private Context mContexts;
    private ListView mListView;
    private List<UserList> mList = new ArrayList<UserList>();
    private int mDirection;//1 关注 2 粉丝

    public interface ScrollToLastCallBack {
        void onScrollToLast(Integer pos);
    }

    public MyFriendsAdapter(int direction, ListView listview, Context context) {
        mListView = listview;
        mContexts = context;
        mDirection = direction;
    }

    public void setDatas(List<UserList> list) {
        for (int i = 0; i < list.size(); i++) {
            //if (list.get(i).status.equals("1"))
            mList.add(list.get(i));
        }
        notifyDataSetChanged();
    }

    public void clearDatas() {
        mList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public UserList getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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

    //    class ViewHolder {
//
//
//        public ViewHolder(View convertView) {
//
//        }
//
//        public void bindView(final UserList item, final int position) {
//
//            int end = mListView.getLastVisiblePosition();
//            if (getCount() - 2 <= end && end <= getCount())
//                mScrollToLastCallBack.onScrollToLast(position);
//
//        }
//
//
//    }
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
            ImageUtil.display(ConfigUtil.baseImageUserUrl + item.avatarPic, user_img, R.mipmap.img_head);
            user_name.setText(item.nickName);
            flag.setVisibility(TextUtils.isEmpty(item.userRole)? View.GONE: View.VISIBLE);
            flag.setText(item.userRole);
            tv_attention_num.setText("粉丝数: " + item.beFocusNum);
            tv_topic_num.setText("发帖数: " + item.postNum);
            //用户是否已经关注
            iv_attention.setImageResource(item.focued ? R.mipmap.already_recommend : R.mipmap.add_recommend);
            iv_attention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (DeviceUtils.isFastDoubleClick()) {
                        return;
                    }
                    String user_id = SpUtil.getString(Constant.CACHE_TAG_UID);
                    if (TextUtils.isEmpty(user_id)) {
                        mContext.getContext().startActivity(new Intent(mContext.getContext(), RegisterActivity.class));
                        return;
                    }

                    if (user_id.equals(item.userId)) {
                        ToastUtil.show("自己不能关注自己");
                        return;
                    }

                    final String foll = item.focued ? "0" : "1";
                    HttpManager.getApi().focus(item.userId, SpUtil.getString(Constant.CACHE_TAG_UID), foll).compose(RxHelper.<JsonNull>handleSimplyResult()).subscribe(new HttpSubscriber<JsonNull>() {
                        @Override
                        protected void _onNext(JsonNull jsonNull) {
                            super._onNext(jsonNull);
                            if(foll.equals("0")){
                                ToastUtil.show("取消关注成功");
                            } else{
                                ToastUtil.show("关注成功");
                            }
                            if (MessageController.getInstance().getFriendCountChange() != null) {
                                MessageController.getInstance().getFriendCountChange().change();
                            }
                            if (MessageController.getInstance().getBeFocusedCountChange() != null) {
                                MessageController.getInstance().getBeFocusedCountChange().change();
                            }
                            BBSFlownEvent bbsFlownEvent = new BBSFlownEvent();
                            bbsFlownEvent.uid = item.userId;
                            bbsFlownEvent.follow = foll;
                            ((MyFriendActivity) mContexts).setData(foll, mDirection);
                            EventBus.getDefault().post(bbsFlownEvent);
                        }

                        @Override
                        protected void _onError(String message) {
                            super._onError(message);
                            ToastUtil.show(message);
                        }
                    });
                }
            });

            user_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (DeviceUtils.isFastDoubleClick()) {
                        return;
                    }
                    String user_id = SpUtil.getString(Constant.CACHE_TAG_UID);
                    if (TextUtils.isEmpty(user_id)) {
                        mContext.getContext().startActivity(new Intent(mContext.getContext(), RegisterActivity.class));
                        return;
                    }
                    if (item.userId.equals(user_id)) {
                        return;
                    } else {
                        Intent intent = new Intent(mContext.getContext(), CircleMineActivity.class);
                        intent.putExtra("userId", item.userId);
                        intent.putExtra("imgHead", ConfigUtil.baseImageUserUrl + item.avatarPic);
                        intent.putExtra("nickName", item.nickName);
                        mContext.getContext().startActivity(intent);
                    }
                }
            });
        }

    }
}


