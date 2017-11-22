package com.honglu.future.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import com.honglu.future.R;
import com.honglu.future.config.Constant;
import com.honglu.future.util.SpUtil;

/**
 * Created by zhuaibing on 2017/11/22
 */

public class TradeGuideDialog extends Dialog{
    private ImageView mImg1;
    private ImageView mImg2;

    public TradeGuideDialog(@NonNull Context context) {
        super(context, R.style.trade_guide_dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_trade_guide);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        mImg1 = (ImageView) findViewById(R.id.iv_img1);
        mImg2 = (ImageView) findViewById(R.id.iv_img2);

        mImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImg1.setVisibility(View.GONE);
                mImg2.setVisibility(View.VISIBLE);
            }
        });

        mImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpUtil.putBoolean(Constant.GUIDE_OPEN_TRANSACTION,true);
                dismiss();
            }
        });
    }

}
