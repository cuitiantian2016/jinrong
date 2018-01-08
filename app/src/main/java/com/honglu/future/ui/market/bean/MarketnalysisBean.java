package com.honglu.future.ui.market.bean;

import android.os.Parcelable;
import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhuaibing on 2017/11/8
 */

public class MarketnalysisBean implements Serializable {


    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }



    public class DeliveryDate implements Serializable {

    }
}
