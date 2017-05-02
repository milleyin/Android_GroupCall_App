package com.afmobi.palmchat.ui.activity.publicaccounts;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Properties;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.social.FunnyClubActivity;
import com.core.AfFriendInfo;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.google.gson.JsonObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Proxy;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebHistoryItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.HttpHost;

/**
 * 公众账号、广播、banner条 URL跳转过滤
 *
 * @author starw
 */
public class InnerNoAbBrowserActivity extends BaseActivity implements OnClickListener {
    /**
     * tag
     */
    private final String TAG = InnerNoAbBrowserActivity.class.getSimpleName();
    /**
     * webview
     */
    private WebView mWebView;
    /**
     * URL
     */
    private String mReSourceUrl;
    /* 初始化的目的地址 该值不能改变*/
    private String mInitReSourceUrl;
    /**
     * 标题
     */
    private TextView mTV_Title;
    /**
     * 进度条
     */
    private ProgressBar mProgressBar;
    /**
     * 不需要服务器做跳转
     */
    private boolean mBL_NOTSKIP;
    /*Filter 类型 对应不同的过滤地址*/
    private int filterType;
    /**
     * 保存当前titile与url对应
     */
    private HashMap<String, String> mHashMap = new HashMap<String, String>();
    /*正在加载页面时显示*/
    private ViewGroup rl_processing;
    /*加载页面失败后显示*/
    private ViewGroup rl_error;
    /*重新加载按钮*/
    private Button btn_retry;
    /*是否当前是错误页*/
    private boolean isWapError = false;

