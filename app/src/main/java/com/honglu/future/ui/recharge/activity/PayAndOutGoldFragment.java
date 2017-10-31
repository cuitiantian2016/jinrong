package com.honglu.future.ui.recharge.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.base.BaseFragment;

import butterknife.BindView;


/**
 * Created by hefei on 2017/10/26.
 * 充值提现页面
 */
public class PayAndOutGoldFragment extends BaseFragment{
    private static final String KEY = "key";
    @BindView(R.id.et_check_bank_asses)
    TextView mCheckAsses;
    @BindView(R.id.tv_type_asses)
    TextView mTypeAsses;
    @BindView(R.id.btn_pay)
    TextView mBtnPay;
    private boolean mIsPay = true;//默认是充值页面
    /**
     *
     * @param isPay 充值
     * @return 页面
     */
    public static PayAndOutGoldFragment getInstance(boolean isPay) {
        PayAndOutGoldFragment payAndOutGoldFragment = new PayAndOutGoldFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY,isPay);
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
        if (arguments!=null){
            mIsPay = arguments.getBoolean(KEY);
        }
        initView();
    }
    /**
     * 根据类型初始化view显示
     */
    private void initView() {
        if (mIsPay){
            mCheckAsses.setText(getString(R.string.check_bank_asses));
            mCheckAsses.setTextColor(getResources().getColor(R.color.color_008EFF));
            mTypeAsses.setText(getString(R.string.put_assess));
            mBtnPay.setText(getString(R.string.confirm_pay));
        }else {
            mCheckAsses.setText(getString(R.string.can_bank_asses));
            mTypeAsses.setText(getString(R.string.get_assess));
            mCheckAsses.setTextColor(getResources().getColor(R.color.color_A4A5A6));
            mBtnPay.setText(getString(R.string.confirm_out));
        }
    }
}
