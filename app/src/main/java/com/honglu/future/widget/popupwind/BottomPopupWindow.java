package com.honglu.future.widget.popupwind;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.honglu.future.R;

/**
 * Created by zq on 2017/10/28.
 */

public class BottomPopupWindow extends PopupWindow {
    private Context mContext;

    public BottomPopupWindow(Context context, View locationView, View rootView) {
        mContext = context;
        initPopWindow(context, locationView, rootView);
    }

    private void initPopWindow(Context context, View locationView, View rootView) {
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        ColorDrawable drawable = new ColorDrawable(context.getResources().getColor(android.R.color.transparent));
        setBackgroundDrawable(drawable);
        setFocusable(true);
        setAnimationStyle(R.style.Popupwindow);
        setContentView(rootView);
        int[] location = new int[2];
        locationView.getLocationOnScreen(location);
        showAtLocation(locationView, Gravity.LEFT | Gravity.BOTTOM, 0, -location[1]);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        ((Activity) mContext).getWindow().setAttributes(lp);
    }
}
