package com.honglu.future.ARouter.service;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.PathReplaceService;
import com.honglu.future.config.Constant;
import com.honglu.future.util.SpUtil;

/**
 * Used for json converter
 *
 * @author hefei
 *
 * 替换uri
 */
@Route(path = "/service/replace")
public class PathReplaceServiceImpl implements PathReplaceService {
    @Override
    public void init(Context context) {
    }
    @Override
    public String forString(String path) {
        return path;
    }

    @Override
    public Uri forUri(Uri uri) {
        String isLogin = uri.getQueryParameter("isLogin");
        if (Boolean.parseBoolean(isLogin)){
            String userID = SpUtil.getString(Constant.CACHE_TAG_UID);
            if (TextUtils.isEmpty(userID)){
                String url = "xiaoniuqihuo://future/future/login?redirect"+"=" + Uri.encode(uri.toString());
                return Uri.parse(url);
            }
        }
        return uri;
    }
}
