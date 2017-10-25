package com.honglu.future.widget.tab;


public class TabEntity implements CustomTabEntity {

    private String title;
    private int selectIconRes;
    private int unselectIconRes;

    public TabEntity(String title) {
        this.title = title;
    }

    public TabEntity(String title, int selectIconRes, int unselectIconRes) {
        this.title = title;
        this.selectIconRes = selectIconRes;
        this.unselectIconRes = unselectIconRes;
    }

    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public int getTabSelectedIcon() {
        return selectIconRes;
    }

    @Override
    public int getTabUnselectedIcon() {
        return unselectIconRes;
    }
}
