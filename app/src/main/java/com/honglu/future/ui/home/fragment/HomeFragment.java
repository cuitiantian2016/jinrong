package com.honglu.future.ui.home.fragment;

import android.text.TextUtils;

import android.view.View;
import android.view.View.OnClickListener;


import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.events.FragmentRefreshEvent;
import com.honglu.future.events.UIBaseEvent;
import com.honglu.future.ui.home.bean.HomeIndexResponseBean;
import com.honglu.future.ui.home.contract.HomeContract;
import com.honglu.future.ui.home.presenter.HomePresenter;
import com.honglu.future.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**********
 * 首页
 */
public class HomeFragment extends BaseFragment<HomePresenter> implements OnClickListener,
        HomeContract.View {
    public static HomeFragment homeFragment;



    public static HomeFragment getInstance() {
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        return homeFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home_layout;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        EventBus.getDefault().register(this);
        initView();

    }

    private void initView() {
        mTitle.setTitle(false, R.color.white, "首页");
    }


    /**
     * 点击我要借款返回成功
     *
     * @param result
     * @param isClickLend
     */
    @Override
    public void indexSuccess(HomeIndexResponseBean result, boolean isClickLend) {

    }


    @Override
    public void confirmFailedSuccess() {

    }

    @Override
    public void showLoading(String content) {

    }

    @Override
    public void stopLoading() {
        App.hideLoading();
    }

    /**
     * @param msg  请求异常信息
     * @param type 若有多个请求，用于区分不同请求（不同请求失败或有不同的处理）
     */
    @Override
    public void showErrorMsg(String msg, String type) {
        if (TextUtils.isEmpty(type)) {
            return;
        }
        if (type.equals(mPresenter.TYPE_INDEX)) {//首页接口
            //Toast.makeText(mainActivity, msg, Toast.LENGTH_SHORT).show();
            ToastUtil.showToast(msg);
        } else if (type.equals(mPresenter.TYPE_FAILED)) { //借款失败调用
            mPresenter.loadIndex(false);
        } else if (type.equals(mPresenter.TYPE_LOAN)) {   //验证借款信息
            ToastUtil.showToast(msg);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(FragmentRefreshEvent event) {
        //当借款申请成功、还款或续期成功、登录成、退出功时刷新数据
        if (event.getType() == UIBaseEvent.EVENT_LOAN_SUCCESS ||
                event.getType() == UIBaseEvent.EVENT_LOGIN ||
                event.getType() == UIBaseEvent.EVENT_REPAY_SUCCESS ||
                event.getType() == UIBaseEvent.EVENT_LOGOUT) {
            // 数据刷新
            if (App.getConfig().getLoginStatus()) {
            } else {

            }
        }
    }




    @Override
    public void onResume() {
        super.onResume();
        //setPaddingAndFillStatusBar(HomeFragment.getInstance());
        MobclickAgent.onPageStart("首页"); //统计页面，"MainScreen"为页面名称，可自定义
        MobclickAgent.onEvent(getContext(), "lend");
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("首页");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        homeFragment = null;
    }

    @Override
    public void onClick(View v) {

    }
}
