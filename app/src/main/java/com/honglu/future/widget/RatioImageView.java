package com.honglu.future.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.honglu.future.R;


public class RatioImageView extends ImageView {

    /**
     * 宽高比
     */
    private float ratio;

    public RatioImageView(Context context) {
        this(context, null);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioImageView);

        ratio = typedArray.getFloat(R.styleable.RatioImageView_riv_ratio, 0f);

        typedArray.recycle();
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = Math.round(width * ratio);
        setMeasuredDimension(width, height);
    }

}
