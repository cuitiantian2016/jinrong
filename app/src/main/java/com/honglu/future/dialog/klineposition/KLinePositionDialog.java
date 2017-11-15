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
import com.honglu.future.config.Constant;
import com.honglu.future.events.ReceiverMarketMessageEvent;
import com.honglu.future.mpush.MPushUtil;
import com.honglu.future.ui.trade.bean.HoldPositionBean;
import com.honglu.future.ui.trade.bean.ProductListBean;
import com.honglu.future.util.SpUtil;
import com.honglu.future.widget.recycler.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    private String mExcode;
    private String mInstrumentId;
    private String mPushCode;
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
        final WindowManager manage = (WindowManager) mContext
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
        mAdapter = new KLinePositionDialogAdapter(mPresenter,KLinePositionDialog.this);
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
               if (mAdapter.getData() !=null
                       && mAdapter.getData().size()> 0
                       &&  mAdapter.getData().size()> mAdapter.getMPosition()){

                   HoldPositionBean holdPositionBean = mAdapter.getData().get(mAdapter.getMPosition());
                   mPresenter.closeOrder(
                           String.valueOf(holdPositionBean.getTodayPosition()),
                           SpUtil.getString(Constant.CACHE_TAG_UID),
                           SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN),
                           String.valueOf(mAdapter.getExpcNum()),
                           String.valueOf(holdPositionBean.getType()),
                           String.valueOf(mAdapter.getExPrice()),
                           holdPositionBean.getInstrumentId(),
                           holdPositionBean.getHoldAvgPrice(),
                           "GUOFU");
               }

            }
        });
    }
    /**
     * 设置数据
     * @param nameValue
     * @param mList
     */
     public KLinePositionDialog setPositionData(boolean isClosed,String excode,String instrumentId,String nameValue ,List<HoldPositionBean> mList){
         this.mNameValue = nameValue;
         this.isClosed = isClosed;
         this.mExcode = excode;
         this.mInstrumentId = instrumentId;
         this.mList = mList;
         this.mPushCode = excode+"|"+instrumentId;
         return KLinePositionDialog.this;
     }


    @Override
    public void dismiss() {
        super.dismiss();
        EventBus.getDefault().unregister(this);
        MPushUtil.pauseRequest();
    }

    public void requestMarket(String productList) {
        if (!TextUtils.isEmpty(productList))
            MPushUtil.requestMarket(productList);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMarketEventMainThread(ReceiverMarketMessageEvent event) {

    }

    public void showDialog(){
         if (TextUtils.isEmpty(mNameValue) || mList == null || mList.size() <=0){ return ;}
         show();
         EventBus.getDefault().register(this);
         mName.setText("平仓-"+mNameValue);
         if (isClosed){
             mPingcang.setEnabled(false);
             mPingcang.setText("休市中");
             mPingcang.setBackgroundResource(R.color.color_B1B1B3);
         }else {
             mPingcang.setEnabled(false);
             mPingcang.setText("平仓");
             mPingcang.setBackgroundResource(R.color.color_B1B1B3);
         }
          mAdapter.notifyDataChanged(mList);
          requestMarket(mPushCode);
     }

     //根据接口返回的数据改变平仓按钮
     public void setPingcangSatte(int type ,float mProfitLoss){
         if (isClosed){
             mPingcang.setBackgroundResource(R.color.color_B1B1B3);
             mPingcang.setEnabled(false);
             mPingcang.setText("休市中");
         }else {
             if (type == 1) { //1 跌  2涨
                 mPingcang.setEnabled(true);
                 mPingcang.setBackgroundResource(R.color.color_2CC593);
             }else {
                 mPingcang.setEnabled(true);
                 mPingcang.setBackgroundResource(R.color.color_FB4F4F);
             }
         }

         mYkprice.setText("￥"+mProfitLoss);
     }


    //产品详情
    @Override
    public void getProductDetailSuccess(ProductListBean bean) {
         if (bean !=null){
             mAdapter.setProductListBean(bean);
         }else {
             mAdapter.clearPosition();
         }
    }

    //委托平仓 / 快速平仓
    @Override
    public void closeOrderSuccess() {

    }
}
