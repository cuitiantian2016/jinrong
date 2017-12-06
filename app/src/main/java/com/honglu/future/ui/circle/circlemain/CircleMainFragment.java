package com.honglu.future.ui.circle.circlemain;

import com.honglu.future.R;
import com.honglu.future.base.BaseFragment;

/**
 * Created by zq on 2017/12/6.
 */

public class CircleMainFragment extends BaseFragment {
    public static CircleMainFragment circleMainFragment;

    public static CircleMainFragment getInstance() {
        if (circleMainFragment == null) {
            circleMainFragment = new CircleMainFragment();
        }
        return circleMainFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_circle_main;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {

    }
}
