package com.afmobi.palmchat.services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.broadcasts.AppReceiver;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.MsgAlarmManager;


public class AppStatusService extends Service{
	public static final String ACTION_START = "start";
	public static final String ACTION_STOP = "stop";
	protected static final String TAG = AppStatusService.class.getSimpleName();
	private ActivityManager activityManager;
	private String packageName;
//	private AppThread appThread;
	private PalmchatApp app;
	private final int LAST_STATUS_FOREGROUND = 0;
	private final int LAST_STATUS_BACKGROUND = 1;
	private int mStatus = LAST_STATUS_BACKGROUND;//0前台,1后台
	private boolean isRequest = false;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		app = (PalmchatApp)getApplication();
		activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
		packageName = this.getPackageName();
//		task = new TimerTask() {
//		    @Override
//		    public void run() {
//		        handler.obtainMessage().sendToTarget();
//		    }
//		};
		PalmchatLogUtils.e(TAG, "----resetMsgAlarm.......");
		registerBroadcastReceiver();
		

//		MsgAlarmManager.getInstence().resetMsgForegroundLocation();
		
//		MsgAlarmManager.getInstence().resetMsgbackgroundLocation();
		
		MsgAlarmManager.getInstence().resetMsgAlarm();
		
	}
	
	ScreenBroadcastReceiver screenBroadcastReceiver;
	private void registerBroadcastReceiver() {
		if(screenBroadcastReceiver == null){
			screenBroadcastReceiver = new ScreenBroadcastReceiver();
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		PalmchatApp.getApplication().registerReceiver(screenBroadcastReceiver, filter);
	}
	
	public class ScreenBroadcastReceiver extends BroadcastReceiver{
		public ScreenBroadcastReceiver(){
			
		}
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			PalmchatLogUtils.e(TAG, "----AppStatusService  " + action);
			if(Intent.ACTION_SCREEN_OFF.equals(action)){
//				MsgAlarmManager.getInstence().cancelAlarm(MsgAlarmManager.MSG_FOREGROUND_LOCATION);
//				MsgAlarmManager.getInstence().resetMsgbackgroundLocation();
				MsgAlarmManager.getInstence().setForeGround(false);
				AppStatusService.start(context, true);
			} else if(Intent.ACTION_SCREEN_ON.equals(action)){
//				MsgAlarmManager.getInstence().cancelAlarm(MsgAlarmManager.MSG_BACKGROUND_LOCATION);
//				MsgAlarmManager.getInstence().resetMsgForegroundLocation();
				MsgAlarmManager.getInstence().setForeGround(true);
				AppStatusService.start(context, true);
				PalmchatLogUtils.e(TAG, "----AppStatusService  AfCoreKCStatusReset::" + action);
				PalmchatApp.getApplication().mAfCorePalmchat.AfCoreKCStatusReset();
			}
			
		}
	}
	
	/**
	 * isLoginAgain true 重新登陆,false正常登陆  开启
	 */
	public static void start(Context context,boolean isLoginAgain) {
		start = true;
		MsgAlarmManager.getInstence().setReLogin(isLoginAgain);
		Intent i = new Intent();
		i.setClass(context, AppStatusService.class);
		i.setAction(ACTION_START);
		context.startService(i);
		
//		i =new Intent(context, AppReceiver.class);
//	    i.setAction(ACTION_START);
//	    PendingIntent sender = PendingIntent.getBroadcast(context, 0, i, 0);
//	    //开始时间
//	    long firstime = SystemClock.elapsedRealtime();
//	    AlarmManager am = (AlarmManager)context.getSystemService(ALARM_SERVICE);//20 seconds
//	    am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime, 20*1000, sender);
//	    
//	    context.startService(i);
	}

	public static void stop(Context context) {
		Intent i = new Intent();
		i.setClass(context, AppReceiver.class);
		i.setAction(ACTION_STOP);
		context.startService(i);
		start = false;
	}

	public static boolean start;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e(TAG, "onStartCommand");
		if (intent != null) {
			String action = intent.getAction();
			if (ACTION_START.equals(action)) {
				try {
					//timer.schedule(task, 2000, 20000);
//					handler.obtainMessage().sendToTarget();
					start = true;
				} catch (Exception e) {
					
				}
				
				
			} else if (ACTION_STOP.equals(action)) {
				//timer.cancel();
				start = false;
				stopSelf();
			}

		}
		return START_STICKY;
	}
	
	public void changeStatus(byte status) {
		
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		PalmchatLogUtils.println("AppStatusService  onDestroy");

		Intent localIntent = new Intent();
        localIntent.setClass(this, AppStatusService.class);  //ondestroy restart service
        localIntent.setAction(ACTION_START);
        PalmchatApp.getApplication().unregisterReceiver(screenBroadcastReceiver);
        this.startService(localIntent);
	}

	public static void start(Context context, boolean isLoginAgain, boolean isFore) {
		// TODO Auto-generated method stub
		start = true;
		MsgAlarmManager.getInstence().setReLogin(isLoginAgain);
//		MsgAlarmManager.getInstence().setForeGround(isFore);
		Intent i = new Intent();
		i.setClass(context, AppStatusService.class);
		i.setAction(ACTION_START);
		context.startService(i);
	}

	
	public static void sendRequts(Context context, boolean b) {
		// TODO Auto-generated method stub
//		handler.obtainMessage().sendToTarget();
	}
	
}
