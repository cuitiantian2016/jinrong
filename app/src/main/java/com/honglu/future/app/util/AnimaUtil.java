package com.honglu.future.app.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by gym on 2017/3/27 0027.
 * 描述：
 */

public class AnimaUtil {
    public static ValueAnimator DropAnim(final View view, int start, int end){

        ValueAnimator animator=ValueAnimator.ofInt(start,end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override

            public void onAnimationUpdate(ValueAnimator animation) {

                int value= (Integer) animation.getAnimatedValue();

                ViewGroup.LayoutParams lp = view.getLayoutParams();

                lp.height=value;

                view.setLayoutParams(lp);

            }

        });

        return animator;

    }

    /**
     * 展开认证列表页
     * @param view
     * @param height
     */

    public static void animatorOpen(final View view, int height){

        view.setVisibility(View.VISIBLE);

        AnimatorSet set=new AnimatorSet();

        set.setDuration(300);

        ValueAnimator animator = DropAnim(view, 0, height);

        ObjectAnimator oa=ObjectAnimator.ofFloat(view, View.ALPHA, 0.0f, 1.0f);

        set.playTogether(animator,oa);

        set.setDuration(300);

        set.start();

    }

    /**
     * 关闭掉认证列表页
     * @param view
     * @param height
     */

    public static void animatorClose(final View view, int height){

        AnimatorSet set=new AnimatorSet();

        set.setDuration(300);

        ValueAnimator animator = DropAnim(view, height,0);

        ObjectAnimator oa=ObjectAnimator.ofFloat(view,View.ALPHA,1.0f,0.0f);

        set.playTogether(animator,oa);

        set.addListener(new AnimatorListenerAdapter() {

            @Override

            public void onAnimationEnd(Animator animation) {

                super.onAnimationEnd(animation);

                view.setVisibility(View.GONE);

            }

        });
        set.start();

    }
}
