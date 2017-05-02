package com.afmobi.palmchat.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.text.TextUtils;

/**
 * 短信接收器  拦截注册短信获取验证码
 * 
 * @author zhh
 * 
 */
public class SMSBroadcastReceiver extends BroadcastReceiver {

	private MessageListener mMessageListener;
	public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

	public SMSBroadcastReceiver() {
		super();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {

			Object[] pdus = (Object[]) intent.getExtras().get("pdus");
			for (Object pdu : pdus) {
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
				// 短信内容
				String content = smsMessage.getDisplayMessageBody().toString().trim();
				if (!TextUtils.isEmpty(content)) {
					if(content.contains("Palmchat")) {
						if (content.length() > 5) {
							content = content.substring(0, 6);
							if (mMessageListener != null) {
								mMessageListener.onReceived(content);
							}
						}
					}
				}
			//	abortBroadcast();  //中断广播继续传递

			}

			// Object[] pdus = (Object[]) intent.getExtras().get("pdus");
			// for (Object pdu : pdus) {
			// SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
			// String sender = smsMessage.getDisplayOriginatingAddress();
			// // 短信内容
			// String content = smsMessage.getDisplayMessageBody();
			// long date = smsMessage.getTimestampMillis();
			// Date tiemDate = new Date(date);
			// SimpleDateFormat simpleDateFormat = new
			// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// String time = simpleDateFormat.format(tiemDate);
			//
			// // 过滤不需要读取的短信的发送号码
			// if ("+8613450214963".equals(sender)) {
			// mMessageListener.onReceived(content);
			// abortBroadcast();
			// }
			// }
		}

	}

	// 回调接口
	public interface MessageListener {
		public void onReceived(String message);
	}

	public void setOnReceivedMessageListener(MessageListener messageListener) {
		this.mMessageListener = messageListener;
	}

}
