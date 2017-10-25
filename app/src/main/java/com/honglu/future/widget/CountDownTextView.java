package com.honglu.future.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.EditText;

import com.honglu.future.R;


/**
 * Created by zq on 2017/10/25.
 */

public class CountDownTextView extends android.support.v7.widget.AppCompatTextView {
    public static final int COUNT = 60;
    private static final int DELAY = 1000;
    private int count = COUNT;
    private Handler mHandler;
    private int mWidth;
    private String mFormat = "%sS后重新获取";
    private String mPhoneText = null;
    private boolean hasStart = false;
    private boolean enable = true;
    public CountDownTextView(Context context) {
        super(context);
        init();
    }

    public CountDownTextView(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public CountDownTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray attributes = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.CountDownTextView,
                        defStyleAttr, 0);
        final int mReferId = attributes.getResourceId(R.styleable.CountDownTextView_refer , -1);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(mReferId > 0){
                    ViewGroup rootView = getRoot();
                    if(rootView != null){
                        EditText editText = (EditText) rootView.findViewById(mReferId);
                        if(editText != null){
                            editText.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    mPhoneText = !TextUtils.isEmpty(s) ? s.toString().replaceAll(" " ,"").trim() : "";
                                    if(hasStart)return;
                                    setEnabled(CheckUtils.checkPhoneNum(mPhoneText) && enable);
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                        }
                    }
                }
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        init();
    }

    private ViewGroup getRoot(){
        ViewParent viewParent = getParent();
        while(viewParent != null){

            if(((ViewGroup)viewParent).getId() == android.R.id.content){
                return (ViewGroup) viewParent;
            }
            viewParent = viewParent.getParent();
        }
        return null;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        if(mWidth > 0) {
//            widthMeasureSpec = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY);
//        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private void init(){

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(count <= 0)return;
                setText(String.format("%sS后重新获取" , --count));
                if(count <= 0 && !isEnabled()){
                    hasStart = false;
                    setEnabled((mPhoneText == null || CheckUtils.checkPhoneNum(mPhoneText)) && enable);
                    setText("获取验证码");
                    mHandler.removeMessages(0);
                    if (listeners !=null){
                        listeners.countDownStop();
                    }
                }else{
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(0) , 1000);
                }
            }
        };
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // TODO Auto-generated method stub
                mWidth = getWidth();
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    public void setFormat(String format){
        mFormat = format;
    }
    public void start(){

        if(hasStart)return;
        hasStart = true;
        count = COUNT;
        setEnabled(false);
        setText(String.format(mFormat , count));
//        setTextColor(getResources().getColor(R.color.color_B2B2B2));
        mHandler.sendMessageDelayed(mHandler.obtainMessage(0) , 1000);
    }

    public void setEnable(boolean enable){
        this.enable = enable;
        if(hasStart)return;
        setEnabled(enable);
    }

    public void removeMessages(){
        if (mHandler !=null){
            mHandler.removeMessages(0);
        }
    }
    private OnCountDownStopListeners listeners;
    public void setOnCountDownStopListeners(OnCountDownStopListeners l){
       this.listeners = l;
    }
    public interface OnCountDownStopListeners{
        void countDownStop();
    }
}
