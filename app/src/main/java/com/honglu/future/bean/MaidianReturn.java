package com.honglu.future.bean;

import com.honglu.future.app.App;
import com.honglu.future.config.Constant;
import com.honglu.future.util.AndroidUtil;
import com.honglu.future.util.SpUtil;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by hefei on 2017/12/29
 *
 * 埋点的数据结构
 {"code":200,"message":"成功","uuid":"7uhajkduqwo398xjdjakdkDGf","product_name":"qihuo"}
 }
 */

public class MaidianReturn implements Serializable{

    public String code;
    public String message;
    public String uuid ;
    public String product_name;

    @Override
    public String toString() {
        return "MaidianReturn{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", uuid='" + uuid + '\'' +
                ", product_name='" + product_name + '\'' +
                '}';
    }
}
