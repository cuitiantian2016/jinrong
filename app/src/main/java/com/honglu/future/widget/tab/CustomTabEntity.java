package com.honglu.future.widget.tab;

import android.support.annotation.DrawableRes;

public interface CustomTabEntity {
    String getTabTitle();

    String getTabType();

    String setTabTitle(String title);

    @DrawableRes
    int getTabSelectedIcon();

    @DrawableRes
    int getTabUnselectedIcon();
}