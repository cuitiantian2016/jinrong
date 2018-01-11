package com.honglu.future.ui.trade.details;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.ui.trade.bean.CloseBuiderBean;
import com.honglu.future.util.NumberUtil;
import com.honglu.future.util.TimeUtil;

import java.util.List;

/**
 * Created by hefei on 2017/12/28.
 *
 * 平仓中建仓详情的页面展示
 */

public class TradeViewModel {
    Context mContext;
    public TradeViewModel(Context context){
        this.mContext = context;
    }

    public void bindData(List<CloseBuiderBean> datas , LinearLayout layout){
        for (CloseBuiderBean data: datas){
            View inflate = View.inflate(mContext, R.layout.item_xiangqing, null);
            TextView mExpBuyRise = (TextView) inflate.findViewById(R.id.tv_exp_buy_rise);
            TextView mExpJiancangPrice = (TextView) inflate.findViewById(R.id.tv_exp_jiancang_price);
            TextView mExpTime = (TextView) inflate.findViewById(R.id.tv_exp_time);
            TextView mExpBaodanNum = (TextView) inflate.findViewById(R.id.tv_exp_baodan_num);
            TextView mBuildSXF =(TextView) inflate.findViewById(R.id.build_sxf);
            TextView mTradeId = (TextView) inflate.findViewById(R.id.trade_id);
            String num;
            if (data.type == 1) {
                mExpBuyRise.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
                num = mContext.getString(R.string.buy_down_num, data.position);
            } else {
                mExpBuyRise.setTextColor(mContext.getResources().getColor(R.color.color_FB4F4F));
                num = mContext.getString(R.string.buy_up_num, data.position);
            }
            mExpBuyRise.setText(num);
            mExpJiancangPrice.setText("建仓保证金 " + data.useMargin);
            String dateStr = "";
            if(data.openTime.length()>16){
                dateStr = data.openTime.substring(0,16);
            } else {
                dateStr = data.openTime;
            }
            mExpTime.setText("时间" + dateStr);
            mExpBaodanNum.setText("报单编号 " + data.orderSysId);
            mBuildSXF.setText("建仓手续费 " + NumberUtil.beautifulDouble(Double.parseDouble(data.openSxf),2));
            mTradeId.setText("成交编号 " + data.tradeId);
            layout.addView(inflate);
        }
    }

}
