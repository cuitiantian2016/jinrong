package com.honglu.future.ui.main.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cfmmc.app.sjkh.*;
import com.honglu.future.ARouter.DebugActivity;
import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.base.PermissionsListener;
import com.honglu.future.config.Constant;
import com.honglu.future.dialog.AlertFragmentDialog;
import com.honglu.future.events.FragmentRefreshEvent;
import com.honglu.future.events.UIBaseEvent;
import com.honglu.future.http.HttpManager;
import com.honglu.future.ui.main.bean.MoreContentBean;
import com.honglu.future.ui.main.contract.MyContract;
import com.honglu.future.ui.main.presenter.MyPresenter;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.LogUtils;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.StringUtil;
import com.honglu.future.util.ToastUtil;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;


/**
 * 网页加载容器
 * xiejingwen
 */
@Route(path = "/future/webview")
public class WebViewActivity extends BaseActivity<MyPresenter> implements MyContract.View {

    @BindView(R.id.web_view)
    WebView mWebView;
    @BindView(R.id.tv_tag_content)
    TextView mTvTagContent;
    @BindView(R.id.dialog_view)
    LinearLayout mDialogView;
    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;

    private String title;
    private String mUrl;
    private HashMap<String, String> mHashMap;
    private boolean isZhbTitle;
    private OnClickListener onClickListener;
    private Button button;
    private WindowManager mWindowManager;
    private boolean isShow;

