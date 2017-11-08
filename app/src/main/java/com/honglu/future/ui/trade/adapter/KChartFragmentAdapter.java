package com.honglu.future.ui.trade.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by fengpeihao on 2017/2/17.
 */

public class KChartFragmentAdapter extends FragmentPagerAdapter {
    private String[] mTitles;
    private String mGoodsType = "XAG1";//交易品代码
    private List<Fragment> mFragments;

    public void setFragments(List<Fragment> fragments) {
        mFragments = fragments;
    }

    public void setGoodsType(String goodsType) {
        mGoodsType = goodsType;
    }

    public void setTitles(String[] titles) {
        mTitles = titles;
    }

    public KChartFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
//        switch (position) {
//            case 0:
//                MinuteHourFragment minuteHourFragment = new MinuteHourFragment();
//                minuteHourFragment.setTouchEnabled(false);
//                minuteHourFragment.setGoodsType(mGoodsType);
//                return minuteHourFragment;
//            default:
//                KLineFragment kLineFragment = new KLineFragment();
//                kLineFragment.setGoodsType(mGoodsType);
//                kLineFragment.setType(String.valueOf(1 + position));
//                return kLineFragment;
//        }
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
