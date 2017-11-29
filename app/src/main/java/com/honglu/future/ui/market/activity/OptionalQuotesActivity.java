package com.honglu.future.ui.market.activity;

import android.app.Service;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.config.Constant;
import com.honglu.future.events.EventBusConstant;
import com.honglu.future.events.MarketRefreshEvent;
import com.honglu.future.ui.market.adapter.AddHavedOptionalAdapter;
import com.honglu.future.ui.market.adapter.AllHavedOptionalAdapter;
import com.honglu.future.ui.market.bean.MarketnalysisBean;
import com.honglu.future.ui.market.contract.OptionalQuotesContract;
import com.honglu.future.ui.market.fragment.MarketFragment;
import com.honglu.future.ui.market.presenter.OptionalQuotesPresenter;
import com.honglu.future.util.SpUtil;
import com.honglu.future.widget.recycler.BaseRecyclerAdapter;
import com.honglu.future.widget.recycler.SpaceItemDecoration;
import com.honglu.future.widget.tab.CustomTabEntity;
import com.honglu.future.widget.tab.HorizontalTabLayout;
import com.honglu.future.widget.tab.SimpleOnTabSelectListener;
import com.honglu.future.widget.tab.TabEntity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import butterknife.BindView;

/**
 * Created by hc on 2017/10/26.
 *
 * @author wsw
 *         自选行情界面
 */
public class OptionalQuotesActivity extends BaseActivity<OptionalQuotesPresenter> implements OptionalQuotesContract.View {

    @BindView(R.id.rv_save_recycler_view)
    RecyclerView zxRecyclerView;

    RecyclerView addRecyclerView;

    private ItemTouchHelper mItemTouchHelper;
    private AllHavedOptionalAdapter zxAdapter; //自选 adapter
    private HorizontalTabLayout mCommonTab;
    private AddHavedOptionalAdapter addAdapter;

    private boolean isSort = false;
    private String mTabSelectType;
    private List<MarketnalysisBean.ListBean> mAllMarketList = new ArrayList<>();//除自选外全部数据
    private List<MarketnalysisBean.ListBean.QuotationDataListBean> zxMarketList = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabList = new ArrayList<>();

    @Override
    public void showLoading(String content) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorMsg(String msg, String type) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.acticity_optionalquotes;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        mAllMarketList = (List<MarketnalysisBean.ListBean>) getIntent().getSerializableExtra("allmarketlist");
        zxMarketList = (List<MarketnalysisBean.ListBean.QuotationDataListBean>) getIntent().getSerializableExtra("zxmarketlist");

        mTitle.setTitle(true, R.mipmap.ic_back_black, R.color.white, getResources().getString(R.string.text_add_qptional));
        View footerView = LayoutInflater.from(OptionalQuotesActivity.this).inflate(R.layout.layout_optional_quotes_footerview, null);
        addRecyclerView = (RecyclerView) footerView.findViewById(R.id.rv_add_recycler_view);
        mCommonTab = (HorizontalTabLayout) footerView.findViewById(R.id.op_common_tab_layout);

        addRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        addRecyclerView.addItemDecoration(new SpaceItemDecoration(5));
        addAdapter = new AddHavedOptionalAdapter();
        addRecyclerView.setAdapter(addAdapter);

        //自选
        zxRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        zxRecyclerView.addItemDecoration(new SpaceItemDecoration(5));
        zxAdapter = new AllHavedOptionalAdapter();
        zxAdapter.addFooterView(footerView);
        zxRecyclerView.setAdapter(zxAdapter);


        //设置上面自选数据
        if (zxMarketList != null && zxMarketList.size() > 0) {
            zxAdapter.addData(zxMarketList);
        }

        //过滤自选数据
        filterMarketData(zxMarketList);

        //设置tab 数据
        if (mAllMarketList != null && mAllMarketList.size() > 0) {
            for (MarketnalysisBean.ListBean bean : mAllMarketList) {
                mTabList.add(new TabEntity(bean.getExchangeName(), bean.getExcode()));
            }
            mCommonTab.setTabData(mTabList);
            setMarketData(mTabList.get(0).getTabType());
        }

