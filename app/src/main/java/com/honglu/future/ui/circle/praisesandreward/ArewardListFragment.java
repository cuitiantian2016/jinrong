package com.honglu.future.ui.circle.praisesandreward;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.JsonNull;
import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.config.Constant;
import com.honglu.future.events.BBSFlownEvent;
import com.honglu.future.ui.circle.bean.ArewardListBean;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 打赏列表
 * Created by zhuaibing on 2017/12/20
 */

public class ArewardListFragment extends BaseFragment<ArewardAndSupportPresenter> implements ArewardAndSupportContract.View{
    public static final String CIRCLEID_KEY = "circleId";

    @BindView(R.id.refresh_view)
    SmartRefreshLayout mRefreshView;
    @BindView(R.id.lv_listView)
    ListView mListView;
    @BindView(R.id.ll_empty)
    LinearLayout mEmpty;

    private ArewardListAdapter mAdapter;
    private String mCircleId;//帖子id
    private int mRows = 0;

    @Override
    public void showLoading(String content) {
        if (!TextUtils.isEmpty(content)) {
            App.loadingContent(getActivity(), content);
        }
    }

    @Override
    public void stopLoading() {
        App.hideLoading();
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        if (!TextUtils.isEmpty(msg)) {
            ToastUtil.show(msg);
        }
    }


    @Override
    public void initPresenter() {
      mPresenter.init(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_areward_list;
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    //关注
    public void focusHttp(String userId,String attentionState){
        mPresenter.focus(userId,SpUtil.getString(Constant.CACHE_TAG_UID),attentionState);
    }

    @Override
    public void loadData() {
        EventBus.getDefault().register(this);
        mCircleId = getArguments().getString(CIRCLEID_KEY,"");
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_f8f8f9_10dp_head,null);
        mRefreshView.setEnableLoadmore(false);
        mAdapter = new ArewardListAdapter(ArewardListFragment.this);
        mListView.setEmptyView(mEmpty);
        mListView.addHeaderView(headView);
        mListView.setAdapter(mAdapter);

        mRefreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mRows = 0;
                mPresenter.getArewardList(SpUtil.getString(Constant.CACHE_TAG_UID),mCircleId,mRows);
                mRefreshView.finishRefresh();
            }
        });

        mRefreshView.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mRows ++;
                mPresenter.getArewardList(SpUtil.getString(Constant.CACHE_TAG_UID),mCircleId,mRows);
                mRefreshView.finishLoadmore();
            }
        });
        mPresenter.getArewardList(SpUtil.getString(Constant.CACHE_TAG_UID),mCircleId,mRows);
    }


    @Override
    public void getArewardList(List<ArewardListBean> list) {
        if (list == null){return;}

        if (list.size() >= 10){
            if (!mRefreshView.isEnableLoadmore()){
                mRefreshView.setEnableLoadmore(true);
            }
        }else {
            if (mRefreshView.isEnableLoadmore()){
                mRefreshView.setEnableLoadmore(false);
            }
        }

        if (mRows > 0){
            mAdapter.notifyDataChanged(true,list);
        }else {
            mAdapter.notifyDataChanged(false,list);
        }
    }

    //关注
    @Override
    public void focus(String userId, final String attentionState) {
        BBSFlownEvent bbsFlownEvent = new BBSFlownEvent();
        bbsFlownEvent.follow = attentionState;
        bbsFlownEvent.uid = userId;
        EventBus.getDefault().post(bbsFlownEvent);
    }

    /**
     * 监听关注
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BBSFlownEvent event) {
        if(mAdapter!=null && mAdapter.getList() !=null && mAdapter.getList().size() > 0){
            boolean mAttention = false;
           for (ArewardListBean bean : mAdapter.getList()){
               if (TextUtils.equals(bean.userId,event.uid)){
                   bean.focus = TextUtils.equals("0",event.follow) ? false : true;
                   mAttention = true;
               }
           }
           if (mAttention){
               mAdapter.notifyDataSetChanged();
           }
        }
    }
}
