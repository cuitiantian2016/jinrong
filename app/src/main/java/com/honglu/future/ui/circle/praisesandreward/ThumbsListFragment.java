package com.honglu.future.ui.circle.praisesandreward;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonNull;
import com.honglu.future.R;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.config.Constant;
import com.honglu.future.events.BBSFlownEvent;
import com.honglu.future.events.BBSPraiseEvent;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.http.RxHelper;
import com.honglu.future.ui.circle.bean.BBS;
import com.honglu.future.ui.circle.circlemain.OnClickThrottleListener;
import com.honglu.future.ui.trade.fragment.PagerFragment;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;


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
    private TextView mFollowIv;
    private boolean isMore;
    private int rows = 0;

    boolean mIsRefresh;
    private void getFriendList(final boolean isRefresh) {
        if (isRequesting)return;
        isRequesting = true;
        mIsRefresh = isRefresh;
        if (mIsRefresh){
            rows = 0;
        }
        HttpManager.getApi().getPraiseImageList(SpUtil.getString(Constant.CACHE_TAG_UID),mTopicId,rows).compose(RxHelper.<List<UserList>>handleSimplyResult())
                .subscribe(new HttpSubscriber<List<UserList>>() {
                    @Override
                    protected void _onNext(List<UserList> userLists) {
                        super._onNext(userLists);
                        isRequesting = false;
                        if (mIsRefresh){
                            mPullToRefreshView.finishRefresh();
                            mAdapter.refreshDatas(userLists);
                        }else {
                            mPullToRefreshView.finishLoadmore();
                            mAdapter.loadMoreDatas(userLists);
                        }
                        isMore = userLists.size() >= 10;
                        if (isMore){
                            ++rows;
                        }
                        mPullToRefreshView.setEnableLoadmore(isMore);
                    }

                    @Override
                    protected void _onError(String message) {
                        super._onError(message);
                        ToastUtil.show(message);
                        isRequesting = false;
                        mPullToRefreshView.finishLoadmore();
                        mPullToRefreshView.finishRefresh();
                    }
                });
    }

    /**
     * 我要点赞
     */
    protected OnClickThrottleListener mOnClickThrottleListener = new OnClickThrottleListener(){
        @Override
        protected void onThrottleClick(final View v) {
            String uid = SpUtil.getString(Constant.CACHE_TAG_UID);
            HttpManager.getApi().praise(uid,uid,true,mTopicId).compose(RxHelper.<JsonNull>handleSimplyResult()).subscribe(new HttpSubscriber<JsonNull>() {
                @Override
                protected void _onNext(JsonNull jsonNull) {
                    super._onNext(jsonNull);
                    mFollowIv.setText("您已点赞");
                    BBSPraiseEvent bbsPraiseEvent = new BBSPraiseEvent();
                    bbsPraiseEvent.topic_id = mTopicId;
                    EventBus.getDefault().post(bbsPraiseEvent);
                    getFriendList(true);
                }

                @Override
                protected void _onError(String message) {
                    super._onError(message);
                    ToastUtil.show(message);
                }
            });
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
        EventBus.getDefault().register(this);
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
        mFollowIv= (TextView) mView.findViewById(R.id.iv_follow);
        mFollowIv.setText(TextUtils.equals("0", mTideId) ? "我也要点赞" : "您已点赞");
        mAdapter=new GetFriendsAdapter();
        listView.setAdapter(mAdapter);
        if (!TextUtils.isEmpty(mTideId) && TextUtils.equals("0", mTideId)) {
            mFollowIv.setOnClickListener(mOnClickThrottleListener);
        }
        getFriendList(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 监听关注
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BBSFlownEvent event) {
        if(mAdapter!=null){
            mAdapter.follow(event.follow,event.uid);
        }
    }
}
