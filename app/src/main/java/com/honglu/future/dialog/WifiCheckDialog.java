package com.honglu.future.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.honglu.future.R;

/**
 * Created by zq on 2017/12/28.
 */

public class WifiCheckDialog extends Dialog implements View.OnClickListener {
    public interface OnItemClickListener {
        void onAudioOnlylClick();

        void onNextClick();

        void onExitClick();
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public WifiCheckDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_check_dialog);
        TextView audioOnly = (TextView) findViewById(R.id.tv_audio_only);
        audioOnly.setOnClickListener(this);
        TextView tvNext = (TextView) findViewById(R.id.tv_next);
        tvNext.setOnClickListener(this);
        TextView tvExit = (TextView) findViewById(R.id.tv_exit);
        tvExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_audio_only:

                break;
            case R.id.tv_next:

                break;
            case R.id.tv_exit:

                break;
        }
    }
}
