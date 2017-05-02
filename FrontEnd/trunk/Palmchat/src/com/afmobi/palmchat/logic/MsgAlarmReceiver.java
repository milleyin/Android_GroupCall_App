package com.afmobi.palmchat.logic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.afmobi.palmchat.log.PalmchatLogUtils;

/**
 * 
 * @author heguiming
 * 
 * msg alarm receiver
 */
public class MsgAlarmReceiver extends BroadcastReceiver {

	private static final String TAG = MsgAlarmReceiver.class.getCanonicalName();

	@Override
	public void onReceive(Context context, Intent intent) {

		PalmchatLogUtils.e(TAG, "----alarm request start!----intent.getAction()="+intent.getAction());
		if (intent.getAction().equals(MsgAlarmManager.MSG_RECEIVER)) {
			MsgAlarmManager.getInstence().requestMessage(intent.getAction());
		} 
	}

}
