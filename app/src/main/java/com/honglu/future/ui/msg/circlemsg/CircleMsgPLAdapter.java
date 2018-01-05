package com.honglu.future.ui.msg.circlemsg;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.config.ConfigUtil;
import com.honglu.future.ui.msg.bean.CircleMsgBean;
import com.honglu.future.ui.circle.circledetail.CircleDetailActivity;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.util.TimeUtil;
import com.honglu.future.widget.CircleImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhuaibing on 2017/12/7
 */

public class CircleMsgPLAdapter extends BaseAdapter{

    private Context mContext;
    private List<CircleMsgBean> mList;
    private String mYear;

    public CircleMsgPLAdapter(Context context){
        this.mContext = context;
        this.mList = new ArrayList<>();
        this.mYear =  getYear();;
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

    public interface OnHuifuClickListener{
        void onHuifuClick(int position);
    }
    public OnHuifuClickListener mListener;
    public void setOnHuifuClickListener(OnHuifuClickListener listener){
        this.mListener = listener;
    }

    public List<CircleMsgBean> getData(){
        return mList;
    }

    //获取第一个的数据
    public CircleMsgBean getCircleBean(int position){
        return getData() !=null && getData().size() > position ? getData().get(position) : null;
    }

    public void clearData(){
        if (mList !=null){
            mList.clear();
            notifyDataSetChanged();
        }
    }

    public void addData(List<CircleMsgBean> list){
        if (list !=null){
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        CircleMsgHFAdapter.ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_circle_msg_layout,null);
            holder = new CircleMsgHFAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (CircleMsgHFAdapter.ViewHolder) convertView.getTag();
        }
        final CircleMsgBean circleMsgBean = mList.get(position);
        ImageUtil.display(ConfigUtil.baseImageUserUrl+ circleMsgBean.avatarPic, holder.mCivHead, R.mipmap.img_head);

        //回复人昵称
        setText(holder.mName,circleMsgBean.nickName);

        //时间
        if (!TextUtils.isEmpty(circleMsgBean.createTime)){
            long time = Long.parseLong(circleMsgBean.createTime)/1000;
            String year = TimeUtil.formatData(TimeUtil.dateFormatY,time);
            if (!TextUtils.isEmpty(year) && year.equals(mYear)){
                holder.mTiem.setText(TimeUtil.formatData(TimeUtil.dateFormatMMdd_HHmm,time));
            }else {
                holder.mTiem.setText(TimeUtil.formatData(TimeUtil.dateFormatYMDHM,Long.parseLong(circleMsgBean.createTime)/1000));
            }

        }else {
            holder.mTiem.setText("");
        }

        //评论内容
        setText(holder.mReplyNameText,circleMsgBean.replyContent);
        holder.mCommentNameText.setVisibility(View.GONE);

        //发帖人name
        setText(holder.mCircleName,circleMsgBean.postNickName);

        //发帖内容
        setText(holder.mCircleContent,circleMsgBean.content);


        if (TextUtils.isEmpty(circleMsgBean.picOne)){
            holder.mCircleImg.setVisibility(View.GONE);
        }else {
            holder.mCircleImg.setVisibility(View.VISIBLE);
            ImageUtil.display(circleMsgBean.picOne, holder.mCircleImg, R.mipmap.img_head);
        }

        //回复按钮
        holder.mReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener !=null){
                    mListener.onHuifuClick(position);
                }
            }
        });
        return convertView;
    }




    static class ViewHolder {
        CircleImageView mCivHead;
        TextView mName;
        TextView mTiem;
        TextView mReply;
        TextView mReplyNameText;
        TextView mCommentNameText;
        ImageView mCircleImg;
        TextView mCircleName;
        TextView mCircleContent;

        ViewHolder(View view) {
            mCivHead = (CircleImageView) view.findViewById(R.id.civ_head);
            mName = (TextView) view.findViewById(R.id.tv_name);
            mTiem = (TextView) view.findViewById(R.id.tv_tiem);
            mReply = (TextView) view.findViewById(R.id.tv_reply);
            mReplyNameText = (TextView) view.findViewById(R.id.tv_reply_name_text);
            mCommentNameText = (TextView) view.findViewById(R.id.tv_comment_name_text);
            mCircleImg = (ImageView) view.findViewById(R.id.iv_circle_img);
            mCircleName = (TextView) view.findViewById(R.id.tv_circle_name);
            mCircleContent = (TextView) view.findViewById(R.id.tv_circle_content);
        }
    }

    private void setText(TextView view,String text){
        if (!TextUtils.isEmpty(text)){
            view.setText(text);
        }else {
            view.setText("");
        }
    }

    private Spannable getSpannableContent(String text1, String text2, String text3 , String text4){
        int text1Start = 0;
        int text1End = getLength(text1);

        int text2Start = text1End;
        int text2End = text2Start + getLength(text2);

        int text3Start = text2End;
        int text3End = text3Start + getLength(text3);

        int text4Start = text3End;
        int text4End = text4Start + getLength(text4);

        Spannable spannable = new SpannableString(text1+text2+text3+text4);
        spannable.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_979899)), text1Start, text1End,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannable.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_333333)),text2Start, text2End,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannable.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_979899)), text3Start,text3End ,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannable.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_333333)), text4Start,text4End ,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    private int getLength(String text){
        return TextUtils.isEmpty(text) ? 0 : text.length();
    }

    public String getYear() {
        Calendar c = Calendar.getInstance();
        return String.valueOf(c.get(Calendar.YEAR));
    }
}
