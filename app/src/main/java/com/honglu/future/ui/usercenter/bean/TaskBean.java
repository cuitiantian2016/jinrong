package com.honglu.future.ui.usercenter.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


//任务的bean类
public class TaskBean implements Serializable {
    @SerializedName("task_link")
    public String url;//跳转链接
    @SerializedName("task_name")
    public String title;//任务名称
    @SerializedName("task_title")
    public String content;//任务内容
    public int is_finish;
    public String warn_word;//是否显示灯泡
    public boolean isComplete; //是否已经完成


    public boolean isComplete() {
        return is_finish == 1;
    }

    public boolean isShowDengBao() {
        return !TextUtils.isEmpty(warn_word);
    }
}
