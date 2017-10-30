package com.honglu.future.widget.popupwind;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.honglu.future.R;
import com.honglu.future.util.ViewUtil;

/**
 * Created by zhuaibing on 2017/10/27
 */

public class PositionPopWind extends PopupWindow{
    private final int mMeasuredWidth;
    private final int mMeasuredHeight;
    private final int mScreenHeight;
    private final int mTabHeight;

    public PositionPopWind(Context context){
        View rootView = View.inflate(context, R.layout.popupwind_posttion_layout,null);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        mMeasuredWidth = rootView.getMeasuredWidth();
        mMeasuredHeight = rootView.getMeasuredHeight();
        mScreenHeight = ViewUtil.getScreenHeight(context);
        mTabHeight = context.getResources().getDimensionPixelSize(R.dimen.dimen_50dp);
        ColorDrawable drawable = new ColorDrawable(context.getResources().getColor(android.R.color.transparent));
        setBackgroundDrawable(drawable);
        setOutsideTouchable(true);
        setFocusable(true);
        setContentView(rootView);
        setOutsideTouchable(true);
    }


    public void showPopupWind(View view){
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            view.getGlobalVisibleRect(rect);
            int h = view.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int surplusHeight = mScreenHeight - location[1] - mMeasuredHeight - mTabHeight;
        if (surplusHeight >= 0){
            showAsDropDown(view);
        }else {
            showAtLocation(view,Gravity.NO_GRAVITY,location[0]+view.getWidth()/2,location[1]-mMeasuredHeight);
        }
    }
}
