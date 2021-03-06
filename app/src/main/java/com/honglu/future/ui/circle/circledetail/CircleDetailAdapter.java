package com.honglu.future.ui.circle.circledetail;

import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.ui.circle.bean.CommentBean;
import com.honglu.future.ui.circle.bean.LayCommentListBean;
import com.honglu.future.ui.circle.circlemine.CircleMineActivity;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.util.TextViewUtil;
import com.honglu.future.util.ViewUtil;
import com.honglu.future.widget.AllListView;
import com.honglu.future.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuaibing on 2017/12/7
 */

public class CircleDetailAdapter extends BaseAdapter{
    private CircleDetailActivity mActivity;
    private List<CommentBean> mCommentList;
    private String mPostUserId;
    private String mCommentType;
    private int mWidth;


    public CircleDetailAdapter(CircleDetailActivity activity ,String mPostUserId){
        this.mCommentList = new ArrayList<>();
        this.mPostUserId = mPostUserId;
        this.mActivity = activity;
        this.mCommentType = mActivity.getCommentType();
        this.mWidth = ViewUtil.getScreenWidth(mActivity) - mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_15dp) * 2 - mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_25dp);
    }


    @Override
    public int getCount() {
        return mCommentList !=null ? mCommentList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mCommentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void notifyDataChanged(String mCommentType){
        this.mCommentType = mCommentType;
        mCommentList.clear();
        notifyDataSetChanged();
    }

    public List<CommentBean> getList(){
        return mCommentList;
    }

    public void notifyDataChanged(boolean isLoadmore,String mCommentType,List<CommentBean> list){
        this.mCommentType = mCommentType;
        if (isLoadmore){
            if (list !=null && list.size() > 0){
                mCommentList.addAll(list);
                notifyDataSetChanged();
            }
        }else {
            mCommentList.clear();
            if (list !=null && list.size() > 0){
                mCommentList.addAll(list);
            }
             notifyDataSetChanged();
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.item_circle_detail_layout,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mActivity == null || mActivity.isDestroyed()){
            return convertView;
        }

        if (position == getCount()-1){
            holder.mLine.setVisibility(View.INVISIBLE);
        }else {

            holder.mLine.setVisibility(View.VISIBLE);
        }

        final  CommentBean bean =  mCommentList.get(position);

        //楼主标记
        if (mActivity.COMMENT_AUTH.equals(mCommentType)){
            holder.mLZhu.setVisibility(View.VISIBLE);
        }else {
            if (!TextUtils.isEmpty(bean.replyUserId) && bean.replyUserId.equals(mPostUserId)){
                holder.mLZhu.setVisibility(View.VISIBLE);
            }else {
                holder.mLZhu.setVisibility(View.GONE);
            }
        }

        //时间
        setTextGone(holder.mTime,bean.createTime);

        //管理员
        setTextGone(holder.mUserLabel,bean.userRole);

        if ("2".equals(bean.replyType)){ //1:贴子评论 2:回复贴子评论
            //头像
            ImageUtil.display(bean.avatarPic, holder.mCivHead, R.mipmap.img_head);
            //名字
            setText(holder.mName,bean.nickName);

            String huiFuStr = "回复 ";
            String huiFuName = bean.beReplyNickName;
            String content = String.format(mActivity.getString(R.string.text_maohao),bean.replyContent);
            holder.mContent.setText(getSpannableContent(huiFuStr,huiFuName,content));
        }else {
            //头像
            ImageUtil.display(bean.avatarPic, holder.mCivHead, R.mipmap.img_head);
            //名字
            setText(holder.mName,bean.nickName);
            //内容
            setText(holder.mContent,bean.replyContent);
        }

        int lZhuWidht = getLZhuWidht(holder.mLZhu);
        int userLabelWidth = getUserLabelWidth(bean, holder.mUserLabel);
        int timeWidht = getTimeWidht(bean, holder.mTime);
        int nameWidht = getNameWidht(bean, holder.mName);

        int width = mWidth - lZhuWidht - userLabelWidth - timeWidht;
        if (width > nameWidht){
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.mName.getLayoutParams();
            params.weight = LinearLayout.LayoutParams.WRAP_CONTENT;
            holder.mName.setLayoutParams(params);
        }else {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.mName.getLayoutParams();
            params.weight = width;
            holder.mName.setLayoutParams(params);
        }

        if (bean.layComment !=null && bean.layComment.count > 0){
            CircleDetailItemAdapter mItemAdapter = new CircleDetailItemAdapter(mActivity,bean.layComment.layList);
            holder.mListView.setVisibility(View.VISIBLE);
            holder.mListView.setAdapter(mItemAdapter);
            if (bean.layComment.count > 3){
                holder.mMoreCommentLayout.setVisibility(View.VISIBLE);
                holder.mLayHuiFu.setVisibility(View.VISIBLE);
                holder.mLayHuiFu.setText("共"+bean.layComment.count+"条回复");
            }else {
                holder.mMoreCommentLayout.setVisibility(View.VISIBLE);
                holder.mLayHuiFu.setVisibility(View.GONE);
                holder.mLayHuiFu.setText("");
            }
        }else {
            holder.mListView.setAdapter(null);
            holder.mMoreCommentLayout.setVisibility(View.GONE);
            holder.mListView.setVisibility(View.GONE);
            holder.mLayHuiFu.setText("");
            holder.mLayHuiFu.setVisibility(View.GONE);
        }


        holder.mCivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, CircleMineActivity.class);
                intent.putExtra("userId",bean.replyUserId);
                intent.putExtra("imgHead",bean.avatarPic);
                intent.putExtra("nickName",bean.nickName);
                mActivity.startActivity(intent);
            }
        });

        holder.mLlRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onItemClick(bean.nickName,bean.replyUserId,String.valueOf(bean.circleReplyId),String.valueOf(bean.circleReplyId));
            }
        });

        holder.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              LayCommentListBean layCommentListBean = (LayCommentListBean) parent.getItemAtPosition(position);
              mActivity.onItemClick(layCommentListBean.nickName,layCommentListBean.replyUserId,String.valueOf(bean.circleReplyId),layCommentListBean.circleReplyId);
            }
        });

        //查看更多回复
        holder.mLayHuiFu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              mActivity.startMoreCommentActivity(bean);
            }
        });

        return convertView;
    }

    static class ViewHolder{
        View mLlRootView;
        CircleImageView mCivHead;
        TextView mName;
        TextView mUserLabel;
        TextView mLZhu;
        TextView mTime;
        TextView mContent;
        AllListView mListView;
        TextView mLayHuiFu;
        View mLine;
        View mMoreCommentLayout;

        public ViewHolder(View v){
            mLlRootView = v.findViewById(R.id.ll_rootView);
            mCivHead = (CircleImageView) v.findViewById(R.id.civ_head);
            mName = (TextView) v.findViewById(R.id.tv_name);
            mUserLabel = (TextView) v.findViewById(R.id.tv_user_label);
            mLZhu = (TextView) v.findViewById(R.id.tv_lzhu);
            mTime = (TextView) v.findViewById(R.id.tv_time);
            mContent = (TextView) v.findViewById(R.id.tv_content);
            mLine = v.findViewById(R.id.v_line);
            mListView = (AllListView) v.findViewById(R.id.lv_listView);
            mLayHuiFu = (TextView) v.findViewById(R.id.tv_layHuiFu);
            mMoreCommentLayout = v.findViewById(R.id.ll_morecomment);
        }
    }


    private Spannable getSpannableContent(String huiFuStr ,String huiFuName ,String content){
        int huiFuStart = 0;
        int huiFuEnd = getLength(huiFuStr);

        int huiFuNameStart = huiFuEnd;
        int huiFuNameEnd = huiFuEnd + getLength(huiFuName);

        int contentStart = huiFuNameEnd;
        int contentEnd = huiFuNameEnd + getLength(content);

        Spannable spannable = new SpannableString(huiFuStr+huiFuName+content);
        spannable.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.color_86A2B0)), huiFuStart, huiFuEnd,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannable.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.color_979899)),huiFuNameStart, huiFuNameEnd,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannable.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.color_333333)), contentStart,contentEnd ,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    private void setText(TextView view , String text){
        if (!TextUtils.isEmpty(text)){
            view.setText(text);
        }else {
            view.setText("");
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
    private int getLength(String text){
        return TextUtils.isEmpty(text) ? 0 : text.length();
    }

    //楼主所占宽度
    private int getLZhuWidht(TextView mLZhu){
        int lZhuWidth = mLZhu.getVisibility()== View.VISIBLE ? (int) (TextViewUtil.getTextWidth(mLZhu.getTextSize(),"楼主")
                + mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_18dp)) : 0;
        return lZhuWidth;
    }

    //角色所占宽度
    private int getUserLabelWidth(CommentBean bean,TextView mUserLabel){
        int userLabelWidth = !TextUtils.isEmpty(bean.userRole) ?(int) (TextViewUtil.getTextWidth(mUserLabel.getTextSize(),bean.userRole)
                + mActivity.getResources().getDimension(R.dimen.dimen_7dp)) : 0;
        return userLabelWidth;
    }

    //名字所占宽度
    private int getNameWidht(CommentBean bean,TextView mName){
        int nameWidht = !TextUtils.isEmpty(bean.nickName) ? (int) (TextViewUtil.getTextWidth(mName.getTextSize(),bean.nickName)
                + mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_10dp)) : 0;
        return nameWidht;
    }

    private int getTimeWidht(CommentBean bean,TextView mTime){
       int timeWidht = !TextUtils.isEmpty(bean.createTime) ? (int) (TextViewUtil.getTextWidth(mTime.getTextSize(),bean.createTime)
             + mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_7dp)) : 0;
       return timeWidht;
    }
}
