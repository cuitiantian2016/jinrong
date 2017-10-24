package com.scwang.smartrefresh.layout.header;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.scwang.smartrefresh.layout.R;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.header.framedrawable.AnimationsContainer;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.scwang.smartrefresh.layout.util.DeviceUtils;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 经典下拉头部
 * Created by SCWANG on 2017/5/28.
 */
@SuppressWarnings("unused")
public class ClassicsHeader extends RelativeLayout implements RefreshHeader {

    public static String REFRESH_HEADER_PULLDOWN = "下拉可以刷新";
    public static String REFRESH_HEADER_REFRESHING = "正在刷新...";
    public static String REFRESH_HEADER_LOADING = "正在加载...";
    public static String REFRESH_HEADER_RELEASE = "释放立即刷新";
    public static String REFRESH_HEADER_FINISH = "刷新完成";
    public static String REFRESH_HEADER_FAILED = "刷新失败";

    protected String KEY_LAST_UPDATE_TIME = "LAST_UPDATE_TIME";

    protected Date mLastTime;
    protected ImageView mDragView;
    protected ImageView mProgressView;
    protected AnimationDrawable mAnimationDrawable;

    protected SpinnerStyle mSpinnerStyle = SpinnerStyle.Translate;
    protected SharedPreferences mShared;
    protected RefreshKernel mRefreshKernel;
    protected int mBackgroundColor;
    protected double mDistanceUnit = 14;
    protected int frameIndex = 1;
    protected final static int[] animPics = {R.drawable.pull0001, R.drawable.pull0002, R.drawable.pull0003, R.drawable.pull0004,
            R.drawable.pull0005, R.drawable.pull0006, R.drawable.pull0007, R.drawable.pull0008, R.drawable.pull0009,
            R.drawable.pull0010, R.drawable.pull0011, R.drawable.pull0012, R.drawable.pull0013, R.drawable.pull0014,
            R.drawable.pull0015, R.drawable.pull0016};

    protected final static int[] refreshPics = {R.drawable.recycle0001, R.drawable.recycle0002, R.drawable.recycle0003, R.drawable.recycle0004,
            R.drawable.recycle0005, R.drawable.recycle0006, R.drawable.recycle0007, R.drawable.recycle0008, R.drawable.recycle0009,
            R.drawable.recycle0010, R.drawable.recycle0011, R.drawable.recycle0012, R.drawable.recycle0013, R.drawable.recycle0014,
            R.drawable.recycle0015, R.drawable.recycle0016, R.drawable.recycle0017, R.drawable.recycle0018, R.drawable.recycle0019, R.drawable.recycle0020, R.drawable.recycle0021
            , R.drawable.recycle0022, R.drawable.recycle0023, R.drawable.recycle0024, R.drawable.recycle0025, R.drawable.recycle0026
            , R.drawable.recycle0027, R.drawable.recycle0028, R.drawable.recycle0029
            , R.drawable.recycle0030, R.drawable.recycle0031, R.drawable.recycle0032};
    private AnimationsContainer.FramesSequenceAnimation mAnimation;

    //<editor-fold desc="RelativeLayout">
    public ClassicsHeader(Context context) {
        super(context);
        this.initView(context, null);
    }