        setListener();
    }

    private void setListener() {

        zxAdapter.setonLongItemClickListener(new BaseRecyclerAdapter.OnLongItemClick() {
            @Override
            public void onLongItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                mItemTouchHelper.startDrag(holder);
                Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);//震动70毫秒
                vib.vibrate(70);
            }
        });

        //自选
        zxAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClick() {
            @Override
            public void onItemClick(View view, int position) {
                MarketnalysisBean.ListBean.QuotationDataListBean bean = zxAdapter.getData().get(position);
                //添加到 all list
                addAllListData(bean);
                //移除自选
                zxAdapter.getData().remove(position);
                //重新设置底部数据
                if (!TextUtils.isEmpty(bean.getExcode()) && bean.getExcode().equals(mTabSelectType)) {
                    setMarketData(mTabSelectType);
                }
                zxAdapter.notifyDataSetChanged();

                EventBus.getDefault().post(new MarketRefreshEvent(EventBusConstant.OPTIONALQUOTES_DEL_MARKET, bean.getExcode(), bean));
            }
        });

        //添加自选
        addAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClick() {
            @Override
            public void onItemClick(final View view, final int position) {
                MarketnalysisBean.ListBean.QuotationDataListBean bean = addAdapter.getData().get(position);
                //添加到自选
                zxAdapter.getData().add(bean);
                //移除 对应的数据
                removeAllListData(bean.getExcode(), bean.getInstrumentID());
                //移除 add 里面的数据
                addAdapter.removeItemData(position);

                zxAdapter.notifyDataSetChanged();

                EventBus.getDefault().post(new MarketRefreshEvent(EventBusConstant.OPTIONALQUOTES_ADD_MARKET, bean.getExcode(), bean));
            }
        });

        mCommonTab.setOnTabSelectListener(new SimpleOnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mTabSelectType = mTabList.get(position).getTabType();
                setMarketData(mTabSelectType);
            }
        });

        //mAllHavedOptionalRecycler.addItemDecoration(new DividerGridItemDecoration(this));
        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {

            /**
             * 是否处理滑动事件 以及拖拽和滑动的方向 如果是列表类型的RecyclerView的只存在UP和DOWN，如果是网格类RecyclerView则还应该多有LEFT和RIGHT
             * @param recyclerView
             * @param viewHolder
             * @return
             */
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                    final int swipeFlags = 0;
                    return makeMovementFlags(dragFlags, swipeFlags);
                } else {
                    final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    final int swipeFlags = 0;
//                    final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                    return makeMovementFlags(dragFlags, swipeFlags);
                }
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //得到当拖拽的viewHolder的Position
                int fromPosition = viewHolder.getAdapterPosition();
                //拿到当前拖拽到的item的viewHolder
                int toPosition = target.getAdapterPosition();
                //addFooterView - 1 最后 - 1
                if (toPosition <= zxAdapter.getItemCount() - 2) {
                    isSort = true;
                    if (fromPosition < toPosition) {
                        for (int i = fromPosition; i < toPosition; i++) {
                            Collections.swap(zxAdapter.getData(), i, i + 1);
                        }
                    } else {
                        for (int i = fromPosition; i > toPosition; i--) {
                            Collections.swap(zxAdapter.getData(), i, i - 1);
                        }
                    }
                    zxAdapter.notifyItemMoved(fromPosition, toPosition);
                }
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                int position = viewHolder.getAdapterPosition();
//                myAdapter.notifyItemRemoved(position);
//                datas.remove(position);
            }

            /**
             * 重写拖拽可用
             * @return
             */
            @Override
            public boolean isLongPressDragEnabled() {
                return false;
            }

            /**
             * 长按选中Item的时候开始调用
             *
             * @param viewHolder
             * @param actionState
             */
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
                }
                super.onSelectedChanged(viewHolder, actionState);
            }

            /**
             * 手指松开的时候还原
             * @param recyclerView
             * @param viewHolder
             */
            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setBackgroundColor(0);
                //saveMarketData(zxAdapter !=null ? zxAdapter.getData() : null);
            }
        });

        mItemTouchHelper.attachToRecyclerView(zxRecyclerView);
    }


    //根据 type 设置对应adapter 数据
    private void setMarketData(String type) {
        if (!TextUtils.isEmpty(type)) {
            mTabSelectType = type;
            addAdapter.getData().clear();

            if (mAllMarketList != null && mAllMarketList.size() > 0) {
                for (MarketnalysisBean.ListBean alysisBean : mAllMarketList) {
                    if (type.equals(alysisBean.getExcode())) {
                        if (alysisBean.getQuotationDataList() != null && alysisBean.getQuotationDataList().size() > 0) {
                            addAdapter.getData().addAll(alysisBean.getQuotationDataList());

                        }
                        break;
                    }
                }
            }
            addAdapter.notifyDataSetChanged();
        }
    }


    /**
     * 过滤数据
     *
     * @param zxMarketList
     */
    private void filterMarketData(List<MarketnalysisBean.ListBean.QuotationDataListBean> zxMarketList) {

        //把对应excode添加到每条数据
        if (mAllMarketList != null && mAllMarketList.size() > 0) {
            for (MarketnalysisBean.ListBean allBena : mAllMarketList) {
                String excode = allBena.getExcode();
                if (allBena.getQuotationDataList() != null && allBena.getQuotationDataList().size() > 0) {
                    for (MarketnalysisBean.ListBean.QuotationDataListBean bean : allBena.getQuotationDataList()) {
                        bean.setExcode(excode);
                    }
                }
            }
        }


        if (zxMarketList != null && zxMarketList.size() > 0 || mAllMarketList == null && mAllMarketList.size() > 0) {

            for (MarketnalysisBean.ListBean.QuotationDataListBean zxBean : zxMarketList) {
                String excode = zxBean.getExcode();
                String instrumentID = zxBean.getInstrumentID();

                if (TextUtils.isEmpty(excode) || TextUtils.isEmpty(instrumentID)) {
                    continue;
                }

                for (MarketnalysisBean.ListBean allBean : mAllMarketList) {
                    if (excode.equals(allBean.getExcode()) && allBean.getQuotationDataList() != null && allBean.getQuotationDataList().size() > 0) {

                        ListIterator<MarketnalysisBean.ListBean.QuotationDataListBean> iterator = allBean.getQuotationDataList().listIterator();
                        while (iterator.hasNext()) {
                            MarketnalysisBean.ListBean.QuotationDataListBean bean = iterator.next();
                            if (excode.equals(bean.getExcode()) && instrumentID.equals(bean.getInstrumentID())) {
                                iterator.remove();
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * 移除 mAllMarketList 里面对应的数据
     */
    private void removeAllListData(String excode, String instrumentId) {
        if (!TextUtils.isEmpty(excode)
                && !TextUtils.isEmpty(instrumentId)
                && mAllMarketList != null
                && mAllMarketList.size() > 0) {

            for (MarketnalysisBean.ListBean allBean : mAllMarketList) {
                if (excode.equals(allBean.getExcode())) {

                    if (allBean.getQuotationDataList() != null && allBean.getQuotationDataList().size() > 0) {

                        ListIterator<MarketnalysisBean.ListBean.QuotationDataListBean> iterator = allBean.getQuotationDataList().listIterator();
                        while (iterator.hasNext()) {
                            MarketnalysisBean.ListBean.QuotationDataListBean bean = iterator.next();
                            if (instrumentId.equals(bean.getInstrumentID())) {
                                iterator.remove();
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }
    }


    private void addAllListData(MarketnalysisBean.ListBean.QuotationDataListBean addBean) {
        if (!TextUtils.isEmpty(addBean.getExcode())
                && !TextUtils.isEmpty(addBean.getInstrumentID())
                && mAllMarketList != null
                && mAllMarketList.size() > 0) {
            for (MarketnalysisBean.ListBean mBean : mAllMarketList) {
                if (addBean.getExcode().equals(mBean.getExcode())) {
                    mBean.getQuotationDataList().add(addBean);
                    break;
                }
            }
        }
    }

    @Override
    public void finish() {
        if (isSort) {
            saveMarketData(zxAdapter != null ? zxAdapter.getData() : null);
            EventBus.getDefault().post(new MarketRefreshEvent(EventBusConstant.OPTIONALQUOTES_SORT_MARKET, MarketFragment.ZXHQ_TYPE, null));
        }
        super.finish();
    }

    private void saveMarketData(List<MarketnalysisBean.ListBean.QuotationDataListBean> list) {
        if (list != null && list.size() > 0) {
            Gson gson = new Gson();
            String toJson = gson.toJson(list);
            SpUtil.putString(Constant.ZX_MARKET_KEY, toJson);
        } else {
            SpUtil.putString(Constant.ZX_MARKET_KEY, "");
        }
    }
}
