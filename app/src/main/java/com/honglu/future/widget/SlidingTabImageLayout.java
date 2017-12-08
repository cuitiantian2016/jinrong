package com.honglu.future.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.widget.kchart.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 网络图片加载指示器简单定制
 */
public class SlidingTabImageLayout extends SlidingTabLayout {

    protected List<String> mSelectIconUrls;
    protected List<String> mUnselectIconUrls;

    OnTabClickListener mTabClickListener;

    public SlidingTabImageLayout(Context context) {
        super(context);
    }

    public SlidingTabImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidingTabImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void notifyDataSetChanged() {
        mTabsContainer.removeAllViews();
        mTabCount = mTitles == null ? mViewPager.getAdapter().getCount() : mTitles.size();
        View tabView;
        for (int i = 0; i < mTabCount; i++) {
            tabView = View.inflate(mContext, R.layout.layout_tab_image, null);
            CharSequence pageTitle = mTitles == null ? mViewPager.getAdapter().getPageTitle(i) : mTitles.get(i);
            addTab(i, pageTitle.toString(), tabView);
        }

        updateTabStyles();
    }

    public void addNewTab(String title, String selectIconUrl, String unselectIconUrl) {
        View tabView = View.inflate(mContext, R.layout.layout_tab_image, null);

        if (mTitles == null) mTitles = new ArrayList<>();
        mTitles.add(title);

        if (mSelectIconUrls == null) mSelectIconUrls = new ArrayList<>();
        mSelectIconUrls.add(selectIconUrl);

        if (mUnselectIconUrls == null) mUnselectIconUrls = new ArrayList<>();
        mUnselectIconUrls.add(unselectIconUrl);

        CharSequence pageTitle = mTitles == null ? mViewPager.getAdapter().getPageTitle(mTabCount) : mTitles.get(mTabCount);
        addTab(mTabCount, pageTitle.toString(), tabView);
        mTabCount = mTitles == null ? mViewPager.getAdapter().getCount() : mTitles.size();

        updateTabStyles();
    }

    @Override
    protected void addTab(int position, String title, View tabView) {
        String unselectIconUrl = mUnselectIconUrls.get(position);
        ImageView iv_tab_icon = (ImageView) tabView.findViewById(R.id.iv_tab_icon);
        if (iv_tab_icon != null) {
            ImageUtil.display(unselectIconUrl, iv_tab_icon, R.drawable.bbs_tab_icon_nomal);
        }

        TextView tv_tab_title = (TextView) tabView.findViewById(R.id.tv_tab_title);
        if (tv_tab_title != null) {
            if (title != null) tv_tab_title.setText(title);
        }

        tabView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mTabsContainer.indexOfChild(v);

                if (mTabClickListener != null && !mTabClickListener.onClickProcess(v, position)) {
                    if (position != -1) {
                        if (mViewPager.getCurrentItem() != position) {
                            if (mSnapOnTabClick) {
                                mViewPager.setCurrentItem(position, false);
                            } else {
                                mViewPager.setCurrentItem(position);
                            }

                            if (mListener != null) {
                                mListener.onTabSelect(position);

                                mListener.onTabUnselected(mViewPager.getCurrentItem());
                            }
                        } else {
                            if (mListener != null) {
                                mListener.onTabReselect(position);
                            }
                        }
                    }
                }
            }
        });

        /** 每一个Tab的布局参数 */
        LinearLayout.LayoutParams lp_tab = mTabSpaceEqual ?
                new LinearLayout.LayoutParams(0, FrameLayout.LayoutParams.MATCH_PARENT, 1.0f) :
                new LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT);
        if (mTabWidth > 0) {
            lp_tab = new LinearLayout.LayoutParams((int) mTabWidth, FrameLayout.LayoutParams.MATCH_PARENT);
        }

        mTabsContainer.addView(tabView, position, lp_tab);
    }

    @Override
    protected void updateTabStyles() {
        for (int i = 0; i < mTabCount; i++) {
            View v = mTabsContainer.getChildAt(i);
            TextView tv_tab_title = (TextView) v.findViewById(R.id.tv_tab_title);
            if (tv_tab_title != null) {
                tv_tab_title.setTextColor(i == mCurrentTab ? mTextSelectColor : mTextUnselectColor);
                tv_tab_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextsize);
                tv_tab_title.setPadding((int) mTabPadding, 0, (int) mTabPadding, 0);
                if (mTextAllCaps) {
                    tv_tab_title.setText(tv_tab_title.getText().toString().toUpperCase());
                }

                if (mTextBold == TEXT_BOLD_BOTH) {
                    tv_tab_title.getPaint().setFakeBoldText(true);
                } else if (mTextBold == TEXT_BOLD_NONE) {
                    tv_tab_title.getPaint().setFakeBoldText(false);
                }
            }

            ImageView iv_tab_icon = (ImageView) v.findViewById(R.id.iv_tab_icon);
            if (iv_tab_icon != null) {
                ImageUtil.display(i == mCurrentTab ? mSelectIconUrls.get(i) : mUnselectIconUrls.get(i)
                        , iv_tab_icon, R.drawable.bbs_tab_icon_nomal);
            }
        }
    }

    @Override
    protected void updateTabSelection(int position) {
        for (int i = 0; i < mTabCount; ++i) {
            View tabView = mTabsContainer.getChildAt(i);
            final boolean isSelect = i == position;
            TextView tab_title = (TextView) tabView.findViewById(R.id.tv_tab_title);

            if (tab_title != null) {
                tab_title.setTextColor(isSelect ? mTextSelectColor : mTextUnselectColor);
                if (mTextBold == TEXT_BOLD_WHEN_SELECT) {
                    tab_title.getPaint().setFakeBoldText(isSelect);
                }
            }

            ImageView iv_tab_icon = (ImageView) tabView.findViewById(R.id.iv_tab_icon);
            if (iv_tab_icon != null) {
                ImageUtil.display(isSelect ? mSelectIconUrls.get(i) : mUnselectIconUrls.get(i)
                        , iv_tab_icon, R.drawable.bbs_tab_icon_nomal);
            }
        }
    }

    public interface OnTabClickListener {
        /**
         * 是否处理 tab 点击事件
         *
         * @param view
         * @param position
         * @return
         */
        boolean onClickProcess(View view, int position);
    }

    public void setOnTabClickListener(OnTabClickListener listener) {
        mTabClickListener = listener;
    }

}
