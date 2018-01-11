//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cfmmc.app.sjkh.ui;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface.OnClickListener;
import android.net.http.SslError;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.cfmmc.app.sjkh.MainActivity;
import com.cfmmc.app.sjkh.common.a;
import com.cfmmc.app.sjkh.ui.l;
import com.cfmmc.app.sjkh.ui.m;
import com.cfmmc.app.sjkh.ui.n;
import com.honglu.future.KaiHuActivity;
import com.honglu.future.config.Constant;
import com.honglu.future.util.SpUtil;

public  class k extends WebViewClient {

    private static final String TAG = "khkhkhkhkhkhkhkhkh";
    private MainActivity b;
    private String c;
    private WebView d;
    OnClickListener a = new l(this);

    public k(MainActivity var1) {
        this.b = var1;
    }

    public final void onReceivedSslError(WebView var1, SslErrorHandler var2, SslError var3) {
        Builder var4;
        (var4 = new Builder(this.b)).setMessage("服务器证书错误或已过期，请联系开户机构确认，是否忽略此问题继续开户（建议咨询开户机构意见）？");
        var4.setTitle("警告");
        var4.setPositiveButton("继续", new m(this, var2));
        var4.setNegativeButton("退出", new n(this));
        var4.create().show();
    }

    public final void onReceivedError(WebView var1, int var2, String var3, String var4) {
        this.c = var1.getOriginalUrl();
        this.d = var1;
        this.b.dismissProgressDialog();
        Message var5;
        (var5 = new Message()).what = 404;
        var5.obj = this.c;
        this.b.cwjHandler.sendMessage(var5);
        var1.loadUrl("file:///android_asset/index.html");
        if (var2 == -2){
            this.b.showProgressDialog("正在加载，请稍等..");
            var1.reload();
        }else if (var2 == -1){
            this.b.finish();
        }
    }

    public final void onReceivedHttpAuthRequest(WebView var1, HttpAuthHandler var2, String var3, String var4) {
        System.out.println(">>>>>>>>>>>>>>> host >>>>>>>>>>>>>>>>>> " + var3);
        super.onReceivedHttpAuthRequest(var1, var2, var3, var4);
    }

    public final boolean shouldOverrideUrlLoading(WebView var1, String var2) {
        Log.d(TAG, "shouldOverrideUrlLoading: "+ var2);
        Toast.makeText(this.b, "url is " + var2, Toast.LENGTH_LONG).show();
        return super.shouldOverrideUrlLoading(var1, var2);
    }

    public final void onPageFinished(WebView var1, String var2) {
        Log.d(TAG, "onPageFinished: "+ var2);
        System.out.println(" >>> page finished...");
        this.b.dismissProgressDialog();
        var1.requestFocus();
        this.b.refreshMainView();
        super.onPageFinished(var1, var2);
    }

    @Override
    public void onLoadResource(final WebView view, String url) {
        Log.d(TAG, "onLoadResource:3145 088 "+ url);
        super.onLoadResource(view, url);
        if (!TextUtils.isEmpty(url)&&url.startsWith("https://appficaos.cfmmc.com/userProfile.do")){
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.loadUrl("javascript:(function(){"  +"var id = document.getElementsByName(\"val1\")[0];"+"var my_company = document.title;"+
                            "var id2 =document.getElementsByName(\"val2\")[0];"+"console.log(id);"+"console.log(id2);"+"console.log(my_company);"+
                            "if(id){if(my_company==\"美尔雅期货\"){id.value = \"上海翡鹿\";id.readOnly= true;}else if(my_company==\"国富期货\"){id.value = \"国富期货大连分公司\";id.readOnly= true;}};"+"if(id2){if(my_company==\"美尔雅期货\"){id2.value = \"3145\";id2.readOnly= true;}else if(my_company==\"国富期货\"){id2.value = \"088\";id2.readOnly= true;}};"+
                            "})()");
                }
            },500);
        }

    }
}
