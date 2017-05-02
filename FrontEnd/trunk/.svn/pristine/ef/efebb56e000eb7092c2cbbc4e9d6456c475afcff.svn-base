package com.afmobi.palmchat.util;

import java.io.File;

import com.afmobi.palmchat.log.PalmchatLogUtils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;

/**
 * 距离传感器管理类
 * @author Transsion
 *
 */
public class SensorManagerUtil implements SensorEventListener {
	/**声音管理器*/
    private AudioManager audioManager = null; 
    /**传感器管理器*/
    private SensorManager _sensorManager = null;
    /**传感器实例*/
    private Sensor mProximiny = null;
    /**上下文*/
    private Context context;
    /**电源锁*/
	private PowerManager.WakeLock localWakeLock = null;
    /**耳机检测广播*/
    private MyHeadsetPlugBroadcastReceiver headsetPlugReceiver;
	/**电源锁注册权限*/
    private final int PROXIMITY_SCREEN_OFF_WAKE_LOCK = 32;
    /**距离传感器实例*/
    public static SensorManagerUtil sensorManagerUtil;
    /**屏幕锁状态*/
	private boolean acquireWakeLock = false;
    /**是否插入耳机*/
    private boolean is_Insert_arphone = false;
    /***/
    private Handler handler = new Handler();
    /**监听对象*/
	private Activity mActivity;
	/**自定义传感器对mediaPlayer的回调*/
	private SensorChangedCallback mSensorChangedCallback;
	/**加个传感器值的判断*/
	private float mPreSensorValue=0.0f;
	private boolean mJustWakeLock;
	
    public SensorManagerUtil() {

    }

    public static SensorManagerUtil getInstance() {
        if (sensorManagerUtil == null) {
            sensorManagerUtil = new SensorManagerUtil();
        }
        return sensorManagerUtil;
    }
    
    /**
     * 获取目前监听对象
     * @return
     */
	public Activity getmActivity() {
		return mActivity;
	}

	/**
	 * 设置监听目标
	 * @param mActivity
	 */
	public void setmActivity(Activity mActivity) {
		this.mActivity = mActivity;
	}

	/**
	 * 屏幕锁状态
	 * @return
	 */
    public boolean isAcquireWakeLock() {
        return acquireWakeLock;
    }


    /**
     * 取消注册
     */
    public void unregister_MyHeadsetPlugBroadcastReceiver() {
        if (headsetPlugReceiver != null && context != null) {
            context.unregisterReceiver(headsetPlugReceiver);
            headsetPlugReceiver = null;
        }

        unregister_sensor();
    }

	/**
	 *  只要播放的时候，就注册一下耳机和距离感应
	 * @param c
	 * @param sensorChangedCallback
	 */
	public void register_MyHeadsetPlugBroadcastReceiver(Context c , SensorChangedCallback sensorChangedCallback) {
		this.context = c;
		this.mJustWakeLock = false;
		this.mSensorChangedCallback = sensorChangedCallback;
		if (headsetPlugReceiver == null) {
			headsetPlugReceiver = new MyHeadsetPlugBroadcastReceiver();
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction("android.intent.action.HEADSET_PLUG");
			c.registerReceiver(headsetPlugReceiver, intentFilter);
		}
        register_sensor();
    }

	/**
	 * 设置 只控制屏幕 ，不控制切换声音
	 * @param c
	 * @param sensorChangedCallback
	 * @param justWakeLock
     */
	public void register_MyHeadsetPlugBroadcastReceiver(Context c,SensorChangedCallback sensorChangedCallback,boolean justWakeLock){
		this.context = c;
		this.mJustWakeLock = justWakeLock;
		this.mSensorChangedCallback = sensorChangedCallback;
		if (headsetPlugReceiver == null) {
			headsetPlugReceiver = new MyHeadsetPlugBroadcastReceiver();
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction("android.intent.action.HEADSET_PLUG");
			c.registerReceiver(headsetPlugReceiver, intentFilter);
		}
		register_sensor();
	}

	/**
	 * 注册距离传感器
	 */
	private void register_sensor() {
		audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		_sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		mProximiny = _sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		_sensorManager.registerListener(this, _sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
				SensorManager.SENSOR_DELAY_NORMAL);
		acquireWakeLock(false);
	}

	/**
	 * 取消注册
	 */
    private void unregister_sensor() {
        if (_sensorManager != null) {
            releaseWakeLock();
            _sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(null==mActivity){
        	unregister_sensor();
        	return;
        }        
        float range = event.values[0];
        if(mPreSensorValue==range){
        	return;
        }
        mPreSensorValue = range;
        PalmchatLogUtils.e("onSensorChanged", "-->  " + range + "  |  " + mProximiny.getMaximumRange());
		if (range >= mProximiny.getMaximumRange() && range > 0.0) { // 正常模式
			PalmchatLogUtils.e("onSensorChanged", "扬声器模式");
			if(!mJustWakeLock){
				audioManager.setMode(AudioManager.MODE_NORMAL);
				audioManager.setSpeakerphoneOn(true);
				if(null!=mActivity) {
					mActivity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
				}
			}

			if(null!=mSensorChangedCallback){
				mSensorChangedCallback.onChanged(false);
			}

			if (!(is_Insert_arphone)) {
				releaseWakeLock(); // 释放设备电源锁
			}
		} else { // 听筒模式
			PalmchatLogUtils.e("onSensorChanged", "听筒模式");
			if(!mJustWakeLock){
				if(null!=mActivity) {
					mActivity.setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
				}
				audioManager.setMode(AudioManager.MODE_NORMAL);
				audioManager.setSpeakerphoneOn(false);
			}

			if(null!=mSensorChangedCallback){
				mSensorChangedCallback.onChanged(true);
			}
			if(!mJustWakeLock){
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {

						if (audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL) < audioManager
								.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL)) {
							audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
									audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
									AudioManager.FLAG_PLAY_SOUND);
						}
					}
				}, 300);
			}

			if (!(is_Insert_arphone)) {
				acquireWakeLock(true);// 申请设备电源锁
			}
		}
	}

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * 耳机监听
     * @author Transsion
     *
     */
    public class MyHeadsetPlugBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("state")) {
                if (intent.getIntExtra("state", 0) == 0) {
                    is_Insert_arphone = false;
                } else if (intent.getIntExtra("state", 0) == 1) {
                    is_Insert_arphone = true;
                }
            }
        }
    }

    /**
     * 请求电源锁
     * @param isLock
     */
	private void acquireWakeLock(boolean isLock) {
		releaseWakeLock();
		if (isLock) {
			PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
			localWakeLock = pm.newWakeLock(PROXIMITY_SCREEN_OFF_WAKE_LOCK, this.getClass().getCanonicalName());
			localWakeLock.acquire();
			acquireWakeLock = true;
		} else
			acquireWakeLock = false;
	}

	/**
	 * 释放电源锁
	 */
	private void releaseWakeLock() {
		if (localWakeLock != null){
			if(localWakeLock.isHeld() ) {
				localWakeLock.release();
			}
			localWakeLock = null;
			acquireWakeLock = false;
		}
	}
	
	/**
	 * 距离传感器改变了 回调 mediaPlayer
	 * @author Transsion
	 *
	 */
	public interface SensorChangedCallback {
		public void onChanged(boolean isEarphone);
	}
}
