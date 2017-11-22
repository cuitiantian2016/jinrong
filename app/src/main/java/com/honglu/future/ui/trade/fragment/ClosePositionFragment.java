package com.honglu.future.ui.trade.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
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
import com.honglu.future.ui.trade.activity.TradeRecordActivity;
import com.honglu.future.ui.trade.adapter.ClosePositionAdapter;
import com.honglu.future.ui.trade.bean.AccountBean;
import com.honglu.future.ui.trade.bean.HistoryClosePositionBean;
import com.honglu.future.ui.trade.bean.SettlementInfoBean;
import com.honglu.future.ui.trade.contract.ClosePositionContract;
import com.honglu.future.ui.trade.presenter.ClosePositionPresenter;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.TimeUtil;
import com.honglu.future.util.Tool;
import com.honglu.future.widget.loading.LoadingLayout;
import com.honglu.future.widget.recycler.DividerItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.honglu.future.util.ToastUtil.showToast;

/**
 * Created by zq on 2017/10/26.
 */

public class ClosePositionFragment extends BaseFragment<ClosePositionPresenter> implements ClosePositionContract.View, AccountContract.View,BillConfirmDialog.OnConfirmClickListener {
    @BindView(R.id.rv_position)
    RecyclerView mPositionListView;
    @BindView(R.id.loading_layout)
    LoadingLayout mLoadingLayout;
    @BindView(R.id.srl_refreshView)
    SmartRefreshLayout mSmartRefreshLayout;
    private ClosePositionAdapter mClosePositionAdapter;
    private AccountLoginDialog mAccountLoginDialog;
    private AccountPresenter mAccountPresenter;
    int page = 1;
    int pageSize = 10;
    boolean isMore;
    private View foot;
    private String mEndDate;
    private BillConfirmDialog billConfirmDialog;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_close_position;
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
    public void loadData() {
        EventBus.getDefault().register(this);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        mEndDate = sdf.format(c.getTime());
        initView();
    }

    private void initView() {
        mPositionListView.setLayoutManager(new LinearLayoutManager(mContext));
        mPositionListView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        View headView = View.inflate(getActivity(),R.layout.layout_close_position_head,null);
        TextView mTip = (TextView) headView.findViewById(R.id.tv_tip);
        View mColorLine = headView.findViewById(R.id.v_colorLine);
        mColorLine.setVisibility(View.INVISIBLE);
        mClosePositionAdapter = new ClosePositionAdapter();
        mClosePositionAdapter.addHeaderView(headView);
        mPositionListView.setAdapter(mClosePositionAdapter);
        foot = View.inflate(getContext(), R.layout.item_see_all, null);
        foot.findViewById(R.id.ll_see_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(TradeRecordActivity.class);
            }
        });
        mClosePositionAdapter.addFooterView(foot, DeviceUtils.dip2px(getContext(), 50));
        mSmartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                loadMore();
            }
        });
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                mSmartRefreshLayout.setEnableLoadmore(true);
                getClosePositionList();
            }
        });

        mTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tool.isFastDoubleClick()) return;
                TradeTipDialog tipDialog = new TradeTipDialog(mContext, R.layout.layout_trade_heyue_closed_tip);
                tipDialog.show();
            }
        });
    }

    /**
     * 加载更多
     */
    private void loadMore() {
        if (!isMore) {
            mSmartRefreshLayout.finishLoadmore();
            mSmartRefreshLayout.setEnableLoadmore(false);
            return;
        }
        getClosePositionList();
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
                        getClosePositionList();
                    }
                }
            } else {
                EventBus.getDefault().post(new ChangeTabEvent(0));
                startActivity(LoginActivity.class);
            }
        }
    }

    private void getClosePositionList() {
        mPresenter.getCloseList("", mEndDate, SpUtil.getString(Constant.CACHE_TAG_UID), SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN), page, pageSize);
    }

    @Override
    public void loginSuccess(AccountBean bean) {
        showToast("登录成功");
        if(billConfirmDialog!=null&& billConfirmDialog.isShowing()){
            billConfirmDialog.dismiss();
        }
        mAccountLoginDialog.dismiss();
        getClosePositionList();
    }

    @Override
    public void showSettlementDialog(SettlementInfoBean bean) {
        billConfirmDialog = new BillConfirmDialog(mContext,bean);
        billConfirmDialog.setOnConfirmClickListenerr(this);
        billConfirmDialog.show();
    }

    @Override
    public void getCloseListSuccess(List<HistoryClosePositionBean> list) {
        if (list == null || list.size() == 0) {
            mLoadingLayout.setStatus(LoadingLayout.Empty);
            return;
        }
        mLoadingLayout.setStatus(LoadingLayout.Success);
        if (page == 1) {
            mClosePositionAdapter.clearData();
            mSmartRefreshLayout.finishRefresh();
        } else {
            mSmartRefreshLayout.finishLoadmore();
        }
        if (list.size() >= pageSize) {
            isMore = true;
            ++page;
        } else {
            isMore = false;
        }
        mClosePositionAdapter.addData(list);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
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
                    page = 1;
                    mSmartRefreshLayout.setEnableLoadmore(true);
                    mClosePositionAdapter.clearData();
                }
            }
        }
    }

    @Override
    public void onConfirmClick() {
        mAccountPresenter.settlementConfirm(SpUtil.getString(Constant.CACHE_TAG_UID));
    }
}
