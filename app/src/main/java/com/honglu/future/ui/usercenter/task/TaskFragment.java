package com.honglu.future.ui.usercenter.task;

import android.widget.ListView;

import com.honglu.future.R;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.config.Constant;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.http.RxHelper;
import com.honglu.future.ui.usercenter.bean.TaskBean;
import com.honglu.future.util.AndroidUtil;
import com.honglu.future.util.SpUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;


/**
 * deprecation:任务列表
 * author:ayb
 * time:2017/6/15
 */
public class TaskFragment extends BaseFragment {
    public static final String EXTRA_IS_NEW = "EXTRA_IS_NEW";
    private boolean isRequesting;//是否正在请求中

    private SmartRefreshLayout mPullToRefreshView;
    private boolean mIsNew;
    private TaskAdapter mAdapter;
    boolean mIsRefresh;

    private void getFriendList(final boolean isRefresh) {
        if (isRequesting) return;
        isRequesting = true;
        mIsRefresh = isRefresh;
        HttpManager.getApi().getTaskList(SpUtil.getString(Constant.CACHE_TAG_UID), mIsNew ? 1 : 2).compose(RxHelper.<List<TaskBean>>handleSimplyResult())
                .subscribe(new HttpSubscriber<List<TaskBean>>() {
                    @Override
                    protected void _onNext(List<TaskBean> userLists) {
                        super._onNext(userLists);
                        isRequesting = false;
                        if (mIsRefresh) {
                            mPullToRefreshView.finishRefresh();
                        } else {
                            mPullToRefreshView.finishLoadmore();
                        }
                        if (userLists == null || userLists.size() <= 0) {
                            if (mIsNew) {
                                ((TaskActivity) getActivity()).hindTab();
                            }
                        }
                        mAdapter.setDatas(userLists);
                    }

                    @Override
                    protected void _onError(String message) {
                        super._onError(message);
                        isRequesting = false;
                        mPullToRefreshView.finishRefresh();
                    }
                });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_task_list;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        if (getArguments() != null) {
            mIsNew = getArguments().getBoolean(EXTRA_IS_NEW);
        }
        mPullToRefreshView = (SmartRefreshLayout) mView.findViewById(R.id.srl_refreshView);
        mPullToRefreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getFriendList(true);
            }
        });
        ListView listView = (ListView) mView.findViewById(R.id.lv_listView);
        mAdapter = new TaskAdapter(getActivity());
        listView.setAdapter(mAdapter);
        listView.setDividerHeight(AndroidUtil.dip2px(getContext(), 10));
        getFriendList(true);
    }

}
