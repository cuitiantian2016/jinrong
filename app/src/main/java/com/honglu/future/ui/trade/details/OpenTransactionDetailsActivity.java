package com.honglu.future.ui.trade.details;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.ui.trade.bean.HistoryBuiderPositionBean;

import butterknife.BindView;

/**
 * 建仓详情
 * Created by zhuaibing on 2017/11/6
 */

public class OpenTransactionDetailsActivity extends BaseActivity{
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_buy_rise)
    TextView tvBuyRise;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_service_charge)
    TextView tvServiceCharge;
    @BindView(R.id.tv_baodan_num)
    TextView tvBaoDanNum;
    @BindView(R.id.tv_chengdan_num)
    TextView tvChengdanNum;

    private static final String KEY_DATA = "KEY_DATA";

    public static void startOpenTransactionDetailsActivity(Context context, HistoryBuiderPositionBean item){
        Intent intent = new Intent(context,OpenTransactionDetailsActivity.class);
        intent.putExtra(KEY_DATA,item);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_open_transaction_details;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        mTitle.setTitle(true,R.mipmap.ic_back_black,null,R.color.color_white,"建仓详情");
        Intent intent = getIntent();
        if (intent!=null){
            HistoryBuiderPositionBean bean = (HistoryBuiderPositionBean) intent.getSerializableExtra(KEY_DATA);
            if (bean!=null){
                String num;
                if (bean.type == 1){
                    tvBuyRise.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
                    num =  mContext.getString(R.string.buy_down_num,bean.position);
                }else {
                    tvBuyRise.setTextColor(mContext.getResources().getColor(R.color.color_FB4F4F));
                    num =  mContext.getString(R.string.buy_up_num,bean.position);
                }
                tvBuyRise.setText(num);
                tvName.setText(bean.instrumentName);
                tvPrice.setText("建仓价 "+bean.price);
                tvTime.setText(bean.tradeTime);
                tvServiceCharge.setText(bean.sxf);
                tvBaoDanNum.setText(bean.orderSysId);
                tvChengdanNum.setText(bean.tradeId);
            }
        }
    }
}
