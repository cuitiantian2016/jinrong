package com.honglu.future.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
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
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.NumberUtil;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.StringUtil;
import com.honglu.future.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;

import static com.honglu.future.util.ToastUtil.showToast;

/**
 * Created by zq on 2017/11/10.
 */

public class BuildTransactionDialog extends Dialog implements View.OnClickListener, BuildTransactionContract.View {
    public static final String TRADE_BUY_RISE = "TRADE_BUY_RISE";
    public static final String TRADE_BUY_DOWN = "TRADE_BUY_DOWN";
    private AppCompatActivity mContext;
    private String mBuyRiseOrDown;
    private ProductListBean mProductListBean;
    private TextView mTvRise, mTvDown;
    private String mBuyType;
    private EditText mHands, mPrice;
    private BuildTransactionPresenter mBuildTransactionPresenter;
    private TextView mTotal;
    private TextView marginMoney;
    private TextView sxf;
    private TextView text_create_tips;
    private boolean mIsStopChangePrice;
    private ImageView mReducePrice, mAddPrice;
    private ImageView mReduceHands, mAddHands;
    private TextView mBuild;
    private TextView isClose;
    private String instrumentName;
    private String instrumentId;
    private String lowerLimitPrice;
    private String upperLimitPrice;
    private String priceTick;
    private int volumeMultiple;
    private int maxSl;
    private int minSl;
    private String isClosed;

    public BuildTransactionDialog(@NonNull Context context, String buyRiseOrDown, ProductListBean bean) {
        super(context, R.style.DateDialog);
        this.mContext = (AppCompatActivity) context;
        mBuyRiseOrDown = buyRiseOrDown;
        mProductListBean = bean;
        instrumentName = bean.getInstrumentName();
        instrumentId = bean.getInstrumentId();
        lowerLimitPrice = bean.getLowerLimitPrice();
        upperLimitPrice = bean.getUpperLimitPrice();
        priceTick = bean.getPriceTick();
        volumeMultiple = bean.getVolumeMultiple();
        maxSl = bean.getMaxSl();
        minSl = bean.getMinSl();
        isClosed = bean.getIsClosed();
        mBuildTransactionPresenter = new BuildTransactionPresenter();
        mBuildTransactionPresenter.init(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_transaction_popup_window);
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
        mBuildTransactionPresenter.getProductDetail(mProductListBean.getInstrumentId());
    }


    public void pushRefresh(ProductListBean bean) {
        if (!mIsStopChangePrice) {
            mProductListBean = bean;
            if (mTvRise != null) {
                mTvRise.setText(bean.getAskPrice1());
                mTvDown.setText(bean.getBidPrice1());
                if (mBuyType.equals("2")) {
                    mPrice.setText(bean.getAskPrice1());
                } else {
                    mPrice.setText(bean.getBidPrice1());
                }
                mPrice.setSelection(mPrice.getText().toString().length());
                setTotalMoney();
            }
        }
    }

