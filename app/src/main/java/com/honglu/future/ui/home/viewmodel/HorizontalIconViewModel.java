package com.honglu.future.ui.home.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.cfmmc.app.sjkh.MainActivity;
import com.honglu.future.R;
import com.honglu.future.base.BasePresenter;
import com.honglu.future.base.IBaseView;
import com.honglu.future.config.Constant;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.home.bean.HomeIcon;
import com.honglu.future.ui.usercenter.activity.KeFuActivity;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.util.SpUtil;

import java.util.List;


/**
 * Created by hefei on 2017/6/5.
 * 横向的icon显示样子为首页的第二个条目的布局
 */

public class HorizontalIconViewModel extends IBaseView<List<HomeIcon>> {

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
            mBannerPresenter = new BasePresenter<IBaseView<List<HomeIcon>>>(this){
                @Override
                public void getData() {
                    super.getData();
                    toSubscribe(HttpManager.getApi().getHomeIcon(), new HttpSubscriber<List<HomeIcon>>() {
                        @Override
                        protected void _onNext(List<HomeIcon> o) {
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
    public void bindData(final List<HomeIcon> homeIcon) {
        if (homeIcon == null) {
            return;
        }
        if (homeIcon.size()<=0) {
            return;
        }
        mView.setVisibility(View.VISIBLE);
        mLine_icons.removeAllViews();
        HorizontalScrollView.LayoutParams hlp = (HorizontalScrollView.LayoutParams) mLine_icons.getLayoutParams();
        if (homeIcon.size() < 4) {
            hlp.gravity = Gravity.CENTER_HORIZONTAL;
        }
        mLine_icons.setLayoutParams(hlp);
        for (final HomeIcon homeIcons : homeIcon) {
            View rootView = ((Activity)mContext).getLayoutInflater().inflate(R.layout.home_icon_layout, null);
            mLine_icons.addView(rootView);
            ImageView circleImageView = (ImageView) rootView.findViewById(R.id.circleImageView);
            TextView tv_text = (TextView) rootView.findViewById(R.id.tv_text);
            int screenWidth = DeviceUtils.getScreenWidth(mContext);
            LinearLayout.LayoutParams llp2 = (LinearLayout.LayoutParams) rootView.getLayoutParams();
            if (homeIcon.size() < 4) {
                llp2.width =(screenWidth / homeIcon.size());
            } else {
                llp2.width =(screenWidth / 4);
            }
            rootView.setLayoutParams(llp2);
            tv_text.setText(homeIcons.title);
            if (homeIcons.image.endsWith("gif")){
                Glide.with(mContext).load(homeIcons.image).diskCacheStrategy(DiskCacheStrategy.ALL).into(
                        new GlideDrawableImageViewTarget(circleImageView)) ;
            }else {
                ImageUtil.display(homeIcons.image, circleImageView, R.mipmap.user_avatar);
            }
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (homeIcons.title.equals("立即开户")){
                        goOpenAccount();
                    }else if (homeIcons.title.equals("新手教程")){
                        //
                    }else if (homeIcons.title.equals("主力合约")){
                        //
                    }else if (homeIcons.title.equals("咨询客服")){
                        mContext.startActivity(new Intent(mContext,KeFuActivity.class));
                    }
                }
            });
        }
    }
    /**
     * 立即开户
     */
    private void goOpenAccount() {
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.putExtra("brokerId", "0101");
        String userMobile = SpUtil.getString(Constant.CACHE_TAG_MOBILE);
        if (!TextUtils.isEmpty(userMobile)) {
            intent.putExtra("mobile", userMobile);
        }
        mContext.startActivity(intent);
    }
}
