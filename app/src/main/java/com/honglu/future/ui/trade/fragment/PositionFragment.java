package com.honglu.future.ui.trade.fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.config.Constant;
import com.honglu.future.dialog.PositionDialog;
import com.honglu.future.ui.main.contract.AccountContract;
import com.honglu.future.ui.main.presenter.AccountPresenter;
import com.honglu.future.ui.trade.adapter.PositionAdapter;
import com.honglu.future.ui.trade.bean.AccountBean;
import com.honglu.future.ui.trade.bean.HoldDetailBean;
import com.honglu.future.ui.trade.bean.HoldPositionBean;
import com.honglu.future.ui.trade.contract.PositionContract;
import com.honglu.future.ui.trade.presenter.PositionPresenter;
import com.honglu.future.util.SpUtil;
import com.honglu.future.widget.popupwind.AccountLoginPopupView;
import com.honglu.future.widget.popupwind.PositionPopWind;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.honglu.future.util.ToastUtil.showToast;

/**
 * Created by zq on 2017/10/26.
 */
public class PositionFragment extends BaseFragment<PositionPresenter> implements PositionContract.View, AccountContract.View, PositionPopWind.OnButtonClickListener, PositionAdapter.OnShowPopupClickListener {
    @BindView(R.id.lv_listView)
    ListView mListView;
    @BindView(R.id.srl_refreshView)
    SmartRefreshLayout mRefreshView;
    @BindView(R.id.tv_remarksEmpty)
    TextView mRemarksEmpty;
    private View mFooterEmptyView;

    private PositionAdapter mAdapter;
    private AccountPresenter mAccountPresenter;
    private AccountLoginPopupView mAccountLoginPopupView;
    private PositionDialog mPositionDialog;
    private PositionPopWind mPopWind;
    private boolean mIsVisible;
    private HoldPositionBean mHoldPositionBean;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_position;
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
    public void initPresenter() {
        mPresenter.init(this);
        mAccountPresenter = new AccountPresenter();
        mAccountPresenter.init(this);
    }

    public void setEmptyView(boolean isEmpty) {
        if (isEmpty) {
            mRemarksEmpty.setVisibility(View.VISIBLE);
            mFooterEmptyView.setVisibility(View.VISIBLE);
            if (mListView.getFooterViewsCount() <= 0 && mFooterEmptyView != null) {
                mListView.addFooterView(mFooterEmptyView);
            }
        } else {
            mRemarksEmpty.setVisibility(View.GONE);
            mFooterEmptyView.setVisibility(View.GONE);
            if (mListView.getFooterViewsCount() > 0 && mFooterEmptyView != null) {
                mListView.removeFooterView(mFooterEmptyView);
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (!App.getConfig().getAccountLoginStatus()) {
                if (isVisible()) {
                    mIsVisible = true;
                    mAccountLoginPopupView = new AccountLoginPopupView(mActivity, mRemarksEmpty, mAccountPresenter);
                    mAccountLoginPopupView.showOpenAccountWindow();
                }
            } else {
                if (isVisible()) {
                    getPositionList();
                }
            }
        }
    }

    @Override
    public void loadData() {
        mPopWind = new PositionPopWind(mContext);
        mPositionDialog = new PositionDialog(mContext);
        mAdapter = new PositionAdapter(PositionFragment.this);
        View headView = LayoutInflater.from(mContext).inflate(R.layout.layout_trade_position_list_header, null);
        mFooterEmptyView = LayoutInflater.from(mContext).inflate(R.layout.layout_trade_position_emptyview, null);
        mListView.addHeaderView(headView);
        mListView.addFooterView(mFooterEmptyView);
        mListView.setAdapter(mAdapter);
        setEmptyView(true);

        mAdapter.setOnShowPopupClickListener(this);
        mPopWind.setOnButtonClickListener(this);
        mRefreshView.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getPositionList();
            }
        });
    }

    private void getPositionList() {
        mPresenter.getHoldPositionList(SpUtil.getString(Constant.CACHE_TAG_UID), SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN), "GUOFU");
    }

    @Override
    public void loginSuccess(AccountBean bean) {
        mAccountLoginPopupView.dismissLoginAccountView();
    }


    @Override
    public void getHoldPositionListSuccess(List<HoldPositionBean> list) {
        mAdapter.notifyDataChanged(false, list);
        mRefreshView.finishRefresh();
    }

    @Override
    public void getHoldDetailListSuccess(List<HoldDetailBean> list) {
        mPositionDialog.showPopupWind(mHoldPositionBean, list);
    }

    @Override
    public void onDetailClick(HoldPositionBean bean) {
        mHoldPositionBean = bean;
        mPresenter.getHoldPositionDetail(bean.getInstrumentId(),
                String.valueOf(bean.getType()),
                String.valueOf(bean.getTodayPosition()),
                SpUtil.getString(Constant.CACHE_TAG_UID),
                SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN));
    }

    @Override
    public void onShowPopupClick(View view, HoldPositionBean bean) {
        mPopWind.showPopupWind(view, bean);
    }
}
