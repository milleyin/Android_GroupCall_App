package com.afmobi.palmchat.ui.activity.payment;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.eventbusmodel.ChangeRegionEvent;
import com.afmobi.palmchat.eventbusmodel.ExitMobileTopUpEvent;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.publicaccounts.InnerNoAbBrowserActivity;
import com.afmobi.palmchat.ui.activity.social.FunnyClubActivity;
import com.afmobi.palmchat.ui.customview.list.CircleAdapter;
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.FileUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobigroup.gphone.R;
import com.core.AfLoginInfo;
import com.core.AfPalmchat;
import com.core.cache.CacheManager;
import com.google.gson.JsonObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;

import de.greenrobot.event.EventBus;

/**
 * 公共账号消息详情
 *
 * @author starw
 */
public class MyWapActivity extends BaseActivity implements View.OnClickListener {
    /**
     * tag
     */
    private final String TAG = MyWapActivity.class.getSimpleName();
    /**
     * webview
     */
    private WebView mWebView;
    /**
     * URL
     */
    private String mUrl;
    /**
     * firstUrl记录从页面带过来的原始url
     */
    private String mFirstUrl = "";
    /**
     * 标题
     */
    private TextView mWapTitle;
    /**
     * 进度条
     */
    private ProgressBar mProgressBar;
    /**
     * 保存当前titile与url对应
     */
    private HashMap<String, String> mHashMap = new HashMap<String, String>();
    /**
     * 与中间件交互类
     */
    private AfPalmchat mAfCorePalmchat;

