package com.honglu.future.dialog.klineposition;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
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
import com.honglu.future.ui.trade.bean.HoldPositionBean;
import com.honglu.future.ui.trade.bean.ProductListBean;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ToastUtil;
import com.honglu.future.util.TradeUtil;

/**
 * Created by zhuaibing on 2018/1/11
 */

public class KLinePositionDetailsDialog extends BaseDialog<KLinePositionDialogPresenter> implements KLinePositionDialogContract.View , View.OnClickListener{

    private TextView mName;
    private ImageView mClose;
    private TextView mBuyNum;
    private TextView mPriced;
    private TextView mLastPrice;
    private TextView mProfitLoss;
    private ImageView mPriceDel;
    private ImageView mPriceAdd;
    private EditText mEtPrice;
    private TextView mMaxpcNum;
    private ImageView mMaxpcDel;
    private ImageView mMaxpcAdd;
    private EditText mEtMaxpc;
    private TextView mSxPrice;
    private TextView mGongShi;
    private TextView mYkprice;
    private TextView mPingcang;

    private HoldPositionBean mHoldPositionBean;
    private ProductListBean mProductListBean;
    private String mLastPriceValue; //最新价格
    private double mLowerLimitPrice;//跌停板
    private double mUpperLimitPrice;//涨停板
    private int mNumPosition;

    public KLinePositionDetailsDialog(@NonNull Activity mActivity) {
        super(mActivity, R.style.DateDialog);
    }

    @Override
    public void initPresenter() {
        mPresenter.init(KLinePositionDetailsDialog.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_kline_position_details);
        Window mWindow = this.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        final WindowManager manage = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        params.width = manage.getDefaultDisplay().getWidth();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        mWindow.setAttributes(params);
        setCanceledOnTouchOutside(false);

        mName = (TextView) findViewById(R.id.tv_name);
        mClose = (ImageView) findViewById(R.id.iv_close);
        mBuyNum = (TextView) findViewById(R.id.tv_buy_num);
        mPriced = (TextView) findViewById(R.id.tv_priced);
        mLastPrice = (TextView) findViewById(R.id.tv_lastPrice);
        mProfitLoss = (TextView) findViewById(R.id.tv_profit_loss);
        mPriceDel = (ImageView) findViewById(R.id.iv_price_del);
        mPriceAdd = (ImageView) findViewById(R.id.iv_price_add);
        mEtPrice = (EditText) findViewById(R.id.et_price);

        mMaxpcNum = (TextView) findViewById(R.id.tv_maxpc_num);
        mMaxpcDel = (ImageView) findViewById(R.id.iv_maxpc_del);
        mMaxpcAdd = (ImageView) findViewById(R.id.iv_maxpc_add);
        mEtMaxpc = (EditText) findViewById(R.id.et_maxpc);

        mSxPrice = (TextView) findViewById(R.id.tv_sxprice);
        mGongShi = (TextView) findViewById(R.id.tv_gongshi);

        mYkprice = (TextView) findViewById(R.id.tv_ykprice);
        mPingcang = (TextView) findViewById(R.id.tv_pingcang);
        setListener();
    }

