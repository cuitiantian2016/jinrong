package com.honglu.future.ui.home.viewmodel;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.honglu.future.R;

/**
 * Created by hefei on 2017/9/6.
 * 指示器的model
 */
public class IndicatorViewModel{

    private LinearLayout linearLayout;
    private int mPreNum = 0;
    private Context mContext;
    View mView;
    IndicatorViewModel(Context context, int mun){
        mContext = context;
        mView = View.inflate(context, R.layout.view_indicator, null);
        linearLayout = (LinearLayout) mView.findViewById(R.id.ll_item_indicator);
        refreshNum(mun);
    }

    void refreshNum(int num) {
        if (mPreNum>0){//
            if (mPreNum!=num){
                mPreNum = num;
                linearLayout.setWeightSum(mPreNum);//设置权重
                addView(mPreNum);
            }
        }else {
            mPreNum = 2;
            linearLayout.setWeightSum(mPreNum);//设置权重
            addView(mPreNum);
        }

    }

    /**
     * 添加一个每个指示器
     * @param sum
     */
    private void addView(int sum){
        if (linearLayout.getChildCount()>=0){
            linearLayout.removeAllViews();
        }
        for (int i = 0; i < sum; i++) {
            View view = new View(mContext);
            view.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_indication));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            params.weight = 1;
            linearLayout.addView(view,params);
            linearLayout.getChildAt(i).setVisibility(View.INVISIBLE);
        }
        linearLayout.getChildAt(0).setVisibility(View.VISIBLE);
    }
    /**
     * 当前显示那个位置
     * @param position
     */
    public void showIndicator(int position){
        int childCount = linearLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (i == position){//当前位置显示
                linearLayout.getChildAt(i).setVisibility(View.VISIBLE);
            }else {
                linearLayout.getChildAt(i).setVisibility(View.INVISIBLE);
            }
        }
    }
}
