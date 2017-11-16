package com.honglu.future.widget;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by zhuaibing on 2017/11/16
 */

public class DinTextView extends TextView {
    public DinTextView(Context context) {
        this(context,null);
    }

    public DinTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs ,0);
    }

    public DinTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTextFonts();
    }

    //设置字体样式
    private void setTextFonts() {
        //得到AssetManager
        AssetManager mgr = getContext().getAssets();
        //根据路径得到Typeface
        Typeface tf = Typeface.createFromAsset(mgr, "fonts/DIN-Medium.ttf");
        //设置字体
        this.setTypeface(tf);
    }
}