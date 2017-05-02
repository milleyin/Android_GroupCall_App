package com.core.timer;


import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.core.AfPalmchat;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

@SuppressLint("HandlerLeak")
public class AfRTCTimer{
//	private int timer_first_elipse_ms;
//	 private int timer_repeat_ms;
	 private int timer_handle;
	
	 private Context mContext = null;
	 private  AlarmManager mAlarmManager = null;
	 private MockAlarmReceiver mMockAlarmReceiver;
	 //private WakeLock mWakeLock;
	 private Object mObject = new Object();
	 
	 private final static int MSG_ON_TIMER =  10;
	 //RTC time
	 private final static String ACTION_ON_TIMER =  "com.core.timer.AfRTCTimer";
	 
	 private PendingIntent mSender;
	 private boolean running = false;
	 
	 private Handler mHandler = new Handler(Looper.getMainLooper()){		 
	        @Override
	        public void handleMessage(Message msg){
	        	
	        	switch(msg.what){
	        		case MSG_ON_TIMER:{
	        		//	 startWakeLock();
	    				 AfPalmchat core = AfPalmchat.getAfCorePalmchat();
	    				 if( null != core){
	    					 AfStopRTCAlarm();
	    					 core.AfRTCDoCallback(timer_handle); 
	    				 }	    				
	        		}
	        		break;
	        	}  
	        	
	        }
	 };
	 
	 public class MockAlarmReceiver extends BroadcastReceiver {	  
		 @Override
		 public void onReceive(Context context, Intent intent) {
			 final String action = intent.getAction();			 
			 if ( action.equals(ACTION_ON_TIMER) || action.equals(Intent.ACTION_TIME_CHANGED) || action.equals(Intent.ACTION_DATE_CHANGED)
					|| action.equals(Intent.ACTION_TIMEZONE_CHANGED) ) {
				 mHandler.sendEmptyMessage(MSG_ON_TIMER);				 
				}
			}	 
	 }
	 
//	 private void startWakeLock(){
//		 synchronized(mObject){
//			 if( null != mContext){
//				 if( null == mWakeLock){
//					 PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE); 
//					 mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"TIMER");	
//				 }
//				 if( !mWakeLock.isHeld() ){
//					 mWakeLock.acquire(); 
//				 } 
//			 }			 
//		 }
//
//	 }
	 
//	 private void stopWakeLock(){
//		 synchronized(mObject){
//			 if( null != mWakeLock){
//				 if( mWakeLock.isHeld()){
//					 mWakeLock.release();
//				 }			
//			 }
//		 }
//	 }
	 
	 public AfRTCTimer(){
	 
	 }
	 
	 public void AFRTCSetup(int elipse_ms, int repeat_ms, int user_data, int handle){		    
		 synchronized(mObject){			
			 timer_handle = handle;
		 }
		 
	}
	
	 public void AFRTCDestroy()
	 {
		 synchronized(mObject){
			if(null != mAlarmManager){
			      mContext.unregisterReceiver(mMockAlarmReceiver);
				  mMockAlarmReceiver = null;
			}	 
		 }
	 }
	 
	public void AfStartRTCAlarm(int elipse_ms, int repeat_ms)
	{

		this.AfStopRTCAlarm();

		 synchronized(mObject){
			 	if( elipse_ms < 1000){
			 		elipse_ms = 1000;
			 	}
				 if( null == mContext){
					 mContext = AfPalmchat.getContext();
				 }
				 if( null != mContext && null == mMockAlarmReceiver){
					 mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
					 mMockAlarmReceiver = new MockAlarmReceiver();					
					 
					 Intent it = new Intent(ACTION_ON_TIMER);
					 mSender = PendingIntent.getBroadcast(mContext, 0, it, PendingIntent.FLAG_CANCEL_CURRENT);
					 
					 IntentFilter filter = new IntentFilter(ACTION_ON_TIMER);
					 filter.addAction(Intent.ACTION_TIME_CHANGED);
					 filter.addAction(Intent.ACTION_DATE_CHANGED);
					 filter.addAction(Intent.ACTION_TIMEZONE_CHANGED); 		 
					 mContext.registerReceiver(mMockAlarmReceiver, filter);						 
					 
				 }
				 if(null != mAlarmManager){
//					 running = true;
//					 PalmchatLogUtils.println("AfStartRTCAlarm running...");
//					 mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + elipse_ms, repeat_ms, mSender);
				 }
				mHandler.sendEmptyMessageDelayed(MSG_ON_TIMER, elipse_ms);	    
		 }
//		stopWakeLock();
	}
	
	public void AfStopRTCAlarm()
	{
		 synchronized(mObject){
			mHandler.removeCallbacksAndMessages(null);
			if( null != mAlarmManager && running){
				mAlarmManager.cancel(mSender);
				running = false;
			}			
		 }
	}
}