package com.honglu.future.ui.msg.praisemsg;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.config.ConfigUtil;
import com.honglu.future.ui.circle.circlemine.CircleMineActivity;
import com.honglu.future.ui.msg.bean.PraiseMsgListBean;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.util.TimeUtil;
import com.honglu.future.widget.CircleImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by zhuaibing on 2018/1/5
 */

public class PraiseMsgAdapter extends BaseAdapter{
    private Context mContext;
    private List<PraiseMsgListBean> mList;
    private String mYear;

    public PraiseMsgAdapter(Context context){
        this.mContext = context;
        this.mList = new ArrayList<>();
        this.mYear = getYear();
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

    public void clearData(){
        if (mList !=null){
            mList.clear();
            notifyDataSetChanged();
        }
    }

    public void notifyDataChanged(boolean isLoadmore, List<PraiseMsgListBean> list){
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_praise_msg_layout,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
         final PraiseMsgListBean praiseBean = mList.get(position);

        ImageUtil.display(ConfigUtil.baseImageUserUrl+ praiseBean.avatarPic, holder.mCivHead, R.mipmap.img_head);

        setText(holder.mName,praiseBean.nickName);

        //时间
        long createTime = praiseBean.createTime / 1000;
        String year = TimeUtil.formatData(TimeUtil.dateFormatY,createTime);
        if (!TextUtils.isEmpty(year) && year.equals(mYear)){
            holder.mTiem.setText(TimeUtil.formatData(TimeUtil.dateFormatMMdd_HHmm,createTime));
        }else {
            holder.mTiem.setText(TimeUtil.formatData(TimeUtil.dateFormatYMDHM,createTime));
        }


        //发帖人name
        setText(holder.mCircleName,praiseBean.beNickName);
        //发帖内容
        setText(holder.mCircleContent,praiseBean.content);

        //发帖图片
        if (TextUtils.isEmpty(praiseBean.picOne)){
            holder.mCircleImg.setVisibility(View.GONE);
        }else {
            holder.mCircleImg.setVisibility(View.VISIBLE);
            ImageUtil.display( praiseBean.picOne, holder.mCircleImg, R.mipmap.img_head);
        }


        holder.mCivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DeviceUtils.isFastDoubleClick()){
                    return;
                }
                Intent intent = new Intent(mContext, CircleMineActivity.class);
                intent.putExtra("userId", String.valueOf(praiseBean.replyUserId));
                intent.putExtra("imgHead", ConfigUtil.baseImageUserUrl+ praiseBean.avatarPic);
                intent.putExtra("nickName", praiseBean.nickName);
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }


    static class ViewHolder{
        CircleImageView mCivHead;
        TextView mName;
        TextView mTiem;
        ImageView mCircleImg;
        TextView mCircleName;
        TextView mCircleContent;

        ViewHolder(View view) {
            mCivHead = (CircleImageView) view.findViewById(R.id.civ_head);
            mName = (TextView) view.findViewById(R.id.tv_name);
            mTiem = (TextView) view.findViewById(R.id.tv_tiem);
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

    public String getYear() {
        Calendar c = Calendar.getInstance();
        return String.valueOf(c.get(Calendar.YEAR));
    }
}
