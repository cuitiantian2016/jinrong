package com.platform.authorize;

import java.util.Map;

import android.app.Activity;
import android.content.Intent;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * @ClassName: AuthorizeUtil
 * @Description:(各个平台的授权操作)
 * @author chengbo
 * @date 2015-5-7 上午11:17:54
 */
public class AuthorizeUtil {
	
	private UMShareAPI mShareAPI = null;
	private static AuthorizeUtil authorizeUtil;
	private Activity context;

	private AuthorizeUtil(Activity context) {
		this.context = context;
		mShareAPI = UMShareAPI.get(context);
	}

	public static AuthorizeUtil getAuthorizeUtil(Activity context) {
		if (authorizeUtil == null) {
			authorizeUtil = new AuthorizeUtil(context);
		}
		return authorizeUtil;
	}
	
	public void doOauthVerify(SHARE_MEDIA platform,UMAuthListener umAuthListener){
		mShareAPI.doOauthVerify(context, platform, umAuthListener);
	}
	
	public void delOauthVerify(SHARE_MEDIA platform,UMAuthListener umAuthListener){
		mShareAPI.deleteOauth(context, platform, umAuthListener);
	}
	
    public interface AuthorizeSuccessListener {
		void onAuthorizeSuccess(Map<String, Object> maps);
	}
    
    public UMShareAPI getUMShareAPI() {
    	return mShareAPI;
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data){
    	mShareAPI.onActivityResult(requestCode, resultCode, data);
    }
    
    public boolean isInstall(SHARE_MEDIA platform){
    	return mShareAPI.isInstall(context, platform);
    }
}
