package com.afmobi.palmchat.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.core.AfPrizeInfo;
import com.core.Consts;
import com.core.cache.CacheManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

public class SharePreferenceUtils {

	private Context context;
    private String name;

	private SharePreferenceUtils(Context context) {
		this.context = context;
	}

    public SharePreferenceUtils(Context context, String name) {
        this.context = context;
        this.name = name;
    }
	
	private static SharePreferenceUtils instance;
	public static SharePreferenceUtils getInstance(Context context) {
		if (instance == null) {
			instance = new SharePreferenceUtils(context);
		}
		return instance;
	}

    public static SharePreferenceUtils getInstance(Context context,String name) {
        if (instance == null) {
            instance = new SharePreferenceUtils(context,name);
        }
        return instance;
    }


	public void setSaveCountryData(boolean success) {
		SharedPreferences account =context. getSharedPreferences(RequestConstant.ACCOUNT, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putBoolean(JsonConstant.KEY_COUNTRY, success);
		editor.commit();
	}

	public boolean isSaveCountryData() {
		SharedPreferences account =context. getSharedPreferences(RequestConstant.ACCOUNT, Context.MODE_PRIVATE);
		return account.getBoolean(JsonConstant.KEY_COUNTRY, true);//update false to true because of region.db has 182 countries.
	} 
	

	/**set launch true or false*/
	public void saveStartPush(boolean flag) {
		SharedPreferences setting =context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		Editor editor = setting.edit();
		editor.putBoolean(RequestConstant.AUTO_START, flag);
		editor.commit();
	}

	public void setLocalLanguage(String localLanguage) {
		SharedPreferences account = context. getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putString(JsonConstant.KEY_LOCAL_LANGUAGE, localLanguage);
		editor.commit();
	}
	
	public String getLocalLanguage() {
		SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		return account.getString(JsonConstant.KEY_LOCAL_LANGUAGE, LanguageUtil.LANGUAGE_DEFAULT);
	}

    public void setPredictAddress(AfPrizeInfo.AddressRecord localLanguage) {
        SharedPreferences account = context. getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
        Editor editor = account.edit();
        //editor.put(JsonConstant.KEY_PREDICT_ADDRESS, localLanguage);
        editor.commit();
    }

    public String getPredictAddress() {
        SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
        return account.getString(JsonConstant.KEY_PREDICT_ADDRESS, LanguageUtil.LANGUAGE_DEFAULT);
    }
	
	/**set current profile album*/
	public void setProfileAblum(String localLanguage) {
		String afid = CacheManager.getInstance().getMyProfile().afId;
		if (!CommonUtils.isEmpty(afid)) {
			SharedPreferences account = context. getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
			Editor editor = account.edit();
			editor.putString(afid + "." + JsonConstant.KEY_PROFILE_ALBUM, localLanguage);
			editor.commit();
		}
	}
	
	/**get current profile album*/
	public String getProfileAblum() {
		String afid = CacheManager.getInstance().getMyProfile().afId;
		if (!CommonUtils.isEmpty(afid)) {
			SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
			return account.getString(afid + "." + JsonConstant.KEY_PROFILE_ALBUM, "01");
		}
		return "01";
	} 
	
	/**set is first time enter*/
	public void setIsFirstTimeEnter(boolean isFirst) {
		SharedPreferences account =context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putBoolean(JsonConstant.KEY_IS_FIRST_TIME, isFirst);
		editor.commit();
	}
	
	/**get is first time enter*/
	public boolean getIsFirstTimeEnter() {
		SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		return account.getBoolean(JsonConstant.KEY_IS_FIRST_TIME, true);
	}

	/** 是否显示编辑广播图片引导
	 */
	public void setShowEditBroadcastTips(  boolean isShow) {
		SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putBoolean(JsonConstant.KEY_IS_SHOW_BROADCAST_TIPS, isShow);
		editor.commit();
	}

	/** 获取是否同意条款
	 * @param
	 * @return
	 */
	public boolean getShowEditBroadcastTips( ) {
		SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		boolean isShow = account.getBoolean(JsonConstant.KEY_IS_SHOW_BROADCAST_TIPS, false);
		return isShow;
	}
	/** 设置是否已同意palmguess条款
	 * @param afid
	 * @param isAgree
	 */
	public void setIsAgreePalmguess(String afid ,boolean isAgree) {
		SharedPreferences account = context.getSharedPreferences(afid, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putBoolean(JsonConstant.KEY_IS_AGREE_PALMGUESS, isAgree);
		editor.commit();
	}

	/** 获取是否同意条款
	 * @param afid
	 * @return
	 */
	public boolean getIsAgreePalmguess(String afid) {
		SharedPreferences account = context.getSharedPreferences(afid, Context.MODE_PRIVATE);
		boolean isAgree = account.getBoolean(JsonConstant.KEY_IS_AGREE_PALMGUESS, false);
		return isAgree;
	}

	/** 设置是否有新的@me
	 * @param afid
	 * @param isAt
	 */
	public void setIsAtme(String afid ,boolean isAt) {
		SharedPreferences account = context.getSharedPreferences(afid, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putBoolean(JsonConstant.KEY_IS_AT_ME, isAt);
		editor.commit();
	}

	/** 获取是否有新的@me
	 * @param afid
	 * @return
	 */
	public boolean getIsAtme(String afid) {
		SharedPreferences account = context.getSharedPreferences(afid, Context.MODE_PRIVATE);
		boolean isAt = account.getBoolean(JsonConstant.KEY_IS_AT_ME, false);
		return isAt;
	}

	/** 获取是否有followed  follow消息的红点提示判断
	 * @param afid
	 * @return
	 */
	public boolean getIsFollowed(String afid) {
		SharedPreferences account = context.getSharedPreferences(afid, Context.MODE_PRIVATE);
		boolean isAt = account.getBoolean(JsonConstant.KEY_IS_AT_ME, false);
		return isAt;
	}

	/** 设置followed    follow消息的红点提示判断
	 * @param afid
	 * @param isAt
	 */
	public void setIsFolloed(String afid ,boolean isAt) {
		SharedPreferences account = context.getSharedPreferences(afid, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putBoolean(JsonConstant.KEY_IS_AT_ME, isAt);
		editor.commit();
	}

	/**set is first install*/
	public void setIsFirstInstall(boolean isFirst) {
		SharedPreferences account =context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putBoolean(JsonConstant.KEY_IS_FIRST_INSTALL, isFirst);
		editor.commit();
	}

	/**get is first install*/
	public boolean getIsFirstInstall() {
		SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		return account.getBoolean(JsonConstant.KEY_IS_FIRST_INSTALL, true);
	}

	/**set new version */
	public void setUpdateNewVersion(String updateVersion) {
		if(null==updateVersion){
			updateVersion =String.valueOf(0);
		}
		SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putString(JsonConstant.KEY_UPDATE_NEWVER,updateVersion);
		editor.commit();
	}

	/**获取上次检查要更新的版本号*/
	public String getUpdateNewVersion(){
		SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING,Context.MODE_PRIVATE);
		return account.getString(JsonConstant.KEY_UPDATE_NEWVER,String.valueOf(0));
	}
    
    /**zhh is  shelves*/
    public void setShelves(boolean isShelve) {
    	SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
    	Editor editor = account.edit();
    	editor.putBoolean(JsonConstant.KEY_IS_SHELVES,isShelve);
    	editor.commit();
    }
    
    /**zhh is  shelves*/
    public boolean isShelves() {
    	SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
    	return account.getBoolean(JsonConstant.KEY_IS_SHELVES,false);
    }
        
    /**获得前一次检查版本时**/
    public long getLastCheckTime() {
    	SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
    	return account.getLong(JsonConstant.KEYI_VER_CHECKTIME, 0);
    }
    
    /**记得当前版本检查时**/
    public void setCurrentVerCheckTime() {
    	long curCheckTime = System.currentTimeMillis();
    	SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
    	Editor editor = account.edit();
    	editor.putLong(JsonConstant.KEYI_VER_CHECKTIME, curCheckTime);
    	editor.commit();
    }

    /**获取tags版本*/
    public String getCurrentTagsVersion(){
    	SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
    	return account.getString(JsonConstant.KEY_VER_TAGS, "");
    }
    
    /**
     * 获得当前tag版本时间
     * @return
     */
    public long getCurrentTagsTime(){
    	SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
    	return account.getLong(JsonConstant.KEY_VER_TAGSTIME,0);
    }

	/**
	 * 设置当前账号的对应的版本信息
	 * @param account
     */
	public void setCurrentVersionnameByAccount(String account){
		SharedPreferences sharedPreferences = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		String version = AppUtils.getAppVersionName(context);
		editor.putString(account,version);
		editor.commit();
	}
	/**
	 * 根据账号名字取版本号
	 * @param account
	 * @return
     */
	public String getCurrentVersionnameByAccount(String account){
		SharedPreferences sharedPreferences = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		return sharedPreferences.getString(account,"");
	}
    
    /**设置当前tags版本*/
    public void setCurrentTagsVersion(String version){
    	SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
    	Editor editor = account.edit();
    	long curVerTagTime = System.currentTimeMillis();
    	if(!TextUtils.isEmpty(version)){
        	editor.putString(JsonConstant.KEY_VER_TAGS, version);
    	}
    	editor.putLong(JsonConstant.KEY_VER_TAGSTIME, curVerTagTime);
    	editor.commit();
    }


	/**send content*/
	public void setSendContent(String sendContent) {
		SharedPreferences account = context. getSharedPreferences(RequestConstant.ACCOUNT, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putString(JsonConstant.KEY_SEND_CONTENT, sendContent);
		editor.commit();
	}
	
	/**get send content*/
	public String getSendContent() {
		SharedPreferences account = context.getSharedPreferences(RequestConstant.ACCOUNT, Context.MODE_PRIVATE);
		return account.getString(JsonConstant.KEY_SEND_CONTENT, "");
	}
	
	
	public void setScoreTime(long scoreTime) {
		SharedPreferences account = context. getSharedPreferences(RequestConstant.ACCOUNT, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putLong(JsonConstant.KEY_SCORE_TIME, scoreTime);
		editor.commit();
	}
	
	public long getScoreTime() {
		SharedPreferences account = context.getSharedPreferences(RequestConstant.ACCOUNT, Context.MODE_PRIVATE);
		return account.getLong(JsonConstant.KEY_SCORE_TIME, 0L);
	}
	
	
	public void setScoreCount(int count) {
		SharedPreferences account = context. getSharedPreferences(RequestConstant.ACCOUNT, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putInt(JsonConstant.KEY_SCORE_COUNT, count);
		editor.commit();
	}
	
	public int getScoreCount() {
		SharedPreferences account = context.getSharedPreferences(RequestConstant.ACCOUNT, Context.MODE_PRIVATE);
		return account.getInt(JsonConstant.KEY_SCORE_COUNT, 0);
	}
	
	
	public void setScoreShow(boolean toShow) {
		SharedPreferences account = context. getSharedPreferences(RequestConstant.ACCOUNT, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putBoolean(JsonConstant.KEY_SCORE_TO_SHOW, toShow);
		editor.commit();
	}
	
	public boolean getScoreShow() {
		SharedPreferences account = context.getSharedPreferences(RequestConstant.ACCOUNT, Context.MODE_PRIVATE);
		return account.getBoolean(JsonConstant.KEY_SCORE_TO_SHOW, true);
	}
	
	public void setTouchGetScore(boolean touchGetScore) {
		SharedPreferences account =context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putBoolean(JsonConstant.KEY_TOUCH_GET_SCORE, touchGetScore);
		editor.commit();
	}
	
	public boolean getTouchGetScore() {
		SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		return account.getBoolean(JsonConstant.KEY_TOUCH_GET_SCORE, true);
	}
	public void setIsFacebookLogin(boolean isFBLogin) {
		SharedPreferences account = context. getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putBoolean( JsonConstant.KEY_ISFACEBOOK_LOGIN, isFBLogin);
		editor.commit();
	}
	
	public boolean getIsFacebookLogin(){
		SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		return account.getBoolean(JsonConstant.KEY_ISFACEBOOK_LOGIN, false);
	}
	public void setGuideStatus(int status) {
		SharedPreferences account = context. getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putInt(JsonConstant.GUIDE_STATUS, status);
		editor.commit();
	}
	
	public int getGuideStatus(){
		SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		return account.getInt(JsonConstant.GUIDE_STATUS, Consts.GUIDE_SLIDE);
	}
	
	public void setSelectedLanguagePosition(int position){
		SharedPreferences account = context. getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putInt(JsonConstant.GUIDE_SELECTED_LANGUAGE_POSITION, position);
		editor.commit();
	}
	
	public int getSelectedLanguagePosition(){
		SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		return account.getInt(JsonConstant.GUIDE_SELECTED_LANGUAGE_POSITION, 0);
	}

	public void setRegionDBVersion(String regionDBVersion) {
		SharedPreferences account = context. getSharedPreferences(RequestConstant.ACCOUNT, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putString(JsonConstant.KEY_REGION_DB_VERSION, regionDBVersion);
		editor.commit();
	}
	
	public String getRegionDBVersion() {
		// TODO Auto-generated method stub
		SharedPreferences account = context.getSharedPreferences(RequestConstant.ACCOUNT, Context.MODE_PRIVATE);
		return account.getString(JsonConstant.KEY_REGION_DB_VERSION, "1");
	}
	
	public void setMissNigeriaEnd(boolean missNigeriaEnd) {
		SharedPreferences account = context.getSharedPreferences(RequestConstant.ACCOUNT, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putBoolean(JsonConstant.KEY_MISS_NIGERIA_END, missNigeriaEnd);
		editor.commit();
	}
	
	public boolean getMissNigeriaEnd() {
		// TODO Auto-generated method stub
		SharedPreferences account = context.getSharedPreferences(RequestConstant.ACCOUNT, Context.MODE_PRIVATE);
		return account.getBoolean(JsonConstant.KEY_MISS_NIGERIA_END, false);
	}
	
	
	/**set BroadCast PageId*/
	/*public void saveBroadCast_PageId(String afid,int pageid) {
		SharedPreferences setting =context.getSharedPreferences(afid, Context.MODE_PRIVATE);
		Editor editor = setting.edit();
		editor.putInt(RequestConstant.BROADCAST_PAGEID, pageid);
		editor.commit();
	}
	
	public int getBroadCast_PageId(String afid) {
		SharedPreferences account = context.getSharedPreferences(afid, Context.MODE_PRIVATE);
		return account.getInt(RequestConstant.BROADCAST_PAGEID, 0);
	}*/
	
	/*public synchronized void saveBrdNotificationJson(String afid, String json) {
		SharedPreferences setting =context.getSharedPreferences(afid, Context.MODE_PRIVATE);
		Editor editor = setting.edit();
		editor.putString(RequestConstant.BROADCAST_NOTIFICATION_JSON, json);
		editor.commit();
	}
	
	public synchronized String getBrdNotificationJson(String afid) {
		SharedPreferences account = context.getSharedPreferences(afid, Context.MODE_PRIVATE);
		return account.getString(RequestConstant.BROADCAST_NOTIFICATION_JSON, "");
	}
	
	public synchronized void saveBrdNotificationNew(String afid, boolean isNew) {
		SharedPreferences setting =context.getSharedPreferences(afid, Context.MODE_PRIVATE);
		Editor editor = setting.edit();
		editor.putBoolean(RequestConstant.BROADCAST_NOTIFICATION_NEW, isNew);
		editor.apply();
//		editor.commit();
	}
	
	public synchronized boolean getBrdNotificationNew(String afid) {
		SharedPreferences account = context.getSharedPreferences(afid, Context.MODE_PRIVATE);
		return account.getBoolean(RequestConstant.BROADCAST_NOTIFICATION_NEW, false);
	}*/
	
	public void setThirdPartLogin(String afid, boolean isLogin) {
		SharedPreferences setting =context.getSharedPreferences(afid, Context.MODE_PRIVATE);
		Editor editor = setting.edit();
		editor.putBoolean(RequestConstant.THIRD_PART_LOGIN, isLogin);
		editor.commit();
	}
	
	public boolean getThirdPartLogin(String afid) {
		SharedPreferences account = context.getSharedPreferences(afid, Context.MODE_PRIVATE);
		return account.getBoolean(RequestConstant.THIRD_PART_LOGIN, false);
	}
	
	
	
	/**set friend req time*/
	public void setFriendReqTime(String my_afid,String stranger_afid) {
		SharedPreferences setting =context.getSharedPreferences(RequestConstant.FRIEND_REQ_TIME, Context.MODE_PRIVATE);
		Editor editor = setting.edit();
		editor.putLong(my_afid + stranger_afid, System.currentTimeMillis());
		editor.commit();
	}
	
	/**get friend req time*/
	public long getFriendReqTime(String my_afid,String stranger_afid) {
		SharedPreferences account = context.getSharedPreferences(RequestConstant.FRIEND_REQ_TIME, Context.MODE_PRIVATE);
		return account.getLong(my_afid + stranger_afid, 0);
	}


	public void setNotCompleteImei(boolean isCompleteImei) {
		// TODO Auto-generated method stub
		SharedPreferences account = context. getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putBoolean(JsonConstant.KEY_NOT_COMPLETE_IMEI, isCompleteImei);
		editor.commit();
	}
	
	public boolean getNotCompleteImei() {
		// TODO Auto-generated method stub
		SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		return account.getBoolean(JsonConstant.KEY_NOT_COMPLETE_IMEI, false);
	}
	
	
	
	public void setLastTime(String afid ,long time) {
		SharedPreferences account = context. getSharedPreferences(afid, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putLong(JsonConstant.KEY_LAST_DATATIME, time);
		editor.commit();
	}
	
	public Long getLastTime(String afid) {
		SharedPreferences account = context.getSharedPreferences(afid, Context.MODE_PRIVATE);	Editor editor = account.edit();
		long time = account.getLong(JsonConstant.KEY_LAST_DATATIME, 0);
		return time;
	}

	/** 设置palmguess版本，此版本用于提示是否有新活动
	 * @param afid
	 * @param version
	 */
	public void setPalmguessVersion(String afid ,int version) {
		SharedPreferences account = context. getSharedPreferences(afid, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putInt(JsonConstant.KEY_PALMGUESS_VERSION, version);
		editor.commit();
	}

	/**获取palmguess版本，此版本用于提示是否有新活动
	 * @param afid
	 * @return
	 */
	public int getPalmguessVersion(String afid) {
		SharedPreferences account = context.getSharedPreferences(afid, Context.MODE_PRIVATE);
		int time = account.getInt(JsonConstant.KEY_PALMGUESS_VERSION, 0);
		return time;
	}
	
	/**
	 * 设置一些动作音效的开关保�?
	 * @param afid
	 * @param isChecked
	 */
	public void setInAppSound(String afid ,boolean isChecked) {
		SharedPreferences account = context. getSharedPreferences(afid, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putBoolean(JsonConstant.KEY_INAPPSOUND, isChecked);
		editor.commit();
	}
	
	public boolean getInAppSound(String afid) {
		SharedPreferences account = context.getSharedPreferences(afid, Context.MODE_PRIVATE);	 
		boolean  isChecked = account.getBoolean(JsonConstant.KEY_INAPPSOUND, true);
		return isChecked;
	}
	 
	public long[] getTrendingLastTimeRefresh(String afid) {
		SharedPreferences account = context.getSharedPreferences(afid, Context.MODE_PRIVATE);
		long  time = account.getLong(JsonConstant.KEY_TRENDING_LASTREFRESH_TIME, 0);
		long  pageid = account.getLong(JsonConstant.KEY_TRENDING_LASTREFRESH_PAGEID, 0);
		long[] _return=new long[]{time,pageid};
		return _return;
	}
	/**
	 * 记录trending界面最后的刷新时间
	 * @param afid
	 * @param lasttime
	 * @param pageid
	 */
	public void setTrendingLastTimeRefresh(String afid,long lasttime,long pageid){
		SharedPreferences account = context. getSharedPreferences(afid, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putLong(JsonConstant.KEY_TRENDING_LASTREFRESH_TIME,lasttime);
		editor.putLong(JsonConstant.KEY_TRENDING_LASTREFRESH_PAGEID,pageid);
		editor.commit();
	}

	
	public void setLatitudeAndLongitude(double lat, double lng) {
//		if (!(lat == 0.0d && lng == 0.0d) || !(lat == 4.9E-324 || lng == 4.9E-324)) {
			if (lat == 1.0d) {
				lat=0.0d;
			}
			if (lng == 1.0d) {
				lng=0.0d;
			}
			PalmchatLogUtils.e("setLatitudeAndLongitude", "lat = "+lat+",lng = "+lng);
			SharedPreferences account = context. getSharedPreferences(RequestConstant.LAT_AND_LNG, Context.MODE_PRIVATE);
			Editor editor = account.edit();
			editor.putString(JsonConstant.KEY_LAT, lat + "");
			editor.putString(JsonConstant.KEY_LNG, lng + "");
			editor.commit();
//		}
	}
	
	public String getLat() {
		SharedPreferences account = context.getSharedPreferences(RequestConstant.LAT_AND_LNG, Context.MODE_PRIVATE);
		return account.getString(JsonConstant.KEY_LAT, "0");
	}
	public String getLng() {
		SharedPreferences account = context.getSharedPreferences(RequestConstant.LAT_AND_LNG, Context.MODE_PRIVATE);
		return account.getString(JsonConstant.KEY_LNG, "0");
	}


	/*public void setLoginType(byte b_login_type) {
		// TODO Auto-generated method stub
		int login_type = 2;
		switch (b_login_type) {
		case Consts.AF_LOGIN_PHONE:
			login_type = 1;
			break;
		case Consts.AF_LOGIN_AFID:
			login_type = 2;
			break;
		case Consts.AF_LOGIN_IMEI:
			login_type = 3;
			break;
		case Consts.AF_LOGIN_EMAIL:
			login_type = 4;
			break;
		case Consts.AF_LOGIN_FACEBOOK:
			login_type = 5;
			break;
		case Consts.AF_LOGIN_GOOGLE:
			login_type = 6;
			break;

		default:
			break;
		}
		SharedPreferences account = context. getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putInt(JsonConstant.KEY_LOGIN_TYPE, login_type);
		editor.commit();
	}
	
	public byte getLoginType() {
		SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		int login_type =  account.getInt(JsonConstant.KEY_LOGIN_TYPE, 2);
		byte b_login_type = Consts.AF_LOGIN_AFID;
			switch (login_type) {
			case 1:
				b_login_type = Consts.AF_LOGIN_PHONE;
				break;
			case 2:
				b_login_type = Consts.AF_LOGIN_AFID;
				break;
			case 3:
				b_login_type = Consts.AF_LOGIN_IMEI;
				break;
			case 4:
				b_login_type = Consts.AF_LOGIN_EMAIL;
				break;
			case 5:
				b_login_type = Consts.AF_LOGIN_FACEBOOK;
				break;
			case 6:
				b_login_type = Consts.AF_LOGIN_GOOGLE;
				break;
		
			}
			return b_login_type;
	}
*/
	public void setLoginModePhoneNumber(boolean login_mode) {
		// TODO Auto-generated method stub
		SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putBoolean(JsonConstant.KEY_LOGIN_MODE_PHONE_NUMBER, login_mode);
		editor.commit();
	}
	
	public boolean getLoginModePhoneNumber(){
		SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		return account.getBoolean(JsonConstant.KEY_LOGIN_MODE_PHONE_NUMBER, false);
	}


	public void setBCPurview(int bcPurviewType) {
		// TODO Auto-generated method stub
		String afid = CacheManager.getInstance().getMyProfile().afId;
		if (!CommonUtils.isEmpty(afid)) {
			SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
			Editor editor = account.edit();
			editor.putInt(afid + "." + JsonConstant.KEY_BC_PURVIEW_TYPE, bcPurviewType);
			editor.commit();
		}
	}

	/**������ҳ�Ƿ�Ϊ���� 1Ϊ����0Ϊ����ʾ*/
	public void setHomeShowMode(int isGrid) {
		// TODO Auto-generated method stub
		String afid = CacheManager.getInstance().getMyProfile().afId;
		if (!CommonUtils.isEmpty(afid)) {
			SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
			Editor editor = account.edit();
			editor.putInt(afid + "." + JsonConstant.KEY_HOME_SHOW_MODE, isGrid);
			editor.commit();
		}
	}

	/**��ȡ��ҳ�Ƿ�Ϊ���� 1Ϊ����0Ϊ����ʾ*/
	public int getHomeShowMode(){
		String afid = CacheManager.getInstance().getMyProfile().afId;
		if (!CommonUtils.isEmpty(afid)) {
			SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
			return account.getInt(afid + "." + JsonConstant.KEY_HOME_SHOW_MODE, 0);
		}else{
			return 0;
		}

	}


	public void setBackgroundForAfid(String myAfid, String from_afid, String background_name) {
		// TODO Auto-generated method stub
		if(TextUtils.isEmpty(myAfid) || TextUtils.isEmpty(from_afid)){
			return;
		}
		SharedPreferences account = context.getSharedPreferences(myAfid, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putString(from_afid+JsonConstant.KEY_BACKGROUND, background_name);
		editor.commit();
	}
	
	public String getBackgroundForAfid(String myAfid,String from_afid){
		SharedPreferences account = context.getSharedPreferences(myAfid, Context.MODE_PRIVATE);
		return account.getString(from_afid+JsonConstant.KEY_BACKGROUND, "background0");//default
	}

	/**set is first time enter guide next activity*/
	public void setIsFirstTimeEnterGuideNext(boolean isFirstEnterGuideNext) {
		// TODO Auto-generated method stub
		SharedPreferences account =context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putBoolean(JsonConstant.KEY_IS_FIRST_TIME_GUIDE_NEXT, isFirstEnterGuideNext);
		editor.commit();
	}
	
	
	public boolean getIsClosePalmchat() {
		SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		return account.getBoolean(JsonConstant.KEY_IS_CLOSE_PALMCHAT, false);
	}
	
	
	public void setIsClosePalmchat(boolean isClosePalmchat) {
		// TODO Auto-generated method stub
		SharedPreferences account =context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putBoolean(JsonConstant.KEY_IS_CLOSE_PALMCHAT, isClosePalmchat);
		editor.commit();
	}

	/*public boolean getIsChangeCity() {
		SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		return account.getBoolean(JsonConstant.KEY_IS_CHANGE_CITY, false);
	}


	public void setIsIsChangeCity(boolean isChangeCity) {
		// TODO Auto-generated method stub
		SharedPreferences account =context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putBoolean(JsonConstant.KEY_IS_CHANGE_CITY, isChangeCity);
		editor.commit();
	}

	public boolean getIsChangeCityForNearBy() {
		SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		return account.getBoolean(JsonConstant.KEY_IS_CHANGE_CITY_FOR_NEARBY, false);
	}


	public void setIsChangeCityForNearBy(boolean isChangeCity) {
		// TODO Auto-generated method stub
		SharedPreferences account =context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putBoolean(JsonConstant.KEY_IS_CHANGE_CITY_FOR_NEARBY, isChangeCity);
		editor.commit();
	}
	*/
	/**get is first time enter*/
	public boolean getIsFirstTimeEnterGuideNext() {
		SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		return account.getBoolean(JsonConstant.KEY_IS_FIRST_TIME_GUIDE_NEXT, true);
	}
	
	
//	public boolean getIsAddShortCut() {
//		SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
//		return account.getBoolean(JsonConstant.KEY_IS_ADDSHORTCUT, false);
//	}
//	public void setIsAddShortCut(boolean isAdded) {
//		// TODO Auto-generated method stub
// 		SharedPreferences account =context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
//		Editor editor = account.edit();
//		editor.putBoolean(JsonConstant.KEY_IS_ADDSHORTCUT, isAdded);
//		editor.commit();
//	}
	public void setSoftkey_h(int Softkey_h) {
		// TODO Auto-generated method stub
		SharedPreferences account =context.getSharedPreferences(RequestConstant.SOFTKEY_H, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putInt(RequestConstant.SOFTKEY_H, Softkey_h);
		editor.commit();
	}
	
	public int getSoftkey_h() {
		SharedPreferences account = context.getSharedPreferences(RequestConstant.SOFTKEY_H, Context.MODE_PRIVATE);
		return account.getInt(RequestConstant.SOFTKEY_H, -1);
	}
	
	/**
	 * 
	 * @param key
	 * @param isFirst
	 */
	public void setFunctionTips_Key(String key, boolean isFirst) {
		// TODO Auto-generated method stub
		String name = RequestConstant.FUNCTIONTIPS + CacheManager.getInstance().getMyProfile().afId; 
		SharedPreferences account =context.getSharedPreferences(RequestConstant.FUNCTIONTIPS , Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putBoolean(key, isFirst);
		editor.commit();
	}
	
	
	public boolean getFunctionTips_Key(String key) {
		String name = RequestConstant.FUNCTIONTIPS + CacheManager.getInstance().getMyProfile().afId;
		SharedPreferences account = context.getSharedPreferences(RequestConstant.FUNCTIONTIPS, Context.MODE_PRIVATE);
		return account.getBoolean(key, true);
	}

	public void setBradcast_FriendCircleNew(int count) {
		// TODO Auto-generated method stub
		String name = RequestConstant.BROADCAST_FRIEND_CIRCLE_NEW + CacheManager.getInstance().getMyProfile().afId;
		SharedPreferences account =context.getSharedPreferences(name, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putInt(RequestConstant.FUNCTIONTIPS, count);
		editor.commit();
	}


	public int getBradcast_FriendCircleNew() {
		String name = RequestConstant.BROADCAST_FRIEND_CIRCLE_NEW + CacheManager.getInstance().getMyProfile().afId;
		SharedPreferences account = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		return account.getInt(RequestConstant.FUNCTIONTIPS, 0);
	}

	
	
//	public void set_isHead(boolean isFirst) {
//		// TODO Auto-generated method stub
//		String name = RequestConstant.FUNCTIONTIPS + CacheManager.getInstance().getMyProfile().afId;
//		SharedPreferences account =context.getSharedPreferences(RequestConstant.FUNCTIONTIPS , Context.MODE_PRIVATE);
//		Editor editor = account.edit();
//		editor.putBoolean(RequestConstant.IS_SET_HEAD, isFirst);
//		editor.commit();
//	}
//
//
//	public boolean getset_isHead() {
//		String name = RequestConstant.FUNCTIONTIPS + CacheManager.getInstance().getMyProfile().afId;
//		SharedPreferences account = context.getSharedPreferences(RequestConstant.FUNCTIONTIPS, Context.MODE_PRIVATE);
//		return account.getBoolean(RequestConstant.IS_SET_HEAD, false);
//	}

    /**
     * 设置赌球消息状�?
     *
     * @param isUnRead 是否已读 true已读，false未读
     * @return
     */
    public void setUnReadPredictRecord(boolean isUnRead) {
        // TODO Auto-generated method stub
        String name = RequestConstant.FUNCTIONTIPS + CacheManager.getInstance().getMyProfile().afId;
        SharedPreferences account =context.getSharedPreferences(name , Context.MODE_PRIVATE);
        Editor editor = account.edit();
        editor.putBoolean(RequestConstant.IS_READ_PREDICT_NOTIFICATION, isUnRead);
        editor.commit();
    }

    /**
     * 获取赌球消息状�?
     *
     * @param
     * @return true 已读，false未读
     */
    public boolean getUnReadPredictRecord() {
        String name = RequestConstant.FUNCTIONTIPS + CacheManager.getInstance().getMyProfile().afId;
        SharedPreferences account = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return account.getBoolean(RequestConstant.IS_READ_PREDICT_NOTIFICATION, false);
    }

	/**
	 * 设置充值成功消息状�?
	 *
	 * @param isUnRead 是否已读 true已读，false未读
	 * @return
	 */
	public void setUnReadExchange(boolean isUnRead) {
		// TODO Auto-generated method stub
		String name = RequestConstant.FUNCTIONTIPS + CacheManager.getInstance().getMyProfile().afId;
		SharedPreferences account =context.getSharedPreferences(name , Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putBoolean(RequestConstant.IS_READ_PREDICT_EXCHANGE, isUnRead);
		editor.commit();
	}

	/**
	 * 获取充值成功消息状�?
	 *
	 * @param
	 * @return true 已读，false未读
	 */
	public boolean getUnReadExchange() {
		String name = RequestConstant.FUNCTIONTIPS + CacheManager.getInstance().getMyProfile().afId;
		SharedPreferences account = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		return account.getBoolean(RequestConstant.IS_READ_PREDICT_EXCHANGE, false);
	}

	/**
	 * 设置派奖消息状�?
	 *
	 * @param isUnRead 是否已读 true已读，false未读
	 * @return
	 */
	public void setUnReadPredictPrize(boolean isUnRead) {
		// TODO Auto-generated method stub
		String name = RequestConstant.FUNCTIONTIPS + CacheManager.getInstance().getMyProfile().afId;
		SharedPreferences account =context.getSharedPreferences(name , Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putBoolean(RequestConstant.IS_READ_PREDICT_PRIZE, isUnRead);
		editor.commit();
	}

	/**
	 * 获取新palmguess活动状�?
	 *
	 * @param
	 * @return true 已读，false未读
	 */
	public boolean getUnReadNewPalmGuess() {
		String name = RequestConstant.FUNCTIONTIPS + CacheManager.getInstance().getMyProfile().afId;
		SharedPreferences account = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		return account.getBoolean(RequestConstant.IS_NEW_PALMGUESS, false);
	}

	/**
	 * 设置新palmguess活动状�?
	 *
	 * @param isUnRead 是否已读 true已读，false未读
	 * @return
	 */
	public void setUnReadNewPalmGuess(boolean isUnRead) {
		// TODO Auto-generated method stub
		String name = RequestConstant.FUNCTIONTIPS + CacheManager.getInstance().getMyProfile().afId;
		SharedPreferences account =context.getSharedPreferences(name , Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putBoolean(RequestConstant.IS_NEW_PALMGUESS, isUnRead);
		editor.commit();
	}

	/**
	 * 国家是否更新
	 *
	 * @param
	 * @return true 已读，false未读
	 */
	public boolean getChangeOfCountry() {
		String name = RequestConstant.FUNCTIONTIPS + CacheManager.getInstance().getMyProfile().afId;
		SharedPreferences account = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		return account.getBoolean(RequestConstant.IS_CHANGE_COUNTRY, false);
	}

	/**
	 * 设置国家是否更新
	 *
	 * @param isChange 是否已更�?true已更新，false未更�?
	 * @return
	 */
	public void setChangeOfCountry(boolean isChange) {
		// TODO Auto-generated method stub
		String name = RequestConstant.FUNCTIONTIPS + CacheManager.getInstance().getMyProfile().afId;
		SharedPreferences account =context.getSharedPreferences(name , Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putBoolean(RequestConstant.IS_CHANGE_COUNTRY, isChange);
		editor.commit();
	}

	/**
	 * 获取派奖消息状�?
	 *
	 * @param
	 * @return true 已读，false未读
	 */
	public boolean getUnReadPredictPrize() {
		String name = RequestConstant.FUNCTIONTIPS + CacheManager.getInstance().getMyProfile().afId;
		SharedPreferences account = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		return account.getBoolean(RequestConstant.IS_READ_PREDICT_PRIZE, false);
	}
	
	/**
	 * zhh存放包名和版本号
	 */
	public void setVerisonCodeByPackage () {
		SharedPreferences sp =context.getSharedPreferences(RequestConstant.PACKAGE_VERSIONCODE, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putInt(context.getPackageName(), AppUtils.getAppVersionCode(context));
		editor.commit();
	}
	
	/**
	 * zhh获取包名和版本号
	 */
	public int getVerisonCodeByPackage () {
		SharedPreferences sp =context.getSharedPreferences(RequestConstant.PACKAGE_VERSIONCODE, Context.MODE_PRIVATE);
		return sp.getInt(context.getPackageName(),0);
	}
/* delete by wxl 不知道以下这4个函�?是用来做什么的 先注释掉�?如果要放出来请与我沟通一�?
	public void setCityBC_city_is_null_Tips_Key(boolean isShow) {
		// TODO Auto-generated method stub
		String name = RequestConstant.CITY_IS_NULL_TIPS + CacheManager.getInstance().getMyProfile().afId; 
		SharedPreferences account =context.getSharedPreferences(name , Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putBoolean(RequestConstant.CITY_IS_NULL, isShow);
		editor.commit();
	}
	
	
	public boolean getCityBC_city_is_null_Tips_Key() {
		String name = RequestConstant.CITY_IS_NULL_TIPS + CacheManager.getInstance().getMyProfile().afId;
		SharedPreferences account = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		return account.getBoolean(RequestConstant.CITY_IS_NULL, false);
	}
	
	public void setRegion_is_null_Tips_Key(boolean isShow) {
		// TODO Auto-generated method stub
		String name = RequestConstant.REGION_IS_NULL_TIPS + CacheManager.getInstance().getMyProfile().afId; 
		SharedPreferences account =context.getSharedPreferences(name , Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putBoolean(RequestConstant.REGION_IS_NULL, isShow);
		editor.commit();
	}
	
	
	public boolean getRegion_is_null_Tips_Key() {
		String name = RequestConstant.REGION_IS_NULL_TIPS + CacheManager.getInstance().getMyProfile().afId;
		SharedPreferences account = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		return account.getBoolean(RequestConstant.REGION_IS_NULL, false);
	}*/

    /**
    * 根据key和预期的value类型获取value的�?
    *
            * @param key
    * @param clazz
    * @return
            */
    public <T> T getValue(String key, Class<T> clazz) {
        if (context == null) {
            throw new RuntimeException("请先调用带有context，name参数的构造！");
        }
        SharedPreferences sp = this.context.getSharedPreferences(this.name, Context.MODE_PRIVATE);
        return getValue(key, clazz, sp);
    }

    /**
     * 针对复杂类型存储<对象>
     *
     * @param key
     * @param object
     */
    public void setObject(String key, Object object) {
        SharedPreferences sp = this.context.getSharedPreferences(this.name, Context.MODE_PRIVATE);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {

            out = new ObjectOutputStream(baos);
            out.writeObject(object);
            String objectVal = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            Editor editor = sp.edit();
            editor.putString(key, objectVal);
            editor.commit();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 针对复杂类型存储<对象>
     *
     * @param key
     * @param clazz
     */
    public <T> T getObject(String key, Class<T> clazz) {
        SharedPreferences sp = this.context.getSharedPreferences(this.name, Context.MODE_PRIVATE);
        if (sp.contains(key)) {
            String objectVal = sp.getString(key, null);
            byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(bais);
                T t = (T) ois.readObject();
                return t;
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bais != null) {
                        bais.close();
                    }
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 对于外部不可见的过渡方法
     *
     * @param key
     * @param clazz
     * @param sp
     * @return
     */
    @SuppressWarnings("unchecked")
    private <T> T getValue(String key, Class<T> clazz, SharedPreferences sp) {
        T t;
        try {

            t = clazz.newInstance();

            if (t instanceof Integer) {
                return (T) Integer.valueOf(sp.getInt(key, 0));
            } else if (t instanceof String) {
                return (T) sp.getString(key, "");
            } else if (t instanceof Boolean) {
                return (T) Boolean.valueOf(sp.getBoolean(key, false));
            } else if (t instanceof Long) {
                return (T) Long.valueOf(sp.getLong(key, 0L));
            } else if (t instanceof Float) {
                return (T) Float.valueOf(sp.getFloat(key, 0L));
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
            Log.e("system", "类型输入错误或者复杂类型无法解析[" + e.getMessage() + "]");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.e("system", "类型输入错误或者复杂类型无法解析[" + e.getMessage() + "]");
        }
        Log.e("system", "无法找到" + key + "对应的值");
        return null;
    }
    /**
     * zhh 设置登录次数
     * @param isChangeCity
     */
    public void setLoginTimes(String afid,int times) {
		SharedPreferences account =context.getSharedPreferences(afid, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putInt(JsonConstant.KEY_LOGIN_TIMES, times);
		editor.commit();
	}
	
	/**
	 *  zhh 获取登录次数
	 * @return
	 */
	public int getLoginTimes(String afid) {
		SharedPreferences account = context.getSharedPreferences(afid, Context.MODE_PRIVATE);
		return account.getInt(JsonConstant.KEY_LOGIN_TIMES, 0);
	}
	
	
	 /**
     * zhh 设置是否已弹出过state修改提示�?
     * @param
     */
    public void setIsShowUpdateRegion(String afid,boolean bool) {
		SharedPreferences account =context.getSharedPreferences(afid, Context.MODE_PRIVATE);
		Editor editor = account.edit();
		editor.putBoolean(JsonConstant.KEY_CHANGE_REGION, bool);
		editor.commit();
	}
	
	/**
	 *  zhh 获取登录次数
	 * @return
	 */
	public boolean getIsShowUpdateRegion(String afid) {
		SharedPreferences account = context.getSharedPreferences(afid, Context.MODE_PRIVATE);
		return account.getBoolean(JsonConstant.KEY_CHANGE_REGION, false);
	}
	

	 /**explore页面wallet处是否显示红点
	  * 
	  * @param isShowRedDot
	  */
	public void setBalancePalmcoinRedDot(boolean isShowRedDot) {
		String afid = CacheManager.getInstance().getMyProfile().afId;
		if (!CommonUtils.isEmpty(afid)) {
			SharedPreferences account = context. getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
			Editor editor = account.edit();
			editor.putBoolean(afid + "." + RequestConstant.BALANCE_PALMCOIN_RED_DOT, isShowRedDot);
			editor.commit();
		}
	 }

	/**
	 * explore页面wallet处是否显示红点
	 * @return
	 */
	public boolean getBalancePalmcoinRedDot() {
		String afid = CacheManager.getInstance().getMyProfile().afId;
		if (!CommonUtils.isEmpty(afid)) {
			SharedPreferences sp = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
			return sp.getBoolean(afid + "." + RequestConstant.BALANCE_PALMCOIN_RED_DOT, false);
		}
		return false;
	}

	/**
	 * 判断wallet中recharge按钮是否第一次点击
	 * @param isClicked
     */
	public void setRechargeCoinClicked(boolean isClicked) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putBoolean(RequestConstant.RECHARGE_COIN_CLICKED, isClicked);
		editor.commit();
	}

	/**
	 * 判断wallet中recharge按钮是否第一次点击
	 * @return
	 */
	public boolean getRechargeCoinClicked() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getBoolean(RequestConstant.RECHARGE_COIN_CLICKED, false);
	}

	/**
	 * 判断explore中mobile top up按钮是否第一次点击
	 * @param isClicked
	 */
	public void setMobileTopUpClicked(boolean isClicked) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putBoolean(RequestConstant.MOBILE_TOP_UP_CLICKED, isClicked);
		editor.commit();
	}

	/**
	 * 判断explore中mobile top up按钮是否第一次点击
	 * @return
	 */
	public boolean getMobileTopUpClicked() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getBoolean(RequestConstant.MOBILE_TOP_UP_CLICKED, false);
	}




	 /** 快捷方式是否存在
	  * 
	  * @param isShowRedDot
	  */
	public void setShortCutExist(boolean shortCutExist) {
			SharedPreferences account = context. getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
			Editor editor = account.edit();
			editor.putBoolean(RequestConstant.IS_SHORCUT_EXIST, shortCutExist);
			editor.commit();
	 }

	/**
	 * 快捷方式是否存在
	 * 
	 * @return
	 */
	public boolean isShortCutExist() {

		SharedPreferences sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
		return sp.getBoolean(RequestConstant.IS_SHORCUT_EXIST, false);

	}
	
	//************ add by zhh 2016-04-27 for recharge  end   *******************************
	
	/**
	 * 读取保存的justcall账户
	 * @return
	 */
	public String getJustAccount(){
		SharedPreferences account =context.getSharedPreferences(RequestConstant.ACCOUNT, Context.MODE_PRIVATE);
		return account.getString(RequestConstant.JUSTCALL_ACCOUNT, "");
	}
	
	/**
	 * 读取保存的justcall密码
	 * @return
	 */
	public String getJustPassword(){
		SharedPreferences account =context.getSharedPreferences(RequestConstant.ACCOUNT, Context.MODE_PRIVATE);
		return account.getString(RequestConstant.JUSTCALL_PASSWORD, "");
	}
	
	/**
	 * 
	 */
	public void setJustAccountPw(String account,String password) {
		SharedPreferences sharepf = context.getSharedPreferences(RequestConstant.ACCOUNT, Context.MODE_PRIVATE);
		Editor editor = sharepf.edit();
		editor.putString(RequestConstant.JUSTCALL_ACCOUNT, account);
		editor.putString(RequestConstant.JUSTCALL_PASSWORD, password);
		editor.commit();
	}

	/**
	 * 查看是否显示过区域更改提示
	 * @param account
	 * @return
     */
	public boolean getNewAroudEndFlagByAccount() {
		SharedPreferences sharepf = context.getSharedPreferences(RequestConstant.SETTING,Context.MODE_PRIVATE);
		String key = CacheManager.getInstance().getMyProfile().afId+ CacheManager.getInstance().getMyProfile().afId;
		return sharepf.getBoolean(key,false);
	}

	/**
	 * 设置提示
	 */
	public void setNewAroundEndFlagByAccount() {
		SharedPreferences sharepf = context.getSharedPreferences(RequestConstant.SETTING,Context.MODE_PRIVATE);
		String key = CacheManager.getInstance().getMyProfile().afId+ CacheManager.getInstance().getMyProfile().afId;
		Editor editor = sharepf.edit();
		editor.putBoolean(key,true);
		editor.commit();
	}

	/**设置palmcall recent更新时间**/
	public void setUpdatePalmCallProfileTime() {
		SharedPreferences setting =context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		Editor editor = setting.edit();
		editor.putLong(CacheManager.getInstance().getMyProfile().afId + RequestConstant.PALMCALL_UPDATE_RECENT_PROFILE, System.currentTimeMillis());
		editor.commit();
	}

	/**获取palmcall recent更新时间**/
	public long getSetUpdatePalmCallProfileTime() {
		SharedPreferences account = context.getSharedPreferences(RequestConstant.SETTING, Context.MODE_PRIVATE);
		return account.getLong(CacheManager.getInstance().getMyProfile().afId + RequestConstant.PALMCALL_UPDATE_RECENT_PROFILE, 0);
	}

	/**
	 * 设置当前登录的palmcallid
	 * @param curPalmcallId
     */
	public synchronized void  setCurPalmcallId() {
		SharedPreferences sharePf = context.getSharedPreferences(RequestConstant.SETTING,Context.MODE_PRIVATE);
		String key = CacheManager.getInstance().getMyProfile().justalkId;
		Editor editor = sharePf.edit();
		editor.putString(RequestConstant.PALMCALL_ID, CacheManager.getInstance().getMyProfile().justalkId);
		editor.commit();
	}

	/**
	 * 返回上次登录的palmcall ID
	 * @return
     */
	public String getCurPalmcallId(){
		SharedPreferences sharePreferences = context.getSharedPreferences(RequestConstant.SETTING,Context.MODE_PRIVATE);
		return  sharePreferences.getString(RequestConstant.PALMCALL_ID,"");
	}

	/**
	 * 是否有未接电话
	 * @param missedCalls
     */
	public void setMissedCalls(boolean missedCalls){
		SharedPreferences sharePf = context.getSharedPreferences(RequestConstant.SETTING,Context.MODE_PRIVATE);
		Editor editor = sharePf.edit();
		editor.putBoolean(CacheManager.getInstance().getMyProfile().afId + RequestConstant.MISSED_CALLS,missedCalls);
		editor.commit();
	}

	/**
	 * 是否有未接电话
	 * @return
     */
	public boolean getMissedCalls(){
		SharedPreferences sp = context.getSharedPreferences(RequestConstant.SETTING,Context.MODE_PRIVATE);
		return sp.getBoolean(CacheManager.getInstance().getMyProfile().afId + RequestConstant.MISSED_CALLS,false);
	}

	/** PalmCall是否已经新手导航
	 * @param isGuide
	 */
	public void setPalmCallIsGuide(boolean isGuide){
		SharedPreferences sharePf = context.getSharedPreferences(RequestConstant.SETTING,Context.MODE_PRIVATE);
		Editor editor = sharePf.edit();
		editor.putBoolean(RequestConstant.PALMCALL_GUIDE,isGuide);
		editor.commit();
	}

	/** 获取PalmCall是否已经新手导航
	 * @return
	 */
	public boolean getPalmCallIsGuide(){
		SharedPreferences sp = context.getSharedPreferences(RequestConstant.SETTING,Context.MODE_PRIVATE);
		return sp.getBoolean(RequestConstant.PALMCALL_GUIDE,false);
	}


	//************ add by mqy 2016-07-14 for palmcall  *******************************
    //设置palmCall 应答开关

	public void setPalmCallAnsweringSwitch(String afid,boolean flag) {
		SharedPreferences setting =context.getSharedPreferences(afid, Context.MODE_PRIVATE);
		Editor editor = setting.edit();
		editor.putBoolean(JsonConstant.PALMCALL_ANSWERING_SWTICH, flag);
		editor.commit();
	}

	/**
	 *  获取palmcall 应答开关
	 * @return
	 */
	public boolean getPalmCallAnsweringSwitch(String afid) {
		SharedPreferences account = context.getSharedPreferences(afid, Context.MODE_PRIVATE);
		return account.getBoolean(JsonConstant.PALMCALL_ANSWERING_SWTICH, false);
	}

	/**
	 *  获取palmcall 开始时间
	 * @return
	 */
//	public void setPalmCallStartTime(String afid,int repeatday) {
//		SharedPreferences setting =context.getSharedPreferences(afid, Context.MODE_PRIVATE);
//		Editor editor = setting.edit();
//		editor.putInt(JsonConstant.PALMCALL_START_TIME, repeatday);
//		editor.commit();
//	}
//	public int getPalmCallStartTime(String afid)
//	{
//		SharedPreferences account = context.getSharedPreferences(afid, Context.MODE_PRIVATE);
//		return account.getInt(JsonConstant.PALMCALL_START_TIME, -1);
//	}
	/**
	 *  获取palmcall 开始时间
	 * @return
	 */
//	public void setPalmCallEndTime(String afid,int repeatday) {
//		SharedPreferences setting =context.getSharedPreferences(afid, Context.MODE_PRIVATE);
//		Editor editor = setting.edit();
//		editor.putInt(JsonConstant.PALMCALL_END_TIME, repeatday);
//		editor.commit();
//	}
//	public int getPalmCallEndTime(String afid)
//	{
//		SharedPreferences account = context.getSharedPreferences(afid, Context.MODE_PRIVATE);
//		return account.getInt(JsonConstant.PALMCALL_END_TIME, -1);
//	}

	public void setPalmCallRewardTime(boolean isReward){
		SharedPreferences sharePf = context.getSharedPreferences(RequestConstant.SETTING,Context.MODE_PRIVATE);
		Editor editor = sharePf.edit();
		editor.putBoolean(CacheManager.getInstance().getMyProfile().afId + RequestConstant.PALMCALL_REWARD,isReward);
		editor.commit();
	}

	public boolean getPalmcallRewardTime(){
		SharedPreferences sp = context.getSharedPreferences(RequestConstant.SETTING,Context.MODE_PRIVATE);
		return sp.getBoolean(CacheManager.getInstance().getMyProfile().afId + RequestConstant.PALMCALL_REWARD,false);
	}

	public void setPalmCallTime(int time){
		SharedPreferences sharePf = context.getSharedPreferences(RequestConstant.SETTING,Context.MODE_PRIVATE);
		Editor editor = sharePf.edit();
		editor.putInt(CacheManager.getInstance().getMyProfile().afId + RequestConstant.PALMCALL_TIME,time);
		editor.commit();
	}

	public int getPalmcallTime(){
		SharedPreferences sp = context.getSharedPreferences(RequestConstant.SETTING,Context.MODE_PRIVATE);
		return sp.getInt(CacheManager.getInstance().getMyProfile().afId + RequestConstant.PALMCALL_TIME,0);
	}

}
