package com.honglu.future.ui.trade.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.honglu.future.R;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.ui.trade.adapter.ClosePositionAdapter;
import com.honglu.future.ui.trade.bean.ClosePositionListBean;
import com.honglu.future.ui.trade.contract.ClosePositionContract;
import com.honglu.future.ui.trade.presenter.ClosePositionPresenter;
import com.honglu.future.util.Tool;
import com.honglu.future.widget.popupwind.BottomPopupWindow;
import com.honglu.future.widget.recycler.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zq on 2017/10/26.
 */

public class ClosePositionFragment extends BaseFragment<ClosePositionPresenter> implements ClosePositionContract.View {
    @BindView(R.id.rv_position)
    RecyclerView mPositionListView;
    private ClosePositionAdapter mClosePositionAdapter;
    private List<ClosePositionListBean> mList;
    private BottomPopupWindow mTipPopupWindow;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_close_position;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        initView();
        initData();
    }

    private void initView() {
        mPositionListView.setLayoutManager(new LinearLayoutManager(mContext));
        mPositionListView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mClosePositionAdapter = new ClosePositionAdapter();
        mPositionListView.setAdapter(mClosePositionAdapter);
    }

    @OnClick({R.id.tv_tip})
    public void onClick(View view) {
        if (Tool.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.tv_tip:
                showTipWindow(view);
                break;
        }
    }

    private void showTipWindow(View view) {
        View layout = LayoutInflater.from(mActivity).inflate(R.layout.layout_trade_tip_pop_window, null);
        showTipBottomWindow(view, layout);
        backgroundAlpha(0.5f);
    }

    private void initData() {
        mList = new ArrayList<>();
        for (int i = 0; i <= 10; i++) {
            ClosePositionListBean bean = new ClosePositionListBean();
            if (i % 2 == 0) {
                bean.setProductName("玉米1801");
                bean.setBuyHands("买涨1手");
                bean.setAveragePrice("2754");
                bean.setNewPrice("2765");
                bean.setProfit("+110");
                bean.setBuyRiseDown("rise");
                mList.add(bean);
            } else {
                bean.setProductName("甲醇1801");
                bean.setBuyHands("买跌2手");
                bean.setAveragePrice("2754");
                bean.setNewPrice("2765");
                bean.setProfit("-220");
                bean.setBuyRiseDown("down");
                mList.add(bean);
            }
        }
        mClosePositionAdapter.addData(mList);
    }

    private void showTipBottomWindow(View view, View layout) {
        if (mTipPopupWindow != null && mTipPopupWindow.isShowing()) {
            return;
        }
        mTipPopupWindow = new BottomPopupWindow(mActivity,
                view, layout);
        //添加按键事件监听
        setButtonListeners(layout);
        mTipPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    private void setButtonListeners(View view) {
        ImageView ivClose = (ImageView) view.findViewById(R.id.iv_close_popup);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTipPopupWindow != null && mTipPopupWindow.isShowing()) {
                    mTipPopupWindow.dismiss();
                }
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mActivity.getWindow().setAttributes(lp);
    }
}
