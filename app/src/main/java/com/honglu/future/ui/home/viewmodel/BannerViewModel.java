package com.honglu.future.ui.home.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.honglu.future.R;
import com.honglu.future.base.BasePresenter;
import com.honglu.future.base.IBaseView;
import com.honglu.future.config.ConfigUtil;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.home.bean.BannerData;
import com.honglu.future.ui.main.activity.WebViewActivity;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.util.ToastUtil;
import com.honglu.future.widget.banner.AutoFlingBannerAdapter;
import com.honglu.future.widget.banner.Banner;
import com.umeng.analytics.MobclickAgent;

import java.util.List;


/**
 * Created by hefei
 * banner的组件
 */

public class BannerViewModel extends IBaseView<List<BannerData>> {
    private static final String TAG = "BannerViewModel";

    public View mView;
    private Context mContext;
    private BasePresenter mBannerPresenter;

    private AutoFlingBannerAdapter mAutoFlingBannerAdapter;
    private final Banner mBanner;

    public BannerViewModel(Context context) {
        mContext = context;
        mView = View.inflate(context, R.layout.view_modle_banner, null);
        mBanner = (Banner) mView.findViewById(R.id.banner);
        mView.findViewById(R.id.rl_home_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.show("点击设置");
            }
        });
        setAdapter();
        refreshData();

    }

    /**
     * 刷新数据
     */
    public void refreshData() {
        if (mBannerPresenter == null)
            mBannerPresenter = new BasePresenter<IBaseView<List<BannerData>>>(this) {
                @Override
                public void getData() {
                    super.getData();
                    toSubscribe(HttpManager.getApi().getBannerData(), new HttpSubscriber<List<BannerData>>() {
                        @Override
                        protected void _onNext(List<BannerData> o) {
                            mView.bindData(o);
                        }
                    });
                }
            };
        mBannerPresenter.getData();
    }

    private void setAdapter() {
        mAutoFlingBannerAdapter = new AutoFlingBannerAdapter(mContext);
        mAutoFlingBannerAdapter.setOnShowPicBannerListener(new AutoFlingBannerAdapter.OnShowPicBannerListener() {
            @Override
            public void showPic(ImageView imageview, String url) {
                ImageUtil.display(mContext, url, imageview, R.mipmap.other_empty);
            }
        });
        //Banner条目点击监听
        mAutoFlingBannerAdapter.setOnClickBannerListener(new AutoFlingBannerAdapter.OnClickBannerListener() {
            @Override
            public void itemClick(String url, String circleColumnName,int position) {
                Log.d(TAG, "url-->" + url + "circleColumnName-->" + circleColumnName);
                MobclickAgent.onEvent(mContext, "sy_" + String.valueOf(position + 1) + "st_banner", "首页_第" + String.valueOf(position + 1)+ "帧banner");
                Intent intentAbout = new Intent(mContext, WebViewActivity.class);
                intentAbout.putExtra("url", url);
                if (!TextUtils.isEmpty(circleColumnName)) {
                    intentAbout.putExtra("title", circleColumnName);
                }
                mContext.startActivity(intentAbout);
            }
        });
        mBanner.setAdapter(mAutoFlingBannerAdapter);
    }

    @Override
    public void bindData(List<BannerData> bannerData) {
        if (bannerData == null || bannerData.size() <= 0) {
            return;
        }
        mBanner.setBackground(null);
        mAutoFlingBannerAdapter.setData(bannerData);
        mBanner.setRule(Banner.Rule.RIGHT);
        mBanner.changeIndicatorStyle(bannerData.size(), 35, Color.TRANSPARENT);
        mBanner.start();
    }

    /**
     * 销毁
     */
    public void onDestory() {
        mBannerPresenter.onDestroy();
    }
}
