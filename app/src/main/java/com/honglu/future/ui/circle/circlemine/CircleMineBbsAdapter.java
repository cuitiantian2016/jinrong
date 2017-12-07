package com.honglu.future.ui.circle.circlemine;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.ui.circle.bean.CircleBbsBean;
import com.honglu.future.widget.recycler.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zq on 2017/12/7.
 */

public class CircleMineBbsAdapter extends BaseRecyclerAdapter<CircleMineBbsAdapter.ViewHolder, CircleBbsBean> {
    @Override
    public CircleMineBbsAdapter.ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_circle_bbs, parent, false);
        return new CircleMineBbsAdapter.ViewHolder(view);
    }

    @Override
    public void mOnBindViewHolder(CircleMineBbsAdapter.ViewHolder holder, final int position) {
        holder.mTvBbsTitle.setText(item.title);
        holder.mTvBbsDate.setText(item.date);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/12/7 跳转帖子详情
            }
        });
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.bbs_title)
        TextView mTvBbsTitle;
        @BindView(R.id.bbs_date)
        TextView mTvBbsDate;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
