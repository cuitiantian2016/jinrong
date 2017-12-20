package com.honglu.future.ui.usercenter.bean;

import java.io.Serializable;

//任务的bean类
public class TaskBean implements Serializable{

    public String url;//跳转链接
    public String title;//任务名称
    public String content;//任务内容
    public boolean isComplete; //是否已经完成

    public boolean isComplete() {
        return isComplete;
    }
}
