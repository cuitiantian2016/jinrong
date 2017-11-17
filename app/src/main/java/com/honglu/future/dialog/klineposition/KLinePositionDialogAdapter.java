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
import com.honglu.future.util.NumberUtil;
import com.honglu.future.widget.recycler.BaseRecyclerAdapter;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zq on 2017/11/2.
 */

public class KLinePositionDialogAdapter extends BaseRecyclerAdapter<KLinePositionDialogAdapter.ViewHolder, HoldPositionBean> {
    private ProductListBean mProductListBean;
    private KLinePositionDialogPresenter mPresenter;
    private KLinePositionDialog mDialog;

    private int mPosition = -1;
    private int mExpcNum = 0; //平仓手数
    private int mExPrice = 0;//价格

    private String mLowerLimitPrice; //跌停板价
    private String mUpperLimitPrice; //涨停板价
    private KLinePositionDialogAdapter.ViewHolder mViewHolder;

    public KLinePositionDialogAdapter(KLinePositionDialogPresenter mPresenter, KLinePositionDialog mDialog) {
        this.mPresenter = mPresenter;
        this.mDialog = mDialog;
    }

    public void setProductListBean(ProductListBean bean) {
        this.mExpcNum = 0;
        this.mExPrice = 0;
        this.mProductListBean = bean;
        this.mLowerLimitPrice = mProductListBean.getLowerLimitPrice();
        this.mUpperLimitPrice = mProductListBean.getUpperLimitPrice();
        notifyDataSetChanged();
    }

    /**
     * mpush 刷新对应数据
     *
     * @param lowerLimitPrice
     * @param upperLimitPrice
     */
    public void setMpushRefreshData(String lowerLimitPrice, String upperLimitPrice) {
        this.mLowerLimitPrice = lowerLimitPrice;
        this.mUpperLimitPrice = upperLimitPrice;
        if (mViewHolder != null)
            mViewHolder.mPriceHint.setText("≥" + getIntNum(mLowerLimitPrice) + " 跌停价 且 ≤" + getIntNum(mUpperLimitPrice) + "涨停价");
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
    public int getExPrice() {
        return mExPrice;
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
                int maxCloseTradeNum = getMaxCloseTradeNum(mBean);
                //设置全局手数
                this.mExpcNum = maxCloseTradeNum;

                holder.mMaxpcNum.setText("平仓手数(最多" + maxCloseTradeNum + "手)");

                //最大输入平仓手数
                holder.mEtMaxpc.setText(String.valueOf(maxCloseTradeNum));

                //设置手数事件
                setPositionListener(mViewHolder, mBean);


                this.mExPrice = getIntNum(mProductListBean.getLastPrice());
                holder.mEtPrice.setText(mExPrice + "");
                setPriceListener(mViewHolder);

                holder.mPriceHint.setText("≥" + getIntNum(mLowerLimitPrice) + " 跌停价 且 ≤" + getIntNum(mUpperLimitPrice) + "涨停价");

                //实际盈亏
                holder.mYkprice.setText("￥" + getActualProfitLoss(maxCloseTradeNum, mBean));

                //平仓手续费
                holder.mSxprice.setText("￥" + getCloseTradePrice(maxCloseTradeNum, mBean));

                //平仓盈亏
                mDialog.setPingcangSatte(mBean.getType(), getCloseProfitLoss(maxCloseTradeNum, mBean));
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
                int textNum = getText(mHolder.mEtMaxpc);
                if (textNum > 1) {
                    textNum--;
                    mHolder.mEtMaxpc.setText(textNum + "");
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
                int textNum = getText(mHolder.mEtMaxpc);
                if (textNum < mBean.getPosition()) {
                    textNum++;
                    mHolder.mEtMaxpc.setText(textNum + "");
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
                mExpcNum = getText(mHolder.mEtMaxpc);
            }
        });
    }


