package com.honglu.future.ui.recharge.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.honglu.future.R;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.base.PermissionsListener;
import com.honglu.future.config.ConfigUtil;
import com.honglu.future.config.Constant;
import com.honglu.future.dialog.AlertFragmentDialog;
import com.honglu.future.events.BankSelectEvent;
import com.honglu.future.events.UIBaseEvent;
import com.honglu.future.ui.main.activity.WebViewActivity;
import com.honglu.future.ui.recharge.bean.AssesData;
import com.honglu.future.ui.recharge.contract.PayAndOutGoldContract;
import com.honglu.future.ui.recharge.presenter.PayAndOutGoldPresent;
import com.honglu.future.ui.usercenter.activity.BindCardActivity;
import com.honglu.future.ui.usercenter.bean.BindCardBean;
import com.honglu.future.util.AESUtils;
import com.honglu.future.util.AndroidUtil;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.util.NumberUtil;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.honglu.future.util.ToastUtil.showToast;


/**
 * Created by hefei on 2017/10/26.
 * 充值提现页面
 */
public class PayAndOutGoldFragment extends BaseFragment<PayAndOutGoldPresent> implements PayAndOutGoldContract.View {
    private static final String KEY = "key";

    private static final int RESULT_CODE = 100;
    public static final String RESULT_KEY = "DATA";
    public static final String RESULT_POSITION = "POSITION";
    @BindView(R.id.et_check_bank_asses)
    TextView mCheckAsses;
    @BindView(R.id.tv_type_asses)
    TextView mTypeAsses;
    @BindView(R.id.ll_close)
    LinearLayout mLLClose;
    @BindView(R.id.et_pay_asses)
    EditText mEtPayAsses;
    @BindView(R.id.et_asses_password)
    EditText mEt_asses_password;
    @BindView(R.id.et_bank_password)
    EditText mEt_bank_password;
    @BindView(R.id.btn_pay)
    TextView mBtnPay;
    @BindView(R.id.tv_phone_num)
    TextView tv_phone_num;
    @BindView(R.id.rl_card)
    LinearLayout mrlCard;//银行卡显示列表
    @BindView(R.id.ll_bank_password)
    LinearLayout ll_bank_password;//银行卡密码
    @BindView(R.id.pay_tip)
    TextView mPayTip;
    @BindView(R.id.iv_comp)
    ImageView mIvComp;
    @BindView(R.id.tv_comp)
    TextView mTvComp;
    @BindView(R.id.line_two)
    View line_two;
    private boolean mIsPay = true;//默认是充值页面
    private BindCardBean mBean;
    private List<BindCardBean> mList;
    private ImageView icon;
    private TextView tvBank;
    private TextView tvBankNum;
    private int position;

