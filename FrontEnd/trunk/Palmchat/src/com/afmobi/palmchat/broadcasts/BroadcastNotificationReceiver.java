package com.afmobi.palmchat.broadcasts;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.ui.activity.social.BroadcastNotificationActivity;
import com.afmobi.palmchat.ui.activity.social.BroadcastNotificationData;
import com.core.cache.CacheManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadcastNotificationReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
//		delete read brd notify msg
//		PalmchatApp.getApplication().mAfCorePalmchat.AfDBBCNotifyDeleteByStatus
//		(CacheManager.getInstance().getMyProfile().afId, BroadcastNotificationData.STATUS_READ);
		
		
//		set unread brd notify msg to read
//		PalmchatApp.getApplication().mAfCorePalmchat.
//		AfDBBCNotifyUpdataAllStatus(CacheManager.getInstance().getMyProfile().afId, BroadcastNotificationData.STATUS_UNREAD, 
//				BroadcastNotificationData.STATUS_READ);
		
		Intent intent1 = new Intent(context, BroadcastNotificationActivity.class);
		intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent1);
	}
 
}
 