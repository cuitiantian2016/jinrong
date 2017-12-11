package com.honglu.future.ui.circle.praisesandreward;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.honglu.future.R;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.ui.circle.circlemain.OnClickThrottleListener;
import com.honglu.future.ui.trade.fragment.PagerFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


/**
 * deprecation:点赞列表
 * author:ayb
 * time:2017/6/15
 */
public class ThumbsListFragment extends BaseFragment {
    public static final String EXTRA_TID = "tid";
    public static final String EXTRA_TIDE_ID = "attutude_id";
    private boolean isRequesting;//是否正在请求中

    private SmartRefreshLayout mPullToRefreshView;
    private String mTopicId;
    private String mTideId;
    private GetFriendsAdapter mAdapter;
    private ImageView mFollowIv;
    private boolean isMore;


    private void getFriendList(final boolean isRefresh) {
        if (isRequesting)return;
        isRequesting = true;

//        String fid = (!isRefresh && mAdapter.getCount() != 0) ? mAdapter.getItem(mAdapter.getCount() - 1).fid : "";
//        ServerAPI.getFriendList(getContext(), "3", fid, mTopicId, new ServerCallBack<GetFriends>() {
//            @Override
//            public void onSucceed(Context context, GetFriends result) {
//                if(result!=null && result.userList != null && result.userList.size() > 0){
//                    if (isRefresh){
//                        mAdapter.refreshDatas(result.userList);
//                    }else {
//                        mAdapter.loadMoreDatas(result.userList);
//                    }
//                    isRequesting = false;
//                    closeLoadingPage();
//                }else {
//                    onError(context, ResultCode.NO_DATA.msg);
//                }
//            }
//            @Override
//            public void onError(Context context, String errorMsg) {
//                if (mAdapter.getCount() == 0){
//                    setLoadFailedMessage(errorMsg,R.drawable.empty_error_icon,false);
//                }
//                isRequesting = false;
//            }
//            @Override
//            public void onFinished(Context context) {
//                mPullToRefreshView.onRefreshComplete();
//            }
//        });
    }

    /**
     * 我要点赞
     */
    protected OnClickThrottleListener mOnClickThrottleListener = new OnClickThrottleListener(){
        @Override
        protected void onThrottleClick(View v) {

        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.fragment_thumbs_list;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        if (getArguments() != null){
            mTopicId = getArguments().getString(EXTRA_TID);
            mTideId = getArguments().getString(EXTRA_TIDE_ID);
        }
        mPullToRefreshView= (SmartRefreshLayout) mView.findViewById(R.id.srl_refreshView);
        mPullToRefreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getFriendList(true);
            }
        });
        mPullToRefreshView.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (isMore){
                    getFriendList(false);
                }else {
                    mPullToRefreshView.finishLoadmore();
                }
            }
        });
        ListView listView = (ListView) mView.findViewById(R.id.lv_listView);
        mFollowIv= (ImageView) mView.findViewById(R.id.iv_follow);
        mFollowIv.setImageResource(TextUtils.equals("0", mTideId) ? R.drawable.big_follow : R.drawable.big_followed);
        mAdapter=new GetFriendsAdapter();
        listView.setAdapter(mAdapter);
        if (!TextUtils.isEmpty(mTideId) && TextUtils.equals("0", mTideId)) {
            mFollowIv.setOnClickListener(mOnClickThrottleListener);
        }
    }
}
