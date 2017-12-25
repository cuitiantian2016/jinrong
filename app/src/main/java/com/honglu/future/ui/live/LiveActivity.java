package com.honglu.future.ui.live;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.ui.main.contract.AccountContract;
import com.honglu.future.util.AndroidUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

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
                srl_refreshView.finishRefresh();
            }
        });
        LiveAdapter liveAdapter = new LiveAdapter(lv_listView);
        ArrayList<LiveListBean> liveListBeen = new ArrayList<>();
        lv_listView.setAdapter(liveAdapter);
        lv_listView.setDividerHeight(AndroidUtil.dip2px(this,10));
        for (int i =0;i<10;i++){
            LiveListBean liveListBean = new LiveListBean();
            liveListBean.follow = "1";
            if (i ==1){
                liveListBean.isLive = true;
            }
            liveListBean.liveDes = "稳健 独创反转战法";
            liveListBean.liveTeacher = "何老师";
            liveListBean.liveImg = "htttp";
            liveListBean.liveNum = "20";
            liveListBean.liveTeacherDes="冯老师善于把握市场大方向，稳健型操作。有着一套完整的操盘理念与操盘技术，曾任职某机构操盘手，操盘资金数亿元。操盘理念：大海航行靠舵手，期海淘金靠趋势。";
            liveListBean.liveTeacherICon = "htt";
            liveListBean.liveTime = "2017203823-3293";
            liveListBean.liveTeacherID = "4";
            liveListBean.liveTitle = "小牛午餐";
            liveListBeen.add(liveListBean);
        }
        liveAdapter.setDatas(liveListBeen);
    }

}
