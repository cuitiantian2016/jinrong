package com.honglu.future.dialog.klineposition;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseDialog;
import com.honglu.future.config.Constant;
import com.honglu.future.dialog.TradeTipDialog;
import com.honglu.future.ui.main.presenter.AccountPresenter;
import com.honglu.future.ui.trade.bean.HoldPositionBean;
import com.honglu.future.ui.trade.bean.ProductListBean;
import com.honglu.future.ui.trade.kchart.KLineMarketActivity;
import com.honglu.future.util.ToastUtil;
import com.honglu.future.util.TradeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuaibing on 2018/1/11
 */

public class KLinePositionListDialog extends BaseDialog<KLinePositionListPresenter> implements KLinePositionListContract.View{
    private  AccountPresenter mAccountPresenter;
    private TextView mName;
    private ImageView mClose;
    private TextView mYkprice;
    private TextView mPingCang;
    private ListView mListView;

    private PositionListDialogAdapter mAdapter;
    private ProductListBean mProductListBean;
    private HoldPositionBean mHoldPositionBean;
    private KLinePositionDetailsDialog mDetailsDialog; //耳机dialog
    private TradeTipDialog mTipDialog;

    private boolean mIsInItProductDetail = false;
    private boolean mIsClosed; //是否休市
    private String mExcode; //交易所code
    private String mInstrumentId; //产品code
    private String mNameValue;

