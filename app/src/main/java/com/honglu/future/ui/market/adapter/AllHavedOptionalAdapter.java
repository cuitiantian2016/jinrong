package com.honglu.future.ui.market.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.ui.market.bean.MarketnalysisBean;
import com.honglu.future.ui.market.bean.QuotationDataListBean;
import com.honglu.future.widget.recycler.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hc on 2017/10/26.
 */

public class AllHavedOptionalAdapter extends BaseRecyclerAdapter<AllHavedOptionalAdapter.ViewHolder, QuotationDataListBean> {
    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.layout_optional_quotes_item, parent, false));
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, int position) {
        holder.mName.setText(item.getName());
    }

    public void addItemData(QuotationDataListBean bean){
        getData().add(bean);
        notifyDataSetChanged();
    }

    public void removeItemData(int position){
        getData().remove(position);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView mName;
        @BindView(R.id.ll_layout)
        LinearLayout mLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
