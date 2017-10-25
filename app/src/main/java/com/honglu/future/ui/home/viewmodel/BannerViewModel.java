package com.honglu.future.ui.home.viewmodel;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.honglu.future.R;
import com.honglu.future.base.BasePresenter;
import com.honglu.future.base.IBaseView;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.home.bean.BannerData;
import com.honglu.future.ui.home.bean.MarketData;
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

public class BannerViewModel extends IBaseView<BannerData>{
    private static final String TAG = "BannerViewModel";

    public View mView;
    private Context mContext;
    private BasePresenter mBannerPresenter;

    private AutoFlingBannerAdapter mAutoFlingBannerAdapter;
    private final Banner mBanner;

    public BannerViewModel(Context context){
        mContext = context;
        mView = View.inflate(context, R.layout.view_modle_banner,null);
        mBanner = (Banner) mView.findViewById(R.id.banner);
        setAdapter();
        refreshData();

    }
    /**
     * 刷新数据
     */
    private void refreshData() {
        if (mBannerPresenter == null){
            mBannerPresenter = new BasePresenter<IBaseView<BannerData>>(this){
                @Override
                public void getData() {
                    super.getData();
                    toSubscribe(HttpManager.getApi().getBannerData(), new HttpSubscriber<BannerData>() {
                        @Override
                        protected void _onNext(BannerData o) {
                            mView.bindData(o);
                        }
                    });
                }
            };
        }
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
