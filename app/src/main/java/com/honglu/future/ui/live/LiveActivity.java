package com.honglu.future.ui.live;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.ui.main.contract.AccountContract;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;


/**
 * deprecation:任务
 * author:ayb
 * time:2017/6/14
 */
public class LiveActivity extends BaseActivity {

    @BindView(R.id.srl_refreshView)
    SmartRefreshLayout srl_refreshView;
    @BindView(R.id.lv_listView)
    ListView lv_listView;
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

            }
        });
        LiveAdapter liveAdapter = new LiveAdapter();
        lv_listView.setAdapter(liveAdapter);
    }

}
