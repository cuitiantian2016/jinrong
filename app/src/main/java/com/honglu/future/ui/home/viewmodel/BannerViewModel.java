package com.honglu.future.ui.home.viewmodel;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.honglu.future.R;
import com.honglu.future.ui.home.bean.BannerData;
import com.honglu.future.ui.home.contract.HomeContract;
import com.honglu.future.ui.home.presenter.HomePresenter;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.widget.banner.AutoFlingBannerAdapter;
import com.honglu.future.widget.banner.Banner;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hefei
 * banner的组件
 */

public class BannerViewModel extends HomeContract.BannerView{
    private static final String TAG = "BannerViewModel";

    public View mBannerView;
    private Context mContext;

    @BindView(R.id.banner)
    public Banner mBanner;
    private AutoFlingBannerAdapter mAutoFlingBannerAdapter;

    public BannerViewModel(Context context){
        mContext = context;
        mBannerView = View.inflate(context, R.layout.view_modle_banner,null);
        ButterKnife.bind(mBannerView);
        setAdapter();
        HomePresenter.BannerPresenter mBannerPresenter = new HomePresenter.BannerPresenter(this);
        mBannerPresenter.getBannerData();
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
            public void itemClick(String url, String circleColumnName) {
//                Bus.callURL(mCurrentActivity,url);
            }
        });
        mBanner.setAdapter(mAutoFlingBannerAdapter);
    }
    @Override
    public void bindData(BannerData bannerData) {
        if (bannerData ==null||bannerData.getData() ==null||bannerData.getData().getData() ==null){
            return;
        }
        mBanner.setBackground(null);
        List<BannerData.DataBeanX.DataBean> data = bannerData.getData().getData();
        mAutoFlingBannerAdapter.setData(data);
        mBanner.changeIndicatorStyle(data.size(), 35, Color.TRANSPARENT);
        mBanner.start();
    }
}
