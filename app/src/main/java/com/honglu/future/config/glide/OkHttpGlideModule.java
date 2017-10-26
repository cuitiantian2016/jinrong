package com.honglu.future.config.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import com.honglu.future.http.glidehttp.SSLSocketFactoryUtils;
import com.honglu.future.http.glidehttp.TrustAllHostnameVerifier;


import java.io.InputStream;

import okhttp3.OkHttpClient;

/**
 * Created by zhuaibing on 2017/5/5
 */

public class OkHttpGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // Do nothing.
    }
    @Override
    public void registerComponents(Context context, Glide glide) {
        OkHttpClient okHttpClient= new OkHttpClient.Builder()
                .sslSocketFactory(SSLSocketFactoryUtils.createSSLSocketFactory()) //信任所有证书
                .hostnameVerifier(new TrustAllHostnameVerifier()) //信任所有证书
                .build();
        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(okHttpClient));
    }
}
