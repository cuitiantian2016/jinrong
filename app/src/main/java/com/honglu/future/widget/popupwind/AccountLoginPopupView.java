package com.honglu.future.widget.popupwind;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cfmmc.app.sjkh.MainActivity;
import com.honglu.future.R;
import com.honglu.future.config.Constant;
import com.honglu.future.events.FragmentRefreshEvent;
import com.honglu.future.ui.main.presenter.AccountPresenter;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ViewUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zq on 2017/11/3.
 */

public class AccountLoginPopupView {
    private Context mContext;
    private BottomPopupWindow mPopupWindow;
    private BottomPopupWindow mTipPopupWindow;
    private View mTipView;
    private AccountPresenter mPresenter;

    public static void tipClickCallBack(OnTipClickCallback onTipClickCallback) {
        onTipClickCallback.tipClick();
    }

    interface OnTipClickCallback {
        void tipClick();
    }

    public AccountLoginPopupView(Context context, View tipView, AccountPresenter presenter) {
        mContext = context;
        mTipView = tipView;
        mPresenter = presenter;

    }

    public void showOpenAccountWindow() {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.future_login_popup_window, null);
        showBottomWindow(mTipView, layout);
        ViewUtil.backgroundAlpha(mContext, 0.5f);
    }

    private void showBottomWindow(View view, View layout) {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            return;
        }
        mPopupWindow = new BottomPopupWindow(mContext, view, layout);
        //添加按键事件监听
        setAccountListener(layout, 0);
        //添加pop窗口关闭事件，主要是实现关闭时改变背景的透明度
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ViewUtil.backgroundAlpha(mContext, 1f);
            }
        });
    }

    private void setAccountListener(View view, final int flag) {
        ImageView ivClose = (ImageView) view.findViewById(R.id.iv_close_popup);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 1) {
//                    if (mTipPopupWindow != null && mTipPopupWindow.isShowing()) {
//                        mTipPopupWindow.dismiss();
//                    }
                } else {
                    if (mPopupWindow != null && mPopupWindow.isShowing()) {
                        EventBus.getDefault().post(new FragmentRefreshEvent());
                        mPopupWindow.dismiss();
                    }
                }
            }
        });

        ImageView ivTip = (ImageView) view.findViewById(R.id.iv_open_account_tip);
        ivTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipClickCallBack(new OnTipClickCallback() {
                    @Override
                    public void tipClick() {
                        showTipWindow(mTipView);
                    }
                });
            }
        });

        final EditText mAccount = (EditText) view.findViewById(R.id.et_account);
        final EditText mPwd = (EditText) view.findViewById(R.id.et_password);
        TextView mLoginAccount = (TextView) view.findViewById(R.id.btn_login_account);
        mLoginAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.login(mAccount.getText().toString(), mPwd.getText().toString(), SpUtil.getString(Constant.CACHE_TAG_UID), "GUOFU");
            }
        });

        TextView mGoOpen = (TextView) view.findViewById(R.id.btn_open_account);
        mGoOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goOpenAccount();
            }
        });

    }

    private void goOpenAccount() {
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.putExtra("brokerId", "0101");
        String userMobile = SpUtil.getString(Constant.CACHE_TAG_MOBILE);
        if (!TextUtils.isEmpty(userMobile)) {
            intent.putExtra("mobile", userMobile);
        }
        mContext.startActivity(intent);
    }

    private void setTipListener(View view, final int flag) {
        ImageView ivClose = (ImageView) view.findViewById(R.id.iv_close_popup);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTipPopupWindow != null && mTipPopupWindow.isShowing()) {
                    mTipPopupWindow.dismiss();
                }
            }
        });
    }

    private void showTipWindow(View view) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.layout_trade_tip_pop_window, null);
        showTipBottomWindow(view, layout, 1);
        ViewUtil.backgroundAlpha(mContext, 0.5f);
    }

    private void showTipBottomWindow(View view, View layout, final int flag) {
        if (mTipPopupWindow != null && mTipPopupWindow.isShowing()) {
            return;
        }
        mTipPopupWindow = new BottomPopupWindow(mContext,
                view, layout);
        //添加按键事件监听
        setTipListener(layout, flag);
        //添加pop窗口关闭事件，主要是实现关闭时改变背景的透明度
        mTipPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (mPopupWindow == null || !mPopupWindow.isShowing()) {
                    ViewUtil.backgroundAlpha(mContext, 1f);
                }
            }
        });
    }

    public void dismissLoginAccountView() {
        mPopupWindow.dismiss();
    }
}
