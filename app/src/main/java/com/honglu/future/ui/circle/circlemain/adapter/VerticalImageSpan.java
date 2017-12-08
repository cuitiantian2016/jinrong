package com.honglu.future.ui.circle.circlemain.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.style.ImageSpan;

/**
 * ImageSpan 图片居中处理
 */
public class VerticalImageSpan extends ImageSpan {

    public VerticalImageSpan(Context context, int resourceId) {
        super(context, resourceId);
    }

    /**
     * 行间距会出现图片过大时被裁减, 重新设置文本绘制参数
     * android:lineSpacingExtra="4dp"
     * android:lineSpacingMultiplier="1.2f"
     */
    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fontMetricsInt) {
        Rect rect = getDrawable().getBounds();
        if (fontMetricsInt != null) {
            Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
            int fontHeight = fmPaint.bottom - fmPaint.top;
            int drHeight = rect.bottom - rect.top;

            int top = drHeight / 2 - fontHeight / 4;
            int bottom = drHeight / 2 + fontHeight / 4;

            fontMetricsInt.ascent = -bottom - 10;
            fontMetricsInt.top = -bottom - 10;
            fontMetricsInt.bottom = top;
            fontMetricsInt.descent = top;
        }

        return rect.right;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end
            , float x, int top, int y, int bottom, @NonNull Paint paint) {
        Drawable b = getDrawable();
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        int transY = (y + fm.descent + y + fm.ascent) / 2 - b.getBounds().bottom / 2;

        canvas.save();
        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }

} 