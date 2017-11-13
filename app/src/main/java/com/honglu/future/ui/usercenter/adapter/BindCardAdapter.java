package com.honglu.future.ui.usercenter.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.config.ConfigUtil;
import com.honglu.future.config.Constant;
import com.honglu.future.ui.usercenter.bean.BindCardBean;
import com.honglu.future.ui.usercenter.bean.HistoryRecordsBean;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.util.SpUtil;
import com.honglu.future.widget.CircleImageView;
import com.honglu.future.widget.recycler.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zq on 2017/11/2.
 */

public class BindCardAdapter extends BaseRecyclerAdapter<BindCardAdapter.ViewHolder, BindCardBean> {
    @Override
    public BindCardAdapter.ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_bind_card, parent, false);
        return new BindCardAdapter.ViewHolder(view);
    }

    @Override
    public void mOnBindViewHolder(BindCardAdapter.ViewHolder holder, int position) {

        ImageUtil.display(ConfigUtil.baseImageUserUrl + item.getBankIcon(), holder.mIcon, R.mipmap.img_head);
        holder.mBankName.setText(item.getBankName());

        String bankAccount = item.getBankAccount();

        holder.mBankCard.setText(bankAccount.substring(0, 4) + " **** **** " + bankAccount.substring(bankAccount.length() - 4, bankAccount.length()));

        int bgResourceId = getBgResourceId(item.getBankId());
        if (bgResourceId == -1){
            //没图随便放了张
            holder.mLayoutBg.setBackgroundResource(R.mipmap.bg_bank_13_ceb);
        }else {
            holder.mLayoutBg.setBackgroundResource(bgResourceId);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_icon)
        CircleImageView mIcon;
        @BindView(R.id.tv_bank_name)
        TextView mBankName;
        @BindView(R.id.tv_bank_card)
        TextView mBankCard;
        @BindView(R.id.layout_bg)
        LinearLayout mLayoutBg;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private int getBgResourceId(String bankBranchId) {
        //银行ID 工行：1，农行：2，    中行：3，    建行：4，   交通：5，   招商：6，  兴业：7，  浦发：8，平安：13

        int resourceId = -1;
        switch (Integer.parseInt(bankBranchId)) {
            case 1:
                resourceId = R.mipmap.bg_bank_1_icbc;
                break;
            case 2:
                resourceId = R.mipmap.bg_bank_2_abc;
                break;
            case 3:
                resourceId = R.mipmap.bg_bank_3_bc;
                break;
            case 4:
                resourceId = R.mipmap.bg_bank_4_cbc;
                break;
            case 5:
                resourceId = R.mipmap.bg_bank_5_bcm;
                break;
            case 6:
                resourceId = R.mipmap.bg_bank_6_cmb;
                break;
            case 7:
                resourceId = R.mipmap.bg_bank_7_cib;
                break;
            case 8:
                resourceId = R.mipmap.bg_bank_8_spdb;
                break;
            case 13:
                resourceId = R.mipmap.bg_bank_13_ceb;
                break;
        }
        return resourceId;
    }
}
