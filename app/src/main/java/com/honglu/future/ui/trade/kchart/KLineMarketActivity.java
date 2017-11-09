package com.honglu.future.ui.trade.kchart;

import android.support.v4.app.Fragment;
import android.view.View;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.config.Constant;
import com.honglu.future.ui.trade.adapter.KChartFragmentAdapter;
import com.honglu.future.ui.trade.bean.RealTimeBean;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.widget.kchart.SlidingTabLayout;
import com.honglu.future.widget.kchart.ViewPagerEx;
import com.honglu.future.widget.kchart.fragment.KLineFragment;
import com.honglu.future.widget.kchart.fragment.KMinuteFragment;
import com.honglu.future.widget.tab.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zq on 2017/11/7.
 */

public class KLineMarketActivity extends BaseActivity<KLineMarketPresenter> implements KLineMarketContract.View {
    @BindView(R.id.tablayout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPagerEx mViewPager;
    private String mExcode;
    private String mCode;
    private String mClosed;

    private String[] mTitles = {"分时", "1分钟", "5分钟", "15分钟", "30分钟", "1小时", "4小时", "日线", "周线"};

    private KLineFragment mKLineFragment;

    @Override
    public int getLayoutId() {
        return R.layout.activity_k_line_market;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        mExcode = getIntent().getStringExtra("excode");
        mCode = getIntent().getStringExtra("code");
        mClosed = getIntent().getStringExtra("close");
        initViewPager();
        initListener();
        mPresenter.getProductRealTime(mExcode + "%" + mCode);
    }

    private void initListener() {

        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                //setUmengKLine(position);
            }

            @Override
            public void onTabReselect(int position) {

            }

            @Override
            public void onTabUnselected(int position) {

            }
        });
//        //选中K线监听
//        chooseKLineListener();
//
//        //选中分时线监听
//        chooseMinuteListener();
    }

    private void initViewPager() {
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < mTitles.length; i++) {
            if (i == 0) {
                KMinuteFragment fragment = new KMinuteFragment();
                fragment.setExcode(mExcode);
                fragment.setCode(mCode);
                fragment.setClosed(mClosed);
                fragment.setTimeStr(Constant.CLOSE_TIME_AG);
//                fragment.setTouchEnabled(false);
//                fragment.setShowHighLine(true);
//                fragment.setGoodsType(mGoodsType);
                //mOnKLineRefreshListener = fragment;
                fragments.add(fragment);
            } else {
                mKLineFragment = new KLineFragment();
                mKLineFragment.setExcode(mExcode);
                mKLineFragment.setCode(mCode);
                switch (i) {
                    case 1:
                        mKLineFragment.setType("10");
                        break;
                    case 2:
                        mKLineFragment.setType("2");
                        break;
                    case 3:
                        mKLineFragment.setType("3");
                        break;
                    case 4:
                        mKLineFragment.setType("4");
                        break;
                    case 5:
                        mKLineFragment.setType("5");
                        break;
                    case 6:
                        mKLineFragment.setType("9");
                        break;
                    case 7:
                        mKLineFragment.setType("6");
                        break;

                }
//                mLightningFragment = new LightningFragment();
//                mLightningFragment.setGoodsType(mGoodsType);
                fragments.add(mKLineFragment);
            }
        }
        KChartFragmentAdapter adapter = new KChartFragmentAdapter(getSupportFragmentManager());
        adapter.setTitles(mTitles);
        adapter.setFragments(fragments);
        mViewPager.setAdapter(adapter);
        int screenWidthDip = DeviceUtils.px2dip(this, DeviceUtils.getScreenWidth(this) - DeviceUtils.dip2px(this, 45));
        int tabWidth = (int) ((float) screenWidthDip / 5.7f);
        int indicatorWidth = (int) (tabWidth * 0.8f);
        mTabLayout.setTabWidth(tabWidth);
        mTabLayout.setIndicatorWidth(indicatorWidth);
        mTabLayout.setViewPager(mViewPager);
    }

    @Override
    public void getProductRealTimeSuccess(RealTimeBean bean) {

    }
}
