package com.honglu.future.ui.market.activity;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.config.Constant;
import com.honglu.future.ui.market.adapter.AllHavedOptionalAdapter;
import com.honglu.future.ui.market.adapter.MarketClassificationAdapter;
import com.honglu.future.ui.market.bean.AllClassificationBean;
import com.honglu.future.ui.market.bean.QuotesItemBean;
import com.honglu.future.ui.market.contract.OptionalQuotesContract;
import com.honglu.future.ui.market.presenter.OptionalQuotesPresenter;
import com.honglu.future.widget.recycler.BaseRecyclerAdapter;
import com.honglu.future.widget.recycler.SpaceItemDecoration;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hc on 2017/10/26.
 * @author wsw
 * 自选行情界面
 */

public class OptionalQuotesActivity extends BaseActivity<OptionalQuotesPresenter> implements OptionalQuotesContract.View{
    //
    @BindView(R.id.recylerview_all_hava_optional)
    RecyclerView mAllHavedOptionalRecycler;
    private AllHavedOptionalAdapter mAllHavedOptionalAdapter;
    private List<QuotesItemBean> mHavedAllOptionalList = new ArrayList<QuotesItemBean>();//所有自选中的产品
    private ItemTouchHelper mItemTouchHelper;

    //
    private List<AllClassificationBean> allQuotesNames = new ArrayList<AllClassificationBean>();//可以增加的期货交易所名称
    @BindView(R.id.qptional_classification_recycler)
    RecyclerView mQptionalClassificationRecycler;
    private MarketClassificationAdapter mMarketClassificationAdapter;
    //可以增加的期货类型产品
    @BindView(R.id.recylerview_can_add)
    RecyclerView mCanAddOptionalRecycler;
    private AllHavedOptionalAdapter mCanAddOptionalAdapter;
    private List<QuotesItemBean> mCanAddOptionalList = new ArrayList<QuotesItemBean>();//此数据需要几份，可以直接在封装成一个类来用
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

    }
    @Override
    public void loadData() {
        for(QuotesItemBean itemBean : DataSupport.findAll(QuotesItemBean.class)){
            mHavedAllOptionalList.add(itemBean);
        }
        DataSupport.deleteAll(QuotesItemBean.class);
        for (int i = 0; i < 5; i++) {
            AllClassificationBean allClassificationBean = new AllClassificationBean();
            if (i == 0){
                allClassificationBean.setClassificationName("自选");
                allClassificationBean.setOnClick(true);
            }else if (i == 1){
                allClassificationBean.setClassificationName("主力合约");
                allClassificationBean.setOnClick(false);
            }else if (i == 2){
                allClassificationBean.setClassificationName("上期所");
                allClassificationBean.setOnClick(false);
            }else if (i == 3){
                allClassificationBean.setClassificationName("大商所");
                allClassificationBean.setOnClick(false);
            }else if (i == 4){
                allClassificationBean.setClassificationName("郑商所");
                allClassificationBean.setOnClick(false);
            }else {
            }
            allQuotesNames.add(allClassificationBean);
        }
        initView();
    }
    private void initView(){
        mTitle.setTitle(true,R.mipmap.ic_back_black, R.color.white, getResources().getString(R.string.text_add_qptional));
        //还能被添加的分类

        LinearLayoutManager ms= new LinearLayoutManager(mContext);
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);// 设置 recyclerview 布局方式为横向布局
        mQptionalClassificationRecycler.setLayoutManager(ms);
        mQptionalClassificationRecycler.setFocusable(false);
        mMarketClassificationAdapter = new MarketClassificationAdapter();
        mQptionalClassificationRecycler.setAdapter(mMarketClassificationAdapter);
        mMarketClassificationAdapter.clearData();
        mMarketClassificationAdapter.addData(allQuotesNames);
        mMarketClassificationAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClick() {
            @Override
            public void onItemClick(View view, int position) {
                for (int i = 0; i < allQuotesNames.size(); i++) {
                    allQuotesNames.get(i).setOnClick((i == position)?true:false);
                }
                mMarketClassificationAdapter.notifyDataSetChanged();
            }
        });
        //已经在自选中的数据处理
        mAllHavedOptionalRecycler.setLayoutManager(new GridLayoutManager(this,4));
        mAllHavedOptionalRecycler.addItemDecoration(new SpaceItemDecoration(5));
        mAllHavedOptionalAdapter = new AllHavedOptionalAdapter();
        mAllHavedOptionalAdapter.addData(mHavedAllOptionalList);
        mAllHavedOptionalRecycler.setAdapter(mAllHavedOptionalAdapter);

        mAllHavedOptionalAdapter.setonLongItemClickListener(new BaseRecyclerAdapter.OnLongItemClick() {
            @Override
            public void onLongItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                mItemTouchHelper.startDrag(holder);
                Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);//震动70毫秒
                vib.vibrate(70);
            }
        });
        mAllHavedOptionalAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClick() {
            @Override
            public void onItemClick(View view, int position) {

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
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(mHavedAllOptionalList, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(mHavedAllOptionalList, i, i - 1);
                    }
                }
                mAllHavedOptionalAdapter.notifyItemMoved(fromPosition, toPosition);
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
                    viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
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
            }
        });

        mItemTouchHelper.attachToRecyclerView(mAllHavedOptionalRecycler);
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (int i = 0; i < mHavedAllOptionalList.size() ; i++) {
            QuotesItemBean itemBean = new QuotesItemBean();
            itemBean.setContractName(mHavedAllOptionalList.get(i).getContractName());
            itemBean.setLatestPrice(mHavedAllOptionalList.get(i).getLatestPrice());
            itemBean.setQuoteChange(mHavedAllOptionalList.get(i).getQuoteChange());
            itemBean.setHavedPositions(mHavedAllOptionalList.get(i).getHavedPositions());
            itemBean.save();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.setResult(Constant.OptionalQuotesLITEPALl, new Intent());
    }
}
