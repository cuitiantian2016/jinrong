package com.honglu.future.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import java.io.Serializable;

public abstract class CommonFragment extends BaseFragment {
    private LoadingPage mLoadingPage;
    private boolean isLoadingPageShown;
    private int mLoadingPageParentId = View.NO_ID;
    private Serializable mParams;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mParams != null) {
            outState.putSerializable("SerializableParams", mParams);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mParams = savedInstanceState.getSerializable("SerializableParams");
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isLoadingPageShown) {
            displayLoadingPage(mLoadingPageParentId);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isLoadingPageShown) {
            closeLoadingPage();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    protected void onReload(Context context) {
    }

    public void showLoadingPage() {
        showLoadingPage(View.NO_ID);
    }

    public void showLoadingPage(int layoutId) {
        if (!isLoadingPageShown) {
            isLoadingPageShown = true;
            if (mLoadingPage == null) {
                mLoadingPage = createLoadingPage();
            }
            mLoadingPageParentId = layoutId;
            mLoadingPage.loading();
            displayLoadingPage(layoutId);
        }
    }

    public void setLoadFailedMessage(String failedMessage) {
        if (mLoadingPage != null) {
            mLoadingPage.failed(failedMessage);
        }
    }

    public void setLoadFailedMessage(int failedStringId) {
        if (mLoadingPage != null) {
            mLoadingPage.failed(failedStringId);
        }
    }

    public void closeLoadingPage() {
        isLoadingPageShown = false;
        removeLoadingPage();
    }

    private void removeLoadingPage() {
        if (mLoadingPage != null) {
            ViewGroup parent = (ViewGroup) mLoadingPage.getParent();
            if (parent != null) {
                parent.removeView(mLoadingPage);
            }
        }
    }

    private boolean displayLoadingPage(int layoutId) {
        return displayView(mLoadingPage, layoutId);
    }

    private LoadingPage createLoadingPage() {
        final Context context = getActivity();
        LoadingPage loadingPage = new LoadingPage(context) {
            @Override
            protected void reload(Context context) {
                onReload(context);
            }
        };
        return loadingPage;
    }

    private boolean displayView(View view, int layoutId) {
        ViewGroup layout = null;
        View rootView = getView();
        if (rootView != null && view != null) {
            int index = -1;
            if (layoutId != View.NO_ID) {
                layout = (ViewGroup) rootView.findViewById(layoutId);
                if (layout instanceof LinearLayout) {
                    index = 0;
                }
                if (view.getParent() != null) {
                    ((ViewGroup) view.getParent()).removeView(view);
                }
            } else {
                layout = ((ViewGroup) rootView);
            }
            if (layout != null) {
                layout.addView(view, index, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            }
        }
        return layout != null;
    }

}
