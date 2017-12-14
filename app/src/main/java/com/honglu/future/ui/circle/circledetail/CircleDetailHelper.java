package com.honglu.future.ui.circle.circledetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.honglu.future.R;
import com.honglu.future.ui.circle.bean.PraiseListBean;
import com.honglu.future.ui.circle.praisesandreward.RewardDetailActivity;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.widget.CircleImageView;
import com.honglu.future.widget.photo.FullScreenDisplayActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuaibing on 2017/12/14
 */

public class CircleDetailHelper {
    private Context mContext;
    private InputMethodManager mInputMethodManager;

    public CircleDetailHelper(Context context){
        this.mContext = context;
        this.mInputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public void updateUserHead(LinearLayout mSupportLinear , String attention , String circleId, List<PraiseListBean> headList){
        mSupportLinear.setVisibility(View.VISIBLE);
        mSupportLinear.removeAllViews();

        int size = mContext.getResources().getDimensionPixelSize(R.dimen.dimen_30dp);
        if (headList != null && headList.size() > 0) {
            for (int i = 0; i < headList.size(); i++) {
                if (i == 4) {
                    break;
                }
                PraiseListBean praiseBean = headList.get(i);
                CircleImageView imgHead = new CircleImageView(mContext);
                ImageUtil.display(praiseBean.avatarPic, imgHead, R.mipmap.img_head);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
                params.rightMargin = size / 2;
                mSupportLinear.addView(imgHead, params);
            }
        }

        final String mAttention = attention;
        final String mCircleId = circleId;
        CircleImageView imgHead = new CircleImageView(mContext);
        imgHead.setImageResource(R.mipmap.ic_more);
        imgHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mAttention)){
                    RewardDetailActivity.startRewardDetailActivity(mContext, mCircleId, mAttention);
                }
            }
        });
        mSupportLinear.addView(imgHead, new LinearLayout.LayoutParams(size, size));
    }




    /**
     * 内容中的图片加载
     *
     * @param picList 图片 链接集合
     */
    public void initContentImage(LinearLayout mImgsLinear ,final ArrayList<String> picList) {
        mImgsLinear.setVisibility(View.VISIBLE);
        mImgsLinear.removeAllViews();

        if (picList != null && picList.size() != 0) {
            for (final String imageStr : picList) {
                final ImageView imageView = new ImageView(mContext);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), FullScreenDisplayActivity.class);
                        Bundle b = new Bundle();
                        b.putStringArrayList("image_urls", picList);
                        b.putInt("position", picList.indexOf(imageStr));
                        intent.putExtras(b);
                        int[] location = new int[2];
                        view.getLocationOnScreen(location);
                        intent.putExtra("locationX", location[0]);
                        intent.putExtra("locationY", location[1]);
                        intent.putExtra("width", view.getWidth());
                        intent.putExtra("height", view.getHeight());
                        view.getContext().startActivity(intent);
                    }
                });
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                imageView.setImageResource(R.mipmap.other_empty);
                params.topMargin = DeviceUtils.dip2px(mContext, 4);
                mImgsLinear.addView(imageView);
                final int imageWidth = DeviceUtils.getScreenWidth(mContext) - DeviceUtils.dip2px(mContext, 30);
                String picurls = imageStr;
                Glide.with(mContext).load(picurls).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        int bitmapWidth = resource.getWidth();
                        int bitmapHeight = resource.getHeight();
                        int height = (int) ((float) imageWidth * (float) bitmapHeight / (float) bitmapWidth);
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                        params.width = imageWidth;
                        params.height = height;
                        params.topMargin = DeviceUtils.dip2px(mContext, 4);
                        imageView.setImageBitmap(resource);
                        imageView.requestLayout();
                    }
                });
            }
        }
    }


    public void toggleSoftInput(EditText mInput){
        mInput.setFocusable(true);
        mInput.setFocusableInTouchMode(true);
        mInput.requestFocus();
        mInputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    public void hideSoftInputFromWindow(EditText mInput){
        if (mInputMethodManager.isActive()) {
            IBinder ibinder = mInput.getWindowToken();
            if (ibinder != null) {
                mInputMethodManager.hideSoftInputFromWindow(ibinder, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public String geInputText(EditText mInput) {
        return mInput.getText() != null && !TextUtils.isEmpty(mInput.getText().toString()) ? mInput.getText().toString().trim() : "";
    }

    public void setText(TextView view, String text) {
        if (!TextUtils.isEmpty(text)) {
            view.setText(text);
        } else {
            view.setText("");
        }
    }
}
