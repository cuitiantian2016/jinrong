package com.honglu.future.ui.main.guide;


import android.content.Intent;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.config.Constant;
import com.honglu.future.ui.main.activity.MainActivity;
import com.honglu.future.util.ConvertUtil;
import com.honglu.future.util.SpUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 引导页
 * xiejingwen
 */
public class GuideActivity extends BaseActivity {
    GuidePagerAdapter pagerAdapter;
    boolean isTransparent = false;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.guidelayout)
    LinearLayout mGuidelayout;
    private int curPosition;
    int[] images = new int[]{R.mipmap.icon_guide1, R.mipmap.icon_guide2, R.mipmap.icon_guide3};

    @Override
    public int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        initGuideLayout();
    }

    private void initGuideLayout() {
//        for (int i = 0; i < images.length; i++) {
//            ImageView view = new ImageView(GuideActivity.this);
//            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//            if (i != 0) {
//                params.setMargins(ConvertUtil.dip2px(this, 8), 0, 0, 0);
//            }
//            view.setLayoutParams(params);
//            view.setPadding(ConvertUtil.px2dip(this, 5), 0, ConvertUtil.px2dip(this, 5), 0);
//            view.setImageResource(R.drawable.shape_aboutdot);
//            view.setId(10000 + i);
//            mGuidelayout.addView(view);
//        }
        //mGuidelayout.getChildAt(0).setEnabled(false);
        pagerAdapter = new GuidePagerAdapter();
        mViewpager.setAdapter(pagerAdapter);
        mViewpager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == images.length - 1 && positionOffset == 0
                        && positionOffsetPixels == 0) {
                    if (isTransparent) {
                        SpUtil.putInt(Constant.CACHE_IS_FIRST_LOGIN, Constant.NOT_FIRST_LOGIN);
                        startActivity(MainActivity.class);
                        finish();
                        isTransparent = false;
                    }

                }
            }

            @Override
            public void onPageSelected(int position) {
                curPosition = position;
                for (int i = 0; i < images.length; i++) {
                    if (i == position) {
                        //mGuidelayout.getChildAt(i).setEnabled(false);
                    } else {
                        //mGuidelayout.getChildAt(i).setEnabled(true);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING
                        && curPosition == images.length - 1) {
                    isTransparent = true;
                } else {
                    isTransparent = false;
                }
            }
        });
    }

    class GuidePagerAdapter extends PagerAdapter {
        List<View> list;

        public GuidePagerAdapter() {
            list = new ArrayList<>();
            for (int i = 0; i < images.length; i++) {
                if (i < images.length - 1) {
                    ImageView view = new ImageView(GuideActivity.this);
                    LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                    view.setLayoutParams(params);
                    view.setScaleType(ScaleType.FIT_XY);
                    view.setImageResource(images[i]);
                    list.add(view);
                } else {
                    View view = LayoutInflater.from(mContext).inflate(R.layout.guide_last_layout, null);
                    ImageView imageView = (ImageView) view.findViewById(R.id.iv_img);
                    TextView button = (TextView) view.findViewById(R.id.btn_next);
                    imageView.setImageResource(images[i]);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SpUtil.putInt(Constant.CACHE_IS_FIRST_LOGIN, Constant.NOT_FIRST_LOGIN);
                            Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    list.add(view);
                }

            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position), 0);
            return list.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return (arg0 == arg1);
        }

    }

}
