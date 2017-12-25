package com.honglu.future.ui.circle.praisesandreward;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.config.Constant;
import com.honglu.future.ui.circle.bean.ArewardListBean;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.TimeUtil;
import com.honglu.future.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuaibing on 2017/12/20
 */

public class ArewardListAdapter extends BaseAdapter{

    private ArewardListFragment mFragment;
    private List<ArewardListBean> mList;
    private String mUserId;

    public ArewardListAdapter(ArewardListFragment fragment){
        this.mFragment = fragment;
        this.mList = new ArrayList<>();
        this.mUserId = SpUtil.getString(Constant.CACHE_TAG_UID);
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

    public List<ArewardListBean> getList(){
        return mList;
    }

    public void notifyDataChanged(boolean isLoadmore,List<ArewardListBean> list){
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
            convertView = LayoutInflater.from(mFragment.getActivity()).inflate(R.layout.item_areward_list_layout,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == getCount() -1){
            holder.mLine.setVisibility(View.INVISIBLE);
        }else {
            holder.mLine.setVisibility(View.VISIBLE);
        }

        final ArewardListBean bean = mList.get(position);

        ImageUtil.display(bean.avatarPic, holder.mCivHead, R.mipmap.img_head);

        holder.mName.setText(bean.nickName);

        holder.mTime.setText(TimeUtil.formatData(TimeUtil.dateFormatYMDHM,bean.createTime/1000));

        holder.mArewardNum.setText(bean.score);

        if (TextUtils.equals(mUserId,bean.userId)){
            holder.mAttention.setVisibility(View.INVISIBLE);
        }else {
            holder.mAttention.setVisibility(View.VISIBLE);
            holder.mAttention.setImageResource(bean.focus ? R.mipmap.already_recommend : R.mipmap.add_recommend);
            //关注

            holder.mAttention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //0 取消关注  1关注
                   String attentionState = bean.focus ? "0" : "1";
                   mFragment.focusHttp(bean.userId,attentionState);
                }
            });
        }
        return convertView;
    }

    public class ViewHolder{
        CircleImageView mCivHead;
        TextView mName;
        TextView mArewardNum;
        TextView mTime;
        ImageView mAttention;
        View mLine;

        public ViewHolder(View v){
            mCivHead = (CircleImageView) v.findViewById(R.id.civ_head);
            mName = (TextView) v.findViewById(R.id.tv_name);
            mArewardNum = (TextView) v.findViewById(R.id.tv_areward_num);
            mTime = (TextView) v.findViewById(R.id.tv_time);
            mAttention = (ImageView) v.findViewById(R.id.iv_attention);
            mLine = v.findViewById(R.id.v_line);
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
        spannable.setSpan(new ForegroundColorSpan(mFragment.getActivity().getResources().getColor(R.color.color_86A2B0)), text1Start, text1End,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannable.setSpan(new ForegroundColorSpan(mFragment.getActivity().getResources().getColor(R.color.color_979899)),text2Start, text2End,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannable.setSpan(new ForegroundColorSpan(mFragment.getActivity().getResources().getColor(R.color.color_333333)), text3Start,text3End ,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    private int getLength(String text){
        return TextUtils.isEmpty(text) ? 0 : text.length();
    }
}
