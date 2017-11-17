package com.honglu.future.ui.home.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honglu.future.BuildConfig;
import com.honglu.future.R;
import com.honglu.future.base.BasePresenter;
import com.honglu.future.base.IBaseView;
import com.honglu.future.config.Constant;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.mpush.MPushUtil;
import com.honglu.future.ui.home.bean.HomeMarketCodeBean;
import com.honglu.future.ui.home.bean.MarketData;
import com.honglu.future.ui.trade.kchart.KLineMarketActivity;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.NumberUtils;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ToastUtil;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.ArrayList;

/**
 * Created by hefei on 2017/6/6.
 * <p>
 * 新的首页组件
 *
 * 取集合的list/3+list%3为页数。
 * 页数*3为步长。再刷新
 * 首頁market組件
 */

public class HomeMarketPriceViewModel extends IBaseView<MarketData> implements View.OnClickListener {
    private static final String TAG = "HomeMarketPrice";

    private ViewPager mViewPager;
    private ArrayList<MarketData.MarketDataBean> arrayList ;
    private static final int PAGE_SIZE = 3;//每页显示的个数
    private PagerAdapter pagerAdapter;
    private IndicatorViewModel indicatorViewModel;
    private boolean isRefresh;
    boolean isAdapterRefresh;
    private int deviceWidth;//设备宽度
    private Context mContext;
    private BasePresenter marketPresenter;
    public View mView;
    public String productList;

