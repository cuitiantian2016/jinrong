package com.honglu.future.ui.circle.circlemsg;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.widget.CircleImageView;
import com.honglu.future.widget.recycler.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhuaibing on 2017/12/7
 */

public class CircleMsgPLAdapter extends BaseRecyclerAdapter<CircleMsgPLAdapter.ViewHolder, String> {


    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_circle_msg_layout, parent, false);
        return new CircleMsgPLAdapter.ViewHolder(view);
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, int position) {

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
}
