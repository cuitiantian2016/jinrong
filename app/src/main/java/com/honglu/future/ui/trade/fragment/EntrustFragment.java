package com.honglu.future.ui.trade.fragment;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.config.Constant;
import com.honglu.future.dialog.AccountLoginDialog;
import com.honglu.future.dialog.BillConfirmDialog;
import com.honglu.future.dialog.TradeTipDialog;
import com.honglu.future.events.ChangeTabEvent;
import com.honglu.future.events.RefreshUIEvent;
import com.honglu.future.events.UIBaseEvent;
import com.honglu.future.ui.login.activity.LoginActivity;
import com.honglu.future.ui.main.contract.AccountContract;
import com.honglu.future.ui.main.presenter.AccountPresenter;
import com.honglu.future.ui.trade.adapter.EntrustAdapter;
import com.honglu.future.ui.trade.bean.AccountBean;
import com.honglu.future.ui.trade.bean.ConfirmBean;
import com.honglu.future.ui.trade.bean.EntrustBean;
import com.honglu.future.ui.trade.bean.SettlementInfoBean;
import com.honglu.future.ui.trade.contract.EntrustContract;
import com.honglu.future.ui.trade.presenter.EntrustPresenter;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.Tool;
import com.honglu.future.widget.loading.LoadingLayout;
import com.honglu.future.widget.recycler.DividerItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.honglu.future.util.ToastUtil.showToast;

/**
 * Created by zq on 2017/10/26.
 */

public class EntrustFragment extends BaseFragment<EntrustPresenter> implements EntrustContract.View, AccountContract.View, EntrustAdapter.OnCancelClickListener,BillConfirmDialog.OnConfirmClickListener {
    @BindView(R.id.rv_entrust_list_view)
    RecyclerView mEntrustListView;
    @BindView(R.id.ll_filter)
    LinearLayout mLlFilter;
    @BindView(R.id.tv_all)
    TextView mAll;
    @BindView(R.id.tv_build)
    TextView mBuild;
    @BindView(R.id.tv_closed)
    TextView mClosed;
    @BindView(R.id.iv_tip)
    ImageView mIvTip;
    @BindView(R.id.loading_layout)
    LoadingLayout mLoadingLayout;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mSmartRefreshLayout;

    private BillConfirmDialog billConfirmDialog;
    private AccountLoginDialog mAccountLoginDialog;
    private AccountPresenter mAccountPresenter;
    private EntrustAdapter mEntrustAdapter;
    private boolean mIsShowFilter;
    private List<EntrustBean> mAllList;
    private List<EntrustBean> mOpenList;
    private List<EntrustBean> mCloseList;
    private Handler mHandler = new Handler();
    private Runnable mRunable = new Runnable() {
        @Override
        public void run() {
            getPositionList();
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.fragment_entrust;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
        mAccountPresenter = new AccountPresenter();
        mAccountPresenter.init(this);
    }

    @Override
    public void showLoading(String content) {
        if (!TextUtils.isEmpty(content)) {
            App.loadingContent(mActivity, content);
        }
    }

    @Override
    public void stopLoading() {
        App.hideLoading();
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        showToast(msg);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (App.getConfig().getLoginStatus()) {
                if (!App.getConfig().getAccountLoginStatus()) {
                    if (isVisible()) {
                        mAccountLoginDialog = new AccountLoginDialog(mActivity, mAccountPresenter);
                        mAccountLoginDialog.show();
                    }
                } else {
                    if (isVisible()) {
                        getPositionList();
                    }
                }
            } else {
                EventBus.getDefault().post(new ChangeTabEvent(0));
                startActivity(LoginActivity.class);
            }
        } else {
            mHandler.removeCallbacks(mRunable);
        }
    }

    @Override
    public void loadData() {
        EventBus.getDefault().register(this);
        initView();
        setButtonListeners();
    }

