package com.afmobi.palmchat.broadcasts;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.util.Log;

import java.lang.reflect.Method;

public class ScreenObserver{
	private static String TAG = "ScreenObserver";
	private Context mContext;
	private ScreenBroadcastReceiver mScreenReceiver;
    private ScreenStateListener mScreenStateListener;
    private static Method mReflectScreenState;
    
	public ScreenObserver(Context context,ScreenStateListener screenStateListener){
		mContext = context;
		mScreenReceiver = new ScreenBroadcastReceiver();
		startScreenBroadcastReceiver();
		mScreenStateListener = screenStateListener;
		try {
			mReflectScreenState = PowerManager.class.getMethod("isScreenOn",
					new Class[] {});
		} catch (NoSuchMethodException nsme) {
			Log.d(TAG, "API < 7," + nsme);
		}


	}

	/**
	 * 监听屏幕状态广播
	 */
	private class ScreenBroadcastReceiver extends BroadcastReceiver{
    	private String action = null;
    	@Override
    	public void onReceive(Context context, Intent intent) {
    		action = intent.getAction();
    		if(Intent.ACTION_SCREEN_ON.equals(action)){
    			mScreenStateListener.onScreenOn();
    		}else if(Intent.ACTION_SCREEN_OFF.equals(action)){
    			mScreenStateListener.onScreenOff();
    		}
			else if(Intent.ACTION_USER_PRESENT.equals(action)){
				mScreenStateListener.onUserPresent();
			}
    	}
    }
    
	
	public void requestScreenStateUpdate(ScreenStateListener listener) {
		mScreenStateListener = listener;
		startScreenBroadcastReceiver();
		
		firstGetScreenState();
	}

	/**
	 * 获取状态
	 */
	private void firstGetScreenState(){
		PowerManager manager = (PowerManager) mContext
				.getSystemService(Activity.POWER_SERVICE);
		if (isScreenOn(manager)) {
			if (mScreenStateListener != null) {
				mScreenStateListener.onScreenOn();
			}
		} else {
			if (mScreenStateListener != null) {
				mScreenStateListener.onScreenOff();
			}
		}
	}

	/**
	 * 停止广播
	 */
	public void stopScreenStateUpdate(){
		if(mScreenReceiver != null) {
			try {
				mContext.unregisterReceiver(mScreenReceiver);
			}
			catch(Exception e) {
			}
		}
	}

	/**
	 * 开始注册监听屏幕广播
	 */
	private void startScreenBroadcastReceiver(){
    	IntentFilter filter = new IntentFilter();
    	filter.addAction(Intent.ACTION_SCREEN_ON);
    	filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_USER_PRESENT);
    	mContext.registerReceiver(mScreenReceiver, filter);
    }

	/** 是否亮屏
	 * @param pm
	 * @return
	 */
	private static boolean isScreenOn(PowerManager pm) {
		boolean screenState;
		try {
			screenState = (Boolean) mReflectScreenState.invoke(pm);
		} catch (Exception e) {
			screenState = false;
		}
		return screenState;
	}

	/**
	 * 供外部调用接口
	 */
	public interface ScreenStateListener {
		public void onScreenOn();
		public void onScreenOff();
		public void onUserPresent();
	}
}