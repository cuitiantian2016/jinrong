package com.honglu.future.widget.tab;

import android.support.annotation.DrawableRes;

public interface CustomTabEntity {
    String getTabTitle();

    String getTabType();

    @DrawableRes
    int getTabSelectedIcon();

    @DrawableRes
    int getTabUnselectedIcon();
}