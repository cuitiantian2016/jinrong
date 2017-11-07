package com.honglu.future.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;


/**
 * Description: 处理全屏设置(FLAG_TRANSLUCENT_STATUS)后导致 windowSoftInputMode:adjustResize 无效,
 * 通过设置 android:fitsSystemWindows="true" 进行修正后, 预留出系统状态栏空间, 重写系统方法进行重置处理
 */
public final class FixInsetsRelativeLayout extends LinearLayout {

    private int[] mInsets = new int[4];

    public FixInsetsRelativeLayout(Context context) {
        super(context);
    }

    public FixInsetsRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixInsetsRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public final int[] getInsets() {
        return mInsets;
    }

    @Override
    protected final boolean fitSystemWindows(Rect insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Intentionally do not modify the bottom inset. For some reason, 
            // if the bottom inset is modified, window resizing stops working.
            // TODO: Figure out why.

            mInsets[0] = insets.left;
            mInsets[1] = insets.top;
            mInsets[2] = insets.right;

            insets.left = 0;
            insets.top = 0;
            insets.right = 0;
        }

        return super.fitSystemWindows(insets);
    }
}