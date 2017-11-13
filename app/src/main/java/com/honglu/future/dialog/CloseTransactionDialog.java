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
import com.honglu.future.ui.trade.bean.HoldPositionBean;
import com.honglu.future.widget.popupwind.PositionPopWind;

/**
 * 平仓 dialog
 * Created by zhuaibing on 2017/11/6
 */

public class CloseTransactionDialog extends Dialog implements View.OnClickListener {
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
    private HoldPositionBean mBean;

    private Context mContext;

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

        mPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s == null || s.length() <= 0 || s.length() == 1 && Integer.parseInt(s.toString()) <= 0) {
                    mPrice.setText("1");
                }
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
                if (s == null || s.length() <= 0 || s.length() == 1 && Integer.parseInt(s.toString()) <= 0) {
                    mSize.setText("1");
                }
            }
        });
    }

    public void showDialog(HoldPositionBean bean) {
        mBean = bean;
        show();
        mName.setText(bean.getInstrumentName());
        if (bean.getType() == 1) {
            mBuyRise.setText("买跌" + bean.getPosition() + "手");
        } else {
            mBuyRise.setText("买涨" + bean.getPosition() + "手");
        }
        mChicangAveragePrice.setText(bean.getHoldAvgPrice());
        mPrice.setText(bean.getSettlementPrice());
        mCloseHands.setText("平仓手数（最多" + bean.getPosition() + "手）");
        mSize.setText(String.valueOf(bean.getPosition()));
        mCloseTransactionPrice.setText("￥" + bean.getSxf());
        mCankaoProfitLoss.setText("￥" + bean.getTotalProfit());
        mProfitLoss.setText("￥" + bean.getTotalProfit());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.iv_price_del: //平仓委托价 -
                int delPrice = getPrice();
                if (delPrice > 1) {
                    delPrice--;
                    mPrice.setText(delPrice + "");
                }
                break;
            case R.id.iv_price_add://平仓委托价 +
                int addPrice = getPrice();
                if (addPrice < 1000000) {
                    addPrice++;
                    mPrice.setText(addPrice + "");
                }
                break;
            case R.id.iv_size_del://平仓手数 -
                int delSize = getSize();
                if (delSize > 1) {
                    delSize--;
                    mSize.setText(delSize + "");
                }
                break;
            case R.id.iv_size_add: //平仓手数 +
                int addSize = getSize();
                if (addSize < 1000000) {
                    addSize++;
                    mSize.setText(addSize + "");
                }
                break;
            case R.id.tv_fast_close_transaction:
                //快速平仓
                mListener.onPostCloseClick(String.valueOf(mBean.getTodayPosition()),
                        mSize.getText().toString(),
                        String.valueOf(mBean.getType()),
                        mPrice.getText().toString(),
                        mBean.getInstrumentId(),
                        mBean.getHoldAvgPrice());
                break;
            case R.id.tv_name:
                TradeTipDialog tipDialog = new TradeTipDialog(mContext, R.layout.layout_close_position_tip);
                tipDialog.show();
                break;
        }
    }


    private int getSize() {
        return !TextUtils.isEmpty(mSize.getText().toString()) ? Integer.parseInt(mSize.getText().toString()) : 0;
    }

    private int getPrice() {
        return !TextUtils.isEmpty(mPrice.getText().toString()) ? Integer.parseInt(mPrice.getText().toString()) : 0;
    }
}
