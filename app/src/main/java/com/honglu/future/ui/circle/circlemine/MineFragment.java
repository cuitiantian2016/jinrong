package com.honglu.future.ui.circle.circlemine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.base.CommonFragment;
import com.honglu.future.ui.circle.bean.AttutudeUser;
import com.honglu.future.ui.circle.bean.BBS;
import com.honglu.future.ui.circle.circledetail.CircleDetailActivity;
import com.honglu.future.ui.circle.circlemain.adapter.BBSAdapter;
import com.honglu.future.ui.circle.publish.PublishActivity;
import com.honglu.future.ui.circlefriend.MyFriendActivity;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.widget.CircleImageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;


public class MineFragment extends CommonFragment {
    @BindView(R.id.smart_refresh)
    SmartRefreshLayout mSmartRefresh;
    @BindView(R.id.listView)
    ListView mListView;
    private BBSAdapter mAdapter;
    private boolean isLoadingNow = false, isLoadingFinished = false;
    private String fid = "0";
    private TextView publish;
    private LinearLayout layout_friends;
    private View empty_view;

    private View header_view;
    private ImageView header_img;
    private TextView flag, user_name, attention_num, endorse_num, topic_num, reward_num;

    private LinearLayout mAttutudeUserLy;
    private OnTopicAlaph mOnTopicAlaph;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_bbs_mine;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        initViews();
        isLoadingFinished = false;
        topicIndexThread("pull_down");
    }

    private void initViews() {
        header_view = LayoutInflater.from(mContext).inflate(R.layout.minefragment_header, null);
        header_img = (CircleImageView) header_view.findViewById(R.id.header_img);
        flag = (TextView) header_view.findViewById(R.id.flag);
        user_name = (TextView) header_view.findViewById(R.id.user_name);
        topic_num = (TextView) header_view.findViewById(R.id.topic_num);
        endorse_num = (TextView) header_view.findViewById(R.id.endorse_num);
        attention_num = (TextView) header_view.findViewById(R.id.attention_num);
        reward_num = (TextView) header_view.findViewById(R.id.reward_num);


        mSmartRefresh.setEnableRefresh(true);
        mSmartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (!isLoadingNow) {
                    isLoadingFinished = false;
                    topicIndexThread("pull_down");
                }
            }
        });
        layout_friends = (LinearLayout) header_view.findViewById(R.id.layout_friends);
        layout_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, MyFriendActivity.class));
            }
        });
        mAttutudeUserLy = (LinearLayout) header_view.findViewById(R.id.ly_likes_user);
        //
        empty_view = LayoutInflater.from(mContext).inflate(R.layout.fragment_bbs_empty_me, null);
        publish = (TextView) empty_view.findViewById(R.id.publish);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DeviceUtils.isFastDoubleClick()) {
                    return;
                }
                mContext.startActivity(new Intent(mContext, PublishActivity.class));
            }
        });
        mAdapter = new BBSAdapter(mListView, mContext, scrollToLastCallBack);
        mListView.addHeaderView(header_view);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(listener);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView listView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    View view = listView.getChildAt(0);
                    if (view != null) {
                        int top = -view.getTop();
                        int headerHeight = view.getHeight();
                        if (top <= headerHeight && top >= 0) {
                            float f = (float) top / (float) headerHeight;
                            if (mOnTopicAlaph != null) {
                                mOnTopicAlaph.onAlaphValue(f);
                            }
                        }
                    }
                } else if (firstVisibleItem > 0) {
                    if (mOnTopicAlaph != null) {
                        mOnTopicAlaph.onAlaphValue(1);
                    }

                } else {
                    if (mOnTopicAlaph != null) {
                        mOnTopicAlaph.onAlaphValue(0);
                    }
                }
                boolean result = false;
