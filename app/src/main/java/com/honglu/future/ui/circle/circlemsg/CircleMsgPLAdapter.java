package com.honglu.future.ui.circle.circlemsg;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
 * Created by zhuaibing on 2017/12/7
 */

public class CircleMsgPLAdapter extends BaseRecyclerAdapter<CircleMsgPLAdapter.ViewHolder, CircleMsgBean> {


    public interface OnHuifuClickListener{
        void onHuifuClick(String name,int replyUserId ,int circleId);
    }
    public OnHuifuClickListener mListener;
    public void setOnHuifuClickListener(OnHuifuClickListener listener){
        this.mListener = listener;
    }

    //获取第一个的数据
    public CircleMsgBean getCircleBean(){
        return getData() !=null && getData().size() > 0 ? getData().get(0) : null;
    }

    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_circle_msg_layout, parent, false);
        return new CircleMsgPLAdapter.ViewHolder(view);
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, int position) {
        ImageUtil.display(ConfigUtil.baseImageUserUrl+ item.avatarPic, holder.mCivHead, R.mipmap.img_head);

        //回复人昵称
        setText(holder.mName,item.nickName);

        //时间
        if (!TextUtils.isEmpty(item.createTime)){
            holder.mTiem.setText(TimeUtil.formatData(TimeUtil.dateFormatHHmm_MMdd,Long.parseLong(item.createTime)));
        }else {
            holder.mTiem.setText("");
        }

        //回复内存
        setText(holder.mHuifuContent,item.replyContent);

        //内容
        setText(holder.mContent,item.content);

        //回复按钮
        holder.mHuifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener !=null){
                    mListener.onHuifuClick(item.nickName,item.replyUserId ,item.circleId);
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
}
