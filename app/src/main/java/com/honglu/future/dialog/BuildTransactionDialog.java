package com.honglu.future.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

    private void showDialogData(ProductListBean bean) {
        mProductListBean = bean;
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
        downRadio.setText((100 - Integer.valueOf(mProductListBean.getShortRate())) + "%");
        mPrice = (EditText) findViewById(R.id.amountView);
        mPrice.setText(mProductListBean.getLastPrice());
        mHands = (EditText) findViewById(R.id.av_hands);
        mHands.setText(String.valueOf(mProductListBean.getMinSl()));
        if (mBuyRiseOrDown.equals(TRADE_BUY_RISE)) {
            mBuyType = "2";
            mTvDown.setBackgroundResource(R.drawable.rise_down_bg_block);
            mTvDown.setTextColor(mContext.getResources().getColor(R.color.color_151515));
        } else if (mBuyRiseOrDown.equals(TRADE_BUY_DOWN)) {
            mBuyType = "1";
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
        TextView marginMoney = (TextView) findViewById(R.id.tv_margin_money);
        String bzj = getBzjStr(mHands.getText().toString(), bean);
        marginMoney.setText("￥" + bzj);

        TextView sxf = (TextView) findViewById(R.id.tv_sxf);
        String sxfStr = getSxfStr(mHands.getText().toString(), bean);
        sxf.setText("￥" + sxfStr);

        TextView total = (TextView) findViewById(R.id.tv_total);
        total.setText("￥" + (Float.valueOf(sxfStr) + Float.valueOf(bzj)));

        TextView goRecharge = (TextView)findViewById(R.id.btn_go_recharge);
        goRecharge.setOnClickListener(this);
    }

    private String getBzjStr(String hands, ProductListBean bean) {
        //float rate = mBuyType.equals("1") ? Float.valueOf(bean.getShortMarginRatioByMoney()) : Float.valueOf(bean.getLongMarginRatioByMoney());
        float price = mBuyType.equals("1") ? Float.valueOf(bean.getAskPrice1()) : Float.valueOf(bean.getBidPrice1());
        return String.valueOf(Integer.valueOf(hands) * 0.15 * price * bean.getVolumeMultiple());
    }

    private String getSxfStr(String hands, ProductListBean bean) {
        float feeRate = mBuyType.equals("1") ? Float.valueOf(bean.getOpenRatioByVolume()) : Float.valueOf(bean.getOpenRatioByMoney());
        float price = mBuyType.equals("1") ? Float.valueOf(bean.getAskPrice1()) : Float.valueOf(bean.getBidPrice1());
        return String.valueOf(feeRate * Integer.valueOf(hands) * bean.getVolumeMultiple() * price);
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

                // TODO: 2017/11/10 总计需要计算获取，目前是写死
                new AlertFragmentDialog.Builder(mContext).setTitle("确认建仓").setContent(mProductListBean.getInstrumentName() + " " + buyTypeStr + " " + mHands.getText().toString() + "手 总计 ¥2511.68")
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
