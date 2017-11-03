package com.honglu.future.widget.wheelview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

/**
 * Created by zhuaibing on 2017/11/3
 */

public class MyWheelView extends WheelView{
    public MyWheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWheelView(Context context) {
        super(context);
    }


    @Override
    public void drawShadows(Canvas canvas) {
        //super.drawShadows(canvas);
        int height = (int) ( getItemHeight());
        topShadow.setBounds(0, 0, getWidth(), height);
        topShadow.draw(canvas);

        bottomShadow.setBounds(0, getHeight() - height, getWidth(), getHeight());
        bottomShadow.draw(canvas);
    }


}
