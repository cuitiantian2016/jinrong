package com.honglu.future.ui.market.adapter;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.ui.market.bean.MarketnalysisBean;
import com.honglu.future.ui.market.fragment.MarketFragment;
import com.honglu.future.ui.market.fragment.MarketItemFragment;
import com.honglu.future.ui.trade.kchart.KLineMarketActivity;
import com.honglu.future.widget.recycler.BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hc on 2017/10/25.
 */

public class MarketListAdapter extends BaseRecyclerAdapter<MarketListAdapter.ViewHolder, MarketnalysisBean.ListBean.QuotationDataListBean> {
    private List<Double> oldPriceList = new ArrayList<>();
    private String mTabSelectType;
    private MarketItemFragment fragment;

    public String getTabSelectType(){

        return  mTabSelectType;
    }

    public void setMarketItemFragment(MarketItemFragment fragment){
        this.fragment =fragment;
    }

    public void setOldPriceList(List<MarketnalysisBean.ListBean.QuotationDataListBean> list){
        if (list !=null && list.size() > 0){
            oldPriceList.clear();
            for (int i = 0 ; i < getData().size() ; i++){
                String lastPrice = getData().get(i).getLastPrice();
                oldPriceList.add(string2Double(lastPrice));
            }
        }
    }
    public void refreshData(String type,List<MarketnalysisBean.ListBean.QuotationDataListBean> list) {
        this.mTabSelectType = type;
        //防止保存的是其他tab 下的数据
        if (list == null){
            list = new ArrayList<>();
        };
        this.data = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.layout_marketlist_item, parent, false));
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, final int position) {
        holder.mTvContractName.setTextSize(14);
        holder.mTvLatestPrice.setTextSize(16);
        holder.mTvQuoteChange.setTextSize(16);
        holder.mTvHavedPositions.setTextSize(16);
        holder.mGreenView.setVisibility(View.GONE);
        holder.mRedView.setVisibility(View.GONE);
        holder.mTvContractName.setText(item.getName());
        holder.mTvLatestPrice.setText(item.getLastPrice());
        holder.mTvQuoteChange.setText(item.getChg());
        holder.mTvHavedPositions.setText(item.getOpenInterest());

        if (item.getChg().indexOf("-") != -1){
            holder.mTvLatestPrice.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
            holder.mTvQuoteChange.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
        }else {
            holder.mTvLatestPrice.setTextColor(mContext.getResources().getColor(R.color.color_FA455B));
            holder.mTvQuoteChange.setTextColor(mContext.getResources().getColor(R.color.color_FA455B));
        }

        if (mTabSelectType.equals(MarketFragment.ZXHQ_TYPE)){
            holder.mAddDelIc.setImageResource(R.mipmap.ic_market_optional_delete);
        }else {
            if ("0".equals(item.getIcAdd())){
                holder.mAddDelIc.setImageResource(R.mipmap.ic_market_optional_add);
            }else {
                holder.mAddDelIc.setImageResource(R.mipmap.ic_market_optional_delete);
            }
        }
        double lastPrice = string2Double(item.getLastPrice());
        double mOldPrice = 0.0;
        try {
            if (oldPriceList !=null && oldPriceList.size() > position)
            mOldPrice = oldPriceList.get(position);
        } catch (Exception e) {
            mOldPrice = 0.0;
        }
        if (mOldPrice != 0.0) {
            if (lastPrice - mOldPrice > 0) {
                holder.mRedView.setVisibility(View.VISIBLE);
                holder.mRedView.setBackgroundResource(R.color.color_FA455B);
                holder.mRedView.setAlpha(0.3F);
                ObjectAnimator alpha = ObjectAnimator.ofFloat(holder.mRedView, "alpha", 0f, 1f, 0f);
                alpha.setDuration(300);
                alpha.setRepeatCount(1);
                alpha.start();
            } else if (lastPrice - mOldPrice < 0) {
                holder.mGreenView.setVisibility(View.VISIBLE);
                holder.mRedView.setBackgroundResource(R.color.color_2CC593);
                holder.mRedView.setAlpha(0.3F);
                ObjectAnimator alpha = ObjectAnimator.ofFloat(holder.mGreenView, "alpha", 0f, 1f, 0f);
                alpha.setDuration(300);
                alpha.setRepeatCount(1);
                alpha.start();
            }
        }
        final MarketnalysisBean.ListBean.QuotationDataListBean mBean = item;
        holder.mAddDelIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //img true 没添加  false 已添加
                if (MarketFragment.ZXHQ_TYPE.equals(mTabSelectType)){
                    fragment.refresh("1",mBean);
                }else {
                    fragment.refresh(mBean.getIcAdd(),mBean);
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, KLineMarketActivity.class);
                intent.putExtra("excode",mBean.getExchangeID());
                intent.putExtra("code",mBean.getInstrumentID());
                intent.putExtra("isClosed","1");
                mContext.startActivity(intent);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_contract_name)
        TextView mTvContractName;//合约名称
        @BindView(R.id.text_latest_price)
        TextView mTvLatestPrice;//最新价
        @BindView(R.id.v_red)
        View mRedView;
        @BindView(R.id.v_green)
        View mGreenView;
        @BindView(R.id.iv_adddel)
        ImageView mAddDelIc;
        @BindView(R.id.text_quote_change)
        TextView mTvQuoteChange;//涨幅量
        @BindView(R.id.text_haved_positions)
        TextView mTvHavedPositions;//持仓量

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    public static double string2Double(String str) {
        if (TextUtils.isEmpty(str)) return 0;
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {

        }
        return 0;
    }
}
