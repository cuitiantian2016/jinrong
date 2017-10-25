package com.honglu.future.ui.home.fragment;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.base.BasePresenter;
import com.honglu.future.events.FragmentRefreshEvent;
import com.honglu.future.events.UIBaseEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
/**********
 * 首页
 */
public class HomeFragment extends BaseFragment<BasePresenter>{
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
    }
    @Override
    public void loadData() {
        EventBus.getDefault().register(this);
        initView();
    }
    private void initView() {
        mTitle.setTitle(false, R.color.white, "首页");
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
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        homeFragment = null;
    }
}
