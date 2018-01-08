package com.honglu.future.ui.market.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.honglu.future.ui.market.bean.ListBean;
import com.honglu.future.ui.market.bean.MarketnalysisBean;
import com.honglu.future.ui.market.bean.QuotationDataListBean;
import com.honglu.future.ui.market.contract.MarketItemContract;
import com.honglu.future.ui.market.presenter.MarketItemPresenter;
import com.honglu.future.ui.trade.bean.RealTimeBean;
import com.honglu.future.util.SpUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zhuaibing on 2017/11/10
 */

public class MarketItemFragment extends BaseFragment<MarketItemPresenter> implements MarketItemContract.View {

    @BindView(R.id.lv_listView)
    ListView mListView;
    @BindView(R.id.market_smart_view)
    SmartRefreshLayout mRefreshView;
    private View mTitleLine;
    private TextView mQuoteChange;
    private TextView mHavedPositions;
    private View mEmptyView;

    private MarketListAdapter mAdapter;
    private List<RealTimeBean.Data> mRealTimeList;
    private List<ListBean> mAllList;
    private String mTabSelectType;
    private String title;
    private String mPushCode;
    private boolean mIsPushRefresh = true;



    public Bundle setArgumentData(String mTabSelectType, List<ListBean> mAllList, List<QuotationDataListBean> mList, String title) {
        Bundle bundle = new Bundle();
        bundle.putString("tab_select", mTabSelectType);
        bundle.putString("title", title);
        bundle.putSerializable("mList", (Serializable) mList);
        if (TextUtils.equals(MarketFragment.ZXHQ_TYPE,mTabSelectType)){
            bundle.putSerializable("mAllList", (Serializable) mAllList);
        }
        return bundle;
    }

    public String getPushCode() {
        return mPushCode;
    }

    public String getTabSelectType() {

        return mTabSelectType;
    }

    public void notifyDataChanged(){
        if (mAdapter !=null){
            mAdapter.notifyDataSetChanged();
            if (isVisible()){
                getRealTimeData();
            }
        }
    }
    //获取 adapter数据
    public List<QuotationDataListBean> getList() {
        return mAdapter != null && mAdapter.getData().size() > 0 ? mAdapter.getData() : null;
    }

    //添加自选
    private void addAdapterBean(QuotationDataListBean bean) {
        mAdapter.getData().add(bean);
        boolean compareData = compareData(mAdapter.getData(), mRealTimeList);
        if (compareData){mIsPushRefresh = false;}
        mAdapter.notifyDataSetChanged();
        if (compareData){ mIsPushRefresh = true;}
        mPushCode = mosaicMPushCode(mAdapter.getData());
        if (mPushCodeRefreshListener != null) {
            mPushCodeRefreshListener.onMPushCodeRefresh(mPushCode);
        }
    }

