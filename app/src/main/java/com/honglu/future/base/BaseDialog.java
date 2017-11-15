package com.honglu.future.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

import com.honglu.future.base.BasePresenter;
import com.honglu.future.base.BaseView;
import com.honglu.future.util.TUtil;

/**
 * Created by zhuaibing on 2017/11/15
 */

public abstract class BaseDialog<T extends BasePresenter> extends Dialog implements BaseView{
    public T mPresenter;
    public Activity mContext;

    public BaseDialog(@NonNull Activity context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          mPresenter = TUtil.getT(this, 0);
    }

    @Override
    public void showLoading(String content) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorMsg(String msg, String type) {

    }
}
