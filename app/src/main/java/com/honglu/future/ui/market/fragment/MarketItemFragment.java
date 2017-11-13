package com.honglu.future.ui.market.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.honglu.future.R;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.config.Constant;
import com.honglu.future.events.EventBusConstant;
import com.honglu.future.events.MarketRefreshEvent;
import com.honglu.future.ui.market.adapter.MarketListAdapter;
import com.honglu.future.ui.market.bean.MarketnalysisBean;
import com.honglu.future.ui.market.contract.MarketItemContract;
import com.honglu.future.ui.market.presenter.MarketItemPresenter;
import com.honglu.future.util.SpUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zhuaibing on 2017/11/10
 */

public class MarketItemFragment extends BaseFragment<MarketItemPresenter> implements MarketItemContract.View {

    @BindView(R.id.market_recycler)
    RecyclerView mMarketRecycler;
    @BindView(R.id.market_smart_view)
    SmartRefreshLayout mRefreshView;

    private MarketListAdapter mAdapter;
    private List<MarketnalysisBean.ListBean.QuotationDataListBean> mList;
    private String mTabSelectType;
    private String mPushCode;

    public Bundle setArgumentData(String mTabSelectType, List<MarketnalysisBean.ListBean.QuotationDataListBean> mList) {
        Bundle bundle = new Bundle();
        bundle.putString("tab_select", mTabSelectType);
        bundle.putSerializable("mList", (Serializable) mList);
        return bundle;
    }

    public String getPushCode() {
        return mPushCode;
    }

    public String getTabSelectType() {

        return mTabSelectType;
    }

    public List<MarketnalysisBean.ListBean.QuotationDataListBean> getList() {
        return mList;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_market_item_layout;
    }

