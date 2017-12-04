package com.honglu.future.ui.details.presenter;

import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.config.ConfigUtil;
import com.honglu.future.ui.details.bean.InformationCommentBean;
import com.honglu.future.ui.details.bean.onIconClickListener;
import com.honglu.future.util.AndroidUtil;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.util.ViewHelper;
import com.honglu.future.widget.CircleImageView;

import java.util.List;


/**
 * 最新消息详情评论Adapter
 */
public class TopicAdapter extends CommonAdapter<InformationCommentBean> {

    private onIconClickListener mOnIconClickListener;
    private String nickName = null;

    public void setOnIconClickListener(onIconClickListener onIconClickListener) {
        mOnIconClickListener = onIconClickListener;
    }


    public interface ScrollToLastCallBack {
        void onScrollToLast(Integer pos);
    }

    public TopicAdapter() {}

    public void setNickName(String nickName){
        this.nickName = nickName;
        notifyDataSetChanged();
    }

    public void setDatas(List<InformationCommentBean> list) {
        if (list == null) {
            return;
        }
        mData.clear();
        for (int i = 0; i < list.size(); i++) {
            mData.add(list.get(i));
        }
        notifyDataSetChanged();
    }

    public void clearDatas() {
        mData.clear();
        notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_topic, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        InformationCommentBean item = getItem(position);
        holder.bindView(item, convertView, position);
        return convertView;

    }

    class ViewHolder {
        CircleImageView userIv;
        TextView userNameTv;
        TextView announceTimeTv;
        TextView contentTv;

        public ViewHolder(View convertView) {
            userIv = (CircleImageView) convertView.findViewById(R.id.header_img);
            userNameTv = (TextView) convertView.findViewById(R.id.user_name);
            announceTimeTv = (TextView) convertView.findViewById(R.id.announce_time);
            contentTv = (TextView) convertView.findViewById(R.id.content);
        }

        public void bindView(final InformationCommentBean item, final View convertView, int position) {
            ImageUtil.display(ConfigUtil.baseImageUserUrl + item.getUserAvatar(), userIv, R.mipmap.img_head);
            ViewHelper.safelySetText(userNameTv, item.getNickname());
            ViewHelper.safelySetText(announceTimeTv, AndroidUtil.splitDateNew(item.getModifyTime()));
            if (!TextUtils.isEmpty(item.getReplyNickname())) {
                contentTv.setText(Html.fromHtml("<font color='#86A2B0'>" +"回复"+ "</font>" + "<font color='#979899'>" + item.getReplyNickname() + "</font>" + ": " + item.getCommentContent()));
            } else {
                ViewHelper.safelySetText(contentTv, item.getCommentContent());
            }
            //头像点击
            userIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnIconClickListener !=null){
                        mOnIconClickListener.clickIcon(item.getPostmanId());
                    }
                }
            });
        }
    }

}
