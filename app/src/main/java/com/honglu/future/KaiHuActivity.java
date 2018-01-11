package com.honglu.future;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.cfmmc.app.sjkh.MainActivity;
import com.cfmmc.app.sjkh.ui.k;

public class KaiHuActivity extends MainActivity {
    public static final String TAG = "KaiHuActivity";
    WebView webView;

    @Override
    public void initWebView(WebView webView) {
        super.initWebView(webView);
        this.webView = webView;
        webView.setWebViewClient(new k(this));

    }

    @Override
    public void refreshMainView() {
        super.refreshMainView();
        Log.d(TAG, "refreshMainView: ");
    }
}
