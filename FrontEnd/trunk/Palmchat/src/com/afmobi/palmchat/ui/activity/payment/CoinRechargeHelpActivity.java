package com.afmobi.palmchat.ui.activity.payment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.afmobi.palmchat.BaseActivity;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.db.DBState;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.social.FunnyClubActivity;
import com.core.AfProfileInfo;
import com.core.cache.CacheManager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 充值说明页
 * 
 * @author zhh 2015-12-18
 *
 */
public class CoinRechargeHelpActivity extends BaseActivity implements OnClickListener {

	@Override
	public void findViews() {
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.activity_coin_recharge_help);

		((TextView) findViewById(R.id.title_text)).setText(R.string.help);
		findViewById(R.id.back_button).setOnClickListener(this);

	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void init() {
		final String recharge_intro_url = CacheManager.getInstance().getRecharge_intro_url();
		if (TextUtils.isEmpty(recharge_intro_url)) {
			throw new IllegalArgumentException("required PARAM_URL_TO_LOAD");
		}

		try {

			AfProfileInfo myProfile = CacheManager.getInstance().getMyProfile();
			String country = myProfile.country;
			String country_code = "";
			if (country != null) {
				country_code = DBState.getInstance(this).getCcFromCountry(country);
			} else {
				country_code = "234";
			}

			String helpUrl = recharge_intro_url + "?cc=" + URLEncoder.encode(country_code, "UTF-8");

			final WebView webView = (WebView) findViewById(R.id.web_view);
			final ProgressBar progressContainer = (ProgressBar) findViewById(R.id.progress_container);

			webView.setWebViewClient(new WebViewClient() {
				@Override
				public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
					super.onReceivedError(view, errorCode, description, failingUrl);
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
				public void onPageFinished(WebView view, String url) {
					super.onPageFinished(view, url);

					if (webView.getContentHeight() != 0) {
						progressContainer.setVisibility(View.GONE);
					}
				}

				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					view.loadUrl(url);
					return true;
				}
			});

			webView.setWebChromeClient(new WebChromeClient() {
				@Override
				public void onProgressChanged(WebView view, int newProgress) {
					super.onProgressChanged(view, newProgress);
					progressContainer.setProgress(newProgress);
				}
			});

			webView.getSettings().setJavaScriptEnabled(true);
			webView.loadUrl(helpUrl);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onBackPressed() {
		onBack();
	}

	private void onBack() {
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_button:
			onBack();
			break;
		}
	}

}
