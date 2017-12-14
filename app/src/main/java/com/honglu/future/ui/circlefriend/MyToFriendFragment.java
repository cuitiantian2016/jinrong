package com.honglu.future.ui.circlefriend;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.honglu.future.R;
import com.honglu.future.base.BasePresenter;
import com.honglu.future.base.CommonFragment;
import com.honglu.future.config.Constant;
import com.honglu.future.events.MessageController;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.circle.bean.UserList;
import com.honglu.future.ui.trade.fragment.TradeFragment;
import com.honglu.future.util.SpUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;


public class MyToFriendFragment extends CommonFragment {
    @BindView(R.id.listview)
    ListView mListView;
    @BindView(R.id.smart_refresh)
    SmartRefreshLayout mSmartRefresh;
    @BindView(R.id.friend1_empty_view)
    LinearLayout empty_view;

    private MyFriendsAdapter mAdapter;
    private String type = "1", follow_id = null, follow_id_temp = "0";
    private boolean isLoadingNow = false;
    private BasePresenter<MyToFriendFragment> mBasePresenter;
    int rows;
    private boolean isMore;
    private boolean mIsRefresh;
    public static MyToFriendFragment myToFriendFragment;

    public static MyToFriendFragment getInstance() {
        if (myToFriendFragment == null) {
            myToFriendFragment = new MyToFriendFragment();
        }
        return myToFriendFragment;
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
        follow_id_temp = null;
        rows = 0;
        getFriends(true);
    }

    public void refresh() {
        follow_id_temp = null;
        rows = 0;
        getFriends(true);
    }

    private void initViews() {
        rows = 0;
        mAdapter = new MyFriendsAdapter(1, mListView, mActivity);
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

        MessageController.getInstance().setFriendCountChange(new MessageController.FriendCountChange() {
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
            mBasePresenter = new BasePresenter<MyToFriendFragment>(this) {
                @Override
                public void getData() {
                    super.getData();
                    toSubscribe(HttpManager.getApi().loadMyFocusList(SpUtil.getString(Constant.CACHE_TAG_UID), String.valueOf(rows), "10"), new HttpSubscriber<List<UserList>>() {
                        @Override
                        protected void _onNext(List<UserList> o) {
                            super._onNext(o);
                            if (o == null || o.size() == 0) {
                                empty_view.setVisibility(View.VISIBLE);
                                mSmartRefresh.setVisibility(View.GONE);
//                                if (o != null) {
//                                    ((MyFriendActivity) getActivity()).setData(o.size());
//                                }
                            } else {
                                if (mIsRefresh)   //下拉刷新
                                    mAdapter.clearDatas();
                                if (o.size() == 0) {
                                    if (mAdapter.getCount() == 0) {
                                        empty_view.setVisibility(View.VISIBLE);
                                        mSmartRefresh.setVisibility(View.GONE);
                                    } else {
                                        empty_view.setVisibility(View.GONE);
                                        mSmartRefresh.setVisibility(View.VISIBLE);
                                    }
                                } else if (o.size() > 0 && o.size() < 10) {
                                    follow_id_temp = o.get(o.size() - 1).userId;
                                    empty_view.setVisibility(View.GONE);
                                    mSmartRefresh.setVisibility(View.VISIBLE);
                                    mAdapter.setDatas(o);
                                } else if (o.size() >= 10) {
                                    follow_id_temp = o.get(o.size() - 1).userId;
                                    empty_view.setVisibility(View.GONE);
                                    mSmartRefresh.setVisibility(View.VISIBLE);
                                    mAdapter.setDatas(o);
                                }
                                if (o.size() >= 10) {
                                    ++rows;
                                    isMore = true;
                                } else {
                                    isMore = false;
                                }
//                                ((MyFriendActivity) getActivity()).setData(o.size());
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
