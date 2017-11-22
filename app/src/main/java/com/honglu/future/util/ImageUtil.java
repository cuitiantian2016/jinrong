package com.honglu.future.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.honglu.future.R;

import java.io.File;

public class ImageUtil {

    public static void displayHandImage(String imageUrl, ImageView imageView, Integer errorImgResouce) {
        if (TextUtils.isEmpty(imageUrl)) {
            imageUrl = "http://xiaoniutaojin.jpg";
        }
        if (errorImgResouce == null) {
            errorImgResouce = R.mipmap.img_head;
        }
        Glide.with(imageView.getContext()).load(imageUrl).
                placeholder(errorImgResouce).error(errorImgResouce).
                diskCacheStrategy(DiskCacheStrategy.ALL).
                dontAnimate().into(imageView);
    }


    public static void display(String imageUrl, ImageView imageView, Integer errorImgResouce) {
        if(TextUtils.isEmpty(imageUrl)){
            imageUrl = "http://xiaoniutaojin.jpg";
        }
        if (errorImgResouce == null) {
            errorImgResouce = R.mipmap.img_head;
        }
        if (imageView.getContext()==null){
            return;
        }
        Glide.with(imageView.getContext()).load(imageUrl).asBitmap().centerCrop().
                placeholder(errorImgResouce).error(errorImgResouce).
                diskCacheStrategy(DiskCacheStrategy.ALL).
                dontAnimate().into(imageView);
    }

    public static void display(Fragment context, String imageUrl, ImageView imageView, Integer errorImgResouce) {
        if(TextUtils.isEmpty(imageUrl)){
            imageUrl = "http://xiaoniutaojin.jpg";
        }
        if (errorImgResouce == null) {
            errorImgResouce = R.mipmap.img_head;
        }
        Glide.with(context).load(imageUrl).
                placeholder(errorImgResouce).error(errorImgResouce).
                diskCacheStrategy(DiskCacheStrategy.ALL).
                dontAnimate().into(imageView);
    }

    public static void display(Context context, String imageUrl, ImageView imageView, Integer errorImgResouce) {
        if(TextUtils.isEmpty(imageUrl)){
            imageUrl = "http://xiaoniutaojin.jpg";
        }
        if (errorImgResouce == null) {
            errorImgResouce = R.mipmap.img_head;
        }
        Glide.with(context).load(imageUrl).
                placeholder(errorImgResouce).error(errorImgResouce).
                diskCacheStrategy(DiskCacheStrategy.ALL).
                dontAnimate().into(imageView);
    }


    public static void display(File file, ImageView imageView, Integer errorImgResouce) {
        if (errorImgResouce == null) {
            errorImgResouce = R.mipmap.img_head;
        }
        Glide.with(imageView.getContext()).load(file).
                placeholder(errorImgResouce).error(errorImgResouce).
                diskCacheStrategy(DiskCacheStrategy.ALL).
                dontAnimate().into(imageView);
    }
    public static void display_simple(int resourceId, ImageView imageView) {
        Glide.with(imageView.getContext()).load(resourceId).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }

    public static Bitmap getRoundCornerImage(Bitmap bitmap, int roundPixels) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap roundConcerImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);//创建一个和原始图片一样大小的位图
        Canvas canvas = new Canvas(roundConcerImage);//创建位图画布
        Paint paint = new Paint();//创建画笔

        Rect rect = new Rect(0, 0, width, height);//创建一个和原始图片一样大小的矩形
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);// 抗锯齿

        canvas.drawRoundRect(rectF, roundPixels, roundPixels, paint);//画一个基于前面创建的矩形大小的圆角矩形
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));//设置相交模式
        canvas.drawBitmap(bitmap, null, rect, paint);//把图片画到矩形去
        return Bitmap.createBitmap(roundConcerImage, 0, 0, width, height - roundPixels);
    }

}
