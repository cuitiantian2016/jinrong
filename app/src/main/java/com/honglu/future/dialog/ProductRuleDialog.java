package com.honglu.future.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.ui.trade.bean.ProductListBean;

/**
 * Created by zq on 2017/11/14.
 */

public class ProductRuleDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private ProductListBean mBean;

    public ProductRuleDialog(@NonNull Context context, ProductListBean bean) {
        super(context, R.style.DateDialog);
        this.mContext = context;
        mBean = bean;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kline_product_detail_layout);
        Window mWindow = this.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        WindowManager manage = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        params.width = manage.getDefaultDisplay().getWidth();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        mWindow.setAttributes(params);
        setCanceledOnTouchOutside(false);
        initTipData();
    }

    private void initTipData() {
        ImageView close = (ImageView) findViewById(R.id.iv_close_popup);
        close.setOnClickListener(this);
        TextView name = (TextView) findViewById(R.id.tv_name);
        name.setText(mBean.getExchangeName() + "-" + mBean.getInstrumentName());
        if (!TextUtils.isEmpty(mBean.getExchangeTimePrompt())) {
            String[] allDateStr = mBean.getExchangeTimePrompt().split("\\|");
            if (allDateStr.length > 0) {
                TextView morning = (TextView) findViewById(R.id.tv_morning);
                morning.setText(allDateStr[0]);
            }
            if (allDateStr.length > 1) {
                TextView middle = (TextView) findViewById(R.id.tv_middle);
                middle.setText(allDateStr[1]);
            }
            if (allDateStr.length > 2) {
                TextView night = (TextView) findViewById(R.id.tv_night);
                night.setText(allDateStr[2]);
            }
        }
        TextView build = (TextView) findViewById(R.id.tv_build);
        build.setText(Double.valueOf(mBean.getOpenRatioByMoney()) * 100 + "%");
        TextView todayClose = (TextView) findViewById(R.id.tv_today_close);
        todayClose.setText(Double.valueOf(mBean.getCloseTodayRatioByMoney()) * 100 + "%");
        TextView notTodayClose = (TextView) findViewById(R.id.tv_not_today_close);
        notTodayClose.setText(Double.valueOf(mBean.getCloseRatioByMoney()) * 100 + "%");
        TextView jydw = (TextView) findViewById(R.id.tv_jydw);
        jydw.setText(mBean.getVolumeMultiple() + mBean.getUnit() + "/手");
        TextView bjdw = (TextView) findViewById(R.id.tv_bjdw);
        bjdw.setText(mBean.getUnit() + "/手");
        TextView zxbddw = (TextView) findViewById(R.id.tv_zxbddw);
        zxbddw.setText(mBean.getPriceTick() + "元/" + mBean.getUnit());
        TextView zdtfd = (TextView) findViewById(R.id.tv_zdtfd);
        zdtfd.setText(mBean.getPlLimitRate());
        TextView date = (TextView) findViewById(R.id.tv_date);
        date.setText(mBean.getExpireDate());
        TextView tv_bzjgg = (TextView)findViewById(R.id.tv_bzjgg);
        tv_bzjgg.setText(Double.valueOf(mBean.getLongMarginRatioByMoney())*100 +"%");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close_popup:
                dismiss();
                break;
        }
    }
}
