package com.honglu.future.ui.msg.mainmsg;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonNull;
import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.config.Constant;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.http.RxHelper;
import com.honglu.future.ui.msg.adapter.SystemMsgAdapter;
import com.honglu.future.ui.msg.bean.HasUnreadMsgBean;
import com.honglu.future.ui.msg.bean.SystemMsgBean;
import com.honglu.future.util.AndroidUtil;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 系统消息
 * Created by hefei on 2018/1/2
 */

public class SystemMsgActivity extends BaseActivity {
    @BindView(R.id.tv_right)
    TextView mTvRight;
    private View empty_view;
    private ListView listView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main_msg_system;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void loadData() {
        mTitle.setTitle(false, R.color.color_white, "系统通知");
        mTvRight.setText("清空");
        mTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearMsg();
            }
        });
        mPullToRefreshView = (SmartRefreshLayout) findViewById(R.id.srl_refreshView);
        mPullToRefreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getSystemMsg(true);
            }
        });
        mPullToRefreshView.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (isMore) {
                    getSystemMsg(false);
                } else {
                    mPullToRefreshView.finishLoadmore();
                }
            }
        });
        listView = (ListView) findViewById(R.id.lv_listView);
        mAdapter = new SystemMsgAdapter();
        listView.setAdapter(mAdapter);
        listView.setDividerHeight(AndroidUtil.dip2px(this, 10));
        empty_view = LayoutInflater.from(this).inflate(R.layout.live_empty, null);
        TextView textView = (TextView) empty_view.findViewById(R.id.empty_tv);
        textView.setText("暂无消息");
        getSystemMsg(true);
    }

    private SmartRefreshLayout mPullToRefreshView;
    private SystemMsgAdapter mAdapter;
    private boolean isMore;
    private int rows = 0;
    private boolean isRequesting;//是否正在请求中
    boolean mIsRefresh;

    public void getSystemMsg(final boolean isRefresh) {
        if (isRequesting) return;
        App.loadingContent(this, "加载中");
        isRequesting = true;
        mIsRefresh = isRefresh;
        if (mIsRefresh) {
            rows = 0;
        }
        //SpUtil.getString(Constant.CACHE_TAG_UID)
        HttpManager.getApi().getTradeMsgList(43 + "", 1, rows).compose(RxHelper.<List<SystemMsgBean>>handleSimplyResult())
                .subscribe(new HttpSubscriber<List<SystemMsgBean>>() {
                    @Override
                    protected void _onNext(List<SystemMsgBean> userLists) {
                        super._onNext(userLists);
                        App.hideLoading();
                        isRequesting = false;
                        if (mIsRefresh) {
                            mPullToRefreshView.finishRefresh();
                            mAdapter.refreshDatas(userLists);
                        } else {
                            mPullToRefreshView.finishLoadmore();
                            mAdapter.loadMoreDatas(userLists);
                        }
                        isMore = userLists.size() >= 10;
                        if (isMore) {
                            ++rows;
                        }
                        if (userLists != null && userLists.size() > 0) {
                            if (listView.getFooterViewsCount() != 0) {
                                listView.removeFooterView(empty_view);
                            }
                        } else {
                            //空布局
                            if (listView.getFooterViewsCount() == 0 && mAdapter.getCount() == 0) {
                                listView.addFooterView(empty_view, null, false);
                            }
                        }
                        mPullToRefreshView.setEnableLoadmore(isMore);
                    }

                    @Override
                    protected void _onError(String message) {
                        super._onError(message);
                        App.hideLoading();
                        isRequesting = false;
                        mPullToRefreshView.finishLoadmore();
                        mPullToRefreshView.finishRefresh();
                        if (mAdapter.getCount() > 0) {
                            if (listView.getFooterViewsCount() != 0) {
                                listView.removeFooterView(empty_view);
                            }
                        } else {
                            //空布局
                            if (listView.getFooterViewsCount() == 0 && mAdapter.getCount() == 0) {
                                listView.addFooterView(empty_view, null, false);
                            }
                        }
                    }
                });
    }

    private void clearMsg() {
        HttpManager.getApi().getMsgClear(SpUtil.getString(Constant.CACHE_TAG_UID), 1).compose(RxHelper.<JsonNull>handleSimplyResult()).subscribe(new HttpSubscriber<JsonNull>() {
            @Override
            protected void _onNext(JsonNull jsonNull) {
                super._onNext(jsonNull);
                if (mAdapter.getCount() > 0) {
                    mAdapter.clearDatas();
                    //空布局
                    if (listView.getFooterViewsCount() == 0) {
                        listView.addFooterView(empty_view, null, false);
                    }
                }
            }

            @Override
            protected void _onError(String message) {
                super._onError(message);
                ToastUtil.show(message);
            }
        });
    }

}
