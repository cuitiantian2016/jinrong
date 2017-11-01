package com.honglu.future.ui.recharge.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.dialog.AlertFragmentDialog;
import com.honglu.future.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by hefei on 2017/10/26.
 * 充值提现页面
 */
public class PayAndOutGoldFragment extends BaseFragment {
    private static final String KEY = "key";
    @BindView(R.id.et_check_bank_asses)
    TextView mCheckAsses;
    @BindView(R.id.tv_type_asses)
    TextView mTypeAsses;
    @BindView(R.id.btn_pay)
    TextView mBtnPay;
    private boolean mIsPay = true;//默认是充值页面

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
    }

    @Override
    public void loadData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mIsPay = arguments.getBoolean(KEY);
        }
        initView();
    }

    /**
     * 根据类型初始化view显示
     */
    private void initView() {
        if (mIsPay) {
            mCheckAsses.setText(getString(R.string.check_bank_asses));
            mCheckAsses.setTextColor(getResources().getColor(R.color.color_008EFF));
            mTypeAsses.setText(getString(R.string.put_assess));
            mBtnPay.setText(getString(R.string.confirm_pay));
            mCheckAsses.setEnabled(true);
        } else {
            mCheckAsses.setText(getString(R.string.can_bank_asses));
            mTypeAsses.setText(getString(R.string.get_assess));
            mCheckAsses.setTextColor(getResources().getColor(R.color.color_A4A5A6));
            mBtnPay.setText(getString(R.string.confirm_out));
            mCheckAsses.setEnabled(false);
        }
    }

    @OnClick({R.id.et_check_bank_asses, R.id.btn_pay, R.id.rl_card})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.et_check_bank_asses) {
            new AlertFragmentDialog.Builder(mActivity)
                    .setLeftBtnText("稍后在说").setContent("招商银行：622 **** **** 2234").setTitle("输入资金密码")
                    .setEtHintText("输入资金密码")
                    .setRightBtnText("确定").setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                @Override
                public void dialogRightBtnClick(String string) {
                    ToastUtil.show(string);
                }
            }).create(AlertFragmentDialog.Builder.TYPE_INPUT);
        } else if (id == R.id.btn_pay) {
            new AlertFragmentDialog.Builder(mActivity)
                    .setLeftBtnText("取消").setContent("¥1000", R.color.color_3C383F, R.dimen.dimen_12sp).setTitle("确认提现", R.color.color_3C383F, R.dimen.dimen_8sp)
                    .setRightBtnText("确定").setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                @Override
                public void dialogRightBtnClick(String string) {
                    ToastUtil.show(string);
                }
            }).create(AlertFragmentDialog.Builder.TYPE_NORMAL);
        } else if (id == R.id.rl_card) {
            new AlertFragmentDialog.Builder(mActivity)
                    .setLeftBtnText("再次提现").setTitle("提现成功")
                    .setContentView(View.inflate(getActivity(),R.layout.item_in_and_out_detail,null))
                    .setRightBtnText("完成").create(AlertFragmentDialog.Builder.TYPE_DIY);
        }
    }
}
