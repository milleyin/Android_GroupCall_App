package com.afmobi.palmchat.ui.activity.guide;

import java.util.Arrays;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.BaseForgroundLoginActivity;
import com.afmobi.palmchat.OSInfo;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.FacebookConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.MainTab;
import com.afmobi.palmchat.ui.activity.login.FacebookCompleteProfileActivity;
import com.afmobi.palmchat.ui.activity.login.LoginActivity;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.register.RegistrationActivity;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobigroup.gphone.R; 
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
import com.core.param.AfRegInfoParam;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.Profile.FetchProfileCallback;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
//import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class NewGuideNextActivity extends BaseForgroundLoginActivity  implements OnClickListener, AfHttpResultListener {

	private final String TAG = NewGuideNextActivity.class.getSimpleName();
 	public AfPalmchat mAfCorePalmchat; 
	private String facebookId;
	private TextView mSignUp, mLogin;
	/**facebook个人信息*/
	private Profile mProfile; 

    /**回调facebooksdk*/
    private CallbackManager mCallbackManager;
    /**获取FB个人资料回调*/
    private FetchProfileCallback mFetchProfileCallback = new FetchProfileCallback(){

		@Override
		public void onSuccess() {
			// TODO Auto-generated method stub
			PalmchatLogUtils.i(TAG, "--FetchProfileCallback---onSuccess----");
			mHandler.sendEmptyMessage(DefaultValueConstant.FETCHPROFILE_SUCCESS);
		}

		@Override
		public void onError(FacebookException error) {
			// TODO Auto-generated method stub
			PalmchatLogUtils.i(TAG, "--FetchProfileCallback---onError----"+error.toString());
			mHandler.sendEmptyMessage(DefaultValueConstant.FETCHPROFILE_FAILURE);
		}
    	
    };
    /**fackbook回调*/
    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
        	PalmchatLogUtils.i(TAG, "--FacebookCallback---onSuccess----"+loginResult.toString());
        	mHandler.sendEmptyMessage(DefaultValueConstant.LOGINFACEBOOK_SUCCESS);
        }

        @Override
        public void onCancel() {
        	PalmchatLogUtils.i(TAG, "--FacebookCallback---onCancel----");
			mHandler.sendEmptyMessage(DefaultValueConstant.LOGINFACEBOOK_CANCEL);
        }

        @Override
        public void onError(FacebookException exception) {
        	PalmchatLogUtils.i(TAG, "--FacebookCallback---onError----"+exception.toString());
        	mHandler.sendEmptyMessage(DefaultValueConstant.LOGINFACEBOOK_FAILURE);
        }
    };
    
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message  msg) {
			switch (msg.what) {
				//facebook登录成功
				case DefaultValueConstant.LOGINFACEBOOK_SUCCESS: {
					ToastManager.getInstance().show(NewGuideNextActivity.this,R.string.success);
					setProfile();
					break;
				}
				//facebook登录失败
				case DefaultValueConstant.LOGINFACEBOOK_FAILURE: {
					dismissAllDialog();
					ToastManager.getInstance().show(NewGuideNextActivity.this,R.string.failure);
					break;
				}
				
				case DefaultValueConstant.LOGINFACEBOOK_CANCEL: {
					ToastManager.getInstance().show(NewGuideNextActivity.this, R.string.facebook_login_cancel_tip);
					break;
				}
				//获取个人资料成功
				case DefaultValueConstant.FETCHPROFILE_SUCCESS:{
					setProfile();
					break;
				}
				//获取个人资料失败
				case DefaultValueConstant.FETCHPROFILE_FAILURE:{
					dismissAllDialog();
					ToastManager.getInstance().show(NewGuideNextActivity.this,R.string.fetchprofile_failure);
					break;
				}
				default:
					break;
				
			}
		}
		
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);

		mAfCorePalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;

		mCallbackManager = CallbackManager.Factory.create();
	    LoginManager.getInstance().registerCallback(mCallbackManager,mCallback);
	  }

	@Override
	public void findViews() {

	}

	@Override
	public void init() {
		setContentView(R.layout.activity_new_guide_next_activity);
		mSignUp = (TextView) findViewById(R.id.signup);
		mLogin = (TextView) findViewById(R.id.login);
		mSignUp.setOnClickListener(this);
		mLogin.setOnClickListener(this);
		findViewById(R.id.r_login_facebook).setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// heguiming 2013-12-04
		new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.ENTRY_GUIDE);
	}

	@Override
	protected void onPause() {
		super.onPause();
		AppEventsLogger.deactivateApp(this);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.signup: // 注册
			SharePreferenceUtils.getInstance(this).setIsFirstTimeEnterGuideNext(false);
			Intent intent = new Intent(this, RegistrationActivity.class);
			intent.putExtra(JsonConstant.KEY_ACTION, RegistrationActivity.REG);
			intent.putExtra(JsonConstant.KEY_FROM, NewGuideActivity.class.getName());
			startActivity(intent);
			new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.ENTRY_REG_I);
			new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.REG_PNUM);
			break;

		case R.id.login: // 登陆
			Log.i("NewGuideNextActivity", "login");
			SharePreferenceUtils.getInstance(this).setIsFirstTimeEnterGuideNext(false);
			startActivity(new Intent(this, LoginActivity.class));
			break;

		case R.id.r_login_facebook: // 使用facebook登陆

			if (hasThirdLoginClient(Consts.AF_LOGIN_FACEBOOK)) {
				new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.ENTRY_LG_FB);
