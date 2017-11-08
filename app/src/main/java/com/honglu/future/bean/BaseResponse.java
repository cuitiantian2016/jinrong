package com.honglu.future.bean;

import java.io.Serializable;

/**
 * 封装服务器返回数据
 */
public class BaseResponse<T> implements Serializable {
    public boolean success;
    private int status;


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String time;
    public T data;

    public boolean success() {
        return success||status == 1000;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "BaseResponse{" +
                "time='" + time + '\'' +
                ", data=" + data +
                '}';
    }
}
