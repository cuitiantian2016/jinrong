package com.honglu.future.ui.circle.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 *"circleTypeId": "圈子分类ID",
 "mainPic": "主图",
 "minorPic": "次图",
 "typeName": "最新"
 */
public class TopicFilter implements Serializable {
    @SerializedName("typeName")
    public String title;//名称
    @SerializedName("minorPic")
    public String icon;//图标
    @SerializedName("circleTypeId")
    public String type;//唯一标识
    @SerializedName("mainPic")
    public String selected_icon;// 选中图标
}
