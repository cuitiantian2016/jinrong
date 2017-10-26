package com.honglu.future.ui.market.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.ui.market.bean.AllClassificationBean;
import com.honglu.future.widget.recycler.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hc on 2017/10/25.
 */

public class MarketClassificationAdapter extends BaseRecyclerAdapter<MarketClassificationAdapter.ViewHolder, AllClassificationBean> {

    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.layout_quotes_classification_item, parent, false));
    }
    @Override
    public void mOnBindViewHolder(ViewHolder holder, int position) {
        Log.e("xiaoniu","item.getClassificationName()=="+item.getClassificationName());
        holder.line_onclick.setVisibility(item.getOnClick() ? View.VISIBLE : View.INVISIBLE);
        holder.mTvQuotesName.setTextColor(mContext.getResources()
                .getColor(item.getOnClick()?R.color.color_008EFF:R.color.color_333333));
        holder.mTvQuotesName.setText(item.getClassificationName());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_quotes_name)
        TextView mTvQuotesName;//行情分类名称
        @BindView(R.id.line_onclick)
        View line_onclick;//被选中时显示

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
