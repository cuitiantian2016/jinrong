package com.honglu.future.ui.circle.circledetail;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.util.TimeUtil;
import com.honglu.future.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuaibing on 2017/12/7
 */

public class CircleDetailAdapter extends BaseAdapter{
    private CircleDetailActivity mActivity;
    private List<String> mList;

    public CircleDetailAdapter(CircleDetailActivity activity){
        this.mList = new ArrayList<>();
        this.mActivity = activity;
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

    public void notifyDataChanged(boolean isLoadmore,List<String> list){
        if (isLoadmore){
            if (list !=null && list.size() > 0){
                mList.addAll(list);
                notifyDataSetChanged();
            }
        }else {
             mList.clear();
             if (list !=null && list.size() > 0){
                 mList.addAll(list);
             }
             notifyDataSetChanged();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.item_circle_detail_layout,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == getCount()-1){
            holder.mLine.setVisibility(View.INVISIBLE);
        }else {
            holder.mLine.setVisibility(View.VISIBLE);
        }

        //测试数据
        holder.mCivHead.setImageResource(R.mipmap.img_head);
        holder.mName.setText("wahcc");
        holder.mUserLabel.setText("超级管理员");
        holder.mLZhu.setVisibility(View.VISIBLE);
        holder.mTime.setText(TimeUtil.formatData(TimeUtil.dateFormatHHmm_MMdd,System.currentTimeMillis()));
        holder.mContent.setText(":周末搞啥子.............");

        String huiFuStr = "回复 ";
        String huiFuName = "小牛1232";
        String content = "：有道理，学习了。，社会我哥，人狠，话不多...666666";

        int huiFuStart = 0;
        int huiFuEnd = huiFuStr.length();

        int huiFuNameStart = huiFuEnd;
        int huiFuNameEnd = huiFuEnd + huiFuName.length();

        int contentStart = huiFuNameEnd;
        int contentEnd = huiFuNameEnd + content.length();

        Spannable spannable = new SpannableString(huiFuStr+huiFuName+content);
        spannable.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.color_86A2B0)), huiFuStart, huiFuEnd,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannable.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.color_979899)),huiFuNameStart, huiFuNameEnd,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannable.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.color_333333)), contentStart,contentEnd ,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.mContent.setText(spannable);
        return convertView;
    }

    static class ViewHolder{
        CircleImageView mCivHead;
        TextView mName;
        TextView mUserLabel;
        TextView mLZhu;
        TextView mTime;
        TextView mContent;
        View mLine;

        public ViewHolder(View v){
            mCivHead = (CircleImageView) v.findViewById(R.id.civ_head);
            mName = (TextView) v.findViewById(R.id.tv_name);
            mUserLabel = (TextView) v.findViewById(R.id.tv_user_label);
            mLZhu = (TextView) v.findViewById(R.id.tv_lzhu);
            mTime = (TextView) v.findViewById(R.id.tv_time);
            mContent = (TextView) v.findViewById(R.id.tv_content);
            mLine = v.findViewById(R.id.v_line);
        }
    }
}
