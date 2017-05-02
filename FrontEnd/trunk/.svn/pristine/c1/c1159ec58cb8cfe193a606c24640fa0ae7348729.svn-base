package com.afmobi.palmchat.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.db.DBState;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.services.AppStatusService;
import com.afmobi.palmchat.ui.activity.palmcall.PalmcallManager;
import com.afmobi.palmchat.ui.activity.payment.MyWapActivity;
import com.afmobi.palmchat.ui.activity.setting.SettingsActivity;
import com.afmobigroup.gphone.R;
import com.core.AfLoginInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;

public class AppUtils {
	
	public static int WIDTH = 480;
	public static int HEIGHT = 800;
	private static DisplayMetrics displayMetrics;
	/*profile�й�� ��phone_cc��Ӧ��ҡ��������� ����״̬��ʶ*/
	public static final int PHONECC_NIGERIA = 1; //phonecc���Ϊ��������
	public static final int PHONECC_NOT_NIGERIA =2 ;  //phonecc��Ҳ�����������
	public static final int PHONECC_IS_NULL =3 ;  //phoneccΪ��
//	private static float density;
	
	public static int getWidth(Context context)
	{
	//	return WIDTH;
		if (displayMetrics == null) {
			displayMetrics = context.getResources().getDisplayMetrics();
		}
		return displayMetrics.widthPixels;
	}
	
	public static int getHeight(Context context)
	{
//		return HEIGHT;
		if (displayMetrics == null) {
			displayMetrics = context.getResources().getDisplayMetrics();
		}
		return displayMetrics.heightPixels;
	}
	
	public static void initScreen(Activity activity)
	{
		displayMetrics = new DisplayMetrics();   
		activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//		density = displayMetrics.density;
		WIDTH = displayMetrics.widthPixels;
		HEIGHT = displayMetrics.heightPixels;
	}
	
	public static int getWidthFromPercent(Context context,float percent) {
		if (displayMetrics == null) {
			displayMetrics = context.getResources().getDisplayMetrics();
		}
		return (int) (percent > 1 ? displayMetrics.widthPixels : displayMetrics.widthPixels * percent);
	}
	
	public static int getHeightFromPercent(Context context,float percent) {
		if (displayMetrics == null) {
			displayMetrics = context.getResources().getDisplayMetrics();
		}
		return (int) (percent > 1 ? displayMetrics.heightPixels : displayMetrics.heightPixels * percent);
	}
	
	public static float getWidthDp (Context context) {
		if (displayMetrics == null) {
			displayMetrics = context.getResources().getDisplayMetrics();
		}
		return displayMetrics.widthPixels / displayMetrics.density;
	}
	
	public static float getHeightDp (Context context) {
		if (displayMetrics == null) {
			displayMetrics = context.getResources().getDisplayMetrics();
		}
		return displayMetrics.heightPixels / displayMetrics.density;
	}
	
	public static int dpToPx(Context context,int dp) {
		if (displayMetrics == null) {			displayMetrics = context.getResources().getDisplayMetrics();
		}
		return (int)((dp * displayMetrics.density) + 0.5);
	}
	