    /**
     * airtime免责声明
     */
    public static final String MODULE_AIRTIME_TERMS = "module_airtime_terms";
    /**
     * palmcoin免责声明
     */
    public static final String MODULE_PALMCOIN_TERMS = "module_palmcoin_terms";
    /**
     * 一般免责声明
     */
    public static final String MODULE_GENERIC_TERMS = "module_generic_terms";
    /**
     * feedback
     */
    public static final String MODULE_FEEDBACK = "module_feedback";
    /**
     * mobile top up
     */
    public static final String MOBILE_TOP_UP = "mobile_top_up";
    /**
     * 来自哪个入口
     */
    public static final String FORM = "form";
    /*正在加载页面时显示*/
    private ViewGroup rl_processing;
    /*加载页面失败后显示*/
    private ViewGroup rl_error;
    /*重新加载按钮*/
    private Button btn_retry;
    /*是否当前是错误页*/
    private boolean isWapError = false;
    //对充值页面进行统计
    /*MyPhoneStateListener类的对象，即设置一个监听器对象*/
    MyPhoneStateListener myListener;
    //TelephonyManager类的对象
    TelephonyManager telPhoneManager;
    /*当前网页加载状态，成功/失败 1/0*/
    private int status = -1;
    /*网络信号强度*/
    private String RSSI = "";
    /*网络信号干扰度*/
    private String CINR = "";
    /*保存到本地的文件名*/
    private String fileName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化对象
        myListener = new MyPhoneStateListener();
        //Return the handle to a system-level service by name.通过名字获得一个系统级服务
        telPhoneManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //Registers a listener object to receive notification of changes in specified telephony states.设置监听器监听特定事件的状态
        telPhoneManager.listen(myListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    @Override
    protected void onResume() {

        super.onResume();
        telPhoneManager.listen(myListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    @Override
    protected void onPause() {
        super.onPause();
        telPhoneManager.listen(myListener, PhoneStateListener.LISTEN_NONE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        telPhoneManager.listen(myListener, PhoneStateListener.LISTEN_NONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mHashMap.clear();
        telPhoneManager.listen(myListener, PhoneStateListener.LISTEN_NONE);
        String from = getIntent().getStringExtra(FORM);
        if (from != null && from.equals(MOBILE_TOP_UP)) {//如果来自mobile top up页面，则发送关闭通知
            EventBus.getDefault().post(new ExitMobileTopUpEvent());
        }


    }

    @Override
    public void findViews() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_mywap);
        mWebView = (WebView) findViewById(R.id.webview);
        mWapTitle = (TextView) findViewById(R.id.title_text);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        rl_processing = (ViewGroup) findViewById(R.id.rl_processing);
        rl_error = (ViewGroup) findViewById(R.id.rl_error);
        btn_retry = (Button) findViewById(R.id.btn_retry);

        mAfCorePalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;
        btn_retry.setOnClickListener(this);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void init() {
        WebSettings webSettings = mWebView.getSettings();
        // 关闭缩放
        webSettings.setBuiltInZoomControls(false);
        webSettings.setSupportZoom(false);
        webSettings.setDisplayZoomControls(false);
        /**设置支持js*/
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(mWebCClient);
        mWebView.setWebViewClient(new DetailsWebViewClicent());

        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mUrl = getIntent().getStringExtra(IntentConstant.WAP_URL);
        mFirstUrl = mUrl;

        mWebView.loadUrl(mUrl);


    }

    /***/
    private WebChromeClient mWebCClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            mWapTitle.setText(title);
            mHashMap.put(mUrl, title);

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
    //
    // @Override
    // public boolean onKeyDown(int keyCode, KeyEvent event) {
    // if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
    //
    // mWebView.goBack();
    // mHashMap.remove(mUrl);
    // return true;
    //
    // }
    // return super.onKeyDown(keyCode, event);
    //
    // }

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
            status = 0;
        }

        //重写此方法可以让webview处理https请求。
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {


            if (Constants.DISABLE_SSL_CHECK_FOR_TESTING) {
                handler.proceed();
            } else {
                super.onReceivedSslError(view, handler, error);
                handler.cancel();
            }


        }

        //在点击请求的是链接是才会调用，重写此方法返回true表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.setWebChromeClient(mWebCClient);

            view.loadUrl(url);

            return true;
        }

        //在页面加载结束时调用
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mWapTitle.setText(mHashMap.get(url));

            rl_processing.setVisibility(View.GONE);
            if (!isWapError) {
                mWebView.setVisibility(View.VISIBLE);
                status = 1;
            }
            isWapError = false;

            saveCurrentSignal();
        }


        //在页面加载开始时调用
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            mUrl = url;

            rl_processing.setVisibility(View.VISIBLE);
            rl_error.setVisibility(View.GONE);
            super.onPageStarted(view, url, favicon);

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_retry:
                rl_error.setVisibility(View.GONE);

                /*从第一个页面重新加载，由于服务器暂时不支持从中间页面恢复*/
                mWebView.loadUrl(mFirstUrl);
                break;
        }
    }

    /**
     * 将当前信号状态等信息提交到服务器进行统计，如果提交失败则保存到本地
     */
    private void saveCurrentSignal() {
        try {
            if (TextUtils.isEmpty(fileName)) {
                fileName = System.currentTimeMillis() + ".txt";
            }

            JsonObject jsonData = new JsonObject();
            jsonData.addProperty("ts", System.currentTimeMillis());
            jsonData.addProperty("url", mUrl);
            jsonData.addProperty("status", status);
            jsonData.addProperty("RSSI", RSSI);
            jsonData.addProperty("CINR", CINR);
            jsonData.addProperty("ping", "");

            FileUtils.writeJsonStrToSDFile(jsonData.toString(), RequestConstant.MOBILE_TOP_UP_CACHE, fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //监听器类
    private class MyPhoneStateListener extends PhoneStateListener {
        /*得到信号的强度由每个tiome供应商,有更新*/
        @Override

        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            //调用超类的该方法，在网络信号变化时得到回答信号
            super.onSignalStrengthsChanged(signalStrength);
            RSSI = String.valueOf(signalStrength.getCdmaDbm());
            CINR = String.valueOf(signalStrength.getGsmSignalStrength());
//            //cinr：Carrier to Interference plus Noise Ratio（载波与干扰和噪声比）
//            Log.i("MyWapActivity_0902", "CDMA RSSI = " + String.valueOf(signalStrength.getCdmaDbm()));
//            Log.i("MyWapActivity_0902", "GSM Cinr = " + String.valueOf(signalStrength.getGsmSignalStrength()));

        }
    }
}