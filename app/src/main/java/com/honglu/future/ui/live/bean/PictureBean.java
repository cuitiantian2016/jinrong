package com.honglu.future.ui.live.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by zq on 2017/12/28.
 */

public class PictureBean {
    private String key;
    private Drawable image;

    public PictureBean(String key, Drawable image) {
        this.image = image;
        this.key = key;
    }

    public String getKey()

    {
        return key;
    }

    public Drawable getImage() {
        return image;
    }

}
