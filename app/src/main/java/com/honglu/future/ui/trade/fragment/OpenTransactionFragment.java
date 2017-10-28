package com.honglu.future.ui.trade.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.dialog.AlertFragmentDialog;
import com.honglu.future.ui.login.activity.LoginActivity;
import com.honglu.future.ui.trade.adapter.OpenTransactionAdapter;
import com.honglu.future.ui.trade.bean.OpenTransactionListBean;
import com.honglu.future.ui.trade.contract.OpenTransactionContract;
import com.honglu.future.ui.trade.presenter.OpenTransactionPresenter;
import com.honglu.future.widget.popupwind.BottomPopupWindow;
import com.honglu.future.widget.recycler.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zq on 2017/10/26.
 */

public class OpenTransactionFragment extends BaseFragment<OpenTransactionPresenter> implements OpenTransactionContract.View, OpenTransactionAdapter.OnRiseDownClickListener {
    @BindView(R.id.rv_open_transaction_list_view)
    RecyclerView mOpenTransactionListView;
    private LinearLayout mTradeHeader;
    private ImageView mTradeTip;
    private OpenTransactionAdapter mOpenTransactionAdapter;
    private List<OpenTransactionListBean> mList;
    private BottomPopupWindow mPopupWindow;
    private BottomPopupWindow mTipPopupWindow;
    private static final String TRADE_BUY_RISE = "TRADE_BUY_RISE";
    private static final String TRADE_BUY_DOWN = "TRADE_BUY_DOWN";

    public static void tipClickCallBack(OnTipClickCallback onTipClickCallback) {
        onTipClickCallback.tipClick();
    }

    interface OnTipClickCallback {
        void tipClick();
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_open_transaction;
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
        mOpenTransactionListView.setLayoutManager(new LinearLayoutManager(mContext));
        mOpenTransactionListView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mOpenTransactionAdapter = new OpenTransactionAdapter();
        View headView = LayoutInflater.from(mActivity).inflate(R.layout.item_trade_list_header, null);
        mTradeHeader = (LinearLayout) headView.findViewById(R.id.ll_trade_header);
        mTradeTip = (ImageView) headView.findViewById(R.id.iv_trade_tip);
        mOpenTransactionAdapter.addHeaderView(headView);
        mOpenTransactionListView.setAdapter(mOpenTransactionAdapter);
        setListener();
    }

