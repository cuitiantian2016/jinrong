package com.honglu.future.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.honglu.future.config.Constant;
import com.honglu.future.ui.main.contract.BuildTransactionContract;
import com.honglu.future.ui.main.presenter.BuildTransactionPresenter;
import com.honglu.future.ui.recharge.activity.InAndOutGoldActivity;
import com.honglu.future.ui.trade.bean.ProductListBean;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ViewUtil;

import static com.honglu.future.util.ToastUtil.showToast;

/**
 * Created by zq on 2017/11/10.
 */

public class BuildTransactionDialog extends Dialog implements View.OnClickListener, BuildTransactionContract.View {
    public static final String TRADE_BUY_RISE = "TRADE_BUY_RISE";
    public static final String TRADE_BUY_DOWN = "TRADE_BUY_DOWN";
    private AppCompatActivity mContext;
    private int mScreenHeight;
    private String mBuyRiseOrDown;
    private ProductListBean mProductListBean;
    private TextView mTvRise, mTvDown;
    private String mBuyType;
    private EditText mHands, mPrice;
    private BuildTransactionPresenter mBuildTransactionPresenter;
    private String mInstrumentId;
    private TextView mTotal;
    private int mHandsNum;
    private int mVolumeMultiple;
    private float mFeeRate;
    private TextView marginMoney;
    private TextView sxf;
    private boolean mIsStopChangePrice = false;
    private float mLowestPrice, mHighestprice;
    private int mMinHands, mMaxHands;
    private float mChangedPrice;

    public BuildTransactionDialog(@NonNull Context context, String buyRiseOrDown, String instrumentId) {
        super(context, R.style.DateDialog);
        this.mContext = (AppCompatActivity) context;
        mBuyRiseOrDown = buyRiseOrDown;
        mInstrumentId = instrumentId;
        mBuildTransactionPresenter = new BuildTransactionPresenter();
        mBuildTransactionPresenter.init(this);
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
        ImageView close = (ImageView) findViewById(R.id.iv_close_popup);
        close.setOnClickListener(this);
        ImageView tip = (ImageView) findViewById(R.id.iv_open_account_tip);
        tip.setOnClickListener(this);
        mBuildTransactionPresenter.getProductDetail(mInstrumentId);
    }

    /**
     * @param askPrice1 买涨价
     * @param bidPrice1 买跌价
     */
    public void pushRefresh(String askPrice1, String bidPrice1) {
        if (!mIsStopChangePrice) {
            if (mTvRise != null) {
                mTvRise.setText(askPrice1);
            }

            if (mTvDown != null) {
                mTvDown.setText(bidPrice1);
            }

            if (mPrice != null) {
                if (mBuyRiseOrDown.equals(TRADE_BUY_RISE)) {
                    mPrice.setText(askPrice1);
                } else {
                    mPrice.setText(bidPrice1);
                }
            }

            String bzj = "";
            if (marginMoney != null) {
                bzj = getBzjStr(mHandsNum, askPrice1, bidPrice1);
                marginMoney.setText("￥" + bzj);
            }

            String sxfStr = "";
            if (sxf != null) {
                sxfStr = getSxfStr(mHandsNum, askPrice1, bidPrice1);
                sxf.setText("￥" + sxfStr);
            }

            if (mTotal != null) {
                mTotal.setText("￥" + (Float.valueOf(sxfStr) + Float.valueOf(bzj)));
            }
        }
    }

