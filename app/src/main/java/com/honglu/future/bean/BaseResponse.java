package com.honglu.future.bean;

import java.io.Serializable;

/**
 * 封装服务器返回数据
 */
public class BaseResponse<T> implements Serializable {
    public boolean success;
    public String errorCode;
    public String errorInfo;
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

    public String getCode() {
        return errorCode;
    }

    public void setCode(String code) {
        this.errorCode = code;
    }

    public String getMessage() {
        return errorInfo;
    }

    public void setMessage(String message) {
        this.errorInfo = message;
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
                "code='" + errorCode + '\'' +
                ", message='" + errorInfo + '\'' +
                ", time='" + time + '\'' +
                ", data=" + data +
                '}';
    }
}
