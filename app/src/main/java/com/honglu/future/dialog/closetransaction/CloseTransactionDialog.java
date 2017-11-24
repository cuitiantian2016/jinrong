package com.honglu.future.dialog.closetransaction;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseDialog;
import com.honglu.future.config.Constant;
import com.honglu.future.dialog.ConfirmDialog;
import com.honglu.future.dialog.TradeTipDialog;
import com.honglu.future.events.ReceiverMarketMessageEvent;
import com.honglu.future.mpush.MPushUtil;
import com.honglu.future.ui.trade.bean.HoldPositionBean;
import com.honglu.future.ui.trade.bean.ProductListBean;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ToastUtil;
import com.honglu.future.util.TradeUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 平仓 dialog
 * Created by zhuaibing on 2017/11/6
 */

public class CloseTransactionDialog extends BaseDialog<CloseTransactionPresenter> implements View.OnClickListener, CloseTransactionContract.View {
    private TextView mName;
    private ImageView mClose;
    private TextView mBuyRise;
    private TextView mChicangAveragePrice;
    private ImageView mPriceDel;
    private EditText mPrice;
    private ImageView mPriceAdd;
    private TextView mPricePrompt;
    private ImageView mSizeDel;
    private EditText mSize;
    private ImageView mSizeAdd;
    private TextView mCloseTransactionPrice;
    private TextView mCankaoProfitLoss;
    private TextView mFastCloseTransaction;
    private TextView mCloseHands;
    private TextView mProfitLoss;

    private HoldPositionBean mHoldPositionBean;
    private ProductListBean mProductListBean;
    private String mLowerLimitPrice; //跌停板价
    private String mUpperLimitPrice; //涨停板价
    private String mLastPrice;
    private int mMaxCloseTradeNum; //最大平仓手数
    private String mMPushCode;
    private String mInstrumentId;
    private String mExcode;
    private boolean mEtHasFocus = false;
    private ConfirmDialog mConfirmDialog = null;

    public CloseTransactionDialog(@NonNull Activity context) {
        super(context, R.style.DateDialog);
    }