    @Override
    public void initPresenter() {
         mPresenter.init(MarketItemFragment.this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMarketDataEventMainThread(MarketRefreshEvent event) {
           if (event.type.equals(EventBusConstant.OPTIONALQUOTES_ADD_MARKET)
                   || event.type.equals(EventBusConstant.ITEMFRAGMENT_ADD_MARKET)){
                //OptionalQuotesActivity 自选行情添加
               if (MarketFragment.ZXHQ_TYPE.equals(mTabSelectType) ){
                    if (isSaveZXBean(event.bean)){
                        mList.add(event.bean);
                        mAdapter.setStopAnimatorTag();
                        mAdapter.refreshData(mTabSelectType, mList);
                        mPushCode = mosaicMPushCode(mList);
                        if (mPushCodeRefreshListener !=null){
                            mPushCodeRefreshListener.OnMPushCodeRefresh(mPushCode);
                        }
                    }
                }else {
                    refreshState("1", event.bean.getExcode(), event.bean.getInstrumentID());
                    mAdapter.refreshData(mTabSelectType, mList);
                }
           }else if (event.type.equals(EventBusConstant.OPTIONALQUOTES_DEL_MARKET)
                   || event.type.equals(EventBusConstant.ITEMFRAGMENT_DEL_MARKET)){
               //OptionalQuotesActivity 自选行情删除
               if (MarketFragment.ZXHQ_TYPE.equals(mTabSelectType) ){
                   refreshZX(event.bean.getExcode(), event.bean.getInstrumentID());
                   mAdapter.setStopAnimatorTag();
                   mAdapter.refreshData(mTabSelectType, mList);
                   mPushCode = mosaicMPushCode(mList);
                   if (mPushCodeRefreshListener !=null){
                       mPushCodeRefreshListener.OnMPushCodeRefresh(mPushCode);
                   }
               }else {
                   refreshState("0", event.bean.getExcode(), event.bean.getInstrumentID());
                   mAdapter.refreshData(mTabSelectType, mList);
               }
           }else if (event.type.equals(EventBusConstant.OPTIONALQUOTES_SORT_MARKET)){
                   //排序
                  if (MarketFragment.ZXHQ_TYPE.equals(mTabSelectType)){
                      mAdapter.refreshData(mTabSelectType,getZxMarketList());
                  }
           }
    }

    @Override
    public void loadData() {
        EventBus.getDefault().register(this);
        mTabSelectType = getArguments().getString("tab_select");
        mList = (List<MarketnalysisBean.ListBean.QuotationDataListBean>) getArguments().getSerializable("mList");
        if (mList == null){
            mList = new ArrayList<>();
        }
        mPushCode = mosaicMPushCode(mList);
        LinearLayout footerView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_quotes_optional_add, null);
        TextView mAddAptional = (TextView) footerView.findViewById(R.id.text_add_qptional);
        mMarketRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mMarketRecycler.setFocusable(false);
        mAdapter = new MarketListAdapter();
        mMarketRecycler.setAdapter(mAdapter);
        mAdapter.addFooterView(footerView, DensityUtil.dp2px(85));
        mAdapter.setMarketItemFragment(MarketItemFragment.this);
        mAdapter.refreshData(mTabSelectType, mList);
        //添加自选
        mAddAptional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onAddAptional();
                }
            }
        });

        mRefreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getHttpRefreshData();
                mRefreshView.finishRefresh();
            }
        });
    }


    public OnMPushCodeRefreshListener mPushCodeRefreshListener;

    public void setOnMPushCodeRefreshListener(OnMPushCodeRefreshListener mPushCodeRefreshListener){
        this.mPushCodeRefreshListener = mPushCodeRefreshListener;
    }


    public interface OnMPushCodeRefreshListener{
        void OnMPushCodeRefresh(String mpushCode);
    }

    public OnAddAptionalListener listener;
    public void setOnAddAptionalListener(OnAddAptionalListener listener) {
        this.listener = listener;
    }

    public interface OnAddAptionalListener {
        void onAddAptional();
    }


    public OnZXMarketListListener zxMarketListListener;
    public void setOnZXMarketListListener(OnZXMarketListListener zxMarketListListener){
        this.zxMarketListListener = zxMarketListListener;
    }
    public interface OnZXMarketListListener{
        List<MarketnalysisBean.ListBean.QuotationDataListBean> OnZXMarketList();
    }

    public void refresh(String isAdd, MarketnalysisBean.ListBean.QuotationDataListBean bean) {
        if ("1".equals(isAdd)){
            EventBus.getDefault().post(new MarketRefreshEvent(EventBusConstant.ITEMFRAGMENT_DEL_MARKET, mTabSelectType, bean));
        }else {
            EventBus.getDefault().post(new MarketRefreshEvent(EventBusConstant.ITEMFRAGMENT_ADD_MARKET, mTabSelectType, bean));
        }
    }

    public void refreshState(String isAdd, String excode, String instrumentID) {
        //img true 没添加  false 已添加
        if (!TextUtils.isEmpty(excode)
                && !TextUtils.isEmpty(instrumentID)
                && mList != null
                && mList.size() > 0) {
            //取反向值
            for (MarketnalysisBean.ListBean.QuotationDataListBean bean : mList) {
                if (excode.equals(bean.getExcode()) && instrumentID.equals(bean.getInstrumentID())) {
                    bean.setIcAdd(isAdd);
                    break;
                }
            }
        }
    }

    //删除自选数据
    public void refreshZX(String excode, String instrumentID) {
        if (!TextUtils.isEmpty(excode)
                && !TextUtils.isEmpty(instrumentID)
                && mList != null
                && mList.size() > 0) {
            ListIterator<MarketnalysisBean.ListBean.QuotationDataListBean> iterator = mList.listIterator();
            while (iterator.hasNext()) {
                MarketnalysisBean.ListBean.QuotationDataListBean bean = iterator.next();
                if (excode.equals(bean.getExcode()) && instrumentID.equals(bean.getInstrumentID())) {
                    iterator.remove();
                    break;
                }
            }
        }
    }


    //是否能保存当前自选bean
    private boolean isSaveZXBean(MarketnalysisBean.ListBean.QuotationDataListBean bean){
        boolean isSave = true;
        if (mList !=null
                && mList.size() > 0
                && bean !=null
                && !TextUtils.isEmpty(bean.getExcode())
                && !TextUtils.isEmpty(bean.getInstrumentID())){
            for (MarketnalysisBean.ListBean.QuotationDataListBean listBean : mList){
                if (bean.getExcode().equals(listBean.getExcode()) && bean.getInstrumentID().equals(listBean.getInstrumentID())){
                    isSave = false;
                    break;
                }
            }
        }
        return isSave;
    }

    //拼接 MPush code
    private String mosaicMPushCode(List<MarketnalysisBean.ListBean.QuotationDataListBean> list) {
        StringBuilder builder = new StringBuilder();
        if (list != null && list.size() > 0) {

            for (MarketnalysisBean.ListBean.QuotationDataListBean bean : list) {
                if (!TextUtils.isEmpty(bean.getExcode()) && !TextUtils.isEmpty(bean.getInstrumentID())) {
                    if (builder.length() > 0) {
                        builder.append(",");
                    }
                    builder.append(bean.getExcode() + "|" + bean.getInstrumentID());
                }
            }
        }
        return builder.toString();
    }


    /**
     * 根据mPush 传过来的值改变数据
     *
     * @param lastPrice    最新价
     * @param chg          涨跌幅
     * @param openInterest 持仓量
     */
    public void mPushRefresh(String instrumentID, String lastPrice, String chg, String openInterest) {
        if (mList != null
                && mList.size() > 0
                && !TextUtils.isEmpty(instrumentID)) {
            mAdapter.setOldPriceList(mList);
            for (MarketnalysisBean.ListBean.QuotationDataListBean mBean : mList) {
                if (instrumentID.equals(mBean.getInstrumentID())) {
                    mBean.setLastPrice(lastPrice);
                    mBean.setChg(chg);
                    mBean.setOpenInterest(openInterest);
                    mAdapter.refreshData(mTabSelectType, mList);
                    break;
                }
            }

        }
    }

    //获取自选数据
    private List<MarketnalysisBean.ListBean.QuotationDataListBean> getZxMarketList() {
        String zxMarketJson = SpUtil.getString(Constant.ZX_MARKET_KEY);
        if (!TextUtils.isEmpty(zxMarketJson)) {
            Gson gson = new Gson();
            try {
                List<MarketnalysisBean.ListBean.QuotationDataListBean> mZxMarketList = gson.fromJson(zxMarketJson,
                        new TypeToken<List<MarketnalysisBean.ListBean.QuotationDataListBean>>() {
                        }.getType());
                return mZxMarketList;
            } catch (JsonSyntaxException e) {
            }
        }
        return null;
    }


    //获取主力合约
    private List<MarketnalysisBean.ListBean.QuotationDataListBean> getZlhyMarketList(List<MarketnalysisBean.ListBean> list) {
        List<MarketnalysisBean.ListBean.QuotationDataListBean> mZlhyMarketList = new ArrayList<>();
        for (MarketnalysisBean.ListBean bean : list) {
            if (bean.getQuotationDataList() != null && bean.getQuotationDataList().size() > 0) {
                mZlhyMarketList.addAll(bean.getQuotationDataList());
            }
        }
        return mZlhyMarketList;
    }


    //给每条数据添加 Excode
    private List<MarketnalysisBean.ListBean> addItemDataExcode(List<MarketnalysisBean.ListBean> list, List<MarketnalysisBean.ListBean.QuotationDataListBean> mZxMarketList) {
        if (mZxMarketList != null && mZxMarketList.size() > 0) {
            for (MarketnalysisBean.ListBean listBean : list) {
                if (!TextUtils.isEmpty(listBean.getExcode())
                        && listBean.getQuotationDataList() != null
                        && listBean.getQuotationDataList().size() > 0) {

                    for (MarketnalysisBean.ListBean.QuotationDataListBean mBean : listBean.getQuotationDataList()) {
                        mBean.setExcode(listBean.getExcode());
                        for (MarketnalysisBean.ListBean.QuotationDataListBean zxBean : mZxMarketList) {
                            if (!TextUtils.isEmpty(mBean.getInstrumentID()) && mBean.getInstrumentID().equals(zxBean.getInstrumentID())) {
                                //已经存在自选 img 显示删除
                                mBean.setIcAdd("1");
                            } else {
                                //不存在自选 img 显示添加
                                if (!"1".equals(mBean.getIcAdd())){
                                    mBean.setIcAdd("0");
                                }
                            }

                        }
                    }
                }
            }
        } else {
            for (MarketnalysisBean.ListBean listBean : list) {

                if (!TextUtils.isEmpty(listBean.getExcode())
                        && listBean.getQuotationDataList() != null
                        && listBean.getQuotationDataList().size() > 0) {

                    for (MarketnalysisBean.ListBean.QuotationDataListBean mBean : listBean.getQuotationDataList()) {
                        mBean.setIcAdd("0");
                        mBean.setExcode(listBean.getExcode());
                    }
                }
            }
        }
        return list;
    }


    private void getHttpRefreshData(){
       if (MarketFragment.ZXHQ_TYPE.equals(mTabSelectType)){
           /*
           List<MarketnalysisBean.ListBean.QuotationDataListBean> zxMarketList = getZxMarketList();
           if (zxMarketList == null || zxMarketList.size() <= 0){
               mList.clear();
           }else {
               mList = zxMarketList;
           }
             mAdapter.refreshData(mTabSelectType, mList);
             */
       }else {
             mPresenter.getMarketData();
       }
    }
    @Override
    public void getMarketData(MarketnalysisBean alysiBean) {
        if (alysiBean  == null
                || alysiBean.getList() == null
                || TextUtils.isEmpty(mTabSelectType)
                || MarketFragment.ZXHQ_TYPE.equals(mTabSelectType)){
            return;
        }

        List<MarketnalysisBean.ListBean.QuotationDataListBean> zxMarketList = zxMarketListListener.OnZXMarketList();
        List<MarketnalysisBean.ListBean> allList = addItemDataExcode(alysiBean.getList(), zxMarketList);

        if (MarketFragment.ZLHY_TYPE.equals(mTabSelectType)){

            List<MarketnalysisBean.ListBean.QuotationDataListBean> zlhyMarketList = getZlhyMarketList(allList);
            if (zlhyMarketList == null || zlhyMarketList.size() <=0){
                 mList.clear();
            }else {
                 mList = zlhyMarketList;
            }
            mAdapter.refreshData(mTabSelectType, mList);
        }else {
             for (MarketnalysisBean.ListBean listBean : allList){
                  if (mTabSelectType.equals(listBean.getExcode())){
                      List<MarketnalysisBean.ListBean.QuotationDataListBean> dataList = listBean.getQuotationDataList();
                      if (dataList == null || dataList.size() <=0){
                          mList.clear();
                      }else {
                          mList = dataList;
                      }
                      mAdapter.refreshData(mTabSelectType, mList);
                      break;
                  }
             }
        }

    }
}
