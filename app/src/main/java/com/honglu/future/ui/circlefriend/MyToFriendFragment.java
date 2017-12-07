package com.honglu.future.ui.circlefriend;

import android.widget.LinearLayout;
import android.widget.ListView;

import com.honglu.future.R;
import com.honglu.future.base.BaseFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;


public class MyToFriendFragment extends BaseFragment<MyFriendPresenter> implements MyFriendContract.View {
    @BindView(R.id.listview)
    ListView mListView;
    @BindView(R.id.smart_refresh)
    SmartRefreshLayout mSmartRefresh;
    @BindView(R.id.friend1_empty_view)
    LinearLayout empty_view;

    private MyFriendsAdapter mAdapter;
    private String type = "1", follow_id = null, follow_id_temp = "0";
    private boolean isLoadingNow = false, isLoadingFinished = false;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_friend;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
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

        mSmartRefresh.setEnableRefresh(true);
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
//        ServerAPI.getFriendList(mContext, type, follow_id,null, new ServerCallBack<GetFriends>() {
//            @Override
//            public void onSucceed(Context context, GetFriends result) {
//
//                if (result.userList == null || result.userList.size()==0) {
//                    isLoadingFinished = true;
//                    empty_view.setVisibility(View.VISIBLE);
//                    mPullToRefreshListView.setVisibility(View.GONE);
//                } else {
//                    if (pull_style.equals("pull_down"))   //下拉刷新
//                        mAdapter.clearDatas();
//                    if (result.userList.size() == 0) {
//                        isLoadingFinished = true;
//                        if (mAdapter.getCount() == 0) {
//                            empty_view.setVisibility(View.VISIBLE);
//                            mPullToRefreshListView.setVisibility(View.GONE);
//                        } else {
//                            empty_view.setVisibility(View.GONE);
//                            mPullToRefreshListView.setVisibility(View.VISIBLE);
//                        }
//                    } else if (result.userList.size() > 0 && result.userList.size() < 20) {
//                        follow_id_temp = result.userList.get(result.userList.size() - 1).fid;
//                        isLoadingFinished = true;
//                        empty_view.setVisibility(View.GONE);
//                        mPullToRefreshListView.setVisibility(View.VISIBLE);
//                        mAdapter.setDatas(result.userList);
//                    } else if (result.userList.size() >= 20) {
//                        follow_id_temp = result.userList.get(result.userList.size() - 1).fid;
//                        isLoadingFinished = false;
//                        empty_view.setVisibility(View.GONE);
//                        mPullToRefreshListView.setVisibility(View.VISIBLE);
//                        mAdapter.setDatas(result.userList);
//                    }
//                }
//                ((MyFriendActivity) getActivity()).setdata(result);
//            }
//
//            @Override
//            public void onError(Context context, String errorMsg) {
//
//            }
//
//            @Override
//            public void onFinished(Context context) {
//                isLoadingNow = false;
//                mPullToRefreshListView.onRefreshComplete();
//            }
//        });
    }
}
