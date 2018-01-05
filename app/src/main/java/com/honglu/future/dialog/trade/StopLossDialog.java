package com.honglu.future.dialog.trade;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.ui.trade.contract.StopLossContract;
import com.honglu.future.ui.trade.presenter.StopLossPresenter;

/**
 * Created by zq on 2018/1/5.
 */

public class StopLossDialog extends Dialog implements View.OnClickListener, StopLossContract.View, CompoundButton.OnCheckedChangeListener {
    private AppCompatActivity mContext;
    private StopLossPresenter mStopLossPresenter;
    private static StopLossDialog dialog = null;
    private TextView mTvHands, mTvHoldPrice, mTvLastPrice, mTvHoldPl, mTvMaxHands;
    private TextView mTvProfitCf, mTvProfitJy, mTvProfit, mTvLossCf;
    private ImageView mIvHnadsReduce, mIvHandsAdd, mIvProfitReduce, mIvProfitAdd;
    private ImageView mIvLossReduce, mIvLossAdd;
    private EditText mEtHands, mEtProfitPrice, mEtLossPricr;
    private CheckBox mProfitCheck, mLossCheck;
    private TextView mTvLossJy, mTvLoss, mTvSubmit, mTvName;
    private ImageView mIvCloseDialog;

    public static StopLossDialog getInstance(Context context) {
        if (dialog == null) {
            synchronized (StopLossDialog.class) {
                if (dialog == null) {
                    dialog = new StopLossDialog(context);
                }
            }
        }
        return dialog;
    }

    public StopLossDialog(@NonNull Context context) {
        super(context, R.style.DateDialog);
        this.mContext = (AppCompatActivity) context;
        mStopLossPresenter = new StopLossPresenter();
        mStopLossPresenter.init(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stop_loss_dialog);
        Window mWindow = this.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        WindowManager manage = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        params.width = manage.getDefaultDisplay().getWidth();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        mWindow.setAttributes(params);
        setCanceledOnTouchOutside(false);
        initViews();
    }

    private void initViews() {
        mTvHands = (TextView) findViewById(R.id.tv_hands);
        mTvHoldPrice = (TextView) findViewById(R.id.tv_hold_price);
        mTvLastPrice = (TextView) findViewById(R.id.tv_last_price);
        mTvHoldPl = (TextView) findViewById(R.id.tv_hold_pl);
        mTvMaxHands = (TextView) findViewById(R.id.tv_max_hands);
        mIvHnadsReduce = (ImageView) findViewById(R.id.btn_deal_reduce);
        mIvHandsAdd = (ImageView) findViewById(R.id.btn_deal_add);
        mEtHands = (EditText) findViewById(R.id.amountView);
        mProfitCheck = (CheckBox) findViewById(R.id.check_box);
        mTvProfitCf = (TextView) findViewById(R.id.tv_profit_cf);
        mIvProfitReduce = (ImageView) findViewById(R.id.btn_profit_reduce);
        mEtProfitPrice = (EditText) findViewById(R.id.profit_price);
        mIvProfitAdd = (ImageView) findViewById(R.id.btn_profit_add);
        mTvProfitJy = (TextView) findViewById(R.id.tv_profit_jy);
        mTvProfit = (TextView) findViewById(R.id.tv_profit);
        mLossCheck = (CheckBox) findViewById(R.id.check_box_loss);
        mTvLossCf = (TextView) findViewById(R.id.tv_loss_cf);
        mIvLossReduce = (ImageView) findViewById(R.id.btn_loss_reduce);
        mEtLossPricr = (EditText) findViewById(R.id.loss_price);
        mIvLossAdd = (ImageView) findViewById(R.id.btn_loss_add);
        mTvLossJy = (TextView) findViewById(R.id.tv_loss_jy);
        mTvLoss = (TextView) findViewById(R.id.tv_loss);
        mTvSubmit = (TextView) findViewById(R.id.tv_submit);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mIvCloseDialog = (ImageView) findViewById(R.id.iv_close_popup);
        mIvCloseDialog.setOnClickListener(this);
        mProfitCheck.setOnCheckedChangeListener(this);
        mLossCheck.setOnCheckedChangeListener(this);
        mIvHnadsReduce.setOnClickListener(this);
        mIvHandsAdd.setOnClickListener(this);
        mIvProfitReduce.setOnClickListener(this);
        mIvProfitAdd.setOnClickListener(this);
        mIvLossReduce.setOnClickListener(this);
        mIvLossAdd.setOnClickListener(this);
        mTvSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close_popup:
                dismiss();
                break;
            case R.id.btn_deal_reduce:
                break;
            case R.id.btn_deal_add:
                break;
            case R.id.btn_profit_reduce:
                break;
            case R.id.btn_profit_add:
                break;
            case R.id.btn_loss_reduce:
                break;
            case R.id.btn_loss_add:
                break;
            case R.id.tv_submit:
                break;
        }
    }

    @Override
    public void showLoading(String content) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorMsg(String msg, String type) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.check_box:
                break;
            case R.id.check_box_loss:
                break;
        }
    }
}
