package com.honglu.future.ui.live.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
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
    public void mOnBindViewHolder(MainPointAdapter.ViewHolder holder, final int position) {
        holder.mTvContent.setText(item.messageDescribe);
        //设置字体大小


    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_content)
        TextView mTvContent;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
