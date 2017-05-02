package com.afmobi.palmchat.ui.activity.social;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.http.SslError;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.setting.MyAccountInfoActivity;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnConfirmButtonDialogListener;
import com.core.AfProfileInfo;
import com.core.cache.CacheManager;
/**
 * 跳转FunnyClub 网页 改名为PalmClub
 * @author xiaolong
 *
 */
public class FunnyClubActivity extends BaseActivity implements OnDismissListener {
	private WebView mWebView;
	private ProgressBar mprogress;
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void findViews() {
		// TODO Auto-generated method stub
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |  
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.funnyclub);  
		mprogress = (ProgressBar) findViewById(R.id.progressbar_forweb);  
		mWebView = (WebView) findViewById(R.id.webView_funnyclub);  
		((TextView)findViewById(R.id.title_text)).setText(R.string.funclub);
		TextView textAfid = (TextView) findViewById(R.id.title_offOnline);
		textAfid.setVisibility(View.VISIBLE);
		String afid=CacheManager.getInstance().getMyProfile().afId;
		if(!TextUtils.isEmpty(afid)){
			textAfid.setText(afid.replace("a", ""));
		}
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void init() {
		
		AfProfileInfo afProfileInfo = CacheManager.getInstance().getMyProfile();
		if(afProfileInfo.is_bind_phone ) {
			// TODO Auto-generated method stub
			// 得到设置属性的对象
			WebSettings webSettings = mWebView.getSettings(); 
			// 使能JavaScript
			webSettings.setJavaScriptEnabled(true);
			mWebView.setWebViewClient(new WebViewClient() {
				@Override
				public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//					super.onReceivedError(view, errorCode, description, failingUrl);
					mWebView.stopLoading();
				} 
				@Override
				public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
					if (Constants.DISABLE_SSL_CHECK_FOR_TESTING) {
						handler.proceed();
					} else {
//						super.onReceivedSslError(view, handler, error);
						handler.cancel();
					}
				} 
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) { //如果有跳转的 那么不跳转到浏览器，在webView内完成加载
					//重定向后 会进这里 url为重定向地址  可以判断url里的参数
					view.loadUrl(url);//如果要显示重定向后的页面 就调这句
					return true;
				}
				 @Override
	             public void onPageFinished(WebView view, String url) {
	                     // TODO Auto-generated method stub
	                     super.onPageFinished(view, url);
	                     //页面下载完毕,却不代表页面渲染完毕显示出来
	                     //WebChromeClient中progress==100时也是一样
	                     if (mWebView.getContentHeight() != 0 && mWebView.getContentHeight() > 100) {
	                             //这个时候网页才显示
	                    	 mprogress.setVisibility(View.GONE);  
	                     }
	             }
			});
			mWebView.setWebChromeClient(new WebChromeClient(){
	             @Override
	             public void onProgressChanged(WebView view, int newProgress) {
	            	 mprogress.setProgress(newProgress);  
//	                 if(newProgress==100){  
//	                	 mprogress.setVisibility(View.GONE);  
//	                 }  
	                 super.onProgressChanged(view, newProgress);  
	     
	             }
	     });
			
			String [] serverInfo=((PalmchatApp)  getApplication()).mAfCorePalmchat.AfHttpGetServerInfo();
			if(serverInfo!=null&&serverInfo[0]!=null){//从中间件获取的dp url和session
				 
				String _strUrl=serverInfo[0];//获取dp的url 
				int _inx=serverInfo[0].indexOf(JsonConstant.HTTP_HEAD); 
				if(_inx>=0){
					_strUrl=_strUrl.substring(JsonConstant.HTTP_HEAD.length());//url地址取掉http://
				}
				String _afid=CacheManager.getInstance().getMyProfile().afId;
				if(_afid!=null&&_afid.startsWith("a")){//afid号要去掉a
					_afid=_afid.substring(1);
				}
				String url="http://fanclub.palmchatnow.com/autoregister/";
				 if(    afProfileInfo.fanclub_url!=null){
					url=afProfileInfo.fanclub_url;//从登陆信息里获取url
				}   
				mWebView.loadUrl(url+ 
						_afid+"/"+serverInfo[2]+"/"+_strUrl);//afid  session和dp url的拼接
			}
		 
			findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(mWebView.canGoBack()) {
						   mWebView.goBack();// 返回前一个页面
					}else{
						finish();
					}
				}
			});
		}else {
			 	AppDialog appDialog = new AppDialog(this);
				String content = context.getString(R.string.bind_phone_messages);
				appDialog.createConfirmDialog(context, content,
						new OnConfirmButtonDialogListener() { 
							@Override
							public void onRightButtonClick() {
								Intent it = new Intent();
								it.setClass(context, MyAccountInfoActivity.class);
								it.putExtra(MyAccountInfoActivity.STATE, MyAccountInfoActivity.PHONE_UNBIND);
								String countryCode = PalmchatApp.getOsInfo().getCountryCode();
								countryCode = "+" + countryCode;
								String country = PalmchatApp.getOsInfo().getCountry(context);
								it.putExtra(MyAccountInfoActivity.PARAM_COUNTRY_CODE, countryCode);
								it.putExtra(MyAccountInfoActivity.PARAM_COUNTRY_NAME, country);
								context.startActivity(it);
								finish();
							}
							@Override
							public void onLeftButtonClick() {
								finish();
							}
						
						});
				appDialog.setOnDismissListener(  this);
				appDialog.show();

			 
			
		}
	
	}

	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	  if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
	   mWebView.goBack();// 返回前一个页面
	   return true;
	  }
	  return super.onKeyDown(keyCode, event);
	 }

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		finish();
	}
}