    private void setListener() {
        mOpenTransactionAdapter.setOnRiseDownClickListener(this);
        mTradeHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!App.getConfig().getLoginStatus()) {
                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        mTradeTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTipWindow(v);
            }
        });
    }

    private void initData() {
        mList = new ArrayList<>();
        for (int i = 0; i <= 10; i++) {
            OpenTransactionListBean bean = new OpenTransactionListBean();
            if (i % 2 == 0) {
                bean.setProductName("玉米1801");
                bean.setNum("338730");
                bean.setRiseNum("买涨1678");
                bean.setDownNum("买跌1677");
                bean.setRiseRadio("50%");
                bean.setDownRadio("50%");
                bean.setIsRest("0");
                mList.add(bean);
            } else {
                bean.setProductName("甲醇1801");
                bean.setNum("4009004");
                bean.setRiseNum("买涨2646");
                bean.setDownNum("买跌2645");
                bean.setRiseRadio("0%");
                bean.setDownRadio("100%");
                bean.setIsRest("1");
                mList.add(bean);
            }
        }
        mOpenTransactionAdapter.addData(mList);
    }

    private void showBottomWindow(View view, View layout, int flag) {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            return;
        }
        mPopupWindow = new BottomPopupWindow(mActivity, view, layout);
        //添加按键事件监听
        setButtonListeners(layout, flag);
        //添加pop窗口关闭事件，主要是实现关闭时改变背景的透明度
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    private void showTipBottomWindow(View view, View layout, final int flag) {
        if (mTipPopupWindow != null && mTipPopupWindow.isShowing()) {
            return;
        }
        mTipPopupWindow = new BottomPopupWindow(mActivity,
                view, layout);
        //添加按键事件监听
        setButtonListeners(layout, flag);
        //添加pop窗口关闭事件，主要是实现关闭时改变背景的透明度
        mTipPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (mPopupWindow == null || !mPopupWindow.isShowing()) {
                    backgroundAlpha(1f);
                }
            }
        });
    }

    private void setButtonListeners(View view, final int flag) {
        ImageView ivClose = (ImageView) view.findViewById(R.id.iv_close_popup);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 1) {
                    if (mTipPopupWindow != null && mTipPopupWindow.isShowing()) {
                        mTipPopupWindow.dismiss();
                    }
                } else {
                    if (mPopupWindow != null && mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                }
            }
        });

        if (flag == 2 || flag == 3) {
            ImageView ivTip = (ImageView) view.findViewById(R.id.iv_open_account_tip);
            ivTip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tipClickCallBack(new OnTipClickCallback() {
                        @Override
                        public void tipClick() {
                            showTipWindow(mTradeTip);
                        }
                    });
                }
            });

            if (flag == 3) {
                TextView fastOpen = (TextView) view.findViewById(R.id.btn_fast_open);
                fastOpen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertFragmentDialog.Builder(mActivity).setTitle("确认建仓").setContent("玉米1080 买涨 1手 总计 ¥2511.68")
                                .setRightBtnText("确定")
                                .setLeftBtnText("取消")
                                .setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                                    @Override
                                    public void dialogRightBtnClick() {
                                    }
                                }).build();
                    }
                });
            }
        }
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

    @Override
    public void onRiseClick(View view) {
        if (!App.getConfig().getLoginStatus()) {
            Intent intent = new Intent(mContext, LoginActivity.class);
            mContext.startActivity(intent);
        } else {
            // TODO: 2017/10/27 判断期货交易所登录或者建仓
            showOpenAccountWindow(view);
        }
    }

    @Override
    public void onDownClick(View view) {
        if (!App.getConfig().getLoginStatus()) {
            new AlertFragmentDialog.Builder(mActivity).setContent("请登陆后再操作")
                    .setRightBtnText("确定")
                    .setLeftBtnText("取消")
                    .setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                        @Override
                        public void dialogRightBtnClick() {
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            mContext.startActivity(intent);
                        }
                    }).build();
        } else {
            // TODO: 2017/10/27 判断期货交易所登录或者建仓
            //showOpenAccountWindow(view);
            showOpenTransactionWindow(view, TRADE_BUY_DOWN);
        }
    }

    private void showTipWindow(View view) {
        View layout = LayoutInflater.from(mActivity).inflate(R.layout.layout_trade_tip_pop_window, null);
        showTipBottomWindow(view, layout, 1);
        backgroundAlpha(0.5f);
    }

    private void showOpenAccountWindow(View view) {
        View layout = LayoutInflater.from(mActivity).inflate(R.layout.future_login_popup_window, null);
        showBottomWindow(view, layout, 2);
        backgroundAlpha(0.5f);
    }

    private void showOpenTransactionWindow(View view, String riseOrDown) {
        View layout = LayoutInflater.from(mActivity).inflate(R.layout.open_transaction_popup_window, null);
        TextView tvRiseOrDown = null;
        if (riseOrDown.equals(TRADE_BUY_DOWN)) {
            tvRiseOrDown = (TextView) layout.findViewById(R.id.tv_rise);
        } else if (riseOrDown.equals(TRADE_BUY_RISE)) {
            tvRiseOrDown = (TextView) layout.findViewById(R.id.tv_down);
        }
        tvRiseOrDown.setBackgroundResource(R.drawable.rise_down_bg_block);
        tvRiseOrDown.setTextColor(mActivity.getResources().getColor(R.color.color_151515));
        showBottomWindow(view, layout, 3);
        backgroundAlpha(0.5f);
    }
}
