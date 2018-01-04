package com.honglu.future.ui.circle.morecomment;

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
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.ui.circle.bean.CommentBean;
import com.honglu.future.ui.circle.bean.CommentBosAllBean;
import com.honglu.future.ui.circle.circlemine.CircleMineActivity;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuaibing on 2018/1/3
 */

public class MoreCommentAdapter extends BaseAdapter{
   private Context mContext;
   private List<CommentBosAllBean> mList;
   private CommentBosAllBean mAllBean;

   public MoreCommentAdapter(Context context, CommentBosAllBean allBean){
       this.mContext = context;
       this.mList = new ArrayList<>();
       this.mAllBean = allBean;
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


    public void notifyDataChanged(boolean isLoadmore, List<CommentBosAllBean> list){
        if (isLoadmore){
            if (list !=null && list.size() > 0){
                mList.addAll(list);
                notifyDataSetChanged();
            }
        }else {
            mList.clear();
            if (list !=null && list.size() > 0){
                if (mAllBean !=null){
                    list.add(0,mAllBean);
                }
                mList.addAll(list);
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_more_comment_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

       final CommentBosAllBean listBean = mList.get(position);

        ImageUtil.display(listBean.avatarPic, holder.mCivHead, R.mipmap.img_head);

        setText(holder.mName,listBean.nickName);

        setText(holder.mTime,listBean.createTime);

        //1 评论   2回复
        if (TextUtils.equals(listBean.pType,listBean.replyUserId)){
            setText(holder.mContent,listBean.replyContent);
        }else {
            Spannable spannableContent = getSpannableContent("回复 ", listBean.beReplyNickName, " :" + listBean.replyContent);
            holder.mContent.setText(spannableContent);
        }

        holder.mCivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DeviceUtils.isFastDoubleClick()){
                    return;
                }
                Intent intent = new Intent(mContext, CircleMineActivity.class);
                intent.putExtra("userId", listBean.replyUserId);
                intent.putExtra("imgHead", listBean.avatarPic);
                intent.putExtra("nickName", listBean.nickName);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    static class ViewHolder{
        CircleImageView mCivHead;
        TextView mName;
        TextView mTime;
        TextView mContent;

      public ViewHolder(View v){
          mCivHead = (CircleImageView) v.findViewById(R.id.civ_head);
          mName = (TextView) v.findViewById(R.id.tv_name);
          mTime = (TextView) v.findViewById(R.id.tv_time);
          mContent = (TextView) v.findViewById(R.id.tv_content);
      }
    }

    private void setText(TextView view , String text){
        if (!TextUtils.isEmpty(text)){
            view.setText(text);
        }else {
            view.setText("");
        }
    }

    private int getLength(String text){
        return TextUtils.isEmpty(text) ? 0 : text.length();
    }

    private Spannable getSpannableContent(String text1 , String text2 , String text3){
        int text1Start = 0;
        int text1End = getLength(text1);

        int text2Start = text1End;
        int text2End = text1End + getLength(text2);

        int text3Start = text2End;
        int text3End = text2End + getLength(text3);

        Spannable spannable = new SpannableString(text1+text2+text3);
        spannable.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_86A2B0)), text1Start, text1End,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannable.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_979899)),text2Start, text2End,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannable.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_333333)), text3Start,text3End ,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }
}
