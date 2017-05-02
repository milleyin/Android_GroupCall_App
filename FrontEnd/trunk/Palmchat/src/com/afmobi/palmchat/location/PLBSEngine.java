package com.afmobi.palmchat.location;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.Log;

import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 高德定位
 * @author xiaolong
 *
 */
public class PLBSEngine implements AMapLocationListener {
    public void exitGeoLoaction() {
        if (mLocationManagerProxy != null) {
            mLocationManagerProxy.removeUpdates(this);
            mLocationManagerProxy.destroy();
        }
        mLocationManagerProxy = null;

    }

    private LocationManagerProxy mLocationManagerProxy;

    private void startBaiduGeo(Context context) {
        mLocationManagerProxy = LocationManagerProxy.getInstance(context);

        //此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        //注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
        //在定位结束后，在合适的生命周期调用destroy()方法
        //其中如果间隔时间为-1，则定位只定一次
        mLocationManagerProxy.requestLocationData(
                LocationProviderProxy.AMapNetwork, 300 * 1000, 100, this);

        mLocationManagerProxy.setGpsEnable(false);
    }

    private static final int DEFAULT_GPS_LAT = 0;
    private static final int DEFAULT_GPS_LON = 0;
    protected static final String TAG = "QLBSEngine"; 
 

    private Context context;
    private PLBSCallback notification;
     
    private double lat = 0.0d;
    private double lon = 0.0d;  

    public PLBSEngine(Context context, PLBSCallback notification) {
        this.context = context;
        this.notification = notification;
        resetData();
    }

    public synchronized boolean startLocation() {
        resetData();
        startBaiduGeo(context);
        return true;
    }

    private void notifyCallback() {
        int result = PLBSCallback.RESULT_LOCATION_ERROR;
        if (isGpsValid()) {
            result = PLBSCallback.RESULT_LOCATION_SUCCESS;
        }
        PalmchatLogUtils.e(TAG, "----notifyCallback----");
        if (notification != null) {
            notification.onLocationNotification(result);
        }
    }


    private void resetData() {
     
        lat = DEFAULT_GPS_LAT;
        lon = DEFAULT_GPS_LON;

 

    }
 

    public boolean isGpsValid() {
        return !(lat == DEFAULT_GPS_LAT && lon == DEFAULT_GPS_LON);
    }
 
 
 

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    public static String locationDesc;

    @Override
    public void onLocationChanged(AMapLocation arg0) {
        // TODO Auto-generated method stub
        PalmchatLogUtils.i("WXL", "onLocationChanged=" + arg0.getAMapException().getErrorCode());
        if (arg0 != null && arg0.getAMapException().getErrorCode() == 0) {
            //获取位置信息
            lat = arg0.getLatitude();
            lon = arg0.getLongitude();
            SharePreferenceUtils.getInstance(context).setLatitudeAndLongitude(lat, lon); 
            notifyCallback(); 
            locationDesc = "";
            Bundle locBundle = arg0.getExtras();
            if (locBundle != null) {
                locationDesc = arg0.getAddress(); 
            }
            String _desc = "lat=" + lat + " lon=" + lon + " desc=" + locationDesc;
            PalmchatLogUtils.i("WXL", _desc);

        } else {
            notifyCallback();
        }
    }
}