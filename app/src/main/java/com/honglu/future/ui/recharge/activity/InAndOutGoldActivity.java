package com.honglu.future.ui.recharge.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.widget.tab.CommonTabLayout;
import com.honglu.future.widget.tab.CustomTabEntity;
import com.honglu.future.widget.tab.SimpleOnTabSelectListener;
import com.honglu.future.widget.tab.TabEntity;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by hc on 2017/10/24.
 * 出入金
 */
public class InAndOutGoldActivity extends BaseActivity {
    private ArrayList<CustomTabEntity> mTabList;
    @BindView(R.id.tv_back)
    ImageView mLeftIcon;
    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.trade_common_tab_layout)
    CommonTabLayout mCommonTabLayout;
    private int mCurrentPosition;
    @Override
    public int getLayoutId() {
        return R.layout.acticity_global_details;
    }
    @Override
    public void initPresenter() {
    }
    @Override
    public void loadData() {
        mTitle.setText(mContext.getString(R.string.out_in_gold));
        mTvRight.setText(mContext.getString(R.string.rule_out_in_gold));
        mLeftIcon.setVisibility(View.VISIBLE);
        mLeftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initView();
        //添加fragment
        addFragments();
    }
    /**
     * 添加fragment
     */
    private void addFragments() {
        ArrayList<Fragment> mFragments = new ArrayList<>();
        mFragments.add(new PayAndOutGoldFragment());
        mFragments.add(new PayAndOutGoldFragment());
        mFragments.add(new PayAndOutGoldFragment());
        mCommonTabLayout.setTabData(mTabList, (FragmentActivity) mContext, R.id.trade_fragment_container, mFragments);
        mCommonTabLayout.setOnTabSelectListener(new SimpleOnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                super.onTabSelect(position);
                mCurrentPosition = position;
            }
        });
    }
    /**
     * 初始化布局
     */
    private void initView() {
        int screenWidthDip = DeviceUtils.px2dip(mContext, DeviceUtils.getScreenWidth(mContext));
        int indicatorWidth = (int) (screenWidthDip * 0.2f);
        mCommonTabLayout.setIndicatorWidth(indicatorWidth);
        mTabList = new ArrayList<>();
        mTabList.add(new TabEntity(mContext.getString(R.string.pay_gold)));
        mTabList.add(new TabEntity(mContext.getString(R.string.out_gold)));
        mTabList.add(new TabEntity(mContext.getString(R.string.recharge)));
    }

}
