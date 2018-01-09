package com.honglu.future.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.config.Constant;
import com.honglu.future.events.ChangeTabEvent;
import com.honglu.future.ui.login.activity.LoginActivity;
import com.honglu.future.ui.main.CheckAccount;
import com.honglu.future.ui.main.presenter.AccountPresenter;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zq on 2017/11/10.
 */

public class AccountLoginDialog extends Dialog implements View.OnClickListener, SelectCompDialog.OnSelectCompListener,AccountPresenter.OnFailListener {
    private Context mContext;
    private static AccountLoginDialog dialog = null;
    private AccountPresenter mPresenter;
    private EditText mAccount, mPwd;
    private TextView mLoginAccount;
    private SelectCompDialog selectCompDialog;
    private ImageView mIvComp;
    private TextView mTvComp;
    private TextView mForgetPwd;
    private String mCompType;
    private CheckAccount mCheckAccount;

    public static AccountLoginDialog getInstance(Context context, AccountPresenter presenter) {
        if (dialog == null) {
            synchronized (AccountLoginDialog.class) {
                if (dialog == null) {
                    dialog = new AccountLoginDialog(context, presenter);
                }
            }
        }
        return dialog;
    }

    public AccountLoginDialog(@NonNull Context context, AccountPresenter presenter) {
        super(context, R.style.DateDialog);
        this.mContext = context;
        mPresenter = presenter;
        mPresenter.setOnFailListener(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.future_login_popup_window);
        Window mWindow = this.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        WindowManager manage = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        params.width = manage.getDefaultDisplay().getWidth();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        mWindow.setAttributes(params);
        setCanceledOnTouchOutside(false);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mPwd.setText("");
            }
        });
        selectCompDialog = new SelectCompDialog(mContext);
        selectCompDialog.setOnSelectCompListener(this);
        mCompType = SelectCompDialog.COMP_TYPE_GUOFU;
        mCheckAccount = new CheckAccount(mContext);
        initTipData();
    }

    private void initTipData() {
        ImageView ivClose = (ImageView) findViewById(R.id.iv_close_popup);
        ivClose.setOnClickListener(this);
        ImageView ivTip = (ImageView) findViewById(R.id.iv_open_account_tip);
        ivTip.setOnClickListener(this);
        mAccount = (EditText) findViewById(R.id.et_account);
        if (!TextUtils.isEmpty(SpUtil.getString(Constant.CACHE_ACCOUNT_USER_NAME)) && mCompType.equals(SelectCompDialog.COMP_TYPE_GUOFU)) {
            mAccount.setText(SpUtil.getString(Constant.CACHE_ACCOUNT_USER_NAME));
        }
        if (!TextUtils.isEmpty(SpUtil.getString(Constant.CACHE_ACCOUNT_USER_NAME_MEIERYA)) && mCompType.equals(SelectCompDialog.COMP_TYPE_MEY)) {
            mAccount.setText(SpUtil.getString(Constant.CACHE_ACCOUNT_USER_NAME_MEIERYA));
        }
        mIvComp = (ImageView) findViewById(R.id.iv_comp);
        mTvComp = (TextView) findViewById(R.id.tv_comp);
        mPwd = (EditText) findViewById(R.id.et_password);
        mLoginAccount = (TextView) findViewById(R.id.btn_login_account);
        mLoginAccount.setOnClickListener(this);
        TextView mGoOpen = (TextView) findViewById(R.id.btn_open_account);
        mGoOpen.setOnClickListener(this);
        TextView selectComp = (TextView) findViewById(R.id.tv_select_comp);
        selectComp.setOnClickListener(this);
        mForgetPwd = (TextView) findViewById(R.id.tv_forget_pwd);
        mForgetPwd.setOnClickListener(this);
        TextView mFxgz = (TextView) findViewById(R.id.tv_fxgz);
        mFxgz.setOnClickListener(this);
        mAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mAccount.getText().toString().length() >= 8 && mPwd.getText().toString().length() >= 6) {
                    mLoginAccount.setEnabled(true);
                    mLoginAccount.setBackgroundResource(R.drawable.account_login_btn_bg);
                } else {
                    mLoginAccount.setEnabled(false);
                    mLoginAccount.setBackgroundResource(R.drawable.shape_xiushi_bg_2dp);
                }
            }
        });

        mPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mAccount.getText().toString().length() >= 8 && mPwd.getText().toString().length() >= 6) {
                    mLoginAccount.setEnabled(true);
                    mLoginAccount.setBackgroundResource(R.drawable.account_login_btn_bg);
                } else {
                    mLoginAccount.setEnabled(false);
                    mLoginAccount.setBackgroundResource(R.drawable.shape_xiushi_bg_2dp);
                }
            }
        });

        if (!TextUtils.isEmpty(SpUtil.getString(Constant.COMPANY_TYPE))) {
            mCompType = SpUtil.getString(Constant.COMPANY_TYPE);
            setCompView(mCompType);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close_popup:
                EventBus.getDefault().post(new ChangeTabEvent(0));
                dismiss();
                break;
            case R.id.iv_open_account_tip:
                if (DeviceUtils.isFastDoubleClick()) {
                    return;
                }
                TradeTipDialog tipDialog = new TradeTipDialog(mContext, R.layout.layout_account_login_tip);
                tipDialog.show();
                break;
            case R.id.btn_login_account:
                if (DeviceUtils.isFastDoubleClick()) {
                    return;
                }
                mLoginAccount.setEnabled(false);
                mLoginAccount.setBackgroundResource(R.drawable.shape_xiushi_bg_2dp);
                MobclickAgent.onEvent(mContext, "qihuodenglu_click", "国富期货登录");
                mPresenter.login(mAccount.getText().toString(), mPwd.getText().toString(), SpUtil.getString(Constant.CACHE_TAG_UID), mCompType, mPwd, mContext);
                break;
            case R.id.btn_open_account:
                if (App.getConfig().getLoginStatus()){
                    mCheckAccount.checkAccount();
                } else{
                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                }
                break;
            case R.id.tv_select_comp:
                selectCompDialog.show();
                break;
            case R.id.tv_forget_pwd:
                new AlertFragmentDialog.Builder((FragmentActivity) mContext)
                        .setRightBtnText("知道了").setContent("请在工作日8:30-17:00拨打小牛智投\n" +
                        "客服电话：400 961 0211").setTitle("忘记密码")
                        .create(AlertFragmentDialog.Builder.TYPE_NORMAL);
                break;
            case R.id.tv_fxgz:
                ToastUtil.show("风险告知");
                // TODO: 2018/1/4 风险告知跳转 
                break;
        }
    }

    public void setBtnEnable(){
        mLoginAccount.setEnabled(true);
        mLoginAccount.setBackgroundResource(R.drawable.account_login_btn_bg);
    }



    //拦截物理返回键
    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        return (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0);
    }

    @Override
    public void onSelect(String comp) {
        if (!mCompType.equals(comp)){
            mAccount.setText("");
            mPwd.setText("");
            mCompType = comp;
        }
        setCompView(comp);
    }

    private void setCompView(String comp) {
        if (comp.equals(SelectCompDialog.COMP_TYPE_GUOFU)) {
            mIvComp.setImageResource(R.mipmap.ic_guofu);
            mTvComp.setText("国富期货");
            if (!TextUtils.isEmpty(SpUtil.getString(Constant.CACHE_ACCOUNT_USER_NAME))) {
                mAccount.setText(SpUtil.getString(Constant.CACHE_ACCOUNT_USER_NAME));
            }
        } else if (comp.equals(SelectCompDialog.COMP_TYPE_MEY)) {
            mIvComp.setImageResource(R.mipmap.ic_mey);
            mTvComp.setText("美尔雅期货");
            if (!TextUtils.isEmpty(SpUtil.getString(Constant.CACHE_ACCOUNT_USER_NAME_MEIERYA))) {
                mAccount.setText(SpUtil.getString(Constant.CACHE_ACCOUNT_USER_NAME_MEIERYA));
            }
        }
        selectCompDialog.dismiss();
    }

    @Override
    public void onFail() {
        setBtnEnable();
    }


}
