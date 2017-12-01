package com.honglu.future.ui.recharge.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.config.ConfigUtil;
import com.honglu.future.ui.main.activity.WebViewActivity;
import com.honglu.future.util.CalendarUtil;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.widget.tab.CommonTabLayout;
import com.honglu.future.widget.tab.CustomTabEntity;
import com.honglu.future.widget.tab.SimpleOnTabSelectListener;
import com.honglu.future.widget.tab.TabEntity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hefei on 2017/10/24.
 * 出入金
 */
@Route(path = "/trade/pay")
public class InAndOutGoldActivity extends BaseActivity {
    private static final String KEY_POSITION = "KEY_POSITION";
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
    @Autowired
    public boolean isLogin;
    private PayAndOutGoldFragment pay;
    private PayAndOutGoldFragment rechange;

    public static void startInAndOutGoldActivity(Context context,int position){
        Intent intent = new Intent(context, InAndOutGoldActivity.class);
        intent.putExtra(KEY_POSITION,position);
        context.startActivity(intent);
    }
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
        mTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAbout = new Intent(mActivity, WebViewActivity.class);
                intentAbout.putExtra("url", ConfigUtil.SCHOOLDETAILS);
                intentAbout.putExtra("title", "出入金规则");
                startActivity(intentAbout);
            }
        });
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
        pay = PayAndOutGoldFragment.getInstance(true);
        rechange = PayAndOutGoldFragment.getInstance(false);
        mFragments.add(pay);
        mFragments.add(rechange);
        mFragments.add(InAndOutDetailFragment.getInstance());
        mCommonTabLayout.setTabData(mTabList, (FragmentActivity) mContext, R.id.trade_fragment_container, mFragments);
        mCommonTabLayout.setOnTabSelectListener(new SimpleOnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                super.onTabSelect(position);
                mCurrentPosition = position;
            }
        });
        Intent intent = getIntent();
        if (intent!=null){
            mCurrentPosition = intent.getIntExtra(KEY_POSITION,0);
        }
        mCommonTabLayout.setCurrentTab(mCurrentPosition);
    }
    /**
     * 初始化布局
     */
    private void initView() {
//        int screenWidthDip = DeviceUtils.px2dip(mContext, DeviceUtils.getScreenWidth(mContext));
//        int indicatorWidth = (int) (screenWidthDip * 0.2f);
//        mCommonTabLayout.setIndicatorWidth(indicatorWidth);
        mTabList = new ArrayList<>();
        mTabList.add(new TabEntity(mContext.getString(R.string.pay_gold)));
        mTabList.add(new TabEntity(mContext.getString(R.string.out_gold)));
        mTabList.add(new TabEntity(mContext.getString(R.string.recharge)));
    }

}