    private void initView() {
        mEntrustListView.setLayoutManager(new LinearLayoutManager(mContext));
        mEntrustListView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mEntrustAdapter = new EntrustAdapter();
        mEntrustListView.setAdapter(mEntrustAdapter);
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
               getPositionList();
            }
        });
    }

    private void getPositionList() {
        mPresenter.getEntrustList(SpUtil.getString(Constant.CACHE_TAG_UID), SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN));
    }

    @OnClick({R.id.tv_see_all, R.id.iv_tip})
    public void onClick(View view) {
        if (Tool.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.tv_see_all:
                if (!mIsShowFilter) {
                    mLlFilter.setVisibility(View.VISIBLE);
                    mIsShowFilter = true;
                } else {
                    mLlFilter.setVisibility(View.GONE);
                    mIsShowFilter = false;
                }
                break;
            case R.id.iv_tip:
                TradeTipDialog tipDialog = new TradeTipDialog(mContext, R.layout.layout_trade_entrust_tip);
                tipDialog.show();
                break;
        }
    }


    private void setButtonListeners() {
        mEntrustAdapter.setOnCancelClickListener(this);
        mAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAll.setBackgroundResource(R.drawable.stroke_radio_button_bg);
                mAll.setTextColor(mActivity.getResources().getColor(R.color.color_333333));
                mBuild.setBackgroundResource(R.drawable.fiter_radio_button_unselect_bg);
                mBuild.setTextColor(mActivity.getResources().getColor(R.color.color_A4A5A6));
                mClosed.setBackgroundResource(R.drawable.fiter_radio_button_unselect_bg);
                mClosed.setTextColor(mActivity.getResources().getColor(R.color.color_A4A5A6));
                mLlFilter.setVisibility(View.GONE);
                mIsShowFilter = false;
                if (mAllList == null || mAllList.size() == 0) {
                    mLoadingLayout.setStatus(LoadingLayout.Empty);
                    return;
                }
                mLoadingLayout.setStatus(LoadingLayout.Success);
                mEntrustAdapter.clearData();
                mEntrustAdapter.addData(mAllList);
                if (mAllList.size() == 0) {
                    mLoadingLayout.setStatus(LoadingLayout.Empty);
                }
            }
        });

        mBuild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAll.setBackgroundResource(R.drawable.fiter_radio_button_unselect_bg);
                mAll.setTextColor(mActivity.getResources().getColor(R.color.color_A4A5A6));
                mBuild.setBackgroundResource(R.drawable.stroke_radio_button_bg);
                mBuild.setTextColor(mActivity.getResources().getColor(R.color.color_333333));
                mClosed.setBackgroundResource(R.drawable.fiter_radio_button_unselect_bg);
                mClosed.setTextColor(mActivity.getResources().getColor(R.color.color_A4A5A6));
                mLlFilter.setVisibility(View.GONE);
                mIsShowFilter = false;
                if (mAllList == null || mAllList.size() == 0) {
                    mLoadingLayout.setStatus(LoadingLayout.Empty);
                    return;
                }
                mOpenList = new ArrayList<>();
                for (EntrustBean bean : mAllList) {
                    if (bean.getOpenClose() == 1) {
                        mOpenList.add(bean);
                    }
                }
                mLoadingLayout.setStatus(LoadingLayout.Success);
                mEntrustAdapter.clearData();
                mEntrustAdapter.addData(mOpenList);
                if (mOpenList.size() == 0) {
                    mLoadingLayout.setStatus(LoadingLayout.Empty);
                }

            }
        });

        mClosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAll.setBackgroundResource(R.drawable.fiter_radio_button_unselect_bg);
                mAll.setTextColor(mActivity.getResources().getColor(R.color.color_A4A5A6));
                mBuild.setBackgroundResource(R.drawable.fiter_radio_button_unselect_bg);
                mBuild.setTextColor(mActivity.getResources().getColor(R.color.color_A4A5A6));
                mClosed.setBackgroundResource(R.drawable.stroke_radio_button_bg);
                mClosed.setTextColor(mActivity.getResources().getColor(R.color.color_333333));
                mLlFilter.setVisibility(View.GONE);
                mIsShowFilter = false;
                if (mAllList == null || mAllList.size() == 0) {
                    mLoadingLayout.setStatus(LoadingLayout.Empty);
                    return;
                }
                mCloseList = new ArrayList<>();
                for (EntrustBean bean : mAllList) {
                    if (bean.getOpenClose() == 2) {
                        mCloseList.add(bean);
                    }
                }

                mLoadingLayout.setStatus(LoadingLayout.Success);
                mEntrustAdapter.clearData();
                mEntrustAdapter.addData(mCloseList);
                if (mCloseList.size() == 0) {
                    mLoadingLayout.setStatus(LoadingLayout.Empty);
                }
            }
        });
    }

    @Override
    public void loginSuccess(AccountBean bean) {
        showToast("登录成功");
        if(billConfirmDialog!=null&&billConfirmDialog.isShowing()){
            billConfirmDialog.dismiss();
        }
        mAccountLoginDialog.dismiss();
        mHandler.postDelayed(mRunable, 1000);
    }

    @Override
    public void showSettlementDialog(SettlementInfoBean bean) {
        billConfirmDialog = new BillConfirmDialog(mContext,bean);
        billConfirmDialog.setOnConfirmClickListenerr(this);
        billConfirmDialog.show();
    }


    @Override
    public void getEntrustListSuccess(List<EntrustBean> list) {
        mSmartRefreshLayout.finishRefresh();
        if (list == null || list.size() == 0) {
            if (mAllList != null && mAllList.size() > 0) {
                mAllList.clear();
            }
            mLoadingLayout.setStatus(LoadingLayout.Empty);
            return;
        }
        mAllList = list;
        mLoadingLayout.setStatus(LoadingLayout.Success);
        mEntrustAdapter.clearData();
        mEntrustAdapter.addData(list);
    }

    @Override
    public void onCancelClick(EntrustBean bean) {
        mPresenter.cancelOrder(bean.getOrderRef(),
                bean.getInstrumentId(),
                String.valueOf(bean.getSessionId()),
                String.valueOf(bean.getFrontId()),
                SpUtil.getString(Constant.CACHE_TAG_UID),
                SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN));
    }

    @Override
    public void cancelOrderSuccess() {
        mHandler.postDelayed(mRunable, 1000);
    }

    /***********
     * eventBus 监听
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UIBaseEvent event) {
        if (event instanceof RefreshUIEvent) {
            int code = ((RefreshUIEvent) event).getType();
            if (code == UIBaseEvent.EVENT_ACCOUNT_LOGOUT) {//安全退出期货账户
                if (!App.getConfig().getAccountLoginStatus()) {
                    mLoadingLayout.setStatus(LoadingLayout.Empty);
                    mEntrustAdapter.clearData();
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onConfirmClick() {
        mAccountPresenter.settlementConfirm(SpUtil.getString(Constant.CACHE_TAG_UID));
    }
}