//                if (mListView.getFirstVisiblePosition() == 0) {
//                    final View topChildView = mListView.getChildAt(0);
//                    result = topChildView.getTop() == 0;
//                }
                if (result) {
                    mSmartRefresh.setEnableRefresh(true);
                } else {
                    mSmartRefresh.setEnableRefresh(false);
                }
            }
        });
    }

    //条目点击事件侦听
    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (DeviceUtils.isFastDoubleClick()) {
                return;
            }
            BBS item = (BBS) parent.getItemAtPosition(position);
            if (item != null) {
                Intent intent = new Intent(view.getContext(), CircleDetailActivity.class);
                intent.putExtra("bbs_item", item);
                startActivity(intent);
            }
        }
    };

    //滑动加载更多
    BBSAdapter.ScrollToLastCallBack scrollToLastCallBack = new BBSAdapter.ScrollToLastCallBack() {
        @Override
        public void onScrollToLast(Integer pos) {
            if (!isLoadingNow) {
                topicIndexThread("pull_up");
            }
        }
    };

    private void topicIndexThread(final String pull_style) {
        if (isLoadingNow || isLoadingFinished) {
            return;
        }

        isLoadingNow = true;


        //判断是下拉刷新还是加载更多
        final boolean isRefresh = TextUtils.equals("pull_down", pull_style);
        //根据情况设置当前的topic_id请求参数
        String topic_id = isRefresh ? "0" : mAdapter.getItem(mAdapter.getCount() - 1).topic_id;

//        ServerAPI.topicIndex(mContext, type, topic_id, fid, null, new ServerCallBack<TopicList>() {
//                    @Override
//                    public void onSucceed(Context context, TopicList result) {
//                        if (result.friends != null && result.friends.size() != 0) {
//                            updateAttutudeUser(result.friends);
//                        }
//                        if (result.user_info != null) {
//                            ImageUtil.display(result.user_info.headimgurl, header_img, R.drawable.iv_no_image);
//                            user_name.setText(result.user_info.user_name);
//                            if (result.user_info.user_flag.equals("1")) {
//                                flag.setVisibility(View.VISIBLE);
//                            } else {
//                                flag.setVisibility(View.INVISIBLE);
//                            }
//                            attention_num.setText("关注" + result.user_info.follow_num);
//                            endorse_num.setText("粉丝" + result.user_info.fans_num);
//                            topic_num.setText("发帖" + result.user_info.topic_num);
//                            reward_num.setText("获赏" + (TextUtils.isEmpty(result.user_info.integral)?"":result.user_info.integral));
//                        }
//
//                        if (result.topicList != null && result.topicList.size() > 0) {
//                            if (mListView.getFooterViewsCount() != 0)
//                                mListView.removeFooterView(empty_view);
//
//                            if (isRefresh) {
//                                mAdapter.clearDatas();
//                                mAdapter.setDatas(result.topicList);
//                            } else {
//                                mAdapter.setDatas(result.topicList);
//                            }
//
//                        } else {
//                            //空布局
//                            if (mListView.getFooterViewsCount() == 0 && mAdapter.getCount() == 0) {
//                                mListView.addFooterView(empty_view, null, false);
//                            }
//                        }
//
//                        closeLoadingPage();
//                    }
//
//                    @Override
//                    public void onError(Context context, String errorMsg) {
//                        Toaster.toast(errorMsg);
//                    }
//
//                    @Override
//                    public void onFinished(Context context) {
//                        isLoadingNow = false;
//                        mPullToRefreshListView.onRefreshComplete();
//                    }
//                }
//
//        );
    }

    @Override
    protected void onReload(Context context) {
        super.onReload(context);
        isLoadingFinished = false;
        topicIndexThread("pull_down");
    }

    private void updateAttutudeUser(List<AttutudeUser> attutudeUserList) {
        mAttutudeUserLy.removeAllViews();
        if (attutudeUserList != null) {
            for (AttutudeUser attutudeUser : attutudeUserList) {
                CircleImageView headIV = new CircleImageView(getActivity());
                int size = getResources().getDimensionPixelSize(R.dimen.dimen_36dp);
                mAttutudeUserLy.addView(headIV, new LinearLayout.LayoutParams(size, size));
                ImageUtil.display(attutudeUser.headimgurl, headIV, R.mipmap.ic_logos);
                if (mAttutudeUserLy.getChildCount() >= 4)
                    break;
            }
        }
    }

    public interface OnTopicAlaph {
        void onAlaphValue(float value);
    }

    public void setOnTopicAlaph(OnTopicAlaph onTopicAlaph) {
        this.mOnTopicAlaph = onTopicAlaph;
    }
}
