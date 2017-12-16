package com.honglu.future.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.honglu.future.R;

/**
 * Created by zq on 2017/12/15.
 */

public class SelectCompDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private ImageView ivGuofu;
    private ImageView ivMey;
    public static final String COMP_TYPE_GUOFU = "GUOFU";
    public static final String COMP_TYPE_MEY = "MEIERYA";

    public interface OnSelectCompListener {
        void onSelect(String comp);
    }

    private OnSelectCompListener mListener;

    public void setOnSelectCompListener(OnSelectCompListener listener) {
        mListener = listener;
    }

    public SelectCompDialog(@NonNull Context context) {
        super(context, R.style.DateDialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_comp);
        Window mWindow = this.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        WindowManager manage = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        params.width = manage.getDefaultDisplay().getWidth();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        mWindow.setAttributes(params);
        setCanceledOnTouchOutside(false);
        initViews();
    }

    private void initViews() {
        RelativeLayout rlGuofu = (RelativeLayout) findViewById(R.id.rl_guofu);
        rlGuofu.setOnClickListener(this);
        RelativeLayout rlMey = (RelativeLayout) findViewById(R.id.rl_mey);
        rlMey.setOnClickListener(this);
        ImageView ivClose = (ImageView) findViewById(R.id.iv_close);
        ivClose.setOnClickListener(this);
        ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
        ImageView ivGfTip = (ImageView) findViewById(R.id.iv_gf_tip);
        ivGfTip.setOnClickListener(this);
        ImageView ivMeyTip = (ImageView) findViewById(R.id.iv_mey_tip);
        ivMeyTip.setOnClickListener(this);
        ivGuofu = (ImageView) findViewById(R.id.iv_guofu_select);
        ivMey = (ImageView) findViewById(R.id.iv_mey_select);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_guofu:
                ivGuofu.setImageResource(R.mipmap.ic_checked);
                ivMey.setImageResource(R.drawable.unselect_circle_bg);
                mListener.onSelect(COMP_TYPE_GUOFU);
                break;
            case R.id.rl_mey:
                ivMey.setImageResource(R.mipmap.ic_checked);
                ivGuofu.setImageResource(R.drawable.unselect_circle_bg);
                mListener.onSelect(COMP_TYPE_MEY);
                break;
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.iv_back:
                dismiss();
                break;
            case R.id.iv_gf_tip:
                TradeTipDialog tipGfDialog = new TradeTipDialog(mContext, R.layout.layout_account_login_tip);
                tipGfDialog.show();
                break;
            case R.id.iv_mey_tip:
                TradeTipDialog tipMeyDialog = new TradeTipDialog(mContext, R.layout.layout_account_login_tip);
                tipMeyDialog.show();
                break;
        }
    }
}
