package com.honglu.future.dialog.klineposition;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.honglu.future.R;
import com.honglu.future.ui.trade.bean.HoldPositionBean;
import com.honglu.future.ui.trade.bean.ProductListBean;
import com.honglu.future.widget.recycler.BaseRecyclerAdapter;
import java.util.List;

/**
 * Created by zq on 2017/11/2.
 */

public class KLinePositionDialogAdapter extends BaseRecyclerAdapter<KLinePositionDialogAdapter.ViewHolder, HoldPositionBean> {
    private int mPosition = -1;
    private ProductListBean mProductListBean;
    private KLinePositionDialogPresenter mPresenter;

    public void setProductListBean(ProductListBean bean){
        this.mProductListBean = bean;
        notifyDataSetChanged();
    }

    public void clearPosition(){
        mPosition = -1;
    }

    public void notifyDataChanged(List<HoldPositionBean> list){
        clearPosition();
        getData().clear();
        if (list !=null){
            addData(list);
        }
    }

    public KLinePositionDialogAdapter(KLinePositionDialogPresenter mPresenter){
          this.mPresenter = mPresenter;
    }

    @Override
    public KLinePositionDialogAdapter.ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_kline_position_dialog, parent, false);
        return new KLinePositionDialogAdapter.ViewHolder(view);
    }

    @Override
    public void mOnBindViewHolder(KLinePositionDialogAdapter.ViewHolder holder, int position) {
        if (position == getItemCount()-1){
            holder.mLine.setVisibility(View.INVISIBLE);
        }else {
            holder.mLine.setVisibility(View.VISIBLE);
        }

        //1 跌  2涨
        if (item.getType() == 1){
            holder.mBuyNum.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
            holder.mBuyNum.setText("买跌"+item.getPosition()+"手");

        }else {
            holder.mBuyNum.setTextColor(mContext.getResources().getColor(R.color.color_FB4F4F));
            holder.mBuyNum.setText("买涨"+item.getPosition()+"手");
        }
        //持仓均价
        holder.mPriced.setText(item.getHoldAvgPrice());

        //持仓盈亏
        holder.mProfitLoss.setText(item.getTotalProfit());
        if (item.getTotalProfit().indexOf("-") != -1){
            holder.mProfitLoss.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
        }else {
            holder.mProfitLoss.setTextColor(mContext.getResources().getColor(R.color.color_FB4F4F));
        }

        final KLinePositionDialogAdapter.ViewHolder mViewHolder = holder;
        final HoldPositionBean mBean = item;

        if (position == mPosition){
            clearPosition();
            holder.mGouxuan.setSelected(true);
            holder.mGouxuan.setEnabled(false);
            holder.mLayoutContent.setVisibility(View.VISIBLE);
            holder.mEtMaxpc.setEnabled(true);

            //平仓手数 最多
            holder.mMaxpcNum.setText("平仓手数(最多"+item.getPosition()+"手)");
            //最大输入平仓手数
            holder.mEtMaxpc.setText(item.getPosition()+"");
            //设置手数事件
            setPositionListener(mViewHolder,mBean);

            if (mProductListBean !=null){
                //平仓委托价
               if (!TextUtils.isEmpty(mProductListBean.getLastPrice())){
                   holder.mEtPrice.setText(mProductListBean.getLastPrice());
                   setPriceListener(mViewHolder);
               }

               if (!TextUtils.isEmpty(mProductListBean.getLowerLimitPrice()) && !TextUtils.isEmpty(mProductListBean.getUpperLimitPrice())) {
                   holder.mPriceHint.setText("≥" + mProductListBean.getLowerLimitPrice() + " 跌停价 且 ≤" + mProductListBean.getUpperLimitPrice() + "涨停价");
               }

            }

        }else {
            holder.mGouxuan.setSelected(false);
            holder.mGouxuan.setEnabled(true);
            holder.mEtMaxpc.setEnabled(false);
            holder.mLayoutContent.setVisibility(View.GONE);
            final int mZPosition = position;
            holder.mGouxuan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPosition = mZPosition;
                    mPresenter.getProductDetail(mBean.getInstrumentId());
                }
            });
        }
    }

    //手数
    private void setPositionListener(final KLinePositionDialogAdapter.ViewHolder mHolder ,final HoldPositionBean mBean){

        //手数 -
        mHolder.mMaxpcDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int textNum = getText(mHolder.mEtMaxpc);
                if (textNum > 1){
                    textNum -- ;
                    mHolder.mEtMaxpc.setText(textNum+"");
                }
            }
        });

        mHolder.mMaxpcAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int textNum = getText(mHolder.mEtMaxpc);
                if (textNum < mBean.getPosition()){
                    textNum ++ ;
                    mHolder.mEtMaxpc.setText(textNum+"");
                }
            }
        });
    }


    //委托价
    private void setPriceListener(final KLinePositionDialogAdapter.ViewHolder mHolder){

        mHolder.mPriceDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int textNum = getText(mHolder.mEtPrice);
                if (textNum > 1){
                    textNum -- ;
                    mHolder.mEtPrice.setText(textNum+"");
                }
            }
        });

        mHolder.mPriceAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double textNum = getDoubleText(mHolder.mEtPrice);
                double lastPrice = !TextUtils.isEmpty(mProductListBean.getLastPrice()) ? Double.parseDouble(mProductListBean.getLastPrice()) : 0;
                if (textNum < lastPrice){
                    textNum ++ ;
                    mHolder.mEtPrice.setText(textNum+"");
                }
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mGouxuan;
        TextView mBuyNum;
        TextView mPriced;
        TextView mProfitLoss;
        ImageView mPriceDel;
        EditText mEtPrice;
        ImageView mPriceAdd;
        TextView mPriceHint;
        TextView mMaxpcNum;
        ImageView mMaxpcDel;
        EditText mEtMaxpc;
        ImageView mMaxpcAdd;
        TextView mSxprice;
        TextView mYkprice;
        LinearLayout mLayoutContent;
        //ExpandableLayout mExpandable;
        View mLine;

        ViewHolder(View view) {
            super(view);
            mGouxuan = (ImageView) view.findViewById(R.id.iv_gouxuan);
            mBuyNum = (TextView) view.findViewById(R.id.tv_buy_num);
            mPriced = (TextView) view.findViewById(R.id.tv_priced);
            mProfitLoss = (TextView) view.findViewById(R.id.tv_profit_loss);
            mPriceDel = (ImageView) view.findViewById(R.id.iv_price_del);
            mEtPrice = (EditText) view.findViewById(R.id.et_price);
            mPriceAdd = (ImageView) view.findViewById(R.id.iv_price_add);
            mPriceHint = (TextView) view.findViewById(R.id.tv_price_hint);
            mMaxpcNum = (TextView) view.findViewById(R.id.tv_maxpc_num);
            mMaxpcDel = (ImageView) view.findViewById(R.id.iv_maxpc_del);
            mEtMaxpc = (EditText) view.findViewById(R.id.et_maxpc);
            mMaxpcAdd = (ImageView) view.findViewById(R.id.iv_maxpc_add);
            mSxprice = (TextView) view.findViewById(R.id.tv_sxprice);
            mYkprice = (TextView) view.findViewById(R.id.tv_ykprice);
            mLayoutContent = (LinearLayout) view.findViewById(R.id.layout_content);
           // mExpandable = (ExpandableLayout) view.findViewById(R.id.el_expandable);
            mLine = view.findViewById(R.id.v_line);
        }
    }


    private int getText(EditText mText){

       return mText.getText() !=null && !TextUtils.isEmpty(mText.getText().toString()) ? Integer.parseInt(mText.getText().toString()) : 0;
    }

    private double getDoubleText(EditText mText){

        return mText.getText() !=null && !TextUtils.isEmpty(mText.getText().toString()) ? Double.parseDouble(mText.getText().toString()): 0;
    }
}