    public KLinePositionListDialog(KLineMarketActivity mActivity, AccountPresenter accountPresenter){
        super(mActivity, R.style.DateDialog);
        this.mAccountPresenter =accountPresenter;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(KLinePositionListDialog.this);
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        if (!TextUtils.isEmpty(msg))
            ToastUtil.show(msg);
    }

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

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDetailsDialog !=null){
            mDetailsDialog.onDestroy();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_kline_position_list);
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
        mYkprice = (TextView) findViewById(R.id.tv_ykprice);
        mPingCang = (TextView) findViewById(R.id.tv_pingcang);
        mPingCang.setEnabled(false);
        mListView = (ListView) findViewById(R.id.lv_listView);

        mDetailsDialog = new KLinePositionDetailsDialog(mContext);
        mTipDialog = new TradeTipDialog(mContext, R.layout.layout_close_position_tip);
        mAdapter = new PositionListDialogAdapter(mContext);
        mListView.setAdapter(mAdapter);
        setListener();
    }



    private void setListener() {
        mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTipDialog.show();
            }
        });

        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mPingCang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (mHoldPositionBean !=null && mProductListBean !=null){
                   mDetailsDialog.showDetailsDialog(mHoldPositionBean,mProductListBean,mNameValue);
               }
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 mHoldPositionBean = (HoldPositionBean) parent.getItemAtPosition(position);
                 mAdapter.updateCheckState(position);
                 if (!mIsClosed && mProductListBean !=null){
                     mPingCang.setEnabled(true);
                     if (mHoldPositionBean.getType() == Constant.TYPE_BUY_DOWN) {
                         mPingCang.setBackgroundResource(R.color.color_2CC593);
                     }else {
                         mPingCang.setBackgroundResource(R.color.color_FB4F4F);
                     }
                 }
            }
        });
    }


    public void showPositionDialog(boolean isClosed, String excode, String instrumentId, String nameValue, List<HoldPositionBean> mList){
        if (TextUtils.isEmpty(excode)
                || TextUtils.isEmpty(instrumentId)
                || TextUtils.isEmpty(nameValue)
                || mList == null
                || mList.size() <=0){
            return;
        }
        this.mIsClosed = isClosed;
        this.mExcode = excode;
        this.mInstrumentId = instrumentId;
        this.mNameValue = nameValue;
        show();
        mName.setText(String.format(mContext.getString(R.string.close_pingcang_),nameValue));
        mPingCang.setEnabled(false);
        if (isClosed) {
            mPingCang.setText(mContext.getString(R.string.close_trade));
            mPingCang.setBackgroundResource(R.color.color_B1B1B3);
        } else {
            mPingCang.setText(mContext.getString(R.string.kuaisu_pingcang));
            mPingCang.setBackgroundResource(R.color.color_B1B1B3);
        }
        mAdapter.notifyDataChanged(mList);
        if (mPresenter !=null){
            mPresenter.getProductDetail(instrumentId);
        }
    }


    public void mPushRefresh(String exchangeID ,String instrumentID ,String lastPrice){
        if (!mIsInItProductDetail
                || !isShowing()
                || mAdapter ==null
                ||  mAdapter.getCount() <= 0
                || TextUtils.isEmpty(instrumentID)
                || !TextUtils.equals(instrumentID,mInstrumentId)){
            return;
        }

        if (mDetailsDialog !=null && mDetailsDialog.isShowing()){
            mDetailsDialog.mPushRefresh(lastPrice);
        }
        int firstVisiblePosition = mListView.getFirstVisiblePosition();
        int lastVisiblePosition = mListView.getLastVisiblePosition() ;

        double mNumCount = 0;
        for (int i = 0 ; i < mAdapter.getCount() ; i ++){
            int viewUpdateIndex = i;
            HoldPositionBean bean = mAdapter.getData().get(viewUpdateIndex);
            String closeProfitLoss = getActualProfitLoss(bean, lastPrice);
            mNumCount += parseDouble(closeProfitLoss);
            if ( viewUpdateIndex - firstVisiblePosition >= 0 && viewUpdateIndex  <= lastVisiblePosition) {
                View view = mListView.getChildAt(viewUpdateIndex - firstVisiblePosition);
                TextView mProfitLoss = (TextView) view.findViewById(R.id.tv_profit_loss);
                //实际盈亏
                bean.setLastPrice(lastPrice);
                mProfitLoss.setText(closeProfitLoss);
            }
        }

        if (mNumCount > 0) {
            mYkprice.setTextColor(mContext.getResources().getColor(R.color.color_FB4F4F));
            mYkprice.setText(String.format(mContext.getString(R.string.z_yuan),String.valueOf(mNumCount)));
        } else if (mNumCount < 0) {
            mYkprice.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
            mYkprice.setText(String.valueOf(mNumCount).replace("-",mContext.getString(R.string.f_yuan)));
        } else {
            mYkprice.setTextColor(mContext.getResources().getColor(R.color.color_333333));
            mYkprice.setText(String.format(mContext.getString(R.string.yuan),String.valueOf(mNumCount)));
        }
    }


    //产品详情
    @Override
    public void getProductDetailSuccess(ProductListBean bean) {
        if (bean  !=null){
            mProductListBean = bean;
            double mYkpriceValue = 0;
            if (mAdapter !=null && mAdapter.getCount() > 0){
                for (HoldPositionBean holdPositionBean: mAdapter.getData()){
                    String closeProfitLoss = getActualProfitLoss(holdPositionBean, mProductListBean.getLastPrice());
                    holdPositionBean.setTodayProfit(closeProfitLoss);
                    holdPositionBean.setLastPrice(bean.getLastPrice());
                    mYkpriceValue += parseDouble(closeProfitLoss);
                }
                mAdapter.notifyDataSetChanged();
            }
            if (mYkpriceValue > 0) {
                mYkprice.setTextColor(mContext.getResources().getColor(R.color.color_FB4F4F));
                mYkprice.setText(String.format(mContext.getString(R.string.z_yuan),String.valueOf(mYkpriceValue)));
            } else if (mYkpriceValue < 0) {
                mYkprice.setTextColor(mContext.getResources().getColor(R.color.color_2CC593));
                mYkprice.setText(String.valueOf(mYkpriceValue).replace("-",mContext.getString(R.string.f_yuan)));
            } else {
                mYkprice.setTextColor(mContext.getResources().getColor(R.color.color_333333));
                mYkprice.setText(String.format(mContext.getString(R.string.yuan),String.valueOf(mYkpriceValue)));
            }
            mIsInItProductDetail = true;
        }
    }


    //实际盈亏
    private String getActualProfitLoss(HoldPositionBean bean,String lastPrice){
        try {
            int type = bean.getType();
            String openAvgPrice = bean.getOpenAvgPrice();
            String priceTick = mProductListBean.getPriceTick();
            int volumeMultiple = mProductListBean.getVolumeMultiple();
            double price = Double.parseDouble(lastPrice);
            int tradeNum = bean.getPosition();
            return  String.valueOf(TradeUtil.getActualProfitLoss(type,openAvgPrice,priceTick,volumeMultiple,price,tradeNum));
        }catch (Exception e){
            e.printStackTrace();
            return "0";
        }
    }

    private double getYkprice(){
        double mZPrice = 0;
        for (HoldPositionBean holdPositionBean: mAdapter.getData()){
            String todayProfit = holdPositionBean.getTodayProfit();
            mZPrice += parseDouble(todayProfit);
        }
        return mZPrice;
    }


    private double parseDouble(String strdouble){
        try {
           return  Double.parseDouble(strdouble);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }




//    private ProductListBean getBean(){
//        ProductListBean productListBean = new ProductListBean();
//        productListBean
//    }
}
