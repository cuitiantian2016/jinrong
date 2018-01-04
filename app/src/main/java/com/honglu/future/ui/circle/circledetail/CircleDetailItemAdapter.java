package com.honglu.future.ui.circle.circledetail;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.ui.circle.bean.CommentBean;

import java.util.List;

/**
 * Created by zhuaibing on 2018/1/3
 */

public class CircleDetailItemAdapter extends BaseAdapter{
    private CircleDetailActivity mActivity;
    private List<CommentBean.LayCommentListBean> mList;

    public CircleDetailItemAdapter(CircleDetailActivity activity,List<CommentBean.LayCommentListBean> list){
      this.mActivity = activity;
      this.mList = list;
    }

    @Override
    public int getCount() {
        return mList !=null ? mList.size() : 0;
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
        if (convertView == null){
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.item_circle_detail_item_layout,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        CommentBean.LayCommentListBean bean = mList.get(position);

        Spannable spannableContent = getSpannableContent(bean.nickName, "回复", bean.beReplyNickName);
        holder.mHintText.setText(spannableContent);

        setTextGone(holder.mContent,bean.replyContent);


        return convertView;
    }


    static class ViewHolder{
         TextView mHintText;
         TextView mContent;
        public ViewHolder(View v){
             mHintText = (TextView) v.findViewById(R.id.tv_hintText);
             mContent = (TextView) v.findViewById(R.id.tv_content);
        }
    }


    private void setTextGone(TextView view , String text){
        if (!TextUtils.isEmpty(text)){
            view.setVisibility(View.VISIBLE);
            view.setText(text);
        }else {
            view.setVisibility(View.GONE);
        }
    }

    private Spannable getSpannableContent(String text1 , String text2 , String text3){
        int text1Start = 0;
        int text1End = getLength(text1);

        int text2Start = text1End;
        int text2End = text1End + getLength(text2);

        int text3Start = text2End;
        int text3End = text2End + getLength(text3);

        Spannable spannable = new SpannableString(text1+text2+text3);
        spannable.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.color_86A2B0)), text1Start, text1End,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannable.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.color_979899)),text2Start, text2End,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannable.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.color_333333)), text3Start,text3End ,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    private int getLength(String text){
        return TextUtils.isEmpty(text) ? 0 : text.length();
    }
}
