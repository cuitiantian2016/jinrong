package com.honglu.future.http;

/**
 * 网络访问回调
 * Created by zq on 2017/12/9.
 */

public interface Callback {

    public void onResponse(String response);

    public void onErrorResponse();
}
