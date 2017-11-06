package com.honglu.future.ui.trade.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.config.Constant;
import com.honglu.future.ui.main.contract.AccountContract;
import com.honglu.future.ui.main.presenter.AccountPresenter;
import com.honglu.future.ui.trade.adapter.ClosePositionAdapter;
import com.honglu.future.ui.trade.bean.AccountBean;
import com.honglu.future.ui.trade.bean.ClosePositionListBean;
import com.honglu.future.ui.trade.contract.ClosePositionContract;
import com.honglu.future.ui.trade.presenter.ClosePositionPresenter;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.Tool;
import com.honglu.future.util.ViewUtil;
import com.honglu.future.widget.loading.LoadingLayout;
import com.honglu.future.widget.popupwind.AccountLoginPopupView;
import com.honglu.future.widget.popupwind.BottomPopupWindow;
import com.honglu.future.widget.recycler.DividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.honglu.future.util.ToastUtil.showToast;

/**
 * Created by zq on 2017/10/26.
 */

public class ClosePositionFragment extends BaseFragment<ClosePositionPresenter> implements ClosePositionContract.View, AccountContract.View {
    @BindView(R.id.rv_position)
    RecyclerView mPositionListView;
    @BindView(R.id.tv_tip)
    TextView mTvTip;
    @BindView(R.id.loading_layout)
    LoadingLayout mLoadingLayout;
    private ClosePositionAdapter mClosePositionAdapter;
    private BottomPopupWindow mTipPopupWindow;
    private AccountLoginPopupView mAccountLoginPopupView;
    private AccountPresenter mAccountPresenter;

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
        initView();
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (!App.getConfig().getAccountLoginStatus()) {
                if (isVisible()) {
                    mAccountLoginPopupView = new AccountLoginPopupView(mActivity, mTvTip, mAccountPresenter);
                    mAccountLoginPopupView.showOpenAccountWindow();
                }
            } else {
                if (isVisible()) {
                    getClosePositionList();
                }
            }
        }
    }

    private void getClosePositionList() {
        // TODO: 2017/11/6 传的日期需要确认 zq
        mPresenter.getCloseList("", "2017-11-06", SpUtil.getString(Constant.CACHE_TAG_UID), SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN), "", "");
    }

    @Override
    public void loginSuccess(AccountBean bean) {
        mAccountLoginPopupView.dismissLoginAccountView();
    }

    private void showTipWindow(View view) {
        View layout = LayoutInflater.from(mActivity).inflate(R.layout.layout_trade_tip_pop_window, null);
        showTipBottomWindow(view, layout);
        ViewUtil.backgroundAlpha(mActivity, .5f);
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
                ViewUtil.backgroundAlpha(mActivity, 1f);
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

    @Override
    public void getCloseListSuccess(List<ClosePositionListBean> list) {
        if (list == null || list.size() == 0) {
            mLoadingLayout.setStatus(LoadingLayout.Empty);
            return;
        }
        mLoadingLayout.setStatus(LoadingLayout.Success);
        mClosePositionAdapter.clearData();
        mClosePositionAdapter.addData(list);
    }
}