    private void showDialogData(ProductListBean bean) {
        mProductListBean = bean;

        TextView name = (TextView) findViewById(R.id.tv_name);
        name.setText(mProductListBean.getInstrumentName());
        mTvRise = (TextView) findViewById(R.id.tv_rise);
        mTvRise.setText(String.format(mContext.getString(R.string.buy_rise), mProductListBean.getAskPrice1()));
        mTvRise.setOnClickListener(this);
        mTvDown = (TextView) findViewById(R.id.tv_down);
        mTvDown.setText(String.format(mContext.getString(R.string.buy_down), mProductListBean.getBidPrice1()));
        mTvDown.setOnClickListener(this);
        text_create_tips = (TextView) findViewById(R.id.text_create_tips);
//        TextView riseRadio = (TextView) findViewById(R.id.tv_rise_radio);
//        riseRadio.setText(mProductListBean.getLongRate() + "%");
//        TextView downRadio = (TextView) findViewById(R.id.tv_down_radio);
//        downRadio.setText((100 - Integer.valueOf(mProductListBean.getLongRate())) + "%");
        mPrice = (EditText) findViewById(R.id.amountView);
        setTextFonts(mPrice);
        mHands = (EditText) findViewById(R.id.av_hands);
        setTextFonts(mHands);
        mHands.setText(String.valueOf(mProductListBean.getMinSl()));
        mHands.setSelection(mHands.getText().toString().length());
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
        mPrice.setSelection(mPrice.getText().toString().length());

        mBuild = (TextView) findViewById(R.id.btn_fast_open);
        mBuild.setOnClickListener(this);
        isClose = (TextView) findViewById(R.id.tv_closed);
        if (bean.getIsClosed().equals("2")) {
            isClose.setVisibility(View.VISIBLE);
            mBuild.setBackgroundResource(R.color.color_CACCCB);
            mBuild.setText("休市中");
            mBuild.setClickable(false);
        } else {
            if (mBuyRiseOrDown.equals(TRADE_BUY_RISE)) {
                mBuild.setBackgroundResource(R.color.color_FB4F4F);
            } else {
                mBuild.setBackgroundResource(R.color.color_2CC593);
            }
            isClose.setVisibility(View.GONE);
            mBuild.setText("快速建仓");
            mBuild.setClickable(true);
        }

        TextView limitPrice = (TextView) findViewById(R.id.tv_limit_price);
        limitPrice.setText("≥" + mProductListBean.getLowerLimitPrice() + " 跌停价 且 ≤" + mProductListBean.getUpperLimitPrice() + " 涨停价");
        TextView useAbleMoney = (TextView) findViewById(R.id.tv_use_able_money);
        useAbleMoney.setText(SpUtil.getString(Constant.CACHE_USER_AVAILABLE_MONEY));
        marginMoney = (TextView) findViewById(R.id.tv_margin_money);
        sxf = (TextView) findViewById(R.id.tv_sxf);
        mTotal = (TextView) findViewById(R.id.tv_total);

        TextView goRecharge = (TextView) findViewById(R.id.btn_go_recharge);
        goRecharge.setOnClickListener(this);

        ykTips();
        setTotalMoney();

        mPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mIsStopChangePrice) {
                    setTextChange();
                    setTotalMoney();
                }
            }
        });

        mPrice.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (!mIsStopChangePrice) {
                        mIsStopChangePrice = true;
                        if (!"2".equals(isClosed)) {
                            mBuild.setText("委托建仓");
                        }
                    }
                }
                return false;
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
                if (TextUtils.isEmpty(mHands.getText().toString())) {
                    mHands.setText(String.valueOf(minSl));
                } else {
                    if (Integer.parseInt(mHands.getText().toString()) < minSl) {
                        mHands.setText(String.valueOf(minSl));
                    } else if (Integer.parseInt(mHands.getText().toString()) > maxSl) {
                        mHands.setText(String.valueOf(maxSl));
                    }
                }
                ykTips();
                setTotalMoney();
            }
        });

        mReducePrice = (ImageView) findViewById(R.id.btn_deal_reduce);
        mReducePrice.setOnClickListener(this);
        mAddPrice = (ImageView) findViewById(R.id.btn_deal_add);
        mAddPrice.setOnClickListener(this);
        mReduceHands = (ImageView) findViewById(R.id.btn_hands_reduce);
        mReduceHands.setOnClickListener(this);
        mAddHands = (ImageView) findViewById(R.id.btn_hands_add);
        mAddHands.setOnClickListener(this);
    }

    //设置字体样式
    private void setTextFonts(EditText editText) {
        //得到AssetManager
        AssetManager mgr = getContext().getAssets();
        //根据路径得到Typeface
        Typeface tf = Typeface.createFromAsset(mgr, "fonts/DIN-Medium.ttf");
        //设置字体
        editText.setTypeface(tf);
    }

    /**
     * 显示需要的预付款
     */
    private void setTotalMoney() {
        try {
            if (mProductListBean == null)
                return;
            if (StringUtil.isEmpty(mHands.getText().toString()))
                return;

            int shouShu = Integer.parseInt(mHands.getText().toString());
            if (shouShu <= 0) {
                shouShu = mProductListBean.getMinSl();
            }
            // 先计算一手保证金的金额
            double oneSlBZj = 0;
            if (mIsStopChangePrice) {
                oneSlBZj = NumberUtil.multiply(new BigDecimal(mProductListBean.getLongMarginRatioByMoney()).doubleValue(), new BigDecimal(mPrice.getText().toString().trim()).doubleValue()) * mProductListBean.getVolumeMultiple();
            } else {
                if (mBuyType.equals("1")) {//买跌
//                if (TextUtils.isEmpty(mProductListBean.getLongMarginRatioByMoney()) || Double.parseDouble(mProductListBean.getLongMarginRatioByMoney()) == 0) {
//                    oneSlBZj = Double.parseDouble(mProductListBean.getLongMarginRatioByVolume());
//                } else {
                    oneSlBZj = NumberUtil.multiply(new BigDecimal(mProductListBean.getShortMarginRatioByMoney()).doubleValue(), new BigDecimal(mProductListBean.getBidPrice1()).doubleValue()) * mProductListBean.getVolumeMultiple();
//                }
                } else {//买涨
//                if (TextUtils.isEmpty(mProductListBean.getShortMarginRatioByMoney()) || Double.parseDouble(mProductListBean.getShortMarginRatioByMoney()) == 0) {
//                    oneSlBZj = Double.parseDouble(mProductListBean.getShortMarginRatioByVolume());
//                } else {
                    oneSlBZj = NumberUtil.multiply(new BigDecimal(mProductListBean.getLongMarginRatioByMoney()).doubleValue(), new BigDecimal(mProductListBean.getAskPrice1()).doubleValue()) * mProductListBean.getVolumeMultiple();
//                }
                }
            }
            String totalBZJ = NumberUtil.moveLast0(NumberUtil.multiply(oneSlBZj, new BigDecimal(shouShu).doubleValue()));

            marginMoney.setText("¥" + StringUtil.forNumber(new BigDecimal(totalBZJ).doubleValue()));
            // 先计算一手手续费的金额
            double oneSlSXF = 0;
            if (TextUtils.isEmpty(mProductListBean.getOpenRatioByMoney()) || Double.parseDouble(mProductListBean.getOpenRatioByMoney()) == 0) {
                oneSlSXF = Double.parseDouble(mProductListBean.getOpenRatioByVolume());
            } else {
                if (SpUtil.getString(Constant.COMPANY_TYPE).equals(Constant.COMPANY_TYPE_GUOFU)) {
                    //国富
                    if (mIsStopChangePrice) {
                        oneSlSXF = NumberUtil.multiply(new BigDecimal(mProductListBean.getOpenRatioByMoney()).doubleValue(), new BigDecimal(mPrice.getText().toString().trim()).doubleValue()) * mProductListBean.getVolumeMultiple();
                    } else {
                        if (mBuyType.equals("1")) {//买跌
                            oneSlSXF = NumberUtil.multiply(new BigDecimal(mProductListBean.getOpenRatioByMoney()).doubleValue(), new BigDecimal(mProductListBean.getBidPrice1()).doubleValue()) * mProductListBean.getVolumeMultiple();
                        } else {
                            oneSlSXF = NumberUtil.multiply(new BigDecimal(mProductListBean.getOpenRatioByMoney()).doubleValue(), new BigDecimal(mProductListBean.getAskPrice1()).doubleValue()) * mProductListBean.getVolumeMultiple();
                        }
                    }
                } else if (SpUtil.getString(Constant.COMPANY_TYPE).equals(Constant.COMPANY_TYPE_MEIERYA)) {
                    //美尔雅
                    if (mProductListBean.getProductId().equals(Constant.PRODUCT_SPECIAL_AU) || mProductListBean.getProductId().equals(Constant.PRODUCT_SPECIAL_NI)
                            || mProductListBean.getProductId().equals(Constant.PRODUCT_SPECIAL_MA) || mProductListBean.getProductId().equals(Constant.PRODUCT_SPECIAL_M)
                            || mProductListBean.getProductId().equals(Constant.PRODUCT_SPECIAL_C)) {
                        oneSlSXF = new BigDecimal(mProductListBean.getOpenRatioByMoney()).doubleValue();
                    } else {
                        if (mIsStopChangePrice) {
                            oneSlSXF = NumberUtil.multiply(new BigDecimal(mProductListBean.getOpenRatioByMoney()).doubleValue(), new BigDecimal(mPrice.getText().toString().trim()).doubleValue()) * mProductListBean.getVolumeMultiple();
                        } else {
                            if (mBuyType.equals("1")) {//买跌
                                oneSlSXF = NumberUtil.multiply(new BigDecimal(mProductListBean.getOpenRatioByMoney()).doubleValue(), new BigDecimal(mProductListBean.getBidPrice1()).doubleValue()) * mProductListBean.getVolumeMultiple();
                            } else {
                                oneSlSXF = NumberUtil.multiply(new BigDecimal(mProductListBean.getOpenRatioByMoney()).doubleValue(), new BigDecimal(mProductListBean.getAskPrice1()).doubleValue()) * mProductListBean.getVolumeMultiple();
                            }
                        }
                    }
                }
            }
            String totalSXF = NumberUtil.moveLast0(NumberUtil.multiply(oneSlSXF, new BigDecimal(shouShu).doubleValue()));
            sxf.setText("¥" + StringUtil.forNumber(new BigDecimal(totalSXF).doubleValue()));

            String totalMoney = NumberUtil.moveLast0(NumberUtil.multiply((oneSlSXF + oneSlBZj), new BigDecimal(shouShu).doubleValue()));
            mTotal.setText("¥" + StringUtil.forNumber(new BigDecimal(totalMoney).doubleValue()));


            if (!"2".equals(isClosed)) {
                if (Double.parseDouble(totalMoney) > Double.parseDouble(SpUtil.getString(Constant.CACHE_USER_AVAILABLE_MONEY))) {
                    mBuild.setBackgroundResource(R.color.color_CACCCB);
                    mBuild.setText("余额不足");
                    mBuild.setClickable(false);
                } else {
                    setViewBG(mBuyType.equals("2"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 改变背景色
     *
     * @param isUp
     */
    private void setViewBG(boolean isUp) {
        if (isUp) {
            if (!"2".equals(isClosed)) {
                mBuild.setBackgroundResource(R.color.color_FB4F4F);
                mBuild.setClickable(true);
                if (mIsStopChangePrice) {
                    mBuild.setText("委托建仓");
                } else {
                    mBuild.setText("快速建仓");
                }
            }
        } else {
            if (!"2".equals(isClosed)) {
                mBuild.setBackgroundResource(R.color.color_2CC593);
                mBuild.setClickable(true);
                if (mIsStopChangePrice) {
                    mBuild.setText("委托建仓");
                } else {
                    mBuild.setText("快速建仓");
                }
            }
        }
    }

    /**
     * 计算每波动一个点的盈利
     */
    public void ykTips() {
        String input = mHands.getText().toString();
        if (TextUtils.isEmpty(input)) {
            return;
        }
        int shouShu = Integer.parseInt(input.trim());
        if (shouShu <= 0) {
            shouShu = mProductListBean.getMinSl();
        }
        text_create_tips.setText(mContext.getResources().getString(R.string.create_order_tips,
                priceTick,
                NumberUtil.moveLast0(NumberUtil.multiply(Double.parseDouble(priceTick), Double.parseDouble(volumeMultiple * shouShu + "")))));

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
                if (!isClosed.equals("2")) {
                    mBuild.setBackgroundResource(R.color.color_FB4F4F);
                }
                setTextChange();
                break;
            case R.id.tv_down:
                mBuyType = "1";
                mTvRise.setBackgroundResource(R.drawable.rise_down_bg_block);
                mTvRise.setTextColor(mContext.getResources().getColor(R.color.color_151515));
                mTvDown.setBackgroundResource(R.drawable.bg_buy_down);
                mTvDown.setTextColor(Color.WHITE);
                if (!isClosed.equals("2")) {
                    mBuild.setBackgroundResource(R.color.color_2CC593);
                }
                setTextChange();
                break;
            case R.id.iv_open_account_tip:
                if (DeviceUtils.isFastDoubleClick()) {
                    return;
                }
                TradeTipDialog tradeTipDialog = new TradeTipDialog(mContext, R.layout.layout_build_tip_pop_window);
                tradeTipDialog.show();
                break;
            case R.id.btn_fast_open:
                if (DeviceUtils.isFastDoubleClick()) {
                    return;
                }
                String buyTypeStr;
                if (mBuyType.equals("1")) {
                    buyTypeStr = "买跌";
                } else {
                    buyTypeStr = "买涨";
                }
                MobclickAgent.onEvent(mContext, "jy_" + mProductListBean.getInstrumentId() + "_jiancang_click", "交易_" + mProductListBean.getInstrumentName() + "_建仓");
                if (TextUtils.isEmpty(mPrice.getText().toString())) {
                    mPrice.setText(lowerLimitPrice);
                    showToast("委托价低于跌停价，已经帮您调整至跌停价");
                    return;
                } else {
                    if (Double.parseDouble(mPrice.getText().toString()) < Double.parseDouble(lowerLimitPrice)) {
                        mPrice.setText(lowerLimitPrice);
                        showToast("委托价低于跌停价，已经帮您调整至跌停价");
                        return;
                    } else if (Double.parseDouble(mPrice.getText().toString()) > Double.parseDouble(upperLimitPrice)) {
                        mPrice.setText(upperLimitPrice);
                        showToast("委托价高于涨停价，已经帮您调整至涨停价");
                        return;
                    }
                }

                new AlertFragmentDialog.Builder(mContext).setTitle("确认建仓").setContent(instrumentName + " " + buyTypeStr + " " + mHands.getText().toString() + "手 总计 " + mTotal.getText().toString()
                        , R.color.color_A4A5A6, R.dimen.dimen_14sp)
                        .setRightBtnText("确定")
                        .setLeftBtnText("取消")
                        .setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                            @Override
                            public void dialogRightBtnClick(String string) {
                                mBuildTransactionPresenter.buildTransaction(!mIsStopChangePrice, mHands.getText().toString(),
                                        mBuyType,
                                        mPrice.getText().toString(),
                                        instrumentId,
                                        SpUtil.getString(Constant.CACHE_TAG_UID),
                                        SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN),
                                        "GUOFU"
                                );
                            }
                        }).build();
                break;
            case R.id.btn_go_recharge:
                MobclickAgent.onEvent(mContext, "jy_" + mProductListBean.getInstrumentId() + "_click", "交易_" + mProductListBean.getInstrumentName() + "_去充值");
                InAndOutGoldActivity.startInAndOutGoldActivity(mContext, 0);
                break;
            case R.id.btn_deal_reduce:
                lessAddPrice(false);
                break;
            case R.id.btn_deal_add:
                lessAddPrice(true);
                break;
            case R.id.btn_hands_reduce:
                lessAddHands(false);
                break;
            case R.id.btn_hands_add:
                lessAddHands(true);
                break;
        }
    }

    private void setTextChange() {
        if (!mIsStopChangePrice) {
            if (mBuyType.equals("2")) {
                mPrice.setText(mProductListBean.getAskPrice1());
            } else {
                mPrice.setText(mProductListBean.getBidPrice1());
            }
            mPrice.setSelection(mPrice.getText().toString().length());
            setTotalMoney();
        } else {
            mTvRise.setText(mPrice.getText().toString().trim());
            mTvDown.setText(mPrice.getText().toString().trim());
        }

        if (TextUtils.isEmpty(mPrice.getText().toString()) || mPrice.getText().toString().equals(".")) {
            mTvRise.setText(lowerLimitPrice);
            mTvDown.setText(lowerLimitPrice);
        } else {
            if (Double.parseDouble(mPrice.getText().toString()) < Double.parseDouble(lowerLimitPrice)) {
                mTvRise.setText(lowerLimitPrice);
                mTvDown.setText(lowerLimitPrice);
            } else if (Double.parseDouble(mPrice.getText().toString()) > Double.parseDouble(upperLimitPrice)) {
                mTvRise.setText(upperLimitPrice);
                mTvDown.setText(upperLimitPrice);
            }
        }
    }

    /**
     * 加减号的点击
     *
     * @param isAdd true表示加号
     */
    private void lessAddHands(boolean isAdd) {
        String input = mHands.getText().toString();
        int shouShu = 0;
        if (TextUtils.isEmpty(input)) {

        } else {
            shouShu = Integer.parseInt(input.trim());
        }
        if (isAdd) {
            shouShu += 1;
            if (shouShu >= maxSl) {
                shouShu = maxSl;
            }
        } else {
            shouShu -= 1;
            if (shouShu <= minSl) {
                shouShu = minSl;
            }
        }
        mHands.setText(String.valueOf(shouShu));
        mHands.setSelection(mHands.getText().toString().length());
        ykTips();
        setTotalMoney();
    }

    /**
     * 加减号的点击
     *
     * @param isAdd true表示加号
     */
    private void lessAddPrice(boolean isAdd) {
        if (!mIsStopChangePrice) {
            mIsStopChangePrice = true;
            if (!"2".equals(isClosed)) {
                mBuild.setText("委托建仓");
            }
        }
        String input = mPrice.getText().toString();
        double price = 0;
        if (TextUtils.isEmpty(input)) {

        } else {
            price = Double.parseDouble(input.trim());
        }
        if (isAdd) {
            price += 1;
            if (price >= Double.parseDouble(mProductListBean.getUpperLimitPrice())) {
                price = Double.parseDouble(mProductListBean.getUpperLimitPrice());
            }
        } else {
            price -= 1;
            if (price <= Double.parseDouble(mProductListBean.getLowerLimitPrice())) {
                price = Double.parseDouble(mProductListBean.getLowerLimitPrice());
            }
        }
        mPrice.setText(NumberUtil.beautifulDouble(price));
        mPrice.setSelection(mPrice.getText().toString().length());
        setTextChange();
        ykTips();
        setTotalMoney();
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
        if (mIsStopChangePrice) {
            ToastUtil.showToast("委托建仓成功");
        } else {
            ToastUtil.showToast("快速建仓成功");
        }
        dismiss();
    }

    @Override
    public void getProductDetailSuccess(ProductListBean bean) {
        showDialogData(bean);
    }
}
