package com.afmobi.palmchat.eventbusmodel;
 
import android.content.Context;

/**
 * 处理转发广播 成功 或失败后返回
 * @author xiaolong
 *
 */
public class ShareBroadcastResultEvent { 
	public boolean isSuccess;
	public int errorCode;
	public ShareBroadcastResultEvent( boolean result,int code){
	 
		isSuccess=result;
		errorCode=code;
	}
}
