package com.honglu.future.ui.live;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.config.Constant;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.http.RxHelper;
import com.honglu.future.ui.live.bean.LiveListBean;
import com.honglu.future.util.AndroidUtil;
import com.honglu.future.util.SpUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;


/**
 * deprecation:任务
 * author:ayb
 * time:2017/6/14
 * <p>
 * "xiaoniuqihuo://future/future/live"
 */
@Route(path = "/future/live")
public class LiveActivity extends BaseActivity {

    @BindView(R.id.srl_refreshView)
    SmartRefreshLayout srl_refreshView;
    @BindView(R.id.lv_listView)
    ListView lv_listView;
    private LiveAdapter liveAdapter;
    private View empty_view;
    private boolean isLoading;

    @Override
    public int getLayoutId() {
        return R.layout.activity_live;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void loadData() {
        initViews();
    }

    private void initViews() {
        mTitle.setTitle(false, R.color.white, "视频直播");
        srl_refreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refresh();
            }
        });
        empty_view = LayoutInflater.from(this).inflate(R.layout.live_empty, null);
        liveAdapter = new LiveAdapter(lv_listView);
        lv_listView.setAdapter(liveAdapter);
        lv_listView.setDividerHeight(AndroidUtil.dip2px(this, 10));
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        if (isLoading) {
            return;
        }
        App.loadingContent(this, "正在加载");
        isLoading = true;
        HttpManager.getApi().getLiveData(SpUtil.getString(Constant.CACHE_TAG_UID)).compose(RxHelper.<List<LiveListBean>>handleSimplyResult()).subscribe(new HttpSubscriber<List<LiveListBean>>() {
            @Override
            protected void _onNext(List<LiveListBean> liveListBean) {
                super._onNext(liveListBean);
                isLoading = false;
                if (srl_refreshView == null) {
                    return;
                }
                App.hideLoading();
                srl_refreshView.finishRefresh();
                liveAdapter.setDatas(liveListBean);
                if (liveListBean != null && liveListBean.size() > 0) {
                    if (lv_listView.getFooterViewsCount() != 0) {
                        lv_listView.removeFooterView(empty_view);
                    }
                } else {
                    //空布局
                    if (lv_listView.getFooterViewsCount() == 0 && liveAdapter.getCount() == 0) {
                        lv_listView.addFooterView(empty_view, null, false);
                    }
                }
            }

            @Override
            protected void _onError(String message) {
                super._onError(message);
                isLoading = false;
                if (srl_refreshView == null) {
                    return;
                }
                App.hideLoading();
                srl_refreshView.finishRefresh();
                if (liveAdapter.getCount() > 0) {
                    if (lv_listView.getFooterViewsCount() != 0) {
                        lv_listView.removeFooterView(empty_view);
                    }
                } else {
                    //空布局
                    if (lv_listView.getFooterViewsCount() == 0 && liveAdapter.getCount() == 0) {
                        lv_listView.addFooterView(empty_view, null, false);
                    }
                }
            }
        });
    }

}
