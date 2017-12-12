package com.honglu.future.ui.circlefriend;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

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


public class MyToFriendFragment extends CommonFragment {
    @BindView(R.id.listview)
    ListView mListView;
    @BindView(R.id.smart_refresh)
    SmartRefreshLayout mSmartRefresh;
    @BindView(R.id.friend1_empty_view)
    LinearLayout empty_view;

    private MyFriendsAdapter mAdapter;
    private String type = "1", follow_id = null, follow_id_temp = "0";
    private boolean isLoadingNow = false, isLoadingFinished = false;
    private BasePresenter<MyToFriendFragment> mBasePresenter;

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
        isLoadingFinished = false;
        getFriends("pull_down");
    }

    public void refresh() {
        follow_id_temp = null;
        isLoadingFinished = false;
        getFriends("pull_down");
    }

    private void initViews() {
        mAdapter = new MyFriendsAdapter("1", mListView, mContext, scrollToLastCallBack);
        mListView.setAdapter(mAdapter);

//        MessageController.getInstance().setFriendCountChange(new MessageController.FriendCountChange() {
//            @Override
//            public void change() {
//                follow_id_temp = null;
//                isLoadingFinished = false;
//                getFriends("pull_down");
//            }
//        });
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
            follow_id = null;
        }

        if (mBasePresenter == null) {
            mBasePresenter = new BasePresenter<MyToFriendFragment>(this) {
                @Override
                public void getData() {
                    super.getData();
                    toSubscribe(HttpManager.getApi().loadMyFocusList(SpUtil.getString(Constant.CACHE_TAG_UID), "0", "10"), new HttpSubscriber<List<UserList>>() {
                        @Override
                        protected void _onNext(List<UserList> o) {
                            super._onNext(o);
                            if (o == null || o.size() == 0) {
                                isLoadingFinished = true;
                                empty_view.setVisibility(View.VISIBLE);
                                mSmartRefresh.setVisibility(View.GONE);
                            } else {
                                if (pull_style.equals("pull_down"))   //下拉刷新
                                    mAdapter.clearDatas();
                                if (o.size() == 0) {
                                    isLoadingFinished = true;
                                    if (mAdapter.getCount() == 0) {
                                        empty_view.setVisibility(View.VISIBLE);
                                        mSmartRefresh.setVisibility(View.GONE);
                                    } else {
                                        empty_view.setVisibility(View.GONE);
                                        mSmartRefresh.setVisibility(View.VISIBLE);
                                    }
                                } else if (o.size() > 0 && o.size() < 10) {
                                    follow_id_temp = o.get(o.size() - 1).userId;
                                    isLoadingFinished = true;
                                    empty_view.setVisibility(View.GONE);
                                    mSmartRefresh.setVisibility(View.VISIBLE);
                                    mAdapter.setDatas(o);
                                } else if (o.size() >= 10) {
                                    follow_id_temp = o.get(o.size() - 1).userId;
                                    isLoadingFinished = false;
                                    empty_view.setVisibility(View.GONE);
                                    mSmartRefresh.setVisibility(View.VISIBLE);
                                    mAdapter.setDatas(o);
                                }
                            }

//                            ((MyFriendActivity) getActivity()).setdata(result);
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