    @Override
    public int getLayoutId() {
        return R.layout.activity_future_webview;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        onClickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        };
        mTitle.setTitle(true,onClickListener,"");
        //mTitle.showClose(null);
        initView();
        if (!TextUtils.isEmpty(mUrl)) {
            mUrl = HttpManager.getUrl(mUrl);
            mWebView.loadUrl(mUrl);
        }
    }

    public void initView() {
        mHashMap = new HashMap<>();
        if (getIntent() != null) {
            if (!StringUtil.isBlank(getIntent().getStringExtra("title"))) {
                title = getIntent().getStringExtra("title");
                mTitle.setTitle(true,onClickListener,title);
            }
            if (!StringUtil.isBlank(getIntent().getStringExtra("improveUrl"))) {//该链接是为了提额的改动
                mUrl = getIntent().getStringExtra("improveUrl");
            } else {
                mUrl = getIntent().getStringExtra("url");
            }

            if (App.getConfig().isDebug() && !TextUtils.isEmpty(mUrl)) {
                LogUtils.loge("当前页面链接:" + mUrl);
                String toastInfo = "";
                toastInfo += "当前页面链接:" + mUrl;
                //ToastUtil.showToast(toastInfo);
            }
        }
        mWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        //WebView属性设置！！！
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不适用缓存
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDatabaseEnabled(true);

        //webview在安卓5.0之前默认允许其加载混合网络协议内容
        // 在安卓5.0之后，默认不允许加载http与https混合内容，需要设置webview允许其加载混合网络协议内容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.addJavascriptInterface(new JavaMethod(), "nativeMethod");
        mWebView.setDownloadListener(new MyWebViewDownLoadListener());
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());}

    private void showDialog() {
        mWebView.setVisibility(View.GONE);
        isZhbTitle = true;
        mDialogView.setVisibility(View.VISIBLE);
        mTvTagContent.setText("正在认证中...");
    }

    private void dismissDialog(String message) {
        isZhbTitle = false;
        mDialogView.setVisibility(View.GONE);
        ToastUtil.showToast(message);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!TextUtils.isEmpty(mUrl) && mUrl.contains("repayment/detail")) {
            //通知还款或续期成功
            EventBus.getDefault().post(new FragmentRefreshEvent(UIBaseEvent.EVENT_REPAY_SUCCESS));
        }
        if (mWebView != null) {
            mWebView.clearHistory();
            mWebView.clearCache(true);
            mWebView.destroy();
        }
    }


    @Override
    public void showLoading(String content) {
        //认证支付宝因为已经有了dialog，所以不弹出
        if (mWebView.getVisibility() == View.VISIBLE) {
            App.loadingContent(this, content);
        }
    }

    @Override
    public void stopLoading() {
        App.hideLoading();
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        if (TextUtils.isEmpty(type)) {
            ToastUtil.showToast(msg);
            return;
        }

    }


    @Override
    public void userInfoSuccess(MoreContentBean result) {

    }

    public class JavaMethod {

        /**
         * 跳转开户页面
         */
        @JavascriptInterface
        public void openAccount() {
            showPopupWindow();
            Intent intent = new Intent(WebViewActivity.this, com.cfmmc.app.sjkh.MainActivity.class);
            intent.putExtra("brokerId", "0101");
            intent.putExtra("channel", "@200$088-2");
            intent.putExtra("packName", "com.honglu.future");
            String userMobile = SpUtil.getString(Constant.CACHE_TAG_MOBILE);
            if (!TextUtils.isEmpty(userMobile)) {
                intent.putExtra("mobile", userMobile);
            }
            startActivity(intent);
        }

        /**
         * 保存传入的键值
         *
         * @param key
         * @param text
         */
        @JavascriptInterface
        public void saveText(String key, String text) {
            mHashMap.put(key, text);
        }

        /**
         * 根据键获取值
         *
         * @param key 传入键
         * @return
         */
        @JavascriptInterface
        public String getText(String key) {
            //调用这个方法返回数据
            String result = mHashMap.get(key);
            return result;
        }

        /**
         * 根据键获取值
         *
         * @return
         */
        @JavascriptInterface
        public void reBankCard() {
            //调用这个方法返回数据
            WebViewActivity.this.finish();
        }
        /***************END 支付宝和淘宝等第三方数据抓取和认证操作***************/
        /**
         * 调用改方法去发送短信
         *
         * @param phoneNumber 手机号码
         * @param message     短信内容
         **/
        @JavascriptInterface
        public void sendMessage(String phoneNumber, String message) {
            // 注册广播 发送消息
            //发送短信并且到发送短信页面
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
            intent.putExtra("sms_body", message);
            startActivity(intent);
        }

        /**
         * 跳转到拨号页面，拨打传入的手机号码
         *
         * @param tele PS:暂未使用到
         */
        @JavascriptInterface
        public void callPhoneMethod(final String tele) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, new PermissionsListener() {
                @Override
                public void onGranted() {
                    String mTele = tele.replaceAll("-", "").trim();
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mTele));
                    if (ActivityCompat.checkSelfPermission(WebViewActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(intent);
                }

                @Override
                public void onDenied(List<String> deniedPermissions, boolean isNeverAsk) {

                }
            });

        }

        /**
         * 协议下载(留存)
         */
        @JavascriptInterface
        public void download(final String link) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            intent.addCategory("android.intent.category.DEFAULT");
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWindowManager!=null&&isShow){
            isShow = false;
            mWindowManager.removeView(button);
        }
    }

    /**
     * 显示弹出框
     */
    public void showPopupWindow() {
        isShow = true;
        // 获取WindowManager
        mWindowManager = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        button = new Button(getApplicationContext());
        button.setText("区间号：088");
        button.setTextColor(getResources().getColor(R.color.white));
        button.setBackgroundColor(getResources().getColor(R.color.actionsheet_blue));
        button.setAlpha(0.5f);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        // 类型
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        // WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        // 设置flag
        int flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        // | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 如果设置了WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE，弹出的View收不到Back键的事件
        params.flags = flags;
        // 不设置这个弹出框的透明遮罩显示为黑色
        params.format = PixelFormat.TRANSLUCENT;
        // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
        // 设置 FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
        // 不设置这个flag的话，home页的划屏会有问题
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = DeviceUtils.dip2px(this, 40);
        params.gravity = Gravity.BOTTOM;
        mWindowManager.addView(button, params);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (isZhbTitle) {
            new AlertFragmentDialog.Builder(this)
                    .setContent("返回操作将中断支付宝认证，\n确认要退出吗？")
                    .setLeftBtnText("取消认证")
                    .setRightBtnText("继续认证")
                    .setLeftCallBack(new AlertFragmentDialog.LeftClickCallBack() {
                        @Override
                        public void dialogLeftBtnClick() {
                            finish();
                        }
                    }).build();
        } else {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                finish();
            }
        }
    }


    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    class MyWebViewClient extends WebViewClient {

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Uri url = request.getUrl();
            Log.d("tag","url-->"+url);
            if (url.getScheme().contains("xn")) {
                ARouter.getInstance().build(url)
                        .navigation(WebViewActivity.this, new NavCallback() {
                            @Override
                            public void onArrival(Postcard postcard) {
                                finish();
                            }
                        });
                return true;
            }
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();  // 接受所有网站的证书  解决https拦截问题
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (mProgressBar != null) {
                mProgressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("tag","url-->"+url);
            if (url.startsWith("xn")) {
                ARouter.getInstance().build(Uri.parse(url))
                        .navigation(WebViewActivity.this, new NavCallback() {
                            @Override
                            public void onArrival(Postcard postcard) {
                                finish();
                            }
                        });
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(final WebView view, String url) {
            super.onPageFinished(view, url);
            mUrl = url;
            LogUtils.loge(mUrl);
            if (mProgressBar != null) {
                mProgressBar.setVisibility(View.GONE);
            }
            if (view.canGoBack()) { //如果当前不是初始页面则显示关闭按钮
//                mTitle.showClose(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (isZhbTitle) {
//                            new AlertFragmentDialog.Builder(mActivity)
//                                    .setContent("返回操作将中断支付宝认证，\n确认要退出吗？")
//                                    .setLeftBtnText("取消认证")
//                                    .setRightBtnText("继续认证")
//                                    .setLeftCallBack(new AlertFragmentDialog.LeftClickCallBack() {
//                                        @Override
//                                        public void dialogLeftBtnClick() {
//                                            finish();
//                                        }
//                                    }).build();
//                        } else {
//                            finish();
//                        }
//                    }
//                });
            } else {
                //mTitle.hintClose();
            }
            Log.e("web", "url==" + url);
            //往当前页面插入一段JS
            if (url.contains("https://my.alipay.com/portal/i.htm") || url.contains("https://shanghu.alipay.com/i.htm")) {
                showDialog();
                String js = "var newscript = document.createElement(\"script\");";
                js += "newscript.src=\"" + App.getConfig().GET_ALIPAY_JS + "\";";
                js += "newscript.onload=function(){toDo();};";  //xxx()代表js中某方法
                js += "document.body.appendChild(newscript);";
                view.loadUrl("javascript:" + js);
            }
        }
    }

    class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (mProgressBar != null) {
                mProgressBar.setProgress(newProgress);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            //是否是支付宝认证
            if (!isZhbTitle) {
                //正常情况下，如果没有传入title，我们则使用h5的title。
                // 当是点推送消息进来的时候,getIntent().getStringExtra("title")会拿到推送传入的乱码般的title字符串。
                // 所以如果是点推送消息进来的，并且推送没有设置title,我们直接使用h5 title
                if (TextUtils.isEmpty(getIntent().getStringExtra("title"))) {
                    WebViewActivity.this.title = title;
                    mTitle.setTitle(true, new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isZhbTitle) {
                                new AlertFragmentDialog.Builder(mActivity)
                                        .setContent("返回操作将中断支付宝认证，\n确认要退出吗？")
                                        .setLeftBtnText("取消认证")
                                        .setRightBtnText("继续认证")
                                        .setLeftCallBack(new AlertFragmentDialog.LeftClickCallBack() {
                                            @Override
                                            public void dialogLeftBtnClick() {
                                                finish();
                                            }
                                        }).build();
                            } else {
                                if (mWebView.canGoBack()) {
                                    mWebView.goBack();
                                } else {
                                    finish();
                                }
                            }
                        }
                    }, title);
                    mTitle.setRightTitle("", null);
                }
            } else {
                mTitle.setTitle(true,onClickListener,"认证中");
            }

        }

    }
}
