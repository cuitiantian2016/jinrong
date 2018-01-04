package com.honglu.future.ui.circle.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuaibing on 2018/1/4
 */

public class LayCommentBean implements Serializable {
    public int count;
    public List<LayCommentListBean> layList;

    public List<LayCommentListBean> getLayList(){
        if (layList == null){
            layList = new ArrayList<>();
        }
        return layList;
    }
}
