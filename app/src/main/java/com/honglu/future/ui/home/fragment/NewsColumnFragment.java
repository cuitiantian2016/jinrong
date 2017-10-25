package com.honglu.future.ui.home.fragment;

import android.widget.LinearLayout;


import com.honglu.future.R;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.base.BasePresenter;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.home.HomeTabViewUtil.NewsCloumnViewUtils;
import com.honglu.future.ui.home.bean.HomeMessageItem;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

/**
 * deprecation:海豚专栏
 * author:hefei
 * time:2017/8/24
 */
public class NewsColumnFragment extends BaseFragment {
    @BindView(R.id.ll_news_column)
    LinearLayout mLinearLayout;
    private List<HomeMessageItem.DataBeanX.DataBean> mListData;
    private BasePresenter<NewsColumnFragment> mBasePresenter;
    /**
     * 刷新view
     * @param data
     */
    private void setDataToView(HomeMessageItem data){
        if (data == null || data.getData() == null || data.getData().getData() == null){
            return;
        }
        mListData = data.getData().getData();
        NewsCloumnViewUtils.refreshEconomicViews(mLinearLayout,mListData);
    }
    /**
     * 错误信息处理
     */
    private void err(){
        if (mListData ==null){
            mListData = new ArrayList<>();
        }
        mListData.clear();
        NewsCloumnViewUtils.refreshEconomicViews(mLinearLayout,mListData);
    }
    /**
     * 从网上获取数据
     */
    public void refresh(){
        if (mBasePresenter == null){
            mBasePresenter = new BasePresenter<NewsColumnFragment>(this) {
                @Override
                public void getData() {
                    super.getData();
                    toSubscribe(HttpManager.getApi().getNewsColumnData(), new HttpSubscriber<HomeMessageItem>() {
                        @Override
                        protected void _onNext(HomeMessageItem o) {
                            super._onNext(o);
                            mView.setDataToView(o);
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            mView.err();
                        }
                    });
                }
            };
        }
        mBasePresenter.getData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBasePresenter.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return  R.layout.fragment_news_column;
    }
    @Override
    public void initPresenter() {
    }
    @Override
    public void loadData() {
        refresh();
    }
    public static class CommentData{
        public int position;//点赞的位置
        public boolean isPraise;//是否点赞
    }
    /**
     * 点赞之后刷新布局
     */
    public void praise(CommentData commentData){
        if (mListData!=null&&mListData.size()>0){
            HomeMessageItem.DataBeanX.DataBean item = mListData.get(commentData.position);
            if (commentData.isPraise){
                item.setPraiseCounts(item.getPraiseCounts()+1);
                item.isPraise = 1;
            }else {
                item.isPraise = 0;
                item.setPraiseCounts(item.getPraiseCounts()-1);
            }
            NewsCloumnViewUtils.refreshEconomicViews(mLinearLayout,mListData);
        }
    }
    /**
     * 评论之后刷新布局
     */
    public void comment(CommentData commentData){
        if (mListData!=null&&mListData.size()>0){
            HomeMessageItem.DataBeanX.DataBean item = mListData.get(commentData.position);
            if (commentData.isPraise){
                item.setCommentNum(item.getCommentNum()+1);
            }
            NewsCloumnViewUtils.refreshEconomicViews(mLinearLayout,mListData);
        }
    }
}