    private void requestMarket(String code) {
        if (!TextUtils.isEmpty(code)) {
            this.mMPushCode = code;
            MPushUtil.requestMarket(code);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMarketEventMainThread(ReceiverMarketMessageEvent event) {
        if (!TextUtils.isEmpty(mMPushCode)
                && event.marketMessage.getExchangeID().equals(mExcode)
                && event.marketMessage.getInstrumentID().equals(mInstrumentId)
                && isShowing()) {
            this.mLowerLimitPrice = event.marketMessage.getLowerLimitPrice();
            this.mUpperLimitPrice = event.marketMessage.getUpperLimitPrice();
            this.mLastPrice = event.marketMessage.getLastPrice();
            mPricePrompt.setText("≥" + mLowerLimitPrice + " 跌停价 且 ≤" + mUpperLimitPrice + "涨停价");
            if (!mEtHasFocus) {
                mPrice.setText(mLastPrice);
                setTextViewData(getPrice(mPrice), getSize(mSize));
            }
        }
    }

    @Override
    public void showLoading(String content) {
        if (!TextUtils.isEmpty(content)) {
            App.loadingContent(mContext, content);
        }
    }

    @Override
    public void stopLoading() {
        super.stopLoading();
        App.hideLoading();
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        if (!TextUtils.isEmpty(msg))
            ToastUtil.show(msg);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_close_transaction);
        Window mWindow = this.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        WindowManager manage = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        params.width = manage.getDefaultDisplay().getWidth();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        mWindow.setAttributes(params);

        mName = (TextView) findViewById(R.id.tv_name);
        mClose = (ImageView) findViewById(R.id.iv_close);
        mBuyRise = (TextView) findViewById(R.id.tv_buy_rise);
        mChicangAveragePrice = (TextView) findViewById(R.id.tv_chicang_average_price);
        mPriceDel = (ImageView) findViewById(R.id.iv_price_del);
        mPrice = (EditText) findViewById(R.id.et_price);
        mPriceAdd = (ImageView) findViewById(R.id.iv_price_add);
        mPricePrompt = (TextView) findViewById(R.id.tv_price_prompt);
        mSizeDel = (ImageView) findViewById(R.id.iv_size_del);
        mSize = (EditText) findViewById(R.id.et_size);
        mSizeAdd = (ImageView) findViewById(R.id.iv_size_add);
        mCloseTransactionPrice = (TextView) findViewById(R.id.tv_close_transaction_price);
        mCankaoProfitLoss = (TextView) findViewById(R.id.tv_cankao_profit_loss);
        mFastCloseTransaction = (TextView) findViewById(R.id.tv_fast_close_transaction);
        mCloseHands = (TextView) findViewById(R.id.tv_close_hands);
        mProfitLoss = (TextView) findViewById(R.id.tv_profit_loss);

        mClose.setOnClickListener(this);
        mPriceDel.setOnClickListener(this);
        mPriceAdd.setOnClickListener(this);
        mSizeDel.setOnClickListener(this);
        mSizeAdd.setOnClickListener(this);
        mFastCloseTransaction.setOnClickListener(this);
        mName.setOnClickListener(this);
        setOrdeListener();
    }


    @Override
    public void dismiss() {
        super.dismiss();
        EventBus.getDefault().unregister(this);
    }

    public void showDialog(HoldPositionBean holdPositionBean, ProductListBean productListBean) {
        show();
        EventBus.getDefault().register(this);
        this.mHoldPositionBean = holdPositionBean;
        this.mProductListBean = productListBean;
        this.mLowerLimitPrice = mProductListBean.getLowerLimitPrice();
        this.mUpperLimitPrice = mProductListBean.getUpperLimitPrice();
        this.mMaxCloseTradeNum = holdPositionBean.getPosition();
        this.mInstrumentId = holdPositionBean.getInstrumentId();
        this.mExcode = holdPositionBean.getExcode();
        mPrice.clearFocus();
        mPrice.setFocusable(false);
        mName.setText(holdPositionBean.getInstrumentName());

        if (Constant.TYPE_BUY_DOWN == holdPositionBean.getType()) {
            mBuyRise.setTextColor(ContextCompat.getColor(mContext, R.color.color_2CC593));
            mBuyRise.setText("买跌" + mMaxCloseTradeNum + "手");
        } else {
            mBuyRise.setTextColor(ContextCompat.getColor(mContext, R.color.color_FB4F4F));
            mBuyRise.setText("买涨" + mMaxCloseTradeNum + "手");
        }

        mChicangAveragePrice.setText(holdPositionBean.getHoldAvgPrice());

        mPrice.setText(mProductListBean.getLastPrice());

        mCloseHands.setText("平仓手数（最多" + mMaxCloseTradeNum + "手）");

        mSize.setText(String.valueOf(mMaxCloseTradeNum));

        mPricePrompt.setText("≥" + mLowerLimitPrice + " 跌停价 且 ≤" + mUpperLimitPrice + "涨停价");

        setTextViewData(Double.parseDouble(mProductListBean.getLastPrice()), mMaxCloseTradeNum);

        boolean mClosed = "2".equals(mProductListBean.getIsClosed());
        if (mClosed) {
            mFastCloseTransaction.setEnabled(false);
            mFastCloseTransaction.setText("休市中");
            mFastCloseTransaction.setBackgroundResource(R.color.color_B1B1B3);
        } else {
            mFastCloseTransaction.setEnabled(true);
            mFastCloseTransaction.setText("快速平仓");
            if (mHoldPositionBean.getType() == Constant.TYPE_BUY_DOWN) {
                mFastCloseTransaction.setBackgroundResource(R.color.color_2CC593);
            } else {
                mFastCloseTransaction.setBackgroundResource(R.color.color_FB4F4F);
            }
        }

        //启动mpus
        requestMarket(holdPositionBean.getExcode() + "|" + holdPositionBean.getInstrumentId());
    }

    private void setTextViewData(double price, int tradeNum) {

        if (price == 0) {
            mCloseTransactionPrice.setText("---");
            mCankaoProfitLoss.setText("---");
            mProfitLoss.setText("---");
            mCloseTransactionPrice.setTextColor(ContextCompat.getColor(mContext, R.color.color_333333));
            mCankaoProfitLoss.setTextColor(ContextCompat.getColor(mContext, R.color.color_333333));
            mProfitLoss.setTextColor(ContextCompat.getColor(mContext, R.color.color_333333));
        } else {
            //平仓手续费
            String closeTradePrice = getCloseTradePrice(price, tradeNum);
            mCloseTransactionPrice.setText(String.format(mContext.getString(R.string.yuan), closeTradePrice));
            //实际盈亏
            double actualProfitLoss = getActualProfitLoss(price, tradeNum);
            mCankaoProfitLoss.setText(String.format(mContext.getString(R.string.yuan), actualProfitLoss));
            if (actualProfitLoss > 0) {
                mCankaoProfitLoss.setTextColor(ContextCompat.getColor(mContext, R.color.color_FB4F4F));
            } else if (actualProfitLoss < 0) {
                mCankaoProfitLoss.setTextColor(ContextCompat.getColor(mContext, R.color.color_2CC593));
            } else {
                mCankaoProfitLoss.setTextColor(ContextCompat.getColor(mContext, R.color.color_333333));
            }

            //平仓盈亏
            double closeProfitLoss = getCloseProfitLoss(price, tradeNum);
            mProfitLoss.setText(String.format(mContext.getString(R.string.yuan), String.valueOf(closeProfitLoss)));
            if (closeProfitLoss > 0) {
                mProfitLoss.setTextColor(ContextCompat.getColor(mContext, R.color.color_FB4F4F));
            } else if (closeProfitLoss < 0) {
                mProfitLoss.setTextColor(ContextCompat.getColor(mContext, R.color.color_2CC593));
            } else {
                mProfitLoss.setTextColor(ContextCompat.getColor(mContext, R.color.color_333333));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.iv_price_del: //平仓委托价 -
                double delPrice = getPrice(mPrice);
                if (delPrice - getDouble(mLowerLimitPrice) > 1) {
                    delPrice--;
                    mPrice.setText(String.valueOf(delPrice));
                }
                break;
            case R.id.iv_price_add://平仓委托价 +
                double addPrice = getPrice(mPrice);
                if (getDouble(mUpperLimitPrice) - addPrice > 1) {
                    addPrice++;
                    mPrice.setText(String.valueOf(addPrice));
                }
                break;
            case R.id.iv_size_del://平仓手数 -
                int delSize = getSize(mSize);
                if (delSize > 1) {
                    delSize--;
                    mSize.setText(String.valueOf(delSize));
                }
                break;
            case R.id.iv_size_add: //平仓手数 +
                int addSize = getSize(mSize);
                if (addSize < mMaxCloseTradeNum) {
                    addSize++;
                    mSize.setText(String.valueOf(addSize));
                }
                break;
            case R.id.tv_fast_close_transaction:
                if (mHoldPositionBean != null && mProductListBean !=null){

                    final double mPcPrice = getPrice(mPrice);//平仓价格
                    final int  mPcNum = getSize(mSize); //平仓手数
                    final double lastPrice = Integer.parseInt(mLastPrice);//最新价
                    final int todayPosition = mHoldPositionBean.getTodayPosition(); //今日持仓
                    final int type = mHoldPositionBean.getType();  //1 跌  2涨
                    final String userId = SpUtil.getString(Constant.CACHE_TAG_UID);
                    final String token = SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN);
                    final String instrumentId = mHoldPositionBean.getInstrumentId();
                    final String holdAvgPrice = mHoldPositionBean.getHoldAvgPrice();
                    final String company = "GUOFU";
                    if (mPcPrice <= 0 || mPcPrice < getDouble(mLowerLimitPrice) || mPcPrice > getDouble(mUpperLimitPrice)){
                        showErrorMsg("平仓委托价必须≥"+mLowerLimitPrice +"且≤"+mUpperLimitPrice,null);
                        return;
                    }
                    if (mPcNum <= 0) {
                        showErrorMsg(mContext.getString(R.string.close_transaction_num_dayu0),null);
                        return;
                    }
                    if (mConfirmDialog == null){
                        mConfirmDialog = new ConfirmDialog(mContext);
                    }

                    String typeStr = Constant.TYPE_BUY_DOWN == type ? "买跌" : "买涨";
                    String ykprice = mProfitLoss.getText().toString();
                    String content = String.format(mContext.getString(R.string.close_trade_hint), mHoldPositionBean.getInstrumentName(), typeStr, mPcNum, ykprice);

                    mConfirmDialog.setTitle("确认平仓")
                            .setContent(content).setRightListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //当价格等于
                            if (lastPrice == mPcPrice) {
                                mPresenter.ksCloseOrder(
                                        String.valueOf(todayPosition),
                                        userId,
                                        token,
                                        String.valueOf(mPcNum),
                                        String.valueOf(type),
                                        String.valueOf(mPcPrice),
                                        instrumentId,
                                        holdAvgPrice,
                                        company);
                            } else {
                                mPresenter.closeOrder(
                                        String.valueOf(todayPosition),
                                        userId,
                                        token,
                                        String.valueOf(mPcNum),
                                        String.valueOf(type),
                                        String.valueOf(mPcPrice),
                                        instrumentId,
                                        holdAvgPrice,
                                        company);
                            }

                        }
                    }).showDialog();
                }


