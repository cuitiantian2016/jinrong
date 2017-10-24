package com.platform.share;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import com.platformlibs.R;
import com.platform.share.bean.ShareBean;
import com.platform.share.bean.ShareImageBean;
import com.platform.share.bean.ShareMusicBean;
import com.platform.share.bean.ShareVideoBean;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

public class ShareUtil {
	private ShareAction shareAction = null;
	private static ShareUtil shareUtil;
	private Activity context;

	private ShareUtil(Activity context) {
		this.context = context;
		shareAction = new ShareAction(context);
	}

	public static ShareUtil getShareUtil(Activity context) {
		if (shareUtil == null) {
			shareUtil = new ShareUtil(context);
		}
		return shareUtil;
	}
	
	public void doShare(SHARE_MEDIA platform, ShareBean shareBean, UMShareListener umShareListener){
		shareAction.setPlatform(platform).setCallback(umShareListener);
		if(shareBean.getTitle() != null){
			shareAction.withTitle(shareBean.getTitle());
		}
		if(shareBean.getText() != null){
			shareAction.withText(shareBean.getText());
		}
		if(shareBean.getTargetUrl() != null){
			shareAction.withTargetUrl(shareBean.getTargetUrl());
		}
		if(shareBean instanceof ShareImageBean){
			shareAction.withMedia(getUMImage(context, ((ShareImageBean) shareBean).getImage())).share();
		}
		if(shareBean instanceof ShareMusicBean){
			shareAction.withMedia(((ShareMusicBean) shareBean).getMusic()).share();
		}
		if(shareBean instanceof ShareVideoBean){
			shareAction.withMedia(((ShareVideoBean) shareBean).getVideo()).share();
		}
	}
	
	private UMImage getUMImage(Context context, Object image) {
		UMImage umImage = null;
		if (image != null && !"".equals(image)) {
			if (image instanceof Bitmap) {
				umImage = new UMImage(context, (Bitmap) image);
			} else if (image instanceof String) {
				umImage = new UMImage(context, (String) image);
			} else if (image instanceof File) {
				umImage = new UMImage(context, (File) image);
			} else if (image instanceof Integer) {
				umImage = new UMImage(context, (Integer) image);
			}
		} else {
			umImage = new UMImage(context, R.drawable.ic_logo);
		}
		return umImage;
	}
}