    /***/
    private WebChromeClient mWebCClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            mTV_Title.setText(title);
            mHashMap.put(mReSourceUrl, title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (100 == newProgress || 0 == newProgress) {
                mProgressBar.setVisibility(View.GONE);
            } else {
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(newProgress);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void findViews() {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_officialaccounts_messagedetails);
        mWebView = (WebView) findViewById(R.id.messagedetails_webview);

        findViewById(R.id.back_button).setOnClickListener(this);

        mTV_Title = (TextView) findViewById(R.id.title_text);
        mProgressBar = (ProgressBar) findViewById(R.id.messagedetails_progress_id);

        rl_processing = (ViewGroup) findViewById(R.id.rl_processing);
        rl_error = (ViewGroup) findViewById(R.id.rl_error);
        btn_retry = (Button) findViewById(R.id.btn_retry);

        btn_retry.setOnClickListener(this);
    }

    private final String MARKET_URL_HEAD = "market://details?id=";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void init() {
        // TODO Auto-generated method stub
        mReSourceUrl = getIntent().getStringExtra(IntentConstant.RESOURCEURL);
        mBL_NOTSKIP = getIntent().getBooleanExtra(IntentConstant.ISNOTNEEDSKIP, false);
        filterType=  getIntent().getIntExtra(IntentConstant.FILTER_URL_TYPE, DefaultValueConstant.FILTER_RUL_NORMAL);//过滤和统计的服务器类型

        if (jumpToMarket()) {//是否要跳市场过外部浏览器
            return;
        }

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(mWebCClient);
        mWebView.setWebViewClient(new DetailsWebViewClicent());
        PalmchatLogUtils.i(TAG, "---mReSourceUrl----" + mReSourceUrl);
        if (Constants.USE_PROXY) {
            setProxy(mWebView, Constants.PROXY_URL,Constants.PROXY_PORT, TAG);
        }
        if (!(mReSourceUrl.indexOf(JsonConstant.HTTP_HEAD) == 0 || (mReSourceUrl.indexOf(JsonConstant.HTTPS_HEAD) == 0))) {
           mReSourceUrl = JsonConstant.HTTP_HEAD + mReSourceUrl;
        }
        mInitReSourceUrl=mReSourceUrl;//记录初始的url给retry用
        loadInWebView();
    }

    /**
     * add by zhh 2016-08-26
     * 内置浏览器打开（提出来方便公用，如果加载失败，需提供重新retry加载按钮）
     */
    private void loadInWebView() {
        if (!TextUtils.isEmpty(mReSourceUrl)) {
            if (mBL_NOTSKIP) {
                mWebView.loadUrl(mReSourceUrl);
            } else {
                //先提交到我们自己的服务器上 让服务器过滤URL，并且统计提交的用户参数打点。然后再重定向。
                String postData = doWithPostParams();
                PalmchatLogUtils.i(TAG, DefaultValueConstant.URL_WEBSKIPTEST + "--postdata====" + postData.toString());
                mWebView.postUrl(PalmchatApp.getApplication().getWebSkipUrl(filterType), postData.getBytes());
            }
        }
     }

    /**
     * 5.4需求 第三方合作的应用商 会跳转
     *
     * @return
     */
    private boolean jumpToMarket() {
        if (!TextUtils.isEmpty(mReSourceUrl)) {
            if (mReSourceUrl.endsWith(".apk")) {//5.4需求，如果是.apk结尾的跳外部浏览器
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri content_url = Uri.parse(mReSourceUrl);
                intent.setData(content_url);
                startActivity(intent);
                finish();//关闭当前这个过滤跳转用的Activity
                return true;
            } else if (mReSourceUrl.startsWith(MARKET_URL_HEAD)) {//是Google市场的应用
                //这里开始执行一个应用市场跳转逻辑，
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(mReSourceUrl)); //跳转到应用市场，非Google Play市场一般情况也实现了这个接口
//存在手机里没安装应用市场的情况，跳转会包异常，做一个接收判断
                if (intent.resolveActivity(getPackageManager()) != null) { //可以接收
                    startActivity(intent);
                } else { //没有应用市场，我们通过浏览器跳转到Google Play
                    String packageName = mReSourceUrl.substring(MARKET_URL_HEAD.length());
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
                    //这里存在一个极端情况就是有些用户浏览器也没有，再判断一次
                    if (intent.resolveActivity(getPackageManager()) != null) { //有浏览器
                        startActivity(intent);
                    } else {
                        // 浏览器也没
                    }
                }
                finish();//关闭当前这个过滤跳转用的Activity
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            //返回按钮
            case R.id.back_button: {
                finish();
                break;
            }
            case R.id.btn_retry:
                rl_error.setVisibility(View.GONE);
                //重新加载wap页面
                mReSourceUrl= mInitReSourceUrl;
                loadInWebView();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            if (!mBL_NOTSKIP) {
                WebBackForwardList backlist= mWebView.copyBackForwardList();
                if(backlist!=null) {
                    String hisUrl = null;
                    for (int i = backlist.getSize() - 1, count = 0; i >= 0; --i, count++) {
                        hisUrl = backlist.getItemAtIndex(i).getUrl();
                        //如果goback列表里 有是过滤页的就退出，用户失败重试后成功的情况
                        if (count < 2 && hisUrl != null && hisUrl.equals(PalmchatApp.getApplication().getWebSkipUrl(filterType))) {
                            return super.onKeyDown(keyCode, event);
                        }
                    }
                }


            }
            mWebView.goBack();
            mHashMap.remove(mReSourceUrl);
            return true;

        }
        return super.onKeyDown(keyCode, event);

    }

    /**
     * 处理post请求参数
     *
     * @return
     */
    private String doWithPostParams() {
        String postDate = "url=" + Base64.encodeToString(mReSourceUrl.getBytes(), Base64.NO_WRAP);//"afid":"a190011", "country":"china", "state":"Guangdong", "sex":"M"
        AfProfileInfo myProfile = CacheManager.getInstance().getMyProfile();
        if (myProfile != null) {
            JsonObject jo = new JsonObject();
            jo.addProperty("afid", myProfile.afId);
            jo.addProperty("country", myProfile.country);
            jo.addProperty("state", myProfile.region);
            jo.addProperty("sex", myProfile.sex == Consts.AFMOBI_SEX_FEMALE ? AfFriendInfo.FEMALE : AfFriendInfo.MALE);
            String joStr = jo.toString();
            postDate += "&data=" + Base64.encodeToString(joStr.getBytes(), Base64.NO_WRAP);
        }
        return postDate;
    }

    /**
     * WebViewClicent
     *
     * @author Transsion
     */
    public class DetailsWebViewClicent extends WebViewClient {
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            mWebView.setVisibility(View.GONE);
            rl_error.setVisibility(View.VISIBLE);
            isWapError = true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            if (Constants.DISABLE_SSL_CHECK_FOR_TESTING) {
                handler.proceed();
            } else {
                super.onReceivedSslError(view, handler, error);
                handler.cancel();
            }

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            if (jumpToMarket()) {//是否要跳市场过外部浏览器
                return true;
            }
            PalmchatLogUtils.e("WXL","shouldOverrideUrlLoading url="+url);
            view.setWebChromeClient(mWebCClient);
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            mTV_Title.setText(mHashMap.get(url));
            rl_processing.setVisibility(View.GONE);
            if (!isWapError) {
                mWebView.setVisibility(View.VISIBLE);
            }
            isWapError = false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            mReSourceUrl = url;
            rl_processing.setVisibility(View.VISIBLE);
            rl_error.setVisibility(View.GONE);
            super.onPageStarted(view, url, favicon);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mWebView.getClass().getMethod("onPause").invoke(mWebView, (Object[]) null);
        } catch (Exception e) {
            PalmchatLogUtils.e(TAG, e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mWebView.getClass().getMethod("onResume").invoke(mWebView, (Object[]) null);
        } catch (Exception e) {
            PalmchatLogUtils.e(TAG, e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (Constants.USE_PROXY) {
            revertBackProxy(mWebView, TAG);
        }
        mHashMap.clear();
    }


    //------------------------------webView代理
    private static String LOG_TAG = "WEBVIEW_PROXY";

    // 注意这里applicationClassName 传递的是 application 的类名
    public static boolean setProxy(WebView webview, String host, int port, String applicationClassName) {
        // 3.2 (HC) or lower
        if (Build.VERSION.SDK_INT <= 13) {
            return setProxyUpToHC(webview, host, port);
        }
        // ICS: 4.0
        else if (Build.VERSION.SDK_INT <= 15) {
            return setProxyICS(webview, host, port);
        }
        // 4.1-4.3 (JB)
        else if (Build.VERSION.SDK_INT <= 18) {
            return setProxyJB(webview, host, port);
        }
        // 4.4 (KK) & 5.0 (Lollipop)
        else {
            return setProxyKKPlus(webview, host, port, applicationClassName);
        }
    }

    public static boolean revertBackProxy(WebView webview, String applicationClassName) {
        // 3.2 (HC) or lower
        if (Build.VERSION.SDK_INT <= 13) {
            return true;
        }
        // ICS: 4.0
        else if (Build.VERSION.SDK_INT <= 15) {
            return revertProxyICS(webview);
        }
        // 4.1-4.3 (JB)
        else if (Build.VERSION.SDK_INT <= 18) {
            return revertProxyJB(webview);
        }
        // 4.4 (KK) & 5.0 (Lollipop)
        else {
            return revertProxyKKPlus(webview, applicationClassName);
        }
    }

    /**
     * Set Proxy for Android 3.2 and below.
     */
    @SuppressWarnings("all")
    private static boolean setProxyUpToHC(WebView webview, String host, int port) {
        Log.d(LOG_TAG, "Setting proxy with <= 3.2 API.");

        HttpHost proxyServer = new HttpHost(host, port);
        // Getting network
        Class networkClass = null;
        Object network = null;
        try {
            networkClass = Class.forName("android.webkit.Network");
            if (networkClass == null) {
                Log.e(LOG_TAG, "failed to get class for android.webkit.Network");
                return false;
            }
            Method getInstanceMethod = networkClass.getMethod("getInstance", Context.class);
            if (getInstanceMethod == null) {
                Log.e(LOG_TAG, "failed to get getInstance method");
            }
            network = getInstanceMethod.invoke(networkClass, new Object[]{webview.getContext()});
        } catch (Exception ex) {
            Log.e(LOG_TAG, "error getting network: " + ex);
            return false;
        }
        if (network == null) {
            Log.e(LOG_TAG, "error getting network: network is null");
            return false;
        }
        Object requestQueue = null;
        try {
            Field requestQueueField = networkClass
                    .getDeclaredField("mRequestQueue");
            requestQueue = getFieldValueSafely(requestQueueField, network);
        } catch (Exception ex) {
            Log.e(LOG_TAG, "error getting field value");
            return false;
        }
        if (requestQueue == null) {
            Log.e(LOG_TAG, "Request queue is null");
            return false;
        }
        Field proxyHostField = null;
        try {
            Class requestQueueClass = Class.forName("android.net.http.RequestQueue");
            proxyHostField = requestQueueClass
                    .getDeclaredField("mProxyHost");
        } catch (Exception ex) {
            Log.e(LOG_TAG, "error getting proxy host field");
            return false;
        }

        boolean temp = proxyHostField.isAccessible();
        try {
            proxyHostField.setAccessible(true);
            proxyHostField.set(requestQueue, proxyServer);
        } catch (Exception ex) {
            Log.e(LOG_TAG, "error setting proxy host");
        } finally {
            proxyHostField.setAccessible(temp);
        }

        Log.d(LOG_TAG, "Setting proxy with <= 3.2 API successful!");
        return true;
    }

    @SuppressWarnings("all")
    private static boolean setProxyICS(WebView webview, String host, int port) {
        try {
            Log.d(LOG_TAG, "Setting proxy with 4.0 API.");

            Class jwcjb = Class.forName("android.webkit.JWebCoreJavaBridge");
            Class params[] = new Class[1];
            params[0] = Class.forName("android.net.ProxyProperties");
            Method updateProxyInstance = jwcjb.getDeclaredMethod("updateProxy", params);

            Class wv = Class.forName("android.webkit.WebView");
            Field mWebViewCoreField = wv.getDeclaredField("mWebViewCore");
            Object mWebViewCoreFieldInstance = getFieldValueSafely(mWebViewCoreField, webview);

            Class wvc = Class.forName("android.webkit.WebViewCore");
            Field mBrowserFrameField = wvc.getDeclaredField("mBrowserFrame");
            Object mBrowserFrame = getFieldValueSafely(mBrowserFrameField, mWebViewCoreFieldInstance);

            Class bf = Class.forName("android.webkit.BrowserFrame");
            Field sJavaBridgeField = bf.getDeclaredField("sJavaBridge");
            Object sJavaBridge = getFieldValueSafely(sJavaBridgeField, mBrowserFrame);

            Class ppclass = Class.forName("android.net.ProxyProperties");
            Class pparams[] = new Class[3];
            pparams[0] = String.class;
            pparams[1] = int.class;
            pparams[2] = String.class;
            Constructor ppcont = ppclass.getConstructor(pparams);

            updateProxyInstance.invoke(sJavaBridge, ppcont.newInstance(host, port, null));

            Log.d(LOG_TAG, "Setting proxy with 4.0 API successful!");
            return true;
        } catch (Exception ex) {
            Log.e(LOG_TAG, "failed to set HTTP proxy: " + ex);
            return false;
        }
    }

    private static boolean revertProxyICS(WebView webview) {
        try {
            Log.d(LOG_TAG, "Setting proxy with 4.0 API.");

            Class jwcjb = Class.forName("android.webkit.JWebCoreJavaBridge");
            Class params[] = new Class[1];
            params[0] = Class.forName("android.net.ProxyProperties");
            Method updateProxyInstance = jwcjb.getDeclaredMethod("updateProxy", params);

            Class wv = Class.forName("android.webkit.WebView");
            Field mWebViewCoreField = wv.getDeclaredField("mWebViewCore");
            Object mWebViewCoreFieldInstance = getFieldValueSafely(mWebViewCoreField, webview);

            Class wvc = Class.forName("android.webkit.WebViewCore");
            Field mBrowserFrameField = wvc.getDeclaredField("mBrowserFrame");
            Object mBrowserFrame = getFieldValueSafely(mBrowserFrameField, mWebViewCoreFieldInstance);

            Class bf = Class.forName("android.webkit.BrowserFrame");
            Field sJavaBridgeField = bf.getDeclaredField("sJavaBridge");
            Object sJavaBridge = getFieldValueSafely(sJavaBridgeField, mBrowserFrame);

            Class ppclass = Class.forName("android.net.ProxyProperties");
            Class pparams[] = new Class[3];
            pparams[0] = String.class;
            pparams[1] = int.class;
            pparams[2] = String.class;
            Constructor ppcont = ppclass.getConstructor(pparams);

            Object o = null;
            updateProxyInstance.invoke(sJavaBridge, o);

            Log.d(LOG_TAG, "Setting proxy with 4.0 API successful!");
            return true;
        } catch (Exception ex) {
            Log.e(LOG_TAG, "failed to set HTTP proxy: " + ex);
            return false;
        }
    }

    /**
     * Set Proxy for Android 4.1 - 4.3.
     */
    @SuppressWarnings("all")
    private static boolean setProxyJB(WebView webview, String host, int port) {
        Log.d(LOG_TAG, "Setting proxy with 4.1 - 4.3 API.");

        try {
            Class wvcClass = Class.forName("android.webkit.WebViewClassic");
            Class wvParams[] = new Class[1];
            wvParams[0] = Class.forName("android.webkit.WebView");
            Method fromWebView = wvcClass.getDeclaredMethod("fromWebView", wvParams);
            Object webViewClassic = fromWebView.invoke(null, webview);

            Class wv = Class.forName("android.webkit.WebViewClassic");
            Field mWebViewCoreField = wv.getDeclaredField("mWebViewCore");
            Object mWebViewCoreFieldInstance = getFieldValueSafely(mWebViewCoreField, webViewClassic);

            Class wvc = Class.forName("android.webkit.WebViewCore");
            Field mBrowserFrameField = wvc.getDeclaredField("mBrowserFrame");
            Object mBrowserFrame = getFieldValueSafely(mBrowserFrameField, mWebViewCoreFieldInstance);

            Class bf = Class.forName("android.webkit.BrowserFrame");
            Field sJavaBridgeField = bf.getDeclaredField("sJavaBridge");
            Object sJavaBridge = getFieldValueSafely(sJavaBridgeField, mBrowserFrame);

            Class ppclass = Class.forName("android.net.ProxyProperties");
            Class pparams[] = new Class[3];
            pparams[0] = String.class;
            pparams[1] = int.class;
            pparams[2] = String.class;
            Constructor ppcont = ppclass.getConstructor(pparams);

            Class jwcjb = Class.forName("android.webkit.JWebCoreJavaBridge");
            Class params[] = new Class[1];
            params[0] = Class.forName("android.net.ProxyProperties");
            Method updateProxyInstance = jwcjb.getDeclaredMethod("updateProxy", params);

            updateProxyInstance.invoke(sJavaBridge, ppcont.newInstance(host, port, null));
        } catch (Exception ex) {
            Log.e(LOG_TAG, "Setting proxy with >= 4.1 API failed with error: " + ex.getMessage());
            return false;
        }

        Log.d(LOG_TAG, "Setting proxy with 4.1 - 4.3 API successful!");
        return true;
    }

    private static boolean revertProxyJB(WebView webview) {
        Log.d(LOG_TAG, "revert proxy with 4.1 - 4.3 API.");

        try {
            Class wvcClass = Class.forName("android.webkit.WebViewClassic");
            Class wvParams[] = new Class[1];
            wvParams[0] = Class.forName("android.webkit.WebView");
            Method fromWebView = wvcClass.getDeclaredMethod("fromWebView", wvParams);
            Object webViewClassic = fromWebView.invoke(null, webview);

            Class wv = Class.forName("android.webkit.WebViewClassic");
            Field mWebViewCoreField = wv.getDeclaredField("mWebViewCore");
            Object mWebViewCoreFieldInstance = getFieldValueSafely(mWebViewCoreField, webViewClassic);

            Class wvc = Class.forName("android.webkit.WebViewCore");
            Field mBrowserFrameField = wvc.getDeclaredField("mBrowserFrame");
            Object mBrowserFrame = getFieldValueSafely(mBrowserFrameField, mWebViewCoreFieldInstance);

            Class bf = Class.forName("android.webkit.BrowserFrame");
            Field sJavaBridgeField = bf.getDeclaredField("sJavaBridge");
            Object sJavaBridge = getFieldValueSafely(sJavaBridgeField, mBrowserFrame);

            Class ppclass = Class.forName("android.net.ProxyProperties");
            Class pparams[] = new Class[3];
            pparams[0] = String.class;
            pparams[1] = int.class;
            pparams[2] = String.class;
            Constructor ppcont = ppclass.getConstructor(pparams);

            Class jwcjb = Class.forName("android.webkit.JWebCoreJavaBridge");
            Class params[] = new Class[1];
            params[0] = Class.forName("android.net.ProxyProperties");
            Method updateProxyInstance = jwcjb.getDeclaredMethod("updateProxy", params);

            Object o = null;
            updateProxyInstance.invoke(sJavaBridge, o);
        } catch (Exception ex) {
            Log.e(LOG_TAG, "Setting proxy with >= 4.1 API failed with error: " + ex.getMessage());
            return false;
        }

        Log.d(LOG_TAG, "revert proxy with 4.1 - 4.3 API successful!");
        return true;
    }

    // from https://stackoverflow.com/questions/19979578/android-webview-set-proxy-programatically-kitkat
    @SuppressLint("NewApi")
    @SuppressWarnings("all")
    private static boolean setProxyKKPlus(WebView webView, String host, int port, String applicationClassName) {
        Log.d(LOG_TAG, "Setting proxy with >= 4.4 API.");

        Context appContext = webView.getContext().getApplicationContext();
        System.setProperty("http.proxyHost", host);
        System.setProperty("http.proxyPort", port + "");
        System.setProperty("https.proxyHost", host);
        System.setProperty("https.proxyPort", port + "");
        try {
            Class applictionCls = Class.forName(applicationClassName);
            Field loadedApkField = applictionCls.getField("mLoadedApk");
            loadedApkField.setAccessible(true);
            Object loadedApk = loadedApkField.get(appContext);
            Class loadedApkCls = Class.forName("android.app.LoadedApk");
            Field receiversField = loadedApkCls.getDeclaredField("mReceivers");
            receiversField.setAccessible(true);
            ArrayMap receivers = (ArrayMap) receiversField.get(loadedApk);
            for (Object receiverMap : receivers.values()) {
                for (Object rec : ((ArrayMap) receiverMap).keySet()) {
                    Class clazz = rec.getClass();
                    if (clazz.getName().contains("ProxyChangeListener")) {
                        Method onReceiveMethod = clazz.getDeclaredMethod("onReceive", Context.class, Intent.class);
                        Intent intent = new Intent(Proxy.PROXY_CHANGE_ACTION);

                        onReceiveMethod.invoke(rec, appContext, intent);
                    }
                }
            }

            Log.d(LOG_TAG, "Setting proxy with >= 4.4 API successful!");
            return true;
        } catch (ClassNotFoundException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.v(LOG_TAG, e.getMessage());
            Log.v(LOG_TAG, exceptionAsString);
        } catch (NoSuchFieldException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.v(LOG_TAG, e.getMessage());
            Log.v(LOG_TAG, exceptionAsString);
        } catch (IllegalAccessException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.v(LOG_TAG, e.getMessage());
            Log.v(LOG_TAG, exceptionAsString);
        } catch (IllegalArgumentException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.v(LOG_TAG, e.getMessage());
            Log.v(LOG_TAG, exceptionAsString);
        } catch (NoSuchMethodException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.v(LOG_TAG, e.getMessage());
            Log.v(LOG_TAG, exceptionAsString);
        } catch (InvocationTargetException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.v(LOG_TAG, e.getMessage());
            Log.v(LOG_TAG, exceptionAsString);
        }
        return false;
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("all")
    private static boolean revertProxyKKPlus(WebView webView, String applicationClassName) {

        Context appContext = webView.getContext().getApplicationContext();
        Properties properties = System.getProperties();

        properties.remove("http.proxyHost");
        properties.remove("http.proxyPort");
        properties.remove("https.proxyHost");
        properties.remove("https.proxyPort");
        try {
            Class applictionCls = Class.forName(applicationClassName);
            Field loadedApkField = applictionCls.getField("mLoadedApk");
            loadedApkField.setAccessible(true);
            Object loadedApk = loadedApkField.get(appContext);
            Class loadedApkCls = Class.forName("android.app.LoadedApk");
            Field receiversField = loadedApkCls.getDeclaredField("mReceivers");
            receiversField.setAccessible(true);
            ArrayMap receivers = (ArrayMap) receiversField.get(loadedApk);
            for (Object receiverMap : receivers.values()) {
                for (Object rec : ((ArrayMap) receiverMap).keySet()) {
                    Class clazz = rec.getClass();
                    if (clazz.getName().contains("ProxyChangeListener")) {
                        Method onReceiveMethod = clazz.getDeclaredMethod("onReceive", Context.class, Intent.class);
                        Intent intent = new Intent(Proxy.PROXY_CHANGE_ACTION);
//                        intent.putExtra("proxy", null);
                        onReceiveMethod.invoke(rec, appContext, intent);
                    }
                }
            }
            Log.d(LOG_TAG, "Revert proxy with >= 4.4 API successful!");
            return true;
        } catch (ClassNotFoundException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.v(LOG_TAG, e.getMessage());
            Log.v(LOG_TAG, exceptionAsString);
        } catch (NoSuchFieldException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.v(LOG_TAG, e.getMessage());
            Log.v(LOG_TAG, exceptionAsString);
        } catch (IllegalAccessException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.v(LOG_TAG, e.getMessage());
            Log.v(LOG_TAG, exceptionAsString);
        } catch (IllegalArgumentException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.v(LOG_TAG, e.getMessage());
            Log.v(LOG_TAG, exceptionAsString);
        } catch (NoSuchMethodException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.v(LOG_TAG, e.getMessage());
            Log.v(LOG_TAG, exceptionAsString);
        } catch (InvocationTargetException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.v(LOG_TAG, e.getMessage());
            Log.v(LOG_TAG, exceptionAsString);
        }
        return false;
    }

    private static Object getFieldValueSafely(Field field, Object classInstance) throws IllegalArgumentException, IllegalAccessException {
        boolean oldAccessibleValue = field.isAccessible();
        field.setAccessible(true);
        Object result = field.get(classInstance);
        field.setAccessible(oldAccessibleValue);
        return result;
    }
}