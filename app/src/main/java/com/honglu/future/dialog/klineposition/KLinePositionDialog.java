package com.honglu.future.dialog.klineposition;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseDialog;
import com.honglu.future.ui.trade.bean.HoldPositionBean;
import com.honglu.future.ui.trade.bean.ProductListBean;
import com.honglu.future.widget.recycler.DividerItemDecoration;

import java.util.List;

/**
 * k 线页面持仓列表 Dialog
 * Created by zhuaibing on 2017/11/14
 */

public class KLinePositionDialog extends BaseDialog<KLinePositionDialogPresenter> implements KLinePositionDialogContract.View{
    private TextView mName;
    private ImageView mClose;
    private RecyclerView mRecyclerView;
    private TextView mYkprice;
    private TextView mPingcang;

    private List<HoldPositionBean> mList;
    private String mNameValue;  //名字
    private boolean isClosed; //是否休市
    private KLinePositionDialogAdapter mAdapter;


    @Override
    public void showLoading(String content) {
        if (!TextUtils.isEmpty(content)) {
            App.loadingContent(mContext, content);
        }
    }

    @Override
    public void stopLoading() {
        super.stopLoading();
         App.hideLoading();
    }

    public KLinePositionDialog(@NonNull Activity mContext) {
        super(mContext, R.style.DateDialog);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.init(KLinePositionDialog.this);
        setContentView(R.layout.dialog_kline_position);
        Window mWindow = this.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        WindowManager manage = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        params.width = manage.getDefaultDisplay().getWidth();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        mWindow.setAttributes(params);
        setCanceledOnTouchOutside(false);

        mName = (TextView) findViewById(R.id.tv_name);
        mClose = (ImageView) findViewById(R.id.iv_close);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mYkprice = (TextView) findViewById(R.id.tv_ykprice);
        mPingcang = (TextView) findViewById(R.id.tv_pingcang);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mAdapter = new KLinePositionDialogAdapter(mPresenter);
        mRecyclerView.setAdapter(mAdapter);

        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //平仓
        mPingcang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    /**
     * 设置数据
     * @param nameValue
     * @param mList
     */
     public KLinePositionDialog setPositionData(boolean isClosed,String nameValue ,List<HoldPositionBean> mList){
         this.mNameValue = nameValue;
         this.isClosed = isClosed;
         this.mList = mList;
         return KLinePositionDialog.this;
     }


     public void showDialog(){
         if (TextUtils.isEmpty(mNameValue) || mList == null || mList.size() <=0){ return ;}
         show();
         mName.setText("平仓-"+mNameValue);
         if (isClosed){
             mPingcang.setEnabled(false);
             mPingcang.setText("休市中");
             mPingcang.setTextColor(mContext.getResources().getColor(R.color.color_white));
             mPingcang.setBackgroundResource(R.color.color_B1B1B3);
         }else {
             mPingcang.setEnabled(false);
             mPingcang.setText("平仓");
             mPingcang.setBackgroundResource(R.color.color_B1B1B3);
         }
          mAdapter.notifyDataChanged(mList);
     }

    @Override
    public void getProductDetailSuccess(ProductListBean bean) {
         if (bean !=null){
             mAdapter.setProductListBean(bean);
         }else {
             mAdapter.clearPosition();
         }
    }
}
