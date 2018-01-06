package com.honglu.future.ui.market.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.ui.market.bean.MarketTabBean;

import java.util.List;

/**
 * Created by zhuaibing on 2018/1/5
 */

public class MarketTabAdapter extends RecyclerView.Adapter<MarketTabAdapter.MarketViewHolder>{
    private Context mContext;
    private String mType;
    private List<MarketTabBean> mList;
    public MarketTabAdapter(List<MarketTabBean> list,String type ,Context mContext){
      this.mList = list;
      this.mType = type;
      this.mContext = mContext;
    }

    public void updateTabSelection(String type){
         this.mType = type;
         notifyDataSetChanged();
    }

    public void notifyDataSetChanged(List<MarketTabBean> list,String mType){
        this.mType = mType;
        this.mList = list;
        this.notifyDataSetChanged();
    }

    @Override
    public MarketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MarketViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_market_horizontal_tab,parent,false));
    }

    @Override
    public int getItemCount() {
        return mList !=null ? mList.size() : 0;
    }

    @Override
    public void onBindViewHolder(MarketViewHolder holder,final int position) {
        final MarketTabBean tabBean = mList.get(position);
        holder.mTitle.setText(tabBean.getTitle());

        if (TextUtils.equals(tabBean.getType(),mType)){
             holder.mTitle.setBackgroundResource(R.drawable.shape_market_bg);
             holder.mTitle.setTextColor(mContext.getResources().getColor(R.color.color_333333));
        }else {
            holder.mTitle.setBackgroundResource(0);
            holder.mTitle.setTextColor(mContext.getResources().getColor(R.color.color_A4A5A6));
        }

        holder.mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener !=null){
                    listener.onItemClick(position,tabBean.getType());
                }
            }
        });
    }

    public  class MarketViewHolder extends RecyclerView.ViewHolder{
        TextView mTitle;
        View mRootView;
        public MarketViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.tv_title);
            mRootView = itemView.findViewById(R.id.ll_rootView);
        }
    }

    public OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public interface OnItemClickListener{
        void onItemClick(int position,String type);
    }
}
