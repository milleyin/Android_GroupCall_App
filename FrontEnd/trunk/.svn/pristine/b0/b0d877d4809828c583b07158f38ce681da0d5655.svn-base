package com.afmobi.palmchat.logic;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.util.CommonUtils;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @author heguiming
 * msg alarm manager
 */
@SuppressLint("UseSparseArrays")
public class MsgAlarmManager implements AfHttpResultListener {

	private static final String TAG = MsgAlarmManager.class.getCanonicalName();
 
	public static final String MSG_RECEIVER = "com.afmobi.palmchat.logic.MSG_REQUEST";

	public static final int MSG_ALARM = 0x1001;
	 
	/**
	 * 提交前后台状态的时间间隔
	 */
	public static final long MSG_TIME = 300 * 1000;
	 
	
	private int index;

	/**
	 * 可定义多个闹钟
	 */
	private HashMap<Integer, PendingIntent> mPendingIntents = new HashMap<Integer, PendingIntent>();

	/**
	 * 单例
	 */
	private static MsgAlarmManager onlyInstance;
	
	private final int LAST_STATUS_FOREGROUND = 0;
	private final int LAST_STATUS_BACKGROUND = 1;
	private int mStatus = LAST_STATUS_BACKGROUND;//0前台,1后台   
	private boolean isForeGround = true;
	private boolean isReLogin = false;

	/**
	 * 私有构造
	 */
	private MsgAlarmManager() {
//		initEngine();
		resetSaveReadyTime();
	}

	public boolean isForeGround() {
		return isForeGround;
	}

	public void setForeGround(boolean isForeGround) {
		if (!this.isForeGround && isForeGround) {
			new ReadyConfigXML().saveReadyTime();
		} else if (this.isForeGround && !isForeGround) {
			new ReadyConfigXML().saveReadyUseTime();
		}
		
		PalmchatLogUtils.e(TAG, "setForeGround:" + isForeGround);
		this.isForeGround = isForeGround;
	}

	public boolean isReLogin() {
		return isReLogin;
	}

	public void setReLogin(boolean isReLogin) {
		this.isReLogin = isReLogin;
	}

	/**
	 * 单例模式
	 * 
	 * @return
	 */
	public static synchronized MsgAlarmManager getInstence() {
		if (onlyInstance == null) {
			onlyInstance = new MsgAlarmManager();
		}
		return onlyInstance;
	}

	/**
	 * 顺延定时拉取消息的定时
	 */
	public void resetMsgAlarm() {
		PalmchatLogUtils.e(TAG, "----resetMsgAlarm!!!.......");
		// 重设消息拉取定时
		setAlarm(MsgAlarmManager.MSG_ALARM, MsgAlarmManager.MSG_TIME);
	}
/*	
	public void resetMsgForegroundLocation() {
		PalmchatLogUtils.e(TAG, "----resetMsgForegroundLocation!!!.......");
		// 前台重设消息拉取定时取lat,log
		setAlarm(MsgAlarmManager.MSG_FOREGROUND_LOCATION, MsgAlarmManager.MSG_FOREGROUND_LOCATION_TIME);
	}
	public void resetMsgbackgroundLocation() {
		PalmchatLogUtils.e(TAG, "----resetMsgbackgroundLocation!!!.......");
		// 重设消息拉取定时取lat,log
		setAlarm(MsgAlarmManager.MSG_BACKGROUND_LOCATION, MsgAlarmManager.MSG_BACKGROUND_LOCATION_TIME);
	}*/

	/**
	 * 顺延定时拉取邮件和彩信的定时
	 */
	public void resetMsgAlarm(String action) {
		if (action != null) {
			// 定时拉取消息
			if (action.equals(MsgAlarmManager.MSG_RECEIVER)) {
				PalmchatLogUtils.e(TAG, "will reset the mms event");
				setAlarm(MsgAlarmManager.MSG_ALARM, MsgAlarmManager.MSG_TIME);
				
			} else {
				PalmchatLogUtils.e(TAG, "nothing.......");
			}
		}
	}

