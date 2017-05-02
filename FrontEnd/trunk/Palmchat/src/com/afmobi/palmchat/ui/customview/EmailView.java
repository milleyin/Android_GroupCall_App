package com.afmobi.palmchat.ui.customview;

import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.ui.activity.login.LoginActivity;
import com.afmobi.palmchat.ui.customview.SystemBarTintManager.SystemBarConfig;

public class EmailView {
	
	private LayoutInflater inflater;
	private View root,lin_title;
	private TextView textCountryText;
	private TextView textCountryCode;
	private int StatusBarAlpha = 125;
	
	public EmailView(LoginActivity context ) {
		inflater = LayoutInflater.from(context);
		root = inflater.inflate(R.layout.activity_loginactivity_email, null);
		textCountryText = (TextView) root.findViewById(R.id.country_text);
		textCountryCode = (TextView) root.findViewById(R.id.country_code);
		lin_title = root.findViewById(R.id.email_lin);
	/*	SystemBarTintManager tintManager = new SystemBarTintManager(context);
		SystemBarConfig systemBarConfig = tintManager.getConfig();
//		tintManager.setStatusBarTintEnabled(true);
//		tintManager.setStatusBarTintResource(R.drawable.title_bg);
		tintManager.setStatusBarAlpha(StatusBarAlpha);
		int StatusBarHeight = systemBarConfig.getStatusBarHeight();
		if(VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			if (lin_title != null) {
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				params.setMargins(0, StatusBarHeight, 0, 0);
				lin_title.setLayoutParams(params);
			}
			if (systemBarConfig.hasNavigtionBar()) {
				if (!(PalmchatApp.getOsInfo().getUa().contains("HTC")|| PalmchatApp.getOsInfo().getUa().contains("MI")
						|| PalmchatApp.getOsInfo().getBrand().contains("Meizu"))) {//过滤htc,小米
					tintManager.setNavigationBarTintEnabled(true);
					tintManager.setNavigationBarTintColor(Color.BLACK);
					if (lin_title != null) {
						int NavigationBarHeight = systemBarConfig.getNavigationBarHeight();
						lin_title.setPadding(0, 0, 0, NavigationBarHeight);
					}
				}
			}
		 }*/
	}
	
	public TextView getTextCountryText() {
		return textCountryText;
	}

	public TextView getTextCountryCode() {
		return textCountryCode;
	}

	public View getRoot() {
		return root;
	}


	public void setRoot(View root) {
		this.root = root;
	}
	

}
