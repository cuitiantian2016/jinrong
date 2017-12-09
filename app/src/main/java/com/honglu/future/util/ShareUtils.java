package com.honglu.future.util;

import android.app.Activity;
import android.text.TextUtils;

import com.honglu.future.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;


/**
 * Created by zq on 2017/12/9.
 */

public class ShareUtils {
    private static final String TAG = "ShareUtils";
    public static ShareUtils mShareUtils;

    public static ShareUtils getIntance() {
        if (mShareUtils == null)
            mShareUtils = new ShareUtils();
        return mShareUtils;
    }

    private ShareUtils() {
    }

    public void share(final Activity activity, String picurl, String linkurl, String title, String content) {
        UMWeb web = new UMWeb(linkurl);//分享链接
        web.setTitle(title);//标题
        if (!TextUtils.isEmpty(picurl)) {
            web.setThumb(new UMImage(activity, picurl));  //缩略图
        } else {
            web.setThumb(new UMImage(activity, R.mipmap.ic_logos));  //缩略图
        }
        web.setDescription(content);//描述
        new ShareAction(activity).withMedia(web)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                        if (share_media == SHARE_MEDIA.WEIXIN || share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
                            //  ToastUtils.showShort("跳转微信分享");
                        } else if (share_media == SHARE_MEDIA.QQ || share_media == SHARE_MEDIA.QZONE) {
                            // ToastUtils.showShort("跳转QQ分享");
                        }
                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        // TODO: 2017/12/9 分享成功通知服务器
                        ToastUtil.show("分享成功");
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                        if (share_media == SHARE_MEDIA.WEIXIN || share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
                            ToastUtil.show("没有安装微信，请先安装应用");
                        } else if (share_media == SHARE_MEDIA.QQ || share_media == SHARE_MEDIA.QZONE) {
                            ToastUtil.show("没有安装QQ，请先安装应用");
                        }
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        ToastUtil.show("已取消");
                    }
                }).open();
    }

}