	public static int pxToDp(Context context,int px) {
//	    return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		if (displayMetrics == null) {
			displayMetrics = context.getResources().getDisplayMetrics();
		}
		return (int) ((px/displayMetrics.density)+0.5);
	}
	
	public static String getAppVersionName(Context context) {     
	    String versionName = "";     
	    try {     
	        // ---get the package info---     
	        PackageManager pm = context.getPackageManager();     
	        PackageInfo pi = pm.getPackageInfo(context.getPackageName() , 0);     
	        versionName = pi.versionName;     
	        if (versionName == null || versionName.length() <= 0) {     
	            return "";     
	        }     
	    } catch (Exception e) {     
	        Log.e("VersionInfo", "Exception", e);     
	    }     
	    return versionName;     
	}    

	public static int getAppVersionCode(Context context) {
		int verCode = 0;
		try {     
	        // ---get the package info---     
	        PackageManager pm = context.getPackageManager();     
	        PackageInfo pi = pm.getPackageInfo(context.getPackageName() , 0);     
	        verCode = pi.versionCode;     
	    } catch (Exception e) {     
	        Log.e("VersionInfo", "Exception", e);     
	    }     
		return verCode;
	}
	
	/**add by zhh 
	 * �ж�profile��country �� phonecc��Ӧcountry�Ƿ�Ϊ��������
	 * @return
	 */
	public static int countryState(Context context) {
 			AfProfileInfo myProfile = CacheManager.getInstance().getMyProfile();
//			String country = myProfile.country;
			String phone_cc = myProfile.phone_cc;
			if(phone_cc != null ) {
				String phonecc_country = DBState.getInstance(context).getCountryFromCode(phone_cc);
				if(DefaultValueConstant.COUNTRY_NIGERIA.equals(phonecc_country)) {
					return PHONECC_NIGERIA;
				}else {
					return PHONECC_NOT_NIGERIA;
				}
			}else {
				return PHONECC_IS_NULL;
			}
		
	}

	private static Handler mHandler = new Handler();

	/**
	 * zhh 注销重登
	 * 
	 * @param context
	 */
	public static void doLogout(Context context,AfHttpResultListener afHttpResultListener) {
		Activity mActivity = (Activity) context;
		PalmcallManager.getInstance().logoutPalmcall();
		final AfPalmchat mAfCorePalmchat = ((PalmchatApp) mActivity.getApplication()).mAfCorePalmchat;
		PalmchatApp mPalmchatApp = (PalmchatApp) mActivity.getApplication();

		new ReadyConfigXML().saveReadyInt(ReadyConfigXML.LGO_LG); 
		CommonUtils.cancelNoticefacation(mActivity.getApplicationContext());
		CommonUtils.cancelNetUnavailableNoticefacation();
		showProgressDialog(R.string.please_wait,mActivity);
		mAfCorePalmchat.AfPollSetStatus(Consts.POLLING_STATUS_PAUSE);
		mAfCorePalmchat.AfHttpRemoveAllListener();
//		PalmchatApp.setIsLoadAccount(false);
//		PalmchatApp.getApplication().savePalmAccount(null,false);
		mPalmchatApp.setInit(false);
		mPalmchatApp.setContext(null);
		mPalmchatApp.setFacebookShareClose(true);
		mAfCorePalmchat.AfHttpLogout(0, afHttpResultListener);
		CacheManager.getInstance().clearCache();
		mAfCorePalmchat.AfDbLoginSetStatus(CacheManager.getInstance().getMyProfile().afId, AfLoginInfo.AFMOBI_LOGIN_INFO_UNNORMAL_EXIT);
		AppStatusService.stop(mActivity);
		CacheManager.getInstance().setAfChatroomDetails(null);
		CacheManager.getInstance().setChatroomListTime(0);
		SharePreferenceUtils.getInstance(mActivity).setIsFacebookLogin(false);
		FolllowerUtil.getInstance().clear();
		JsonConstant.getMyProfileFromServer_forFollowCount_byPalmID = null;
		mHandler.postDelayed(new Runnable() {//该为3秒后就logout 不再等待
			public void run() {
				mAfCorePalmchat.AfDbLoginSetStatus(CacheManager.getInstance().getMyProfile().afId, AfLoginInfo.AFMOBI_LOGIN_INFO_NORMAL);
				mAfCorePalmchat.AfCoreSwitchAccount();
			}
		}, 3*1000);	
	}

	/**
	 * 显示进度对话框
	 * 
	 * @param resId
	 *            需要显示的资源id
	 */
	private static void showProgressDialog(int resId,Activity context) {

		Bundle data = new Bundle();
		data.putInt(SettingsActivity.DIALOG_RES, resId);
		context.showDialog(SettingsActivity.DIALOG_PROGRESS, data);

	}
	public static AfProfileInfo createProfileInfoFromCacheValue() {
		AfProfileInfo afProfileInfo = new AfProfileInfo();
		AfProfileInfo cacheInfo = CacheManager.getInstance().getMyProfile();
		afProfileInfo.afId = cacheInfo.afId;
		afProfileInfo.name = cacheInfo.name;
		afProfileInfo.birth = cacheInfo.birth;
		afProfileInfo.sex = cacheInfo.sex;
		afProfileInfo.region = cacheInfo.region;
		afProfileInfo.hobby = cacheInfo.hobby;
		afProfileInfo.religion = cacheInfo.religion;
		afProfileInfo.profession = cacheInfo.profession;
		afProfileInfo.country = cacheInfo.country;
		afProfileInfo.city = cacheInfo.city;
		afProfileInfo.local_img_path = cacheInfo.local_img_path;
		afProfileInfo.school = cacheInfo.school;
		afProfileInfo.email = cacheInfo.email;
		afProfileInfo.phone = cacheInfo.phone;
		afProfileInfo.secureAnswer = cacheInfo.secureAnswer;
		afProfileInfo.imsi = cacheInfo.imsi;
		afProfileInfo.user_msisdn = cacheInfo.user_msisdn;
		afProfileInfo.fdsn = cacheInfo.fdsn;
		afProfileInfo.blsn = cacheInfo.blsn;
		afProfileInfo.pbsn = cacheInfo.pbsn;
		afProfileInfo.wall_paper = cacheInfo.wall_paper;
		afProfileInfo.gpsn = cacheInfo.gpsn;
		afProfileInfo.gmax = cacheInfo.gmax;
		afProfileInfo.gmaxman = cacheInfo.gmaxman;
		afProfileInfo.level = cacheInfo.level;
		afProfileInfo.credit = cacheInfo.credit;
		afProfileInfo.coin = cacheInfo.coin;
		afProfileInfo.age = cacheInfo.age;
		afProfileInfo._id = cacheInfo._id;
		afProfileInfo.type = cacheInfo.type;
		afProfileInfo.status = cacheInfo.status;
		afProfileInfo.signature = cacheInfo.signature;
		afProfileInfo.alias = cacheInfo.alias;
		afProfileInfo.head_img_path = cacheInfo.head_img_path;
		afProfileInfo.sid = cacheInfo.sid;
		afProfileInfo.serials = cacheInfo.serials;
		afProfileInfo.is_bind_email = cacheInfo.is_bind_email;
		afProfileInfo.is_bind_phone = cacheInfo.is_bind_phone;
		afProfileInfo.password = cacheInfo.password;
		afProfileInfo.dating.charm_level = cacheInfo.dating.charm_level;
		afProfileInfo.dating.marital_status = cacheInfo.dating.marital_status;
		afProfileInfo.dating.dating_phone = "";// cacheInfo.dating.dating_phone;
		afProfileInfo.dating.is_show_dating_phone = cacheInfo.dating.is_show_dating_phone;
		afProfileInfo.dating.is_show_star = cacheInfo.dating.is_show_star;
		afProfileInfo.dating.wealth_flower = cacheInfo.dating.wealth_flower;
		afProfileInfo.dating.gift_flower = cacheInfo.dating.gift_flower;
		afProfileInfo.dating.dating_phone_flower = cacheInfo.dating.dating_phone_flower;
		afProfileInfo.phone_cc = cacheInfo.phone_cc;
		afProfileInfo.palmguess_flag = cacheInfo.palmguess_flag;
		afProfileInfo.palmguess_version = cacheInfo.palmguess_version;
		afProfileInfo.airtime = cacheInfo.airtime;
		afProfileInfo.fanclub_url = cacheInfo.fanclub_url;
		afProfileInfo.palmcoin_menu_list = cacheInfo.palmcoin_menu_list;
		afProfileInfo.palmcoin_walletlayout_list =  cacheInfo.palmcoin_walletlayout_list;
		afProfileInfo.palmcoin_recharge = cacheInfo.palmcoin_recharge;
		afProfileInfo.recharge_intro_url =  cacheInfo.recharge_intro_url;
		afProfileInfo.error_url =  cacheInfo.error_url;
		afProfileInfo.order_list_url =  cacheInfo.order_list_url;
		afProfileInfo.order_detail_url =  cacheInfo.order_detail_url;
		afProfileInfo.password_url =  cacheInfo.password_url;
		afProfileInfo.betway =  cacheInfo.betway;
		return afProfileInfo;
	}
	
	/**
	 * zhh 根据模块获取url
	 * 
	 * @param module
	 *            功能模块
	 * @return
	 */
	public static String getWapUrlByModule(String module) {
		String url = "";
		if (module != null) {
			/** 根据当前环境库获取域名 */
			url = PalmchatApp.getApplication().getDomainBySo(module);
			if (url != null) {
				if (module.equals(MyWapActivity.MODULE_PALMCOIN_TERMS) || module.equals(MyWapActivity.MODULE_AIRTIME_TERMS)) {// palmcoin免责申明/airtime免责申明
					url = url + getCCFromProfile_forRecharge(CacheManager.getInstance().getMyProfile());
					url = url + ".palm-chat.com/statement/show/";
				} else if (module.equals(MyWapActivity.MODULE_GENERIC_TERMS)) { // 全局免责声明
					url = url + ".palm-chat.com/statement/show/";
				} else if (module.equals(MyWapActivity.MODULE_FEEDBACK)) {
					url = url + "/feedback/";
				}
			}
		}

		return url;
	}

	/**
	 * 判断3秒钟内不能再次点击
	 * @return
	 */
	private static long lastClickTime;
	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if ( 0 < timeD && timeD < 2000) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
	/**
	 * 给充值用的  如果Profile绑定了手机号 就取phone_cc如果没绑定 就取Profile 对应的国码
	 * @param profile
	 * @return
	 */
	public static String getCCFromProfile_forRecharge( AfProfileInfo profile){
		if(profile!=null){
			if(!TextUtils.isEmpty( profile.phone_cc)){
				return profile.phone_cc;
			}else  if(!TextUtils.isEmpty(profile.country)){
				return DBState.getInstance(PalmchatApp.getApplication()).getCcFromCountry(profile.country);
			}
		}
		return "";
	}
}