    private void setListener() {
        mClose.setOnClickListener(this);
        mMaxpcDel.setOnClickListener(this);
        mMaxpcAdd.setOnClickListener(this);
        mPriceDel.setOnClickListener(this);
        mPriceAdd.setOnClickListener(this);
        mPingcang.setOnClickListener(this);
        mEtMaxpc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                int numPosition = getIntText(mEtMaxpc);
                mPingcang.setText(mContext.getString(R.string.weituo_pingcang));
                if (mHoldPositionBean !=null && numPosition <= mHoldPositionBean.getPosition()){
                    mNumPosition = numPosition;
                    double doubleText = getDoubleText(mEtPrice);
                    setTextViewData(doubleText,mNumPosition);
                }else {
                    mNumPosition = mHoldPositionBean.getPosition();
                    mEtMaxpc.setText(String.valueOf(mNumPosition));
                    setTextViewData(getDoubleText(mEtPrice),mNumPosition);
                }
            }
        });

        mEtPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                mPingcang.setText(mContext.getString(R.string.weituo_pingcang));
                double doubleText = getDoubleText(mEtPrice);
                setTextViewData(doubleText,getIntText(mEtMaxpc));
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_close:
                 dismiss();
                break;
            case R.id.iv_maxpc_del: //手数减
                int mNumDel = getIntText(mEtMaxpc);
                if (mNumDel > 1) {
                    mNumDel--;
                    mEtMaxpc.setText(String.valueOf(mNumDel));
                }
                break;
            case R.id.iv_maxpc_add: //手数加
                int mNumAdd = getIntText(mEtMaxpc);
                if (mNumAdd < mHoldPositionBean.getPosition()) {
                    mNumAdd++;
                    mEtMaxpc.setText(String.valueOf(mNumAdd));
                }
                break;
            case R.id.iv_price_del: //金额减
                double mPriceDel = getDoubleText(mEtPrice);
                if (mPriceDel - mLowerLimitPrice > 1) {
                    mPriceDel--;
                    mEtPrice.setText(String.valueOf(mPriceDel));
                }
                break;
            case R.id.iv_price_add: //金额加
                double mPriceAdd = getDoubleText(mEtPrice);
                if (mUpperLimitPrice - mPriceAdd > 1) {
                    mPriceAdd++;
                    mEtPrice.setText(String.valueOf(mPriceAdd));
                }
                break;
            case R.id.tv_pingcang: //平仓
                if(DeviceUtils.isFastDoubleClick() || !App.getConfig().getAccountLoginStatus()){
                    return;
                }
                String todayPosition = String.valueOf(mHoldPositionBean.getTodayPosition());
                String userId = SpUtil.getString(Constant.CACHE_TAG_UID);
                String token = SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN);
                double exPcNum = getDoubleText(mEtMaxpc);
                double exPrice = getDoubleText(mEtPrice);
                String type = String.valueOf(mHoldPositionBean.getType());
                String instrumentId = mHoldPositionBean.getInstrumentId();
                String holdAvgPrice = mHoldPositionBean.getHoldAvgPrice();
                String company = SpUtil.getString(Constant.COMPANY_TYPE);

                if (exPrice <= 0 || exPrice < mLowerLimitPrice || exPrice > mUpperLimitPrice) {
                    showErrorMsg("平仓委托价必须≥" + mLowerLimitPrice + "且≤" + mUpperLimitPrice, null);
                    return;
                }
                if (exPcNum <= 0) {
                    showErrorMsg("手数必须大于0", null);
                    return;
                }

                mPresenter.closeOrder(
                        String.valueOf(todayPosition),
                        userId,
                        token,
                        String.valueOf(exPcNum),
                        type,
                        String.valueOf(exPrice),
                        instrumentId,
                        holdAvgPrice,
                        company);
                break;
        }
    }


    public void mPushRefresh(String lastPrice){
        if (isShowing() && mHoldPositionBean !=null && mProductListBean !=null){
            mLastPrice.setText(lastPrice);
            double closeProfitLoss = getCloseProfitLoss(mHoldPositionBean, parseDouble(lastPrice), mNumPosition);
            mProfitLoss.setText(String.valueOf(closeProfitLoss));
        }
    }


    //HoldPositionBean list bean     ProductListBean 详情bean
    public void showDetailsDialog(HoldPositionBean holdPositionBean, ProductListBean productListBean,String nameValue) {
        if (holdPositionBean == null || productListBean == null) {
            return;
        }
        this.mHoldPositionBean = holdPositionBean;
        this.mProductListBean = productListBean;
        this.mLastPriceValue = holdPositionBean.getLastPrice();
        this.mLowerLimitPrice = parseDouble(mProductListBean.getLowerLimitPrice());
        this.mUpperLimitPrice = parseDouble(mProductListBean.getUpperLimitPrice());
        show();
        mName.setText(String.format(mContext.getString(R.string.close_pingcang_),nameValue));
        //手数  1 跌  2涨
        if (mHoldPositionBean.getType() == Constant.TYPE_BUY_DOWN) {
            mBuyNum.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
            mBuyNum.setText("买跌" + mHoldPositionBean.getPosition() + "手");

        } else {
            mBuyNum.setTextColor(mContext.getResources().getColor(R.color.color_FB4F4F));
            mBuyNum.setText("买涨" + mHoldPositionBean.getPosition() + "手");
        }

        mPingcang.setText(mContext.getString(R.string.kuaisu_pingcang));
        //持仓均价
        mPriced.setText(mHoldPositionBean.getOpenAvgPrice());
        //最新价格
        mLastPrice.setText(mHoldPositionBean.getLastPrice());
        //价格
        mEtPrice.setText(mHoldPositionBean.getLastPrice());
        mEtPrice.setSelection(mHoldPositionBean.getLastPrice().length());
        //手数
        mEtMaxpc.setText(String.valueOf(mHoldPositionBean.getPosition()));
        mEtMaxpc.setSelection(String.valueOf(mHoldPositionBean.getPosition()).length());

        mMaxpcNum.setText(String.format(mContext.getString(R.string.pingcang_num),mHoldPositionBean.getPosition()));

        setTextViewData(parseDouble(mHoldPositionBean.getLastPrice()), mHoldPositionBean.getPosition());

        if (mHoldPositionBean.getType() == Constant.TYPE_BUY_DOWN) { //1 跌  2涨
            mPingcang.setBackgroundResource(R.color.color_2CC593);
        } else {
            mPingcang.setBackgroundResource(R.color.color_FB4F4F);
        }
    }


    private void setTextViewData(double mPrice, int mPcNum) {

        if (mPrice == 0 || mPcNum == 0){
            mYkprice.setTextColor(ContextCompat.getColor(mContext, R.color.color_B1B1B3));
            mYkprice.setText("---");
            mProfitLoss.setTextColor(ContextCompat.getColor(mContext, R.color.color_B1B1B3));
            mProfitLoss.setText("---");
            mSxPrice.setTextColor(ContextCompat.getColor(mContext, R.color.color_B1B1B3));
            mSxPrice.setText("---");
        }else {
            //平仓盈亏
            double actualProfitLoss = getCloseProfitLoss(mHoldPositionBean, mPrice, mPcNum);

            if (actualProfitLoss > 0) {
                mYkprice.setTextColor(ContextCompat.getColor(mContext, R.color.color_FB4F4F));
                mYkprice.setText(String.format(mContext.getString(R.string.z_yuan), String.valueOf(actualProfitLoss)));
                mProfitLoss.setTextColor(ContextCompat.getColor(mContext, R.color.color_FB4F4F));
                mProfitLoss.setText(String.valueOf(actualProfitLoss));
            } else if (actualProfitLoss < 0) {
                mYkprice.setTextColor(ContextCompat.getColor(mContext, R.color.color_2CC593));
                mYkprice.setText(String.valueOf(actualProfitLoss).replace("-", mContext.getString(R.string.f_yuan)));
                mProfitLoss.setTextColor(ContextCompat.getColor(mContext, R.color.color_2CC593));
                mProfitLoss.setText(String.valueOf(actualProfitLoss));
            } else {
                mYkprice.setTextColor(ContextCompat.getColor(mContext, R.color.color_333333));
                mYkprice.setText(String.format(mContext.getString(R.string.yuan), String.valueOf(actualProfitLoss)));
                mProfitLoss.setTextColor(ContextCompat.getColor(mContext, R.color.color_333333));
                mProfitLoss.setText(String.valueOf(actualProfitLoss));
            }

            //平仓手续费
            double closeTradePrice = getCloseTradePrice(mPrice, mPcNum, mHoldPositionBean);
            mSxPrice.setText(String.format(mContext.getString(R.string.yuan), String.valueOf(closeTradePrice)));

            getCloseTradePrice(mPrice,mPcNum,mGongShi);
        }
    }



    @Override
    public void getProductDetailSuccess(ProductListBean bean) {
    }

    @Override
    public void closeOrderSuccess() {
        ToastUtil.show("平仓申请已提交");
        dismiss();
    }

    //实际盈亏
    private double getCloseProfitLoss(HoldPositionBean bean, double lastPrice, int mPcNum) {
        try {
            int type = bean.getType();
            String openAvgPrice = bean.getOpenAvgPrice();
            String priceTick = mProductListBean.getPriceTick();
            int volumeMultiple = mProductListBean.getVolumeMultiple();
            return TradeUtil.getActualProfitLoss(type,openAvgPrice,priceTick,volumeMultiple,lastPrice,mPcNum);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //实际盈亏
    private void getCloseTradePrice(double lastPrice, int mPcNum,TextView textView) {
        try {
            String closeTodayRatioByMoney = mProductListBean.getCloseTodayRatioByMoney();
            int volumeMultiple = mProductListBean.getVolumeMultiple();
            String closeTodayRatioByVolume = mProductListBean.getCloseTodayRatioByVolume();
            String closeRatioByMoney = mProductListBean.getCloseRatioByMoney();
            String closeRatioByVolume = mProductListBean.getCloseRatioByVolume();
            int todayPosition = mHoldPositionBean.getTodayPosition();
            TradeUtil.getCloseTradePrice(mProductListBean, todayPosition, closeTodayRatioByMoney,
                    closeTodayRatioByVolume, closeRatioByMoney, closeRatioByVolume, lastPrice, mPcNum, volumeMultiple,textView);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取平仓手续费
     *
     * @param tradeNum 手数
     * @param bean
     * @return
     */
    private double getCloseTradePrice(double price, int tradeNum, HoldPositionBean bean) {
        String closeTodayRatioByMoney = mProductListBean.getCloseTodayRatioByMoney();
        int volumeMultiple = mProductListBean.getVolumeMultiple();
        String closeTodayRatioByVolume = mProductListBean.getCloseTodayRatioByVolume();
        String closeRatioByMoney = mProductListBean.getCloseRatioByMoney();
        String closeRatioByVolume = mProductListBean.getCloseRatioByVolume();
        int todayPosition = bean.getTodayPosition();
        try {
            double closeTradePrice = TradeUtil.getCloseTradePrice(mProductListBean, todayPosition, closeTodayRatioByMoney,
                    closeTodayRatioByVolume, closeRatioByMoney, closeRatioByVolume, price, tradeNum, volumeMultiple);
            return closeTradePrice;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private double parseDouble(String strdouble) {
        try {
            return Double.parseDouble(strdouble);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private int getIntText(EditText mText) {
        return mText.getText() != null && !TextUtils.isEmpty(mText.getText().toString().trim()) ? Integer.parseInt(mText.getText().toString().trim()) : 0;
    }

    private String getText(EditText mText){
        return mText.getText() !=null && !TextUtils.isEmpty(mText.getText().toString().trim()) ? mText.getText().toString() : "";
    }

    private double getDoubleText(EditText mText) {
        return mText.getText() != null && !TextUtils.isEmpty(mText.getText().toString().trim()) ? Double.parseDouble(mText.getText().toString().trim()) : 0;
    }
}