    /**
     * @param isPay 充值
     * @return 页面
     */
    public static PayAndOutGoldFragment getInstance(boolean isPay) {
        PayAndOutGoldFragment payAndOutGoldFragment = new PayAndOutGoldFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY, isPay);
        payAndOutGoldFragment.setArguments(bundle);
        return payAndOutGoldFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_in_and_out_gold;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        EventBus.getDefault().register(this);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mIsPay = arguments.getBoolean(KEY);
            if (!mIsPay) {
                mPayTip.setText("提现时间:\n工作日 9:30-15:30");
            } else {
                mPayTip.setText("充值时间:\n工作日 8:30-15:30（工商银行8:45开始）20:30-次日 2:30");
            }
        }
        tv_phone_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, requestPermissions);
            }
        });
        if (SpUtil.getString(Constant.COMPANY_TYPE).equals(Constant.COMPANY_TYPE_GUOFU)) {
            mTvComp.setText("国富期货");
            mIvComp.setImageResource(R.mipmap.ic_guofu);
        } else if (SpUtil.getString(Constant.COMPANY_TYPE).equals(Constant.COMPANY_TYPE_MEIERYA)) {
            mTvComp.setText("美尔雅期货");
            mIvComp.setImageResource(R.mipmap.ic_mey);
        }
        initView();
        mBtnPay.setEnabled(false);
        setListener();
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    private void setListener() {
        final TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String amount = mEtPayAsses.getText().toString();
                String asses_password = mEt_asses_password.getText().toString();
                String bank_password = mEt_bank_password.getText().toString();
                if (!TextUtils.isEmpty(amount) && !TextUtils.isEmpty(asses_password)) {
                    if (mBean != null) {
                        if (mBean.cashoutFlag == 1 || mBean.rechargeFlag == 1) {
                            mBtnPay.setEnabled(true);
                        } else {
                            if (!TextUtils.isEmpty(bank_password)) {
                                mBtnPay.setEnabled(true);
                            } else {
                                mBtnPay.setEnabled(false);
                            }
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
        mEtPayAsses.addTextChangedListener(textWatcher);
        mEt_asses_password.addTextChangedListener(textWatcher);
        mEt_bank_password.addTextChangedListener(textWatcher);
    }

    private PermissionsListener requestPermissions = new PermissionsListener() {
        @Override
        public void onGranted() {
            //拨打电话
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                    + Constant.CUSTOMER_PHONE));
            startActivity(intent);
        }

        @Override
        public void onDenied(List<String> deniedPermissions, boolean isNeverAsk) {
            for (String denied : deniedPermissions) {
                if (denied.equals(Manifest.permission.CALL_PHONE)) {
                    showToast(getString(R.string.please_open_permission, getString(R.string.call_phone)));
                }
            }
        }
    };

    /**
     * 根据类型初始化view显示
     */
    private void initView() {
        if (mIsPay) {
            line_two.setVisibility(View.GONE);
            mCheckAsses.setVisibility(View.GONE);
            mCheckAsses.setText(getString(R.string.check_bank_asses));
            mCheckAsses.setTextColor(getResources().getColor(R.color.color_008EFF));
            mTypeAsses.setText(getString(R.string.put_assess));
            mBtnPay.setText(getString(R.string.confirm_pay));
            mCheckAsses.setEnabled(true);
            mEtPayAsses.setHint("请填写充值金额");
        } else {
            line_two.setVisibility(View.VISIBLE);
            mCheckAsses.setVisibility(View.VISIBLE);
            String string = SpUtil.getString(Constant.CACHE_USER_ASSES);
            mCheckAsses.setText(getString(R.string.can_bank_asses, string));
            mTypeAsses.setText(getString(R.string.get_assess));
            mCheckAsses.setTextColor(getResources().getColor(R.color.color_A4A5A6));
            mBtnPay.setText(getString(R.string.confirm_out));
            mCheckAsses.setEnabled(false);
            mEtPayAsses.setHint("请填写提现金额");
        }
    }

    public void getBankList() {
        String cathch = SpUtil.getString(Constant.CACHE_TAG_BANK_LIST);
        if (!TextUtils.isEmpty(cathch)) {
            Type type = new TypeToken<List<BindCardBean>>() {
            }.getType();
            List<BindCardBean> list = new Gson().fromJson(cathch, type);
            this.mList = list;
            bindBnakList(list);
        }
        mPresenter.getBankList(SpUtil.getString(Constant.CACHE_TAG_UID), SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN));
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getBankList();
        } else {
            mEtPayAsses.setText("");
            mEt_asses_password.setText("");
            mEt_bank_password.setText("");
            mBtnPay.setEnabled(false);
        }
    }

    @OnClick({R.id.et_check_bank_asses, R.id.btn_pay})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.et_check_bank_asses) {
            if (mBean == null) {
                bindBankErr("您没有绑定银行卡");
                return;
            }
            int type = AlertFragmentDialog.Builder.TYPE_INPUT_TWO;
            if (mIsPay) {
                if (mBean != null && mBean.rechargeFlag == 1) {
                    type = AlertFragmentDialog.Builder.TYPE_INPUT_ONE;
                }
            } else {
                if (mBean != null && mBean.cashoutFlag == 1) {
                    type = AlertFragmentDialog.Builder.TYPE_INPUT_ONE;
                }
            }
            new AlertFragmentDialog.Builder(mActivity)
                    .setLeftBtnText("稍后在说").setContent(mBean.getBankName() + "：" + NumberUtil.bankNameFilter(mBean.getBankAccount())).setTitle("输入资金密码")
                    .setEtHintText("输入资金密码")
                    .setRightBtnText("确定").setRightClickInputCallBack(new AlertFragmentDialog.RightClickInputCallBack() {
                @Override
                public void dialogRightBtnClick(String inputOne, String inputTwo) {
                    if (mIsPay) {
                        if (mBean != null) {
                            mPresenter.getBalanceAsses(SpUtil.getString(Constant.CACHE_TAG_UID),
                                    mBean.getBrokerBranchId(),
                                    AESUtils.encrypt(inputOne),
                                    mBean.getBankId(),
                                    mBean.getBankBranchId(),
                                    mBean.getBankAccount(),
                                    AESUtils.encrypt(inputTwo),
                                    SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN)
                            );
                        }
                    }
                }
            }).create(type);
        } else if (id == R.id.btn_pay) {
            if (mBean == null) {
                bindBankErr("您没有绑定银行卡");
                return;
            }
            final String amount = mEtPayAsses.getText().toString();
            final String password = mEt_asses_password.getText().toString();
            final String bankPassword = mEt_bank_password.getText().toString();
            if (TextUtils.isEmpty(amount) || TextUtils.isEmpty(password) || (TextUtils.isEmpty(bankPassword) && (mBean.rechargeFlag != 1 || mBean.cashoutFlag != 1))) {
                ToastUtil.show("请输入正确的信息");
                return;
            }
            if (!mIsPay) {
                new AlertFragmentDialog.Builder(mActivity)
                        .setLeftBtnText("取消").setContent(amount, R.color.color_333333, R.dimen.dimen_25sp).setTitle("确认提现", R.color.color_3C383F, R.dimen.dimen_16sp)
                        .setRightBtnText("确定").setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                    @Override
                    public void dialogRightBtnClick(String string) {
                        if (mBean != null) {
                            mPresenter.cashout(SpUtil.getString(Constant.CACHE_TAG_UID),
                                    mBean.getBrokerBranchId(),
                                    AESUtils.encrypt(password),
                                    mBean.getBankId(),
                                    mBean.getBankBranchId(),
                                    mBean.getBankAccount(),
                                    AESUtils.encrypt(bankPassword),
                                    amount,
                                    SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN)
                            );
                        }
                    }
                }).create(AlertFragmentDialog.Builder.TYPE_NORMAL);
            } else {
                new AlertFragmentDialog.Builder(mActivity)
                        .setLeftBtnText("取消").setContent(amount, R.color.color_333333, R.dimen.dimen_25sp).setTitle("确认充值", R.color.color_3C383F, R.dimen.dimen_16sp)
                        .setRightBtnText("确定").setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                    @Override
                    public void dialogRightBtnClick(String string) {
                        if (mBean != null) {
                            mPresenter.rechage(SpUtil.getString(Constant.CACHE_TAG_UID),
                                    mBean.getBrokerBranchId(),
                                    AESUtils.encrypt(password),
                                    mBean.getBankId(),
                                    mBean.getBankBranchId(),
                                    mBean.getBankAccount(),
                                    AESUtils.encrypt(bankPassword),
                                    amount,
                                    SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN)
                            );
                        }
                    }
                }).create(AlertFragmentDialog.Builder.TYPE_NORMAL);
            }
        }
    }

    @Override
    public void bindBnakList(final List<BindCardBean> list) {
        if (mrlCard == null) {
            return;
        }
        this.mList = list;
        if (list != null && list.size() > 0) {
            //  SpUtil.putString(Constant.CACHE_TAG_BANK_LIST,new Gson().toJson(list));
            mrlCard.removeAllViews();
            if (position ==0){
                position = 0;
            }
            mBean = list.get(0);
            View cardItem = View.inflate(getActivity(), R.layout.item_card_layout, null);
            icon = (ImageView) cardItem.findViewById(R.id.icon_bank_card);
            tvBank = (TextView) cardItem.findViewById(R.id.tv_bank);
            tvBankNum = (TextView) cardItem.findViewById(R.id.tv_bank_number);
            bindData();
            cardItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BindCardActivity.startBindCardActivityForResult(getActivity(), mList, position,mIsPay);
                }
            });
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(AndroidUtil.dip2px(getContext(), 15), 0, AndroidUtil.dip2px(getContext(), 15), 0);
            mrlCard.addView(cardItem, params);
        } else {
            SpUtil.putString(Constant.CACHE_TAG_BANK_LIST, "");
            mrlCard.removeAllViews();
            bindBankErr("您没有绑定银行卡");
        }
    }

    private void bindData() {
        if (mBean.rechargeFlag == 1 || mBean.cashoutFlag == 1) {
            ll_bank_password.setVisibility(View.GONE);
        } else {
            ll_bank_password.setVisibility(View.VISIBLE);
        }
        ImageUtil.display(getContext(), mBean.getBankIcon(), icon, R.mipmap.bank_card);
        tvBank.setText(mBean.getBankName());
        tvBankNum.setText(NumberUtil.bankNameFilter(mBean.getBankAccount()));
    }

    /***********
     * eventBus 监听
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BankSelectEvent event) {
        if (mIsPay){
            if (event.isPay){
                mBean = event.bean;
                position =event.position;
                bindData();
            }
        }else {
            if (!event.isPay){
                mBean = event.bean;
                position =event.position;
                bindData();
            }
        }

    }

    boolean isReQuest = false;

    @Override
    public void bindBankErr(String msg) {
        if (isHidden()) {
            return;
        }
        if (mrlCard != null) {
            SpUtil.putString(Constant.CACHE_TAG_BANK_LIST, "");
            mrlCard.removeAllViews();
        }
        if ("您没有绑定银行卡".equals(msg)) {
            new AlertFragmentDialog.Builder(mActivity)
                    .setLeftBtnText("稍后再说").setContent("您没有绑定银行卡", R.color.color_333333, R.dimen.dimen_16sp).setTitle("", R.color.color_3C383F, R.dimen.dimen_16sp)
                    .setRightBtnText("去绑卡").setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                @Override
                public void dialogRightBtnClick(String string) {
                    Intent intent = new Intent(mActivity, WebViewActivity.class);
                    intent.putExtra("title", "如何添加银行卡");
                    intent.putExtra("url", ConfigUtil.BIND_CARD_TEACH);
                    startActivity(intent);
                }
            }).setLeftCallBack(new AlertFragmentDialog.LeftClickCallBack() {
                @Override
                public void dialogLeftBtnClick() {
                    getActivity().finish();
                }
            }).create(AlertFragmentDialog.Builder.TYPE_NORMAL);
        } else if ("请求太频繁 请稍后再试(-2)".equals(msg)) {
            if (isReQuest) {
                return;
            }
            mCheckAsses.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d("Tag", "request");
                    isReQuest = true;
                    getBankList();
                }
            }, 700);
        }

    }

    @Override
    public void rechageSuccess() {
        new AlertFragmentDialog.Builder(mActivity)
                .setLeftBtnText("取消").setTitle("充值成功", R.color.color_3C383F, R.dimen.dimen_16sp).setImageRes(R.mipmap.success_tixian)
                .setLeftCallBack(new AlertFragmentDialog.LeftClickCallBack() {
                    @Override
                    public void dialogLeftBtnClick() {
                        getActivity().finish();
                    }
                })
                .setRightBtnText("继续充值").create(AlertFragmentDialog.Builder.TYPE_IMAGE);
    }

    @Override
    public void cashout() {
        new AlertFragmentDialog.Builder(mActivity)
                .setLeftBtnText("取消").setTitle("提现成功", R.color.color_3C383F, R.dimen.dimen_16sp).setImageRes(R.mipmap.success_tixian)
                .setLeftCallBack(new AlertFragmentDialog.LeftClickCallBack() {
                    @Override
                    public void dialogLeftBtnClick() {
                        getActivity().finish();
                    }
                })
                .setRightBtnText("继续提现").create(AlertFragmentDialog.Builder.TYPE_IMAGE);
    }

    /**
     * 提现失败
     *
     * @param msg
     */
    @Override
    public void cashoutErr(String msg) {
        String msgStr;
        if (TextUtils.isEmpty(msg)) {
            msgStr = "提现失败";
        } else {
            msgStr = msg;
        }
        new AlertFragmentDialog.Builder(mActivity)
                .setLeftBtnText("取消").setTitle(msgStr, R.color.color_3C383F, R.dimen.dimen_16sp).setImageRes(R.mipmap.fail_tixian)
                .setRightBtnText("重新提现").create(AlertFragmentDialog.Builder.TYPE_IMAGE);
    }

    @Override
    public void bindAssess(AssesData amount) {
        mCheckAsses.setText(getString(R.string.bank_asses, amount.amount));
        mCheckAsses.setTextColor(getResources().getColor(R.color.color_A4A5A6));
        mCheckAsses.setEnabled(false);
    }
}
