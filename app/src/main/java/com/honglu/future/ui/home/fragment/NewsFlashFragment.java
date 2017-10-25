package com.honglu.future.ui.home.fragment;

import android.widget.LinearLayout;

import com.honglu.future.R;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.base.BasePresenter;
import com.honglu.future.events.HomeNotifyRefreshEvent;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.home.HomeTabViewUtil.FastMsgViewUtils;
import com.honglu.future.ui.home.bean.NewsFlashData;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

/**
 * deprecation:24小时快讯页面
 * author:ayb
 * time:2017/8/24
 */
public class NewsFlashFragment extends BaseFragment {
    @BindView(R.id.news_flash)
    LinearLayout linearLayout;
    private int page = 1;
    private BasePresenter<NewsFlashFragment> mBasePresenter;
    private List<NewsFlashData.NewsFlashDataX.NewsFlashBean> list = new ArrayList<>();

    private boolean isMore;
    private void getNewsData(){
        if (mBasePresenter == null){
            mBasePresenter = new BasePresenter<NewsFlashFragment>(this) {
                @Override
                public void getData() {
                    super.getData();
                    toSubscribe(HttpManager.getApi().geFlashNewData(page), new HttpSubscriber<NewsFlashData>() {
                        @Override
                        protected void _onNext(NewsFlashData newsFlashData) {
                            super._onNext(newsFlashData);
                            if (newsFlashData != null && newsFlashData.data != null && newsFlashData.data.data != null) {
                                if (page == 1){
                                   EventBus.getDefault().post(new HomeNotifyRefreshEvent(HomeNotifyRefreshEvent.TYPE_REFRESH_FINISH));
                                    list.clear();
                                    list = newsFlashData.data.data;
                                }else {
                                    EventBus.getDefault().post(new HomeNotifyRefreshEvent(HomeNotifyRefreshEvent.TYPE_LOAD_MORE_FINISH));
                                    list.addAll(newsFlashData.data.data);
                                }
                                if (list.size()>9){//当集合的长度大于10的时候表示有下一页
                                    isMore = true;
                                    page++;
                                }else {//没有下一页
                                    page = 1;
                                    isMore = false;
                                }
                                if (list.size()>9){
                                    page++;
                                    isMore = true;
                                }else {
                                    isMore = false;
                                }
                                FastMsgViewUtils.refreashDatas(list, linearLayout);
                            }else {
                                page = 1;
                                isMore = false;
                                if (linearLayout !=null){
                                    linearLayout.removeAllViews();
                                }
                            }
                        }
                        @Override
                        protected void _onError(String message) {
                            super._onError(message);
                            list.clear();
                            FastMsgViewUtils.refreashDatas(list, linearLayout);
                        }
                    });
                }
            };
        }
        mBasePresenter.getData();
    }
    /**
     * 下拉刷新
     */
    public void refresh(){
        page = 1;
        getNewsData();
    }
    /**
     * 加载更多
     */
    public void loadMore(){
        if (isMore){
            getNewsData();
        }else {
          EventBus.getDefault().post(new HomeNotifyRefreshEvent(HomeNotifyRefreshEvent.TYPE_LOAD_MORE_FINISH));
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_news_falsh;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        getNewsData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBasePresenter.onDestroy();
    }
}
