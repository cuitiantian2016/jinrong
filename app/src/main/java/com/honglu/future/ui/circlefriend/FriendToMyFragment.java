package com.honglu.future.ui.circlefriend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.base.BasePresenter;
import com.honglu.future.base.CommonFragment;
import com.honglu.future.config.Constant;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.circle.bean.UserList;
import com.honglu.future.util.SpUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;


public class FriendToMyFragment extends CommonFragment {
    private Context mContext;
    private MyFriendsAdapter mAdapter;

    @BindView(R.id.listview)
    ListView mListView;
    @BindView(R.id.smart_refresh)
    SmartRefreshLayout mSmartRefresh;
    View empty_view;

    private String type = "2", follow_id = "0", follow_id_temp = "0";
    private boolean isLoadingNow = false, isLoadingFinished = false;
    private BasePresenter<FriendToMyFragment> mBasePresenter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_friend;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void loadData() {
        initViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        follow_id_temp = "0";
        isLoadingFinished = false;
        getFriends("pull_down");
    }

    public void refresh() {
        follow_id_temp = "0";
        isLoadingFinished = false;
        getFriends("pull_down");
    }

    private void initViews() {
        mSmartRefresh.setEnableRefresh(true);
        empty_view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_bbs_empty, null);
        TextView empty_text = (TextView) empty_view.findViewById(R.id.empty_tv);
        empty_text.setText("你还没有粉丝哦~");
        mAdapter = new MyFriendsAdapter("2", mListView, mContext, scrollToLastCallBack);
        mListView.setAdapter(mAdapter);
        mSmartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (!isLoadingNow) {
                    follow_id_temp = "0";
                    isLoadingFinished = false;
                    getFriends("pull_down");
                }
            }
        });
    }

    //滑动加载更多
    MyFriendsAdapter.ScrollToLastCallBack scrollToLastCallBack = new MyFriendsAdapter.ScrollToLastCallBack() {
        @Override
        public void onScrollToLast(Integer pos) {
            if (!isLoadingNow) {
                getFriends("pull_up");
            }
        }
    };

    private void getFriends(final String pull_style) {
        if (isLoadingNow || isLoadingFinished) {
            return;
        }
        isLoadingNow = true;
        if (pull_style.equals("pull_up")) {  //加载更多
            follow_id = follow_id_temp;
        } else if (pull_style.equals("pull_down")) {  //下拉刷新
            follow_id = "0";
        }
        if (mBasePresenter == null) {
            mBasePresenter = new BasePresenter<FriendToMyFragment>(this) {
                @Override
                public void getData() {
                    super.getData();
                    toSubscribe(HttpManager.getApi().loadMyBeFocusList(SpUtil.getString(Constant.CACHE_TAG_UID), "0", "10"), new HttpSubscriber<List<UserList>>() {
                        @Override
                        protected void _onNext(List<UserList> o) {
                            super._onNext(o);
                            if (o == null) {
                                isLoadingFinished = true;
                                if (mListView.getFooterViewsCount() == 0)
                                    mListView.addFooterView(empty_view, null, false);
                            } else {
                                if (pull_style.equals("pull_down"))   //下拉刷新
                                    mAdapter.clearDatas();
                                if (o.size() == 0) {
                                    isLoadingFinished = true;
                                    if (mAdapter.getCount() == 0) {
                                        if (mListView.getFooterViewsCount() == 0)
                                            mListView.addFooterView(empty_view, null, false);
                                    } else {
                                        if (mListView.getFooterViewsCount() != 0)
                                            mListView.removeFooterView(empty_view);
                                    }
                                } else if (o.size() > 0 && o.size() < 10) {
                                    follow_id_temp = o.get(o.size() - 1).userId;
                                    isLoadingFinished = true;
                                    if (mListView.getFooterViewsCount() != 0)
                                        mListView.removeFooterView(empty_view);
                                    mAdapter.setDatas(o);
                                } else if (o.size() >= 10) {
                                    follow_id_temp = o.get(o.size() - 1).userId;
                                    isLoadingFinished = false;
                                    if (mListView.getFooterViewsCount() != 0)
                                        mListView.removeFooterView(empty_view);
                                    mAdapter.setDatas(o);
                                }
                            }
                            mSmartRefresh.finishRefresh();
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            mSmartRefresh.finishRefresh();
                        }
                    });
                }
            };
        }
        mBasePresenter.getData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBasePresenter.onDestroy();
    }
}
