package com.honglu.future.ui.live.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.ui.live.bean.LivePointBean;
import com.honglu.future.widget.recycler.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zq on 2018/1/6.
 */

public class MainPointAdapter extends BaseRecyclerAdapter<MainPointAdapter.ViewHolder, LivePointBean> {


    @Override
    public MainPointAdapter.ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_live_point, parent, false);
        return new MainPointAdapter.ViewHolder(view);
    }

    @Override
    public void mOnBindViewHolder(final MainPointAdapter.ViewHolder holder, final int position) {

        holder.mTvTime.setText(item.createTime);

        if (item.messageTitle.length() > 14) {
            holder.mTvTitle.setText(item.messageTitle.substring(0, 13) + "...");
        } else {
            holder.mTvTitle.setText(item.messageTitle);
        }
        holder.mTvContent.setText(item.messageDescribe);
        holder.mTvContentAll.setText(item.messageDescribe);
        holder.isShow = false;
        if (item.messageDescribe.length() > 100) {
            holder.mTvContent.setVisibility(View.VISIBLE);
            holder.mTvContentAll.setVisibility(View.GONE);
            holder.mIvArrow.setVisibility(View.VISIBLE);
        } else {
            holder.mTvContent.setVisibility(View.GONE);
            holder.mTvContentAll.setVisibility(View.VISIBLE);
            holder.mIvArrow.setVisibility(View.GONE);
        }

        holder.mIvArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!holder.isShow) {
                    holder.isShow = true;
                    holder.mIvArrow.setImageResource(R.mipmap.ic_up);
                    holder.mTvContent.setVisibility(View.GONE);
                    holder.mTvContentAll.setVisibility(View.VISIBLE);
                } else {
                    holder.isShow = false;
                    holder.mIvArrow.setImageResource(R.mipmap.ic_down);
                    holder.mTvContent.setVisibility(View.VISIBLE);
                    holder.mTvContentAll.setVisibility(View.GONE);
                }

            }
        });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_content)
        TextView mTvContent;
        @BindView(R.id.tv_time)
        TextView mTvTime;
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.tv_content_all)
        TextView mTvContentAll;
        @BindView(R.id.iv_arrow)
        ImageView mIvArrow;

        boolean isShow;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