    public HomeMarketPriceViewModel(Context context) {
        mContext = context;
        mView = View.inflate(context, R.layout.home_market_view, null);
        initView(mView);
        refreshData();
    }
    /**
     * 刷新数据
     */
    public void refreshData() {
        if (marketPresenter ==null) {
            marketPresenter = new BasePresenter<IBaseView<MarketData>>(this) {
                @Override
                public void getData() {
                    super.getData();
                    //拿到codes
                    toSubscribe(HttpManager.getApi().getMarketCodesData(
                            2, BuildConfig.VERSION_NAME, SpUtil.getString(Constant.CACHE_TAG_UID)
                    ), new HttpSubscriber<HomeMarketCodeBean>() {
                        @Override
                        protected void _onNext(HomeMarketCodeBean o) {
                            //拿到list
                            productList = o.productList;
                            MPushUtil.CODES_TRADE_HOME = productList;
                            String replace = productList.replace("|", "%7C");
                            toSubscribe(HttpManager.getApi().getMarketCodesData(replace,2), new HttpSubscriber<MarketData>() {
                                @Override
                                protected void _onNext(MarketData o) {
                                    super._onNext(o);
                                   mView.bindData(o);
                                }
                                @Override
                                protected void _onError(String message) {
                                    super._onError(message);
                                    ToastUtil.show(message);
                                }
                            });
                            //绑定list
                        }
                    });
                }
            };
        }
        marketPresenter.getData();
    }
    private void initView(View view) {
        deviceWidth = DeviceUtils.getScreenWidth(mContext);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_market);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.ll_indicator_view);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dp2px(60), DensityUtil.dp2px(2));
        params.gravity = Gravity.CENTER;
        indicatorViewModel = new IndicatorViewModel(mContext, 3);
        linearLayout.addView(indicatorViewModel.mView,params);
        initViewPage();
    }
    /**
     * 初始化viewpage
     */
    private void initViewPage(){
        if (pagerAdapter == null){
            pagerAdapter = new PagerAdapter() {
                private int mChildCount = 0;
                @Override
                public void notifyDataSetChanged() {
                    mChildCount = getCount();
                    super.notifyDataSetChanged();
                }
                @Override
                public int getItemPosition(Object object)   {
                    if ( mChildCount > 0) {
                        mChildCount --;
                        return POSITION_NONE;
                    }
                    return super.getItemPosition(object);
                }
                @Override
                public int getCount() {
                    if (arrayList.size() % PAGE_SIZE>0){
                        return (arrayList == null || arrayList.size() == 0) ? 0 : arrayList.size() / PAGE_SIZE + 1;
                    }else {
                        return (arrayList == null || arrayList.size() == 0) ? 0 : arrayList.size() / PAGE_SIZE;
                    }
                }
                @Override
                public Object instantiateItem(ViewGroup container, int position) {
                    View view = View.inflate(container.getContext(), R.layout.new_home_item2, null);
                    LinearLayout llContainer = (LinearLayout) view.findViewById(R.id.line_ggy);
                    addViewToPager(llContainer,position,arrayList);
                    container.addView(llContainer);
                    return llContainer;
                }
                @Override
                public void destroyItem(ViewGroup container, int position, Object object) {
                    container.removeView((View) object);
                }
                @Override
                public boolean isViewFromObject(View view, Object object) {
                    return view == object;
                }
            };
        }
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (indicatorViewModel != null){
                    indicatorViewModel.showIndicator(position);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    /**
     * 添加view到pager中
     * @param llContainer 当前的布局
     * @param position 当前的位置
     * @param arrayListBean 当前的数据
     */
    private void addViewToPager(LinearLayout llContainer,int position,ArrayList<MarketData.MarketDataBean> arrayListBean){
        if (arrayListBean ==null||arrayListBean.size()==0){
            return;
        }
        int pageCount = arrayListBean.size() / PAGE_SIZE;//有的整页的页数
        int lastPageCount = arrayListBean.size() % PAGE_SIZE;//最后一页省的个数
        LinearLayout childAt = (LinearLayout) llContainer.getChildAt(position);
        int childCount = 0;
        if (childAt!=null){
            childCount = childAt.getChildCount();
        }
        if (pageCount-1>=position){//完整的一页
            if (childCount == PAGE_SIZE){//说明view已经添加
                for (int i = position*PAGE_SIZE; i < (position+1)*PAGE_SIZE; i++) {//需要加一
                    bindGoodsItem(childAt.getChildAt(i-position*PAGE_SIZE),arrayListBean.get(i));
                }
            }else {//添加View
                llContainer.removeAllViews();
                LinearLayout viewThr = (LinearLayout) View.inflate(mContext, R.layout.homegoods_item_thr, null);
                viewThr.removeAllViews();
                for (int i = position*PAGE_SIZE; i < (position+1)*PAGE_SIZE; i++) {//需要加一
                    View goodsItem = View.inflate(mContext, R.layout.new_homegoods_item, null);
                    goodsItem.setTag(arrayListBean.get(i));
                    goodsItem.setOnClickListener(this);//设置点击事件
                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                    params1.width = deviceWidth / 3;
                    viewThr.addView(goodsItem, params1);
                    try {
                        if (arrayListBean !=null  && arrayListBean.size() > i){
                            bindGoodsItem(goodsItem,arrayListBean.get(i));
                        }
                    }catch (Exception e){

                    }
                }
                llContainer.addView(viewThr);
            }
        }else {
            if (childCount == lastPageCount){//说明view已经添加
                for (int i = position*PAGE_SIZE; i < arrayList.size(); i++) {//需要加一
                    bindGoodsItem(childAt.getChildAt(i-position*PAGE_SIZE),arrayListBean.get(i));
                }
            }else {//添加View
                llContainer.removeAllViews();
                LinearLayout viewThr = (LinearLayout) View.inflate(mContext, R.layout.homegoods_item_thr, null);
                viewThr.removeAllViews();
                for (int i = position*PAGE_SIZE; i < arrayList.size(); i++) {//需要加一
                    View goodsItem = View.inflate(mContext, R.layout.new_homegoods_item, null);
                    goodsItem.setTag(arrayListBean.get(i));
                    goodsItem.setOnClickListener(this);//设置点击事件
                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                    params1.width = deviceWidth/3;
                    viewThr.addView(goodsItem, params1);
                    try {
                        bindGoodsItem(goodsItem,arrayListBean.get(i));
                    }catch (Exception e){}
                }
                llContainer.addView(viewThr);
            }
        }
    }
    /**
     * 刷新item控件的数据
     * @param goodsItem
     */
    private void bindGoodsItem(View goodsItem , MarketData.MarketDataBean data){
        TextView mTvItemName = (TextView) goodsItem.findViewById(R.id.tvitemname);
        TextView mTvItemPrice = (TextView) goodsItem.findViewById(R.id.tvitemprice);
        TextView mTvitemchg = (TextView) goodsItem.findViewById(R.id.tvitemchg);
        TextView mTvItemRise = (TextView) goodsItem.findViewById(R.id.tvitemrise);
        double change = NumberUtils.getDouble(data.change);
        //当当前价小于昨收价时，价格颜色应变更为绿色
        if (change>= 0){
            Drawable drawable = ContextCompat.getDrawable(goodsItem.getContext(),R.mipmap.icon_up_arr);
            drawable.setBounds(0, 0, 24,24);
            mTvItemName.setCompoundDrawables(null,null,drawable,null);
            mTvItemRise.setTextColor(mContext.getResources().getColor(R.color.color_ff5376));
            mTvItemPrice.setTextColor(mContext.getResources().getColor(R.color.color_ff5376));
            mTvitemchg.setTextColor(mContext.getResources().getColor(R.color.color_ff5376));
        }else if (change< 0){
            Drawable drawable = ContextCompat.getDrawable(goodsItem.getContext(),R.mipmap.icon_dwon_arr);
            drawable.setBounds(0, 0, 24,24);
            mTvItemName.setCompoundDrawables(null,null,drawable,null);
            mTvItemRise.setTextColor(mContext.getResources().getColor(R.color.color_00ce64));
            mTvItemPrice.setTextColor(mContext.getResources().getColor(R.color.color_00ce64));
            mTvitemchg.setTextColor(mContext.getResources().getColor(R.color.color_00ce64));
        }
        //设置产品名称
        mTvItemName.setText(data.name);
        //设置上涨下跌比例
        mTvItemRise.setText(data.change);
        mTvitemchg.setText(data.chg);
        mTvItemPrice.setText(data.lastPrice);
    }
    @Override
    public void bindData(MarketData marketData) {
        if (marketData==null||marketData.list==null||marketData.list.size()<=0)return;
        MPushUtil.requestMarket(productList);
        ArrayList<MarketData.MarketDataBean> dataList = marketData.list;
        if (arrayList!=null){
            arrayList.clear();
        }else {
            arrayList = new ArrayList<>();
        }
        for (int n = 0; n < dataList.size(); n++) {
            if (dataList.get(n) != null) {
                arrayList.add(dataList.get(n));
            }
        }
        PagerAdapter adapter = mViewPager.getAdapter();
        if (adapter == null){
            adapter =  pagerAdapter;
            mViewPager.setAdapter(pagerAdapter);
        }
        if (arrayList!=null&&arrayList.size()>0){
            if (!isRefresh){
                isRefresh = true;
                int sum ;
                if (arrayList.size() % PAGE_SIZE>0){
                    sum = (arrayList == null || arrayList.size() == 0) ? 0 : arrayList.size() / PAGE_SIZE + 1;
                }else {
                    sum = (arrayList == null || arrayList.size() == 0) ? 0 : arrayList.size() / PAGE_SIZE;
                }
                if (indicatorViewModel!=null){
                    indicatorViewModel.refreshNum(sum);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }
    /**
     * 点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        MarketData.MarketDataBean marketDataBean = (MarketData.MarketDataBean)view.getTag();
        Intent intent = new Intent(mContext, KLineMarketActivity.class);
        intent.putExtra("excode",marketDataBean.exchangeID);
        intent.putExtra("code",marketDataBean.instrumentID);
        intent.putExtra("isClosed","1");
        mContext.startActivity(intent);
    }

    public void requestMarket(){
        if (!TextUtils.isEmpty(productList))
        MPushUtil.requestMarket(productList);
    }

    /**
     * 刷新数据
     * @param dataBean
     */
    public void refreshPrice(MarketData.MarketDataBean dataBean){
        if (arrayList!=null&&arrayList.size()>0){
            int index = -1;
            MarketData.MarketDataBean marketDataBean = null;
            for (int i = 0;i<arrayList.size();i++) {
                marketDataBean = arrayList.get(i);
                if (marketDataBean.instrumentID.equals(dataBean.instrumentID)){
                    index = i;
                    break;
                }
            }
            if (index > 0){
                marketDataBean.change =   dataBean.change;
                marketDataBean.chg =  dataBean.chg;
                marketDataBean.lastPrice =  dataBean.lastPrice;
                arrayList.set(index,marketDataBean);
                pagerAdapter.notifyDataSetChanged();
            }

        }
    }
}
