package com.honglu.future.ui.circle.circlemain.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.honglu.future.config.Constant;
import com.honglu.future.ui.circle.bean.TopicFilter;
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
        TopicFilter topicType = Constant.topic_filter.get(position);
        Bundle bundle = new Bundle();
        bundle.putString(BBSClassifyFragment.EXTRA_CLASSIFY_TYPE, topicType.type);
        bundle.putString(BBSClassifyFragment.EXTRA_CLASSIFY_NAME, topicType.title);
        BBSClassifyFragment fragment = new BBSClassifyFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public int getCount() {
        return fragmentsTopicType.size();
    }

}