                break;
            case R.id.tv_name:
                TradeTipDialog tipDialog = new TradeTipDialog(mContext, R.layout.layout_close_position_tip);
                tipDialog.show();
                break;
        }
    }

    private void setOrdeListener() {
        mPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setTextViewData(getPrice(mPrice), getSize(mSize));
            }
        });

        mSize.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setTextViewData(getPrice(mPrice), getSize(mSize));
            }
        });

        mPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mEtHasFocus = hasFocus;
            }
        });
    }


    private int getSize(EditText text) {
        return !TextUtils.isEmpty(text.getText().toString()) ? Integer.parseInt(mSize.getText().toString()) : 0;
    }

    private double getPrice(EditText text) {
        return !TextUtils.isEmpty(text.getText().toString()) ? Double.parseDouble(mPrice.getText().toString()) : 0;
    }

    private double getDouble(String num) {
        if (!TextUtils.isEmpty(num)) {
            return Float.parseFloat(num);
        }
        return 0;
    }

    /**
     * 获取平仓手续费
     *
     * @param tradeNum 手数
     * @return
     */
    private String getCloseTradePrice(double price, int tradeNum) {
        String closeTodayRatioByMoney = mProductListBean.getCloseTodayRatioByMoney();
        int volumeMultiple = mProductListBean.getVolumeMultiple();
        String closeTodayRatioByVolume = mProductListBean.getCloseTodayRatioByVolume();
        String closeRatioByMoney = mProductListBean.getCloseRatioByMoney();
        String closeRatioByVolume = mProductListBean.getCloseRatioByVolume();
        int todayPosition = mHoldPositionBean.getTodayPosition();
        try {
            double closeTradePrice = TradeUtil.getCloseTradePrice(todayPosition, closeTodayRatioByMoney, closeTodayRatioByVolume, closeRatioByMoney, closeRatioByVolume, price, tradeNum, volumeMultiple);
            return String.valueOf(closeTradePrice);
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    /**
     * 实际盈亏
     *
     * @param tradeNum 手数
     * @return
     */
    private double getActualProfitLoss(double price, int tradeNum) {
        String openAvgPrice = mHoldPositionBean.getOpenAvgPrice();
        String priceTick = mProductListBean.getPriceTick();
        int volumeMultiple = mProductListBean.getVolumeMultiple();
        int type = mHoldPositionBean.getType();
        try {
            return TradeUtil.getActualProfitLoss(type, openAvgPrice, priceTick, volumeMultiple, price, tradeNum);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 平仓盈亏
     *
     * @param tradeNum 手数
     * @return
     */
    private double getCloseProfitLoss(double price, int tradeNum) {
        String holdAvgPrice = mHoldPositionBean.getHoldAvgPrice();
        String priceTick = mProductListBean.getPriceTick();
        int volumeMultiple = mProductListBean.getVolumeMultiple();
        int type = mHoldPositionBean.getType();
        try {
            return TradeUtil.getCloseProfitLoss(type, holdAvgPrice, priceTick, volumeMultiple, price, tradeNum);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    @Override
    public void closeOrderSuccess() {
        ToastUtil.show("平仓申请已提交");
        dismiss();
    }
}