    //删除自选
    private void delAdapterBean(String excode, String instrumentID) {
        boolean refreshZX = refreshZX(excode, instrumentID);
        if (refreshZX) {
            boolean compareData = compareData(mAdapter.getData(), mRealTimeList);
            if (compareData){mIsPushRefresh = false;}
            mAdapter.notifyDataSetChanged();
            if (compareData){mIsPushRefresh = true;}
            mPushCode = mosaicMPushCode(mAdapter.getData());
            if (mPushCodeRefreshListener != null) {
                mPushCodeRefreshListener.onMPushCodeRefresh(mPushCode);
            }
        }
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
    public void onEventMainThread(MarketRefreshEvent event) {
        if (event.type.equals(EventBusConstant.OPTIONALQUOTES_ADD_MARKET)
                || event.type.equals(EventBusConstant.ITEMFRAGMENT_ADD_MARKET)) {
            //OptionalQuotesActivity 自选行情添加
            if (MarketFragment.ZXHQ_TYPE.equals(mTabSelectType)) {
                if (isSaveZXBean(event.bean)) {
                    addAdapterBean(event.bean);
                    mTitleLine.setVisibility(mAdapter.getData().size() > 0 ? View.VISIBLE : View.INVISIBLE);
                    saveData();
                    setEmptyView(mAdapter.getData());
                }
            }
        } else if (event.type.equals(EventBusConstant.OPTIONALQUOTES_DEL_MARKET)
                || event.type.equals(EventBusConstant.ITEMFRAGMENT_DEL_MARKET)) {
            //OptionalQuotesActivity 自选行情删除
            if (MarketFragment.ZXHQ_TYPE.equals(mTabSelectType)) {
                delAdapterBean(event.bean.getExchangeID(), event.bean.getInstrumentID());
                mTitleLine.setVisibility(mAdapter.getData().size() > 0 ? View.VISIBLE : View.INVISIBLE);
                saveData();
                setEmptyView(mAdapter.getData());
            }
        } else if (event.type.equals(EventBusConstant.OPTIONALQUOTES_SORT_MARKET)) {
            //排序
            if (MarketFragment.ZXHQ_TYPE.equals(mTabSelectType)) {
                mAdapter.notifyDataChanged(true,getZxMarketList());
            }
        }else if (event.type.equals(EventBusConstant.KLINE_MARKET_ADD_MARKET)){
            //添加自选择
            if (MarketFragment.ZXHQ_TYPE.equals(mTabSelectType)) {
                  String exchangeID = event.mTabSelectType;
                  String instrumentID = event.mInstrumentID;
                  if (isSaveZXBean(exchangeID,instrumentID)){
                      addZXMarketItem(exchangeID,instrumentID);
                      saveData();
                      setEmptyView(mAdapter.getData());
                  }
            }
        }else if (event.type.equals(EventBusConstant.KLINE_MARKET_DEL_MARKET)){
            //删除自选
            if (MarketFragment.ZXHQ_TYPE.equals(mTabSelectType)) {
                String exchangeID = event.mTabSelectType;
                String instrumentID = event.mInstrumentID;
                delAdapterBean(exchangeID,instrumentID);
                mTitleLine.setVisibility(mAdapter.getData().size() > 0 ? View.VISIBLE : View.INVISIBLE);
                saveData();
                setEmptyView(mAdapter.getData());
            }
        }
    }


    private void saveData(){
        if (mAdapter != null) {
            List<QuotationDataListBean> zxList = mAdapter.getData();
            if (zxList != null && zxList.size() > 0) {
                Gson gson = new Gson();
                String toJson = gson.toJson(zxList);
                SpUtil.putString(Constant.ZX_MARKET_KEY, toJson);
            } else {
                SpUtil.putString(Constant.ZX_MARKET_KEY, "");
            }
        }
    }


    private void setEmptyView(List<QuotationDataListBean> list){
        if (list == null || list.size() == 0){
            mEmptyView.setVisibility(View.VISIBLE);
        }else {
            mEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void loadData() {
        EventBus.getDefault().register(this);
        mTabSelectType = getArguments().getString("tab_select");
        title = getArguments().getString("title");
        if (TextUtils.equals(MarketFragment.ZXHQ_TYPE,mTabSelectType)){
            mAllList = (List<ListBean>) getArguments().getSerializable("mAllList");
        }
        List<QuotationDataListBean> mList = (List<QuotationDataListBean>) getArguments().getSerializable("mList");
        if (mList == null){mList = new ArrayList<>(); }
        mPushCode = mosaicMPushCode(mList);
        LinearLayout footerView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_quotes_optional_add, null);
        TextView mAddAptional = (TextView) footerView.findViewById(R.id.text_add_qptional);
        mEmptyView = footerView.findViewById(R.id.ll_empty_view);
        LinearLayout headView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_market_item_title, null);
        mHavedPositions = (TextView) headView.findViewById(R.id.text_haved_positions);
        mQuoteChange = (TextView) headView.findViewById(R.id.text_quote_change);
        mTitleLine = headView.findViewById(R.id.v_titleLine);
        mTitleLine.setVisibility(mList.size() > 0 ? View.VISIBLE : View.INVISIBLE);
        if (MarketFragment.ZXHQ_TYPE.equals(mTabSelectType)){
            mAddAptional.setVisibility(View.VISIBLE);
        }else {
            mAddAptional.setVisibility(View.GONE);
        }
        mListView.addHeaderView(headView);
        mListView.addFooterView(footerView);
        mAdapter = new MarketListAdapter(MarketItemFragment.this,mTabSelectType,mList,title);
        mListView.setAdapter(mAdapter);
        setEmptyView(mList);

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

        //涨跌幅、值切换
        mQuoteChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdapter !=null && mAdapter.getCount() > 0) {
                    boolean isChange = !mAdapter.getIsChange();
                    if (isChange) {
                        mQuoteChange.setText(mContext.getString(R.string.text_quote_change_value));
                    } else {
                        mQuoteChange.setText(mContext.getString(R.string.text_quote_change));
                    }
                    mAdapter.setChangeSelect(isChange);
                }
            }
        });

        //持仓量/成交量切换
        mHavedPositions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (mAdapter !=null && mAdapter.getCount() > 0){
                   boolean isHavedPositions = !mAdapter.getmIsHavedPositions();
                   if (isHavedPositions){
                       mHavedPositions.setText("成交量");
                   }else {
                       mHavedPositions.setText("持仓量");
                   }
                   mAdapter.setHavedPositionsSelect(isHavedPositions);
               }
            }
        });


        if (MarketFragment.ZXHQ_TYPE.equals(mTabSelectType)){
            if (mList !=null && mList.size() > 0){
                mPresenter.getRealTimeData(mPushCode);
            }
        }else if (MarketFragment.ZLHY_TYPE.equals(mTabSelectType)){
            List<QuotationDataListBean> zxMarketList = getZxMarketList();
            if (zxMarketList == null || zxMarketList.size() <=0){
                mPresenter.getRealTimeData(mPushCode);
            }
        }
    }

    //tab 切换时调用接口
    public void getRealTimeData(){
        if (mPresenter !=null && !TextUtils.isEmpty(mPushCode)){
            mPresenter.getRealTimeData(mPushCode);
        }
    }

    public OnMPushCodeRefreshListener mPushCodeRefreshListener;

    public void setOnMPushCodeRefreshListener(OnMPushCodeRefreshListener mPushCodeRefreshListener) {
        this.mPushCodeRefreshListener = mPushCodeRefreshListener;
    }


    public interface OnMPushCodeRefreshListener {
        void onMPushCodeRefresh(String mpushCode);
    }

    public OnAddAptionalListener listener;

    public void setOnAddAptionalListener(OnAddAptionalListener listener) {
        this.listener = listener;
    }

    public interface OnAddAptionalListener {
        void onAddAptional();
    }


    public OnZXMarketListListener zxMarketListListener;

    public void setOnZXMarketListListener(OnZXMarketListListener zxMarketListListener) {
        this.zxMarketListListener = zxMarketListListener;
    }

    public interface OnZXMarketListListener {
        List<QuotationDataListBean> onZXMarketList();
    }

    public void refresh(String isAdd,QuotationDataListBean bean) {
        if ("1".equals(isAdd)) {
            EventBus.getDefault().post(new MarketRefreshEvent(EventBusConstant.ITEMFRAGMENT_DEL_MARKET, mTabSelectType, bean));
        } else {
            EventBus.getDefault().post(new MarketRefreshEvent(EventBusConstant.ITEMFRAGMENT_ADD_MARKET, mTabSelectType, bean));
        }
    }


    //添加自选
    private void addZXMarketItem(String excode,String instrumentID) {
         if (mAllList !=null && mAllList.size() > 0){
             boolean isFor = true;
             for (ListBean listBean : mAllList){
                 if (TextUtils.equals(listBean.getExcode(),excode)
                         && listBean.getQuotationDataList() !=null
                         && listBean.getQuotationDataList().size() > 0){
                     for (QuotationDataListBean dataBean : listBean.getQuotationDataList()){
                         if (TextUtils.equals(instrumentID,dataBean.getInstrumentID())){
                             isFor = false;
                             addAdapterBean(dataBean);
                             break;
                         }
                     }
                     if (!isFor){
                         break;
                     }
                 }
             }
         }
    }

    //删除自选数据
    public boolean refreshZX(String excode, String instrumentID) {
        boolean isRemove = false;
        if (!TextUtils.isEmpty(excode)
                && !TextUtils.isEmpty(instrumentID)
                && mAdapter !=null
                && mAdapter.getData().size() > 0) {
            ListIterator<QuotationDataListBean> iterator = mAdapter.getData().listIterator();
            while (iterator.hasNext()) {
                QuotationDataListBean bean = iterator.next();
                if (excode.equals(bean.getExchangeID()) && instrumentID.equals(bean.getInstrumentID())) {
                    iterator.remove();
                    isRemove = true;
                    break;
                }
            }
        }
        return isRemove;
    }


    //是否能保存当前自选bean
    private boolean isSaveZXBean(QuotationDataListBean bean) {
        boolean isSave = true;
        if (mAdapter !=null && mAdapter.getData().size() > 0
                && bean != null
                && !TextUtils.isEmpty(bean.getExchangeID())
                && !TextUtils.isEmpty(bean.getInstrumentID())) {
            for (QuotationDataListBean listBean : mAdapter.getData()) {
                if (bean.getExchangeID().equals(listBean.getExchangeID()) && bean.getInstrumentID().equals(listBean.getInstrumentID())) {
                    isSave = false;
                    break;
                }
            }
        }
        return isSave;
    }


   private boolean isSaveZXBean(String exchangeID,String instrumentID){
       boolean isSave = true;
       if (TextUtils.isEmpty(exchangeID) || TextUtils.isEmpty(instrumentID)){
           isSave = false;
       }else  if (mAdapter !=null
               && mAdapter.getData() !=null
               && mAdapter.getData().size() >0){
           for (QuotationDataListBean listBean : mAdapter.getData()) {
               if (exchangeID.equals(listBean.getExchangeID()) && instrumentID.equals(listBean.getInstrumentID())) {
                   isSave = false;
                   break;
               }
           }
       }
       return isSave;
   }

    //拼接 MPush code
    private String mosaicMPushCode(List<QuotationDataListBean> list) {
        StringBuilder builder = new StringBuilder();
        if (list != null && list.size() > 0) {

            for (QuotationDataListBean bean : list) {
                if (!TextUtils.isEmpty(bean.getExchangeID()) && !TextUtils.isEmpty(bean.getInstrumentID())) {
                    if (builder.length() > 0) {
                        builder.append(",");
                    }
                    builder.append(bean.getExchangeID());
                    builder.append("|");
                    builder.append(bean.getInstrumentID());
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
    public void mPushRefresh(final String instrumentID, final String lastPrice, final String chg, final String openInterest , final String change) {
        if (isHidden() || mAdapter ==null ||  mAdapter.getData().size() <= 0 || TextUtils.isEmpty(instrumentID)){
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int updatePosition = mAdapter.getUpdatePosition(instrumentID);
                int firstVisiblePosition = mListView.getFirstVisiblePosition();
                int lastVisiblePosition = mListView.getLastVisiblePosition() ;
                int viewUpdateIndex = updatePosition + mListView.getHeaderViewsCount();

                if (updatePosition != -1
                        && mAdapter.getData().size() > updatePosition
                        && viewUpdateIndex - firstVisiblePosition >= 0
                        && viewUpdateIndex  < lastVisiblePosition  + mListView.getHeaderViewsCount()){
                        View view = mListView.getChildAt(viewUpdateIndex - firstVisiblePosition);
                        TextView mTvQuoteChange = (TextView) view.findViewById(R.id.text_quote_change);
                        TextView mTvLatestPrice = (TextView) view.findViewById(R.id.text_latest_price);
                        TextView mTvHavedPositions = (TextView) view.findViewById(R.id.text_haved_positions);
                        View mColorView = view.findViewById(R.id.v_color_shansuo);
                        QuotationDataListBean listBean = mAdapter.getData().get(updatePosition);
                        String mOldChg = listBean.getChg();
                        String mOldLastPrice = listBean.getLastPrice();
                        String mOldopenInterest = listBean.getOpenInterest();
                        listBean.setChg(chg);
                        listBean.setLastPrice(lastPrice);
                        listBean.setOpenInterest(openInterest);
                        listBean.setChange(change);
                        if (mIsPushRefresh){
                            mAdapter.updateItemView(mTvQuoteChange,mTvLatestPrice,mTvHavedPositions,mColorView,mOldChg,mOldLastPrice,mOldopenInterest,chg,lastPrice,openInterest,change);
                        }
                }
            }
        });
    }

    //获取自选数据
    private List<QuotationDataListBean> getZxMarketList() {
        String zxMarketJson = SpUtil.getString(Constant.ZX_MARKET_KEY);
        if (!TextUtils.isEmpty(zxMarketJson)) {
            Gson gson = new Gson();
            try {
                List<QuotationDataListBean> mZxMarketList = gson.fromJson(zxMarketJson,
                        new TypeToken<List<QuotationDataListBean>>() {
                        }.getType());
                return mZxMarketList;
            } catch (JsonSyntaxException e) {
            }
        }
        return null;
    }


    //获取主力合约
    private List<QuotationDataListBean> getZlhyMarketList(List<ListBean> list) {
        List<QuotationDataListBean> mZlhyMarketList = new ArrayList<>();
        for (ListBean bean : list) {
            if (bean.getQuotationDataList() != null && bean.getQuotationDataList().size() > 0) {
                mZlhyMarketList.addAll(bean.getQuotationDataList());
            }
        }
        return mZlhyMarketList;
    }


    //给每条数据添加 Excode
    private List<ListBean> addItemDataExcode(List<ListBean> list, List<QuotationDataListBean> mZxMarketList) {
        if (mZxMarketList != null && mZxMarketList.size() > 0) {
            for (ListBean listBean : list) {
                if (!TextUtils.isEmpty(listBean.getExcode())
                        && listBean.getQuotationDataList() != null
                        && listBean.getQuotationDataList().size() > 0) {

                    for (QuotationDataListBean mBean : listBean.getQuotationDataList()) {
                        for (QuotationDataListBean zxBean : mZxMarketList) {
                            if (!TextUtils.isEmpty(mBean.getInstrumentID()) && mBean.getInstrumentID().equals(zxBean.getInstrumentID())) {
                                //已经存在自选 img 显示删除
                                mBean.setIcAdd("1");
                            } else {
                                //不存在自选 img 显示添加
                                if (!"1".equals(mBean.getIcAdd())) {
                                    mBean.setIcAdd("0");
                                }
                            }

                        }
                    }
                }
            }
        } else {
            for (ListBean listBean : list) {

                if (!TextUtils.isEmpty(listBean.getExcode())
                        && listBean.getQuotationDataList() != null
                        && listBean.getQuotationDataList().size() > 0) {

                    for (QuotationDataListBean mBean : listBean.getQuotationDataList()) {
                        mBean.setIcAdd("0");
                    }
                }
            }
        }
        return list;
    }


    private void getHttpRefreshData() {
        //if (MarketFragment.ZXHQ_TYPE.equals(mTabSelectType)) {
           /*
           List<MarketnalysisBean.ListBean.QuotationDataListBean> zxMarketList = getZxMarketList();
           if (zxMarketList == null || zxMarketList.size() <= 0){
               mList.clear();
           }else {
               mList = zxMarketList;
           }
             mAdapter.refreshData(mTabSelectType, mList);
             */
        //} else {

        //}
        mPresenter.getMarketData();
    }

    @Override
    public void getMarketData(MarketnalysisBean alysiBean) {
        if (alysiBean == null
                || mAdapter == null
                || alysiBean.getList() == null
                || TextUtils.isEmpty(mTabSelectType)) {
            return;
        }


        if (MarketFragment.ZXHQ_TYPE.equals(mTabSelectType)){
            List<QuotationDataListBean> zxDataList = mAdapter.getData();
            for (QuotationDataListBean zxBean : zxDataList){

                for (ListBean listBean : alysiBean.getList()){

                    if (zxBean.getExchangeID().equals(listBean.getExcode())){
                        for (QuotationDataListBean allBean : listBean.getQuotationDataList()){

                            if (zxBean.getInstrumentID().equals(allBean.getInstrumentID())){
                                zxBean.setLastPrice(allBean.getLastPrice());
                                zxBean.setChg(allBean.getChg());
                                zxBean.setOpenInterest(allBean.getOpenInterest());
                                zxBean.setChange(allBean.getChange());
                            }
                        }
                    }
                }
            }
            mAdapter.notifyDataSetChanged();
            if (mAdapter.getData() !=null && mAdapter.getData().size() > 0){
                getRealTimeData();
            }
        }else {
            List<QuotationDataListBean> zxMarketList = zxMarketListListener.onZXMarketList();
            List<ListBean> allList = addItemDataExcode(alysiBean.getList(), zxMarketList);
            if (MarketFragment.ZLHY_TYPE.equals(mTabSelectType)) {
                List<QuotationDataListBean> zlhyMarketList = getZlhyMarketList(allList);
                mAdapter.notifyDataChanged(true,zlhyMarketList);
                if (mAdapter.getData() !=null && mAdapter.getData().size() > 0){
                    getRealTimeData();
                }
            } else {
                for (ListBean listBean : allList) {
                    if (mTabSelectType.equals(listBean.getExcode())) {
                        List<QuotationDataListBean> dataList = listBean.getQuotationDataList();
                        mAdapter.notifyDataChanged(true,dataList);
                        if (mAdapter.getData() !=null && mAdapter.getData().size() > 0){
                            getRealTimeData();
                        }
                        break;
                    }
                }
            }
        }
    }


    //是否休市
    @Override
    public void getRealTimeData(RealTimeBean realTimeBean) {
         if (realTimeBean == null
                 || realTimeBean.getList() == null
                 || realTimeBean.getList().size() == 0){
             return;
         }
        mRealTimeList = realTimeBean.getList();
        boolean compareData = compareData(mAdapter.getData(), mRealTimeList);
        if (compareData){
            mIsPushRefresh = false;
            mAdapter.notifyDataSetChanged();
            mIsPushRefresh = true;
        }
    }

    //排序
    private boolean compareData(List<QuotationDataListBean> dataList,List<RealTimeBean.Data> realTimeList){
         if (realTimeList == null
                 || realTimeList.size()== 0
                 || dataList == null
                 || dataList.size() == 0){
             return false;
         }
         //循环添加休市标记
         for (QuotationDataListBean listBean : dataList){

             for (RealTimeBean.Data realTimeBean : realTimeList){
                  if (realTimeBean.getExchangeID().equals(listBean.getExchangeID()) && listBean.getInstrumentID().equals(realTimeBean.getInstrumentID())){
                      listBean.setIsClosed(realTimeBean.getIsClosed());
                  }
             }
         }

        Collections.sort(dataList, new Comparator<QuotationDataListBean>() {
            @Override
            public int compare(QuotationDataListBean o1, QuotationDataListBean o2) {
                //1 开市 2 休市  o1 - o2 升序（从小到大） o2 - 01 降序（从大到小）
                return o1.getIsClosed() - o2.getIsClosed();
            }
        });
        return true;
    }
}
