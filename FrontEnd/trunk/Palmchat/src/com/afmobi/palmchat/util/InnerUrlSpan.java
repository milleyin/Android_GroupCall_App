package com.afmobi.palmchat.util;

import java.util.LinkedList;

import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.ui.activity.publicaccounts.InnerNoAbBrowserActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.style.ClickableSpan;
import android.view.View;

public class InnerUrlSpan extends ClickableSpan {
	/** 当前点击的实际链接 */
	private String mUrl;
	private Context mContext;
	/**
	 * 根据需求，一个TextView中存在多个link的话，这个和我求有关，可已删除掉
	 * 无论点击哪个都必须知道该TextView中的所有link，因此添加改变量
	 */
	private LinkedList<String> mUrls;

	InnerUrlSpan(Context context, String url, LinkedList<String> urls) {
		mContext = context;
		mUrl = url;
		mUrls = urls;
	}
	
	InnerUrlSpan(Context context, String url) {
		mContext = context;
		mUrl = url;
	}

	@Override
	public void onClick(View widget) {
		// 这里你可以做任何你想要的处理
		// 比如在你自己的应用中用webview打开，而不是打开系统的浏览器
		Uri uri = Uri.parse(mUrl);
		Intent intent = new Intent();
		intent.setClass(mContext, InnerNoAbBrowserActivity.class);
		intent.putExtra(IntentConstant.RESOURCEURL, uri.toString());
		((Activity) mContext).startActivity(intent);
	}
}
