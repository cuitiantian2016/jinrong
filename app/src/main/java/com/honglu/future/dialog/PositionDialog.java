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
import com.honglu.future.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuaibing on 2017/10/30
 */

public class PositionDialog extends Dialog{
    private Context mContext;
    private ImageView mClose;
    private TextView mName;
    private TextView mBuyNumber;
    private TextView mPosProfitLoss;
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
        Window  mWindow = this.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        WindowManager manage = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        params.width = manage.getDefaultDisplay().getWidth();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        mWindow.setAttributes(params);

        mClose = (ImageView) findViewById(R.id.iv_close);
        View headView = LayoutInflater.from(mContext).inflate(R.layout.layout_dialog_position_head,null);
        mName = (TextView) headView.findViewById(R.id.tv_name);
        mBuyNumber = (TextView) headView.findViewById(R.id.tv_buy_number);//买入手数
        mPosProfitLoss = (TextView) headView.findViewById(R.id.tv_position_profit_loss);//持仓盈亏
        mListView = (ListView) findViewById(R.id.lv_listView);
        mListView.addHeaderView(headView);
        mAdapter = new PositionDialogAdapter(mContext,null);
        mListView.setAdapter(mAdapter);
    }

    public void showPopupWind(){
        show();
        List<String>  list = new ArrayList<>();
        for (int i = 0 ; i < 2 ; i ++){
            list.add(new String("1111"));
        }
        if (list !=null && list.size() >= 6){
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = mScreenHeight / 2;
            mListView.setLayoutParams(params);
        }else {
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            mListView.setLayoutParams(params);
        }
        mAdapter.notifyDataChanged(list);
    }
}
