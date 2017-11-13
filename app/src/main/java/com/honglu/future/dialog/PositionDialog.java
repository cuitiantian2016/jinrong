package com.honglu.future.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.ui.trade.bean.HoldDetailBean;
import com.honglu.future.ui.trade.bean.HoldPositionBean;
import com.honglu.future.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuaibing on 2017/10/30
 */

public class PositionDialog extends Dialog {
    private Context mContext;
    private ImageView mClose;
    private TextView mName;
    private TextView mBuyNumber;
    private TextView mPosProfitLoss;
    private TextView mActualProfitLoss;
    private ListView mListView;
    private PositionDialogAdapter mAdapter;
    private int mScreenHeight;


    public PositionDialog(@NonNull Context context) {
        super(context, R.style.DateDialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_position);
        mScreenHeight = ViewUtil.getScreenHeight(mContext);
        Window mWindow = this.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        WindowManager manage = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        params.width = manage.getDefaultDisplay().getWidth();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        mWindow.setAttributes(params);

        mClose = (ImageView) findViewById(R.id.iv_close);
        final TextView tip = (TextView) findViewById(R.id.tv_tip);
        View headView = LayoutInflater.from(mContext).inflate(R.layout.layout_dialog_position_head, null);
        mName = (TextView) headView.findViewById(R.id.tv_name);
        mBuyNumber = (TextView) headView.findViewById(R.id.tv_buy_number);//买入手数
        mPosProfitLoss = (TextView) headView.findViewById(R.id.tv_position_profit_loss);//持仓盈亏
        mActualProfitLoss = (TextView) headView.findViewById(R.id.tv_actual_profit_loss);//实际盈亏

        mListView = (ListView) findViewById(R.id.lv_listView);
        mListView.addHeaderView(headView);
        mAdapter = new PositionDialogAdapter(mContext, null);
        mListView.setAdapter(mAdapter);

        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TradeTipDialog tipDialog = new TradeTipDialog(mContext, R.layout.layout_position_detail_tip);
                tipDialog.show();
            }
        });
    }

    public void showPopupWind(HoldPositionBean bean, List<HoldDetailBean> list) {
        show();
        mName.setText(bean.getInstrumentName());
        //1 买跌 2 买涨
        if (bean.getType() == 1) {
            mBuyNumber.setText("买跌" + bean.getPosition() + "手");
        } else {
            mBuyNumber.setText("买涨" + bean.getPosition() + "手");
        }
        mPosProfitLoss.setText(bean.getTodayProfit());
        mActualProfitLoss.setText(bean.getTotalProfit());

        if (list != null && list.size() >= 6) {
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = mScreenHeight / 2;
            mListView.setLayoutParams(params);
        } else {
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            mListView.setLayoutParams(params);
        }
        mAdapter.notifyDataChanged(list);
    }


}
