package com.honglu.future.ui.circlefriend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.base.BaseFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;


public class FriendToMyFragment extends BaseFragment<MyFriendPresenter> implements MyFriendContract.View {
    private Context mContext;
    private MyFriendsAdapter mAdapter;

    @BindView(R.id.listview)
    ListView mListView;
    @BindView(R.id.smart_refresh)
    SmartRefreshLayout mSmartRefresh;
    View empty_view;

    private String type = "2", follow_id = "0", follow_id_temp = "0";
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
//        ServerAPI.getFriendList(mContext, type, follow_id, null, new ServerCallBack<GetFriends>() {
//            @Override
//            public void onSucceed(Context context, GetFriends result) {
//                if (result == null) {
//                    isLoadingFinished = true;
//                    if (mListView.getFooterViewsCount() == 0)
//                        mListView.addFooterView(empty_view, null, false);
//                } else {
//                    if (pull_style.equals("pull_down"))   //下拉刷新
//                        mAdapter.clearDatas();
//                    if (result.userList.size() == 0) {
//                        isLoadingFinished = true;
//                        if (mAdapter.getCount() == 0) {
//                            if (mListView.getFooterViewsCount() == 0)
//                                mListView.addFooterView(empty_view, null, false);
//                        } else {
//                            if (mListView.getFooterViewsCount() != 0)
//                                mListView.removeFooterView(empty_view);
//                        }
//                    } else if (result.userList.size() > 0 && result.userList.size() < 20) {
//                        follow_id_temp = result.userList.get(result.userList.size() - 1).fid;
//                        isLoadingFinished = true;
//                        if (mListView.getFooterViewsCount() != 0)
//                            mListView.removeFooterView(empty_view);
//                        mAdapter.setDatas(result.userList);
//                    } else if (result.userList.size() >= 20) {
//                        follow_id_temp = result.userList.get(result.userList.size() - 1).fid;
//                        isLoadingFinished = false;
//                        if (mListView.getFooterViewsCount() != 0)
//                            mListView.removeFooterView(empty_view);
//                        mAdapter.setDatas(result.userList);
//                    }
//                }
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
