package com.honglu.future.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.honglu.future.R;
import com.honglu.future.config.Constant;
import com.honglu.future.util.SpUtil;

/**
 * Created by zhuaibing on 2017/11/22
 */

public class TradeGuideDialog extends Dialog {
    private ImageView mIvGuide;
    private int mCurImg = 1;
    private static final int[] imgRes = {R.mipmap.guide_trade01, R.mipmap.guide_trade02, R.mipmap.guide_trade03};

    public TradeGuideDialog(@NonNull Context context) {
        super(context, R.style.trade_guide_dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_trade_guide);
        SpUtil.putBoolean(Constant.GUIDE_OPEN_TRANSACTION, true);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mIvGuide = (ImageView) findViewById(R.id.iv_guide);
        mIvGuide.setImageResource(imgRes[0]);
        mIvGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurImg <= 2) {
                    mIvGuide.setImageResource(imgRes[mCurImg]);
                    mCurImg++;
                } else if (mCurImg == 3) {

                    dismiss();
                }
            }
        });
    }

}
