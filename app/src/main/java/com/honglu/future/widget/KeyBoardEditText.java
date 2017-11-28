package com.honglu.future.widget;

import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * Created by zhuaibing on 2017/11/27
 */

public class KeyBoardEditText extends EditText{
    public KeyBoardEditText(Context context) {
        this(context,null);
    }

    public KeyBoardEditText(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public KeyBoardEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setKeyboard(final KeyboardView keyboardView){
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int inputType = getInputType();
                setInputType(InputType.TYPE_NULL);
                int visibility = keyboardView.getVisibility();
                if (visibility == View.GONE || visibility == View.INVISIBLE){
                    keyboardView.setVisibility(View.VISIBLE);
                }
                setInputType(inputType);
                setSelection(getText().length());
                //complete
                return false;
            }
        });

        keyboardView.setOnKeyboardActionListener(new KeyboardView.OnKeyboardActionListener() {
            @Override
            public void swipeLeft() {}
            @Override
            public void swipeRight() {}
            @Override
            public void swipeDown() {}
            @Override
            public void swipeUp() {}
            @Override
            public void onPress(int primaryCode) {}
            @Override
            public void onRelease(int primaryCode) {}
            @Override
            public void onText(CharSequence text) {

            }
            @Override
            public void onKey(int primaryCode, int[] keyCodes) {
                Editable editable = getText();
                int start = getSelectionStart();
                if (primaryCode == 10){ //完成
                    if (mListener !=null){
                        mListener.onComplete();
                    }
                    keyboardView.setVisibility(View.GONE);
                }else if (primaryCode == 14){ //隐藏
                    keyboardView.setVisibility(View.GONE);
                    if (mListener  !=null){
                        mListener.onCancel();
                    }
                }else if (primaryCode == 11){
                     //删除
                    if (editable != null && editable.length() > 0 && start != 0)
                    {
                        editable.delete(start - 1, start);
                    }

                }else if (primaryCode == 12){//点
                    String etText = getEtText();
                    if (!TextUtils.isEmpty(etText) && !etText.contains(".") && start != 0){
                        editable.insert(start, String.valueOf("."));
                    }
                }else if (primaryCode != 13){
                    editable.insert(start, String.valueOf(primaryCode));
                }
            }
        });
    }

    private String getEtText(){

        return getText() !=null && !TextUtils.isEmpty(getText().toString()) ? getText().toString() : "";
    }


    private OnKeyboardListener mListener;
    public void setOnKeyboardListener(OnKeyboardListener listener){
        this.mListener = listener;
    }
    public interface OnKeyboardListener{
           void onComplete();
           void onCancel();
    }
}
