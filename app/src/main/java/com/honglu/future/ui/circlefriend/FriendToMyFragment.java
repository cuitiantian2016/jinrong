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
import com.honglu.future.events.MessageController;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.circle.bean.UserList;
import com.honglu.future.util.SpUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
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
    private boolean isLoadingNow = false;
    private BasePresenter<FriendToMyFragment> mBasePresenter;
    int rows;
    private boolean isMore;
    private boolean mIsRefresh;
    public static FriendToMyFragment friendToMyFragment;

    public static FriendToMyFragment getInstance() {
        if (friendToMyFragment == null) {
            friendToMyFragment = new FriendToMyFragment();
        }
        return friendToMyFragment;
    }

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
        rows = 0;
        getFriends(true);
    }

    public void refresh() {
        follow_id_temp = "0";
        rows = 0;
        getFriends(true);
    }

    private void initViews() {
        rows = 0;
        mSmartRefresh.setEnableRefresh(true);
        empty_view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_bbs_empty, null);
        TextView empty_text = (TextView) empty_view.findViewById(R.id.empty_tv);
        empty_text.setText("你还没有粉丝哦~");
        mAdapter = new MyFriendsAdapter(2, mListView, mActivity);
        mListView.setAdapter(mAdapter);
        mSmartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (!isLoadingNow) {
                    follow_id_temp = "0";
                    rows = 0;
                    getFriends(true);
                }
            }
        });

        mSmartRefresh.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (!isLoadingNow && isMore) {//上拉加载更多
                    getFriends(false);
                } else {
                    mSmartRefresh.finishLoadmore();
                }
            }
        });

        MessageController.getInstance().setBeFocusedCountChange(new MessageController.BeFocusedCountChange() {
            @Override
            public void change() {
                if (!isLoadingNow) {
                    follow_id_temp = "0";
                    rows = 0;
                    getFriends(true);
                }
            }
        });
    }


    private void getFriends(boolean isRefresh) {
        isLoadingNow = true;
        mIsRefresh = isRefresh;
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
                                if (mListView.getFooterViewsCount() == 0)
                                    mListView.addFooterView(empty_view, null, false);
                            } else {
                                if (mIsRefresh)   //下拉刷新
                                    mAdapter.clearDatas();
                                if (o.size() == 0) {
                                    if (mAdapter.getCount() == 0) {
                                        if (mListView.getFooterViewsCount() == 0)
                                            mListView.addFooterView(empty_view, null, false);
                                    } else {
                                        if (mListView.getFooterViewsCount() != 0)
                                            mListView.removeFooterView(empty_view);
                                    }
//                                    ((MyFriendActivity) getActivity()).setData(o.size());
                                } else if (o.size() > 0 && o.size() < 10) {
                                    follow_id_temp = o.get(o.size() - 1).userId;
                                    if (mListView.getFooterViewsCount() != 0)
                                        mListView.removeFooterView(empty_view);
                                    mAdapter.setDatas(o);
                                } else if (o.size() >= 10) {
                                    follow_id_temp = o.get(o.size() - 1).userId;
                                    if (mListView.getFooterViewsCount() != 0)
                                        mListView.removeFooterView(empty_view);
                                    mAdapter.setDatas(o);
                                }
                                if (o.size() >= 10) {
                                    ++rows;
                                    isMore = true;
                                } else {
                                    isMore = false;
                                }
                            }
                            mSmartRefresh.setEnableLoadmore(isMore);
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                        }

                        @Override
                        public void onCompleted() {
                            super.onCompleted();
                            isLoadingNow = false;
                            mSmartRefresh.finishRefresh();
                            mSmartRefresh.finishLoadmore();
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
