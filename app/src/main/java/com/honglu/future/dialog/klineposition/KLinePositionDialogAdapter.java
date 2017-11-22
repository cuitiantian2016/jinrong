package com.honglu.future.dialog.klineposition;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.config.Constant;
import com.honglu.future.ui.trade.bean.HoldPositionBean;
import com.honglu.future.ui.trade.bean.ProductListBean;
import com.honglu.future.util.TradeUtil;
import com.honglu.future.widget.recycler.BaseRecyclerAdapter;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by zq on 2017/11/2.
 */

public class KLinePositionDialogAdapter extends BaseRecyclerAdapter<KLinePositionDialogAdapter.ViewHolder, HoldPositionBean> {
    private ProductListBean mProductListBean;
    private KLinePositionDialogPresenter mPresenter;
    private KLinePositionDialog mDialog;

    private int mPosition = -1;
    private int mExpcNum = 0; //平仓手数
    private int mMaxCloseTradeNum;//最大平仓手数
    private double mExPrice = 0;//价格
    private double mExLastPrice = 0; //上一次输入的价格

    private double mLowerLimitPrice; //跌停板价
    private double mUpperLimitPrice; //涨停板价
    private KLinePositionDialogAdapter.ViewHolder mViewHolder;

    public KLinePositionDialogAdapter(KLinePositionDialogPresenter mPresenter, KLinePositionDialog mDialog) {
        this.mPresenter = mPresenter;
        this.mDialog = mDialog;
    }

    public void setProductListBean(ProductListBean bean) {
        this.mExpcNum = 0;
        this.mExPrice = 0;
        this.mExLastPrice = 0;
        this.mProductListBean = bean;
        this.mLowerLimitPrice = getDouble(mProductListBean.getLowerLimitPrice());
        this.mUpperLimitPrice = getDouble(mProductListBean.getUpperLimitPrice());
        notifyDataSetChanged();
    }

    /**
     * mpush 刷新对应数据
     *
     * @param lowerLimitPrice
     * @param upperLimitPrice
     */
    public void setMpushRefreshData(String lowerLimitPrice, String upperLimitPrice) {

    }

    //当前展开的 position
    public int getMPosition() {
        return mPosition;
    }

    //重置 position
    public void clearPosition() {
        this.mPosition = -1;
    }

    //手数
    public int getExpcNum() {
        return mExpcNum;
    }

    //价格
    public double getExPrice() {
        return mExPrice;
    }

    //跌停板价
    public double getLowerLimitPrice(){
        return mLowerLimitPrice;
    }

    //涨停板价
    public double getUpperLimitPrice(){
        return mUpperLimitPrice;
    }

    //是否刷新mpush 消息
    public boolean isMpushRefresh() {

        return mProductListBean != null && mPosition != -1;
    }

    //重置数据
    public void resetData() {
        this.mProductListBean = null;
        this.mExpcNum = 0;
        this.mExPrice = 0;
        this.mExLastPrice = 0;
        clearPosition();
    }

    public void notifyDataChanged(List<HoldPositionBean> list) {
        getData().clear();
        if (list != null) {
            addData(list);
        }
    }


