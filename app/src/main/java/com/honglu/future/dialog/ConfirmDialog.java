package com.honglu.future.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.honglu.future.R;

/**
 * Created by zhuaibing on 2017/11/17
 */

public class ConfirmDialog extends Dialog {
    private Activity mContext;
    private View.OnClickListener leftListener;
    private View.OnClickListener rightListener;
    private String mTitle;
    private String mContnet;
    private String mLeftText;
    private String mRightText;


    private TextView mTitleView;
    private TextView mContentView;
    private TextView mLeftbutView;
    private TextView mRightbutView;

    public ConfirmDialog(@NonNull Activity context) {
        super(context, R.style.confirm_dialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int width = (int) mContext.getResources().getDimension(R.dimen.dimen_270dp);
        params.width = (width > screenWidth * 0.9) ? (int) (screenWidth * 0.75) : width;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        mTitleView = (TextView) findViewById(R.id.tv_title);
        mContentView = (TextView) findViewById(R.id.tv_content);
        mLeftbutView = (TextView) findViewById(R.id.tv_leftbut);
        mRightbutView = (TextView) findViewById(R.id.tv_rightbut);
    }


    public void showDialog(){
        if (!TextUtils.isEmpty(mContnet) && !TextUtils.isEmpty(mTitle)){
            show();
            mTitleView.setText(mTitle);
            mContentView.setText(mContnet);
            if (!TextUtils.isEmpty(mLeftText)){
                mLeftbutView.setText(mLeftText);
            }
            if (!TextUtils.isEmpty(mRightText)){
                mRightbutView.setText(mRightText);
            }
            if (leftListener !=null){
                mLeftbutView.setOnClickListener(leftListener);
            }else {
                mLeftbutView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
            }

            if (rightListener !=null){
                mRightbutView.setOnClickListener(rightListener);
            }else {
                mRightbutView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
            }

        }
    }

    /**
     * 标题
     *
     * @param title
     * @return
     */
    public ConfirmDialog setTitle(String title) {
        this.mTitle = title;
        return ConfirmDialog.this;
    }

    /**
     * 内容
     *
     * @param content
     * @return
     */
    public ConfirmDialog setContent(String content) {
        this.mContnet = content;
        return ConfirmDialog.this;
    }

    /**
     * 左边按钮text
     * @param text
     * @return
     */
    public ConfirmDialog setLeftButtonText(String text){
        this.mLeftText = text;
        return ConfirmDialog.this;
    }

    /**
     * 右边按钮text
     * @param text
     * @return
     */
    public ConfirmDialog setRightButtonText(String text){
        this.mRightText = text;
        return ConfirmDialog.this;
    }

    /**
     *  left事件
     *
     * @param leftListener
     * @return
     */
    public ConfirmDialog setQxListener(View.OnClickListener leftListener) {
        this.leftListener = leftListener;
        return ConfirmDialog.this;
    }

    /**
     * right事件
     *
     * @param rightListener
     * @return
     */
    public ConfirmDialog setRightListener(View.OnClickListener rightListener) {
        this.rightListener = rightListener;
        return ConfirmDialog.this;
    }

}
