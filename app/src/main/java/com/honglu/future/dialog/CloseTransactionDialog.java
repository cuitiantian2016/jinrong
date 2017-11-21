package com.honglu.future.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.honglu.future.config.Constant;
import com.honglu.future.ui.trade.bean.HoldPositionBean;
import com.honglu.future.ui.trade.bean.ProductListBean;
import com.honglu.future.util.TradeUtil;
import com.honglu.future.widget.popupwind.PositionPopWind;

/**
 * 平仓 dialog
 * Created by zhuaibing on 2017/11/6
 */

public class CloseTransactionDialog extends Dialog implements View.OnClickListener {
    private Context mContext;

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
    private double mLowerLimitPrice; //跌停板价
    private double mUpperLimitPrice; //涨停板价
    private int mMaxCloseTradeNum; //最大平仓手数
    private double mExLastPrice;


    public interface OnPostCloseClickListener {
        void onPostCloseClick(String todayPosition, String orderNumber, String type, String price, String insId, String avgPrice);
    }

    private OnPostCloseClickListener mListener;

    public void setOnPostCloseClickListener(OnPostCloseClickListener listener) {
        mListener = listener;
    }

    public CloseTransactionDialog(@NonNull Context context) {
        super(context, R.style.DateDialog);
        this.mContext = context;
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

    public void showDialog(HoldPositionBean holdPositionBean ,ProductListBean productListBean) {
        show();
        this.mHoldPositionBean = holdPositionBean;
        this.mProductListBean = productListBean;
        this.mLowerLimitPrice = getDouble(mProductListBean.getLowerLimitPrice());
        this.mUpperLimitPrice = getDouble(mProductListBean.getUpperLimitPrice());
        this.mMaxCloseTradeNum = TradeUtil.getMaxCloseTradeNum(mHoldPositionBean.getPosition() ,mHoldPositionBean.getYdPosition(),mHoldPositionBean.getTodayPosition(),mHoldPositionBean.getExcode());

        mName.setText(holdPositionBean.getInstrumentName());

        if (Constant.TYPE_BUY_DOWN == holdPositionBean.getType()) {
            mBuyRise.setTextColor(mContext.getResources().getColor(R.color.color_FB4F4F));
            mBuyRise.setText("买跌" + mMaxCloseTradeNum + "手");
        } else {
            mBuyRise.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
            mBuyRise.setText("买涨" + mMaxCloseTradeNum + "手");
        }

        mChicangAveragePrice.setText(holdPositionBean.getHoldAvgPrice());

        mPrice.setText(mProductListBean.getLastPrice());

        mCloseHands.setText("平仓手数（最多" +mMaxCloseTradeNum + "手）");

        mSize.setText(String.valueOf(mMaxCloseTradeNum));

        mPricePrompt.setText("≥" + mLowerLimitPrice+ " 跌停价 且 ≤" + mUpperLimitPrice + "涨停价");

        //平仓手续费
        String closeTradePrice = getCloseTradePrice(mMaxCloseTradeNum);
        mCloseTransactionPrice.setText("￥" + closeTradePrice);

        //实际盈亏
        double actualProfitLoss = getActualProfitLoss(mMaxCloseTradeNum);
        if (actualProfitLoss > 0){
            mCankaoProfitLoss.setTextColor(mContext.getResources().getColor(R.color.color_FB4F4F));
            mCankaoProfitLoss.setText("￥" + actualProfitLoss);
        }else {
            mBuyRise.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
            mCankaoProfitLoss.setText("￥" + actualProfitLoss);
        }

        //平仓盈亏
        double closeProfitLoss = getCloseProfitLoss(mMaxCloseTradeNum);
        if (closeProfitLoss > 0){
            mProfitLoss.setTextColor(mContext.getResources().getColor(R.color.color_FB4F4F));
            mProfitLoss.setText("￥" + closeProfitLoss);
        }else {
            mProfitLoss.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
            mProfitLoss.setText("￥" + closeProfitLoss);
        }
        boolean mClosed = "2".equals(mProductListBean.getIsClosed());
        if (mClosed){
            mFastCloseTransaction.setEnabled(false);
            mFastCloseTransaction.setText("休市中");
            mFastCloseTransaction.setBackgroundResource(R.color.color_B1B1B3);
        }else {
            mFastCloseTransaction.setEnabled(true);
            mFastCloseTransaction.setText("快速平仓");
            if (mHoldPositionBean.getType() == Constant.TYPE_BUY_DOWN){
                mFastCloseTransaction.setBackgroundResource(R.color.color_2CC593);
            }else {
                mFastCloseTransaction.setBackgroundResource(R.color.color_FB4F4F);
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
                if (delPrice - mLowerLimitPrice > 1) {
                    delPrice--;
                    mPrice.setText(String.valueOf(delPrice));
                }
                break;
            case R.id.iv_price_add://平仓委托价 +
                double addPrice = getPrice(mPrice);
                if (mUpperLimitPrice - addPrice > 1) {
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
                //快速平仓
                mListener.onPostCloseClick(String.valueOf(mHoldPositionBean.getTodayPosition()),
                        mSize.getText().toString(),
                        String.valueOf(mHoldPositionBean.getType()),
                        mPrice.getText().toString(),
                        mHoldPositionBean.getInstrumentId(),
                        mHoldPositionBean.getHoldAvgPrice());
                break;
            case R.id.tv_name:
                TradeTipDialog tipDialog = new TradeTipDialog(mContext, R.layout.layout_close_position_tip);
                tipDialog.show();
                break;
        }
    }

     private void setOrdeListener(){
         mPrice.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {
             }

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {
             }

             @Override
             public void afterTextChanged(Editable s) {
                 double price = getPrice(mPrice);
                 if (price >= mLowerLimitPrice && price <= mUpperLimitPrice){
                     mExLastPrice = price;
                 }else {
                     mPrice.setText(String.valueOf(mExLastPrice));
                 }
             }
         });
     }


    private int getSize(EditText text) {
        return !TextUtils.isEmpty(text.getText().toString()) ? Integer.parseInt(mSize.getText().toString()) : 0;
    }

    private double getPrice(EditText text) {
        return !TextUtils.isEmpty(text.getText().toString()) ? Double.parseDouble(mPrice.getText().toString()) : 0;
    }

    private double getDouble(String num){
        if (!TextUtils.isEmpty(num)){
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
    private String getCloseTradePrice(int tradeNum) {
        //String closeTodayRatioByMoney,String bidPrice1,String askPrice1,int volumeMultiple,String closeTodayRatioByVolume
        // ,String closeRatioByMoney,String closeRatioByVolume, int todayPosition , int type , int tradeNum ,String excode
        String closeTodayRatioByMoney = mProductListBean.getCloseTodayRatioByMoney();
        String bidPrice1 = mProductListBean.getBidPrice1();
        String askPrice1 = mProductListBean.getAskPrice1();
        int volumeMultiple = mProductListBean.getVolumeMultiple();
        String closeTodayRatioByVolume = mProductListBean.getCloseTodayRatioByVolume();
        String closeRatioByMoney = mProductListBean.getCloseRatioByMoney();
        String closeRatioByVolume = mProductListBean.getCloseRatioByVolume();
        int todayPosition = mHoldPositionBean.getTodayPosition();
        int type = mHoldPositionBean.getType();
        String excode = mHoldPositionBean.getExcode();
        try {
            double closeTradePrice  = TradeUtil.getCloseTradePrice(closeTodayRatioByMoney,bidPrice1,askPrice1,volumeMultiple,closeTodayRatioByVolume,closeRatioByMoney,closeRatioByVolume,todayPosition,type,tradeNum,excode);
            return String.valueOf(closeTradePrice);
        }catch (Exception e){
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
    private double getActualProfitLoss(int tradeNum) {
        //String openAvgPrice,String bidPrice1,String askPrice1,String priceTick,int volumeMultiple,int type ,int tradeNum
        String openAvgPrice = mHoldPositionBean.getOpenAvgPrice();
        String bidPrice1 = mProductListBean.getBidPrice1();
        String askPrice1 = mProductListBean.getAskPrice1();
        String priceTick = mProductListBean.getPriceTick();
        int volumeMultiple = mProductListBean.getVolumeMultiple();
        int type = mHoldPositionBean.getType();
        try {
            return TradeUtil.getActualProfitLoss(openAvgPrice,bidPrice1,askPrice1,priceTick,volumeMultiple,type,tradeNum);
        }catch (Exception e){
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
    private double getCloseProfitLoss(int tradeNum) {
        //String holdAvgPrice ,String askPrice1 ,String bidPrice1 ,String priceTick ,int volumeMultiple ,int type ,int tradeNum
        String holdAvgPrice = mHoldPositionBean.getHoldAvgPrice();
        String askPrice1 = mProductListBean.getAskPrice1();
        String bidPrice1 = mProductListBean.getBidPrice1();
        String priceTick = mProductListBean.getPriceTick();
        int volumeMultiple = mProductListBean.getVolumeMultiple();
        int type = mHoldPositionBean.getType();
        try {
            return   TradeUtil.getCloseProfitLoss(holdAvgPrice,askPrice1,bidPrice1,priceTick,volumeMultiple,type,tradeNum);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

}
