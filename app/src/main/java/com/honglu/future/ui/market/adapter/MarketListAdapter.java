package com.honglu.future.ui.market.adapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.ui.market.bean.MarketnalysisBean;
import com.honglu.future.ui.market.fragment.MarketFragment;
import com.honglu.future.ui.market.fragment.MarketItemFragment;
import com.honglu.future.ui.trade.kchart.KLineMarketActivity;
import com.honglu.future.widget.DinTextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hc on 2017/10/25.
 */

public class MarketListAdapter extends BaseAdapter {

    private MarketItemFragment mFragment;
    private Context mContext;
    private LayoutInflater mInflater;
    private List<MarketnalysisBean.ListBean.QuotationDataListBean> mList;
    private String mTabSelectType;
    private AnimationDrawable redAnimation;
    private AnimationDrawable greenAnimation;

    public MarketListAdapter(MarketItemFragment fragment, String tabSelectType ,List<MarketnalysisBean.ListBean.QuotationDataListBean> list) {
        this.mContext = fragment.getActivity();
        this.mInflater =LayoutInflater.from(fragment.getActivity());
        this.mList = list;
        this.mFragment = fragment;
        this.mTabSelectType = tabSelectType;
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<MarketnalysisBean.ListBean.QuotationDataListBean> getData(){
        return mList;
    }


    public void notifyDataChanged(boolean isClear,List<MarketnalysisBean.ListBean.QuotationDataListBean> list){
       if (isClear && mList.size() > 0){
           mList.clear();
       }
       if (list !=null && list.size() > 0){
           mList = list;
       }
       notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_marketlist_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MarketnalysisBean.ListBean.QuotationDataListBean listBean = mList.get(position);

        holder.mTvContractName.setText(listBean.getName());
        holder.mTvLatestPrice.setText(listBean.getLastPrice());
        holder.mTvQuoteChange.setText(listBean.getChg());
        holder.mTvHavedPositions.setText(listBean.getOpenInterest());

        if (listBean.getChg().indexOf("-") != -1) {
            holder.mTvLatestPrice.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
            holder.mTvQuoteChange.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
        } else {
            holder.mTvLatestPrice.setTextColor(mContext.getResources().getColor(R.color.color_FA455B));
            holder.mTvQuoteChange.setTextColor(mContext.getResources().getColor(R.color.color_FA455B));
        }

        if (mTabSelectType.equals(MarketFragment.ZXHQ_TYPE)) {
            holder.mAddDelIc.setImageResource(R.mipmap.ic_market_optional_delete);
        } else {
            if ("0".equals(listBean.getIcAdd())) {
                holder.mAddDelIc.setImageResource(R.mipmap.ic_market_optional_add);
            } else {
                holder.mAddDelIc.setImageResource(R.mipmap.ic_market_optional_delete);
            }
        }

        final MarketnalysisBean.ListBean.QuotationDataListBean mBean = listBean;
        holder.mAddDelIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //img true 没添加  false 已添加
                if (MarketFragment.ZXHQ_TYPE.equals(mTabSelectType)) {
                    mFragment.refresh("1", mBean);
                } else {
                    mFragment.refresh(mBean.getIcAdd(), mBean);
                }
            }
        });

        holder.mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, KLineMarketActivity.class);
                intent.putExtra("excode", mBean.getExchangeID());
                intent.putExtra("code", mBean.getInstrumentID());
                intent.putExtra("isClosed", "1");
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    public int getUpdatePosition(String instrumentID) {
        int mPosition = -1;
        if (mList != null && mList.size() > 0) {
            for (int i = 0; i < mList.size(); i++) {
                MarketnalysisBean.ListBean.QuotationDataListBean listBean = mList.get(i);
                if (listBean.getInstrumentID().equals(instrumentID)) {
                    mPosition = i;
                    break;
                }
            }
        }
        return mPosition;
    }

    public void updateItemView(TextView mTvQuoteChange, TextView mTvLatestPrice, TextView mTvHavedPositions, View mColorView
            , String mOldChg, String mOldLastPrice, String mOldopenInterest, String mChg, String mLastPrice, String openInterest) {
        double oldChe = string2Double(mOldChg);
        double oldLastPrice = string2Double(mOldLastPrice);
        double che = string2Double(mChg);
        double lastPrice = string2Double(mLastPrice);
        mTvQuoteChange.setText(mChg);
        mTvLatestPrice.setText(mLastPrice);
        mTvHavedPositions.setText(openInterest);

        if (mChg.indexOf("-") != -1) {
            mTvLatestPrice.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
            mTvQuoteChange.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
        } else {
            mTvLatestPrice.setTextColor(mContext.getResources().getColor(R.color.color_FA455B));
            mTvQuoteChange.setTextColor(mContext.getResources().getColor(R.color.color_FA455B));
        }

        double lastPriceValue = lastPrice - oldLastPrice;
        if (lastPriceValue > 0) {
            mColorView.setBackgroundResource(R.drawable.shansuo_red);
            redAnimation = (AnimationDrawable) mColorView.getBackground();
            redAnimation.stop();
            redAnimation.start();
        } else if (lastPriceValue < 0) {
            mColorView.setBackgroundResource(R.drawable.shansuo_green);
            greenAnimation = (AnimationDrawable) mColorView.getBackground();
            greenAnimation.stop();
            greenAnimation.start();
        }
    }


    static class ViewHolder {
        View mRootView;
        TextView mTvContractName;//合约名称
        TextView mTvLatestPrice;//最新价
        View mColorView;
        ImageView mAddDelIc;
        TextView mTvQuoteChange;//涨幅量
        TextView mTvHavedPositions;//持仓量

        ViewHolder(View view) {
            mRootView = view.findViewById(R.id.rootView);
            mTvContractName = (TextView) view.findViewById(R.id.text_contract_name);
            mTvLatestPrice = (TextView) view.findViewById(R.id.text_latest_price);
            mColorView = view.findViewById(R.id.v_color_shansuo);
            mAddDelIc = (ImageView) view.findViewById(R.id.iv_adddel);
            mTvQuoteChange = (TextView) view.findViewById(R.id.text_quote_change);
            mTvHavedPositions = (TextView) view.findViewById(R.id.text_haved_positions);
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
