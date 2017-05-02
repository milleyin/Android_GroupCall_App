package com.afmobi.palmchat.util;

import java.util.List;

import com.afmobi.palmchat.log.PalmchatLogUtils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class NetworkUtils {
	public static boolean isNetworkAvailable(Context context) { 
		if (context != null) {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cm == null) {
				return false;
			}
			NetworkInfo info = cm.getActiveNetworkInfo();
			if (info == null) {
				return false;
			}
			PalmchatLogUtils.println("isNetworkAvailable  info  " + info);
			return info.isAvailable();
		}else{
			return false;
		}
	}
}
