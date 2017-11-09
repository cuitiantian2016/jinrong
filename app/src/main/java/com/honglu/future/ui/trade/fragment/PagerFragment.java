package com.honglu.future.ui.trade.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;


import com.honglu.future.base.CommonFragment;

import java.util.List;

public abstract class PagerFragment extends CommonFragment {

    /**
     * ViewPager 正常预加载逻辑
     */
    protected void doLoadData() {

    }

    /**
     * ViewPager 懒加载
     */
    protected void doLazyLoadData() {

    }

    /**
     * [onHiddenChanged() + onResume()/onPause() + setUserVisibleHint()]
     */
    protected void onVisible() {

    }

    /**
     * [onHiddenChanged() + onResume()/onPause() +serVisibleHint()]
     */
    protected void onInvisible() {
    }

    private boolean mIsSupportVisible;
    private boolean mNeedDispatch = true;
    private boolean mInvisibleWhenLeave;
    private boolean mIsFirstVisible = true;
    private boolean mFixStatePagerAdapter;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isResumed()) {
            if (!mIsSupportVisible && isVisibleToUser) {
                dispatchSupportVisible(true);
            } else if (mIsSupportVisible && !isVisibleToUser) {
                dispatchSupportVisible(false);
            }
        } else if (isVisibleToUser) {
            mInvisibleWhenLeave = false;
            mFixStatePagerAdapter = true;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        if (isResumed()) {
            dispatchSupportVisible(!hidden);
//        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        doLoadData();

        if (!mInvisibleWhenLeave && !isHidden() && (getUserVisibleHint() || mFixStatePagerAdapter)) {
            if ((getParentFragment() != null && !getParentFragment().isHidden())
                    || getParentFragment() == null) {
                mNeedDispatch = false;
                dispatchSupportVisible(true);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!mIsFirstVisible) {
            if (!mIsSupportVisible && !mInvisibleWhenLeave && !isHidden() && getUserVisibleHint()) {
                mNeedDispatch = false;
                dispatchSupportVisible(true);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mIsSupportVisible && !isHidden() && getUserVisibleHint()) {
            mNeedDispatch = false;
            mInvisibleWhenLeave = false;
            dispatchSupportVisible(false);
        } else {
            mInvisibleWhenLeave = true;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIsFirstVisible = true;
        mFixStatePagerAdapter = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public final boolean isSupportVisible() {
        return mIsSupportVisible;
    }

    private void dispatchSupportVisible(boolean visible) {
        mIsSupportVisible = visible;

        if (!mNeedDispatch) {
            mNeedDispatch = true;
        } else {
            FragmentManager fragmentManager = getChildFragmentManager();
            if (fragmentManager != null) {
                List<Fragment> childFragments = fragmentManager.getFragments();
                if (childFragments != null) {
                    for (Fragment child : childFragments) {
                        if (child instanceof PagerFragment && !child.isHidden() && child.getUserVisibleHint()) {
                            ((PagerFragment) child).dispatchSupportVisible(visible);
                        }
                    }
                }
            }
        }

        if (visible) {
            if (mIsFirstVisible) {
                mIsFirstVisible = false;
                doLazyLoadData();
            }

            onVisible();
        } else {
            onInvisible();
        }
    }
}
