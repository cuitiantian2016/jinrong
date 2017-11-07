package com.honglu.future.widget.kchart;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ViewPagerEx extends ViewPager {

    private boolean isEnable;

    public ViewPagerEx(Context context) {
        super(context);
    }

    public ViewPagerEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isEnable ? super.onInterceptTouchEvent(ev) : false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isEnable ? super.onTouchEvent(ev) : false;
    }


    public void isEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

}
