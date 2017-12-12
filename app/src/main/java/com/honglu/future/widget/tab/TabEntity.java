package com.honglu.future.widget.tab;


public class TabEntity implements CustomTabEntity {

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;
    private String type;
    private int selectIconRes;
    private int unselectIconRes;

    public TabEntity(String title) {
        this.title = title;
    }

    public TabEntity(String title ,String type) {
        this.title = title;
        this.type = type;
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
    public String getTabType() {
        return type;
    }

    @Override
    public String setTabTitle(String title) {
        this.title = title;
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
