package com.afmobi.palmchat.ui.activity.social;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.widget.Toast;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.log.PalmchatLogUtils;

public class ShakeListener implements SensorEventListener {

	private static int SPEED_SHRESHOLD = 2000;

	private static final int UPTATE_INTERVAL_TIME = 70;

	private SensorManager sensorManager;

	private Sensor sensor;

	private OnShakeListener onShakeListener;

	private Context mContext;

	private float lastX;
	private float lastY;
	private float lastZ;

	private long lastUpdateTime;

	public ShakeListener(Context mContext) {
		this.mContext = mContext;
		start();
	}

	public void start() {
		sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);

	}

	public void stop() {
		if (sensorManager != null) {
			sensorManager.unregisterListener(this, sensor);
			sensorManager = null;
			sensor = null;
			onShakeListener = null;
			mContext = null;
		}
	}

	public void setOnShakeListener(OnShakeListener listener) {
		onShakeListener = listener;
	}

	private long currentTime;

	@Override
	public void onSensorChanged(SensorEvent event) {

		long currentUpdateTime = System.currentTimeMillis();

		long timeInterval = currentUpdateTime - lastUpdateTime;

		if (timeInterval < UPTATE_INTERVAL_TIME) {
			return;
		}

		lastUpdateTime = currentUpdateTime;

		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];

		float deltaX = x - lastX;
		float deltaY = y - lastY;
		float deltaZ = z - lastZ;

		lastX = x;
		lastY = y;
		lastZ = z;

		double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ)/ timeInterval * 10000;
//		PalmchatLogUtils.e("-----getOsInfo---------", PalmchatApp.getOsInfo().getUa()+"|"+ Build.VERSION.SDK_INT);
		if (PalmchatApp.getOsInfo().getUa().equals("TECNO-A7") && Build.VERSION.SDK_INT == 19 ) {
			SPEED_SHRESHOLD = 5000 ;
		}
			
		if (speed >= SPEED_SHRESHOLD) {
			if (System.currentTimeMillis() - currentTime < 3000) {
				return;
			}
			currentTime = System.currentTimeMillis();
			if (onShakeListener != null) {
				onShakeListener.onShake();
			}
		}
		
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	public interface OnShakeListener {
		public void onShake();
	}
	
	public int getAndroidOSVersion()
    {
     	 int osVersion = 0;
     	 try
     	 {
     		osVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
     	 }
     	 catch (NumberFormatException e)
     	 {
     		osVersion = 0;
     	 }
     	 
     	 return osVersion;
   }
}