    private void showDialogData(ProductListBean bean) {
        mProductListBean = bean;
        mVolumeMultiple = bean.getVolumeMultiple();
        mFeeRate = mBuyRiseOrDown.equals(TRADE_BUY_DOWN) ? Float.valueOf(bean.getOpenRatioByVolume()) : Float.valueOf(bean.getOpenRatioByMoney());
        mLowestPrice = Float.valueOf(bean.getLowerLimitPrice());
        mHighestprice = Float.valueOf(bean.getUpperLimitPrice());
        mMinHands = bean.getMinSl();
        mMaxHands = bean.getMaxSl();

        TextView name = (TextView) findViewById(R.id.tv_name);
        name.setText(mProductListBean.getInstrumentName());
        mTvRise = (TextView) findViewById(R.id.tv_rise);
        mTvRise.setText(mProductListBean.getAskPrice1());
        mTvRise.setOnClickListener(this);
        mTvDown = (TextView) findViewById(R.id.tv_down);
        mTvDown.setText(mProductListBean.getBidPrice1());
        mTvDown.setOnClickListener(this);
        TextView riseRadio = (TextView) findViewById(R.id.tv_rise_radio);
        riseRadio.setText(mProductListBean.getLongRate() + "%");
        TextView downRadio = (TextView) findViewById(R.id.tv_down_radio);
        downRadio.setText((100 - Integer.valueOf(mProductListBean.getLongRate())) + "%");
        mPrice = (EditText) findViewById(R.id.amountView);
        mHands = (EditText) findViewById(R.id.av_hands);
        mHands.setText(String.valueOf(mProductListBean.getMinSl()));
        mHandsNum = Integer.valueOf(mHands.getText().toString());
        if (mBuyRiseOrDown.equals(TRADE_BUY_RISE)) {
            mBuyType = "2";
            mPrice.setText(bean.getAskPrice1());
            mTvDown.setBackgroundResource(R.drawable.rise_down_bg_block);
            mTvDown.setTextColor(mContext.getResources().getColor(R.color.color_151515));
        } else if (mBuyRiseOrDown.equals(TRADE_BUY_DOWN)) {
            mBuyType = "1";
            mPrice.setText(bean.getBidPrice1());
            mTvRise.setBackgroundResource(R.drawable.rise_down_bg_block);
            mTvRise.setTextColor(mContext.getResources().getColor(R.color.color_151515));
        }

        TextView mBuild = (TextView) findViewById(R.id.btn_fast_open);
        mBuild.setOnClickListener(this);
        TextView isClose = (TextView) findViewById(R.id.tv_closed);
        if (bean.getIsClosed().equals("2")) {
            isClose.setVisibility(View.VISIBLE);
            mBuild.setBackgroundResource(R.color.color_CACCCB);
            mBuild.setText("休市中");
            mBuild.setClickable(false);
        } else {
            isClose.setVisibility(View.GONE);
            mBuild.setBackgroundResource(R.color.color_2CC593);
            mBuild.setText("快速建仓");
            mBuild.setClickable(true);
        }

        TextView limitPrice = (TextView) findViewById(R.id.tv_limit_price);
        limitPrice.setText("≥" + mProductListBean.getLowerLimitPrice() + " 跌停价 且 ≤" + mProductListBean.getUpperLimitPrice() + " 涨停价");
        TextView useAbleMoney = (TextView) findViewById(R.id.tv_use_able_money);
        useAbleMoney.setText(SpUtil.getString(Constant.CACHE_USER_ASSES));
        marginMoney = (TextView) findViewById(R.id.tv_margin_money);
        String bzj = getBzjStr(mHandsNum, bean.getAskPrice1(), bean.getBidPrice1());
        marginMoney.setText("￥" + bzj);

        sxf = (TextView) findViewById(R.id.tv_sxf);
        String sxfStr = getSxfStr(mHandsNum, bean.getAskPrice1(), bean.getBidPrice1());
        sxf.setText("￥" + sxfStr);

        mTotal = (TextView) findViewById(R.id.tv_total);
        mTotal.setText("￥" + (Float.valueOf(sxfStr) + Float.valueOf(bzj)));

        TextView goRecharge = (TextView) findViewById(R.id.btn_go_recharge);
        goRecharge.setOnClickListener(this);

        mPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //mIsStopChangePrice = true;
                if (s.length() < 1) {
                    mChangedPrice = mLowestPrice;
                    mPrice.setText(String.valueOf(mLowestPrice));
                } else {
                    mChangedPrice = Float.valueOf(mPrice.getText().toString());
                    if (mChangedPrice > mHighestprice) {
                        mChangedPrice = mHighestprice;
                        mPrice.setText(String.valueOf(mHighestprice));
                    }
                }
                calResult();
            }
        });
        mHands.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 1) {
                    mHandsNum = mMinHands;
                    mHands.setText(String.valueOf(mMinHands));
                } else {
                    mHandsNum = Integer.valueOf(mHands.getText().toString());
                    if (mHandsNum > mMaxHands) {
                        mHandsNum = mMaxHands;
                        mHands.setText(String.valueOf(mMaxHands));
                    }
                }
                calResult();

            }
        });
    }

    private void calResult() {
        String bzj = getBzjStr(mHandsNum, String.valueOf(mChangedPrice), String.valueOf(mChangedPrice));
        marginMoney.setText("￥" + bzj);

        String sxfStr = getSxfStr(mHandsNum, String.valueOf(mChangedPrice), String.valueOf(mChangedPrice));
        sxf.setText("￥" + sxfStr);
        mTotal.setText("￥" + (Float.valueOf(sxfStr) + Float.valueOf(bzj)));
    }

    private String getBzjStr(int hands, String askPrice1, String bidPrice1) {
        //float rate = mBuyType.equals("1") ? Float.valueOf(bean.getShortMarginRatioByMoney()) : Float.valueOf(bean.getLongMarginRatioByMoney());
        float price = mBuyType.equals("1") ? Float.valueOf(askPrice1) : Float.valueOf(bidPrice1);
        return String.valueOf(hands * 0.15 * price * mVolumeMultiple);
    }

    private String getSxfStr(int hands, String askPrice1, String bidPrice1) {
        float price = mBuyType.equals("1") ? Float.valueOf(askPrice1) : Float.valueOf(bidPrice1);
        return String.valueOf(mFeeRate * hands * mVolumeMultiple * price);
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
                TradeTipDialog tradeTipDialog = new TradeTipDialog(mContext, R.layout.layout_trade_tip_pop_window);
                tradeTipDialog.show();
                break;
            case R.id.btn_fast_open:
                String buyTypeStr;
                if (mBuyType.equals("1")) {
                    buyTypeStr = "买跌";
                } else {
                    buyTypeStr = "买涨";
                }

                new AlertFragmentDialog.Builder(mContext).setTitle("确认建仓").setContent(mProductListBean.getInstrumentName() + " " + buyTypeStr + " " + mHands.getText().toString() + "手 总计 " + mTotal.getText().toString())
                        .setRightBtnText("确定")
                        .setLeftBtnText("取消")
                        .setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                            @Override
                            public void dialogRightBtnClick(String string) {
                                mBuildTransactionPresenter.buildTransaction(mHands.getText().toString(),
                                        mBuyType,
                                        mPrice.getText().toString(),
                                        mProductListBean.getInstrumentId(),
                                        SpUtil.getString(Constant.CACHE_TAG_UID),
                                        SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN),
                                        "GUOFU"
                                );
                            }
                        }).build();
                break;
            case R.id.btn_go_recharge:
                InAndOutGoldActivity.startInAndOutGoldActivity(mContext, 0);
                break;
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
        App.hideLoading();
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        showToast(msg);
    }

    @Override
    public void buildTransactionSuccess() {
        dismiss();
    }

    @Override
    public void getProductDetailSuccess(ProductListBean bean) {
        showDialogData(bean);
    }
}
