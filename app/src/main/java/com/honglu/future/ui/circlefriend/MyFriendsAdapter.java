package com.honglu.future.ui.circlefriend;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.honglu.future.R;
import com.honglu.future.config.Constant;
import com.honglu.future.ui.circle.bean.UserList;
import com.honglu.future.ui.details.presenter.CommonAdapter;
import com.honglu.future.ui.register.activity.RegisterActivity;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ToastUtil;
import com.honglu.future.widget.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MyFriendsAdapter extends CommonAdapter<UserList> {

    private Context mContext;
    private ListView mListView;
    private ScrollToLastCallBack mScrollToLastCallBack = null;
    private List<UserList> mList = new ArrayList<UserList>();
    private String mDirection;

    public interface ScrollToLastCallBack {
        void onScrollToLast(Integer pos);
    }

    public MyFriendsAdapter(String direction, ListView listview, Context context, ScrollToLastCallBack callback) {
        mListView = listview;
        mContext = context;
        mScrollToLastCallBack = callback;
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
        holder.bindView(item,convertView,position);
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
     class  ViewHolder{
        CircleImageView user_img;
        TextView user_name,flag,tv_attention_num,tv_topic_num;
        ImageView iv_attention;

        public ViewHolder(View convertView) {
            user_img= (CircleImageView) convertView.findViewById(R.id.user_img);
            user_name= (TextView) convertView.findViewById(R.id.user_name);
            flag= (TextView) convertView.findViewById(R.id.flag);
            tv_attention_num= (TextView) convertView.findViewById(R.id.tv_attention_num);
            tv_topic_num= (TextView) convertView.findViewById(R.id.tv_topic_num);
            iv_attention= (ImageView) convertView.findViewById(R.id.iv_attention);
        }

        public void bindView(final UserList item, final View mContext, final int position) {
            ImageUtil.display(item.avatarPic,user_img,R.mipmap.ic_logos);
            user_name.setText(item.nickName);
            // TODO: 2017/12/11 用户角色，需要接口添加
//            flag.setVisibility(TextUtils.isEmpty(item.user_level)? View.GONE: View.VISIBLE);
//            flag.setText(item.user_level);
            tv_attention_num.setText("粉丝数: "+item.beFocusNum);
            tv_topic_num.setText("发帖数: "+item.postNum);
            //用户是否已经关注
            iv_attention.setImageResource(item.focued?R.mipmap.already_recommend:R.mipmap.add_recommend);
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
//                    String type = item.follow.equals("1") ? "2" : "1";

//                    ServerAPI.follow(mContext.getContext(), type, item.uid, new ServerCallBack<JSONObject>() {
//                        @Override
//                        public void onSucceed(Context context, JSONObject result) {
//                            try {
//                                if (MessageController.getInstance().getFriendCountChange() != null){
//                                    MessageController.getInstance().getFriendCountChange().change();
//                                }
//                                String msg = result.getString("msg");
//                                if (msg.equals("取消关注成功")) {
//                                    iv_attention.setImageResource(R.drawable.add_recommend);
////                                    Toaster.toast(msg);
//                                    item.follow = "0";
//                                } else if (msg.equals("关注成功")) {
//                                    iv_attention.setImageResource(R.drawable.already_recommend);
////                                    Toaster.toast(msg);
//                                    item.follow = "1";
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        @Override
//                        public void onError(Context context, String errorMsg) {
//                            Toaster.toast(errorMsg);
//                        }
//
//                        @Override
//                        public void onFinished(Context context) {
////                            loadingDialog.dismiss();
//                        }
//                    });
                }
            });
                int end = mListView.getLastVisiblePosition();
                if (getCount() - 2 <= end && end <= getCount())
                    mScrollToLastCallBack.onScrollToLast(position);
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
                    if(item.userId.equals(user_id)){
                        return;
                    }else{
                        // TODO: 2017/12/7 跳转圈友主页 
//                        Intent i1 = new Intent(mContext.getContext(), OtherDetailActivity.class);
//                        i1.putExtra("fid", item.uid);
//                        i1.putExtra("user_name", item.user_name);
//                        mContext.getContext().startActivity(i1);
                    }
                }
            });
        }

        }
    }


