package com.platform.util;

import com.umeng.socialize.PlatformConfig;

public class InitUM {
	public static void initUM(){
        //微信    
        PlatformConfig.setWeixin("", "");
        //新浪微博
        PlatformConfig.setSinaWeibo("", "");
        //QQ
        PlatformConfig.setQQZone("", "");
    }
}
