package com.honglu.future.ui.circle.circlemain;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.base.BasePresenter;
import com.honglu.future.config.Constant;
import com.honglu.future.events.BBSCommentContentEvent;
import com.honglu.future.events.BBSCommentEvent;
import com.honglu.future.events.BBSFlownEvent;
import com.honglu.future.events.BBSIndicatorEvent;
import com.honglu.future.events.BBSPraiseEvent;
import com.honglu.future.events.PublishEvent;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.circle.bean.BBS;
import com.honglu.future.ui.circle.circlemain.adapter.BBSAdapter;
import com.honglu.future.util.ImageUtil;
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

public class BBSClassifyFragment extends BaseFragment {

    public static final String EXTRA_CLASSIFY_TYPE = "filter_type";
    private View empty_view;
    private View mNewMsgLy;
    private TextView mNewMessageTV;
    private ImageView mNewMsgFromPortraitIV;
    private ListView mListView;
    private SmartRefreshLayout srl_refreshView;
    private BBSAdapter mAdapter;
    private boolean isLoadingNow = false;
    private String topicType = "1";
    int rows;
    private boolean isMore;

    /**
     * 评论
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BBSCommentEvent event) {
        if(mAdapter!=null){
            List<BBS> list = mAdapter.getList();
            if (list != null) {
                for (BBS bbs : list) {
                    if (TextUtils.equals(bbs.topic_id,event.topic_id)) {
                        bbs.reply_num = event.commentNum;
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        }
    }
    /**
     * 评论
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(PublishEvent event) {
        if(mAdapter!=null){
          if (event.type.equals(topicType)){
              mListView.setSelection(0);
              refreshData();
          }
        }
    }
    /**
     * 点赞的事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BBSPraiseEvent event) {
        if(mAdapter!=null){
            List<BBS> list = mAdapter.getList();
            if (list != null) {
                for (BBS bbs : list) {
                    if (TextUtils.equals(bbs.topic_id,event.topic_id)) {
                        bbs.support_num = event.praiseNum;
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        }
    }
    /**
     * 监听关注
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BBSFlownEvent event) {
        if(mAdapter!=null){
            List<BBS> list = mAdapter.getList();
            if (list != null) {
                for (BBS bbs : list) {
                    if (TextUtils.equals(bbs.uid,event.uid)) {
                        bbs.follow = event.follow;
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

        }
    }
    /**
     * 评论消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BBSCommentContentEvent event) {
        if(mAdapter!=null){
            List<BBS> list = mAdapter.getList();
            if (list != null) {
                for (BBS bbs : list) {
                    if (TextUtils.equals(bbs.topic_id,event.top_id)) {
                        bbs.replyContent = event.replyContent;
                        bbs.replyNickName = event.replyNickName;
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

        }
    }

    private void refreshData() {
        if (!isLoadingNow) {
            rows=0;
            topicIndexThread(true);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BBSIndicatorEvent event) {
//        srl_refreshView.setEnableRefresh(false);
        if (event.getTopicType().equals(topicType)) {
            if (event.isBackTop()) {
                mListView.setSelection(0);
            }
            refreshData();
        }
    }

    // 滑动加载更多
    public BBSAdapter.ScrollToLastCallBack getLoadMoreListener() {
        return new BBSAdapter.ScrollToLastCallBack() {
            @Override
            public void onScrollToLast(Integer pos) {

            }
        };
    }

    private View.OnClickListener getReaderMessageListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNewMsgLy.setVisibility(View.GONE);
//                Intent intent = new Intent(getActivity(), BBSMessageActivity.class);
//                intent.putExtra(BBSMessageActivity.EXTRA_MESSAGE_POSITION, messageTabPosition);
//                startActivity(intent);
            }
        };
    }
    private boolean mIsRefresh;
    BasePresenter<BBSClassifyFragment> basePresenter;
    private void topicIndexThread( boolean isRefresh) {
        isLoadingNow = true;
        mIsRefresh = isRefresh;
        if (basePresenter ==null){
            basePresenter = new BasePresenter<BBSClassifyFragment>(BBSClassifyFragment.this) {
                @Override
                public void getData() {
                    super.getData();
                    toSubscribe(HttpManager.getApi().getCircleType(SpUtil.getString(Constant.CACHE_TAG_UID), topicType, rows), new HttpSubscriber<List<BBS>>() {
                        @Override
                        protected void _onNext(List<BBS> o) {
                            super._onNext(o);
                            if (mIsRefresh) {
                                mAdapter.clearDatas();
                                mAdapter.setDatas(o);
                                srl_refreshView.finishRefresh();
                                if (o != null && o.size() > 0) {
                                    if (mListView.getFooterViewsCount() != 0){
                                        mListView.removeFooterView(empty_view);
                                    }
                                } else {
                                    //空布局
                                    if (mListView.getFooterViewsCount() == 0 && mAdapter.getCount() == 0) {
                                        mListView.addFooterView(empty_view, null, false);
                                    }
                                }
                            } else {
                                mAdapter.setDatas(o);
                                srl_refreshView.finishLoadmore();
                            }
                            if (o.size() >= 10) {
                                ++rows;
                                isMore = true;
                            } else {
                                isMore = false;
                            }
                            //srl_refreshView.setEnableRefresh(true);
                            srl_refreshView.setEnableLoadmore(isMore);
                            isLoadingNow = false;
                        }

                        @Override
                        protected void _onError(String message) {
                            super._onError(message);
                            ToastUtil.show(message);
                            isLoadingNow = false;
                            //srl_refreshView.setEnableRefresh(true);
                            srl_refreshView.finishRefresh();
                            srl_refreshView.finishLoadmore();
                        }
                    });
                }
            };
        }
        basePresenter.getData();

    }

    private void performNewMessage(String newMessage, String fromPortrait) {
        if (!TextUtils.isEmpty(newMessage)) {
            mNewMsgLy.setVisibility(View.VISIBLE);
            mNewMessageTV.setText(newMessage);
            ImageUtil.display(fromPortrait, mNewMsgFromPortraitIV, R.mipmap.img_head);
        } else {
            mNewMsgLy.setVisibility(View.GONE);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bbs_newest;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        EventBus.getDefault().register(this);
        topicType = getArguments().getString(EXTRA_CLASSIFY_TYPE);
        srl_refreshView = (SmartRefreshLayout) mView.findViewById(R.id.srl_refreshView);

        mListView = (ListView) mView.findViewById(R.id.lv_listView);
        empty_view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_bbs_empty, null);
        TextView empty_text = (TextView) empty_view.findViewById(R.id.empty_tv);
        empty_text.setText("还没有最新的消息哦~");
        mAdapter = new BBSAdapter(mListView, getContext(), getLoadMoreListener());
        mAdapter.setTopicType(topicType);
        mAdapter.setToRefreshListViewListener(new BBSAdapter.ToRefreshListViewListener() {
            @Override
            public void refresh() {
                //isLoadingFinished = false;
                //  topicIndexThread("pull_down", topicType, false);
            }
        });
        mListView.setAdapter(mAdapter);
        mNewMsgLy = mView.findViewById(R.id.message_new_ly);
        mNewMsgLy.setOnClickListener(getReaderMessageListener());
        mNewMessageTV = (TextView) mView.findViewById(R.id.tv_new_message);
        mNewMsgFromPortraitIV = (ImageView) mView.findViewById(R.id.iv_from_portrait);
        srl_refreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshData();
            }
        });
        srl_refreshView.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (!isLoadingNow&&isMore) {//上拉加载更多
                    topicIndexThread(false);
                }else {
                    srl_refreshView.finishLoadmore();
                }
            }
        });
        refreshData();
    }
}
