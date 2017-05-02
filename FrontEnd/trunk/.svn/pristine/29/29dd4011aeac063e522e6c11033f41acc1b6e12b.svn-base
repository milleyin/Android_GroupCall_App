package com.afmobi.palmchat.db;

import java.util.Calendar;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;

public class SharePreferenceService {

	private Context context;

	private SharePreferenceService(Context context) {
		this.context = context;
	}

	private static SharePreferenceService instance;

	public static SharePreferenceService getInstance(Context context) {
		if (instance == null) {
			instance = new SharePreferenceService(context);
		}
		return instance;
	}

	public void savaFilename(String filename) {
		SharedPreferences p = context.getSharedPreferences(RequestConstant.DEVICE, Context.MODE_PRIVATE);
		Editor e = p.edit();
		e.putString(JsonConstant.KEY_FILENAME, filename);
		e.commit();
	}

	public String getFilename() {
		SharedPreferences p = context.getSharedPreferences(RequestConstant.DEVICE, Context.MODE_PRIVATE);
		String filename = p.getString(JsonConstant.KEY_FILENAME, "");
		p.edit().remove(JsonConstant.KEY_FILENAME).commit();
		return filename;
	}

	public void clearFilename() {
		SharedPreferences p = context.getSharedPreferences(RequestConstant.DEVICE, Context.MODE_PRIVATE);
		p.edit().remove(JsonConstant.KEY_FILENAME).commit();
	}

	public long getReflashTime() {
		SharedPreferences account = context.getSharedPreferences(RequestConstant.DEVICE, Context.MODE_PRIVATE);
		return account.getLong(JsonConstant.KEY_TIME, Calendar.getInstance().getTimeInMillis());
	}

	public void setListenMode(int mode) {
		SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putInt(JsonConstant.KEY_LISTEN_MODE, mode);
		editor.commit();
	}

	public int getListenMode() {
		SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		return account.getInt(JsonConstant.KEY_LISTEN_MODE, AudioManager.MODE_NORMAL);
	}

}
