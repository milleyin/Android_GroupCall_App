package com.afmobi.palmchat.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Process;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.FacebookConstant;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.guide.NewGuideActivity;
import com.afmobi.palmchat.ui.activity.guide.NewGuideNextActivity;
import com.afmobi.palmchat.ui.activity.login.LoginActivity;
import com.afmobi.palmchat.ui.activity.publicaccounts.InnerNoAbBrowserActivity;
import com.afmobi.palmchat.ui.activity.register.CompleteProfileActivity;
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.KeyDownUtil;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.cache.CacheManager;

import java.util.ArrayList;

import bolts.AppLinks;

/**
 * 启动页 根据账号信息决定 进引导页、注册登陆选择页、登录、主页
 */
public class LaunchActivity extends BaseActivity {
	private AfPalmchat mAfCorePalmchat;
	private ArrayList<Uri> mImageUris =null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void findViews() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		// init screen resolution
		AppUtils.initScreen(this);
		mAfCorePalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;
		// 初始化屏幕分辨率
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		mAfCorePalmchat.AfSetScreen(dm.widthPixels, dm.heightPixels);
		PalmchatLogUtils.println("LaunchActivity  dm.widthPixels  " + dm.widthPixels + "  dm.heightPixels  " + dm.heightPixels);
		PalmchatLogUtils.println("LaunchActivity  init");

		Uri facebookFacebookFeedback = AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
		String facebookFeedbackUri=null;
		if(facebookFacebookFeedback!=null){
			facebookFeedbackUri = facebookFacebookFeedback.toString();
		}

		if(((facebookFeedbackUri!=null)&&(facebookFeedbackUri.contains(FacebookConstant.FACEBOOKFEEDBACE_PHOTOTEXT_FLAG)))){
			facebookFeedbackUri = null;
		}
		if(!TextUtils.isEmpty(facebookFeedbackUri)){
			Intent intent = new Intent();
			intent.setClass(this, InnerNoAbBrowserActivity.class);
			intent.putExtra(IntentConstant.RESOURCEURL, facebookFeedbackUri);
			intent.putExtra(IntentConstant.ISNOTNEEDSKIP,true);
			startActivity(intent);
			finish();
			return;
		}
		// 添加桌面快捷方式
		//!LauncherUtil.isShortCutExist(context, getString(R.string.app_name)) && 
		if (!SharePreferenceUtils.getInstance(this).isShortCutExist()) {
			if (!CommonUtils.isAndroidGp()) {
				CommonUtils.addShortcut(this);
				SharePreferenceUtils.getInstance(this).setShortCutExist(true);
			}
		}
		
		Intent intent = getIntent();
		try{
			mImageUris= (ArrayList<Uri>)intent.getSerializableExtra(IntentConstant.SHAREIMAGE_URLS);
		} catch(Exception e) {
			e.printStackTrace();
		}
		// 跳转页面选择
		selectActivity();

	}


	/**
	 * 跳转页面选择
	 */
	private void selectActivity() {
		String password = CacheManager.getInstance().getMyProfile().password;
		String afid = CacheManager.getInstance().getMyProfile().afId;
		boolean isFirst = SharePreferenceUtils.getInstance(context).getIsFirstTimeEnter();
		boolean isNotCompleteImei = SharePreferenceUtils.getInstance(this).getNotCompleteImei();
		if (isFirst || isNotCompleteImei) { // 第一次进入应用或者手机串号IMEI未获取则进入引导页面
			mAfCorePalmchat.AfSetLoginInfo(null);
			// 进入引导页面
			toGuideActivity();
			return;
		}
		boolean isFirstEnterGuideNext = SharePreferenceUtils.getInstance(context).getIsFirstTimeEnterGuideNext();
		if (isFirstEnterGuideNext) {// 第一次进入Welcome页面
			mAfCorePalmchat.AfSetLoginInfo(null);
			// 进入Welcome页面
			toGuideNextActivity();
			return;
		}

		AfProfileInfo profile = CacheManager.getInstance().getMyProfile();


		String msg = "afid =" + afid + ",pwd=" + password + ",name=" + profile.name + ",birth=" + profile.birth + ",country=" + profile.country + ",gender=" + profile.sex;
		PalmchatLogUtils.e("---LaunchActivity---", msg);		
        if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(afid)) {//已经登录过
            AfProfileInfo myProfile = CacheManager.getInstance().getMyProfile();
            if ( CommonUtils.isEmpty(myProfile.name)
                    || CommonUtils.isEmpty(myProfile.birth)
                    || CommonUtils.isEmpty(myProfile.country)
                    || DefaultValueConstant.OVERSEA.equals(myProfile.country)) {  //profile中名字为空/生日为空/国家为空，或者国家为Oversea时，进入完善资料页面
                //进入完善资料页面
                toCompleteProfile(myProfile);
            } else {
                //进入首页
                toMainTab();
            }
        } else { //尚未登录时，进入登录页面
            mAfCorePalmchat.AfSetLoginInfo(null);
            //进入登录页面
            toLogin();
        }

	}

	/**
	 * 进入登录页面
	 */
	private void toLogin() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 进入首页
	 */
	private void toMainTab() {
		Intent intent = new Intent(LaunchActivity.this, MainTab.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 关闭MainTab外的所有页面
		intent.putExtra(IntentConstant.SHAREIMAGE_URLS, mImageUris);
		startActivity(intent);
		finish();
	}

	/**
	 * 进入完善资料页面
	 * 
	 * @param myProfile
	 */
	private void toCompleteProfile(AfProfileInfo myProfile) {
		Intent intent = new Intent(this, CompleteProfileActivity.class);
		intent.putExtra(JsonConstant.KEY_PROFILE, myProfile);
		startActivity(intent);
	}

	/**
	 * 进入引导页面
	 */
	private void toGuideActivity() {
		Intent intent = new Intent(this, NewGuideActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 进入Welcome页面
	 */
	private void toGuideNextActivity() {
		Intent intent = new Intent(this, NewGuideNextActivity.class);
		startActivity(intent);
		finish();
	}
	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {// 当按Back键时退出当前页面并取消通知和广播
			finish();
			/*
			 * Intent intent = new Intent(Intent.ACTION_MAIN);
			 * intent.addCategory(Intent.CATEGORY_HOME);
			 * intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 * startActivity(intent);
			 */
			KeyDownUtil.actionMain(LaunchActivity.this, Intent.FLAG_ACTIVITY_CLEAR_TOP);
			CommonUtils.cancelNoticefacation(getApplicationContext());
			CommonUtils.cancelNetUnavailableNoticefacation();
			android.os.Process.killProcess(Process.myPid());
		}
		return false;
	}
}
