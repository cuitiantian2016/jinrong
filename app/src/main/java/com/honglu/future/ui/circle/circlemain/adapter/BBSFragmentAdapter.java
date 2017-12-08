package com.honglu.future.ui.circle.circlemain.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.honglu.future.ui.circle.circlemain.BBSClassifyFragment;

import java.util.List;

public class BBSFragmentAdapter extends FragmentStatePagerAdapter {
    private List<String> fragmentsTopicType;

    public BBSFragmentAdapter(FragmentManager fm, List<String> fragmentList) {
        super(fm);
        this.fragmentsTopicType = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        String topicType = fragmentsTopicType.get(position);
        Bundle bundle = new Bundle();
        bundle.putString(BBSClassifyFragment.EXTRA_CLASSIFY_TYPE, topicType);
        BBSClassifyFragment fragment = new BBSClassifyFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public int getCount() {
        return fragmentsTopicType.size();
    }

}
