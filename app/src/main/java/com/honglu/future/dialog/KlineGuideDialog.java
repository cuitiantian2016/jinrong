package com.honglu.future.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;

import com.honglu.future.R;
import com.honglu.future.config.Constant;
import com.honglu.future.util.SpUtil;

/**
 * Created by zq on 2017/12/13.
 */

public class KlineGuideDialog extends Dialog {
    private LinearLayout mFullGuide;

    public KlineGuideDialog(@NonNull Context context) {
        super(context,R.style.trade_guide_dialog);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_kline_guide);
        SpUtil.putBoolean(Constant.GUIDE_KLINE_FULLSCREEN, true);
        mFullGuide = (LinearLayout) findViewById(R.id.ll_full_guide);
        mFullGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
