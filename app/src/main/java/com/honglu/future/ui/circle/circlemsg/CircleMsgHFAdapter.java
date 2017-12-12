package com.honglu.future.ui.circle.circlemsg;

import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.config.ConfigUtil;
import com.honglu.future.ui.circle.bean.CircleMsgBean;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.util.TimeUtil;
import com.honglu.future.widget.CircleImageView;
import com.honglu.future.widget.recycler.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 收到的评论
 * Created by zhuaibing on 2017/12/7
 */

public class CircleMsgHFAdapter extends BaseRecyclerAdapter<CircleMsgHFAdapter.ViewHolder, CircleMsgBean> {


    public interface OnHuifuClickListener{
        void onHuifuClick(int position);
    }
    public OnHuifuClickListener mListener;
    public void setOnHuifuClickListener(OnHuifuClickListener listener){
        this.mListener = listener;
    }

    //获取第一个的数据
    public CircleMsgBean getCircleBean(int position){
        return getData() !=null && getData().size() > position ? getData().get(position) : null;
    }

    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_circle_msg_layout, parent, false);
        return new CircleMsgHFAdapter.ViewHolder(view);
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, final int position) {

        ImageUtil.display(ConfigUtil.baseImageUserUrl+ item.beAvatarPic, holder.mCivHead, R.mipmap.img_head);

        //回复人昵称
        setText(holder.mName,item.beNickName);

        //时间
        if (!TextUtils.isEmpty(item.createTime)){
            holder.mTiem.setText(TimeUtil.formatData(TimeUtil.dateFormatHHmm_MMdd,Long.parseLong(item.createTime)));
        }else {
            holder.mTiem.setText("");
        }

        //回复内容
        setText(holder.mHuifuContent,item.replyContent);

        //内容
        setText(holder.mContent,item.content);

        //回复按钮
        holder.mHuifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (mListener !=null){
                  mListener.onHuifuClick(position);
              }
            }
        });

        //回复详情 跳转帖子详情
        holder.mHuifuDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.civ_head)
        CircleImageView mCivHead;
        @BindView(R.id.tv_name)
        TextView mName;
        @BindView(R.id.tv_tiem)
        TextView mTiem;
        @BindView(R.id.tv_huifu_content)
        TextView mHuifuContent;
        @BindView(R.id.tv_content)
        TextView mContent;
        @BindView(R.id.tv_huifu)
        TextView mHuifu;
        @BindView(R.id.tv_huifu_detail)
        TextView mHuifuDetail;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private void setText(TextView view,String text){
      if (!TextUtils.isEmpty(text)){
          view.setText(text);
      }else {
          view.setText("");
      }
    }

    private Spannable getSpannableContent(String text1,String text2,String text3 ,String text4){
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
}
