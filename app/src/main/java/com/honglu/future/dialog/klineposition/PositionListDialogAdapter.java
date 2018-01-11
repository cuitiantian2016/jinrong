package com.honglu.future.dialog.klineposition;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.honglu.future.R;
import com.honglu.future.config.Constant;
import com.honglu.future.ui.trade.bean.HoldPositionBean;
import com.honglu.future.widget.DinTextView;
import java.util.List;

/**
 * Created by zhuaibing on 2018/1/11
 */

public class PositionListDialogAdapter extends BaseAdapter {
    private Context mContext;
    private List<HoldPositionBean> mList;
    private int mCheckPosition = -1;

    public PositionListDialogAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<HoldPositionBean> getData(){
        return mList;
    }

    public void notifyDataChanged(List<HoldPositionBean> list){
        this.mList = list;
        this.mCheckPosition = -1;
        notifyDataSetChanged();
    }


    public int getUpdatePosition(String exchangeID ,String instrumentID){
        int mPosition = -1;
        if (mList !=null && mList.size() > 0){
            for (int i = 0 ; i < mList.size() ; i ++){
                HoldPositionBean bean = mList.get(i);
                if (TextUtils.equals(instrumentID,bean.getInstrumentId())){
                    mPosition = i;
                    break;
                }
            }
        }
        return mPosition;
    }

    public void updateCheckState(int position){
      this.mCheckPosition = position;
      notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_position_list_dialog, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        HoldPositionBean bean = mList.get(position);

        if (mCheckPosition == position){
            holder.mGouxuan.setSelected(true);
        }else {
            holder.mGouxuan.setSelected(false);
        }

        //1 跌  2涨
        if (bean.getType() == Constant.TYPE_BUY_DOWN) {
            holder.mBuyNum.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
            holder.mBuyNum.setText("买跌" + bean.getPosition() + "手");

        } else {
            holder.mBuyNum.setTextColor(mContext.getResources().getColor(R.color.color_FB4F4F));
            holder.mBuyNum.setText("买涨" + bean.getPosition() + "手");
        }

        //持仓均价
        holder.mPriced.setText(bean.getOpenAvgPrice());


        //持仓盈亏
        if (Double.parseDouble(bean.getTodayProfit()) > 0) {
            holder.mProfitLoss.setText("+" + bean.getTodayProfit());
            holder.mProfitLoss.setTextColor(mContext.getResources().getColor(R.color.color_FB4F4F));
        } else if (Double.parseDouble(bean.getTodayProfit()) < 0) {
            holder.mProfitLoss.setText(bean.getTodayProfit());
            holder.mProfitLoss.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
        } else {
            holder.mProfitLoss.setText(bean.getTodayProfit());
            holder.mProfitLoss.setTextColor(mContext.getResources().getColor(R.color.color_333333));
        }
        return convertView;
    }


    static class ViewHolder {
        ImageView mGouxuan;
        TextView mBuyNum;
        DinTextView mPriced;
        DinTextView mProfitLoss;

        ViewHolder(View view) {
            mGouxuan = (ImageView) view.findViewById(R.id.iv_gouxuan);
            mBuyNum = (TextView) view.findViewById(R.id.tv_buy_num);
            mPriced = (DinTextView) view.findViewById(R.id.tv_priced);
            mProfitLoss = (DinTextView) view.findViewById(R.id.tv_profit_loss);
        }
    }
}
