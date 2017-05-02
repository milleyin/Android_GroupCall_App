package com.afmobi.palmchat.broadcasts;


import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.services.AppStatusService;
import com.afmobi.palmchat.util.CommonUtils;
import com.core.Consts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

public class AppReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent != null) {
			String action = intent.getAction();
			PalmchatLogUtils.println("AppReceiver  action  "+action);
			if(Intent.ACTION_BOOT_COMPLETED.equals(action)){
				PalmchatLogUtils.println("PalmchatApp  PalmchatApp.getOsInfo().getMcc()  end  onReceive  "+PalmchatApp.getOsInfo().getMcc());
			}
			if (Intent.ACTION_BOOT_COMPLETED.equals(action) || ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {//
//				if(ConnectivityManager.CONNECTIVITY_ACTION.equals(action)){
					PalmchatApp app = (PalmchatApp) context.getApplicationContext();
					PalmchatLogUtils.println("AppReceiver  app.isInit()  "+app.isInit()
							+"  isCanUseSdCard  "+CommonUtils.IsCanUseSdCard()
							+"  isCanUseSimCard  "+CommonUtils.isCanUseSim(context)
							+"  countryCode  "+PalmchatApp.getOsInfo().getCountryCode());
					if(CommonUtils.isNetworkAvailable(context) && app.isInit()){
						PalmchatLogUtils.println("AppReceiver  has network  "+true);
						AppStatusService.start(context, true, true);
						app.mAfCorePalmchat.AfPollSetStatus(Consts.POLLING_STATUS_RUNNING);
					}else{
						PalmchatLogUtils.println("AppReceiver  has network  "+false);
//						app.mAfCorePalmchat.AfPollSetStatus(Consts.POLLING_STATUS_PAUSE);
					}
//				}
//				boolean isStart = SharePreferenceService.getInstance(context).getStartPush();
//				LogUtils.e(action, "isStart  "+isStart);
//				LogUtils.d("AppReceiver", "action: " + action + " isStart = " + isStart);
//				if (isStart) {
//					if( ConnectivityManager.CONNECTIVITY_ACTION.equals(action) ){
//						
//						if( DataManager.isExecuting() || null != ((ChatApp) context.getApplicationContext()).getCurrentActivity() /**存在Activity*/){
//							LogUtils.d("AppReceiver", "application is running");
//							return;
//						}
//					}
//					
//					PushService.token = SharePreferenceService.getInstance(context).getToken();
//					PushService.mtoken = SharePreferenceService.getInstance(context).getMToken();
//					PushService.session = SharePreferenceService.getInstance(context).getSession();
//					PushService.ptoken = SharePreferenceService.getInstance(context).getPToken();				
//					LoginInfo info = SharePreferenceService.getInstance(context).getLoginInfo();
//					if( null != info){
//						DataManager.getInstance().setLoginInfo(info);
//					}
//					DataManager.getInstance().setChats(ChatsSQLService.getInstance(context).getDialogue());
//					PushService.startAction(context);	
//					LogUtils.d("AppReceiver", "start PushService");
//
//				}
			}
			
			
//			if(Intent.ACTION_SCREEN_OFF.equals(action)){
//				AppStatusService.start(context, true, false);
//			} else if(Intent.ACTION_SCREEN_ON.equals(action)){
//				AppStatusService.start(context, true, true);
//			} else 
			
			
//			if(Intent.ACTION_PACKAGE_RESTARTED.equals(action)){
//				AppStatusService.start(context, true, true);
//			} else if(Intent.ACTION_USER_PRESENT.equals(action)){
//				AppStatusService.start(context, true, true);
//			} else if(AppStatusService.ACTION_START.equals(action)){
//				AppStatusService.sendRequts(context, false);
//			}
	        	
			
		}
	}


}
