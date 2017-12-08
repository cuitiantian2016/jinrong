package com.honglu.future.ui.circle.circlemain;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.honglu.future.base.CommonFragment;
import com.honglu.future.util.LogUtils;

import java.util.List;

public abstract class PagerFragment extends CommonFragment {

    protected View mRootView;

    @LayoutRes
    protected abstract int doGetContentLayout();

    protected abstract void doInit(View root);

    /**
     * ViewPager 正常预加载逻辑
     */
    protected void doLoadData() {
        LogUtils.logd(toString());
    }

    /**
     * ViewPager 懒加载
     */
    protected void doLazyLoadData() {
        LogUtils.logd(toString());
    }

    /**
     * [onHiddenChanged() + onResume()/onPause() + setUserVisibleHint()]
     */
    protected void onVisible() {
        LogUtils.logd(toString());
    }

    /**
     * [onHiddenChanged() + onResume()/onPause() +serVisibleHint()]
     */
    protected void onInvisible() {
        LogUtils.logd(toString());
    }

    private boolean mIsSupportVisible;
    private boolean mNeedDispatch = true;
    private boolean mInvisibleWhenLeave;
    private boolean mIsFirstVisible = true;
    private boolean mFixStatePagerAdapter;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.logd(toString());
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
        LogUtils.logd(toString());
        if (isResumed()) {
            dispatchSupportVisible(!hidden);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.logd(toString());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtils.logd(toString());
        if (mRootView == null) {
            mRootView = inflater.inflate(doGetContentLayout(), null);
            doInit(mRootView);
        }
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.logd(toString());

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
        LogUtils.logd(toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.logd(toString());

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
        LogUtils.logd(toString());

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
        LogUtils.logd(toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.logd(toString());
        mIsFirstVisible = true;
        mFixStatePagerAdapter = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.logd(toString());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.logd(toString());
    }

    public View getRootView() {
        return mRootView;
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
