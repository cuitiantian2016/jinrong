package com.honglu.future.ui.usercenter.task;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.ui.usercenter.bean.TaskBean;
import com.honglu.future.util.AndroidUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;


/**
 * deprecation:任务列表
 * author:ayb
 * time:2017/6/15
 */
public class TaskFragment extends BaseFragment {
    public static final String EXTRA_IS_NEW = "EXTRA_IS_NEW";
    private boolean isRequesting;//是否正在请求中

    private SmartRefreshLayout mPullToRefreshView;
    private View mLLEmpty;
    private boolean mIsNew;
    private TaskAdapter mAdapter;
    private int rows = 0;

    String[] urls = {"xiaoniuqihuo://future/future/main",
            "xiaoniuqihuo://future/future/main?select=2",
            "xiaoniuqihuo://future/future/main?select=1&isTrade=0",
            "xiaoniuqihuo://future/future/main?select=1&isTrade=1",
            "xiaoniuqihuo://future/future/main?select=1&isTrade=2",
            "xiaoniuqihuo://future/future/main?select=1&isTrade=3",
            "xiaoniuqihuo://future/future/systemmsgdetail?msgId=ssdsdasds",
            "xiaoniuqihuo://future/future/newsdetail?informationId=45&position=0",
            "xiaoniuqihuo://future/future/main?select=3"
            , "kaihu",
            "xiaoniuqihuo://future/future/modify", "xiaoniuqihuo://future/trade/pay"};


    boolean mIsRefresh;
    int count;

    private void getFriendList(final boolean isRefresh) {
        if (isRequesting) return;
        isRequesting = true;
        mIsRefresh = isRefresh;
        if (mIsRefresh) {
            rows = 0;
        }
        ArrayList<TaskBean> taskBeenlist = new ArrayList<>();
        for (String url : urls) {
            TaskBean taskBean = new TaskBean();
            ++count;
            taskBean.title = url;
            taskBean.content = count + "牛逼";
            taskBean.isComplete = false;
            taskBean.url = url;
            taskBeenlist.add(taskBean);
        }
        mAdapter.setDatas(taskBeenlist);
//        HttpManager.getApi().getPraiseImageList(SpUtil.getString(Constant.CACHE_TAG_UID),mTopicId,rows).compose(RxHelper.<List<UserList>>handleSimplyResult())
//                .subscribe(new HttpSubscriber<List<UserList>>() {
//                    @Override
//                    protected void _onNext(List<UserList> userLists) {
//                        super._onNext(userLists);
//                        isRequesting = false;
//                        if (mIsRefresh){
//                            mPullToRefreshView.finishRefresh();
//                            //mAdapter.refreshDatas(userLists);
//                        }else {
//                            mPullToRefreshView.finishLoadmore();
//                           // mAdapter.loadMoreDatas(userLists);
//                        }
//                        isMore = userLists.size() >= 10;
//                        if (isMore){
//                            ++rows;
//                        }
//                        mPullToRefreshView.setEnableLoadmore(isMore);
//                    }
//
//                    @Override
//                    protected void _onError(String message) {
//                        super._onError(message);
//                        ToastUtil.show(message);
//                        isRequesting = false;
//                        mPullToRefreshView.finishLoadmore();
//                        mPullToRefreshView.finishRefresh();
//                    }
//                });
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
        mLLEmpty = mView.findViewById(R.id.ll_empty);
        mPullToRefreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getFriendList(true);
            }
        });
        ListView listView = (ListView) mView.findViewById(R.id.lv_listView);
        mAdapter = new TaskAdapter();
        listView.setAdapter(mAdapter);
        listView.setDividerHeight(AndroidUtil.dip2px(getContext(), 10));
        getFriendList(true);
    }

}
