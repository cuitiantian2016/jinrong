package com.honglu.future.widget.banner;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.honglu.future.ui.home.bean.BannerData;

import java.util.List;


public class AutoFlingBannerAdapter extends AutoFlingPagerAdapter<BannerData> {

    private OnClickBannerListener mOnClickBannerListener;
    private OnShowPicBannerListener mOnShowPicBannerListener;

    public interface OnClickBannerListener {

        void itemClick(String url, String circleColumnName);
    }

    public interface OnShowPicBannerListener {

        void showPic(ImageView imageview, String url);
    }

    private Context mContext;

    public AutoFlingBannerAdapter(Fragment context) {
        mContext = context.getContext();
    }

    public AutoFlingBannerAdapter(Context activity) {
        mContext = activity;
    }

    public void setOnClickBannerListener(OnClickBannerListener onClickBannerListener) {
        mOnClickBannerListener = onClickBannerListener;
    }

    public void setOnShowPicBannerListener(OnShowPicBannerListener sOnShowPicBannerListener) {
        mOnShowPicBannerListener = sOnShowPicBannerListener;
    }

    @Override
    public View instantiateView(Context context) {
        ImageView convertView = new ImageView(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.
                MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        convertView.setAdjustViewBounds(true);
        convertView.setScaleType(ScaleType.CENTER_CROP);
        convertView.setLayoutParams(layoutParams);
        return convertView;
    }

    @Override
    public void bindView(final BannerData newsSlide, View view, int position) {
        ImageView imageView = (ImageView) view;
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.
                MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ScaleType.CENTER_CROP);
        imageView.setLayoutParams(layoutParams);
        mOnShowPicBannerListener.showPic(imageView, newsSlide.pic);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickBannerListener.itemClick(newsSlide.url, newsSlide.informationColumnId);
            }
        });
    }

    @Override
    public String getTitle(int position) {
        return "";
    }


}
