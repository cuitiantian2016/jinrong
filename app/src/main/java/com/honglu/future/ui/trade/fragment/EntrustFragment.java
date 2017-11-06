package com.honglu.future.ui.trade.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.config.Constant;
import com.honglu.future.ui.main.contract.AccountContract;
import com.honglu.future.ui.main.presenter.AccountPresenter;
import com.honglu.future.ui.trade.adapter.EntrustAdapter;
import com.honglu.future.ui.trade.bean.AccountBean;
import com.honglu.future.ui.trade.bean.EntrustBean;
import com.honglu.future.ui.trade.contract.EntrustContract;
import com.honglu.future.ui.trade.presenter.EntrustPresenter;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.Tool;
import com.honglu.future.widget.loading.LoadingLayout;
import com.honglu.future.widget.popupwind.AccountLoginPopupView;
import com.honglu.future.widget.recycler.DividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.honglu.future.util.ToastUtil.showToast;

/**
 * Created by zq on 2017/10/26.
 */

public class EntrustFragment extends BaseFragment<EntrustPresenter> implements EntrustContract.View, AccountContract.View, EntrustAdapter.OnCancelClickListener {
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

    private AccountLoginPopupView mAccountLoginPopupView;
    private AccountPresenter mAccountPresenter;
    private EntrustAdapter mEntrustAdapter;
    private boolean mIsShowFilter;

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
            if (!App.getConfig().getAccountLoginStatus()) {
                if (isVisible()) {
                    mAccountLoginPopupView = new AccountLoginPopupView(mActivity, mIvTip, mAccountPresenter);
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
        initView();
        setButtonListeners();
    }

    private void initView() {
        mEntrustListView.setLayoutManager(new LinearLayoutManager(mContext));
        mEntrustListView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mEntrustAdapter = new EntrustAdapter();
        mEntrustListView.setAdapter(mEntrustAdapter);
    }

    private void getPositionList() {
        mPresenter.getEntrustList(SpUtil.getString(Constant.CACHE_TAG_UID), SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN));
    }

    @OnClick({R.id.tv_see_all})
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
            }
        });
    }

    @Override
    public void loginSuccess(AccountBean bean) {
        mAccountLoginPopupView.dismissLoginAccountView();
    }

    @Override
    public void getEntrustListSuccess(List<EntrustBean> list) {
        if (list == null || list.size() == 0) {
            mLoadingLayout.setStatus(LoadingLayout.Empty);
            return;
        }
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
        mEntrustAdapter.clearData();
        getPositionList();
    }

}
