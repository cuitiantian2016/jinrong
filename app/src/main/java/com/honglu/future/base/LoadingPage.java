package com.honglu.future.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honglu.future.R;
import com.scwang.smartrefresh.layout.header.framedrawable.AnimationsContainer;

public class LoadingPage extends FrameLayout {

    private ViewGroup mLoadingLayout;

    private TextView mRefreshBtn;

    private ViewGroup mFailedLayout;
    private ViewGroup mFailedLayoutPic;
    //无网络相关
    private RelativeLayout rel_refresh;//点击刷新
    private TextView tv_net_error;
    private ImageView net_error_iv;

    private TextView mFailedText;



    public LoadingPage(Context context) {
        super(context);
        initViews(context);
    }

    public LoadingPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    private void initViews(Context context) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.common_loading_layout, null);
        this.addView(contentView);
        mLoadingLayout = (ViewGroup) contentView.findViewById(R.id.loading_layout);
        mFailedLayout = (ViewGroup) contentView.findViewById(R.id.failed_layout);
        mFailedLayoutPic = (ViewGroup) contentView.findViewById(R.id.rl_internet_disconnection);
        rel_refresh = (RelativeLayout) contentView.findViewById(R.id.rel_refresh);
        mFailedText = (TextView) contentView.findViewById(R.id.failed_message_text);
        mRefreshBtn = (TextView) contentView.findViewById(R.id.refresh_btn);
        tv_net_error = (TextView) contentView.findViewById(R.id.tv_net_error);
        net_error_iv = (ImageView) contentView.findViewById(R.id.net_error_iv);
        ImageView ivLoading = (ImageView) contentView.findViewById(R.id.iv_loading);

        //if (!PullRefreshAnimHelper.isNewYearStyleType()){
//        }else {
//            //Glide加载Gif
//            Glide.with(getContext()).load(R.mipmap.center_loading).
//                    diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ivLoading);
//        }
        mRefreshBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                loading();
                reload(v.getContext());
            }
        });
        rel_refresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                reload(v.getContext());
            }
        });
        setNetErrorParams();
    }

    public void loading() {
        this.setVisibility(View.VISIBLE);
        mFailedLayout.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.VISIBLE);
        mFailedText.setText("");
    }

    //带图片的网络异常
    public void loadingPic() {
        this.setVisibility(View.VISIBLE);
        mFailedLayoutPic.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.VISIBLE);
    }

    public void failed(String failedMessage) {
        this.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.INVISIBLE);
        mFailedLayout.setVisibility(View.VISIBLE);
        if (failedMessage != null) {
            mFailedText.setText(failedMessage);
        }
    }

    public void failed(int failedStringId) {
        this.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.INVISIBLE);
        mFailedLayout.setVisibility(View.VISIBLE);
        mFailedText.setText(getContext().getString(failedStringId));
    }

    public void failedWithPic() {
        this.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.INVISIBLE);
        mFailedLayout.setVisibility(View.INVISIBLE);
        mFailedLayoutPic.setVisibility(View.VISIBLE);
    }

    public boolean isFailed() {
        return mFailedLayout.getVisibility() == View.VISIBLE;
    }

    protected void reload(Context context) {
    }

    public void setNetErrorParams() {
        tv_net_error.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = tv_net_error.getWidth();
                LinearLayout.LayoutParams llp = (LinearLayout.LayoutParams) rel_refresh.getLayoutParams();
//                llp.width = width - 20;
                rel_refresh.setLayoutParams(llp);
                LinearLayout.LayoutParams llp2 = (LinearLayout.LayoutParams) net_error_iv.getLayoutParams();
                llp2.width = width - 30;
                llp2.height = width - 30;
                net_error_iv.setLayoutParams(llp2);
                tv_net_error.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }
}
