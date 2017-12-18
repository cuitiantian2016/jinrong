package com.honglu.future.util;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.widget.DrawableCenterTextView;


/**
 * xiejingwen
 */
public class TitleUtil {
    private Toolbar mToolbar;
    private DrawableCenterTextView mLeft;
    private DrawableCenterTextView mClose;
    private TextView mTitle;
    private DrawableCenterTextView mRight;
    private AppCompatActivity mActivity;
    private View mFlBack;
    private View tv_back;
    //activity构造
    public TitleUtil(AppCompatActivity activity,View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        if (mToolbar==null){
            return;
        }
        mLeft = (DrawableCenterTextView) view.findViewById(R.id.tv_left);
        mClose = (DrawableCenterTextView) view.findViewById(R.id.tv_close);
        mTitle = (TextView) view.findViewById(R.id.tv_title);
        mRight = (DrawableCenterTextView) view.findViewById(R.id.tv_right);
        mFlBack = view.findViewById(R.id.fl_back);
        tv_back = view.findViewById(R.id.tv_back);
        this.mActivity = activity;
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    /**
     * 设置title与返回键
     *
     * @param isShowBack 是否显示返回按钮
     * @param listener  返回按钮的点击事件，默认为传入null  销毁当前activity
     * @param title 标题
     */
    public void setTitle(boolean isShowBack,View.OnClickListener listener,int toolbarColor,String title){
        mTitle.setText(title);
        mToolbar.setBackgroundResource(toolbarColor);
        if (isShowBack){
            Drawable left = ContextCompat.getDrawable(mActivity,R.mipmap.ic_back);
            left.setBounds(0,0,left.getMinimumWidth(),left.getMinimumHeight());
            mLeft.setCompoundDrawables(left,null,null,null);
            if (listener!=null){
                mLeft.setOnClickListener(listener);
            }else{
                mLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mActivity.finish();
                    }
                });
            }
        }else{
            mFlBack.setVisibility(View.VISIBLE);
            tv_back.setVisibility(View.VISIBLE);
            tv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.finish();
                }
            });
            mLeft.setCompoundDrawables(null,null,null,null);
            mLeft.setClickable(false);
        }
    }

    /**
     * 设置title与返回键样式
     *
     * @param isShowBack 是否显示返回按钮
     * @param backID 返回键图片
     * @param listener  返回按钮的点击事件，默认为传入null  销毁当前activity
     * @param title 标题
     */
    public void setTitle(boolean isShowBack,int backID,View.OnClickListener listener,int toolbarColor,String title){
        mTitle.setText(title);
        mToolbar.setBackgroundResource(toolbarColor);
        if (isShowBack){
            Drawable left = ContextCompat.getDrawable(mActivity,backID);
            left.setBounds(0,0,left.getMinimumWidth(),left.getMinimumHeight());
            mLeft.setCompoundDrawables(left,null,null,null);
            if (listener!=null){
                mLeft.setOnClickListener(listener);
            }else{
                mLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mActivity.finish();
                    }
                });
            }
        }else{
            mFlBack.setVisibility(View.VISIBLE);
            tv_back.setVisibility(View.VISIBLE);
            tv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.finish();
                }
            });
            mLeft.setCompoundDrawables(null,null,null,null);
            mLeft.setClickable(false);
        }
    }

    /**
     * 设置title与返回键
     *
     * @param isShowBack 是否显示返回按钮
     * @param listener  返回按钮的点击事件，默认为传入null  销毁当前activity
     * @param title 标题
     */
    public void setTitle(boolean isShowBack,View.OnClickListener listener,String title){
        mTitle.setText(title);
        if (isShowBack){
            Drawable left = ContextCompat.getDrawable(mActivity,R.mipmap.ic_back);
            left.setBounds(0,0,left.getMinimumWidth(),left.getMinimumHeight());
            mLeft.setCompoundDrawables(left,null,null,null);
            if (listener!=null){
                mLeft.setOnClickListener(listener);
            }else{
                mLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mActivity.finish();
                    }
                });
            }
        }else{
            mFlBack.setVisibility(View.VISIBLE);
            tv_back.setVisibility(View.VISIBLE);
            tv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.finish();
                }
            });
            mLeft.setCompoundDrawables(null,null,null,null);
            mLeft.setClickable(false);
        }
    }
    /**
     * 设置title与返回键
     *
     * @param isShowBack 是否显示返回按钮
     * @param listener  返回按钮的点击事件，默认为传入null  销毁当前activity
     * @param title 标题
     */
    public void setTitle(boolean isShowBack,View.OnClickListener listener,int title){
        mTitle.setText(title);
        if (isShowBack){
            Drawable left = ContextCompat.getDrawable(mActivity,R.mipmap.ic_back);
            left.setBounds(0,0,left.getMinimumWidth(),left.getMinimumHeight());
            mLeft.setCompoundDrawables(left,null,null,null);
            if (listener!=null){
                mLeft.setOnClickListener(listener);
            }else{
                mLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mActivity.finish();
                    }
                });
            }
        }else{
            mFlBack.setVisibility(View.VISIBLE);
            tv_back.setVisibility(View.VISIBLE);
            tv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.finish();
                }
            });
            mLeft.setCompoundDrawables(null,null,null,null);
            mLeft.setClickable(false);
        }
    }
    /**
     * 设置title与返回键
     * @param title 标题
     */
    public void setTitle(String title){
        setTitle(true,null,title);
    }
    public void setTitle(int title){
        setTitle(true,null,title);
    }
    /**
     *
     * 设置title与返回键
     * @param isShowBack
     * @param title
     */
    public void setTitle(boolean isShowBack,String title){
        setTitle(isShowBack,null,title);
    }
    /**
     *
     * 设置title与返回键,背景色
     * @param isShowBack
     * @param title
     */
    public void setTitle(boolean isShowBack,int toolbarColor,String title){
        setTitle(isShowBack,null,toolbarColor,title);
    }
    /**
     *
     * 设置title与返回键,背景色
     * @param isShowBack
     * @param title
     */
    public void setTitle(boolean isShowBack,int backID,int toolbarColor,String title){
        setTitle(isShowBack,backID,null,toolbarColor,title);
    }
    /**
     * 显示关闭按钮
     */
    public void showClose(View.OnClickListener listener){
        mClose.setVisibility(View.VISIBLE);
        mClose.setText(mActivity.getResources().getString(R.string.text_close));
        mClose.setTextColor(mActivity.getResources().getColor(R.color.color_9B9B9B));
        if (listener!=null){
            mClose.setOnClickListener(listener);
        }else {
            mClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.finish();
                }
            });
        }
    }
    /**
     * 隐藏关闭按钮
     */
    public void hintClose(){
        mClose.setVisibility(View.GONE);
    }
    /**
     * 设置右边文字
     * @param right
     * @param rightOnClick
     */
    public void setRightTitle(String right, View.OnClickListener rightOnClick){
        mRight.setText(right);
        if (rightOnClick==null){
            mRight.setClickable(false);
            return;
        }
        mRight.setOnClickListener(rightOnClick);
    }

    public void setRightTitle(int rightTextColor,String right, View.OnClickListener rightOnClick){
        mRight.setText(right);
        mRight.setTextColor(mActivity.getResources().getColor(rightTextColor));
        if (rightOnClick==null){
            mRight.setClickable(false);
            return;
        }
        mRight.setOnClickListener(rightOnClick);
    }
    /**
     * 设置右边文字
     * @param right
     * @param rightOnClick
     */
    public void setRightTitle(SpannableStringBuilder right, View.OnClickListener rightOnClick){
        mRight.setText(right);
        if (rightOnClick==null){
            mRight.setClickable(false);
            return;
        }
        mRight.setOnClickListener(rightOnClick);
    }

    /**
     * 设置标题右边图片
     * @param rightRes
     * @param rightOnClick
     */
    public void setRightTitle(int rightRes, View.OnClickListener rightOnClick){
        if (rightRes>0){
            Drawable img = ContextCompat.getDrawable(mActivity,rightRes);
            img.setBounds(0,0,img.getMinimumWidth(),img.getMinimumHeight());
            mRight.setCompoundDrawables(img,null,null,null);
        }else{
            mRight.setCompoundDrawables(null,null,null,null);
        }
        if (rightOnClick!=null){
            mRight.setOnClickListener(rightOnClick);
        }else{
            mRight.setClickable(false);
        }
    }

    public View getFlBack(){
        return mFlBack;
    }
}