    public ClassicsHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context, attrs);
    }

    public ClassicsHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context, attrs);
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public ClassicsHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        int screenWidth = DeviceUtils.getScreenWidth(context);
        DensityUtil density = new DensityUtil();

        setMinimumHeight(density.dip2px(80));

        LinearLayout layout = new LinearLayout(context);
        layout.setId(android.R.id.widget_frame);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setOrientation(LinearLayout.VERTICAL);

        LayoutParams lpHeaderLayout = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lpHeaderLayout.addRule(CENTER_IN_PARENT);
        addView(layout, lpHeaderLayout);

        mProgressView = new ImageView(context);


        LayoutParams lpProgress = new LayoutParams(density.dip2px(80), density.dip2px(80));
        lpProgress.addRule(CENTER_IN_PARENT);

        addView(mProgressView, lpProgress);
        LayoutParams htProgress = new LayoutParams(density.dip2px(80), density.dip2px(80));
        htProgress.addRule(CENTER_IN_PARENT);
        //htProgress.leftMargin = screenWidth / 2 - density.dip2px(70);
        mDragView = new ImageView(context);
        addView(mDragView, htProgress);

        if (isInEditMode()) {
            mDragView.setVisibility(GONE);
        } else {
            mProgressView.setVisibility(GONE);
        }

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ClassicsHeader);

        mSpinnerStyle = SpinnerStyle.values()[ta.getInt(R.styleable.ClassicsHeader_srlClassicsSpinnerStyle, mSpinnerStyle.ordinal())];

        if (ta.hasValue(R.styleable.ClassicsHeader_srlArrowDrawable)) {
            mDragView.setImageDrawable(ta.getDrawable(R.styleable.ClassicsHeader_srlArrowDrawable));
        } else {
            mDragView.setBackgroundResource(R.drawable.pull0001);
        }

        if (ta.hasValue(R.styleable.ClassicsHeader_srlProgressDrawable)) {
            mProgressView.setImageDrawable(ta.getDrawable(R.styleable.ClassicsHeader_srlProgressDrawable));
        } else {
            mAnimation = AnimationsContainer.getInstance().createProgressDialogAnim(mProgressView, refreshPics);
        }

        int primaryColor = ta.getColor(R.styleable.ClassicsHeader_srlPrimaryColor, 0);
        int accentColor = ta.getColor(R.styleable.ClassicsHeader_srlAccentColor, 0);
        if (primaryColor != 0) {
            if (accentColor != 0) {
                setPrimaryColors(primaryColor, accentColor);
            } else {
                setPrimaryColors(primaryColor);
            }
        } else if (accentColor != 0) {
            setPrimaryColors(0, accentColor);
        }

        ta.recycle();

        try {//try 不能删除-否则会出现兼容性问题
            if (context instanceof FragmentActivity) {
                FragmentManager manager = ((FragmentActivity) context).getSupportFragmentManager();
                if (manager != null) {
                    List<Fragment> fragments = manager.getFragments();
                    if (fragments != null && fragments.size() > 0) {
                        setLastUpdateTime(new Date());
                        return;
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        KEY_LAST_UPDATE_TIME += context.getClass().getName();
        mShared = context.getSharedPreferences("ClassicsHeader", Context.MODE_PRIVATE);
        setLastUpdateTime(new Date(mShared.getLong(KEY_LAST_UPDATE_TIME, System.currentTimeMillis())));
    }

    //</editor-fold>

    //<editor-fold desc="RefreshHeader">
    @Override
    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {
        mRefreshKernel = kernel;
        mRefreshKernel.requestDrawBackgoundForHeader(mBackgroundColor);
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
    }

    @Override
    public void onPullingDown(float percent, int offset, int headHeight, int extendHeight) {
        if (offset >= 0 && offset <= 13.75) {
            mDragView.setBackgroundResource(animPics[0]);
        } else if (offset > 13.75 & offset <= 27.5) {
            mDragView.setBackgroundResource(animPics[1]);
        } else if (offset > 27.5 & offset <= 41.25) {
            mDragView.setBackgroundResource(animPics[2]);
        } else if (offset > 41.25 & offset <= 55) {
            mDragView.setBackgroundResource(animPics[3]);
        } else if (offset > 55 & offset <= 68.75) {
            mDragView.setBackgroundResource(animPics[4]);
        } else if (offset > 68.75 & offset <= 82.5) {
            mDragView.setBackgroundResource(animPics[5]);
        } else if (offset > 82.5 & offset <= 96.25) {
            mDragView.setBackgroundResource(animPics[6]);
        } else if (offset > 96.25 & offset <= 110) {
            mDragView.setBackgroundResource(animPics[7]);
        } else if (offset > 110 & offset <= 123.75) {
            mDragView.setBackgroundResource(animPics[8]);
        } else if (offset > 123.75 & offset <= 137.5) {
            mDragView.setBackgroundResource(animPics[9]);
        } else if (offset > 137.5 & offset <= 151.25) {
            mDragView.setBackgroundResource(animPics[10]);
        } else if (offset > 151.25 & offset <= 165) {
            mDragView.setBackgroundResource(animPics[11]);
        } else if (offset > 165 & offset <= 178.75) {
            mDragView.setBackgroundResource(animPics[12]);
        } else if (offset > 178.75 & offset <= 192.5) {
            mDragView.setBackgroundResource(animPics[13]);
        } else if (offset > 192.5 & offset <= 206.25) {
            mDragView.setBackgroundResource(animPics[14]);
        } else if (offset > 206.25) {
            mDragView.setBackgroundResource(animPics[15]);
        }
    }

    @Override
    public void onReleasing(float percent, int offset, int headHeight, int extendHeight) {
        if (offset == 0) {
            if (mAnimation != null) {
                mAnimation.stop();
            }
            mProgressView.setVisibility(GONE);
        }
    }

    @Override
    public void onStartAnimator(RefreshLayout layout, int headHeight, int extendHeight) {
        if (mAnimation != null) {
            mAnimation.start();
        }
    }

    @Override
    public int onFinish(RefreshLayout layout, boolean success) {


        return 500;//延迟500毫秒之后再弹回
    }

    @Override
    public void setPrimaryColors(int... colors) {
        if (colors.length > 1) {
            if (!(getBackground() instanceof BitmapDrawable)) {
                setBackgroundColor(mBackgroundColor = colors[0]);
                if (mRefreshKernel != null) {
                    mRefreshKernel.requestDrawBackgoundForHeader(colors[0]);
                }
            }


        } else if (colors.length > 0) {
            if (!(getBackground() instanceof BitmapDrawable)) {
                setBackgroundColor(mBackgroundColor = colors[0]);
                if (mRefreshKernel != null) {
                    mRefreshKernel.requestDrawBackgoundForHeader(colors[0]);
                }
            }
        }
    }

    @NonNull
    public View getView() {
        return this;
    }

    @Override
    public SpinnerStyle getSpinnerStyle() {
        return mSpinnerStyle;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch (newState) {
            case None:
//                restoreRefreshLayoutBackground();
            case PullDownToRefresh:
                mDragView.setVisibility(VISIBLE);
                mProgressView.setVisibility(GONE);
                break;
            case Refreshing:
                mProgressView.setVisibility(VISIBLE);
                mDragView.setVisibility(GONE);
                break;
            case ReleaseToRefresh:
//                replaceRefreshLayoutBackground(refreshLayout);
                break;
            case Loading:
                mDragView.setVisibility(GONE);
                mProgressView.setVisibility(GONE);
                break;
        }
    }
    //</editor-fold>

    //<editor-fold desc="background">
//    private Runnable restoreRunable;
//    private void restoreRefreshLayoutBackground() {
//        if (restoreRunable != null) {
//            restoreRunable.run();
//            restoreRunable = null;
//        }
//    }
//
//    private void replaceRefreshLayoutBackground(final RefreshLayout refreshLayout) {
//        if (restoreRunable == null && mSpinnerStyle == SpinnerStyle.FixedBehind) {
//            restoreRunable = new Runnable() {
//                Drawable drawable = refreshLayout.getLayout().getBackground();
//                @Override
//                public void run() {
//                    refreshLayout.getLayout().setBackgroundDrawable(drawable);
//                }
//            };
//            refreshLayout.getLayout().setBackgroundDrawable(getBackground());
//        }
//    }
    //</editor-fold>

    //<editor-fold desc="API">
    public ClassicsHeader setProgressBitmap(Bitmap bitmap) {

        mProgressView.setImageBitmap(bitmap);
        return this;
    }

    public ClassicsHeader setProgressDrawable(Drawable drawable) {

        mProgressView.setImageDrawable(drawable);
        return this;
    }

    public ClassicsHeader setProgressResource(@DrawableRes int resId) {

        mProgressView.setImageResource(resId);
        return this;
    }

    public ClassicsHeader setArrowBitmap(Bitmap bitmap) {
        mDragView.setImageBitmap(bitmap);
        return this;
    }

    public ClassicsHeader setArrowDrawable(Drawable drawable) {
        mDragView.setImageDrawable(drawable);
        return this;
    }

    public ClassicsHeader setArrowResource(@DrawableRes int resId) {
        mDragView.setImageResource(resId);
        return this;
    }

    public ClassicsHeader setLastUpdateTime(Date time) {
        mLastTime = time;
        if (mShared != null && !isInEditMode()) {
            mShared.edit().putLong(KEY_LAST_UPDATE_TIME, time.getTime()).apply();
        }
        return this;
    }

    public ClassicsHeader setTimeFormat(DateFormat format) {
        return this;
    }

    public ClassicsHeader setSpinnerStyle(SpinnerStyle style) {
        this.mSpinnerStyle = style;
        return this;
    }

    public ClassicsHeader setAccentColor(int accentColor) {
        return this;
    }
    //</editor-fold>

}
