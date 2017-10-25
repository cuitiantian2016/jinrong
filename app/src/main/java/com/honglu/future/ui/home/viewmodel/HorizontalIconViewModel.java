package com.honglu.future.ui.home.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.honglu.future.R;
import com.honglu.future.base.BasePresenter;
import com.honglu.future.base.IBaseView;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.home.bean.HomeIcon;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.ImageUtil;


/**
 * Created by hefei on 2017/6/5.
 * 横向的icon显示样子为首页的第二个条目的布局
 */

public class HorizontalIconViewModel extends IBaseView<HomeIcon> {

    private LinearLayout mLine_icons;
    public View mView;
    private Context mContext;
    private BasePresenter mBannerPresenter;
    public HorizontalIconViewModel(Context context) {
        mContext = context;
        mView = View.inflate(context, R.layout.home_item1, null);
        mLine_icons = (LinearLayout)mView.findViewById(R.id.line_icons);
        refreshData();
    }
    /**
     * 刷新数据
     */
    private void refreshData() {
        if (mBannerPresenter == null){
            mBannerPresenter = new BasePresenter<IBaseView<HomeIcon>>(this){
                @Override
                public void getData() {
                    super.getData();
                    toSubscribe(HttpManager.getApi().getHomeIcon(), new HttpSubscriber<HomeIcon>() {
                        @Override
                        protected void _onNext(HomeIcon o) {
                            mView.bindData(o);
                        }
                    });
                }
            };
        }
        mBannerPresenter.getData();
    }
    /**
     * 首页图标
     * @param homeIcon
     */
    @Override
    public void bindData(HomeIcon homeIcon) {
        if (homeIcon == null) {
            return;
        }
        if (homeIcon.getData() == null) {
            return;
        }
        if (homeIcon.getData().getData() == null || homeIcon.getData().getData().size()<=0) {
            return;
        }
        mView.setVisibility(View.VISIBLE);
        mLine_icons.removeAllViews();

        HorizontalScrollView.LayoutParams hlp = (HorizontalScrollView.LayoutParams) mLine_icons.getLayoutParams();
        if (homeIcon.getData().getData().size() < 4) {
            hlp.gravity = Gravity.CENTER_HORIZONTAL;
        }
        mLine_icons.setLayoutParams(hlp);
        for (final HomeIcon.DataBeanX.DataBean homeIcons : homeIcon.getData().getData()) {
            View frameview = ((Activity)mContext).getLayoutInflater().inflate(R.layout.home_icon_layout, null);
            mLine_icons.addView(frameview);
            ImageView circleImageView = (ImageView) frameview.findViewById(R.id.circleImageView);
            TextView tv_text = (TextView) frameview.findViewById(R.id.tv_text);
            int layoutWidth = 0;
            int screenWidth = DeviceUtils.getScreenWidth(mContext);
            if (homeIcon.getData().getData().size() < 4) {
                layoutWidth = (int) (screenWidth / 4);
            } else {
                layoutWidth = (int) (screenWidth / 4);
            }
            LinearLayout.LayoutParams llp2 = (LinearLayout.LayoutParams) frameview.getLayoutParams();
            if (homeIcon.getData().getData().size() < 4) {
                llp2.width = (int) (screenWidth / homeIcon.getData().getData().size());
            } else {
                llp2.width = (int) (screenWidth / 4);
            }
            frameview.setLayoutParams(llp2);
            tv_text.setText("" + homeIcons.getTitle());
            if (homeIcons.getImage().endsWith("gif")){
                Glide.with(mContext).load(homeIcons.getImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(new GlideDrawableImageViewTarget(circleImageView)) ;
            }else {
                ImageUtil.display(homeIcons.getImage(), circleImageView, R.mipmap.other_empty);
            }
            frameview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
