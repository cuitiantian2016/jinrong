package com.honglu.future.http.glidehttp;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Created by zhuaibing on 2017/4/24
 */

public class TrustAllHostnameVerifier implements HostnameVerifier{

    @Override
    public boolean verify(String hostname, SSLSession session) {
        //信任所有证书（true）
        return true;
    }
}