    //委托价
    private void setPriceListener(final KLinePositionDialogAdapter.ViewHolder mHolder) {

        mHolder.mPriceDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int textNum = getText(mHolder.mEtPrice);
                if (textNum > 1) {
                    textNum--;
                    mHolder.mEtPrice.setText(textNum + "");
                }
            }
        });

        mHolder.mPriceAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double textNum = getDoubleText(mHolder.mEtPrice);
                double lastPrice = !TextUtils.isEmpty(mProductListBean.getLastPrice()) ? Double.parseDouble(mProductListBean.getLastPrice()) : 0;
                if (textNum < lastPrice) {
                    textNum++;
                    mHolder.mEtPrice.setText(textNum + "");
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
                mExPrice = getText(mHolder.mEtPrice);
            }
        });
    }

    private int getText(EditText mText) {

        return mText.getText() != null && !TextUtils.isEmpty(mText.getText().toString()) ? Integer.parseInt(mText.getText().toString()) : 0;
    }

    private double getDoubleText(EditText mText) {

        return mText.getText() != null && !TextUtils.isEmpty(mText.getText().toString()) ? Double.parseDouble(mText.getText().toString()) : 0;
    }


    private int getIntNum(String num) {
        if (!TextUtils.isEmpty(num)) {
            return Integer.parseInt(num);
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
        int maxCloseTrade = 0;

        if (Constant.CODE_SHFE.equals(mBean.getExcode())) {
            //上期所
            if (item.getTodayPosition() > 0) {  //今平手数
                maxCloseTrade = mBean.getPosition();
            } else {  //昨平手数
                maxCloseTrade = mBean.getYdPosition();
            }
        } else { //其他交易所
            maxCloseTrade = mBean.getPosition();
        }
        return maxCloseTrade;
    }


    /**
     * 平仓盈亏
     *
     * @param tradeNum 手数
     * @param bean
     * @return
     */
    private double getCloseProfitLoss(int tradeNum, HoldPositionBean bean) {
        try {
            double oneProfitAndLossToday = 0;
            if (Constant.TYPE_BUY_DOWN == bean.getType()) {
                oneProfitAndLossToday = NumberUtil.multiply(NumberUtil.divide(NumberUtil.subtract(new BigDecimal(bean.getHoldAvgPrice()).doubleValue(), new BigDecimal(mProductListBean.getAskPrice1()).doubleValue()),
                        new BigDecimal(mProductListBean.getPriceTick()).doubleValue()), NumberUtil.multiply(new BigDecimal(mProductListBean.getVolumeMultiple()).doubleValue(), new BigDecimal(mProductListBean.getPriceTick()).doubleValue()));
            } else {
                oneProfitAndLossToday = NumberUtil.multiply(NumberUtil.divide(NumberUtil.subtract(new BigDecimal(mProductListBean.getBidPrice1()).doubleValue(), new BigDecimal(bean.getHoldAvgPrice()).doubleValue()),
                        new BigDecimal(mProductListBean.getPriceTick()).doubleValue()),
                        NumberUtil.multiply(new BigDecimal(mProductListBean.getVolumeMultiple()).doubleValue(), new BigDecimal(mProductListBean.getPriceTick()).doubleValue()));
            }
            return NumberUtil.multiply(oneProfitAndLossToday, new BigDecimal(tradeNum).doubleValue());
        } catch (Exception e) {
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
        try {
            double oneProfitAndLossTotal = 0;
            if (Constant.TYPE_BUY_DOWN == bean.getType()) {

                oneProfitAndLossTotal = NumberUtil.multiply(NumberUtil.divide(NumberUtil.subtract(new BigDecimal(bean.getOpenAvgPrice()).doubleValue(), new BigDecimal(mProductListBean.getAskPrice1()).doubleValue()),
                        new BigDecimal(mProductListBean.getPriceTick()).doubleValue()),
                        NumberUtil.multiply(new BigDecimal(mProductListBean.getVolumeMultiple()).doubleValue(), new BigDecimal(mProductListBean.getPriceTick()).doubleValue()));
            } else {
                oneProfitAndLossTotal = NumberUtil.multiply(NumberUtil.divide(NumberUtil.subtract(new BigDecimal(mProductListBean.getBidPrice1()).doubleValue(), new BigDecimal(bean.getOpenAvgPrice()).doubleValue()),
                        new BigDecimal(mProductListBean.getPriceTick()).doubleValue()),
                        NumberUtil.multiply(new BigDecimal(mProductListBean.getVolumeMultiple()).doubleValue(), new BigDecimal(mProductListBean.getPriceTick()).doubleValue()));
            }

            return NumberUtil.multiply(oneProfitAndLossTotal, new BigDecimal(tradeNum).doubleValue());

        } catch (Exception e) {
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
        try {
            boolean mCloseTodayRatioByMoney = TextUtils.isEmpty(mProductListBean.getCloseTodayRatioByMoney())
                    || Double.parseDouble(mProductListBean.getCloseTodayRatioByMoney()) == 0 ? false : true;

            double oneCloseToday; //今平一手
            if (mCloseTodayRatioByMoney) {
                if (bean.getType() == Constant.TYPE_BUY_DOWN) {
                    oneCloseToday = NumberUtil.multiply(new BigDecimal(mProductListBean.getCloseTodayRatioByMoney()).doubleValue(),
                            new BigDecimal(mProductListBean.getBidPrice1()).doubleValue()) * mProductListBean.getVolumeMultiple();
                } else {
                    oneCloseToday = NumberUtil.multiply(new BigDecimal(mProductListBean.getCloseTodayRatioByMoney()).doubleValue(),
                            new BigDecimal(mProductListBean.getAskPrice1()).doubleValue()) * mProductListBean.getVolumeMultiple();
                }
            } else {
                oneCloseToday = Double.parseDouble(mProductListBean.getCloseTodayRatioByVolume());
            }

            double oneCloseYD = 0;// 昨平一手的手续费
            if (mCloseTodayRatioByMoney) {
                if (bean.getType() == Constant.TYPE_BUY_DOWN) {//买跌
                    oneCloseYD = NumberUtil.multiply(new BigDecimal(mProductListBean.getCloseRatioByMoney()).doubleValue(),
                            new BigDecimal(mProductListBean.getBidPrice1()).doubleValue()) * mProductListBean.getVolumeMultiple();
                } else {//买涨
                    oneCloseYD = NumberUtil.multiply(new BigDecimal(mProductListBean.getCloseRatioByMoney()).doubleValue(),
                            new BigDecimal(mProductListBean.getAskPrice1()).doubleValue()) * mProductListBean.getVolumeMultiple();
                }
            } else {
                oneCloseYD = Double.parseDouble(mProductListBean.getCloseRatioByVolume());
            }


            double closeTradePrice = 0;
            if (Constant.CODE_SHFE.equals(bean.getExcode())) {
                if (bean.getTodayPosition() > 0) {
                    closeTradePrice = NumberUtil.multiply(oneCloseToday, new BigDecimal(tradeNum).doubleValue());
                } else {
                    closeTradePrice = NumberUtil.multiply(oneCloseYD, new BigDecimal(tradeNum).doubleValue());
                }
            } else {
                if (tradeNum <= bean.getTodayPosition()) {
                    closeTradePrice = NumberUtil.multiply(oneCloseToday, new BigDecimal(tradeNum).doubleValue());
                } else {
                    double todaySXF = NumberUtil.multiply(oneCloseToday, new BigDecimal(bean.getTodayPosition()).doubleValue());
                    double ydSXF = NumberUtil.multiply(oneCloseYD, new BigDecimal(tradeNum - bean.getTodayPosition()).doubleValue());
                    closeTradePrice = todaySXF + ydSXF;
                }
            }

            return String.valueOf(closeTradePrice);
        } catch (Exception e) {
            e.printStackTrace();
            return "--";
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


    /**
     * 平仓手续费
     * @param type
     * @param mText
     * @param bean
     * @return private float getPCprice(int type,EditText mText, HoldPositionBean bean){
    float closePrice = "2".equals(type) ? getFloatNum(mProductListBean.getBidPrice1()) : getFloatNum(mProductListBean.getAskPrice1());

    float closeRatio = getFloatNum(mProductListBean.getCloseRatioByMoney());

    int buyCount = getText(mText);

    float fee = 0;
    boolean isToday = bean.getTodayPosition() > 0;

    if (closeRatio !=0){
    if (isToday){
    closeRatio = getFloatNum(mProductListBean.getCloseTodayRatioByMoney());
    }
    fee = closePrice * mProductListBean.getVolumeMultiple() * closeRatio * buyCount;
    }else {
    closeRatio = getFloatNum(mProductListBean.getCloseRatioByVolume());
    if (isToday){
    closeRatio = getFloatNum(mProductListBean.getCloseTodayRatioByVolume());
    }
    fee = closeRatio * buyCount;
    }
    return fee;
    }

     */


    /**
     * 平仓盈亏
     *
     * @param type  1 跌  2涨
     * @param mText
     * @param bean
     * @return private float getPCProfitLoss(int type, EditText mText, HoldPositionBean bean) {
    float closePrice = "2".equals(type) ? getFloatNum(mProductListBean.getBidPrice1()) : getFloatNum(mProductListBean.getAskPrice1());

    float price = closePrice - getFloatNum(bean.getOpenAvgPrice());

    float priceTick = getFloatNum(mProductListBean.getPriceTick());

    float mProfitLoss = price / priceTick * priceTick * mProductListBean.getVolumeMultiple() * getText(mText);

    return mProfitLoss;
    }

     */


    /**
     * 实际盈亏
     *
     * @param type  1 跌  2涨
     * @param mText
     * @param bean
     * @return private float getActualProfitLoss(int type, EditText mText, HoldPositionBean bean) {
    float closePrice = "2".equals(type) ? getFloatNum(mProductListBean.getBidPrice1()) : getFloatNum(mProductListBean.getAskPrice1());

    float price = closePrice - getFloatNum(bean.getOpenAvgPrice());

    float priceTick = getFloatNum(mProductListBean.getPriceTick());

    float mProfitLoss = price / priceTick * priceTick * mProductListBean.getVolumeMultiple() * getText(mText);

    return mProfitLoss;
    }


    private float getFloatNum(String num) {
    if (!TextUtils.isEmpty(num)) {
    return Float.parseFloat(num);
    }
    return 0;
    }

    private int getIntNum(String num) {
    if (!TextUtils.isEmpty(num)) {
    return Integer.parseInt(num);
    }
    return 0;
    }
     */

}
