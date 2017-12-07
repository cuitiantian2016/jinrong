package com.honglu.future.ui.circle.circledetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.honglu.future.R;
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

        return convertView;
    }

    static class ViewHolder{
        CircleImageView mCivHead;
        TextView mName;
        TextView mUserLabel;
        TextView mTime;
        TextView mContent;
        View mLine;

        public ViewHolder(View v){
            mCivHead = (CircleImageView) v.findViewById(R.id.civ_head);
            mName = (TextView) v.findViewById(R.id.tv_name);
            mUserLabel = (TextView) v.findViewById(R.id.tv_user_label);
            mTime = (TextView) v.findViewById(R.id.tv_time);
            mContent = (TextView) v.findViewById(R.id.tv_content);
            mLine = v.findViewById(R.id.v_line);
        }
    }
}
