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
        mBuildTransactionPresenter.getProductDetail(mInstrumentId);
    }

    private void showDialogData(ProductListBean bean) {
        mProductListBean = bean;
        TextView name = (TextView) findViewById(R.id.tv_name);
        name.setText(mProductListBean.getInstrumentName());
        mTvRise = (TextView) findViewById(R.id.tv_rise);
        mTvRise.setText(mProductListBean.getLastPrice());
        mTvRise.setOnClickListener(this);
        mTvDown = (TextView) findViewById(R.id.tv_down);
        mTvDown.setText(String.valueOf(Double.valueOf(mProductListBean.getLastPrice()) - 1));
        mTvDown.setOnClickListener(this);
        TextView riseRadio = (TextView) findViewById(R.id.tv_rise_radio);
        riseRadio.setText(mProductListBean.getLongRate() + "%");
        TextView downRadio = (TextView) findViewById(R.id.tv_down_radio);
        downRadio.setText(mProductListBean.getShortRate() + "%");
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
                TradeTipDialog tradeTipDialog = new TradeTipDialog(mContext,R.layout.layout_trade_tip_pop_window);
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
