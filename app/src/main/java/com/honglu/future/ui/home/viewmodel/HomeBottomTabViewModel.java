package com.honglu.future.ui.home.viewmodel;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.honglu.future.R;
import com.honglu.future.ui.home.fragment.NewsColumnFragment;
import com.honglu.future.ui.home.fragment.NewsFlashFragment;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.widget.tab.CommonTabLayout;
import com.honglu.future.widget.tab.CustomTabEntity;
import com.honglu.future.widget.tab.SimpleOnTabSelectListener;
import com.honglu.future.widget.tab.TabEntity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * Created by hefei on 2017/9/4.
 *
 *  首页下面的tab新闻切换的model
 */

public class HomeBottomTabViewModel {

    private CommonTabLayout mCommonTabLayout;
//    private ArrayList<Fragment> mFragments ;
    private int currentPosition;
//    private NewsColumnFragment newsColumnFragment;
//    private NewsFlashFragment newsFlashFragment;
    private SmartRefreshLayout mSmartRefreshLayout;
    private Context mContext;
    public final View mView;
    private NewsColumnFragment newsColumnFragment;


    public HomeBottomTabViewModel(Context context, SmartRefreshLayout smartRefreshLayout) {
        mContext = context;
        mSmartRefreshLayout = smartRefreshLayout;
        mView = View.inflate(context, R.layout.view_home_tab, null);
        initView(mView);
    }

    public void initView(View view) {
//        mCommonTabLayout = (CommonTabLayout) view.findViewById(R.id.home_common_tab_layout);
        //int screenWidthDip = DeviceUtils.px2dip(mContext, DeviceUtils.getScreenWidth(mContext));
         //mCommonTabLayout.setIndicatorWidth((int) (screenWidthDip*0.2f));
//        ArrayList<CustomTabEntity> entities = new ArrayList<>();
//        entities.add(new TabEntity(mContext.getString(R.string.news_column)));
        //entities.add(new TabEntity(mContext.getString(R.string.news_flash)));
//        if (mFragments == null){
//            mFragments = new ArrayList<>();
//        }
//        newsColumnFragment =//海豚专栏
//        mFragments.add(newsColumnFragment);
//        newsFlashFragment = new NewsFlashFragment();//24小时
//        mFragments.add(newsFlashFragment);
//        mCommonTabLayout.setTabData(entities,(FragmentActivity) mContext,R.id.fragment_container_id,mFragments);
//        mCommonTabLayout.setOnTabSelectListener(new SimpleOnTabSelectListener(){
//            @Override
//            public void onTabSelect(int position) {
//                super.onTabSelect(position);
//                mSmartRefreshLayout.finishLoadmore();//每次切换的时候不显示上一个刷新
//                mSmartRefreshLayout.finishRefresh();
//                currentPosition = position;
//                if (currentPosition == 0){
//                    mSmartRefreshLayout.setEnableLoadmore(false);
//                }else {
//                    MobclickAgent.onEvent(mContext,"shouye_24hkuaixun_click","首页_24h快讯");
//                    mSmartRefreshLayout.setEnableLoadmore(true);
//                }
//                refreshData();
//            }
//        });
        newsColumnFragment = new NewsColumnFragment();
      mSmartRefreshLayout.setEnableLoadmore(false);
        ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_id,newsColumnFragment).commit();
    }
    /**
     * 下拉刷新
     */
    public void refreshData(){
        newsColumnFragment.refresh();
//      if (currentPosition == 0){
//
//        }else if (currentPosition == 1){
////            newsFlashFragment.refresh();
//        }
    }
    /**
     * 上拉加载更多
     */
    public void loadMore(){
//        if (currentPosition == 1){
////           newsFlashFragment.loadMore();
//        }
    }
}
