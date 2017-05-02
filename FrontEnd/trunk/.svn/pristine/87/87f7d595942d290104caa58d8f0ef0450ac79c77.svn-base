package com.afmobi.palmchat.ui.activity.payment;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.MyActivityManager;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.eventbusmodel.UpdateCoinsEvent;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.social.FunnyClubActivity;
import com.afmobi.palmchat.util.CommonUtils;

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
import de.greenrobot.event.EventBus;

public class PagaRechargeActivity extends BaseActivity implements OnClickListener {
	// 给外界传值进来的
	public static final String PARAM_URL_TO_LOAD = "param_url_to_load";
	public static final String TAG_PAGA_FROM = "from_tag_paga"; // 用于区分从哪个页面进入的充值
	public static final int TAG_PAGA_FROM_WALLET = 1;
	public static final int TAG_PAGA_FROM_AIRTIME = 2;
	public static final int TAG_PAGA_FROM_POINT = 3;
	public static final int TAG_PAGA_FROM_PREDICT = 4;
	private int tag_from;
	TextView tv_right;

	@Override
	public void findViews() {
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.activity_paga_recharge);

		((TextView) findViewById(R.id.title_text)).setText(R.string.recharge_with_paga);
		findViewById(R.id.back_button).setOnClickListener(this);
		tv_right = (TextView) findViewById(R.id.op_text);
		tv_right.setTextColor(getResources().getColor(R.color.log_blue));
		tv_right.setText(R.string.done);
		tv_right.setOnClickListener(this);
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void init() {
		tag_from = getIntent().getIntExtra(TAG_PAGA_FROM, 0);
		final String paramUrlToLoad = getIntent().getStringExtra(PARAM_URL_TO_LOAD);
		if (TextUtils.isEmpty(paramUrlToLoad)) {
			throw new IllegalArgumentException("required PARAM_URL_TO_LOAD");
		}

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

				// 无此参数：该笔交易仍在进行中
				// unknown：该笔交易仍在进行中
				// succeed：该笔交易已成功
				// fail：该笔交易已失败
				if (!url.isEmpty() && url.contains("status=")) {
					String status = "";
					int start = url.indexOf("status=");
					String subStr = url.substring(start + 7, url.length());
					if (subStr != null) {
						if (subStr.contains("&")) {
							int end = subStr.indexOf("&");
							status = subStr.substring(0, end);
						} else {
							status = subStr;
						}
					}

					if (status != null) {
						if (status.equals("succeed") || status.equals("fail")) // 当paga回调页返回状态（成功/失败）才显示Done按钮
							tv_right.setVisibility(View.VISIBLE);
					}
				}
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
		webView.loadUrl(paramUrlToLoad);
	}

	@Override
	public void onBackPressed() {
		onBack();
	}

	private void onBack() {
		switch (tag_from) {
		case TAG_PAGA_FROM_AIRTIME: // 话费充值
			EventBus.getDefault().post(new UpdateCoinsEvent(0, true)); // 重新从服务器获取最新Balance
			MyActivityManager.getScreenManager().popActivity(PalmCoinRechargeActivty.class);
			exit();
			break;
		case TAG_PAGA_FROM_WALLET: // coin充值
			EventBus.getDefault().post(new UpdateCoinsEvent(0, true)); // 重新从服务器获取最新Balance
			MyActivityManager.getScreenManager().popActivity(PalmCoinRechargeActivty.class);
			exit();
			break;
		case TAG_PAGA_FROM_POINT: // point充值
			EventBus.getDefault().post(new UpdateCoinsEvent(0, true)); // 重新从服务器获取最新Balance
			MyActivityManager.getScreenManager().popActivity(PointsRechargeActivity.class);
			exit();
			break;
		case TAG_PAGA_FROM_PREDICT: // 下注页面余额不足时，进行Point充值
			EventBus.getDefault().post(new UpdateCoinsEvent(0, true)); // 重新从服务器获取最新Balance
			MyActivityManager.getScreenManager().popActivity(PointsRechargeActivity.class);
			exit();
			break;

		default:
			break;
		}
		exit();
	}

	/**
	 * 退出当前页面
	 */
	private void exit() {
		CommonUtils.closeSoftKeyBoard(tv_right);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_button:
			onBack();
			break;
		case R.id.op_text:
			onBack();
			break;
		}
	}

}
