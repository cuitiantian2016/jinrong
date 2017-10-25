package com.honglu.future.widget.tab;

public interface OnTabSelectListener {
    void onTabSelect(int position);

    void onTabReselect(int position);

    void onTabUnselected(int position);
}