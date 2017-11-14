package com.honglu.future.widget.popupwind;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.config.ConfigUtil;
import com.honglu.future.ui.main.activity.WebViewActivity;

/**
 * Created by zq on 2017/11/14.
 */

public class KLinePopupWin extends PopupWindow {
    private TextView mTvFull;
    private TextView mTvQhsy;
    private TextView mTvZjsy;

    private View conentView;
    private Context context;

    public interface OnPopItemClickListener {
        void onFullScreeClick();
    }

    private OnPopItemClickListener mListener;

    public void setOnPopItemClickListener(OnPopItemClickListener listener) {
        mListener = listener;
    }

    public KLinePopupWin(final Activity context) {
        View conentView = View.inflate(context, R.layout.popupwin_kline_layout, null);
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

        mTvFull = (TextView) conentView.findViewById(R.id.tv_full);
        mTvQhsy = (TextView) conentView.findViewById(R.id.tv_qhsy);
        mTvZjsy = (TextView) conentView.findViewById(R.id.tv_zjsy);

        mTvFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mListener.onFullScreeClick();
            }
        });
        mTvQhsy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("title", "期货术语");
                intent.putExtra("url", ConfigUtil.FUTURE_SHUYU);
                context.startActivity(intent);
            }
        });
        mTvZjsy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("title", "资金术语");
                intent.putExtra("url", ConfigUtil.ZIJIN_SHUYU);
                context.startActivity(intent);
            }
        });
    }


    public void showPopupWind(View view) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(view, view.getLayoutParams().width / 2, 0);

        } else {
            this.dismiss();
        }
    }
}
