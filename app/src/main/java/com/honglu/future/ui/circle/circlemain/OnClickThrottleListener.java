package com.honglu.future.ui.circle.circlemain;

import android.view.View;

import com.honglu.future.util.DeviceUtils;


/**
 * 抖动点击封装
 */
public abstract class OnClickThrottleListener implements View.OnClickListener {

    protected abstract void onThrottleClick(View v);

    @Override
    public void onClick(View v) {
        if (DeviceUtils.isFastDoubleClick()) return;

        onThrottleClick(v);
    }

}