	/**
	 * 设置一个定时拉取
	 */
	public void setAlarm(int key, long time) {
		synchronized (mPendingIntents) {
			PendingIntent pi = mPendingIntents.get(key);
			Intent i = new Intent(PalmchatApp.getApplication(),MsgAlarmReceiver.class);
			if (key == MSG_ALARM) {
				i.setAction(MSG_RECEIVER);
			}
			/*else if(key == MSG_FOREGROUND_LOCATION||key == MSG_BACKGROUND_LOCATION){
				i.setAction(MSG_LOCATION);
			}*/
			// 初始化设置
			if (pi == null) {
				pi = PendingIntent.getBroadcast(PalmchatApp.getApplication(), key,
						i, 0);
				mPendingIntents.put(key, pi);
			}
			// 重新设置闹铃
			AlarmManager alarmManager = (AlarmManager) PalmchatApp.getApplication().getSystemService(Context.ALARM_SERVICE);
			
			alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, pi);

			PalmchatLogUtils.e(TAG, "----setAlarm!!!.......");
			
			PalmchatLogUtils.e(TAG, "AlarmManager setMMSAlarm :" + i.getAction() + " id: " + time + mPendingIntents.size());
		}
	}

	/**
	 * 根据类型取消闹钟
	 */
	public void cancelAlarm(int key) {
		AlarmManager alarmManager = (AlarmManager) PalmchatApp.getApplication().getSystemService(Context.ALARM_SERVICE);
		synchronized (mPendingIntents) {
			if (mPendingIntents.get(key) != null) {
				alarmManager.cancel(mPendingIntents.get(key));
				mPendingIntents.clear();
				PalmchatLogUtils.i(TAG, "AlarmManager cancelAlarm :" + " key: " + key);
			}
		}
	}

	/**
	 * 取消全部定时
	 */
	public void cancelAllAlarm() {
		AlarmManager alarmManager = (AlarmManager) PalmchatApp.getApplication()
				.getSystemService(Context.ALARM_SERVICE);
		synchronized (mPendingIntents) {
			for (PendingIntent pi : mPendingIntents.values()) {
				alarmManager.cancel(pi);
				PalmchatLogUtils.i(TAG, "AlarmManager All cancelAlarm :");
			}
			mPendingIntents.clear();
		}
	}
	
	private void resetSaveReadyTime() {
		long saveReadyTime = new ReadyConfigXML().getLong(ReadyConfigXML.KEY_RECEPTION);
		
		if (saveReadyTime == 0L) {
			new ReadyConfigXML().saveReadyTime();
		}
	}

	/**
	 * 每隔一段时间 就告诉服务器我的前后台状态
	 * 
	 * @param action
	 */
	public void requestMessage(String action) {
		resetSaveReadyTime();
		
//		MobclickAgent.updateOnlineConfig(PalmchatApp.getApplication());
		
		if (new ReadyConfigXML().canUpdate()) {//判断是否满足24小时上传一次统计数据的需求 
			if (index % 50 == 0) {
				PalmchatLogUtils.e(TAG, "-----Can Update");
				new ReadyConfigXML().saveReadyUseTime();
				if (CacheManager.getInstance().isLoginSuccess()) {
					PalmchatLogUtils.e(TAG, "-----Can Update--isLoginSuccess");
					PalmchatApp.getApplication().mAfCorePalmchat.AfHttpStatistic(true, true, new ReadyConfigXML().getLoginSuccessHttpJsonStr(), null, this);
				} else {
					PalmchatLogUtils.e(TAG, "-----Can Update--isNotLogin");
					PalmchatApp.getApplication().mAfCorePalmchat.AfHttpStatistic(false, true, new ReadyConfigXML().getNoLoginHttpJsonStr(), null, this);
				}
			}
		} 
		
		PalmchatLogUtils.e(TAG, "-----mStatus="+mStatus+",isReLogin="+isReLogin+",isForeGround="+isForeGround);
		if (CacheManager.getInstance().getMyProfile().afId != null) {
			if (isForeGround) {
					if (mStatus == LAST_STATUS_BACKGROUND || isReLogin) {
						// mStatus = LAST_STATUS_FOREGROUND;
						if (CommonUtils.isNetworkAvailable(PalmchatApp.getApplication())) {
							PalmchatLogUtils.i(TAG, "AppStatusService  send foreground...request");
							PalmchatApp.getApplication().mAfCorePalmchat.AfHttpLoginStatus(Consts.LOGIN_STATUS_FRONT, LAST_STATUS_FOREGROUND, this);
							PalmchatLogUtils.e(TAG, "----requestMessage:----LAST_STATUS_FOREGROUND");
						} else {
							resetMsgAlarm();
						}
					} else {
						resetMsgAlarm();
					}
				} else {
					PalmchatLogUtils.i(TAG, "AppStatusService  send background...mStatus  " + mStatus);
					if (mStatus == LAST_STATUS_FOREGROUND || isReLogin) {
						if (CommonUtils.isNetworkAvailable(PalmchatApp.getApplication())) {
							PalmchatLogUtils.i(TAG, "AppStatusService  send background...request");
							// mStatus = LAST_STATUS_BACKGROUND;
							PalmchatApp.getApplication().mAfCorePalmchat.AfHttpLoginStatus(Consts.LOGIN_STATUS_BACK, LAST_STATUS_BACKGROUND, this);
							PalmchatLogUtils.e(TAG, "----requestMessage:----LAST_STATUS_BACKGROUND");
						} else {
							resetMsgAlarm();
						}
					} else {
						resetMsgAlarm();
					}
				}
		} else {
			resetMsgAlarm();
		}
		index++;
	}
	
   
	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result,Object user_data) {
		PalmchatLogUtils.e(TAG, "----AfOnResult--MsgAlarm:flag:" + flag + "----code:" + code);
		if (Consts.REQ_CODE_SUCCESS == code) {
			if (flag == Consts.REQ_FLAG_STATISTIC) {
				new ReadyConfigXML().saveUpdateTime();
				new ReadyConfigXML().clearLoginSuccessData();
				new ReadyConfigXML().clearNoLoginData();
			} else {
				if (null != user_data) {
					mStatus = (Integer) user_data;
					setReLogin(false);
				}
			}
		}
		resetMsgAlarm();
	}
	
}
