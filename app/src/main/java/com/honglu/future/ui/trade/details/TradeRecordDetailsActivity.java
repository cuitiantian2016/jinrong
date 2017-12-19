package com.honglu.future.ui.trade.details;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.ui.trade.bean.HistoryBuiderPositionBean;
import com.honglu.future.ui.trade.bean.HistoryMissPositionBean;

import java.io.Serializable;

import butterknife.BindView;

/**
 * 交易记录详情
 * Created by zhuaibing on 2017/11/6
 */

public class TradeRecordDetailsActivity extends BaseActivity {
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_buy_rise)
    TextView tvBuyRise;
    @BindView(R.id.tv_entrust)
    TextView tv_entrust;
    @BindView(R.id.tv_entrust_type)
    TextView tv_entrust_type;
    @BindView(R.id.tv_entrust_time)
    TextView tv_entrust_time;
    @BindView(R.id.tv_entrust_effective)
    TextView tv_entrust_effective;
    @BindView(R.id.tv_revoke_type)
    TextView tv_revoke_type;
    @BindView(R.id.tv_entrust_time2)
    TextView tv_entrust_time2;
    private static final String KEY_DATA = "KEY_DATA";

    public static void startTradeRecordDetailsActivity(Context context, HistoryMissPositionBean item) {
        Intent intent = new Intent(context, TradeRecordDetailsActivity.class);
        intent.putExtra(KEY_DATA, item);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_trade_record_details;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        mTitle.setTitle(true, R.mipmap.ic_back_black, null, R.color.color_white, "撤单详情");
        Intent intent = getIntent();
        if (intent != null) {
            HistoryMissPositionBean bean = (HistoryMissPositionBean) intent.getSerializableExtra(KEY_DATA);
            if (bean != null) {
                tvName.setText(bean.instrumentName);
                String num;
                if (bean.type == 1) {
                    tvBuyRise.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
                    num = mContext.getString(R.string.buy_down_num, bean.position);
                } else {
                    tvBuyRise.setTextColor(mContext.getResources().getColor(R.color.color_FB4F4F));
                    num = mContext.getString(R.string.buy_up_num, bean.position);
                }
                tvBuyRise.setText(num);
                if (bean.openClose == 1) {
                    tv_entrust_type.setText("建仓");
                } else {
                    tv_entrust_type.setText("平仓");
                }
                if (bean.cancelType == 2) {
                    tv_revoke_type.setText("手动撤单");
                } else {
                    tv_revoke_type.setText("自动撤单");
                }
                tv_entrust.setText(bean.price);
                tv_entrust_time.setText(bean.createTime);
                tv_entrust_time2.setText(bean.cancelTime);
                switch (bean.timeCondition) {
                    case 1:
                        tv_entrust_effective.setText("立即完成，否则撤销");
                        break;
                    case 2:
                        tv_entrust_effective.setText("本节有效");
                        break;
                    case 3:
                        tv_entrust_effective.setText("当日有效");
                        break;
                    case 4:
                        tv_entrust_effective.setText("指定日期前有效");
                        break;
                    case 5:
                        tv_entrust_effective.setText("撤销前有效");
                        break;
                    case 6:
                        tv_entrust_effective.setText("集合竞价有效");
                        break;
                    default:
                        tv_entrust_effective.setText("");
                        break;
                }
            }
        }
    }
}
