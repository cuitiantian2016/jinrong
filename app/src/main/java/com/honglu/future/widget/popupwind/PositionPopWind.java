package com.honglu.future.widget.popupwind;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.honglu.future.R;

/**
 * Created by zhuaibing on 2017/10/27
 */

public class PositionPopWind extends PopupWindow{

    public PositionPopWind(Context context){
        View rootView = View.inflate(context, R.layout.popupwind_posttion_layout,null);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        ColorDrawable drawable = new ColorDrawable(context.getResources().getColor(android.R.color.transparent));
        setBackgroundDrawable(drawable);
        setOutsideTouchable(true);
        setFocusable(true);
        setContentView(rootView);
        setOutsideTouchable(true);
    }



    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor);
    }
}
