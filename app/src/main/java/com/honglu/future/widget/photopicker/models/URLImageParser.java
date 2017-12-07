package com.honglu.future.widget.photopicker.models;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;

public class URLImageParser implements Html.ImageGetter {
    TextView mTextView;

    public URLImageParser(TextView textView) {
        this.mTextView = textView;
    }

    @Override
    public Drawable getDrawable(String source) {
        final URLDrawable urlDrawable = new URLDrawable();
        Glide.with(mTextView.getContext()).load(source).into(new Target<GlideDrawable>() {
            @Override
            public void onLoadStarted(Drawable placeholder) {
            }
            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
            }
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                Bitmap loadedImage =drawableToBitmap(resource);
                if (loadedImage != null){
                    urlDrawable.bitmap = loadedImage;
                    urlDrawable.setBounds(0, 0,loadedImage.getWidth(), loadedImage.getHeight());
                    mTextView.invalidate();
                    mTextView.setText(mTextView.getText());
                }
            }
            @Override
            public void onLoadCleared(Drawable placeholder) {
            }
            @Override
            public void getSize(SizeReadyCallback cb) {
            }
            @Override
            public void setRequest(Request request) {
            }
            @Override
            public Request getRequest() {
                return null;
            }
            @Override
            public void onStart() {
            }
            @Override
            public void onStop() {
            }
            @Override
            public void onDestroy() {
            }
        });
        return urlDrawable;
    }


    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

}