    @Override
    public KLinePositionDialogAdapter.ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_kline_position_dialog, parent, false);
        return new KLinePositionDialogAdapter.ViewHolder(view);
    }

    @Override
    public void mOnBindViewHolder(KLinePositionDialogAdapter.ViewHolder holder, int position) {
        if (position == getItemCount() - 1) {
            holder.mLine.setVisibility(View.INVISIBLE);
        } else {
            holder.mLine.setVisibility(View.VISIBLE);
        }

        //1 跌  2涨
        if (item.getType() == Constant.TYPE_BUY_DOWN) {
            holder.mBuyNum.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
            holder.mBuyNum.setText("买跌" + item.getPosition() + "手");

        } else {
            holder.mBuyNum.setTextColor(mContext.getResources().getColor(R.color.color_FB4F4F));
            holder.mBuyNum.setText("买涨" + item.getPosition() + "手");
        }

        //持仓均价
        holder.mPriced.setText(item.getHoldAvgPrice());

        //持仓盈亏
        holder.mProfitLoss.setText(item.getTotalProfit());
        if (item.getTotalProfit().indexOf("-") != -1) {
            holder.mProfitLoss.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
        } else {
            holder.mProfitLoss.setTextColor(mContext.getResources().getColor(R.color.color_FB4F4F));
        }

        mViewHolder = holder;
        final HoldPositionBean mBean = item;

        if (position == mPosition) {
            holder.mGouxuan.setSelected(true);
            holder.mGouxuan.setEnabled(false);
            holder.mLayoutContent.setVisibility(View.VISIBLE);
            holder.mEtMaxpc.setEnabled(false);

            if (mProductListBean !=null) {
                //获取最大平仓手数
                mMaxCloseTradeNum = getMaxCloseTradeNum(mBean);
                //设置全局手数
                this.mExpcNum = mMaxCloseTradeNum;

                holder.mMaxpcNum.setText("平仓手数(最多" + mMaxCloseTradeNum + "手)");

                //最大输入平仓手数
                holder.mEtMaxpc.setText(String.valueOf(mMaxCloseTradeNum));

                //设置手数事件
                setPositionListener(mViewHolder, mBean);

                double mLastPrice = getDouble(mProductListBean.getLastPrice());
                this.mExPrice = mLastPrice;
                this.mExLastPrice = mLastPrice;
                holder.mEtPrice.setText(mExPrice + "");
                setPriceListener(mViewHolder);

                holder.mPriceHint.setText("≥" + mLowerLimitPrice + " 跌停价 且 ≤" + mUpperLimitPrice + "涨停价");

                //实际盈亏
                holder.mYkprice.setText("￥" + getActualProfitLoss(mMaxCloseTradeNum, mBean));

                //平仓手续费
                holder.mSxprice.setText("￥" + getCloseTradePrice(mMaxCloseTradeNum, mBean));

                //平仓盈亏
                mDialog.setPingcangSatte(mBean.getType(), getCloseProfitLoss(mMaxCloseTradeNum, mBean));
            }
        } else {
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
    private void setPositionListener(final KLinePositionDialogAdapter.ViewHolder mHolder, final HoldPositionBean mBean) {

        //手数 -
        mHolder.mMaxpcDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int textNum = getIntText(mHolder.mEtMaxpc);
                if (textNum > 1) {
                    textNum--;
                    mHolder.mEtMaxpc.setText(String.valueOf(textNum));
                    //实际盈亏
                    mHolder.mYkprice.setText("￥" + getActualProfitLoss(textNum,mBean));
                    //平仓手续费
                    mHolder.mSxprice.setText("￥" + getCloseTradePrice(textNum, mBean));
                    //平仓盈亏
                    mDialog.setPingcangSatte(item.getType(), getCloseProfitLoss(textNum, mBean));
                }
            }
        });

        mHolder.mMaxpcAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int textNum = getIntText(mHolder.mEtMaxpc);
                if (textNum < mMaxCloseTradeNum) {
                    textNum++;
                    mHolder.mEtMaxpc.setText(String.valueOf(textNum));
                    //实际盈亏
                    mHolder.mYkprice.setText("￥" + getActualProfitLoss(textNum,mBean));
                    //平仓手续费
                    mHolder.mSxprice.setText("￥" + getCloseTradePrice(textNum, mBean));
                    //平仓盈亏
                    mDialog.setPingcangSatte(mBean.getType(), getCloseProfitLoss(textNum, mBean));
                }
            }
        });

        mHolder.mEtMaxpc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mExpcNum = getIntText(mHolder.mEtMaxpc);
            }
        });
    }


    //委托价
    private void setPriceListener(final KLinePositionDialogAdapter.ViewHolder mHolder) {

        mHolder.mPriceDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double textPrice = getDoubleText(mHolder.mEtPrice);
                if (textPrice - mLowerLimitPrice > 1) {
                    textPrice--;
                    mHolder.mEtPrice.setText(String.valueOf(textPrice));
                }
            }
        });

        mHolder.mPriceAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double textPrice = getDoubleText(mHolder.mEtPrice);
                if (mUpperLimitPrice - textPrice  > 1) {
                    textPrice++;
                    mHolder.mEtPrice.setText(String.valueOf(textPrice));
                }
            }
        });

        mHolder.mEtPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //跌停板价 mLowerLimitPrice  mUpperLimitPrice; //涨停板价
                //boolean isInt = Pattern.compile("^-?[1-9]\\d*$").matcher(s.toString()).find();
                //boolean isDouble = Pattern.compile("^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$").matcher(s.toString()).find();
                mExPrice = getDoubleText(mHolder.mEtPrice);
                mHolder.mEtPrice.setSelection(mHolder.mEtPrice.length());
            }
        });
    }

    private int getIntText(EditText mText) {

        return mText.getText() != null && !TextUtils.isEmpty(mText.getText().toString()) ? Integer.parseInt(mText.getText().toString()) : 0;
    }

    private double getDoubleText(EditText mText) {

        return mText.getText() != null && !TextUtils.isEmpty(mText.getText().toString()) ? Double.parseDouble(mText.getText().toString()) : 0;
    }

    private double getDouble(String num){
        if (!TextUtils.isEmpty(num)){
            return Float.parseFloat(num);
        }
        return 0;
    }

    /**
     * 获取最大平仓手数
     *
     * @param mBean
     * @return
     */
    private int getMaxCloseTradeNum(HoldPositionBean mBean) {
        //int position ,int ydPosition,int todayPosition ,String excode
        return TradeUtil.getMaxCloseTradeNum(mBean.getPosition(),mBean.getYdPosition(),mBean.getTodayPosition(),mBean.getExcode());
    }


    /**
     * 平仓盈亏
     *
     * @param tradeNum 手数
     * @param bean
     * @return
     */
    private double getCloseProfitLoss(int tradeNum, HoldPositionBean bean) {
        //String holdAvgPrice ,String askPrice1 ,String bidPrice1 ,String priceTick ,int volumeMultiple ,int type ,int tradeNum
        String holdAvgPrice = bean.getHoldAvgPrice();
        String askPrice1 = mProductListBean.getAskPrice1();
        String bidPrice1 = mProductListBean.getBidPrice1();
        String priceTick = mProductListBean.getPriceTick();
        int volumeMultiple = mProductListBean.getVolumeMultiple();
        int type = bean.getType();
        try {
          return   TradeUtil.getCloseProfitLoss(holdAvgPrice,askPrice1,bidPrice1,priceTick,volumeMultiple,type,tradeNum);
        }catch (Exception e){
          e.printStackTrace();
            return 0;
        }
    }


    /**
     * 实际盈亏
     *
     * @param tradeNum 手数
     * @param bean
     * @return
     */
    private double getActualProfitLoss(int tradeNum, HoldPositionBean bean) {
        //String openAvgPrice,String bidPrice1,String askPrice1,String priceTick,int volumeMultiple,int type ,int tradeNum
        String openAvgPrice = bean.getOpenAvgPrice();
        String bidPrice1 = mProductListBean.getBidPrice1();
        String askPrice1 = mProductListBean.getAskPrice1();
        String priceTick = mProductListBean.getPriceTick();
        int volumeMultiple = mProductListBean.getVolumeMultiple();
        int type = bean.getType();
        try {
            return TradeUtil.getActualProfitLoss(openAvgPrice,bidPrice1,askPrice1,priceTick,volumeMultiple,type,tradeNum);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * 获取平仓手续费
     *
     * @param tradeNum 手数
     * @param bean
     * @return
     */
    private String getCloseTradePrice(int tradeNum, HoldPositionBean bean) {
        //String closeTodayRatioByMoney,String bidPrice1,String askPrice1,int volumeMultiple,String closeTodayRatioByVolume
        // ,String closeRatioByMoney,String closeRatioByVolume, int todayPosition , int type , int tradeNum ,String excode
        String closeTodayRatioByMoney = mProductListBean.getCloseTodayRatioByMoney();
        String bidPrice1 = mProductListBean.getBidPrice1();
        String askPrice1 = mProductListBean.getAskPrice1();
        int volumeMultiple = mProductListBean.getVolumeMultiple();
        String closeTodayRatioByVolume = mProductListBean.getCloseTodayRatioByVolume();
        String closeRatioByMoney = mProductListBean.getCloseRatioByMoney();
        String closeRatioByVolume = mProductListBean.getCloseRatioByVolume();
        int todayPosition = bean.getTodayPosition();
        int type = bean.getType();
        String excode = bean.getExcode();
        try {
            double closeTradePrice  = TradeUtil.getCloseTradePrice(closeTodayRatioByMoney,bidPrice1,askPrice1,volumeMultiple,closeTodayRatioByVolume,closeRatioByMoney,closeRatioByVolume,todayPosition,type,tradeNum,excode);
            return String.valueOf(closeTradePrice);
        }catch (Exception e){
            e.printStackTrace();
            return "0";
        }
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
}
