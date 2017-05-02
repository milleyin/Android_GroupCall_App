package com.afmobi.palmchat;

import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.palmcall.PalmcallManager;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.core.AfLoginInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.tencent.bugly.crashreport.CrashReport;

import android.text.TextUtils;
/**
 * 所有用到前台登陆的都继承该Activity  为了setLoadAccount这个方法，前台登陆完成后 就写入数据库 这样不用在每个有前台登陆的地方写
 * @author wangxl
 *
 */
public class BaseForgroundLoginActivity extends BaseActivity {

	@Override
	public void findViews() {
		// TODO Auto-generated method stub
		 
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}
	
	/**
	 * 如果是前台登陆的 把登陆信息和登陆方式通知中间件保存到数据库
	 * @param myProfile
	 * @param  login_type
	 */
	protected boolean setLoadAccount(AfProfileInfo myProfile ,int  login_type,AfPalmchat mAfCorePalmchat) { 
		byte b_login_type=(byte)login_type;
			if ((TextUtils.isEmpty( myProfile.afId)||TextUtils.isEmpty(myProfile.password ))   && b_login_type != Consts.AF_LOGIN_FACEBOOK && b_login_type != Consts.AF_LOGIN_IMEI //&& myProfile.login_type <= 0
					) {
				return false;
			} else { 
				
				PalmchatLogUtils.println("--fffdddss getFriendList  login_type  " + b_login_type + "  myProfile.third_account " + myProfile.third_account);
				AfLoginInfo login =CacheManager.getInstance().getLoginInfo() ; 

				if (b_login_type == Consts.AF_LOGIN_FACEBOOK || b_login_type == Consts.AF_LOGIN_IMEI //|| myProfile.login_type > 0
						) {
					myProfile.password = Constants.FACEBOOK_PASSWORD; 
					if (TextUtils.isEmpty(myProfile.third_account)) {
						AfLoginInfo tmp = mAfCorePalmchat.AfDbLoginGet(Consts.AF_LOGIN_AFID, myProfile.afId);
						if (tmp != null) {
							myProfile.third_account = tmp.third_account;
						}
					} 
					if (b_login_type == Consts.AF_LOGIN_IMEI) {
						if (TextUtils.isEmpty(myProfile.third_account)) {
							myProfile.third_account = PalmchatApp.getOsInfo().getImei();
						}
					} 
					PalmchatLogUtils.println("getFriendList myProfile.third_account " + myProfile.third_account);
					if (TextUtils.isEmpty(myProfile.third_account)) {
						 
						return false;
					}
					login.third_account = myProfile.third_account;
				}
				login.afid = myProfile.afId;
				login.password = myProfile.password;
				login.cc = PalmchatApp.getOsInfo().getCountryCode();
				login.email = myProfile.email;
				login.fdsn = myProfile.fdsn;
				login.blsn = myProfile.blsn;
				login.gpsn = myProfile.gpsn;
				login.phone = myProfile.phone;
				login.pbsn = myProfile.pbsn;
				login.mcc = PalmchatApp.getOsInfo().getAfid_mcc();
				login.type = CommonUtils.getLoginType(b_login_type);
				mAfCorePalmchat.AfLoadAccount(login);
//				PalmchatApp.setIsLoadAccount(true);
				app.getSettingMode().getSettingValue();
				if (TextUtils.isEmpty(myProfile.third_account)) {
					SharePreferenceUtils.getInstance(PalmchatApp.getApplication()).setThirdPartLogin(myProfile.afId, false);
				} else {
					SharePreferenceUtils.getInstance(PalmchatApp.getApplication()).setThirdPartLogin(myProfile.afId, true);
				}
				CrashReport.setUserId(myProfile.afId);
			}
		return true;

	}

	/**
	 * 判断是否退出了，没有退出的话先退出palmcall
	 */
	protected void judgePalmcallLogout(){
		if(PalmcallManager.getInstance().isPalmcallLogined()){
			PalmcallManager.getInstance().logoutPalmcall();
		}
	}

}
