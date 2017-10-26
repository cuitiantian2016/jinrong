package com.honglu.future.ui.market.bean;

/**
 * Created by hc on 2017/10/25.
 */

public class AllClassificationBean {
    private String classificationName;//分类名称
    private Boolean onClick;//点击状态

    public String getClassificationName() {
        return classificationName;
    }

    public void setClassificationName(String classificationName) {
        this.classificationName = classificationName;
    }

    public Boolean getOnClick() {
        return onClick;
    }

    public void setOnClick(Boolean onClick) {
        this.onClick = onClick;
    }

    @Override
    public String toString() {
        return "AllClassificationBean{" +
                "classificationName='" + classificationName + '\'' +
                ", onClick=" + onClick +
                '}';
    }
}
