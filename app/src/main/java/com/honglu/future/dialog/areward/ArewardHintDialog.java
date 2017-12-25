package com.honglu.future.dialog.areward;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.honglu.future.R;

import retrofit2.http.GET;

/**
 * Created by zhuaibing on 2017/12/25
 */

public class ArewardHintDialog extends Dialog{
    private Context mContext;
    private ImageView mImage;
    private TextView mTitle;
    private TextView mContent;
    private TextView mCancel;
    private TextView mAccomplish;
    private View mLine;

    public ArewardHintDialog(@NonNull Context context) {
        super(context, R.style.areward_dialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_areward_hint);
        Window mWindow = this.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        WindowManager manage = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        params.width = manage.getDefaultDisplay().getWidth();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        mWindow.setAttributes(params);
        mImage = (ImageView) findViewById(R.id.iv_image);
        mTitle = (TextView) findViewById(R.id.tv_title);
        mContent = (TextView) findViewById(R.id.tv_content);

        mCancel = (TextView) findViewById(R.id.tv_cancel);
        mLine = findViewById(R.id.v_line);
        mAccomplish = (TextView) findViewById(R.id.tv_accomplish);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dismiss();
            }
        });
        mAccomplish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mListener !=null){
                    mListener.onCcomplish();
                }
            }
        });
    }

    public void showArewardHint(int imgRes,CharSequence title,CharSequence content){
        show();
        mContent.setTextColor(mContext.getResources().getColor(R.color.color_979899));
        mLine.setVisibility(View.GONE);
        mAccomplish.setVisibility(View.GONE);
        mImage.setImageResource(imgRes);
        mTitle.setText(title);
        mContent.setText(content);
    }

    public void showArewardSuccess(int imgRes,CharSequence title,CharSequence content){
        show();
        mLine.setVisibility(View.VISIBLE);
        mAccomplish.setVisibility(View.VISIBLE);
        mImage.setImageResource(imgRes);
        mTitle.setText(title);
        mContent.setText(content);
    }


    public void setOnArewardHintClickListener(OnArewardHintClickListener listener){
        this.mListener = listener;
    }
    public OnArewardHintClickListener mListener;
    public interface OnArewardHintClickListener{
        void onCcomplish();
    }
}
