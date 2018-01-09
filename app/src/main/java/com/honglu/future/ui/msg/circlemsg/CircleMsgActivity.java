package com.honglu.future.ui.msg.circlemsg;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.gson.JsonNull;
import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.config.Constant;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.http.RxHelper;
import com.honglu.future.util.AndroidUtil;
import com.honglu.future.util.SpUtil;
import com.honglu.future.widget.tab.CommonTabLayout;
import com.honglu.future.widget.tab.CustomTabEntity;
import com.honglu.future.widget.tab.SimpleOnTabSelectListener;
import com.honglu.future.widget.tab.TabEntity;

import java.util.ArrayList;


import butterknife.BindView;

/**
 * 消息
 * Created by zhuaibing on 2017/12/7
 */

public class CircleMsgActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.circle_msg_tab)
    CommonTabLayout mCircleMsgTab;

    private CircleMsgHFragment mHfFragment;
    private CircleMsgPLFragment mPlFragment;
    private int mPosition = 0;

    @Override
    public void initPresenter() {

    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_circle_msg;
    }

    @Override
    public void loadData() {
        mTitle.setTitle(false, R.color.color_white,"消息");
        mTitle.setRightTitle(R.color.color_979899,"清空",this);

        ArrayList<CustomTabEntity> mTabList = new ArrayList<>();
        mTabList.add(new TabEntity("收到的回复"));
        mTabList.add(new TabEntity("收到的评论"));

        ArrayList<Fragment> mFragments = new ArrayList<>();
        mHfFragment = new CircleMsgHFragment();
        mPlFragment = new CircleMsgPLFragment();
        mFragments.add(mHfFragment);
        mFragments.add(mPlFragment);

        mCircleMsgTab.setTabData(mTabList, (FragmentActivity) mContext, R.id.fl_content, mFragments);
        mCircleMsgTab.setOnTabSelectListener(new SimpleOnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                super.onTabSelect(position);
                mPosition = position;
                AndroidUtil.hideInputKeyboard(CircleMsgActivity.this);
            }
        });
        read(3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_right: //清空
                if (mPosition == 0){
                    mHfFragment.getClearReply();
                }else {
                    mPlFragment.getClearComments();
                }
                break;
        }
    }

    private void read(int type){
        HttpManager.getApi().updateReplyStatus(SpUtil.getString(Constant.CACHE_TAG_UID),type).compose(RxHelper.<JsonNull>handleSimplyResult()).subscribe(new HttpSubscriber<JsonNull>() {
            @Override
            protected void _onNext(JsonNull jsonNull) {
                super._onNext(jsonNull);
            }

            @Override
            protected void _onError(String message) {
                super._onError(message);
            }
        });
    }
}
