package com.honglu.future.widget.popupwind;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.honglu.future.R;
import com.scwang.smartrefresh.layout.util.DeviceUtils;

/**
 * Created by zq on 2017/12/26.
 */

public class PlayerChangePopWin extends PopupWindow {
    private LinearLayout mVideo;
    private LinearLayout mAudioOnly;
    private ImageView mIvVideo, mIvAudio;

    private View conentView;
    private Context context;

    public interface OnPopItemClickListener {
        void onVideoOnlyClick();

        void onAudioOnlyClick();
    }

    private OnPopItemClickListener mListener;

    public void setOnPopItemClickListener(OnPopItemClickListener listener) {
        mListener = listener;
    }

    public PlayerChangePopWin(final Activity context) {
        this.context = context;
        View conentView = View.inflate(context, R.layout.popupwin_player_layout, null);
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w / 2 - 100);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimTools);

        mVideo = (LinearLayout) conentView.findViewById(R.id.tv_video_only);
        mAudioOnly = (LinearLayout) conentView.findViewById(R.id.tv_audio_only);
        mIvVideo = (ImageView) conentView.findViewById(R.id.iv_video);
        mIvAudio = (ImageView) conentView.findViewById(R.id.iv_audio);

        mVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvVideo.setVisibility(View.VISIBLE);
                mIvAudio.setVisibility(View.GONE);
                dismiss();
                mListener.onVideoOnlyClick();
            }
        });
        mAudioOnly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvVideo.setVisibility(View.GONE);
                mIvAudio.setVisibility(View.VISIBLE);
                dismiss();
                mListener.onAudioOnlyClick();
            }
        });
    }


    public void showPopupWind(View view, boolean isVideo) {
        if (isVideo) {
            mIvVideo.setVisibility(View.VISIBLE);
            mIvAudio.setVisibility(View.GONE);
        } else {
            mIvVideo.setVisibility(View.GONE);
            mIvAudio.setVisibility(View.VISIBLE);
        }
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(view, view.getLayoutParams().width / 2, -DeviceUtils.dip2px(context, 25.f));

        } else {
            this.dismiss();
        }
    }
}
