package com.honglu.future.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.ui.trade.bean.ProductListBean;
import com.honglu.future.util.ViewUtil;

/**
 * Created by zq on 2017/11/10.
 */

public class BuildTransactionDialog extends Dialog implements View.OnClickListener {
    public static final String TRADE_BUY_RISE = "TRADE_BUY_RISE";
    public static final String TRADE_BUY_DOWN = "TRADE_BUY_DOWN";
    private Context mContext;
    private int mScreenHeight;
    private String mBuyRiseOrDown;
    private ProductListBean mProductListBean;
    private TextView mTvRise, mTvDown;
    private String mBuyType;
    private EditText mHands, mPrice;

    public interface OnBuildClickListener {
        void onBuildClick(ProductListBean bean, String type, String hands, String price);
    }

    private OnBuildClickListener mListener;

    public void setOnBuildClickListener(OnBuildClickListener listener) {
        mListener = listener;
    }


    public BuildTransactionDialog(@NonNull Context context, String buyRiseOrDown, ProductListBean bean) {
        super(context, R.style.DateDialog);
        this.mContext = context;
        mBuyRiseOrDown = buyRiseOrDown;
        mProductListBean = bean;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_transaction_popup_window);
        mScreenHeight = ViewUtil.getScreenHeight(mContext);
        Window mWindow = this.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        WindowManager manage = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        params.width = manage.getDefaultDisplay().getWidth();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        mWindow.setAttributes(params);
        setCanceledOnTouchOutside(false);
        initTransactionData();
    }

    private void initTransactionData() {
        TextView name = (TextView) findViewById(R.id.tv_name);
        name.setText(mProductListBean.getInstrumentName());
        mTvRise = (TextView) findViewById(R.id.tv_rise);
        mTvRise.setText(mProductListBean.getLastPrice());
        mTvRise.setOnClickListener(this);
        mTvDown = (TextView) findViewById(R.id.tv_down);
        mTvDown.setText(String.valueOf(Double.valueOf(mProductListBean.getLastPrice()) - 1));
        mTvDown.setOnClickListener(this);
        TextView riseRadio = (TextView) findViewById(R.id.tv_rise_radio);
        riseRadio.setText(mProductListBean.getLongRate());
        TextView downRadio = (TextView) findViewById(R.id.tv_down_radio);
        downRadio.setText(mProductListBean.getShortRate());
        mPrice = (EditText) findViewById(R.id.amountView);
        mPrice.setText(mProductListBean.getLastPrice());
        mHands = (EditText) findViewById(R.id.av_hands);
        mHands.setText(String.valueOf(mProductListBean.getMinSl()));
        ImageView close = (ImageView) findViewById(R.id.iv_close_popup);
        close.setOnClickListener(this);
        if (mBuyRiseOrDown.equals(TRADE_BUY_RISE)) {
            mBuyType = "2";
            mTvDown.setBackgroundResource(R.drawable.rise_down_bg_block);
            mTvDown.setTextColor(mContext.getResources().getColor(R.color.color_151515));
        } else if (mBuyRiseOrDown.equals(TRADE_BUY_DOWN)) {
            mBuyType = "1";
            mTvRise.setBackgroundResource(R.drawable.rise_down_bg_block);
            mTvRise.setTextColor(mContext.getResources().getColor(R.color.color_151515));
        }
        ImageView tip = (ImageView) findViewById(R.id.iv_open_account_tip);
        tip.setOnClickListener(this);
        TextView mBuild = (TextView) findViewById(R.id.btn_fast_open);
        mBuild.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close_popup:
                dismiss();
                break;
            case R.id.tv_rise:
                mBuyType = "2";
                mTvDown.setBackgroundResource(R.drawable.rise_down_bg_block);
                mTvDown.setTextColor(mContext.getResources().getColor(R.color.color_151515));
                mTvRise.setBackgroundResource(R.drawable.bg_buy_rise);
                mTvRise.setTextColor(Color.WHITE);
                break;
            case R.id.tv_down:
                mBuyType = "1";
                mTvRise.setBackgroundResource(R.drawable.rise_down_bg_block);
                mTvRise.setTextColor(mContext.getResources().getColor(R.color.color_151515));
                mTvDown.setBackgroundResource(R.drawable.bg_buy_down);
                mTvDown.setTextColor(Color.WHITE);
                break;
            case R.id.iv_open_account_tip:
                TradeTipDialog tradeTipDialog = new TradeTipDialog(mContext);
                tradeTipDialog.show();
                break;
            case R.id.btn_fast_open:
                mListener.onBuildClick(mProductListBean, mBuyType, mHands.getText().toString(), mPrice.getText().toString());
                break;
        }
    }

}