//				MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_LG_FB);
			} else {
				new ReadyConfigXML().saveNoLoginReadyInt(ReadyConfigXML.ENTRY_LG_WFB);
//				MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_LG_WFB);
			}

			SharePreferenceUtils.getInstance(this).setIsFirstTimeEnterGuideNext(false);
			 

			try {
				LoginManager.getInstance().logOut();
			} catch (Exception e) {
				e.printStackTrace();
			}
			//showProgressDialog(R.string.loading);
			new Thread(new Runnable() {
				@Override
				public void run() {
					LoginManager.getInstance().logInWithReadPermissions(NewGuideNextActivity.this,
							Arrays.asList("user_location", "user_birthday", "email", "user_friends"));
				}
			}).start();

			break;

		default:
			break;
		}
	}

	private void setProfile(){
		showProgressDialog(R.string.loading);
		OSInfo os = PalmchatApp.getApplication().osInfo;
		Profile profile = Profile.getCurrentProfile();
		mProfile = profile;
		if(null==profile){
			new Thread(new Runnable() {
				@Override
				public void run() {
					PalmchatLogUtils.i(TAG, "--FacebookCallback---profile==NULL----");	
					Profile.setFetchProfileCallback(mFetchProfileCallback);	
					Profile.fetchProfileForCurrentAccessToken();
				}
			}).start();
		}
		if((null!=os)&&(null!=profile)){
			String countryCode = PalmchatApp.getApplication().osInfo.getCountryCode();
			 
			facebookId = profile.getId();
			AfRegInfoParam param = new AfRegInfoParam();
			param.cc = countryCode;
			param.imei = PalmchatApp.getOsInfo().getImei();
			param.imsi = PalmchatApp.getOsInfo().getImsi();
			param.sex = Consts.AFMOBI_SEX_MALE;
			
			param.birth = DefaultValueConstant.BIRTHDAY;
			param.name = null;
			param.phone_or_email = profile.getId();
			param.password = Constants.FACEBOOK_PASSWORD;
			
			param.region = PalmchatApp.getOsInfo().getState(context);
			param.city = PalmchatApp.getOsInfo().getCity(context);
			param.country = PalmchatApp.getOsInfo().getCountry(context);
            param.fb_token = AccessToken.getCurrentAccessToken().getToken();
            mAfCorePalmchat.AfHttpRegLogin(param, facebookId, Consts.AF_LOGIN_FACEBOOK, Consts.AF_LOGIN_FACEBOOK, NewGuideNextActivity.this);
		}
	}


	@Override
	public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
		PalmchatLogUtils.e("NewGuideNextActivity", "----AfOnResult--Flag:" + flag + "---Code:" + code);
		//dismissAllDialog();
		if (code == Consts.REQ_CODE_SUCCESS) {
			switch (flag) {
			case Consts.REQ_FLAG_LOGIN:
				AfProfileInfo myProfile = (AfProfileInfo) result;

				if (myProfile != null) {
					double lat = 0d;
					double lon = 0d;
					PalmchatLogUtils.println("myProfile.lat:" + myProfile.lat + "  myProfile.lng:" + myProfile.lng);
					if (  !TextUtils.isEmpty(myProfile.lat) && myProfile.lat.trim().length() > 0) {
						lat =CommonUtils.stringToDouble(myProfile.lat);
					}
					if (!TextUtils.isEmpty(myProfile.lng) && myProfile.lat.trim().length() > 0) {
						lon =CommonUtils.stringToDouble(myProfile.lng);
					}
					SharePreferenceUtils.getInstance(context).setLatitudeAndLongitude(lat, lon);

					myProfile.password = Constants.FACEBOOK_PASSWORD;
					 
						/*
						 * myProfile.third_account = facebookId;
						 * toMainTab(Consts.AF_LOGIN_AFID);
						 */
						myProfile.password = Constants.FACEBOOK_PASSWORD;
						myProfile.third_account = facebookId;
						SharePreferenceUtils.getInstance(this).setIsFacebookLogin(true);
						CacheManager.getInstance().setMyProfile(myProfile);

						mAfCorePalmchat.AfDbProfileSaveOrUpdate(Consts.AFMOBI_DB_TYPE_PROFILE, myProfile);
						setLoadAccount(myProfile, Consts.AF_LOGIN_FACEBOOK, mAfCorePalmchat);//调中间件保存当前的登陆方式和信息
						
						// "1" was setted by server to distinguish whether third
						// login user registered or not.
						if (!"1,".equals(myProfile.attr2) || (TextUtils.isEmpty(myProfile.city) || TextUtils.isEmpty(myProfile.country) || TextUtils.isEmpty(myProfile.region) || "other".equals(myProfile.city) || DefaultValueConstant.OTHERS.equals(myProfile.city) || DefaultValueConstant.OTHERS.equals(myProfile.country) || DefaultValueConstant.OVERSEA.equals(myProfile.country) || DefaultValueConstant.OTHERS.equals(myProfile.region) || "other".equals(myProfile.region))) {
							Intent intent = new Intent(this, FacebookCompleteProfileActivity.class);
							intent.putExtra("FacebookId", facebookId);
							intent.putExtra("afid", myProfile.afId);
							intent.putExtra("SocialPerson",mProfile);
							if (user_data instanceof Byte) {
								byte thirdLoginType = (Byte) user_data;
								intent.putExtra("ThirdLoginType", thirdLoginType);
							}
							dismissAllDialog();
							startActivity(intent);
						} else {
							toMainTab(Consts.AF_LOGIN_FACEBOOK);
						}
					 
				 
					/*CacheManager.getInstance().setPalmGuessShow(myProfile.palmguess_flag == 1);
					 
					int ver = SharePreferenceUtils.getInstance(this).getPalmguessVersion(myProfile.afId);
					if (ver < myProfile.palmguess_version) {
						SharePreferenceUtils.getInstance(this).setPalmguessVersion(myProfile.afId, myProfile.palmguess_version);
						SharePreferenceUtils.getInstance(this).setUnReadNewPalmGuess(true);
					} */
				}
				break;

			default:
				dismissAllDialog();
				break;
			}
		} else { 
			dismissAllDialog();
			startActivity(new Intent(this, LoginActivity.class));
			finish();
			Consts.getInstance().showToast(this, code, flag, http_code);
		}
	}

 

	private void toMainTab(byte login_type) {
		dismissProgressDialog();
		Intent intent = new Intent(this, MainTab.class);
		intent.putExtra(JsonConstant.KEY_IS_LOGIN, true);
		intent.putExtra(JsonConstant.KeyPopMsg_NotAFriend, true);
		intent.putExtra(JsonConstant.KEY_LOGIN_TYPE, login_type);
		startActivity(intent);
		finish();
	}

	 

	private boolean hasThirdLoginClient(byte type) {

		String thirdPackageName;

		switch (type) {

		case Consts.AF_LOGIN_FACEBOOK:
			thirdPackageName = FacebookConstant.FACEBOOKPACKAGE;
			break;

		case Consts.AF_LOGIN_GOOGLE:
			thirdPackageName = "com.google.android.apps.plus";
			break;

		case Consts.AF_LOGIN_TWITTER:
			thirdPackageName = "com.twitter.android";
			break;

		default:
			thirdPackageName = "";
			break;
		}

		boolean value;
		try {
			getPackageManager().getApplicationInfo(thirdPackageName, PackageManager.GET_UNINSTALLED_PACKAGES);
			value = true;
		} catch (NameNotFoundException e) {
			value = false;
		}

		PalmchatLogUtils.println("--xxxx hasThirdLoginClient: type = " + type + " value = " + value);
		return value;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mCallbackManager.onActivityResult(requestCode, resultCode, data);
	}

}
