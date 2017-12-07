package com.honglu.future.widget.gridimage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.honglu.future.R;


public class GridImageView extends ImageView {

    private int moreNum = 0;              //显示更多的数量
    private String msg = "";                  //要绘制的文字
    private TextPaint textPaint;              //文字的画笔

    public GridImageView(Context context) {
        super(context);
        //转化单位

        textPaint = new TextPaint();
        textPaint.setTextAlign(Paint.Align.CENTER);  //文字居中对齐
        textPaint.setAntiAlias(true);                //抗锯齿
        textPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.dimen_28sp));//设置文字大小
        textPaint.setColor(getResources().getColor(R.color.color_white));               //设置文字颜色
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        点击阴影
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                Drawable drawable = getDrawable();
//                if (drawable != null) {
//                    drawable.mutate().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
//                }
//                break;
//            case MotionEvent.ACTION_MOVE:
//                break;
//            case MotionEvent.ACTION_CANCEL:
//            case MotionEvent.ACTION_UP:
//                Drawable drawableUp = getDrawable();
//                if (drawableUp != null) {
//                    drawableUp.mutate().clearColorFilter();
//                }
//                break;
//        }
        return super.onTouchEvent(event);
    }

    public void setMoreNum(int moreNum) {
        this.moreNum = moreNum;
        msg = "+ " + moreNum;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (moreNum > 0) {
            canvas.drawColor(getResources().getColor(R.color.transparent_33));
            float baseY = getHeight() / 2 - (textPaint.ascent() + textPaint.descent()) / 2;
            canvas.drawText(msg, getWidth() / 2, baseY, textPaint);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setImageDrawable(null);
    }
}