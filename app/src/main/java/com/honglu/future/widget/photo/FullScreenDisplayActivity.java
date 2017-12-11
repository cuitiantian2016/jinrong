package com.honglu.future.widget.photo;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.honglu.future.R;

import java.util.ArrayList;

import cn.falcon.photogallery.PhotoViewAttacher;


public class FullScreenDisplayActivity extends FragmentActivity {
    private static final String IS_LOCKED_ARG = "isLocked";
    private ViewPager mViewPager;
    private ArrayList<String> mImageUrls;

    private int mLocationX;
    private int mLocationY;
    private int mWidth;
    private int mHeight;

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreendisplay);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager mTintManager = new SystemBarTintManager(this);
        mTintManager.setStatusBarTintEnabled(true);
        mTintManager.setNavigationBarTintEnabled(true);
        mTintManager.setStatusBarTintResource(Color.parseColor("#00000000"));
        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
        setContentView(mViewPager);
        mViewPager.setBackgroundColor(0xff000000);
        mImageUrls = getIntent().getExtras().getStringArrayList("image_urls");
        int position = getIntent().getExtras().getInt("position");

        mLocationX = getIntent().getIntExtra("locationX", 0);
        mLocationY = getIntent().getIntExtra("locationY", 0);
        mWidth = getIntent().getIntExtra("width", 0);
        mHeight = getIntent().getIntExtra("height", 0);

        mViewPager.setAdapter(new SamplePagerAdapter());
        mViewPager.setCurrentItem(position);
        if (savedInstanceState != null) {
            boolean isLocked = savedInstanceState.getBoolean(IS_LOCKED_ARG, false);
            ((HackyViewPager) mViewPager).setLocked(isLocked);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class SamplePagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mImageUrls.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            SmoothImageView photoView = new SmoothImageView(container.getContext());
            photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    finish();
                }
            });
            String imageUri = mImageUrls.get(position);
            Glide.with(FullScreenDisplayActivity.this).load(imageUri).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate().dontTransform().error(R.mipmap.other_empty).into(photoView);
            if (mWidth != 0 && mHeight != 0) {
                photoView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
                photoView.transformIn();
            }
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    private boolean isViewPagerActive() {
        return (mViewPager != null && mViewPager instanceof HackyViewPager);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (isViewPagerActive()) {
            outState.putBoolean(IS_LOCKED_ARG, ((HackyViewPager) mViewPager).isLocked());
        }
        super.onSaveInstanceState(outState);
    }

}
